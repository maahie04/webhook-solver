package com.maahie.webhook_solver.dto;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Map;

public class WebhookResponse {
    private String webhookUrl;
    private String accessToken;
    private String problem;
    private Map<String, Object> data; // For flexible response handling

    // Getters and setters with JSON property annotations
    @JsonProperty("webhook")
    public String getWebhookUrl() { return webhookUrl; }
    public void setWebhookUrl(String webhookUrl) { this.webhookUrl = webhookUrl; }

    @JsonProperty("accessToken")
    public String getAccessToken() { return accessToken; }
    public void setAccessToken(String accessToken) { this.accessToken = accessToken; }

    @JsonProperty("problem")
    public String getProblem() { return problem; }
    public void setProblem(String problem){this.problem=problem;}
    public Map<String, Object> getData() { return data; }
    public void setData(Map<String, Object> data) { this.data = data; }
}
