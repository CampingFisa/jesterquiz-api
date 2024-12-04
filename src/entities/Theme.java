package entities;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import map.CategoryMap;
import map.ThemeMap;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Theme extends AbstractTheme{

    @JsonProperty("themeId")
    private int themeId;

    @JsonProperty("themeName")
    private String themeName;

    @JsonProperty("themeDescription")
    private String themeDescription;

    @JsonProperty("isMain")
    private boolean isMain;

    @JsonProperty("themeChildren")
    private List<Theme> childrenThemes;

    @JsonProperty("categories")
    private List<Category> categories;

    public Theme() {}

    public Theme(List<Theme> childrenThemes, List<Category> categories, boolean isMain, String themeDescription, String themeName) {
        this.childrenThemes = childrenThemes != null ?  childrenThemes : new ArrayList<>();
        this.categories = categories;
        this.isMain = isMain;
        this.themeDescription = themeDescription;
        this.themeName = themeName;
    }

    // Getters and Setters


    public int getThemeId() {
        return themeId;
    }

    public void setThemeId(int themeId) {
        this.themeId = themeId;
    }

    public boolean isMain() {
        return isMain;
    }

    public List<Category> getCategories() {
        return categories;
    }

    public void setCategories(List<Category> categories) {
        this.categories = categories;
    }

    public String getThemeName() {
        return themeName;
    }

    public void setThemeName(String themeName) {
        this.themeName = themeName;
    }

    public String getThemeDescription() {
        return themeDescription;
    }

    public void setThemeDescription(String themeDescription) {
        this.themeDescription = themeDescription;
    }

    @JsonProperty("isMain")
    public boolean getIsMain() {
        return isMain;
    }

    public void setMain(boolean isMain) {
        this.isMain = isMain;
    }

    public List<Theme> getChildrenThemes() {
        return childrenThemes;
    }

    public void setChildrenThemes(List<Theme> childrenThemes) {
        this.childrenThemes = childrenThemes;
    }

    public static List<Theme> convertStringIntoThemes(String themesStr){
        ObjectMapper objectMapper = new ObjectMapper();
        List<Theme> themes = new ArrayList<>();
        try {
            List<ThemeMap> themesMap = objectMapper.readValue(themesStr, new TypeReference<>(){});

            for (ThemeMap theme : themesMap) {
                themes.add(new Theme(new ArrayList<>(), new ArrayList<>(), true, theme.getDescription(), theme.getTheme()));
            }
            return themes;

        } catch (IOException e) {
            e.printStackTrace();
        }
        return themes;
    }

    public static List<AbstractTheme> convertStringIntoSubThemes(String subThemesStr, int poidsMinimumSousCat) {
        ObjectMapper objectMapper = new ObjectMapper();
        List<AbstractTheme> themes = new ArrayList<>();
        try {
            List<CategoryMap> subThemesMap = objectMapper.readValue(subThemesStr, new TypeReference<>(){});

            for (CategoryMap subTheme : subThemesMap) {
                if(subTheme.getPoids() > poidsMinimumSousCat) {
                    themes.add(new Theme(new ArrayList<>(), new ArrayList<>(), false, null, subTheme.getCategorie()));
                } else {
                    themes.add(new Category(subTheme.getCategorie(), subTheme.getDescription(), new ArrayList<>()));
                }
            }
            return themes;

        } catch (IOException e) {
            e.printStackTrace();
        }
        return themes;
    }

    public static List<Category> convertStringIntoFinalCategories(String subThemesStr) {
        ObjectMapper objectMapper = new ObjectMapper();
        List<Category> categories = new ArrayList<>();
        try {
            List<CategoryMap> subThemesMap = objectMapper.readValue(subThemesStr, new TypeReference<>(){});

            for (CategoryMap subTheme : subThemesMap) {
                categories.add(new Category(subTheme.getCategorie(), subTheme.getDescription(), new ArrayList<>()));
            }
            return categories;

        } catch (IOException e) {
            e.printStackTrace();
        }
        return categories;
    }

}

