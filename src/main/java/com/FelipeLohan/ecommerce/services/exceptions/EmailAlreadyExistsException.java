package com.FelipeLohan.ecommerce.services.exceptions;

public class EmailAlreadyExistsException extends RuntimeException {

    public EmailAlreadyExistsException(String msg) {
        super(msg);
    }
}
