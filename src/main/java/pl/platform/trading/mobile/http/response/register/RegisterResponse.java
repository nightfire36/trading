package pl.platform.trading.mobile.http.response.register;

public class RegisterResponse {

    private int status;
    private int invalidFields;

    public RegisterResponse(int status, int invalidFields) {
        this.status = status;
        this.invalidFields = invalidFields;
    }

    public int getStatus() {
        return status;
    }

    public int getInvalidFields() {
        return invalidFields;
    }
}
