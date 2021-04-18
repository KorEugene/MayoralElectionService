package net.thumbtack.school.elections.response;

public class AnswerNominateRequestDtoResponse {

    private String candidateId;
    private String message;

    public AnswerNominateRequestDtoResponse(String candidateId, String message) {
        this.candidateId = candidateId;
        this.message = message;
    }

    public String getCandidateId() {
        return candidateId;
    }

    public void setCandidateId(String candidateId) {
        this.candidateId = candidateId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
