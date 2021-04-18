package net.thumbtack.school.elections.request;

public class StartVotingDtoRequest {

    private String token;

    public StartVotingDtoRequest(String token) {
        this.token = token;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
