package com.server.http.utils.properties;

public class Property {
    private PropertyName name;
    private String value;

    public Property(PropertyName name, String value) {
        this.name = name;
        this.value = value;
    }

    public Property(PropertyName name) {
        this.name = name;
    }

    public PropertyName getName() {
        return name;
    }

    public void setName(PropertyName name) {
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
