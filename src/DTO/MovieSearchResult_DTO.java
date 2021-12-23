package DTO;

import java.io.Serializable;

public class MovieSearchResult_DTO implements Serializable {
    private static final long serialVersionUID=1L;
    String ID;
    String title;
    String img;
    String director;
    String stars;
    String rating;

    public MovieSearchResult_DTO(){
    }

    public MovieSearchResult_DTO(String ID, String title, String img, String director, String stars, String rating){
        this.ID = ID;
        this.title = title;
        this.img=img;
        this.director = director;
        this.stars = stars;
        this.rating = rating;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getDirector() {
        return director;
    }

    public void setDirector(String director) {
        this.director = director;
    }

    public String getStars() {
        return stars;
    }

    public void setStars(String stars) {
        this.stars = stars;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }
}
