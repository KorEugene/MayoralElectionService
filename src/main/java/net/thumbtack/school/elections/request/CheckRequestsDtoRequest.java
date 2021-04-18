package net.thumbtack.school.elections.request;

public class CheckRequestsDtoRequest {

    private String token;

    public CheckRequestsDtoRequest(String token) {
        this.token = token;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
