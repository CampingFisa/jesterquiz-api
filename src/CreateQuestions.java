import entities.Category;
import entities.Question;
import entities.Theme;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class CreateQuestions {

    public static void main(String[] args) throws IOException, InterruptedException {
        int themeId = 1120;
        // TODO ajouter: créer question si catégorie en a pas

        int nbTheme = 15; //15
        int nbCat = 20;  //20
        int poidsMinimumSousCat = 5; //5
        int nbSousCat = 10;  //10
        int poidsMinimumSousSousCat = 8; //8
        int nbSousSousCat = 5; //5
        int nbQuestions = 10; //10

        RequestGpt requestGpt = new RequestGpt(nbTheme, nbCat ,poidsMinimumSousCat, nbSousCat, poidsMinimumSousSousCat, nbSousSousCat, nbQuestions);

        Theme theme = RequestDB.getTheme(themeId);
        List<Category> categories = new ArrayList<>();
        List<Theme> descendants = theme.getChildrenThemes();
        while(!descendants.isEmpty()) {
            descendants.addAll(descendants.getFirst().getChildrenThemes());
            categories.addAll(descendants.removeFirst().getCategories());
        }
        for(Category category : categories) {
            String questionsStr = requestGpt.getQuestions(category);
            List<Question> questions = Question.convertStringIntoQuestions(questionsStr);
            category.setQuestions(questions);
            RequestDB.updateCategory(category);
        }
    }
}
