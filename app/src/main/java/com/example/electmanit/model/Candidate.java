package com.example.electmanit.model;

import java.lang.reflect.Constructor;

public class Candidate {

    String name;
    String branch;
    String position;
    String id;

    String batch;
    int count = 0;

    public Candidate(String name, String branch, String position, String batch, String id) {
        this.name = name;
        this.branch = branch;
        this.position = position;
        this.batch = batch;
        this.id = id;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
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

    public String getBranch() {
        return branch;
    }

    public void setBranch(String branch) {
        this.branch = branch;
    }

    public String getBatch() {
        return batch;
    }

    public void setBatch(String batch) {
        this.batch = batch;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }
}
