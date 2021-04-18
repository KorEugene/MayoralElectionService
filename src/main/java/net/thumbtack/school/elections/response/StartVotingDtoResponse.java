package net.thumbtack.school.elections.response;

public class StartVotingDtoResponse {

    private String message;

    public StartVotingDtoResponse(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
