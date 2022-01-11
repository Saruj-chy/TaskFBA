package com.sd.spartan.taskfba.model;

public class Task {
    private String title, detail ;

    public Task(String title, String detail) {
        this.title = title;
        this.detail = detail;
    }


    public String getTitle() {
        return title;
    }

    public Task setTitle(String title) {
        this.title = title;
        return this;
    }

    public String getDetail() {
        return detail;
    }

    public Task setDetail(String detail) {
        this.detail = detail;
        return this;
    }
}
