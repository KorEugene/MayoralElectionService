package net.thumbtack.school.elections.response;

import net.thumbtack.school.elections.model.Offer;

import java.util.ArrayList;
import java.util.List;

public class GetOffersByAverageRatingDtoResponse {

    private List<OfferWithAverageRating> offers;

    public GetOffersByAverageRatingDtoResponse() {
        this.offers = new ArrayList<>();
    }

    public static class OfferWithAverageRating {

        private double averageRating;
        private Offer offer;

        public OfferWithAverageRating(double averageRating, Offer offer) {
            this.averageRating = averageRating;
            this.offer = offer;
        }

        public double getAverageRating() {
            return averageRating;
        }

        public void setAverageRating(double averageRating) {
            this.averageRating = averageRating;
        }

        public Offer getOffer() {
            return offer;
        }

        public void setOffer(Offer offer) {
            this.offer = offer;
        }
    }

    public List<OfferWithAverageRating> getOffers() {
        return offers;
    }

    public void setOffers(List<OfferWithAverageRating> offers) {
        this.offers = offers;
    }

    public void createOfferWithAverageRating(double averageRating, Offer offer) {
        OfferWithAverageRating offerWithAverageRating = new OfferWithAverageRating(averageRating, offer);
        offers.add(offerWithAverageRating);
    }
}
