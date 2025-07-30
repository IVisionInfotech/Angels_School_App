package com.ivision.model;

import java.io.Serializable;

public class Subject implements Serializable {

    private String id, subject, type, title, vidCode;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getVidCode() {
        return vidCode;
    }

    public void setVidCode(String vidCode) {
        this.vidCode = vidCode;
    }
}
