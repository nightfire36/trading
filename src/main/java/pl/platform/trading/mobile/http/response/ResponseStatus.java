package pl.platform.trading.mobile.http.response;

public class ResponseStatus {
    private int status;

    public ResponseStatus(int status) {
        this.status = status;
    }

    public int getStatus() {
        return status;
    }
}
