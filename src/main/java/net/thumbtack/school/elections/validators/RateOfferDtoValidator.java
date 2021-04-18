package net.thumbtack.school.elections.validators;

import net.thumbtack.school.elections.dao.OfferDao;
import net.thumbtack.school.elections.dao.RatingDao;
import net.thumbtack.school.elections.dao.VoterDao;
import net.thumbtack.school.elections.daoimpl.OfferDaoImpl;
import net.thumbtack.school.elections.daoimpl.RatingDaoImpl;
import net.thumbtack.school.elections.daoimpl.VoterDaoImpl;
import net.thumbtack.school.elections.exceptionimpl.OfferError;
import net.thumbtack.school.elections.exceptionimpl.OfferException;
import net.thumbtack.school.elections.exceptionimpl.RatingError;
import net.thumbtack.school.elections.exceptionimpl.RatingException;
import net.thumbtack.school.elections.model.Offer;
import net.thumbtack.school.elections.model.Rating;
import net.thumbtack.school.elections.request.RateOfferDtoRequest;

import java.util.Arrays;

import static java.util.Objects.isNull;

public class RateOfferDtoValidator {

    private static final String[] VALID_RATINGS = new String[]{"0", "1", "2", "3", "4", "5"};
    private static final String ZERO = "0";

    private VoterDao voterDao = VoterDaoImpl.getInstance();
    private OfferDao offerDao = OfferDaoImpl.getInstance();
    private RatingDao ratingDao = RatingDaoImpl.getInstance();
    private Offer offer;

    public void validateRateOfferDtoRequest(RateOfferDtoRequest request) {
        validateOfferIsExist(request.getOfferId());
        validateOfferAuthor(request.getToken());
        validateDataForNewRating(request.getToken(), request.getOfferId(), request.getValue());
        validateRatingValue(request.getValue());
    }

    private void validateOfferIsExist(String id) {
        offer = offerDao.getOfferById(id);
        if (isNull(offer)) {
            throw new OfferException(OfferError.OFFER_DOES_NOT_EXIST);
        }
    }

    private void validateOfferAuthor(String token) {
        String authorId = voterDao.getVoterIdByToken(token);
        if (offer.getAuthorId().equals(authorId)) {
            throw new RatingException(RatingError.AUTHOR_CHANGE_RATING);
        }
    }

    private void validateDataForNewRating(String token, String offerId, String value) {
        String voterId = voterDao.getVoterIdByToken(token);
        String ratingId = ratingDao.getRatingIdByAuthorIdAndOfferId(voterId, offerId);
        Rating rating = ratingDao.getRatingById(ratingId);
        if (isNull(rating) && value.equals(ZERO)) {
            throw new RatingException(RatingError.NOT_CORRECT_VALUE);
        }
    }

    private void validateRatingValue(String value) {
        if (!Arrays.asList(VALID_RATINGS).contains(value)) {
            throw new RatingException(RatingError.NOT_CORRECT_VALUE);
        }
    }
}
