package net.thumbtack.school.elections.service;

import net.thumbtack.school.elections.dao.CandidateProgramDao;
import net.thumbtack.school.elections.dao.RatingDao;
import net.thumbtack.school.elections.dao.VoterDao;
import net.thumbtack.school.elections.daoimpl.CandidateProgramDaoImpl;
import net.thumbtack.school.elections.daoimpl.RatingDaoImpl;
import net.thumbtack.school.elections.daoimpl.VoterDaoImpl;
import net.thumbtack.school.elections.exceptionimpl.CandidateError;
import net.thumbtack.school.elections.exceptionimpl.CandidateException;
import net.thumbtack.school.elections.model.*;
import net.thumbtack.school.elections.request.*;
import net.thumbtack.school.elections.response.*;
import net.thumbtack.school.elections.server.DataBase;
import net.thumbtack.school.elections.server.DataBaseManagementSystem;
import net.thumbtack.school.elections.server.Server;
import org.junit.After;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.*;

public class TestVoterService {

    private static Server server = Server.getInstance();
    private static VoterService voterService = server.getVoterService();
    private static DataBaseManagementSystem dbms = DataBaseManagementSystem.getInstance();
    private static VoterDao voterDao = VoterDaoImpl.getInstance();
    private static RatingDao ratingDao = RatingDaoImpl.getInstance();
    private static CandidateProgramDao candidateProgramDao = CandidateProgramDaoImpl.getInstance();
    private static FullName fullName1 = new FullName("Ivan", "Ivanov", "Ivanovich");
    private static Address address1 = new Address("Fedora Abramova", "15", "27");
    private static Account account1 = new Account("IVA", "123456");
    private static FullName fullName2 = new FullName("Petr", "Petrov", "Petrovich");
    private static Address address2 = new Address("Primorskaya", "35", "4");
    private static Account account2 = new Account("PETYA", "654321");
    private static FullName fullName3 = new FullName("Semen", "Semenov", "Semenovich");
    private static Address address3 = new Address("Compositorov", "2", "11");
    private static Account account3 = new Account("Semena33", "987654");
    private static String token1;
    private static String token2;
    private static String token3;
    private static String content1 = "use your brain!";
    private static String content2 = "do not be stupid!";
    private static String content3 = "do not be shy!";
    private static String offerId1;
    private static String offerId2;
    private static String offerId3;

    @After
    public void clearData() {
        DataBase.clearInstance();
    }

    @Test
    public void testRegisterVoter() throws Exception {
        //act
        RegisterVoterDtoRequest registerVoterDtoRequest1 = new RegisterVoterDtoRequest(fullName1, address1, account1);
        RegisterVoterDtoResponse voterResponse1 = voterService.registerNewVoter(registerVoterDtoRequest1);
        token1 = voterResponse1.getToken();
        RegisterVoterDtoRequest registerVoterDtoRequest2 = new RegisterVoterDtoRequest(fullName2, address2, account2);
        RegisterVoterDtoResponse voterResponse2 = voterService.registerNewVoter(registerVoterDtoRequest2);
        token2 = voterResponse2.getToken();
        RegisterVoterDtoRequest registerVoterDtoRequest3 = new RegisterVoterDtoRequest(fullName3, address3, account3);
        RegisterVoterDtoResponse voterResponse3 = voterService.registerNewVoter(registerVoterDtoRequest3);
        token3 = voterResponse3.getToken();
        //assert
        assertEquals(3, dbms.getVoterMap().size());
        assertEquals(3, dbms.getPersonMap().size());
        assertTrue(dbms.getPersonMap().containsKey("1"));
        assertTrue(dbms.getPersonMap().containsKey("2"));
        assertTrue(dbms.getPersonMap().containsKey("3"));
        assertEquals(token1, dbms.getVoterMap().get("1").getToken());
        assertEquals(token2, dbms.getVoterMap().get("2").getToken());
        assertEquals(token3, dbms.getVoterMap().get("3").getToken());
        assertEquals(4, dbms.getVoterId());
    }

    @Test
    public void testAddOffer() throws Exception {
        //arrange
        String correctOfferId_1 = "1";
        String correctOfferId_2 = "2";
        String correctOfferId_3 = "3";
        RegisterVoterDtoRequest registerVoterDtoRequest1 = new RegisterVoterDtoRequest(fullName1, address1, account1);
        RegisterVoterDtoResponse voterResponse1 = voterService.registerNewVoter(registerVoterDtoRequest1);
        token1 = voterResponse1.getToken();
        RegisterVoterDtoRequest registerVoterDtoRequest2 = new RegisterVoterDtoRequest(fullName2, address2, account2);
        RegisterVoterDtoResponse voterResponse2 = voterService.registerNewVoter(registerVoterDtoRequest2);
        token2 = voterResponse2.getToken();
        RegisterVoterDtoRequest registerVoterDtoRequest3 = new RegisterVoterDtoRequest(fullName3, address3, account3);
        RegisterVoterDtoResponse voterResponse3 = voterService.registerNewVoter(registerVoterDtoRequest3);
        token3 = voterResponse3.getToken();
        //act
        AddOfferDtoRequest addOfferDtoRequest1 = new AddOfferDtoRequest(token1, content1);
        AddOfferDtoResponse offerResponse1 = voterService.addNewOffer(addOfferDtoRequest1);
        offerId1 = offerResponse1.getOfferId();
        AddOfferDtoRequest addOfferDtoRequest2 = new AddOfferDtoRequest(token2, content2);
        AddOfferDtoResponse offerResponse2 = voterService.addNewOffer(addOfferDtoRequest2);
        offerId2 = offerResponse2.getOfferId();
        AddOfferDtoRequest addOfferDtoRequest3 = new AddOfferDtoRequest(token3, content3);
        AddOfferDtoResponse offerResponse3 = voterService.addNewOffer(addOfferDtoRequest3);
        offerId3 = offerResponse3.getOfferId();
        String authorId1 = voterDao.getVoterIdByToken(token1);
        String authorId2 = voterDao.getVoterIdByToken(token2);
        String authorId3 = voterDao.getVoterIdByToken(token3);
        //assert
        assertEquals(3, dbms.getOfferMap().size());
        assertEquals(correctOfferId_1, offerId1);
        assertEquals(correctOfferId_2, offerId2);
        assertEquals(correctOfferId_3, offerId3);
        assertEquals(3, dbms.getRatingMap().size());
        assertEquals(authorId1, dbms.getRatingMap().get("1").getAuthorId());
        assertEquals(authorId2, dbms.getRatingMap().get("2").getAuthorId());
        assertEquals(authorId3, dbms.getRatingMap().get("3").getAuthorId());
        assertEquals(5, dbms.getRatingMap().get("1").getValue());
        assertEquals(5, dbms.getRatingMap().get("2").getValue());
        assertEquals(5, dbms.getRatingMap().get("3").getValue());
    }

    @Test
    public void testCreateNewRating() throws Exception {
        //arrange
        String correctCreateRatingResponse = ResponseStatusMessage.RATING_STATUS_CREATED.getMessage();
        RegisterVoterDtoRequest registerVoterDtoRequest1 = new RegisterVoterDtoRequest(fullName1, address1, account1);
        RegisterVoterDtoResponse voterResponse1 = voterService.registerNewVoter(registerVoterDtoRequest1);
        token1 = voterResponse1.getToken();
        RegisterVoterDtoRequest registerVoterDtoRequest2 = new RegisterVoterDtoRequest(fullName2, address2, account2);
        RegisterVoterDtoResponse voterResponse2 = voterService.registerNewVoter(registerVoterDtoRequest2);
        token2 = voterResponse2.getToken();
        RegisterVoterDtoRequest registerVoterDtoRequest3 = new RegisterVoterDtoRequest(fullName3, address3, account3);
        RegisterVoterDtoResponse voterResponse3 = voterService.registerNewVoter(registerVoterDtoRequest3);
        token3 = voterResponse3.getToken();
        AddOfferDtoRequest addOfferDtoRequest1 = new AddOfferDtoRequest(token1, content1);
        AddOfferDtoResponse offerResponse1 = voterService.addNewOffer(addOfferDtoRequest1);
        offerId1 = offerResponse1.getOfferId();
        AddOfferDtoRequest addOfferDtoRequest2 = new AddOfferDtoRequest(token2, content2);
        AddOfferDtoResponse offerResponse2 = voterService.addNewOffer(addOfferDtoRequest2);
        offerId2 = offerResponse2.getOfferId();
        AddOfferDtoRequest addOfferDtoRequest3 = new AddOfferDtoRequest(token3, content3);
        AddOfferDtoResponse offerResponse3 = voterService.addNewOffer(addOfferDtoRequest3);
        offerId3 = offerResponse3.getOfferId();
        //act
        RateOfferDtoRequest rateOfferDtoRequest1 = new RateOfferDtoRequest(token1, offerId2, "2");
        RateOfferDtoResponse rateResponse1 = voterService.rateOffer(rateOfferDtoRequest1);
        String response_1_2 = rateResponse1.getMessage();
        RateOfferDtoRequest rateOfferDtoRequest2 = new RateOfferDtoRequest(token1, offerId3, "2");
        RateOfferDtoResponse rateResponse2 = voterService.rateOffer(rateOfferDtoRequest2);
        String response_1_3 = rateResponse2.getMessage();
        RateOfferDtoRequest rateOfferDtoRequest3 = new RateOfferDtoRequest(token2, offerId1, "3");
        RateOfferDtoResponse rateResponse3 = voterService.rateOffer(rateOfferDtoRequest3);
        String response_2_1 = rateResponse3.getMessage();
        RateOfferDtoRequest rateOfferDtoRequest4 = new RateOfferDtoRequest(token2, offerId3, "3");
        RateOfferDtoResponse rateResponse4 = voterService.rateOffer(rateOfferDtoRequest4);
        String response_2_3 = rateResponse4.getMessage();
        RateOfferDtoRequest rateOfferDtoRequest5 = new RateOfferDtoRequest(token3, offerId1, "4");
        RateOfferDtoResponse rateResponse5 = voterService.rateOffer(rateOfferDtoRequest5);
        String response_3_1 = rateResponse5.getMessage();
        RateOfferDtoRequest rateOfferDtoRequest6 = new RateOfferDtoRequest(token3, offerId2, "4");
        RateOfferDtoResponse rateResponse6 = voterService.rateOffer(rateOfferDtoRequest6);
        String response_3_2 = rateResponse6.getMessage();
        String ratingId_1_1 = ratingDao.getRatingIdByAuthorIdAndOfferId(voterDao.getVoterIdByToken(token1), offerId1);
        String ratingId_2_1 = ratingDao.getRatingIdByAuthorIdAndOfferId(voterDao.getVoterIdByToken(token2), offerId1);
        String ratingId_3_1 = ratingDao.getRatingIdByAuthorIdAndOfferId(voterDao.getVoterIdByToken(token3), offerId1);
        String ratingId_1_2 = ratingDao.getRatingIdByAuthorIdAndOfferId(voterDao.getVoterIdByToken(token1), offerId2);
        String ratingId_2_2 = ratingDao.getRatingIdByAuthorIdAndOfferId(voterDao.getVoterIdByToken(token2), offerId2);
        String ratingId_3_2 = ratingDao.getRatingIdByAuthorIdAndOfferId(voterDao.getVoterIdByToken(token3), offerId2);
        String ratingId_1_3 = ratingDao.getRatingIdByAuthorIdAndOfferId(voterDao.getVoterIdByToken(token1), offerId3);
        String ratingId_2_3 = ratingDao.getRatingIdByAuthorIdAndOfferId(voterDao.getVoterIdByToken(token2), offerId3);
        String ratingId_3_3 = ratingDao.getRatingIdByAuthorIdAndOfferId(voterDao.getVoterIdByToken(token3), offerId3);
        long quantityOffer1Ratings = dbms.getRatingMap().values().stream().filter(v -> v.getOfferId().equals(offerId1)).count();
        long quantityOffer2Ratings = dbms.getRatingMap().values().stream().filter(v -> v.getOfferId().equals(offerId2)).count();
        long quantityOffer3Ratings = dbms.getRatingMap().values().stream().filter(v -> v.getOfferId().equals(offerId3)).count();
        //assert
        assertEquals(9, dbms.getRatingMap().size());
        assertEquals(3, quantityOffer1Ratings);
        assertEquals(3, quantityOffer2Ratings);
        assertEquals(3, quantityOffer3Ratings);
        assertEquals(correctCreateRatingResponse, response_1_2);
        assertEquals(correctCreateRatingResponse, response_1_3);
        assertEquals(correctCreateRatingResponse, response_2_1);
        assertEquals(correctCreateRatingResponse, response_2_3);
        assertEquals(correctCreateRatingResponse, response_3_1);
        assertEquals(correctCreateRatingResponse, response_3_2);
        //offer_1
        assertEquals(5, ratingDao.getRatingById(ratingId_1_1).getValue());
        assertEquals("1", ratingDao.getRatingById(ratingId_1_1).getAuthorId());
        assertEquals(3, ratingDao.getRatingById(ratingId_2_1).getValue());
        assertEquals("2", ratingDao.getRatingById(ratingId_2_1).getAuthorId());
        assertEquals(4, ratingDao.getRatingById(ratingId_3_1).getValue());
        assertEquals("3", ratingDao.getRatingById(ratingId_3_1).getAuthorId());
        //offer_2
        assertEquals(5, ratingDao.getRatingById(ratingId_2_2).getValue());
        assertEquals("2", ratingDao.getRatingById(ratingId_2_2).getAuthorId());
        assertEquals(2, ratingDao.getRatingById(ratingId_1_2).getValue());
        assertEquals("1", ratingDao.getRatingById(ratingId_1_2).getAuthorId());
        assertEquals(4, ratingDao.getRatingById(ratingId_3_2).getValue());
        assertEquals("3", ratingDao.getRatingById(ratingId_3_2).getAuthorId());
        //offer_3
        assertEquals(5, ratingDao.getRatingById(ratingId_3_3).getValue());
        assertEquals("3", ratingDao.getRatingById(ratingId_3_3).getAuthorId());
        assertEquals(2, ratingDao.getRatingById(ratingId_1_3).getValue());
        assertEquals("1", ratingDao.getRatingById(ratingId_1_3).getAuthorId());
        assertEquals(3, ratingDao.getRatingById(ratingId_2_3).getValue());
        assertEquals("2", ratingDao.getRatingById(ratingId_2_3).getAuthorId());
    }

    @Test
    public void testDeleteExistingRating() throws Exception {
        //arrange
        String correctDeleteRatingResponse = ResponseStatusMessage.RATING_STATUS_DELETED.getMessage();
        RegisterVoterDtoRequest registerVoterDtoRequest1 = new RegisterVoterDtoRequest(fullName1, address1, account1);
        RegisterVoterDtoResponse voterResponse1 = voterService.registerNewVoter(registerVoterDtoRequest1);
        token1 = voterResponse1.getToken();
        RegisterVoterDtoRequest registerVoterDtoRequest2 = new RegisterVoterDtoRequest(fullName2, address2, account2);
        RegisterVoterDtoResponse voterResponse2 = voterService.registerNewVoter(registerVoterDtoRequest2);
        token2 = voterResponse2.getToken();
        RegisterVoterDtoRequest registerVoterDtoRequest3 = new RegisterVoterDtoRequest(fullName3, address3, account3);
        RegisterVoterDtoResponse voterResponse3 = voterService.registerNewVoter(registerVoterDtoRequest3);
        token3 = voterResponse3.getToken();
        AddOfferDtoRequest addOfferDtoRequest1 = new AddOfferDtoRequest(token1, content1);
        AddOfferDtoResponse offerResponse1 = voterService.addNewOffer(addOfferDtoRequest1);
        offerId1 = offerResponse1.getOfferId();
        AddOfferDtoRequest addOfferDtoRequest2 = new AddOfferDtoRequest(token2, content2);
        AddOfferDtoResponse offerResponse2 = voterService.addNewOffer(addOfferDtoRequest2);
        offerId2 = offerResponse2.getOfferId();
        AddOfferDtoRequest addOfferDtoRequest3 = new AddOfferDtoRequest(token3, content3);
        AddOfferDtoResponse offerResponse3 = voterService.addNewOffer(addOfferDtoRequest3);
        offerId3 = offerResponse3.getOfferId();
        RateOfferDtoRequest rateOfferDtoRequest1 = new RateOfferDtoRequest(token1, offerId2, "2");
        voterService.rateOffer(rateOfferDtoRequest1);
        RateOfferDtoRequest rateOfferDtoRequest2 = new RateOfferDtoRequest(token1, offerId3, "2");
        voterService.rateOffer(rateOfferDtoRequest2);
        RateOfferDtoRequest rateOfferDtoRequest3 = new RateOfferDtoRequest(token2, offerId1, "3");
        voterService.rateOffer(rateOfferDtoRequest3);
        RateOfferDtoRequest rateOfferDtoRequest4 = new RateOfferDtoRequest(token2, offerId3, "3");
        voterService.rateOffer(rateOfferDtoRequest4);
        RateOfferDtoRequest rateOfferDtoRequest5 = new RateOfferDtoRequest(token3, offerId1, "4");
        voterService.rateOffer(rateOfferDtoRequest5);
        RateOfferDtoRequest rateOfferDtoRequest6 = new RateOfferDtoRequest(token3, offerId2, "4");
        voterService.rateOffer(rateOfferDtoRequest6);
        //act
        RateOfferDtoRequest deleteRateRequest1 = new RateOfferDtoRequest(token1, offerId2, "0");
        RateOfferDtoResponse deleteRateResponse1 = voterService.rateOffer(deleteRateRequest1);
        String response_1_2 = deleteRateResponse1.getMessage();
        RateOfferDtoRequest deleteRateRequest2 = new RateOfferDtoRequest(token2, offerId3, "0");
        RateOfferDtoResponse deleteRateResponse2 = voterService.rateOffer(deleteRateRequest2);
        String response_2_3 = deleteRateResponse2.getMessage();
        RateOfferDtoRequest deleteRateRequest3 = new RateOfferDtoRequest(token3, offerId1, "0");
        RateOfferDtoResponse deleteRateResponse3 = voterService.rateOffer(deleteRateRequest3);
        String response_3_1 = deleteRateResponse3.getMessage();
        String ratingId_1_1 = ratingDao.getRatingIdByAuthorIdAndOfferId(voterDao.getVoterIdByToken(token1), offerId1);
        String ratingId_2_1 = ratingDao.getRatingIdByAuthorIdAndOfferId(voterDao.getVoterIdByToken(token2), offerId1);
        String ratingId_2_2 = ratingDao.getRatingIdByAuthorIdAndOfferId(voterDao.getVoterIdByToken(token2), offerId2);
        String ratingId_3_2 = ratingDao.getRatingIdByAuthorIdAndOfferId(voterDao.getVoterIdByToken(token3), offerId2);
        String ratingId_1_3 = ratingDao.getRatingIdByAuthorIdAndOfferId(voterDao.getVoterIdByToken(token1), offerId3);
        String ratingId_3_3 = ratingDao.getRatingIdByAuthorIdAndOfferId(voterDao.getVoterIdByToken(token3), offerId3);
        long quantityOffer1Ratings = dbms.getRatingMap().values().stream().filter(v -> v.getOfferId().equals(offerId1)).count();
        long quantityOffer2Ratings = dbms.getRatingMap().values().stream().filter(v -> v.getOfferId().equals(offerId2)).count();
        long quantityOffer3Ratings = dbms.getRatingMap().values().stream().filter(v -> v.getOfferId().equals(offerId3)).count();
        //assert
        assertEquals(2, quantityOffer1Ratings);
        assertEquals(2, quantityOffer2Ratings);
        assertEquals(2, quantityOffer3Ratings);
        assertEquals(correctDeleteRatingResponse, response_1_2);
        assertEquals(correctDeleteRatingResponse, response_2_3);
        assertEquals(correctDeleteRatingResponse, response_3_1);
        //offer_1
        assertEquals(5, ratingDao.getRatingById(ratingId_1_1).getValue());
        assertEquals("1", ratingDao.getRatingById(ratingId_1_1).getAuthorId());
        assertEquals(3, ratingDao.getRatingById(ratingId_2_1).getValue());
        assertEquals("2", ratingDao.getRatingById(ratingId_2_1).getAuthorId());
        //offer_2
        assertEquals(5, ratingDao.getRatingById(ratingId_2_2).getValue());
        assertEquals("2", ratingDao.getRatingById(ratingId_2_2).getAuthorId());
        assertEquals(4, ratingDao.getRatingById(ratingId_3_2).getValue());
        assertEquals("3", ratingDao.getRatingById(ratingId_3_2).getAuthorId());
        //offer_3
        assertEquals(5, ratingDao.getRatingById(ratingId_3_3).getValue());
        assertEquals("3", ratingDao.getRatingById(ratingId_3_3).getAuthorId());
        assertEquals(2, ratingDao.getRatingById(ratingId_1_3).getValue());
        assertEquals("1", ratingDao.getRatingById(ratingId_1_3).getAuthorId());
    }

    @Test
    public void testUpdateExistingRating() throws Exception {
        //arrange
        String correctUpdateRatingResponse = ResponseStatusMessage.RATING_STATUS_UPDATED.getMessage();
        RegisterVoterDtoRequest registerVoterDtoRequest1 = new RegisterVoterDtoRequest(fullName1, address1, account1);
        RegisterVoterDtoResponse voterResponse1 = voterService.registerNewVoter(registerVoterDtoRequest1);
        token1 = voterResponse1.getToken();
        RegisterVoterDtoRequest registerVoterDtoRequest2 = new RegisterVoterDtoRequest(fullName2, address2, account2);
        RegisterVoterDtoResponse voterResponse2 = voterService.registerNewVoter(registerVoterDtoRequest2);
        token2 = voterResponse2.getToken();
        RegisterVoterDtoRequest registerVoterDtoRequest3 = new RegisterVoterDtoRequest(fullName3, address3, account3);
        RegisterVoterDtoResponse voterResponse3 = voterService.registerNewVoter(registerVoterDtoRequest3);
        token3 = voterResponse3.getToken();
        AddOfferDtoRequest addOfferDtoRequest1 = new AddOfferDtoRequest(token1, content1);
        AddOfferDtoResponse offerResponse1 = voterService.addNewOffer(addOfferDtoRequest1);
        offerId1 = offerResponse1.getOfferId();
        AddOfferDtoRequest addOfferDtoRequest2 = new AddOfferDtoRequest(token2, content2);
        AddOfferDtoResponse offerResponse2 = voterService.addNewOffer(addOfferDtoRequest2);
        offerId2 = offerResponse2.getOfferId();
        AddOfferDtoRequest addOfferDtoRequest3 = new AddOfferDtoRequest(token3, content3);
        AddOfferDtoResponse offerResponse3 = voterService.addNewOffer(addOfferDtoRequest3);
        offerId3 = offerResponse3.getOfferId();
        RateOfferDtoRequest rateOfferDtoRequest1 = new RateOfferDtoRequest(token1, offerId2, "2");
        voterService.rateOffer(rateOfferDtoRequest1);
        RateOfferDtoRequest rateOfferDtoRequest2 = new RateOfferDtoRequest(token1, offerId3, "2");
        voterService.rateOffer(rateOfferDtoRequest2);
        RateOfferDtoRequest rateOfferDtoRequest3 = new RateOfferDtoRequest(token2, offerId1, "3");
        voterService.rateOffer(rateOfferDtoRequest3);
        RateOfferDtoRequest rateOfferDtoRequest4 = new RateOfferDtoRequest(token2, offerId3, "3");
        voterService.rateOffer(rateOfferDtoRequest4);
        RateOfferDtoRequest rateOfferDtoRequest5 = new RateOfferDtoRequest(token3, offerId1, "4");
        voterService.rateOffer(rateOfferDtoRequest5);
        RateOfferDtoRequest rateOfferDtoRequest6 = new RateOfferDtoRequest(token3, offerId2, "4");
        voterService.rateOffer(rateOfferDtoRequest6);
        //act
        RateOfferDtoRequest updateRateRequest1 = new RateOfferDtoRequest(token1, offerId3, "3");
        RateOfferDtoResponse updateRateResponse1 = voterService.rateOffer(updateRateRequest1);
        String response_1_3 = updateRateResponse1.getMessage();
        RateOfferDtoRequest updateRateRequest2 = new RateOfferDtoRequest(token2, offerId1, "2");
        RateOfferDtoResponse updateRateResponse2 = voterService.rateOffer(updateRateRequest2);
        String response_2_1 = updateRateResponse2.getMessage();
        RateOfferDtoRequest updateRateRequest3 = new RateOfferDtoRequest(token3, offerId2, "1");
        RateOfferDtoResponse updateRateResponse3 = voterService.rateOffer(updateRateRequest3);
        String response_3_2 = updateRateResponse3.getMessage();
        String ratingId_1_1 = ratingDao.getRatingIdByAuthorIdAndOfferId(voterDao.getVoterIdByToken(token1), offerId1);
        String ratingId_2_1 = ratingDao.getRatingIdByAuthorIdAndOfferId(voterDao.getVoterIdByToken(token2), offerId1);
        String ratingId_3_1 = ratingDao.getRatingIdByAuthorIdAndOfferId(voterDao.getVoterIdByToken(token3), offerId1);
        String ratingId_1_2 = ratingDao.getRatingIdByAuthorIdAndOfferId(voterDao.getVoterIdByToken(token1), offerId2);
        String ratingId_2_2 = ratingDao.getRatingIdByAuthorIdAndOfferId(voterDao.getVoterIdByToken(token2), offerId2);
        String ratingId_3_2 = ratingDao.getRatingIdByAuthorIdAndOfferId(voterDao.getVoterIdByToken(token3), offerId2);
        String ratingId_1_3 = ratingDao.getRatingIdByAuthorIdAndOfferId(voterDao.getVoterIdByToken(token1), offerId3);
        String ratingId_2_3 = ratingDao.getRatingIdByAuthorIdAndOfferId(voterDao.getVoterIdByToken(token2), offerId3);
        String ratingId_3_3 = ratingDao.getRatingIdByAuthorIdAndOfferId(voterDao.getVoterIdByToken(token3), offerId3);
        long quantityOffer1Ratings = dbms.getRatingMap().values().stream().filter(v -> v.getOfferId().equals(offerId1)).count();
        long quantityOffer2Ratings = dbms.getRatingMap().values().stream().filter(v -> v.getOfferId().equals(offerId2)).count();
        long quantityOffer3Ratings = dbms.getRatingMap().values().stream().filter(v -> v.getOfferId().equals(offerId3)).count();
        //assert
        assertEquals(3, quantityOffer1Ratings);
        assertEquals(3, quantityOffer2Ratings);
        assertEquals(3, quantityOffer3Ratings);
        assertEquals(correctUpdateRatingResponse, response_1_3);
        assertEquals(correctUpdateRatingResponse, response_2_1);
        assertEquals(correctUpdateRatingResponse, response_3_2);
        //offer_1
        assertEquals(5, ratingDao.getRatingById(ratingId_1_1).getValue());
        assertEquals("1", ratingDao.getRatingById(ratingId_1_1).getAuthorId());
        assertEquals(2, ratingDao.getRatingById(ratingId_2_1).getValue());
        assertEquals("2", ratingDao.getRatingById(ratingId_2_1).getAuthorId());
        assertEquals(4, ratingDao.getRatingById(ratingId_3_1).getValue());
        assertEquals("3", ratingDao.getRatingById(ratingId_3_1).getAuthorId());
        //offer_2
        assertEquals(5, ratingDao.getRatingById(ratingId_2_2).getValue());
        assertEquals("2", ratingDao.getRatingById(ratingId_2_2).getAuthorId());
        assertEquals(2, ratingDao.getRatingById(ratingId_1_2).getValue());
        assertEquals("1", ratingDao.getRatingById(ratingId_1_2).getAuthorId());
        assertEquals(1, ratingDao.getRatingById(ratingId_3_2).getValue());
        assertEquals("3", ratingDao.getRatingById(ratingId_3_2).getAuthorId());
        //offer_3
        assertEquals(5, ratingDao.getRatingById(ratingId_3_3).getValue());
        assertEquals("3", ratingDao.getRatingById(ratingId_3_3).getAuthorId());
        assertEquals(3, ratingDao.getRatingById(ratingId_1_3).getValue());
        assertEquals("1", ratingDao.getRatingById(ratingId_1_3).getAuthorId());
        assertEquals(3, ratingDao.getRatingById(ratingId_2_3).getValue());
        assertEquals("2", ratingDao.getRatingById(ratingId_2_3).getAuthorId());
    }

    @Test
    public void testGetOffersSortedByAverageRating() throws Exception {
        //arrange
        RegisterVoterDtoRequest registerVoterDtoRequest1 = new RegisterVoterDtoRequest(fullName1, address1, account1);
        RegisterVoterDtoResponse voterResponse1 = voterService.registerNewVoter(registerVoterDtoRequest1);
        token1 = voterResponse1.getToken();
        RegisterVoterDtoRequest registerVoterDtoRequest2 = new RegisterVoterDtoRequest(fullName2, address2, account2);
        RegisterVoterDtoResponse voterResponse2 = voterService.registerNewVoter(registerVoterDtoRequest2);
        token2 = voterResponse2.getToken();
        RegisterVoterDtoRequest registerVoterDtoRequest3 = new RegisterVoterDtoRequest(fullName3, address3, account3);
        RegisterVoterDtoResponse voterResponse3 = voterService.registerNewVoter(registerVoterDtoRequest3);
        token3 = voterResponse3.getToken();
        AddOfferDtoRequest addOfferDtoRequest1 = new AddOfferDtoRequest(token1, content1);
        AddOfferDtoResponse offerResponse1 = voterService.addNewOffer(addOfferDtoRequest1);
        offerId1 = offerResponse1.getOfferId();
        AddOfferDtoRequest addOfferDtoRequest2 = new AddOfferDtoRequest(token2, content2);
        AddOfferDtoResponse offerResponse2 = voterService.addNewOffer(addOfferDtoRequest2);
        offerId2 = offerResponse2.getOfferId();
        AddOfferDtoRequest addOfferDtoRequest3 = new AddOfferDtoRequest(token3, content3);
        AddOfferDtoResponse offerResponse3 = voterService.addNewOffer(addOfferDtoRequest3);
        offerId3 = offerResponse3.getOfferId();
        RateOfferDtoRequest rateOfferDtoRequest1 = new RateOfferDtoRequest(token1, offerId2, "2");
        voterService.rateOffer(rateOfferDtoRequest1);
        RateOfferDtoRequest rateOfferDtoRequest2 = new RateOfferDtoRequest(token1, offerId3, "2");
        voterService.rateOffer(rateOfferDtoRequest2);
        RateOfferDtoRequest rateOfferDtoRequest3 = new RateOfferDtoRequest(token2, offerId1, "3");
        voterService.rateOffer(rateOfferDtoRequest3);
        RateOfferDtoRequest rateOfferDtoRequest4 = new RateOfferDtoRequest(token2, offerId3, "3");
        voterService.rateOffer(rateOfferDtoRequest4);
        RateOfferDtoRequest rateOfferDtoRequest5 = new RateOfferDtoRequest(token3, offerId1, "4");
        voterService.rateOffer(rateOfferDtoRequest5);
        RateOfferDtoRequest rateOfferDtoRequest6 = new RateOfferDtoRequest(token3, offerId2, "4");
        voterService.rateOffer(rateOfferDtoRequest6);
        //act
        GetOffersByAverageRatingDtoRequest request = new GetOffersByAverageRatingDtoRequest(token1);
        GetOffersByAverageRatingDtoResponse response = voterService.getOffersSortedByAverageRate(request);
        List<GetOffersByAverageRatingDtoResponse.OfferWithAverageRating> resultList = response.getOffers();
        //assert
        assertEquals(dbms.getOfferMap().get(offerId1), resultList.get(0).getOffer());
        assertEquals(dbms.getOfferMap().get(offerId2), resultList.get(1).getOffer());
        assertEquals(dbms.getOfferMap().get(offerId3), resultList.get(2).getOffer());
    }

    @Test
    public void testGetOffersByAuthorId() throws Exception {
        //arrange
        RegisterVoterDtoRequest registerVoterDtoRequest1 = new RegisterVoterDtoRequest(fullName1, address1, account1);
        RegisterVoterDtoResponse voterResponse1 = voterService.registerNewVoter(registerVoterDtoRequest1);
        token1 = voterResponse1.getToken();
        RegisterVoterDtoRequest registerVoterDtoRequest2 = new RegisterVoterDtoRequest(fullName2, address2, account2);
        RegisterVoterDtoResponse voterResponse2 = voterService.registerNewVoter(registerVoterDtoRequest2);
        token2 = voterResponse2.getToken();
        RegisterVoterDtoRequest registerVoterDtoRequest3 = new RegisterVoterDtoRequest(fullName3, address3, account3);
        RegisterVoterDtoResponse voterResponse3 = voterService.registerNewVoter(registerVoterDtoRequest3);
        token3 = voterResponse3.getToken();
        AddOfferDtoRequest addOfferDtoRequest1 = new AddOfferDtoRequest(token1, content1);
        AddOfferDtoResponse offerResponse1 = voterService.addNewOffer(addOfferDtoRequest1);
        offerId1 = offerResponse1.getOfferId();
        AddOfferDtoRequest addOfferDtoRequest2 = new AddOfferDtoRequest(token2, content2);
        AddOfferDtoResponse offerResponse2 = voterService.addNewOffer(addOfferDtoRequest2);
        offerId2 = offerResponse2.getOfferId();
        AddOfferDtoRequest addOfferDtoRequest3 = new AddOfferDtoRequest(token3, content3);
        AddOfferDtoResponse offerResponse3 = voterService.addNewOffer(addOfferDtoRequest3);
        offerId3 = offerResponse3.getOfferId();
        String voterId2 = voterDao.getVoterIdByToken(token2);
        String voterId3 = voterDao.getVoterIdByToken(token3);
        //act
        String[] ids = {voterId2, voterId3};
        GetOfferListByAuthorDtoRequest request = new GetOfferListByAuthorDtoRequest(token1, ids);
        GetOfferListByAuthorDtoResponse response = voterService.getOfferListByAuthor(request);
        List<List<Offer>> resultList = response.getOffers();
        //assert
        assertTrue(resultList.get(0).contains(dbms.getOfferMap().get(offerId2)));
        assertTrue(resultList.get(1).contains(dbms.getOfferMap().get(offerId3)));
    }

    @Test
    public void testAddYourselfToCandidates() throws Exception {
        //arrange
        RegisterVoterDtoRequest registerVoterDtoRequest1 = new RegisterVoterDtoRequest(fullName1, address1, account1);
        RegisterVoterDtoResponse voterResponse1 = voterService.registerNewVoter(registerVoterDtoRequest1);
        token1 = voterResponse1.getToken();
        RegisterVoterDtoRequest registerVoterDtoRequest2 = new RegisterVoterDtoRequest(fullName2, address2, account2);
        RegisterVoterDtoResponse voterResponse2 = voterService.registerNewVoter(registerVoterDtoRequest2);
        token2 = voterResponse2.getToken();
        RegisterVoterDtoRequest registerVoterDtoRequest3 = new RegisterVoterDtoRequest(fullName3, address3, account3);
        RegisterVoterDtoResponse voterResponse3 = voterService.registerNewVoter(registerVoterDtoRequest3);
        token3 = voterResponse3.getToken();
        AddOfferDtoRequest addOfferDtoRequest1 = new AddOfferDtoRequest(token1, content1);
        AddOfferDtoResponse offerResponse1 = voterService.addNewOffer(addOfferDtoRequest1);
        offerId1 = offerResponse1.getOfferId();
        AddOfferDtoRequest addOfferDtoRequest2 = new AddOfferDtoRequest(token2, content2);
        AddOfferDtoResponse offerResponse2 = voterService.addNewOffer(addOfferDtoRequest2);
        offerId2 = offerResponse2.getOfferId();
        AddOfferDtoRequest addOfferDtoRequest3 = new AddOfferDtoRequest(token3, content3);
        AddOfferDtoResponse offerResponse3 = voterService.addNewOffer(addOfferDtoRequest3);
        offerId3 = offerResponse3.getOfferId();
        //act
        AddCandidateDtoRequest request1 = new AddCandidateDtoRequest(token1, "1");
        AddCandidateDtoResponse response1 = voterService.registerNewCandidate(request1);
        String candidateId1 = response1.getId();
        AddCandidateDtoRequest request2 = new AddCandidateDtoRequest(token3, "3");
        AddCandidateDtoResponse response2 = voterService.registerNewCandidate(request2);
        String candidateId2 = response2.getId();
        //assert
        assertEquals(2, dbms.getCandidateMap().size());
        assertEquals(2, dbms.getProgramMap().size());
        assertEquals("use your brain!", dbms.getProgramMap().get("1").getOffer().getContent());
        assertEquals("do not be shy!", dbms.getProgramMap().get("2").getOffer().getContent());
        assertEquals("1", candidateId1);
        assertEquals("2", candidateId2);
        assertEquals(dbms.getVoterMap().get("1"), dbms.getCandidateMap().get("1").getVoter());
        assertEquals(dbms.getVoterMap().get("3"), dbms.getCandidateMap().get("2").getVoter());
    }

    @Test
    public void testNominateCandidate() throws Exception {
        //arrange
        RegisterVoterDtoRequest registerVoterDtoRequest1 = new RegisterVoterDtoRequest(fullName1, address1, account1);
        RegisterVoterDtoResponse voterResponse1 = voterService.registerNewVoter(registerVoterDtoRequest1);
        token1 = voterResponse1.getToken();
        RegisterVoterDtoRequest registerVoterDtoRequest2 = new RegisterVoterDtoRequest(fullName2, address2, account2);
        RegisterVoterDtoResponse voterResponse2 = voterService.registerNewVoter(registerVoterDtoRequest2);
        token2 = voterResponse2.getToken();
        RegisterVoterDtoRequest registerVoterDtoRequest3 = new RegisterVoterDtoRequest(fullName3, address3, account3);
        RegisterVoterDtoResponse voterResponse3 = voterService.registerNewVoter(registerVoterDtoRequest3);
        token3 = voterResponse3.getToken();
        AddOfferDtoRequest addOfferDtoRequest1 = new AddOfferDtoRequest(token1, content1);
        AddOfferDtoResponse offerResponse1 = voterService.addNewOffer(addOfferDtoRequest1);
        offerId1 = offerResponse1.getOfferId();
        AddOfferDtoRequest addOfferDtoRequest2 = new AddOfferDtoRequest(token2, content2);
        AddOfferDtoResponse offerResponse2 = voterService.addNewOffer(addOfferDtoRequest2);
        offerId2 = offerResponse2.getOfferId();
        AddOfferDtoRequest addOfferDtoRequest3 = new AddOfferDtoRequest(token3, content3);
        AddOfferDtoResponse offerResponse3 = voterService.addNewOffer(addOfferDtoRequest3);
        offerId3 = offerResponse3.getOfferId();
        //act
        AddCandidateDtoRequest request1 = new AddCandidateDtoRequest(token1, "2");
        AddCandidateDtoResponse response1 = voterService.registerNewCandidate(request1);
        String nominateCandidateRequestId1 = response1.getId();
        AddCandidateDtoRequest request2 = new AddCandidateDtoRequest(token2, "1");
        AddCandidateDtoResponse response2 = voterService.registerNewCandidate(request2);
        String nominateCandidateRequestId2 = response2.getId();
        //assert
        assertEquals(0, dbms.getCandidateMap().size());
        assertEquals(2, dbms.getNominateRequests().size());
        assertEquals("1", nominateCandidateRequestId1);
        assertEquals("2", nominateCandidateRequestId2);
    }

    @Test
    public void testCheckNominateRequests() throws Exception {
        //arrange
        RegisterVoterDtoRequest registerVoterDtoRequest1 = new RegisterVoterDtoRequest(fullName1, address1, account1);
        RegisterVoterDtoResponse voterResponse1 = voterService.registerNewVoter(registerVoterDtoRequest1);
        token1 = voterResponse1.getToken();
        RegisterVoterDtoRequest registerVoterDtoRequest2 = new RegisterVoterDtoRequest(fullName2, address2, account2);
        RegisterVoterDtoResponse voterResponse2 = voterService.registerNewVoter(registerVoterDtoRequest2);
        token2 = voterResponse2.getToken();
        RegisterVoterDtoRequest registerVoterDtoRequest3 = new RegisterVoterDtoRequest(fullName3, address3, account3);
        RegisterVoterDtoResponse voterResponse3 = voterService.registerNewVoter(registerVoterDtoRequest3);
        token3 = voterResponse3.getToken();
        AddOfferDtoRequest addOfferDtoRequest1 = new AddOfferDtoRequest(token1, content1);
        AddOfferDtoResponse offerResponse1 = voterService.addNewOffer(addOfferDtoRequest1);
        offerId1 = offerResponse1.getOfferId();
        AddOfferDtoRequest addOfferDtoRequest2 = new AddOfferDtoRequest(token2, content2);
        AddOfferDtoResponse offerResponse2 = voterService.addNewOffer(addOfferDtoRequest2);
        offerId2 = offerResponse2.getOfferId();
        AddOfferDtoRequest addOfferDtoRequest3 = new AddOfferDtoRequest(token3, content3);
        AddOfferDtoResponse offerResponse3 = voterService.addNewOffer(addOfferDtoRequest3);
        offerId3 = offerResponse3.getOfferId();
        AddCandidateDtoRequest request1 = new AddCandidateDtoRequest(token1, "2");
        AddCandidateDtoResponse response1 = voterService.registerNewCandidate(request1);
        //act
        CheckRequestsDtoRequest request = new CheckRequestsDtoRequest(token2);
        CheckRequestsDtoResponse response = voterService.checkRequests(request);
        List<CandidateNominateRequest> resultList = response.getRequests();
        //assert
        assertEquals(0, dbms.getCandidateMap().size());
        assertEquals(1, dbms.getNominateRequests().size());
        assertEquals(resultList.get(0), dbms.getNominateRequests().get("1"));
    }

    @Test
    public void testPositiveAnswerToNominateRequest() throws Exception {
        //arrange
        RegisterVoterDtoRequest registerVoterDtoRequest1 = new RegisterVoterDtoRequest(fullName1, address1, account1);
        RegisterVoterDtoResponse voterResponse1 = voterService.registerNewVoter(registerVoterDtoRequest1);
        token1 = voterResponse1.getToken();
        RegisterVoterDtoRequest registerVoterDtoRequest2 = new RegisterVoterDtoRequest(fullName2, address2, account2);
        RegisterVoterDtoResponse voterResponse2 = voterService.registerNewVoter(registerVoterDtoRequest2);
        token2 = voterResponse2.getToken();
        RegisterVoterDtoRequest registerVoterDtoRequest3 = new RegisterVoterDtoRequest(fullName3, address3, account3);
        RegisterVoterDtoResponse voterResponse3 = voterService.registerNewVoter(registerVoterDtoRequest3);
        token3 = voterResponse3.getToken();
        AddOfferDtoRequest addOfferDtoRequest1 = new AddOfferDtoRequest(token1, content1);
        AddOfferDtoResponse offerResponse1 = voterService.addNewOffer(addOfferDtoRequest1);
        offerId1 = offerResponse1.getOfferId();
        AddOfferDtoRequest addOfferDtoRequest2 = new AddOfferDtoRequest(token2, content2);
        AddOfferDtoResponse offerResponse2 = voterService.addNewOffer(addOfferDtoRequest2);
        offerId2 = offerResponse2.getOfferId();
        AddOfferDtoRequest addOfferDtoRequest3 = new AddOfferDtoRequest(token3, content3);
        AddOfferDtoResponse offerResponse3 = voterService.addNewOffer(addOfferDtoRequest3);
        offerId3 = offerResponse3.getOfferId();
        AddCandidateDtoRequest request1 = new AddCandidateDtoRequest(token1, "2");
        AddCandidateDtoResponse response1 = voterService.registerNewCandidate(request1);
        //act
        AnswerNominateRequestDtoRequest request = new AnswerNominateRequestDtoRequest(token2, response1.getId(), true);
        AnswerNominateRequestDtoResponse response = voterService.answerToNominateRequest(request);
        String responseCandidateId = response.getCandidateId();
        String responseRequestStatus = response.getMessage();
        //assert
        assertEquals(0, dbms.getNominateRequests().size());
        assertEquals(1, dbms.getCandidateMap().size());
        assertEquals("1", responseCandidateId);
        assertEquals(ResponseStatusMessage.NOMINATE_REQUEST_REMOVED.getMessage(), responseRequestStatus);
    }

    @Test
    public void testNegativeAnswerToNominateRequest() throws Exception {
        //arrange
        RegisterVoterDtoRequest registerVoterDtoRequest1 = new RegisterVoterDtoRequest(fullName1, address1, account1);
        RegisterVoterDtoResponse voterResponse1 = voterService.registerNewVoter(registerVoterDtoRequest1);
        token1 = voterResponse1.getToken();
        RegisterVoterDtoRequest registerVoterDtoRequest2 = new RegisterVoterDtoRequest(fullName2, address2, account2);
        RegisterVoterDtoResponse voterResponse2 = voterService.registerNewVoter(registerVoterDtoRequest2);
        token2 = voterResponse2.getToken();
        RegisterVoterDtoRequest registerVoterDtoRequest3 = new RegisterVoterDtoRequest(fullName3, address3, account3);
        RegisterVoterDtoResponse voterResponse3 = voterService.registerNewVoter(registerVoterDtoRequest3);
        token3 = voterResponse3.getToken();
        AddOfferDtoRequest addOfferDtoRequest1 = new AddOfferDtoRequest(token1, content1);
        AddOfferDtoResponse offerResponse1 = voterService.addNewOffer(addOfferDtoRequest1);
        offerId1 = offerResponse1.getOfferId();
        AddOfferDtoRequest addOfferDtoRequest2 = new AddOfferDtoRequest(token2, content2);
        AddOfferDtoResponse offerResponse2 = voterService.addNewOffer(addOfferDtoRequest2);
        offerId2 = offerResponse2.getOfferId();
        AddOfferDtoRequest addOfferDtoRequest3 = new AddOfferDtoRequest(token3, content3);
        AddOfferDtoResponse offerResponse3 = voterService.addNewOffer(addOfferDtoRequest3);
        offerId3 = offerResponse3.getOfferId();
        AddCandidateDtoRequest request1 = new AddCandidateDtoRequest(token1, "2");
        AddCandidateDtoResponse response1 = voterService.registerNewCandidate(request1);
        //act
        AnswerNominateRequestDtoRequest request = new AnswerNominateRequestDtoRequest(token2, response1.getId(), false);
        AnswerNominateRequestDtoResponse response = voterService.answerToNominateRequest(request);
        String responseCandidateId = response.getCandidateId();
        String responseRequestStatus = response.getMessage();
        //assert
        assertEquals(0, dbms.getNominateRequests().size());
        assertEquals(0, dbms.getCandidateMap().size());
        assertEquals("0", responseCandidateId);
        assertEquals(ResponseStatusMessage.NOMINATE_REQUEST_REMOVED.getMessage(), responseRequestStatus);
    }

    @Test
    public void testUpdateCandidateProgram() throws Exception {
        //arrange
        RegisterVoterDtoRequest registerVoterDtoRequest1 = new RegisterVoterDtoRequest(fullName1, address1, account1);
        RegisterVoterDtoResponse voterResponse1 = voterService.registerNewVoter(registerVoterDtoRequest1);
        token1 = voterResponse1.getToken();
        RegisterVoterDtoRequest registerVoterDtoRequest2 = new RegisterVoterDtoRequest(fullName2, address2, account2);
        RegisterVoterDtoResponse voterResponse2 = voterService.registerNewVoter(registerVoterDtoRequest2);
        token2 = voterResponse2.getToken();
        RegisterVoterDtoRequest registerVoterDtoRequest3 = new RegisterVoterDtoRequest(fullName3, address3, account3);
        RegisterVoterDtoResponse voterResponse3 = voterService.registerNewVoter(registerVoterDtoRequest3);
        token3 = voterResponse3.getToken();
        AddOfferDtoRequest addOfferDtoRequest1 = new AddOfferDtoRequest(token1, content1);
        AddOfferDtoResponse offerResponse1 = voterService.addNewOffer(addOfferDtoRequest1);
        offerId1 = offerResponse1.getOfferId();
        AddOfferDtoRequest addOfferDtoRequest2 = new AddOfferDtoRequest(token2, content2);
        AddOfferDtoResponse offerResponse2 = voterService.addNewOffer(addOfferDtoRequest2);
        offerId2 = offerResponse2.getOfferId();
        AddOfferDtoRequest addOfferDtoRequest3 = new AddOfferDtoRequest(token3, content3);
        AddOfferDtoResponse offerResponse3 = voterService.addNewOffer(addOfferDtoRequest3);
        offerId3 = offerResponse3.getOfferId();
        AddCandidateDtoRequest addCandidateRequest1 = new AddCandidateDtoRequest(token1, "1");
        AddCandidateDtoResponse addCandidateResponse1 = voterService.registerNewCandidate(addCandidateRequest1);
        String candidateId1 = addCandidateResponse1.getId();
        //act
        UpdateCandidateProgramDtoRequest request1 = new UpdateCandidateProgramDtoRequest(token1, offerId2, true);
        UpdateCandidateProgramDtoResponse response1 = voterService.updateCandidateProgram(request1);
        String responseMessage1 = response1.getMessage();
        UpdateCandidateProgramDtoRequest request2 = new UpdateCandidateProgramDtoRequest(token1, offerId3, true);
        UpdateCandidateProgramDtoResponse response2 = voterService.updateCandidateProgram(request2);
        String responseMessage2 = response2.getMessage();
        UpdateCandidateProgramDtoRequest request3 = new UpdateCandidateProgramDtoRequest(token1, offerId3, false);
        UpdateCandidateProgramDtoResponse response3 = voterService.updateCandidateProgram(request3);
        String responseMessage3 = response3.getMessage();
        UpdateCandidateProgramDtoRequest request4 = new UpdateCandidateProgramDtoRequest(token1, offerId1, false);
        String responseMessage4 = "Test failed!";
        try {
            voterService.updateCandidateProgram(request4);
        } catch (CandidateException ex) {
            responseMessage4 = ex.getErrorCode().getErrorString();
        }
        //assert
        assertEquals(3, dbms.getOfferMap().size());
        assertEquals(2, dbms.getProgramMap().size());
        assertTrue(dbms.getProgramMap().values().contains(candidateProgramDao.getCandidateProgramById(candidateProgramDao.getCandidateProgramIdByCandidateIdAndOfferId(candidateId1, offerId1))));
        assertTrue(dbms.getProgramMap().values().contains(candidateProgramDao.getCandidateProgramById(candidateProgramDao.getCandidateProgramIdByCandidateIdAndOfferId(candidateId1, offerId2))));
        assertFalse(dbms.getProgramMap().values().contains(candidateProgramDao.getCandidateProgramById(candidateProgramDao.getCandidateProgramIdByCandidateIdAndOfferId(candidateId1, offerId3))));
        assertEquals(ResponseStatusMessage.OFFER_ADDED_TO_CANDIDATE_PROGRAM.getMessage(), responseMessage1);
        assertEquals(ResponseStatusMessage.OFFER_ADDED_TO_CANDIDATE_PROGRAM.getMessage(), responseMessage2);
        assertEquals(ResponseStatusMessage.OFFER_REMOVED_FROM_CANDIDATE_PROGRAM.getMessage(), responseMessage3);
        assertEquals(CandidateError.AUTHOR_REJECTS_HIS_OFFERS.getErrorString(), responseMessage4);
    }

    @Test
    public void testWithdrawCandidate() throws Exception {
        //arrange
        RegisterVoterDtoRequest registerVoterDtoRequest1 = new RegisterVoterDtoRequest(fullName1, address1, account1);
        RegisterVoterDtoResponse voterResponse1 = voterService.registerNewVoter(registerVoterDtoRequest1);
        token1 = voterResponse1.getToken();
        RegisterVoterDtoRequest registerVoterDtoRequest2 = new RegisterVoterDtoRequest(fullName2, address2, account2);
        RegisterVoterDtoResponse voterResponse2 = voterService.registerNewVoter(registerVoterDtoRequest2);
        token2 = voterResponse2.getToken();
        RegisterVoterDtoRequest registerVoterDtoRequest3 = new RegisterVoterDtoRequest(fullName3, address3, account3);
        RegisterVoterDtoResponse voterResponse3 = voterService.registerNewVoter(registerVoterDtoRequest3);
        token3 = voterResponse3.getToken();
        AddOfferDtoRequest addOfferDtoRequest1 = new AddOfferDtoRequest(token1, content1);
        AddOfferDtoResponse offerResponse1 = voterService.addNewOffer(addOfferDtoRequest1);
        offerId1 = offerResponse1.getOfferId();
        AddOfferDtoRequest addOfferDtoRequest2 = new AddOfferDtoRequest(token2, content2);
        AddOfferDtoResponse offerResponse2 = voterService.addNewOffer(addOfferDtoRequest2);
        offerId2 = offerResponse2.getOfferId();
        AddOfferDtoRequest addOfferDtoRequest3 = new AddOfferDtoRequest(token3, content3);
        AddOfferDtoResponse offerResponse3 = voterService.addNewOffer(addOfferDtoRequest3);
        offerId3 = offerResponse3.getOfferId();
        AddCandidateDtoRequest addCandidateRequest1 = new AddCandidateDtoRequest(token1, "1");
        voterService.registerNewCandidate(addCandidateRequest1);
        //act
        WithdrawCandidateDtoRequest request = new WithdrawCandidateDtoRequest(token1);
        WithdrawCandidateDtoResponse response = voterService.withdrawCandidate(request);
        String responseMessage = response.getMessage();
        //assert
        assertEquals(0, dbms.getCandidateMap().size());
        assertEquals(ResponseStatusMessage.CANDIDACY_HAS_BEEN_WITHDRAWN.getMessage(), responseMessage);
    }

    @Test
    public void testGetCandidateListWithPrograms() throws Exception {
        RegisterVoterDtoRequest registerVoterDtoRequest1 = new RegisterVoterDtoRequest(fullName1, address1, account1);
        RegisterVoterDtoResponse voterResponse1 = voterService.registerNewVoter(registerVoterDtoRequest1);
        token1 = voterResponse1.getToken();
        RegisterVoterDtoRequest registerVoterDtoRequest2 = new RegisterVoterDtoRequest(fullName2, address2, account2);
        RegisterVoterDtoResponse voterResponse2 = voterService.registerNewVoter(registerVoterDtoRequest2);
        token2 = voterResponse2.getToken();
        RegisterVoterDtoRequest registerVoterDtoRequest3 = new RegisterVoterDtoRequest(fullName3, address3, account3);
        RegisterVoterDtoResponse voterResponse3 = voterService.registerNewVoter(registerVoterDtoRequest3);
        token3 = voterResponse3.getToken();
        AddOfferDtoRequest addOfferDtoRequest1 = new AddOfferDtoRequest(token1, content1);
        AddOfferDtoResponse offerResponse1 = voterService.addNewOffer(addOfferDtoRequest1);
        offerId1 = offerResponse1.getOfferId();
        AddOfferDtoRequest addOfferDtoRequest2 = new AddOfferDtoRequest(token2, content2);
        AddOfferDtoResponse offerResponse2 = voterService.addNewOffer(addOfferDtoRequest2);
        offerId2 = offerResponse2.getOfferId();
        AddOfferDtoRequest addOfferDtoRequest3 = new AddOfferDtoRequest(token3, content3);
        AddOfferDtoResponse offerResponse3 = voterService.addNewOffer(addOfferDtoRequest3);
        offerId3 = offerResponse3.getOfferId();
        AddCandidateDtoRequest addCandidateRequest1 = new AddCandidateDtoRequest(token1, "1");
        voterService.registerNewCandidate(addCandidateRequest1);
        UpdateCandidateProgramDtoRequest request1 = new UpdateCandidateProgramDtoRequest(token1, offerId2, true);
        voterService.updateCandidateProgram(request1);
        AddCandidateDtoRequest addCandidateRequest2 = new AddCandidateDtoRequest(token2, "2");
        voterService.registerNewCandidate(addCandidateRequest2);
        UpdateCandidateProgramDtoRequest request2 = new UpdateCandidateProgramDtoRequest(token2, offerId3, true);
        voterService.updateCandidateProgram(request2);
        //act
        GetCandidateListWithProgramsDtoRequest request = new GetCandidateListWithProgramsDtoRequest(token3);
        GetCandidateListWithProgramsDtoResponse response = voterService.getCandidatesWithPrograms(request);
        List<GetCandidateListWithProgramsDtoResponse.CandidateWithProgram> resultList = response.getCandidates();
        //assert
        assertEquals(dbms.getCandidateMap().get("1").getVoter().getPerson().getFullName(), resultList.get(0).getCandidateFullName());
        assertEquals(dbms.getOfferMap().get("1"), resultList.get(0).getPrograms().get(0).getOffer());
        assertEquals(dbms.getOfferMap().get("2"), resultList.get(0).getPrograms().get(1).getOffer());
        assertEquals(dbms.getCandidateMap().get("2").getVoter().getPerson().getFullName(), resultList.get(1).getCandidateFullName());
        assertEquals(dbms.getOfferMap().get("2"), resultList.get(1).getPrograms().get(0).getOffer());
        assertEquals(dbms.getOfferMap().get("3"), resultList.get(1).getPrograms().get(1).getOffer());
    }

    @Test
    public void testLogOutVoter() throws Exception {
        //arrange
        RegisterVoterDtoRequest registerVoterDtoRequest1 = new RegisterVoterDtoRequest(fullName1, address1, account1);
        RegisterVoterDtoResponse voterResponse1 = voterService.registerNewVoter(registerVoterDtoRequest1);
        token1 = voterResponse1.getToken();
        RegisterVoterDtoRequest registerVoterDtoRequest2 = new RegisterVoterDtoRequest(fullName2, address2, account2);
        RegisterVoterDtoResponse voterResponse2 = voterService.registerNewVoter(registerVoterDtoRequest2);
        token2 = voterResponse2.getToken();
        RegisterVoterDtoRequest registerVoterDtoRequest3 = new RegisterVoterDtoRequest(fullName3, address3, account3);
        RegisterVoterDtoResponse voterResponse3 = voterService.registerNewVoter(registerVoterDtoRequest3);
        token3 = voterResponse3.getToken();
        AddOfferDtoRequest addOfferDtoRequest1 = new AddOfferDtoRequest(token1, content1);
        AddOfferDtoResponse offerResponse1 = voterService.addNewOffer(addOfferDtoRequest1);
        offerId1 = offerResponse1.getOfferId();
        AddOfferDtoRequest addOfferDtoRequest2 = new AddOfferDtoRequest(token2, content2);
        AddOfferDtoResponse offerResponse2 = voterService.addNewOffer(addOfferDtoRequest2);
        offerId2 = offerResponse2.getOfferId();
        AddOfferDtoRequest addOfferDtoRequest3 = new AddOfferDtoRequest(token3, content3);
        AddOfferDtoResponse offerResponse3 = voterService.addNewOffer(addOfferDtoRequest3);
        offerId3 = offerResponse3.getOfferId();
        RateOfferDtoRequest rateOfferDtoRequest1 = new RateOfferDtoRequest(token1, offerId2, "2");
        voterService.rateOffer(rateOfferDtoRequest1);
        RateOfferDtoRequest rateOfferDtoRequest2 = new RateOfferDtoRequest(token1, offerId3, "2");
        voterService.rateOffer(rateOfferDtoRequest2);
        RateOfferDtoRequest rateOfferDtoRequest3 = new RateOfferDtoRequest(token2, offerId1, "3");
        voterService.rateOffer(rateOfferDtoRequest3);
        RateOfferDtoRequest rateOfferDtoRequest4 = new RateOfferDtoRequest(token2, offerId3, "3");
        voterService.rateOffer(rateOfferDtoRequest4);
        RateOfferDtoRequest rateOfferDtoRequest5 = new RateOfferDtoRequest(token3, offerId1, "4");
        voterService.rateOffer(rateOfferDtoRequest5);
        RateOfferDtoRequest rateOfferDtoRequest6 = new RateOfferDtoRequest(token3, offerId2, "4");
        voterService.rateOffer(rateOfferDtoRequest6);
        AddCandidateDtoRequest request1 = new AddCandidateDtoRequest(token1, "1");
        voterService.registerNewCandidate(request1);
        AddCandidateDtoRequest request2 = new AddCandidateDtoRequest(token1, "2");
        voterService.registerNewCandidate(request2);
        //act
        LogOutDtoRequest logOutRequest1 = new LogOutDtoRequest(token1);
        String responseMessage1 = "Test failed!";
        try {
            voterService.logOutVoter(logOutRequest1);
        } catch (CandidateException ex) {
            responseMessage1 = ex.getErrorCode().getErrorString();
        }
        LogOutDtoRequest logOutRequest2 = new LogOutDtoRequest(token2);
        LogOutDtoResponse logOutResponse2 = voterService.logOutVoter(logOutRequest2);
        String responseMessage2 = logOutResponse2.getMessage();
        //assert
        assertEquals(ResponseStatusMessage.SUCCESSFULLY_LOGGED_OUT.getMessage(), responseMessage2);
        assertEquals(CandidateError.CANDIDACY_ACTIVE.getErrorString(), responseMessage1);
        assertEquals(2, dbms.getTokenMap().size());
        assertEquals(3, dbms.getVoterMap().size());
        assertTrue(dbms.getVoterMap().containsKey("1"));
        assertTrue(dbms.getVoterMap().containsKey("2"));
        assertTrue(dbms.getVoterMap().containsKey("3"));
        assertEquals(3, dbms.getOfferMap().size());
        assertTrue(dbms.getOfferMap().containsKey("1"));
        assertFalse(dbms.getOfferMap().get("2").isCopyrightsActive());
        assertTrue(dbms.getOfferMap().containsKey("3"));
        assertEquals(6, dbms.getRatingMap().size());
        assertTrue(dbms.getRatingMap().containsKey("1"));
        assertFalse(dbms.getRatingMap().containsKey("2"));
        assertTrue(dbms.getRatingMap().containsKey("3"));
        assertTrue(dbms.getRatingMap().containsKey("4"));
        assertTrue(dbms.getRatingMap().containsKey("5"));
        assertFalse(dbms.getRatingMap().containsKey("6"));
        assertFalse(dbms.getRatingMap().containsKey("7"));
        assertTrue(dbms.getRatingMap().containsKey("8"));
        assertTrue(dbms.getRatingMap().containsKey("9"));
        assertEquals(0, dbms.getNominateRequests().size());
        assertFalse(dbms.getNominateRequests().containsKey("1"));
        assertEquals(1, dbms.getCandidateMap().size());
        assertTrue(dbms.getCandidateMap().containsKey("1"));
    }

    @Test
    public void testLogOnVoter() throws Exception {
        //arrange
        RegisterVoterDtoRequest registerVoterDtoRequest1 = new RegisterVoterDtoRequest(fullName1, address1, account1);
        RegisterVoterDtoResponse voterResponse1 = voterService.registerNewVoter(registerVoterDtoRequest1);
        token1 = voterResponse1.getToken();
        RegisterVoterDtoRequest registerVoterDtoRequest2 = new RegisterVoterDtoRequest(fullName2, address2, account2);
        RegisterVoterDtoResponse voterResponse2 = voterService.registerNewVoter(registerVoterDtoRequest2);
        token2 = voterResponse2.getToken();
        RegisterVoterDtoRequest registerVoterDtoRequest3 = new RegisterVoterDtoRequest(fullName3, address3, account3);
        RegisterVoterDtoResponse voterResponse3 = voterService.registerNewVoter(registerVoterDtoRequest3);
        token3 = voterResponse3.getToken();
        String oldToken = token2;
        LogOutDtoRequest logOutRequest2 = new LogOutDtoRequest(token2);
        voterService.logOutVoter(logOutRequest2);
        //act
        LogOnDtoRequest request = new LogOnDtoRequest("PETYA", "654321");
        LogOnDtoResponse response = voterService.logOnVoter(request);
        String responseMessage = response.getMessage();
        String newToken = response.getToken();
        //assert
        assertEquals(3, dbms.getTokenMap().size());
        assertEquals(3, dbms.getVoterMap().size());
        assertEquals(3, dbms.getPersonMap().size());
        assertEquals(ResponseStatusMessage.SUCCESSFULLY_LOGGED_ON.getMessage(), responseMessage);
        assertEquals(voterDao.getVoterById("2").getToken(), newToken);
        assertTrue(dbms.getVoterMap().containsKey("1"));
        assertTrue(dbms.getVoterMap().containsKey("2"));
        assertTrue(dbms.getVoterMap().containsKey("3"));
        assertNotEquals(oldToken, newToken);
    }
}
