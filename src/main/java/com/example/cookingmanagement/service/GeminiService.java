package com.example.cookingmanagement.service;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.node.ArrayNode;

@Service
public class GeminiService {

    @Value ("${gemini.api.key}")
    private String geminiApiKey;

    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;

    public GeminiService() {
        this.restTemplate = new RestTemplate();
        this.objectMapper = new ObjectMapper();
    }

    /**
     * テキストをGemini APIを使って優しい解説モードに変換する
     * @param originalText 変換したい元のテキスト
     * @return 優しい解説モードに変換されたテキスト、またはエラーの場合は元のテキスト
     */
    public String getGentleExplanation(String originalText) {
        if (originalText == null || originalText.trim().isEmpty()) {
            return originalText;
        }

        String apiUrl = "https://generativelanguage.googleapis.com/v1/models/gemini-1.5-flash-002:generateContent?key=" + geminiApiKey;




        try {
            // リクエストボディの構築
            ObjectNode rootNode = objectMapper.createObjectNode();
            ArrayNode contentsNode = rootNode.putArray("contents");
            ObjectNode contentItemNode = contentsNode.addObject();
            ArrayNode partsNode = contentItemNode.putArray("parts");
            ObjectNode partNode = partsNode.addObject();
            partNode.put("text", "以下のテキストを、料理初心者にも分かりやすいように、専門用語を避け、親しみやすい言葉で解説してください。重要なポイントがあれば補足してください。\n\n" + originalText);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            HttpEntity<String> request = new HttpEntity<>(rootNode.toString(), headers);

            JsonNode response = restTemplate.postForObject(apiUrl, request, JsonNode.class);

            if (response != null && response.has("candidates")) {
                JsonNode candidates = response.path("candidates");
                if (candidates.isArray() && candidates.size() > 0) {
                    JsonNode content = candidates.get(0).path("content");
                    if (content.has("parts")) {
                        JsonNode parts = content.path("parts");
                        if (parts.isArray() && parts.size() > 0) {
                            return parts.get(0).path("text").asText();
                        }
                    }
                }
            }
        } catch (Exception e) {
            System.err.println("Error calling Gemini API: " + e.getMessage());
            e.printStackTrace();
        }
        return originalText; // エラーの場合は元のテキストを返す
    }
}
