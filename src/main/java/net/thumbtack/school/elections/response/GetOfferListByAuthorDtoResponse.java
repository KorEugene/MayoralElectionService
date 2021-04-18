package net.thumbtack.school.elections.response;

import net.thumbtack.school.elections.model.Offer;

import java.util.ArrayList;
import java.util.List;

public class GetOfferListByAuthorDtoResponse {

    private List<List<Offer>> offers;

    public GetOfferListByAuthorDtoResponse() {
        this.offers = new ArrayList<>();
    }

    public List<List<Offer>> getOffers() {
        return offers;
    }

    public void setOffers(List<List<Offer>> offers) {
        this.offers = offers;
    }
}

