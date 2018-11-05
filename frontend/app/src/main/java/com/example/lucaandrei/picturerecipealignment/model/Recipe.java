package com.example.lucaandrei.picturerecipealignment.model;

import android.webkit.URLUtil;

public class Recipe {
    private Integer id;
    private String name;
    private String url;
    private String strip;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        if (!URLUtil.isValidUrl(url)) {
            throw new IllegalArgumentException("The url has to be valid.");
        }
        this.url = url;
    }

    public String getStrip() {
        return strip;
    }

    public void setStrip(String strip) {
        for (Strip value: Strip.values()) {
            if (value.getValue().equals(strip)) {
                this.strip = strip;
                return;
            }
        }
        throw new IllegalArgumentException("Strip must have a valid name.");
    }
}
