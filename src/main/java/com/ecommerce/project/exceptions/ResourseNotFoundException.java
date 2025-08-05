package com.ecommerce.project.exceptions;

public class ResourseNotFoundException extends RuntimeException {
    String resourseName;
    String fieldName;
    Long fieldvalue;
    String fieldvalue2;

    public ResourseNotFoundException(String resourseName, String fieldName, Long fieldvalue) {
        this.resourseName = resourseName;
        this.fieldName = fieldName;
        this.fieldvalue = fieldvalue;
    }

    public ResourseNotFoundException(String resourseName, String fieldName, String fieldvalue2) {
        this.resourseName = resourseName;
        this.fieldName = fieldName;
        this.fieldvalue = fieldvalue;
    }

    public String getMessage() {
        return String.format("%s not found with %s: %s", resourseName,  fieldName, fieldvalue);
    }

}
