package net.thumbtack.school.elections.request;

public class LogOutDtoRequest {

    private String token;

    public LogOutDtoRequest(String token) {
        this.token = token;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
