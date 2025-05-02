package org.dami.pfa_back.Documents; // Assurez-vous que le package est correct

import lombok.Data; // Génère getters, setters, toString, equals, hashCode
import lombok.NoArgsConstructor; // Génère le constructeur sans arguments
import lombok.AllArgsConstructor; // Génère le constructeur avec tous les arguments
import org.springframework.data.annotation.Id; // Annotation pour l'identifiant
import org.springframework.data.elasticsearch.annotations.Document; // Pour mapper la classe à un index Elasticsearch
import org.springframework.data.elasticsearch.annotations.Field; // Optionnel : pour un contrôle fin du mapping
import org.springframework.data.elasticsearch.annotations.FieldType; // Optionnel : pour spécifier le type de champ Elasticsearch

@Document(indexName = "artists") // Lie cette classe à l'index Elasticsearch nommé "artists"

public class Artist {

    @Id // Marque ce champ comme l'identifiant unique du document dans Elasticsearch
    private String id;
    private String name;
    private String bio;
    private String photoUrl;

    public Artist(String name) {
        this.name=name;
    }

    @Override
    public String toString() {
        return "Artist{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", bio='" + bio + '\'' +
                ", photoUrl='" + photoUrl + '\'' +
                '}';
    }

    public Artist() {
    }

    public Artist(String id, String name, String bio, String photoUrl) {
        this.id = id;
        this.name = name;
        this.bio = bio;
        this.photoUrl = photoUrl;
    }

    public String getId() {
        return id;
    }

    public Artist setId(String id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return name;
    }

    public Artist setName(String name) {
        this.name = name;
        return this;
    }

    public String getBio() {
        return bio;
    }

    public Artist setBio(String bio) {
        this.bio = bio;
        return this;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public Artist setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
        return this;
    }
}
