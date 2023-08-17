package pl.aircheck.server;

public class NoDataFromOriginException extends Exception {
    public NoDataFromOriginException(String message) {
        super(message);
    }
}
