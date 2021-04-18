package net.thumbtack.school.elections.response;

public class VoteInElectionsDtoResponse {

    private String message;

    public VoteInElectionsDtoResponse(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
