package com.okworo.cards.exceptions;

/**
 * @author Morris.Okworo on 26/08/2023
 */
public class CustomValidationException extends RuntimeException {
    public CustomValidationException(String message) {
        super(message);
    }
}
