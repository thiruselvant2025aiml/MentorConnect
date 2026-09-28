package com.mentorconnect.exception;

public class MentorCapacityExceededException extends RuntimeException {
    public MentorCapacityExceededException(String message) {
        super(message);
    }
}
