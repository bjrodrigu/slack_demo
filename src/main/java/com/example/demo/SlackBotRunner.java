package com.example.demo;

import com.example.demo.entities.Activity;
import com.slack.api.bolt.App;
import com.slack.api.bolt.socket_mode.SocketModeApp;
import com.slack.api.model.block.Blocks;
import com.slack.api.model.block.LayoutBlock;
import com.slack.api.model.block.composition.BlockCompositions;
import com.slack.api.model.block.element.BlockElements;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import com.slack.api.bolt.AppConfig;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Arrays;
import java.util.List;
import com.fasterxml.jackson.databind.ObjectMapper;

@Component
public class SlackBotRunner implements CommandLineRunner {

    @Override
    public void run(String[] args) throws Exception {

        String botToken = System.getenv("SLACK_BOT_TOKEN");
        String appToken = System.getenv("SLACK_APP_TOKEN");

        if (botToken == null || appToken == null) {
            throw new IllegalStateException("Missing SLACK_BOT_TOKEN or SLACK_APP_TOKEN environment variable.");
        }

        AppConfig config = new AppConfig();
        config.setSingleTeamBotToken(botToken);
        App app = new App(config);

        app.command("/hello", (req, ctx) -> {
            return ctx.ack("Hello, Slack!");
        });

        app.command("/test1", (req, ctx) -> {

            String localUrl = "http://localhost:8080/test";

            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(localUrl))
                    .build();

            try {
                HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
                String responseBody = response.body();
                return ctx.ack("Here’s the activity: " + responseBody);
            } catch (Exception e) {
                return ctx.ack("Error getting response: " + e.getMessage());
            }
        });

        app.command("/test2", (req, ctx) -> {
            Activity activity = getActivityFromApi();

            List<LayoutBlock> blocks = List.of(
                    Blocks.section(s -> s
                            .text(BlockCompositions
                                    .markdownText("Here’s the activity: *" + activity.getActivity() + "*"))),
                    Blocks.actions(a -> a.elements(Arrays.asList(
                            BlockElements.button(
                                    b -> b.text(BlockCompositions.plainText("Show Type")).actionId("show_type")),
                            BlockElements.button(
                                    b -> b.text(BlockCompositions.plainText("Show Price")).actionId("show_price")),
                            BlockElements.button(b -> b.text(BlockCompositions.plainText("Show Duration"))
                                    .actionId("show_duration"))))));

            return ctx.ack(r -> r.blocks(blocks));
        });

        app.blockAction("show_type", (req, ctx) -> {
            Activity activity = getActivityFromApi();
            var ackResponse = ctx.ack();
            ctx.respond(r -> r.responseType("ephemeral").text("Type: " + activity.getType()));
            return ackResponse;
        });

        app.blockAction("show_price", (req, ctx) -> {
            Activity activity = getActivityFromApi();
            var ackResponse = ctx.ack();
            ctx.respond(r -> r.responseType("ephemeral").text("Price: " + activity.getPrice()));
            return ackResponse;
        });

        app.blockAction("show_duration", (req, ctx) -> {
            Activity activity = getActivityFromApi();
            var ackResponse = ctx.ack();
            ctx.respond(r -> r.responseType("ephemeral").text("Duration: " + activity.getDuration()));
            return ackResponse;
        });

        SocketModeApp socketModeApp = new SocketModeApp(appToken, app);
        socketModeApp.start();
    }

    private Activity getActivityFromApi() {
        try {
            String url = "http://localhost:8080/test";
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .build();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            String responseBody = response.body();

            ObjectMapper mapper = new ObjectMapper();
            return mapper.readValue(responseBody, Activity.class);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
