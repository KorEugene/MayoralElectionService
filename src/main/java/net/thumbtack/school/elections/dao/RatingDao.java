package net.thumbtack.school.elections.dao;

import net.thumbtack.school.elections.model.Rating;

import java.util.List;

public interface RatingDao {

    String addNewRating(Rating rating);

    Rating getRatingById(String ratingId);

    String updateExistingRating(String ratingId, int value);

    String deleteExistingRating(String ratingId);

    List<Rating> getRatings();

    String getRatingIdByAuthorIdAndOfferId(String voterId, String offerId);
}
