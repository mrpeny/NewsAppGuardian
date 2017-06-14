package com.example.mrpeny.mrpenynewsapp;

/**
 * Class representing the News queried through Guardian search API
 */

class NewsData {
    private String sectionName;
    private String webPublicationDate;
    private String webTitle;
    private String webUrl;

    NewsData(String sectionName, String webPublicationDate, String webTitle, String webUrl) {
        this.sectionName = sectionName;
        this.webPublicationDate = webPublicationDate;
        this.webTitle = webTitle;
        this.webUrl = webUrl;
    }

    String getSectionName() {
        return sectionName;
    }

    String getWebPublicationDate() {
        return webPublicationDate;
    }

    String getWebTitle() {
        return webTitle;
    }

    String getWebUrl() {
        return webUrl;
    }
}
