package ru.job4j.accidents.exception;

public class SaveNotSuccessException extends RuntimeException {
    public SaveNotSuccessException(String message) {
        super(message);
    }
}
