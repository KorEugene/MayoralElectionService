package net.thumbtack.school.elections.daoimpl;

import net.thumbtack.school.elections.dao.RatingDao;
import net.thumbtack.school.elections.model.Rating;
import net.thumbtack.school.elections.server.DataBaseManagementSystem;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class RatingDaoImpl implements RatingDao {

    private static volatile RatingDaoImpl instance;

    private DataBaseManagementSystem dbms = DataBaseManagementSystem.getInstance();

    private RatingDaoImpl() {
    }

    static {
        instance = new RatingDaoImpl();
    }

    public static RatingDaoImpl getInstance() {
        return instance;
    }

    @Override
    public String addNewRating(Rating rating) {
        return dbms.insertNewRatingToDataBase(rating);
    }

    @Override
    public Rating getRatingById(String ratingId) {
        return dbms.getRatingMap().get(ratingId);
    }

    @Override
    public String updateExistingRating(String ratingId, int value) {
        return dbms.updateRatingInDataBase(ratingId, value);
    }

    @Override
    public String deleteExistingRating(String ratingId) {
        return dbms.deleteRatingFromDataBase(ratingId);
    }

    @Override
    public List<Rating> getRatings() {
        return new ArrayList<>(dbms.getRatingMap().values());
    }

    @Override
    public String getRatingIdByAuthorIdAndOfferId(String voterId, String offerId) {
        return dbms.getRatingMap()
                .entrySet()
                .stream()
                .filter(entry -> entry.getValue().getAuthorId().equals(voterId)
                        && entry.getValue().getOfferId().equals(offerId))
                .map(Map.Entry::getKey)
                .findFirst()
                .orElse(null);
    }
}
