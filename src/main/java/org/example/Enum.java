package org.example;

//перечисления для удобства работы с json
public enum Enum {
    ID("id"),
    TASK("task"),
    STATUS("status"),
    CREATEDAT("createdAt"),
    UPDATEAT("updatedAt");

    Enum (String name){
        this.name = name;
    }

    private String name;
    public String getName(){
        return name;
    }

}
