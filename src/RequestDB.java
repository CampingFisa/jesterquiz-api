package src;

import com.fasterxml.jackson.core.type.TypeReference;
import entities.Category;
import entities.Question;
import entities.Theme;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;

public class RequestDB {

    private static final String CREATE_THEME_URL = "http://localhost:8080/api/themes";
    private static final String GET_THEMES_URL = "http://localhost:8080/api/themes";
    private static final String UPDATE_CATEGORY = "http://localhost:8080/api/categories";


    public static void insertTheme(Theme theme) {
        try {

            // Convertir le thème en JSON
            ObjectMapper objectMapper = new ObjectMapper();
            String requestBody = objectMapper.writeValueAsString(theme);

            // Créer un client HTTP
            HttpClient client = HttpClient.newHttpClient();

            // Créer une requête POST
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(CREATE_THEME_URL))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                    .build();

            // Envoyer la requête et obtenir la réponse
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            // Vérifier le code de réponse
            if (response.statusCode() == 201 || response.statusCode() == 200) {
                System.out.println("Entities.Theme created successfully: " + response.body());
            } else {
                System.err.println("Failed to create theme. HTTP Status: " + response.statusCode());
                System.err.println("Response body: " + response.body());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static List<Theme> getThemes() {

        HttpClient client = HttpClient.newHttpClient();

        // Créer une requête HTTP GET
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(GET_THEMES_URL + "?isMain=true"))
                .header("Content-Type", "application/json")
                .GET()
                .build();

        List<Theme> themes = new ArrayList<>();

        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() == 200) {
                ObjectMapper objectMapper = new ObjectMapper();
                themes = objectMapper.readValue(response.body(), new TypeReference<>() {});

            } else {
                System.err.println("Failed to create theme. HTTP Status: " + response.statusCode());
                System.err.println("Response body: " + response.body());
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return themes;

    }

    public static Theme getTheme(int themeId) {

        HttpClient client = HttpClient.newHttpClient();

        // Créer une requête HTTP GET
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(GET_THEMES_URL + "/" + themeId + "?isMain=true"))
                .header("Content-Type", "application/json")
                .GET()
                .build();

        Theme theme = new Theme();

        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() == 200) {
                ObjectMapper objectMapper = new ObjectMapper();
                theme = objectMapper.readValue(response.body(), new TypeReference<>() {});

            } else {
                System.err.println("Failed to create theme. HTTP Status: " + response.statusCode());
                System.err.println("Response body: " + response.body());
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return theme;

    }

    public static void updateCategory(Category category) {
        try {

            // Convertir le thème en JSON
            ObjectMapper objectMapper = new ObjectMapper();
            String requestBody = objectMapper.writeValueAsString(category);

            // Créer un client HTTP
            HttpClient client = HttpClient.newHttpClient();

            // Créer une requête POST
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(UPDATE_CATEGORY + "/" + category.getCategoryId()))
                    .header("Content-Type", "application/json")
                    .PUT(HttpRequest.BodyPublishers.ofString(requestBody))
                    .build();

            // Envoyer la requête et obtenir la réponse
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            // Vérifier le code de réponse
            if (response.statusCode() == 201 || response.statusCode() == 200) {
                System.out.println("Entities.Theme created successfully: " + response.body());
            } else {
                System.err.println("Failed to create theme. HTTP Status: " + response.statusCode());
                System.err.println("Response body: " + response.body());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
