package DTO;

import java.io.Serializable;

public class Review_DTO implements Serializable {
    private static final long serialVersionUID=4L;
    String username;
    String date;
    String rate;
    String title;
    String content;

    public Review_DTO(){
    }

    public Review_DTO(String username, String date, String rate, String title, String content){
        this.username=username;
        this.date=date;
        this.rate=rate;
        this.title=title;
        this.content=content;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getRate() {
        return rate;
    }

    public void setRate(String rate) {
        this.rate = rate;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
