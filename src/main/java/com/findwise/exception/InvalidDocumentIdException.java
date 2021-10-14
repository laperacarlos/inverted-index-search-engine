package com.findwise.exception;

public class InvalidDocumentIdException extends Exception {

    public InvalidDocumentIdException() {
        super("Document can't be identified by Id field.");
    }
}
