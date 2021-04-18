package net.thumbtack.school.elections.request;

public class GetOfferListByAuthorDtoRequest {

    private String token;
    private String[] authorId;

    public GetOfferListByAuthorDtoRequest(String token, String[] authorId) {
        this.token = token;
        this.authorId = authorId;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String[] getAuthorId() {
        return authorId;
    }

    public void setAuthorId(String[] authorId) {
        this.authorId = authorId;
    }
}
