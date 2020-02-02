package com.mahmoud.printinghouse.models.local;

import androidx.annotation.NonNull;

public class ShieldType {
    public String title ;
    public String url ;
    public int imgId ;

    public ShieldType(String title, String url,int imgId) {
        this.title = title;
        this.url = url;
        this.imgId = imgId;
    }

    public ShieldType() {
    }

    @NonNull
    @Override
    public String toString() {
        return title;
    }
}
