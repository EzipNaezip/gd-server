package com.manofsteel.gd.service;

import com.google.gson.Gson;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import okhttp3.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
public class ChatGPTClientService {


    // API endpoint URL and API key
    @Value("${openai.api.url}")
    private static String API_URL;

    // Gson object for converting JSON strings to Java objects
    private static final Gson gson = new Gson();

    private static final String initialPrompted ="You are now a DALLE-2 prompt generator tool that will generate prompts suitable for recommending interior projections based on the interior descriptions I will give , generate a prompt that gives the DALLE-2 AI text to generate a picture model, please narrate in English and need to maintain a real photo style. The prompt I give will be given to you with “” around the text and , the prompt must not exceed 200 tokens.If it's not an interior related question, ask me to enter it again";
           // Media type and HTTP client for making API requests
    private static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    private static final OkHttpClient client = new OkHttpClient.Builder()
            .readTimeout(60, TimeUnit.SECONDS)
            .writeTimeout(60, TimeUnit.SECONDS)
            .connectTimeout(60, TimeUnit.SECONDS)
            .build();
    private Call currentApiCall; // 현재 실행 중인 API 호출 객체

    // Method for generating a response using Chat GPT API
    public String generateResponse(String input, String apiKey) {
        try {

            // Set the previousMessage variable to input
            List<RequestBodyParams.Message> conversationHistory = new ArrayList<>();
                input= input +"\n\n위의 문장에 대해 물체의 크기와 위치에 대한 묘사, 물체의 색상, 모양, 재질, 패턴등의 속성에 대한 묘사를 추가해서 방의 모습을 글 형태로 90자 이내로 작성해줘";
                conversationHistory.add(new RequestBodyParams.Message("user", "인테리어 디자인을 하기 위해 도움을 받고 싶어 dalle에게 프롬프트로 전달하기 위해 영어로 작성해줘 만약 인테리어와 관련된 질문이 아니라면 please enter it again이라고 입력해줘"));
                conversationHistory.add(new RequestBodyParams.Message("user", input));


            RequestBody requestBody = RequestBody.create(
                    gson.toJson(new RequestBodyParams(conversationHistory, "gpt-3.5-turbo")),
                    JSON
            );

            Request request = new Request.Builder()
                    .url("https://api.openai.com/v1/chat/completions")
                    .addHeader("Authorization", "Bearer " + apiKey)
                    .addHeader("Content-Type", "application/json")
                    .post(requestBody)
                    .build();

            try (Response response = client.newCall(request).execute()) {
                String responseBody = response.body().string();
                System.out.println("responsebody:" + responseBody);
                ObjectMapper mapper = new ObjectMapper();
                JsonNode responseNode = mapper.readTree(responseBody);
                JsonNode choicesNode = responseNode.get("choices");

                if (choicesNode != null && choicesNode.isArray() && choicesNode.size() > 0) {
                    JsonNode choiceNode = choicesNode.get(0);
                    JsonNode messageNode = choiceNode.get("message");
                    return messageNode.get("content").asText();
                }
            }
        } catch (SocketTimeoutException e) {
            e.printStackTrace();
            return "Oops! Looks like we've got a timeout issue. Please try again later.";
        } catch (Exception e) {
            e.printStackTrace();
            return "Error generating response: " + e.getMessage();
        }
        return "";
    }

    public void cancelRequest() {
        if (currentApiCall != null) {
            currentApiCall.cancel(); // API 호출 취소

            // 사용자에게 중단되었음을 알리는 메시지를 생성합니다.
            String response = "요청이 중단되었습니다.";

            // 생성된 응답을 사용자에게 제공합니다.
            // 예: 콘솔에 출력하거나 API 응답으로 반환
            System.out.println(response);
        }
    }

    // Request body parameters class with input prompt and API parameters
    private static class RequestBodyParams {
        List<Message> messages;
        Double temperature;
        Integer max_tokens;
        Double top_p;
        String model;

        // Constructor for initializing request parameters with conversation history
        public RequestBodyParams(List<Message> conversationHistory, String model) {
            this.messages = conversationHistory;
            this.temperature = 0.7;
            this.max_tokens = 400;
            this.top_p = 1.0;
            this.model = model;
        }

        // Message class for storing the message and its role
        private static class Message {
            String role;
            String content;

            public Message(String role, String content) {
                this.role = role;
                this.content = content;
            }
        }
    }

}