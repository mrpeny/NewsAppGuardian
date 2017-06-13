package com.example.mrpeny.mrpenynewsapp;

import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by MrPeny on 2017. 06. 12..
 */

public class NewsData {
    private String sectionName;
    private String webPublicationDate;
    private String webTitle;
    private String webUrl;

    public NewsData(String sectionName, String webPublicationDate, String webTitle, String webUrl) {
        this.sectionName = sectionName;
        this.webPublicationDate = webPublicationDate;
        this.webTitle = webTitle;
        this.webUrl = webUrl;
    }

    public String getSectionName() {
        return sectionName;
    }

    public String getWebPublicationDate() {
        return webPublicationDate;
    }

    public String getWebTitle() {
        return webTitle;
    }

    public String getWebUrl() {
        return webUrl;
    }
}
