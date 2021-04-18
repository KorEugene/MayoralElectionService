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
import net.thumbtack.school.elections.model.Account;
import net.thumbtack.school.elections.model.Address;
import net.thumbtack.school.elections.model.FullName;
import net.thumbtack.school.elections.model.Offer;
import net.thumbtack.school.elections.model.Person;
import net.thumbtack.school.elections.model.Rating;
import net.thumbtack.school.elections.model.Voter;
import net.thumbtack.school.elections.request.RateOfferDtoRequest;
import net.thumbtack.school.elections.server.DataBase;
import net.thumbtack.school.elections.server.DataBaseManagementSystem;
import net.thumbtack.school.elections.server.TokenGenerator;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

public class TestRateOfferDtoValidator {

    private static final int AUTHOR_RATING_VALUE = 5;

    private DataBaseManagementSystem dbms = DataBaseManagementSystem.getInstance();
    private VoterDao voterDao = VoterDaoImpl.getInstance();
    private OfferDao offerDao = OfferDaoImpl.getInstance();
    private RatingDao ratingDao = RatingDaoImpl.getInstance();
    private static RateOfferDtoValidator validator = new RateOfferDtoValidator();
    private static FullName fullName = new FullName("Ivan", "Ivanov", "Ivanovich");
    private static Address address = new Address("Fedora Abramova", "15", "27");
    private static Account account = new Account("IVA", "123456");
    private static Voter voter = new Voter(new Person(fullName, address, account));
    private static String content = "use your brain";

    @Before
    public void generateTokens() {
        voter.setToken(TokenGenerator.generateNewToken());
    }

    @After
    public void cleanData() {
        DataBase.clearInstance();
    }

    @Test
    public void shouldReturnOfferExceptionWhenGetOfferFromDBWithInvalidId() throws Exception {
        //arrange
        String voterId = voterDao.addNewVoter(voter);
        String token = dbms.getVoterMap().get(voterId).getToken();
        Offer offer = new Offer(voterId, content);
        String offerId = offerDao.addNewOffer(offer);
        Rating rating = new Rating(offerId, voterId, AUTHOR_RATING_VALUE);
        ratingDao.addNewRating(rating);
        RateOfferDtoRequest request = new RateOfferDtoRequest(token, "2", "5");
        try {
            //act
            validator.validateRateOfferDtoRequest(request);
            fail();
        } catch (OfferException ex) {
            //assert
            assertEquals(1, dbms.getOfferMap().size());
            assertEquals(offerId, dbms.getOfferMap().get(offerId).getId());
            assertEquals(OfferError.OFFER_DOES_NOT_EXIST, ex.getErrorCode());
        }
    }

    @Test
    public void shouldReturnRatingExceptionWhenVoterIsAuthor() throws Exception {
        //arrange
        String voterId = voterDao.addNewVoter(voter);
        String token = dbms.getVoterMap().get(voterId).getToken();
        Offer offer = new Offer(voterId, content);
        String offerId = offerDao.addNewOffer(offer);
        Rating rating = new Rating(offerId, voterId, AUTHOR_RATING_VALUE);
        ratingDao.addNewRating(rating);
        RateOfferDtoRequest request = new RateOfferDtoRequest(token, offerId, "3");
        try {
            //act
            validator.validateRateOfferDtoRequest(request);
            fail();
        } catch (RatingException ex) {
            //assert
            assertEquals(1, dbms.getOfferMap().size());
            assertEquals(voterId, dbms.getOfferMap().get(offerId).getAuthorId());
            assertEquals(RatingError.AUTHOR_CHANGE_RATING, ex.getErrorCode());
        }
    }

    @Test
    public void shouldReturnRatingExceptionWhenChangeRatingWithInvalidValue() throws Exception {
        //arrange
        String voterId = voterDao.addNewVoter(voter);
        String token = dbms.getVoterMap().get(voterId).getToken();
        Offer offer = new Offer(voterId, content);
        String offerId = offerDao.addNewOffer(offer);
        Rating rating = new Rating(offerId, voterId, AUTHOR_RATING_VALUE);
        String message = ratingDao.addNewRating(rating);
        FullName fullName2 = new FullName("Petr", "Petrov", "Petrovich");
        Address address2 = new Address("Uralskaya", "11", "35");
        Account account2 = new Account("PETYA", "654321");
        Voter voter2 = new Voter(new Person(fullName2, address2, account2));
        voter2.setToken(TokenGenerator.generateNewToken());
        String voterId2 = voterDao.addNewVoter(voter2);
        String token2 = voterDao.getVoterTokenById(voterId2);
        RateOfferDtoRequest request = new RateOfferDtoRequest(token2, offerId, "6");
        try {
            //act
            validator.validateRateOfferDtoRequest(request);
            fail();
        } catch (RatingException ex) {
            //assert
            assertEquals(1, dbms.getOfferMap().size());
            assertEquals(RatingError.NOT_CORRECT_VALUE, ex.getErrorCode());
        }
    }
}
