package com.example.bullsAndCows.exceptions;

public class WrongNumberException extends IllegalArgumentException {
    public WrongNumberException(String s) {
        super(s);
    }
}
