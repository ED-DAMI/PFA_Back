package org.dami.pfa_back.Documents; // Assurez-vous que le chemin du package est correct

import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import java.util.Objects;


@Document(indexName = "genres") // Lie cette classe à l'index Elasticsearch
public class Genre {

    @Id // Marque ce champ comme l'identifiant unique
    private String id;

    private String name;

    private String description;


    public Genre() {
    }
    public Genre(String id, String name, String description) {
        this.id = id;
        this.name = name;
        this.description = description;
    }

    public Genre(String genreName) {
        this.name=genreName;
    }


    public String getId() {
        return id;
    }
    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }
    public void setId(String id) {
        this.id = id;
    }


    public void setName(String name) {
        this.name = name;
    }


    public void setDescription(String description) {
        this.description = description;
    }
    @Override
    public String toString() {
        return "Genre{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                '}';
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false; // Vérifie null et type
        Genre genre = (Genre) o;
        return Objects.equals(id, genre.id); // Compare basé sur l'ID (suppose que l'ID est l'identifiant unique)
        }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
