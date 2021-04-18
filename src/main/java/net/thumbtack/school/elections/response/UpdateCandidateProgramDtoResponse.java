package net.thumbtack.school.elections.response;

public class UpdateCandidateProgramDtoResponse {

    private String message;

    public UpdateCandidateProgramDtoResponse(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
