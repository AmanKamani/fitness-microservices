package com.fitness.recommendationservice.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fitness.recommendationservice.model.Activity;
import com.fitness.recommendationservice.model.Recommendation;
import com.fitness.recommendationservice.service.AIService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ActivityRecommendationService {
    private final AIService aiService;
    private final ObjectMapper mapper;

    public Mono<Recommendation> generateRecommendation(Activity activity) {
        String prompt = createPrompt(activity);
        return aiService.getAnswer(prompt)
                .doOnError(error -> {
                    log.error("Error Response from AI", error);
                })
                .map(answer -> createRecommendation(activity, answer));
    }

    private Recommendation createRecommendation(Activity activity, String aiResponse) {
        try {
            JsonNode analysisJson = extractRequiredJsonFromGeminiResponse(aiResponse);
            JsonNode analysisNode = analysisJson.path("analysis");

            StringBuilder completeAnalysis = new StringBuilder();
            addAnalysisSection(completeAnalysis, analysisNode, "overall", "Overall: ");
            addAnalysisSection(completeAnalysis, analysisNode, "pace", "Pace: ");
            addAnalysisSection(completeAnalysis, analysisNode, "heartRate", "HeartRate: ");
            addAnalysisSection(completeAnalysis, analysisNode, "caloriesBurned", "Calories: ");

            List<String> improvements = extractImprovements(analysisJson.path("improvements"));
            List<String> suggestions = extractSuggestions(analysisJson.path("suggestions"));
            List<String> safety = extractSafetyGuidelines(analysisJson.path("safety"));

            return Recommendation.builder()
                    .activityId(activity.getId())
                    .userId(activity.getUserId())
                    .activityType(activity.getType())
                    .recommendation(completeAnalysis.toString().trim())
                    .improvements(improvements)
                    .suggestions(suggestions)
                    .safety(safety)
                    .build();

        } catch (JsonProcessingException e) {
            log.error("Error parsing AI response", e);
            return createDefaultRecommendation(activity);
        }
    }

    private Recommendation createDefaultRecommendation(Activity activity) {
        return Recommendation.builder()
                .activityId(activity.getId())
                .userId(activity.getUserId())
                .activityType(activity.getType())
                .recommendation("Unable to generate detailed analysis")
                .improvements(Collections.singletonList("Continue with your current routine"))
                .suggestions(Collections.singletonList("Consider consulting a fitness professional"))
                .safety(Arrays.asList(
                        "Always warm up before exercise",
                        "Stay hydrated",
                        "Listen to your body"
                ))
                .build();
    }

    private JsonNode extractRequiredJsonFromGeminiResponse(String aiResponse) throws JsonProcessingException {
        JsonNode rootNode = mapper.readTree(aiResponse);
        String jsonContent = rootNode.path("candidates")
                .get(0)
                .path("content")
                .path("parts")
                .get(0)
                .path("text")
                .asText()
                .replaceAll("```json\\n", "")
                .replaceAll("\\n```", "")
                .trim();

        log.debug("Parsed from AI Response: [{}]", jsonContent);
        return mapper.readTree(jsonContent);
    }

    private List<String> extractSafetyGuidelines(JsonNode node) {
        if (!node.isArray()) {
            return List.of("Follow general safety guidelines");
        }

        List<String> safety = new ArrayList<>();
        node.forEach(item -> safety.add(item.asText()));

        return safety;
    }

    private List<String> extractSuggestions(JsonNode node) {
        if (!node.isArray()) {
            return List.of("No specific suggestions provided");
        }

        List<String> suggestions = new ArrayList<>();
        node.forEach(item -> {
            String workout = item.path("workout").asText();
            String description = item.path("description").asText();
            suggestions.add(String.format("%s: %s", workout, description));
        });

        return suggestions;
    }

    private List<String> extractImprovements(JsonNode node) {
        if (!node.isArray()) {
            return List.of("No specific improvements provided");
        }

        List<String> improvements = new ArrayList<>();
        node.forEach(item -> {
            String area = item.path("area").asText();
            String recommendation = item.path("recommendation").asText();
            improvements.add(String.format("%s: %s", area, recommendation));
        });

        return improvements;
    }

    private void addAnalysisSection(StringBuilder completeAnalysis, JsonNode analysisNode, String key, String prefix) {
        if (analysisNode.path(key).isMissingNode()) {
            return;
        }

        completeAnalysis.append(prefix)
                .append(analysisNode.path(key).asText())
                .append("\n\n");
    }

    private String createPrompt(Activity activity) {
        return String.format("""
                        Analyze this fitness activity and provide detailed recommendations in the following EXACT JSON format:
                        {
                          "analysis": {
                            "overall": "Overall analysis here",
                            "pace": "Pace analysis here",
                            "heartRate": "Heart rate analysis here",
                            "caloriesBurned": "Calories analysis here"
                          },
                          "improvements": [
                            {
                              "area": "Area name",
                              "recommendation": "Detailed recommendation"
                            }
                          ],
                          "suggestions": [
                            {
                              "workout": "Workout name",
                              "description": "Detailed workout description"
                            }
                          ],
                          "safety": [
                            "Safety point 1",
                            "Safety point 2"
                          ]
                        }
                        
                        Analyze this activity:
                        Activity Type: %s
                        Duration: %d minutes
                        Calories Burned: %d
                        Additional Metrics: %s
                        
                        Provide detailed analysis focusing on performance, improvements, next workout suggestions, and safety guidelines.
                        Ensure the response follows the EXACT JSON format shown above.
                        """,
                activity.getType(),
                activity.getDuration(),
                activity.getCaloriesBurned(),
                activity.getAdditionalMetrics()
        );
    }
}
