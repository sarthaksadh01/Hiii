package com.cool.sarthak.hiii;

/**
 * Created by sarthak on 15/01/18.
 */

public class Users {
    String name;
    String image;
    String status;

    String thumbnail_image;

    public Users(String thumbnail_image) {
        this.thumbnail_image = thumbnail_image;
    }

    public void setThumbnail_image(String thumbnail_image) {

        this.thumbnail_image = thumbnail_image;
    }

    public String getThumbnail_image() {

        return thumbnail_image;
    }


    public Users()
    {

    }

    public String getStatus() {
        return status;
    }
    public void setStatus(String status) {
        this.status = status;
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {

        this.name = name;
    }




    public String getImage() {


        return image;
    }
    public void setImage(String image) {

        this.image = image;
    }


    public Users(String name, String image, String status) {
        this.name = name;
        this.image = image;
        this.status = status;
    }






}
