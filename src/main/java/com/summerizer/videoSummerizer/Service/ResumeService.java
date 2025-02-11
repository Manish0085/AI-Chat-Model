package com.summerizer.videoSummerizer.Service;


import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

@Service
public class ResumeService {


    private ChatClient chatClient;
    public ResumeService(ChatClient.Builder builder){
        this.chatClient = builder.build();
    }

    public Map<String, Object> generateResumeResponse(String userResumeDescription) throws IOException {

        String promptString = this.loadPromptFromFile("resume_prompt.txt");
        String promptContent = this.putValuesToTemplate(promptString, Map.of(
                "userDescription", userResumeDescription
        ));

        Prompt prompt = new Prompt(promptContent);
        String response = chatClient.prompt(prompt).call().content();

        Map<String, Object> stringObjectMap = parseMultipleResponses(response);
        return stringObjectMap;
    }

    public String loadPromptFromFile(String fileName) throws IOException {
        Path path = new ClassPathResource(fileName).getFile().toPath();
        return Files.readString(path);
    }

    public String putValuesToTemplate(String template,
                                      Map<String, String> values){
        for (Map.Entry<String, String> entry: values.entrySet()){

            template = template.replace("{{" +entry.getKey()+ "}}", entry.getValue());

        }
        return template;
    }


    public static Map<String, Object> parseMultipleResponses(String response) {
        Map<String, Object> jsonResponse = new HashMap<>();

        try {
            ObjectMapper objectMapper = new ObjectMapper();

            // First, check if response is **pure JSON** and parse it directly
            if (response.trim().startsWith("{")) {
                jsonResponse = objectMapper.readValue(response, Map.class);
            } else {
                // If response is not directly JSON, extract `think` and `data`

                // Extract content inside <think> tags
                int thinkStart = response.indexOf("<think>") + 7;
                int thinkEnd = response.indexOf("</think>");
                if (thinkStart != -1 && thinkEnd != -1) {
                    String thinkContent = response.substring(thinkStart, thinkEnd).trim();
                    jsonResponse.put("think", thinkContent);
                } else {
                    jsonResponse.put("think", null); // Handle missing <think> tags
                }

                // Extract JSON content (without using ```json ... ``` markers)
                int jsonStart = response.indexOf("{"); // First occurrence of '{'
                int jsonEnd = response.lastIndexOf("}"); // Last occurrence of '}'

                if (jsonStart != -1 && jsonEnd != -1 && jsonStart < jsonEnd) {
                    String jsonContent = response.substring(jsonStart, jsonEnd + 1).trim();
                    try {
                        Map<String, Object> dataContent = objectMapper.readValue(jsonContent, Map.class);
                        jsonResponse.put("data", dataContent);
                    } catch (Exception e) {
                        jsonResponse.put("data", null); // Handle invalid JSON
                        System.err.println("❌ Invalid JSON format: " + e.getMessage());
                    }
                } else {
                    jsonResponse.put("data", null); // Handle missing JSON
                }
            }
        } catch (Exception e) {
            jsonResponse.put("think", null);
            jsonResponse.put("data", null);
            System.err.println("❌ Failed to parse response: " + e.getMessage());
        }

        System.out.println("✅ Parsed Response: " + jsonResponse);
        return jsonResponse;
    }
}
