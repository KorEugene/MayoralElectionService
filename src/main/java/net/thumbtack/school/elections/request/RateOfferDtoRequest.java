package net.thumbtack.school.elections.request;

public class RateOfferDtoRequest {

    private String token;
    private String offerId;
    private String value;

    public RateOfferDtoRequest(String token, String offerId, String value) {
        this.token = token;
        this.offerId = offerId;
        this.value = value;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getOfferId() {
        return offerId;
    }

    public void setOfferId(String offerId) {
        this.offerId = offerId;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
