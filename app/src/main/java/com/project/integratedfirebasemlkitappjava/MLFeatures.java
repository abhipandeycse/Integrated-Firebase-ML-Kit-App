package com.project.integratedfirebasemlkitappjava;

public class MLFeatures {
    String featuresName;
    int featuresImg;

    public MLFeatures(String featuresName, int featuresImg) {
        this.featuresName = featuresName;
        this.featuresImg = featuresImg;
    }

    public String getFeaturesName() {
        return featuresName;
    }

    public void setFeaturesName(String featuresName) {
        this.featuresName = featuresName;
    }

    public int getFeaturesImg() {
        return featuresImg;
    }

    public void setFeaturesImg(int featuresImg) {
        this.featuresImg = featuresImg;
    }
}
