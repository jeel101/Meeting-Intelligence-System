package com.hintro.meeting_intelligence.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hintro.meeting_intelligence.dto.ActionItemDto;
import com.hintro.meeting_intelligence.dto.groq.GroqRequestDto;
import com.hintro.meeting_intelligence.dto.groq.MessageDto;
import com.hintro.meeting_intelligence.dto.response.AnalysisResponseDto;
import com.hintro.meeting_intelligence.entity.*;
import com.hintro.meeting_intelligence.exception.AppException;
import com.hintro.meeting_intelligence.repository.ActionItemRepository;
import com.hintro.meeting_intelligence.repository.AnalysisRepository;
import com.hintro.meeting_intelligence.repository.MeetingRepository;
import com.hintro.meeting_intelligence.repository.TranscriptRepository;
import com.hintro.meeting_intelligence.service.AnalysisService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class AnalysisServiceImpl implements AnalysisService {
    private final MeetingRepository meetingRepository;
    private final TranscriptRepository transcriptRepository;
    private final AnalysisRepository meetingAnalysisRepository;
    private final ActionItemRepository actionItemRepository;
    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;

    @Value("${groq.api.key}")
    private String groqApiKey;

    @Value("${groq.url}")
    private String groqUrl;

    @Override
    public AnalysisResponseDto analyzeMeeting(Long meetingId) {
        String today = LocalDate.now().toString();
        Meeting meeting = meetingRepository.findById(meetingId)
                        .orElseThrow(() ->AppException.notFound("Meeting not found"));

        List<Transcript> transcripts = transcriptRepository.findByMeetingIdOrderByIdAsc(meetingId);

        //convert transcripts to prompt
        String transcriptText = transcripts.stream().map(
                                t ->"[" + t.getTimestamp() + "] "
                                        + t.getSpeaker()
                                        + ": "
                                        + t.getText())
                        .collect(Collectors.joining("\n"));

        //build prompt
        String prompt = """
            You are a meeting analysis assistant.  
            Today's date is %s.
            Extract insights STRICTLY from the transcript provided.    
            Return ONLY VALID JSON — no explanation, no markdown, no preamble.         
            {
              "summary":[
                      {
                        "text":[],
                        "citations":[
                          {
                            "timestamp":"00:10"
                          }
                        ]
                      }
                    ],
                    "decisions":[
                      {
                        "text":[],
                        "citations":[
                          {
                            "timestamp":"00:10"
                          }
                        ]
                      }
                    ],
                    "followUps":[
                      {
                        "text": [],
                        "citations":[
                          {
                            "timestamp":"00:10"
                          }
                        ]
                      }
                    ],
                      "actionItems":[
                         {
                            "task":"...",
                            "assignee":"...",
                            "dueDate":"2026-06-15",
                            "citations":[
                               {
                                 "timestamp":"00:10"
                               }
                            ]
                         }
                    ]
             } 
            Rules:
            1. NEVER invent attendees not mentioned in the transcript
            2. NEVER invent action items not explicitly stated
            3. NEVER invent decisions not explicitly made
            4. NEVER add information absent from the transcript
            5. Use only transcript information.
            6. Do not hallucinate.
            7. Every insight MUST have at least one citation with exact timestamp from transcript  
            8. If something is unclear, omit it — do not guess    
            9. If no assignee is explicitly mentioned in the transcript, return assignee as null
            10. If a due date is mentioned (e.g. "by Monday", "15th June", "end of week"), 
            convert it to YYYY-MM-DD format using today's date as reference. If no due date is mentioned, return null.
            11. summary is ALWAYS required — every transcript has something worth summarizing.
            Write at least 1-3 concise bullet points describing what was discussed, even if no decisions or action items were made
            12. Every summary, decision, follow-up and action item MUST contain at least one citation.
            13. Do not generate any insight without a citation.      
            Transcript:            
            """.formatted(today) + transcriptText;

        GroqRequestDto request = GroqRequestDto.builder()
                        .model("llama-3.3-70b-versatile")
                        .messages(List.of(new MessageDto("user",prompt)))
                        .build();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(groqApiKey);

        ResponseEntity<String> response = restTemplate.exchange(
                        groqUrl,
                        HttpMethod.POST,
                        new HttpEntity<>(request, headers),
                        String.class
                );

        try {
            JsonNode root = objectMapper.readTree(response.getBody());

            String content = root.path("choices")
                    .get(0)
                    .path("message")
                    .path("content")
                    .asText();


            System.out.println(content);

            content = content.replaceAll("(?s)```json\\s*", "");
            content = content.replaceAll("(?s)```\\s*", "");
            content = content.trim();

            AnalysisResponseDto analysis = objectMapper.readValue(content, AnalysisResponseDto.class);

            if(meetingAnalysisRepository.existsByMeetingId(meetingId)) {
                throw AppException.conflict("Meeting already analyzed");
            }

            saveMeetingAnalysis(meeting, content);
            saveActionItems(meeting, analysis);

            return analysis;
        }catch (JsonProcessingException e) {
            throw AppException.badRequest("Failed to parse AI response");
        }
    }

    private void saveMeetingAnalysis(Meeting meeting, String content) {
        MeetingAnalysis meetingAnalysis = new MeetingAnalysis();
        meetingAnalysis.setMeeting(meeting);
        meetingAnalysis.setAnalysisJson(content);
        meetingAnalysis.setGeneratedAt( LocalDateTime.now() );

        meetingAnalysisRepository.save(meetingAnalysis);
    }
    private void saveActionItems(Meeting meeting, AnalysisResponseDto analysis) throws JsonProcessingException {
        for(ActionItemDto actionItemDto: analysis.getActionItems()) {

            ActionItem actionItem = new ActionItem();
            actionItem.setTask(actionItemDto.getTask());
            actionItem.setAssignee(actionItemDto.getAssignee());
            actionItem.setStatus( ActionItemStatus.PENDING);
            actionItem.setMeeting(meeting);
            actionItem.setCitationsJson(objectMapper.writeValueAsString(actionItemDto.getCitations()));

            // ← ADD HERE
            if (actionItemDto.getDueDate() != null) {
                actionItem.setDueDate(actionItemDto.getDueDate());
            }

            actionItemRepository.save(actionItem);
        }
    }
}
