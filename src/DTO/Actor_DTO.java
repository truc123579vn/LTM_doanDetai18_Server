package DTO;

import java.io.Serializable;

public class Actor_DTO implements Serializable {
    private static final long serialVersionUID=3L;
    String id;
    String name;
    String character;
    String image;

    public Actor_DTO(){
    }

    public Actor_DTO(String id, String name, String character, String image){
        this.id=id;
        this.name=name;
        this.character=character;
        this.image=image;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCharacter() {
        return character;
    }

    public void setCharacter(String character) {
        this.character = character;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}



