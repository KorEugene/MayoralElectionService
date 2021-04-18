package net.thumbtack.school.elections.request;

public class GetOffersByAverageRatingDtoRequest {

    private String token;

    public GetOffersByAverageRatingDtoRequest(String token){
        this.token = token;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
