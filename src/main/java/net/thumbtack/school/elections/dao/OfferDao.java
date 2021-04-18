package net.thumbtack.school.elections.dao;

import net.thumbtack.school.elections.model.Offer;

import java.util.List;

public interface OfferDao {

    String addNewOffer(Offer offer);

    Offer getOfferById(String offerId);

    String updateOfferCopyrights(String offerId, boolean status);

    List<Offer> getOffers();

    List<Offer> getOffersByAuthorId(String authorId);
}
