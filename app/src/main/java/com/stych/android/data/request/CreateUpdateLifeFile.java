package com.stych.android.data.request;

public class CreateUpdateLifeFile {
    public String name;

    public CreateUpdateLifeFile(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "CreateUpdateLifeFile{" +
                "name='" + name + '\'' +
                '}';
    }
}
