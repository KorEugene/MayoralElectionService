package net.thumbtack.school.elections.request;

public class AddOfferDtoRequest {

    private String token;
    private String content;

    public AddOfferDtoRequest(String token, String content){
        this.token = token;
        this.content = content;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
