package com.maahie.webhook_solver.component;
import com.maahie.webhook_solver.dto.WebhookResponse;
import com.maahie.webhook_solver.service.WebhookService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class StartupRunner implements CommandLineRunner{
    private final WebhookService webhookService;

    public StartupRunner(WebhookService webhookService) {
        this.webhookService = webhookService;
    }

    @Override
    public void run(String... args) throws Exception {
        System.out.println("Application started. Generating webhook...");

        // Step 1: Generate webhook
        WebhookResponse response = webhookService.generateWebhook();

        if (response != null && response.getAccessToken() != null) {
            System.out.println("Webhook URL: " + response.getWebhookUrl());
            System.out.println("Access Token: " + response.getAccessToken());
            System.out.println("Problem: " + response.getProblem());

            // Only proceed if we have a valid webhook URL
            if (response.getWebhookUrl() != null && !response.getWebhookUrl().isEmpty()) {
                // Step 2: Solve SQL problem - you need to implement this based on actual problem
                String sqlQuery = webhookService.solveSqlProblem(
                        response.getProblem(),
                        "REG12347"
                );

                System.out.println("Solved SQL Query: " + sqlQuery);

                // Step 3: Submit solution
                webhookService.submitSolution(
                        response.getWebhookUrl(),
                        response.getAccessToken(),
                        sqlQuery
                );
            } else {
                System.out.println("No webhook URL received. Cannot submit solution.");
                System.out.println("Full response data: " + response.getData());
            }
        } else {
            System.out.println("Failed to generate webhook or no access token received");
        }
    }
}
