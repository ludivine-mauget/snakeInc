package org.snakeinc.snake.api;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializer;
import org.apache.hc.client5.http.classic.methods.HttpGet;
import org.apache.hc.client5.http.classic.methods.HttpPost;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.CloseableHttpResponse;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.core5.http.io.entity.EntityUtils;
import org.apache.hc.core5.http.io.entity.StringEntity;
import org.apache.hc.core5.http.ParseException;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class ApiClient {
    private static final String BASE_URL = "http://localhost:8080/api/v1";
    private final CloseableHttpClient httpClient;
    private final Gson gson;

    public ApiClient() {
        this.httpClient = HttpClients.createDefault();
        this.gson = new GsonBuilder()
                .registerTypeAdapter(LocalDateTime.class, (JsonSerializer<LocalDateTime>)
                        (src, typeOfSrc, context) -> new JsonPrimitive(src.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)))
                .registerTypeAdapter(LocalDateTime.class, (JsonDeserializer<LocalDateTime>)
                        (json, typeOfT, context) -> LocalDateTime.parse(json.getAsString(), DateTimeFormatter.ISO_LOCAL_DATE_TIME))
                .registerTypeAdapter(LocalDate.class, (JsonSerializer<LocalDate>)
                        (src, typeOfSrc, context) -> new JsonPrimitive(src.format(DateTimeFormatter.ISO_LOCAL_DATE)))
                .registerTypeAdapter(LocalDate.class, (JsonDeserializer<LocalDate>)
                        (json, typeOfT, context) -> LocalDate.parse(json.getAsString(), DateTimeFormatter.ISO_LOCAL_DATE))
                .create();
    }

    public PlayerListResponse getPlayers() throws IOException {
        HttpGet request = new HttpGet(BASE_URL + "/players");
        try (CloseableHttpResponse response = httpClient.execute(request)) {
            String json = EntityUtils.toString(response.getEntity(), StandardCharsets.UTF_8);
            return gson.fromJson(json, PlayerListResponse.class);
        } catch (ParseException e) {
            throw new IOException("Failed to parse response", e);
        }
    }

    public PlayerResponse createPlayer(String name, int age) throws IOException {
        HttpPost request = new HttpPost(BASE_URL + "/players");
        request.setHeader("Content-Type", "application/json");

        PlayerRequest playerRequest = new PlayerRequest(name, age);
        String json = gson.toJson(playerRequest);
        request.setEntity(new StringEntity(json, StandardCharsets.UTF_8));

        try (CloseableHttpResponse response = httpClient.execute(request)) {
            String responseJson = EntityUtils.toString(response.getEntity(), StandardCharsets.UTF_8);
            return gson.fromJson(responseJson, PlayerResponse.class);
        } catch (ParseException e) {
            throw new IOException("Error parsing response", e);
        }
    }

    public void createScore(String snake, int score, Integer playerId) throws IOException {
        HttpPost request = new HttpPost(BASE_URL + "/scores");
        request.setHeader("Content-Type", "application/json");

        ScoreRequest scoreRequest = new ScoreRequest(snake, score, LocalDateTime.now(), playerId);
        String json = gson.toJson(scoreRequest);
        request.setEntity(new StringEntity(json, StandardCharsets.UTF_8));

        try (CloseableHttpResponse response = httpClient.execute(request)) {
            String responseJson = EntityUtils.toString(response.getEntity(), StandardCharsets.UTF_8);
            gson.fromJson(responseJson, ScoreResponse.class);
        } catch (ParseException e) {
            throw new IOException("Failed to parse response", e);
        }
    }

    public ScoreListResponse getScores(String snake, Integer playerId) throws IOException {
        StringBuilder url = new StringBuilder(BASE_URL + "/scores?");
        if (snake != null) {
            url.append("snake=").append(snake).append("&");
        }
        if (playerId != null) {
            url.append("player=").append(playerId);
        }

        HttpGet request = new HttpGet(url.toString());
        try (CloseableHttpResponse response = httpClient.execute(request)) {
            String json = EntityUtils.toString(response.getEntity(), StandardCharsets.UTF_8);
            return gson.fromJson(json, ScoreListResponse.class);
        } catch (ParseException e) {
            throw new IOException("Error parsing response", e);
        }
    }

    public void close() throws IOException {
        httpClient.close();
    }
}
