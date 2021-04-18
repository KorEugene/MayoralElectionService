package net.thumbtack.school.elections.response;

public class RateOfferDtoResponse {

    private String message;

    public RateOfferDtoResponse(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
