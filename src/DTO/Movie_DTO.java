package DTO;

import DTO.Actor_DTO;
import DTO.Review_DTO;

import java.io.Serializable;
import java.util.List;

public class Movie_DTO implements Serializable {
    private static final long serialVersionUID=2L;
    String ID;
    String Title;
    String image;
    String trailer;
    String imDbRating;
    String imDbRatingVotes;
    String tagline;
    String plot;
    String releaseDate;
    String runtimeMins;
    String directors;
    String writers;
    List<Actor_DTO> actors;
    String type;
    String genres;
    String companies;
    String countries;
    String languages;
    String contentRating;
    String keywords;
    List<Review_DTO> reviews;

    public Movie_DTO(){
    }

    public Movie_DTO( String ID, String Title, String image, String trailer, String imDbRating, String imDbRatingVotes,
                      String tagline, String plot, String releaseDate, String runtimeMins, String directors, String writers,
                      List<Actor_DTO> actors, String type, String genres, String companies, String countries, String languages,
                      String contentRating, String keywords, List<Review_DTO> reviews){
        this.ID=ID;
        this.Title=Title;
        this.image=image;
        this.trailer=trailer;
        this.imDbRating=imDbRating;
        this.imDbRatingVotes=imDbRatingVotes;
        this.tagline=tagline;
        this.plot=plot;
        this.releaseDate=releaseDate;
        this.runtimeMins=runtimeMins;
        this.directors=directors;
        this.writers=writers;
        this.actors=actors;
        this.type=type;
        this.genres=genres;
        this.companies=companies;
        this.countries=countries;
        this.languages=languages;
        this.contentRating=contentRating;
        this.keywords=keywords;
        this.reviews=reviews;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getTrailer() {
        return trailer;
    }

    public void setTrailer(String trailer) {
        this.trailer = trailer;
    }

    public String getImDbRating() {
        return imDbRating;
    }

    public void setImDbRating(String imDbRating) {
        this.imDbRating = imDbRating;
    }

    public String getImDbRatingVotes() {
        return imDbRatingVotes;
    }

    public void setImDbRatingVotes(String imDbRatingVotes) {
        this.imDbRatingVotes = imDbRatingVotes;
    }

    public String getTagline() {
        return tagline;
    }

    public void setTagline(String tagline) {
        this.tagline = tagline;
    }

    public String getPlot() {
        return plot;
    }

    public void setPlot(String plot) {
        this.plot = plot;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    public String getRuntimeMins() {
        return runtimeMins;
    }

    public void setRuntimeMins(String runtimeMins) {
        this.runtimeMins = runtimeMins;
    }

    public String getDirectors() {
        return directors;
    }

    public void setDirectors(String directors) {
        this.directors = directors;
    }

    public String getWriters() {
        return writers;
    }

    public void setWriters(String writers) {
        this.writers = writers;
    }

    public List<Actor_DTO> getActors() {
        return actors;
    }

    public void setActors(List<Actor_DTO> actors) {
        this.actors = actors;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getGenres() {
        return genres;
    }

    public void setGenres(String genres) {
        this.genres = genres;
    }

    public String getCompanies() {
        return companies;
    }

    public void setCompanies(String companies) {
        this.companies = companies;
    }

    public String getCountries() {
        return countries;
    }

    public void setCountries(String countries) {
        this.countries = countries;
    }

    public String getLanguages() {
        return languages;
    }

    public void setLanguages(String languages) {
        this.languages = languages;
    }

    public String getContentRating() {
        return contentRating;
    }

    public void setContentRating(String contentRating) {
        this.contentRating = contentRating;
    }

    public String getKeywords() {
        return keywords;
    }

    public void setKeywords(String keywords) {
        this.keywords = keywords;
    }

    public List<Review_DTO> getReviews() {
        return reviews;
    }

    public void setReviews(List<Review_DTO> reviews) {
        this.reviews = reviews;
    }
}


