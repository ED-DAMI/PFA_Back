package org.dami.pfa_back.Documents; // Assurez-vous que le chemin du package est correct

import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
// Optionnel: import org.springframework.data.elasticsearch.annotations.Field;
// Optionnel: import org.springframework.data.elasticsearch.annotations.FieldType;

import java.util.Date; // Importer la classe Date
import java.util.Objects; // Nécessaire pour equals et hashCode

/**
 * Représente un album musical dans l'index Elasticsearch "albums".
 */
@Document(indexName = "albums") // Lie cette classe à l'index Elasticsearch
public class Album {

    @Id // Marque ce champ comme l'identifiant unique
    private String id;

    // Le titre de l'album
    private String title;

    // L'identifiant de l'artiste auquel cet album appartient
    private String artistId;

    private Date releaseDate;

    // L'URL de l'image de couverture de l'album
    private String coverImage;

    // --- Constructeurs ---

    /**
     * Constructeur par défaut (sans arguments).
     * Nécessaire pour certains frameworks (comme Jackson pour la désérialisation JSON
     * ou Spring Data pour créer des instances à partir de la base de données).
     */
    public Album() {
        // Constructeur vide
    }

    /**
     * Constructeur avec tous les arguments pour initialiser un objet Album complet.
     *
     * @param id L'identifiant unique de l'album.
     * @param title Le titre de l'album.
     * @param artistId L'identifiant de l'artiste associé.
     * @param releaseDate La date de sortie de l'album.
     * @param coverImage L'URL de l'image de couverture.
     */
    public Album(String id, String title, String artistId, Date releaseDate, String coverImage) {
        this.id = id;
        this.title = title;
        this.artistId = artistId;
        this.releaseDate = releaseDate; // Assigner directement l'objet Date
        this.coverImage = coverImage;
    }

    // --- Getters ---

    /**
     * @return L'identifiant unique de l'album.
     */
    public String getId() {
        return id;
    }

    /**
     * @return Le titre de l'album.
     */
    public String getTitle() {
        return title;
    }

    /**
     * @return L'identifiant de l'artiste associé.
     */
    public String getArtistId() {
        return artistId;
    }

    /**
     * @return La date de sortie de l'album.
     * Note: Renvoie la référence de l'objet Date. Si l'immutabilité est critique,
     * vous pourriez envisager de renvoyer une copie (new Date(this.releaseDate.getTime())).
     */
    public Date getReleaseDate() {
        return releaseDate;
    }

    /**
     * @return L'URL de l'image de couverture (peut être null).
     */
    public String getCoverImage() {
        return coverImage;
    }

    // --- Setters ---

    /**
     * Définit l'identifiant unique de l'album.
     * @param id Le nouvel identifiant.
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * Définit le titre de l'album.
     * @param title Le nouveau titre.
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * Définit l'identifiant de l'artiste associé.
     * @param artistId Le nouvel identifiant d'artiste.
     */
    public void setArtistId(String artistId) {
        this.artistId = artistId;
    }

    /**
     * Définit la date de sortie de l'album.
     * @param releaseDate La nouvelle date de sortie.
     * Note: Accepte la référence de l'objet Date. Si l'immutabilité est critique,
     * vous pourriez envisager de stocker une copie (this.releaseDate = new Date(releaseDate.getTime())).
     */
    public void setReleaseDate(Date releaseDate) {
        this.releaseDate = releaseDate;
    }

    /**
     * Définit l'URL de l'image de couverture.
     * @param coverImage La nouvelle URL.
     */
    public void setCoverImage(String coverImage) {
        this.coverImage = coverImage;
    }

    // --- Méthodes Utilitaires (toString, equals, hashCode) ---

    /**
     * Retourne une représentation textuelle de l'objet Album.
     * Utile pour le débogage.
     * @return Une chaîne de caractères décrivant l'objet.
     */
    @Override
    public String toString() {
        return "Album{" +
                "id='" + id + '\'' +
                ", title='" + title + '\'' +
                ", artistId='" + artistId + '\'' +
                ", releaseDate=" + releaseDate +
                ", coverImage='" + coverImage + '\'' +
                '}';
    }

    /**
     * Compare cet objet Album à un autre objet pour vérifier l'égalité.
     * Deux albums sont considérés comme égaux s'ils ont le même ID.
     * @param o L'objet à comparer.
     * @return true si les objets sont égaux, false sinon.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true; // Optimisation : même instance
        if (o == null || getClass() != o.getClass()) return false; // Vérifie null et type
        Album album = (Album) o; // Cast après vérification du type
        return Objects.equals(id, album.id); // Compare basé sur l'ID (suppose que l'ID est l'identifiant unique)
    }

    /**
     * Calcule le code de hachage pour cet objet Album.
     * Doit être cohérent avec la méthode equals(). Si deux objets sont égaux
     * selon equals(), ils doivent avoir le même hashCode().
     * @return Le code de hachage de l'objet.
     */
    @Override
    public int hashCode() {
        // Basé principalement sur l'ID, car c'est ce qui est utilisé dans equals()
        return Objects.hash(id);
    }
}
