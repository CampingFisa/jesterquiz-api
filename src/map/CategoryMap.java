package map;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonProperty;

public class CategoryMap extends AbstractThemeMap{

    @JsonProperty("sous-catégorie")
    @JsonAlias("sous-categorie")
    private String categorie;

    @JsonProperty("description")
    private String description;

    private int poids;

    public int getPoids() {
        return poids;
    }

    public void setPoids(int poids) {
        this.poids = poids;
    }

    public String getCategorie() {
        return categorie;
    }

    public void setCategorie(String categorie) {
        this.categorie = categorie;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
