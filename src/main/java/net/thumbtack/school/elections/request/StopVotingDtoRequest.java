package net.thumbtack.school.elections.request;

public class StopVotingDtoRequest {

    private String token;

    public StopVotingDtoRequest(String token) {
        this.token = token;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
