package net.thumbtack.school.elections.request;

public class AddCandidateDtoRequest {

    private String token;
    private String nomineeId;

    public AddCandidateDtoRequest(String token, String nomineeId) {
        this.token = token;
        this.nomineeId = nomineeId;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getNomineeId() {
        return nomineeId;
    }

    public void setNomineeId(String nomineeId) {
        this.nomineeId = nomineeId;
    }
}
