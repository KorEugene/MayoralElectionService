package net.thumbtack.school.elections.request;

public class VoteInElectionsDtoRequest {

    private String token;
    private String candidateId;

    public VoteInElectionsDtoRequest(String token, String candidateId) {
        this.token = token;
        this.candidateId = candidateId;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getCandidateId() {
        return candidateId;
    }

    public void setCandidateId(String candidateId) {
        this.candidateId = candidateId;
    }
}
