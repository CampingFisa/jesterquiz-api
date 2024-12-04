import entities.AbstractTheme;
import entities.Category;
import entities.Theme;

import java.io.IOException;
import java.util.List;

public class CreateCategories {

    public static void main(String[] args) throws IOException, InterruptedException {
        int nbTheme = 15; //15
        int nbCat = 20;  //20
        int poidsMinimumSousCat = 5; //5
        int nbSousCat = 10;  //10
        int poidsMinimumSousSousCat = 8; //8
        int nbSousSousCat = 5; //5
        int nbQuestions = 10; //10

        RequestGpt requestGpt = new RequestGpt(nbTheme, nbCat ,poidsMinimumSousCat, nbSousCat, poidsMinimumSousSousCat, nbSousSousCat, nbQuestions);

        // get themes
        String themesStr = requestGpt.getThemes();
        List<Theme> themes = Theme.convertStringIntoThemes(themesStr);
        themes.forEach(theme -> {
            System.out.println("Theme principal: " + theme.getThemeName());
            String subThemesStr;
            try {

                // get subThemes
                subThemesStr = requestGpt.getSubThemes(theme);
            } catch (IOException | InterruptedException e) {
                throw new RuntimeException(e);
            }
            List<AbstractTheme> subThemes = Theme.convertStringIntoSubThemes(subThemesStr, poidsMinimumSousCat);
            subThemes.forEach(subTheme -> {
                System.out.println("-- Sous Thème: " + subTheme.getThemeName());
                if(subTheme instanceof Theme) {
                    theme.getChildrenThemes().add((Theme) subTheme);
                    String subSubThemesStr;
                    try {

                        // get subSubThemes
                        subSubThemesStr = requestGpt.getSubSubThemes((Theme) subTheme);
                    } catch (IOException | InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                    List<AbstractTheme> subSubThemes = Theme.convertStringIntoSubThemes(subSubThemesStr, poidsMinimumSousSousCat);
                    subSubThemes.forEach(subSubTheme -> {
                        System.out.println("---- Sous sous thème: " + subSubTheme.getThemeName());
                        if(subSubTheme instanceof Theme) {
                            ((Theme) subTheme).getChildrenThemes().add((Theme) subSubTheme);
                            String finalCategoriesStr;
                            try {

                                //get final categories
                                finalCategoriesStr = requestGpt.getFinalCategories((Theme) subSubTheme);
                            } catch (IOException | InterruptedException e) {
                                throw new RuntimeException(e);
                            }
                            List<Category> finalCategories = Theme.convertStringIntoFinalCategories(finalCategoriesStr);
                            finalCategories.forEach(category -> {System.out.println("------ Categorie: " + category.getThemeName());});
                            ((Theme) subSubTheme).setCategories(finalCategories);

                        } else if(subSubTheme instanceof Category) {
                            ((Theme) subTheme).getCategories().add((Category) subSubTheme);
                        }
                    });

                } else if(subTheme instanceof Category) {
                    theme.getCategories().add((Category) subTheme);
                }
            });
        });
        System.out.println(themes);
        themes.forEach(RequestDB::insertTheme);

    }
}
