package net.thumbtack.school.elections.response;

public class StopVotingDtoResponse {

    private String message;

    public StopVotingDtoResponse(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
