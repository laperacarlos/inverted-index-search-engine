package com.findwise.exception;

public class InvalidDocumentContentException extends Exception {

    public InvalidDocumentContentException(String id) {
        super("Document identified by Id: " + id + " has blank content.");
    }
}
