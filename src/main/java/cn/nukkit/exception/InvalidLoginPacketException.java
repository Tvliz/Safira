package cn.nukkit.exception;

public class InvalidLoginPacketException extends RuntimeException {

    public InvalidLoginPacketException() {
    }

    public InvalidLoginPacketException(String message) {
        super(message);
    }

    public InvalidLoginPacketException(String message, Throwable cause) {
        super(message, cause);
    }

    public InvalidLoginPacketException(Throwable cause) {
        super(cause);
    }

    public InvalidLoginPacketException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
