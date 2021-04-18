package net.thumbtack.school.elections.request;

public class WithdrawCandidateDtoRequest {

    private String token;

    public WithdrawCandidateDtoRequest(String token) {
        this.token = token;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
