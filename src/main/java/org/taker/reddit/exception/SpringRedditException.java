package org.taker.reddit.exception;

public class SpringRedditException extends RuntimeException {

    public SpringRedditException(String messgae) {
        super(messgae);
    }

    public SpringRedditException(String message, Exception cause) {
        super(message, cause);
    }
}
