package com.curioud.signclass.domain.etc;

public enum ObjectType {
    CHECKBOX("OBJECT_TYPE_CHECKBOX"),
    TEXT("OBJECT_TYPE_TEXT"),
    SIGN("OBJECT_TYPE_SIGN");

    final private String name;

    public String getName(){
        return name;
    }

    private ObjectType(String name) {
        this.name = name;
    }

}
