package src;

import entities.Category;
import entities.Theme;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import io.github.cdimascio.dotenv.Dotenv;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class RequestGpt {

    private static final Dotenv dotenv = Dotenv.load();
    private static final String API_KEY = dotenv.get("OPENAI_API_KEY");
    private static final String API_URL = dotenv.get("OPENAI_API_URL");

    int nbTheme;
    int nbCat;
    int poidsMinimumSousCat;
    int nbSousCat;
    int poidsMinimumSousSousCat;
    int nbSousSousCat;
    int nbQuestions;

    int nbRequest = 1;

    public RequestGpt(int nbTheme, int nbCat, int poidsMinimumSousCat, int nbSousCat, int poidsMinimumSousSousCat, int nbSousSousCat, int nbQuestions) {
        this.nbTheme = nbTheme;
        this.nbCat = nbCat;
        this.poidsMinimumSousCat = poidsMinimumSousCat;
        this.nbSousCat = nbSousCat;
        this.poidsMinimumSousSousCat = poidsMinimumSousSousCat;
        this.nbSousSousCat = nbSousSousCat;
        this.nbQuestions = nbQuestions;
    }

    public String getGptAnswer(String text) throws IOException, InterruptedException {
        System.out.println("Requete " + nbRequest);
        nbRequest++;
        HttpClient client = HttpClient.newHttpClient();

        JsonObject messageObject = new JsonObject();
        messageObject.addProperty("role", "user");
        messageObject.addProperty("content", text);

        JsonArray messagesArray = new JsonArray();
        messagesArray.add(messageObject);

        JsonObject requestBody = new JsonObject();
        requestBody.addProperty("model", "gpt-3.5-turbo");
        requestBody.add("messages", messagesArray);

        String body = new Gson().toJson(requestBody);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(API_URL))
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer " + API_KEY)
                .POST(HttpRequest.BodyPublishers.ofString(body))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        String messageOpenAi = response.body();
        Gson gson = new Gson();

        String content = "";

        // Assurez-vous que le contenu de la réponse est bien interprété comme un objet JSON
        try {
            JsonObject jsonResponse = gson.fromJson(messageOpenAi, JsonObject.class);

            if (jsonResponse.has("choices")) {
                content = jsonResponse.getAsJsonArray("choices")
                        .get(0).getAsJsonObject().get("message")
                        .getAsJsonObject().get("content").getAsString();
                return content;
            } else {
                content = "error";
                System.out.println("Error in response: " + jsonResponse.toString());
            }
        } catch (Exception e) {
            System.out.println("Error in response: " + e);
        }


        return content;
    }

    public String getThemes() throws IOException, InterruptedException {

        String texteThemes = String.format(
                "Dans l'objectif de se divertir, j'aimerais créer des quizz sur de nombreuses catégories. "
                        + "Je veux que ta réponse soit un fichier json de ce format et rien d'autre: "
                        + "[{\"theme\": \"Histoire\", \"description\": \"Comprend les événements historiques, les personnages célèbres, les guerres, les civilisations anciennes, etc.\"}, {...}] "
                        + "%d thèmes très larges qui permettent d'englober la quasi totalité des sujets que l'on pourrait aborder "
                        + "dans ces quizzs.", nbTheme);

        return this.getGptAnswer(texteThemes);
    }

    public String getSubThemes(Theme theme) throws IOException, InterruptedException {

        String texteSousThemes = String.format(
                "Dans l'objectif de se divertir, j'aimerais créer des quizz sur de nombreuses catégories déclinées en sous-catégories. Donne moi %d sous-catégories du thème suivant: %s.\n" +
                        "Il faut que les sous-catégories soient assez larges pour permettre d'englober la quasi totalité des sujets concernant le thème tout en restant bien spécifique au thème.\n" +
                        "Par exemple si le thème est football, il ne \n" +
                        "faut pas que la sous-catégorie soit Joueurs célèbres mais plutôt Joueurs de football célèbres" +
                        "Il faut que le nom de la sous-catégorie soit bien spécifique au thème donné.\n" +
                        "Je veux aussi que tu pondères chaque catégorie (entre 1 et 10) de manière arbitraire en rapport avec l'intérêt global que porte un jeune français lambda pour la sous-catégorie et de la pertinence à la découper de nouveau en sous-catégories\n" +
                        "Je veux que ta réponse soit un fichier json suivant strictement ce format et rien d'autre: \n" +
                        "[{\"sous-catégorie\": \"Révolution française\", \"poids\": 7}, {...}] ", nbCat, theme.getThemeName());

        return this.getGptAnswer(texteSousThemes);
    }

    public String getSubSubThemes(Theme theme) throws IOException, InterruptedException {

        String texteSousThemes = String.format(
                "Dans l'objectif de se divertir, j'aimerais créer des quizz sur de nombreuses catégories déclinées en sous-catégories. Donne moi %d sous-catégories du thème suivant: %s. " +
                        "Il faut que les sous-catégories soient assez larges pour permettre d'englober la quasi totalité des sujets concernant le thème tout en restant bien spécifique au thème. " +
                        "Il faut que le nom de la sous-catégorie soit bien spécifique au thème donné.Par exemple si le thème est football, il ne \n" +
                        "faut pas que la sous-catégorie soit Joueurs célèbres mais plutôt Joueurs de football célèbres\n" +
                        "Je veux aussi que tu pondères chaque catégorie (entre 1 et 10) de manière arbitraire en rapport avec l'intérêt global que porte un jeune français lambda pour la sous-catégorie et de la pertinence à la découper de nouveau en sous-catégories\n" +
                        "Je veux que ta réponse soit un fichier json suivant strictement ce format et rien d'autre: \n" +
                        "[{\"sous-catégorie\": \"Révolution française\", \"poids\": 7}, {...}] ", nbSousCat, theme.getThemeName());

        return this.getGptAnswer(texteSousThemes);
    }

    public String getFinalCategories(Theme subSubTheme) throws IOException, InterruptedException {
        String texteSousThemes = String.format(
                "Dans l'objectif de se divertir, j'aimerais créer des quizz sur de nombreuses catégories déclinées en sous-catégories. Donne moi %d sous-catégories du thème suivant: %s. " +
                        "Il faut que les sous-catégories soient assez larges pour permettre d'englober la quasi totalité des sujets concernant le thème tout en restant bien spécifique au thème. " +
                        "Il faut que le nom de la sous-catégorie soit bien spécifique au thème donné. Par exemple si le thème est football, il ne " +
                        "faut pas que la sous-catégorie soit Joueurs célèbres mais plutôt Joueurs de football célèbres\n" +
                        " Je veux que ta réponse soit un fichier json suivant strictement ce format et rien d'autre:\n" +
                        "\"[{\"sous-catégorie\": \"Les compétitions majeurs de football\", \"description\": \"comprend les compétitions nationales et internationales majeurs de football comme les grands championnats européen, la coupe du monde, etc...\"}, {...}] ", nbSousSousCat, subSubTheme.getThemeName());

        return this.getGptAnswer(texteSousThemes);
    }

    public String getQuestions(Category category) throws IOException, InterruptedException {
        String texteSousThemes = String.format(
                "Dans l'objectif de se divertir, j'aimerais créer des quizz sur de nombreuses catégories comportant chacune des questions.\n" +
                        "Ecris mois %d questions différentes sous formes de qcm sur la catégorie suivante: %s\n" +
                        "Je veux que ta réponse soit un fichier json de ce format et rien d'autre: \n" +
                        "[{\"question\": \"Quel pays a remporté le plus de coupe du monde de football?\", \"correcte\": \"Brésil\", \"A\": \"Allemagne\", \"B\": \"France\", \"C\": \"Italie\", \"D\": \"Argentine\"}, {...}] ", nbQuestions, category.getThemeName());

        return this.getGptAnswer(texteSousThemes);
    }
}
