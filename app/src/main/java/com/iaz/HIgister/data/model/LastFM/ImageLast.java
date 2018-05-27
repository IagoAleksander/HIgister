package com.iaz.HIgister.data.model.LastFM;

import com.google.gson.annotations.SerializedName;

import org.simpleframework.xml.Root;

/**
 * Created by alksander on 26/05/2018.
 */

@Root(name = "Image", strict = false)
public class ImageLast {

    @SerializedName("#text")
    String imageUrl;

    @SerializedName("size")
    String imageSizeDescription;

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getImageSizeDescription() {
        return imageSizeDescription;
    }

    public void setImageSizeDescription(String imageSizeDescription) {
        this.imageSizeDescription = imageSizeDescription;
    }
}
