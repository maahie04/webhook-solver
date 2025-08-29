package com.maahie.webhook_solver.service;
import com.maahie.webhook_solver.dto.SolutionRequest;
import com.maahie.webhook_solver.dto.WebhookRequest;
import com.maahie.webhook_solver.dto.WebhookResponse;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import java.util.Map;
import org.springframework.web.client.HttpClientErrorException; // Add this import
import org.springframework.web.client.RestTemplate;

@Service
public class WebhookService {
    private final RestTemplate restTemplate;
    private final JwtService jwtService;

    public WebhookService(RestTemplate restTemplate, JwtService jwtService) {
        this.restTemplate = restTemplate;
        this.jwtService = jwtService;
    }

    public WebhookResponse generateWebhook() {
        String url = "https://bfhldevapigw.healthrx.co.in/hiring/generateWebhook/JAVA";

        WebhookRequest request = new WebhookRequest(
                "John Doe",
                "REG12347",
                "john@example.com"
        );

        try {
            ResponseEntity<Map> response = restTemplate.postForEntity(
                    url, request, Map.class
            );

            WebhookResponse webhookResponse = new WebhookResponse();

            if (response.getBody() != null) {
                Map<String, Object> responseBody = response.getBody();

                // Extract values with null checks
                webhookResponse.setWebhookUrl((String) responseBody.get("webhook"));
                webhookResponse.setAccessToken((String) responseBody.get("accessToken"));
                webhookResponse.setProblem((String) responseBody.get("problem"));
                webhookResponse.setData(responseBody);

                // Debug logging
                System.out.println("Full API Response: " + responseBody);
            }

            return webhookResponse;

        } catch (Exception e) {
            System.err.println("Error generating webhook: " + e.getMessage());
            return null;
        }
    }
    public void submitSolution(String webhookUrl, String accessToken, String sqlQuery) {
        debugJwtToken(accessToken);
        if (webhookUrl == null || webhookUrl.isEmpty()) {
            System.err.println("Cannot submit solution: Webhook URL is null or empty");
            return;
        }

        try {
            SolutionRequest solution = new SolutionRequest(sqlQuery);
            HttpHeaders headers = jwtService.createAuthHeaders(accessToken);
            HttpEntity<SolutionRequest> entity = new HttpEntity<>(solution, headers);

            System.out.println("=== SUBMISSION DEBUG INFO ===");
            System.out.println("URL: " + webhookUrl);
            System.out.println("Authorization Header: " + headers.getFirst("Authorization"));
            System.out.println("SQL Query: " + sqlQuery);
            System.out.println("Request Headers: " + headers);
            System.out.println("Request Body: " + solution.getFinalQuery());
            System.out.println("============================");

            ResponseEntity<String> response = restTemplate.exchange(
                    webhookUrl, HttpMethod.POST, entity, String.class
            );

            System.out.println("✅ Submission Successful!");
            System.out.println("Response Status: " + response.getStatusCode());
            System.out.println("Response Body: " + response.getBody());

        } catch (HttpClientErrorException e) {
            System.err.println("❌ HTTP Error: " + e.getStatusCode());
            System.err.println("Error Response Body: " + e.getResponseBodyAsString());
            System.err.println("Error Headers: " + e.getResponseHeaders());
            System.err.println("Full error: " + e.getMessage());
            e.printStackTrace();
        } catch (Exception e) {
            System.err.println("❌ General Error: " + e.getMessage());
            e.printStackTrace();
        }
    }
    public String solveSqlProblem(String problemDescription, String registrationNumber) {
        // Extract last two digits of registration number
        String lastTwoDigits = registrationNumber.substring(registrationNumber.length() - 2);
        int lastDigits = Integer.parseInt(lastTwoDigits);

        // Determine if odd or even
        boolean isOdd = (lastDigits % 2) == 1;

        // Based on the problem description and odd/even, solve the SQL problem
        // This is a placeholder - you'll need to implement the actual SQL problem solving
        if (isOdd) {
            return solveOddProblem(problemDescription);
        } else {
            return solveEvenProblem(problemDescription);
        }
    }
    private String solveOddProblem(String problemDescription) {
        // Implement logic for odd registration number problems
        // This is a sample solution - you need to analyze the actual problem
        return "SELECT * FROM patients WHERE condition = 'critical';";
    }

    private String solveEvenProblem(String problemDescription) {
        // Implement logic for even registration number problems
        // This is a sample solution - you need to analyze the actual problem
        return "SELECT name, age FROM users WHERE status = 'active';";
    }
    // Add this method to debug the JWT token
    private void debugJwtToken(String token) {
        try {
            String[] parts = token.split("\\.");
            if (parts.length == 3) {
                String header = new String(java.util.Base64.getUrlDecoder().decode(parts[0]));
                String payload = new String(java.util.Base64.getUrlDecoder().decode(parts[1]));

                System.out.println("=== JWT TOKEN DEBUG ===");
                System.out.println("Header: " + header);
                System.out.println("Payload: " + payload);
                System.out.println("=======================");
            }
        } catch (Exception e) {
            System.err.println("Failed to decode JWT: " + e.getMessage());
        }
    }
}
