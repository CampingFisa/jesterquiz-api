import io.github.cdimascio.dotenv.Dotenv;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpRequest.BodyPublishers;
import java.net.http.HttpResponse.BodyHandlers;

public class Main {

    private static final Dotenv dotenv = Dotenv.load();
    private static final String API_KEY = dotenv.get("OPENAI_API_KEY");
    private static final String API_URL = dotenv.get("OPENAI_API_URL");

    public static void main(String[] args) throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        String theme = "La politique";
        String texte = "Dans le cadre du divertissement je souhaite creer un quizz sur un theme principal. Ce thème comprends 10 sous catégories que tu choissiras. le but d'avoir ces sous categories et d'avoir une meuilleur strucutre dans la base de donnée et d'aussi donner un poid a ces sous categories. En fonction du theme que je vais te donner tu vas devoir donc creer des sous catégories et leur attribue un poid. Ce poid a pour but de déterminer si la sous categorie est populaire ou non, si les personnes connnaissent bien le sous theme ou non. Tu es en charge de noter ce poid sur tes connaissances. La derniere partie est d'ajouer des questions a ces sous categories, plus son poid est haut plus elle doit posseder de question, plus il est bas moins il doit en posseder. les poids sont noté de 1 a 10, 1 etant le plus bas et 10 le plus haut. Les sous categories avec un poid de 1 a 3 contiendront 2 questions, les poids de 4 a 6 conteindront 4 questions et les poids de 7 a 10 conteindront 6 questions.Ta réponse doit avoir une structure très précise, je veux que tu me renvoie un fichier json avec LE theme principal tout en haut qui contient les sous theme qui contiennent leur poids, leurs questions ainsi que leurs réponses. Les réponses doivent etre au nombre de 4, 3 sont fausses et 1 vrais (elle doit etre differencier dans le json pour la reconnaitre). Si la question contient des chiffres ou des dates ils faut les mettre dans l'ordre croissant.Voici un exemple de a quoi ressemble le json : {\"theme_principal\": \"La science\",\"sous_categories\": [{ \"nom\": \"Physique\", \"poids\": 8, \"questions\": [ { \"question\": \"Quelle est la vitesse de la lumière dans le vide ?\",\"reponses\": { \"correcte\": \"299 792 458 m/s \",\"fausses\": [ \"150 000 m/s\", \"1 000 000 m/s\", \"500 000 000 m/s\" ] } },Maintenant en fonction du theme general donne moi les sous themes avec leur poids et leurs questions. Tu es en charge d'estimer les poids et de prendre des themes qui sont accessibles au grand public.Tu dois uniquement me renvoyer le fichier json et rien d'autre, je ne veux aucune explication uniquement le fichier json.Le thème a prendre est : " + theme;
        String test2 = "reponds moi uniquement \"oui\"";
        String body = "{\n" +
                "\"model\": \"gpt-3.5-turbo\",\n" +
                "\"messages\": [\n" +
                "    {\"role\": \"user\", \"content\": \"" + test2.replace("\"", "\\\"") + "\"}\n" +
                "]\n" +
                "}";
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(API_URL))
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer " + API_KEY)
                .POST(BodyPublishers.ofString(body))
                .build();

        HttpResponse<String> response = client.send(request, BodyHandlers.ofString());

        System.out.println("Status Code: " + response.statusCode());
        System.out.println("Response Body: " + response.body());
    }
}
