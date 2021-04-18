package net.thumbtack.school.elections.response;

public class AddOfferDtoResponse {

    private String offerId;

    public AddOfferDtoResponse(String offerId){
        this.offerId = offerId;
    }

    public String getOfferId() {
        return offerId;
    }

    public void setOfferId(String offerId) {
        this.offerId = offerId;
    }
}
