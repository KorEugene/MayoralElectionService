package net.thumbtack.school.elections.response;

public class LogOutDtoResponse {

    private String message;

    public LogOutDtoResponse(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
