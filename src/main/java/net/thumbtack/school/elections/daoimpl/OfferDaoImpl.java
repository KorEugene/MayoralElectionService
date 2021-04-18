package net.thumbtack.school.elections.daoimpl;

import net.thumbtack.school.elections.dao.OfferDao;
import net.thumbtack.school.elections.model.Offer;
import net.thumbtack.school.elections.server.DataBaseManagementSystem;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class OfferDaoImpl implements OfferDao {

    private static volatile OfferDaoImpl instance;

    private DataBaseManagementSystem dbms = DataBaseManagementSystem.getInstance();

    private OfferDaoImpl() {
    }

    static {
        instance = new OfferDaoImpl();
    }

    public static OfferDaoImpl getInstance() {
        return instance;
    }

    @Override
    public String addNewOffer(Offer offer) {
        return dbms.insertNewOfferToDataBase(offer);
    }

    @Override
    public Offer getOfferById(String offerId) {
        return dbms.getOfferMap().get(offerId);
    }

    @Override
    public String updateOfferCopyrights(String offerId, boolean status) {
        return dbms.updateOfferCopyrights(offerId, status);
    }

    @Override
    public List<Offer> getOffers() {
        return new ArrayList<>(dbms.getOfferMap().values());
    }

    @Override
    public List<Offer> getOffersByAuthorId(String authorId) {
        return getOffers()
                .stream()
                .filter(o -> o.getAuthorId().equals(authorId) && o.isCopyrightsActive())
                .collect(Collectors.toList());
    }
}
