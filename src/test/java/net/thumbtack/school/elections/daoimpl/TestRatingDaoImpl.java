package net.thumbtack.school.elections.daoimpl;

import net.thumbtack.school.elections.dao.OfferDao;
import net.thumbtack.school.elections.dao.RatingDao;
import net.thumbtack.school.elections.dao.VoterDao;
import net.thumbtack.school.elections.model.*;
import net.thumbtack.school.elections.server.DataBase;
import net.thumbtack.school.elections.server.DataBaseManagementSystem;
import net.thumbtack.school.elections.server.TokenGenerator;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class TestRatingDaoImpl {

    private static final int AUTHOR_RATING_VALUE = 5;

    private DataBaseManagementSystem dbms = DataBaseManagementSystem.getInstance();
    private VoterDao voterDao = VoterDaoImpl.getInstance();
    private OfferDao offerDao = OfferDaoImpl.getInstance();
    private RatingDao ratingDao = RatingDaoImpl.getInstance();
    private static FullName fullName1 = new FullName("Ivan", "Ivanov", "Ivanovich");
    private static Address address1 = new Address("Fedora Abramova", "15", "27");
    private static Account account1 = new Account("IVA", "123456");
    private static Voter voter1 = new Voter(new Person(fullName1, address1, account1));
    private static String voterId1;
    private static FullName fullName2 = new FullName("Petr", "Petrov", "Petrovich");
    private static Address address2 = new Address("Primorskaya", "35", "4");
    private static Account account2 = new Account("PETYA", "654321");
    private static Voter voter2 = new Voter(new Person(fullName2, address2, account2));
    private static String voterId2;
    private static String content = "TEST";
    private static Offer offer1 = new Offer(voterId1, content);
    private static Offer offer2 = new Offer(voterId2, content);
    private static String offerId1;
    private static String offerId2;

    @Before
    public void generateData() {
        voter1.setToken(TokenGenerator.generateNewToken());
        voter2.setToken(TokenGenerator.generateNewToken());
        voterId1 = voterDao.addNewVoter(voter1);
        voterId2 = voterDao.addNewVoter(voter2);
        offerId1 = offerDao.addNewOffer(offer1);
        offerId2 = offerDao.addNewOffer(offer2);
    }

    @After
    public void cleanData() {
        DataBase.clearInstance();
    }

    @Test
    public void shouldAddNewRatingToDataBase() throws Exception {
        //arrange
        Rating rating1 = new Rating(offerId1, voterId1, AUTHOR_RATING_VALUE);
        Rating rating2 = new Rating(offerId2, voterId1, 4);
        Rating rating3 = new Rating(offerId2, voterId2, AUTHOR_RATING_VALUE);
        Rating rating4 = new Rating(offerId1, voterId2, 4);
        //act
        String ratingId1 = ratingDao.addNewRating(rating1);
        String ratingId2 = ratingDao.addNewRating(rating2);
        String ratingId3 = ratingDao.addNewRating(rating3);
        String ratingId4 = ratingDao.addNewRating(rating4);
        //assert
        assertEquals(4, dbms.getRatingMap().size());
        assertEquals("1", ratingId1);
        assertEquals("2", ratingId2);
        assertEquals("3", ratingId3);
        assertEquals("4", ratingId4);
        assertEquals(5, dbms.getRatingMap().get("1").getValue());
        assertEquals(4, dbms.getRatingMap().get("2").getValue());
        assertEquals(5, dbms.getRatingMap().get("3").getValue());
        assertEquals(4, dbms.getRatingMap().get("4").getValue());
        assertEquals(voterId1, dbms.getRatingMap().get("1").getAuthorId());
        assertEquals(voterId1, dbms.getRatingMap().get("2").getAuthorId());
        assertEquals(voterId2, dbms.getRatingMap().get("3").getAuthorId());
        assertEquals(voterId2, dbms.getRatingMap().get("4").getAuthorId());
    }

    @Test
    public void shouldGetRatingById() throws Exception {
        //arrange
        Rating rating1 = new Rating(offerId1, voterId1, AUTHOR_RATING_VALUE);
        Rating rating2 = new Rating(offerId2, voterId1, 4);
        Rating rating3 = new Rating(offerId2, voterId2, AUTHOR_RATING_VALUE);
        Rating rating4 = new Rating(offerId1, voterId2, 4);
        String ratingId1 = ratingDao.addNewRating(rating1);
        String ratingId2 = ratingDao.addNewRating(rating2);
        String ratingId3 = ratingDao.addNewRating(rating3);
        String ratingId4 = ratingDao.addNewRating(rating4);
        //act
        Rating cloneRating1 = ratingDao.getRatingById(ratingId1);
        Rating cloneRating2 = ratingDao.getRatingById(ratingId2);
        Rating cloneRating3 = ratingDao.getRatingById(ratingId3);
        Rating cloneRating4 = ratingDao.getRatingById(ratingId4);
        //assert
        assertEquals(4, dbms.getRatingMap().size());
        assertEquals(rating1, cloneRating1);
        assertEquals(rating2, cloneRating2);
        assertEquals(rating3, cloneRating3);
        assertEquals(rating4, cloneRating4);
    }

    @Test
    public void shouldUpdateExistingRatingInDataBase() throws Exception {
        //arrange
        Rating rating1 = new Rating(offerId1, voterId1, AUTHOR_RATING_VALUE);
        Rating rating2 = new Rating(offerId2, voterId1, 4);
        Rating rating3 = new Rating(offerId2, voterId2, AUTHOR_RATING_VALUE);
        Rating rating4 = new Rating(offerId1, voterId2, 4);
        String ratingId1 = ratingDao.addNewRating(rating1);
        String ratingId2 = ratingDao.addNewRating(rating2);
        String ratingId3 = ratingDao.addNewRating(rating3);
        String ratingId4 = ratingDao.addNewRating(rating4);
        //act
        String message1 = ratingDao.updateExistingRating(ratingId2, 3);
        String message2 = ratingDao.updateExistingRating(ratingId4, 1);
        //assert
        assertEquals(4, dbms.getRatingMap().size());
        assertEquals(5, dbms.getRatingMap().get(ratingId1).getValue());
        assertEquals(3, dbms.getRatingMap().get(ratingId2).getValue());
        assertEquals(5, dbms.getRatingMap().get(ratingId3).getValue());
        assertEquals(1, dbms.getRatingMap().get(ratingId4).getValue());
        assertEquals(ResponseStatusMessage.RATING_STATUS_UPDATED.getMessage(), message1);
        assertEquals(ResponseStatusMessage.RATING_STATUS_UPDATED.getMessage(), message2);
    }

    @Test
    public void shouldDeleteExistingRatingFromDataBase() throws Exception {
        //arrange
        Rating rating1 = new Rating(offerId1, voterId1, AUTHOR_RATING_VALUE);
        Rating rating2 = new Rating(offerId2, voterId1, 4);
        Rating rating3 = new Rating(offerId2, voterId2, AUTHOR_RATING_VALUE);
        Rating rating4 = new Rating(offerId1, voterId2, 4);
        String ratingId1 = ratingDao.addNewRating(rating1);
        String ratingId2 = ratingDao.addNewRating(rating2);
        String ratingId3 = ratingDao.addNewRating(rating3);
        String ratingId4 = ratingDao.addNewRating(rating4);
        //act
        String ratingResultMessage1 = ratingDao.deleteExistingRating(ratingId2);
        String ratingResultMessage2 = ratingDao.deleteExistingRating(ratingId4);
        //assert
        assertEquals(2, dbms.getRatingMap().size());
        assertTrue(dbms.getRatingMap().containsKey(ratingId1));
        assertTrue(dbms.getRatingMap().containsKey(ratingId3));
        assertEquals(ResponseStatusMessage.RATING_STATUS_DELETED.getMessage(), ratingResultMessage1);
        assertEquals(ResponseStatusMessage.RATING_STATUS_DELETED.getMessage(), ratingResultMessage2);
    }

    @Test
    public void shouldReturnRatingList() throws Exception {
        //arrange
        Rating rating1 = new Rating(offerId1, voterId1, AUTHOR_RATING_VALUE);
        Rating rating2 = new Rating(offerId2, voterId1, 4);
        Rating rating3 = new Rating(offerId2, voterId2, AUTHOR_RATING_VALUE);
        Rating rating4 = new Rating(offerId1, voterId2, 4);
        String ratingId1 = ratingDao.addNewRating(rating1);
        String ratingId2 = ratingDao.addNewRating(rating2);
        String ratingId3 = ratingDao.addNewRating(rating3);
        String ratingId4 = ratingDao.addNewRating(rating4);
        //act
        List<Rating> ratings1 = ratingDao.getRatings();
        //assert
        assertEquals(4, ratings1.size());
        assertTrue(ratings1.contains(dbms.getRatingMap().get(ratingId1)));
        assertTrue(ratings1.contains(dbms.getRatingMap().get(ratingId2)));
        assertTrue(ratings1.contains(dbms.getRatingMap().get(ratingId3)));
        assertTrue(ratings1.contains(dbms.getRatingMap().get(ratingId4)));
    }

    @Test
    public void shouldGetRatingIdByAuthorIdAndOfferId() throws Exception {
        //arrange
        Rating rating1 = new Rating(offerId1, voterId1, AUTHOR_RATING_VALUE);
        Rating rating2 = new Rating(offerId2, voterId1, 4);
        Rating rating3 = new Rating(offerId2, voterId2, AUTHOR_RATING_VALUE);
        Rating rating4 = new Rating(offerId1, voterId2, 4);
        ratingDao.addNewRating(rating1);
        ratingDao.addNewRating(rating2);
        ratingDao.addNewRating(rating3);
        ratingDao.addNewRating(rating4);
        //act
        String ratingResultId1 = ratingDao.getRatingIdByAuthorIdAndOfferId(voterId1, offerId1);
        String ratingResultId2 = ratingDao.getRatingIdByAuthorIdAndOfferId(voterId1, offerId2);
        String ratingResultId3 = ratingDao.getRatingIdByAuthorIdAndOfferId(voterId2, offerId2);
        String ratingResultId4 = ratingDao.getRatingIdByAuthorIdAndOfferId(voterId2, offerId1);
        //assert
        assertEquals(4, dbms.getRatingMap().size());
        assertEquals("1", ratingResultId1);
        assertEquals("2", ratingResultId2);
        assertEquals("3", ratingResultId3);
        assertEquals("4", ratingResultId4);
    }
}
