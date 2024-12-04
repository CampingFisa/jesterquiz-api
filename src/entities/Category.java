package entities;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class Category extends AbstractTheme{

    @JsonProperty("categoryId")
    private int categoryId;

    @JsonProperty("categoryName")
    private String themeName;

    @JsonProperty("categoryDescription")
    private String categoryDescription;

    @JsonProperty("questions")
    private List<Question> questions;

    public Category() {}

    public Category(String themeName, String categoryDescription, List<Question> questions) {
        this.themeName = themeName;
        this.categoryDescription = categoryDescription;
        this.questions = questions;
    }

    @Override
    public String getThemeName() {
        return themeName;
    }

    public void setThemeName(String themeName) {
        this.themeName = themeName;
    }

    public List<Question> getQuestions() {
        return questions;
    }

    public void setQuestions(List<Question> questions) {
        this.questions = questions;
    }

    public String getCategoryDescription() {
        return categoryDescription;
    }

    public void setCategoryDescription(String categoryDescription) {
        this.categoryDescription = categoryDescription;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }
}
