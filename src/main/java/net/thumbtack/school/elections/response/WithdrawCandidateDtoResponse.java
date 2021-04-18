package net.thumbtack.school.elections.response;

public class WithdrawCandidateDtoResponse {

    private String message;

    public WithdrawCandidateDtoResponse(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
