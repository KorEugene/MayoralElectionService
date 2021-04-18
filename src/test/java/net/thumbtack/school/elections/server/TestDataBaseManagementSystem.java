package net.thumbtack.school.elections.server;

import net.thumbtack.school.elections.model.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.Map;

import static org.junit.Assert.*;

public class TestDataBaseManagementSystem {

    private static final int AUTHOR_RATING_VALUE = 5;

    private static DataBase db = DataBase.getInstance();
    private static DataBaseManagementSystem dbms = DataBaseManagementSystem.getInstance();
    private static FullName fullName = new FullName("Ivan", "Ivanov", "Ivanovich");
    private static Address address = new Address("Fedora Abramova", "15", "27");
    private static Account account = new Account("IVA", "123456");
    private static Voter voter = new Voter(new Person(fullName, address, account));
    private static FullName fullName2 = new FullName("Petr", "Petrov", "Petrovich");
    private static Address address2 = new Address("Primorskaya", "35", "4");
    private static Account account2 = new Account("PETYA", "654321");
    private static Voter voter2 = new Voter(new Person(fullName2, address2, account2));

    @Before
    public void generateTokens() {
        voter.setToken(TokenGenerator.generateNewToken());
        voter2.setToken(TokenGenerator.generateNewToken());
    }

    @After
    public void cleanData() {
        DataBase.clearInstance();
    }

    @Test
    public void shouldInsertNewTokenToDataBaseAndVoter() throws Exception {
        //arrange
        String voterId = dbms.insertNewVoterToDataBase(voter);
        String token = voter.getToken();
        dbms.removeTokenFromDataBase(token);
        //act
        String newToken = TokenGenerator.generateNewToken();
        String message = dbms.insertNewTokenToDataBase(voterId, newToken);
        //assert
        assertEquals(1, db.getTokenMap().size());
        assertEquals(1, db.getVoterMap().size());
        assertEquals(ResponseStatusMessage.TOKEN_UPDATED.getMessage(), message);
        assertEquals(voter.getToken(), newToken);
        assertNotEquals(voter.getToken(), token);
    }

    @Test
    public void shouldRemoveTokenFromDataBase() throws Exception {
        //arrange
        String voterId = dbms.insertNewVoterToDataBase(voter);
        String token = voter.getToken();
        //act
        String message = dbms.removeTokenFromDataBase(token);
        //assert
        assertEquals(0, db.getTokenMap().size());
        assertEquals(1, db.getVoterMap().size());
        assertNotNull(voter.getToken());
    }

    @Test
    public void shouldAddNewVoterToDataBaseAndReturnVoterId() throws Exception {
        //act
        String voterId = dbms.insertNewVoterToDataBase(voter);
        //assert
        assertEquals("1", voterId);
        assertEquals(1, db.getVoterMap().size());
    }

    @Test
    public void shouldReturnVoterIdCounterValueFromDataBase() throws Exception {
        //act
        int resultId = dbms.getVoterId();
        //assert
        assertEquals(1, resultId);
    }

    @Test
    public void shouldAutoIncrementVoterIdCounterAfterInsertNewVoter() throws Exception {
        //arrange
        String voterId = dbms.insertNewVoterToDataBase(voter);
        //act
        String resultId = String.valueOf(dbms.getVoterId());
        //assert
        assertEquals("2", resultId);
        assertNotEquals(voterId, resultId);
    }

    @Test
    public void shouldAddNewOfferAndAuthorRatingToDataBaseAndReturnOfferId() throws Exception {
        //arrange
        String content = "TEST";
        String voterId = dbms.insertNewVoterToDataBase(voter);
        //act
        Offer offer = new Offer(voterId, content);
        String offerId = dbms.insertNewOfferToDataBase(offer);
        Rating rating = new Rating(offerId, voterId, AUTHOR_RATING_VALUE);
        String ratingId = dbms.insertNewRatingToDataBase(rating);
        //assert
        assertEquals(1, db.getVoterMap().size());
        assertEquals(1, db.getOfferMap().size());
        assertEquals(1, db.getRatingMap().size());
        assertEquals("1", voterId);
        assertEquals("1", offerId);
        assertEquals("1", ratingId);
    }

    @Test
    public void shouldReturnOfferIdCounterValueFromDataBase() throws Exception {
        //act
        int resultId = dbms.getOfferId();
        //assert
        assertEquals(1, resultId);
    }

    @Test
    public void shouldAutoIncrementOfferIdCounterAfterInsertNewOffer() throws Exception {
        //arrange
        String content = "TEST";
        String voterId = dbms.insertNewVoterToDataBase(voter);
        Offer offer = new Offer(voterId, content);
        String offerId = dbms.insertNewOfferToDataBase(offer);
        //act
        String resultId = String.valueOf(dbms.getOfferId());
        //assert
        assertEquals("2", resultId);
        assertNotEquals(offerId, resultId);
    }

    @Test
    public void shouldReturnRatingIdCounterValueFromDataBase() throws Exception {
        //act
        int resultId = dbms.getRatingId();
        //assert
        assertEquals(1, resultId);
    }

    @Test
    public void shouldAutoIncrementRatingIdCounterAfterInsertNewRating() throws Exception {
        //arrange
        String content = "TEST";
        String voterId = dbms.insertNewVoterToDataBase(voter);
        Offer offer = new Offer(voterId, content);
        String offerId = dbms.insertNewOfferToDataBase(offer);
        Rating rating = new Rating(offerId, voterId, AUTHOR_RATING_VALUE);
        String ratingId = dbms.insertNewRatingToDataBase(rating);
        //act
        String resultId = String.valueOf(dbms.getOfferId());
        //assert
        assertEquals("2", resultId);
        assertNotEquals(ratingId, resultId);
    }

    @Test
    public void shouldAddNewRatingToDataBase() throws Exception {
        //arrange
        String content = "TEST";
        String voterId = dbms.insertNewVoterToDataBase(voter);
        Offer offer = new Offer(voterId, content);
        String offerId = dbms.insertNewOfferToDataBase(offer);
        Rating rating = new Rating(offerId, voterId, AUTHOR_RATING_VALUE);
        dbms.insertNewRatingToDataBase(rating);
        //act
        String voterId2 = dbms.insertNewVoterToDataBase(voter2);
        Rating rating2 = new Rating(offerId, voterId2, 3);
        dbms.insertNewRatingToDataBase(rating2);
        //assert
        assertEquals(2, db.getVoterMap().size());
        assertEquals(1, db.getOfferMap().size());
        assertEquals(2, db.getRatingMap().size());
        assertEquals(offerId, rating.getOfferId());
        assertEquals(offerId, rating2.getOfferId());
        assertEquals(voterId, rating.getAuthorId());
        assertEquals(voterId2, rating2.getAuthorId());
    }

    @Test
    public void shouldDeleteRatingFromDataBase() throws Exception {
        //arrange
        String content = "TEST";
        String voterId = dbms.insertNewVoterToDataBase(voter);
        Offer offer = new Offer(voterId, content);
        String offerId = dbms.insertNewOfferToDataBase(offer);
        Rating rating = new Rating(offerId, voterId, AUTHOR_RATING_VALUE);
        dbms.insertNewRatingToDataBase(rating);
        String voterId2 = dbms.insertNewVoterToDataBase(voter2);
        Rating rating2 = new Rating(offerId, voterId2, 3);
        String ratingId2 = dbms.insertNewRatingToDataBase(rating2);
        //act
        String message = dbms.deleteRatingFromDataBase(ratingId2);
        //assert
        assertEquals(2, db.getVoterMap().size());
        assertEquals(1, db.getOfferMap().size());
        assertEquals(1, db.getRatingMap().size());
        assertEquals(ResponseStatusMessage.RATING_STATUS_DELETED.getMessage(), message);
    }

    @Test
    public void shouldUpdateExistingRating() throws Exception {
        //arrange
        String content = "TEST";
        String voterId = dbms.insertNewVoterToDataBase(voter);
        Offer offer = new Offer(voterId, content);
        String offerId = dbms.insertNewOfferToDataBase(offer);
        Rating rating = new Rating(offerId, voterId, AUTHOR_RATING_VALUE);
        dbms.insertNewRatingToDataBase(rating);
        String voterId2 = dbms.insertNewVoterToDataBase(voter2);
        Rating rating2 = new Rating(offerId, voterId2, 3);
        String ratingId2 = dbms.insertNewRatingToDataBase(rating2);
        //act
        String message = dbms.updateRatingInDataBase(ratingId2, 4);
        //assert
        assertEquals(2, db.getVoterMap().size());
        assertEquals(1, db.getOfferMap().size());
        assertEquals(2, db.getRatingMap().size());
        assertEquals(ResponseStatusMessage.RATING_STATUS_UPDATED.getMessage(), message);
    }

    @Test
    public void shouldInsertNewCandidateToDataBase() throws Exception {
        //arrange
        String content = "TEST";
        String voterId = dbms.insertNewVoterToDataBase(voter);
        Offer offer = new Offer(voterId, content);
        String offerId = dbms.insertNewOfferToDataBase(offer);
        Rating rating = new Rating(offerId, voterId, AUTHOR_RATING_VALUE);
        dbms.insertNewRatingToDataBase(rating);
        //act
        Candidate candidate = new Candidate(voter);
        String candidateId = dbms.insertNewCandidateToDataBase(candidate);
        //assert
        assertEquals(1, db.getCandidateMap().size());
        assertEquals("1", candidateId);
    }

    @Test
    public void shouldDeleteCandidateFromDataBase() throws Exception {
        //arrange
        String content = "TEST";
        String voterId = dbms.insertNewVoterToDataBase(voter);
        Offer offer = new Offer(voterId, content);
        String offerId = dbms.insertNewOfferToDataBase(offer);
        Rating rating = new Rating(offerId, voterId, AUTHOR_RATING_VALUE);
        dbms.insertNewRatingToDataBase(rating);
        Candidate candidate = new Candidate(voter);
        String candidateId = dbms.insertNewCandidateToDataBase(candidate);
        //act
        String message = dbms.deleteCandidateFromDataBase(candidateId);
        //assert
        assertEquals(0, db.getCandidateMap().size());
        assertEquals(ResponseStatusMessage.CANDIDACY_HAS_BEEN_WITHDRAWN.getMessage(), message);
    }

    @Test
    public void shouldReturnCandidateIdCounterValueFromDataBase() throws Exception {
        //act
        int resultId = dbms.getCandidateId();
        //assert
        assertEquals(1, resultId);
    }

    @Test
    public void shouldAutoIncrementCandidateIdCounterAfterInsertNewCandidate() throws Exception {
        //arrange
        String content = "TEST";
        String voterId = dbms.insertNewVoterToDataBase(voter);
        Offer offer = new Offer(voterId, content);
        String offerId = dbms.insertNewOfferToDataBase(offer);
        Rating rating = new Rating(offerId, voterId, AUTHOR_RATING_VALUE);
        dbms.insertNewRatingToDataBase(rating);
        Candidate candidate = new Candidate(voter);
        String candidateId = dbms.insertNewCandidateToDataBase(candidate);
        //act
        String resultId = String.valueOf(dbms.getCandidateId());
        //assert
        assertEquals("2", resultId);
        assertNotEquals(candidateId, resultId);
    }

    @Test
    public void shouldInsertNewNominateRequestToDataBase() throws Exception {
        //arrange
        String content = "TEST";
        String voterId = dbms.insertNewVoterToDataBase(voter);
        Offer offer = new Offer(voterId, content);
        String offerId = dbms.insertNewOfferToDataBase(offer);
        Rating rating = new Rating(offerId, voterId, AUTHOR_RATING_VALUE);
        dbms.insertNewRatingToDataBase(rating);
        String voterId2 = dbms.insertNewVoterToDataBase(voter2);
        Offer offer2 = new Offer(voterId2, content);
        String offerId2 = dbms.insertNewOfferToDataBase(offer2);
        Rating rating2 = new Rating(offerId2, voterId2, AUTHOR_RATING_VALUE);
        dbms.insertNewRatingToDataBase(rating2);
        //act
        CandidateNominateRequest candidateNominateRequest = new CandidateNominateRequest(voterId2, voterId);
        String nominateRequestId = dbms.insertNewNominateRequest(candidateNominateRequest);
        //assert
        assertEquals(1, db.getNominateRequests().size());
        assertEquals("1", nominateRequestId);
    }

    @Test
    public void shouldDeleteNominateRequestFromDataBase() throws Exception {
        //arrange
        String content = "TEST";
        String voterId = dbms.insertNewVoterToDataBase(voter);
        Offer offer = new Offer(voterId, content);
        String offerId = dbms.insertNewOfferToDataBase(offer);
        Rating rating = new Rating(offerId, voterId, AUTHOR_RATING_VALUE);
        dbms.insertNewRatingToDataBase(rating);
        String voterId2 = dbms.insertNewVoterToDataBase(voter2);
        Offer offer2 = new Offer(voterId2, content);
        String offerId2 = dbms.insertNewOfferToDataBase(offer2);
        Rating rating2 = new Rating(offerId2, voterId2, AUTHOR_RATING_VALUE);
        dbms.insertNewRatingToDataBase(rating2);
        CandidateNominateRequest candidateNominateRequest = new CandidateNominateRequest(voterId2, voterId);
        String nominateRequestId = dbms.insertNewNominateRequest(candidateNominateRequest);
        //act
        String message = dbms.deleteNominateRequest(nominateRequestId);
        //assert
        assertEquals(0, db.getNominateRequests().size());
        assertEquals(ResponseStatusMessage.NOMINATE_REQUEST_REMOVED.getMessage(), message);
    }

    @Test
    public void shouldReturnNominateRequestIdCounterValueFromDataBase() throws Exception {
        //act
        int resultId = dbms.getNominateRequestId();
        //assert
        assertEquals(1, resultId);
    }

    @Test
    public void shouldAutoIncrementNominateRequestIdCounterAfterInsertNewRating() throws Exception {
        //arrange
        String content = "TEST";
        String voterId = dbms.insertNewVoterToDataBase(voter);
        Offer offer = new Offer(voterId, content);
        String offerId = dbms.insertNewOfferToDataBase(offer);
        Rating rating = new Rating(offerId, voterId, AUTHOR_RATING_VALUE);
        String ratingId = dbms.insertNewRatingToDataBase(rating);
        String voterId2 = dbms.insertNewVoterToDataBase(voter2);
        Offer offer2 = new Offer(voterId2, content);
        String offerId2 = dbms.insertNewOfferToDataBase(offer2);
        Rating rating2 = new Rating(offerId2, voterId2, AUTHOR_RATING_VALUE);
        dbms.insertNewRatingToDataBase(rating2);
        CandidateNominateRequest candidateNominateRequest = new CandidateNominateRequest(voterId2, voterId);
        dbms.insertNewNominateRequest(candidateNominateRequest);
        //act
        String resultId = String.valueOf(dbms.getNominateRequestId());
        //assert
        assertEquals("2", resultId);
        assertNotEquals(ratingId, resultId);
    }

    @Test
    public void shouldInsertNewCandidateProgramToDataBase() throws Exception {
        //arrange
        String content = "TEST";
        String voterId = dbms.insertNewVoterToDataBase(voter);
        Offer offer = new Offer(voterId, content);
        String offerId = dbms.insertNewOfferToDataBase(offer);
        Rating rating = new Rating(offerId, voterId, AUTHOR_RATING_VALUE);
        dbms.insertNewRatingToDataBase(rating);
        Candidate candidate = new Candidate(voter);
        String candidateId = dbms.insertNewCandidateToDataBase(candidate);
        //act
        CandidateProgram candidateProgram = new CandidateProgram(candidateId, offer);
        String candidateProgramId = dbms.insertCandidateProgram(candidateProgram);
        //assert
        assertEquals(1, db.getProgramMap().size());
        assertEquals("1", candidateProgramId);
    }

    @Test
    public void shouldDeleteCandidateProgramFromDataBase() throws Exception {
        //arrange
        String content = "TEST";
        String content2 = "TEST2";
        String voterId = dbms.insertNewVoterToDataBase(voter);
        Offer offer = new Offer(voterId, content);
        String offerId = dbms.insertNewOfferToDataBase(offer);
        Rating rating = new Rating(offerId, voterId, AUTHOR_RATING_VALUE);
        dbms.insertNewRatingToDataBase(rating);
        Offer offer2 = new Offer(voterId, content2);
        String offerId2 = dbms.insertNewOfferToDataBase(offer2);
        Rating rating2 = new Rating(offerId2, voterId, AUTHOR_RATING_VALUE);
        dbms.insertNewRatingToDataBase(rating2);
        Candidate candidate = new Candidate(voter);
        String candidateId = dbms.insertNewCandidateToDataBase(candidate);
        CandidateProgram candidateProgram = new CandidateProgram(candidateId, offer);
        CandidateProgram candidateProgram2 = new CandidateProgram(candidateId, offer2);
        dbms.insertCandidateProgram(candidateProgram);
        String candidateProgramId2 = dbms.insertCandidateProgram(candidateProgram2);
        //act
        String message = dbms.deleteCandidateProgram(candidateProgramId2);
        //assert
        assertEquals(1, db.getProgramMap().size());
        assertEquals(ResponseStatusMessage.OFFER_REMOVED_FROM_CANDIDATE_PROGRAM.getMessage(), message);
    }

    @Test
    public void shouldReturnCandidateProgramIdCounterValueFromDataBase() throws Exception {
        //act
        int resultId = dbms.getProgramId();
        //assert
        assertEquals(1, resultId);
    }

    @Test
    public void shouldAutoIncrementCandidateProgramIdCounterAfterInsertCandidateProgram() throws Exception {
        //arrange
        String content = "TEST";
        String voterId = dbms.insertNewVoterToDataBase(voter);
        Offer offer = new Offer(voterId, content);
        String offerId = dbms.insertNewOfferToDataBase(offer);
        Rating rating = new Rating(offerId, voterId, AUTHOR_RATING_VALUE);
        String ratingId = dbms.insertNewRatingToDataBase(rating);
        Candidate candidate = new Candidate(voter);
        String candidateId = dbms.insertNewCandidateToDataBase(candidate);
        CandidateProgram candidateProgram = new CandidateProgram(candidateId, offer);
        dbms.insertCandidateProgram(candidateProgram);
        //act
        String resultId = String.valueOf(dbms.getProgramId());
        //assert
        assertEquals("2", resultId);
        assertNotEquals(ratingId, resultId);
    }

    @Test
    public void shouldInsertNewVoteToDataBase() throws Exception {
        //arrange
        String voterId1 = dbms.insertNewVoterToDataBase(voter);
        Candidate candidate2 = new Candidate(voter2);
        String candidateId2 = dbms.insertNewCandidateToDataBase(candidate2);
        Vote vote1 = new Vote(voterId1, candidateId2);
        //act
        String voteId = dbms.insertVoteToDataBase(vote1);
        //assert
        assertEquals(1, dbms.getVoteMap().size());
        assertEquals("1", voteId);
    }

    @Test
    public void shouldReturnVoteIdCounterValueFromDataBase() throws Exception {
        //act
        int resultId = dbms.getVoteId();
        //assert
        assertEquals(1, resultId);
    }

    @Test
    public void shouldAutoIncrementVoteIdCounterAfterInsertCandidateProgram() throws Exception {
        //arrange
        String voterId1 = dbms.insertNewVoterToDataBase(voter);
        Candidate candidate2 = new Candidate(voter2);
        String candidateId2 = dbms.insertNewCandidateToDataBase(candidate2);
        Vote vote1 = new Vote(voterId1, candidateId2);
        String voteId = dbms.insertVoteToDataBase(vote1);
        //act
        String resultId = String.valueOf(dbms.getVoteId());
        //assert
        assertEquals("2", resultId);
        assertNotEquals(voteId, resultId);
    }

    @Test
    public void shouldDeleteAllVotesFromDataBase() throws Exception {
        //arrange
        String voterId1 = dbms.insertNewVoterToDataBase(voter);
        String voterId2 = dbms.insertNewVoterToDataBase(voter2);
        Candidate candidate1 = new Candidate(voter);
        String candidateId1 = dbms.insertNewCandidateToDataBase(candidate1);
        Candidate candidate2 = new Candidate(voter2);
        String candidateId2 = dbms.insertNewCandidateToDataBase(candidate2);
        Vote vote1 = new Vote(voterId1, candidateId2);
        String voteId1 = dbms.insertVoteToDataBase(vote1);
        Vote vote2 = new Vote(voterId2, candidateId1);
        String voteId2 = dbms.insertVoteToDataBase(vote2);
        //act
        String message = dbms.deleteAllVotesFromDataBase();
        //
        assertEquals(0, dbms.getVoteMap().size());
        assertEquals(ResponseStatusMessage.ALL_VOTES_HAVE_BEEN_DELETED.getMessage(), message);
    }

    @Test
    public void shouldReturnRatingMapFromDataBase() throws Exception {
        //arrange
        String content = "TEST";
        String voterId = dbms.insertNewVoterToDataBase(voter);
        String voterId2 = dbms.insertNewVoterToDataBase(voter2);
        Offer offer = new Offer(voterId, content);
        String offerId = dbms.insertNewOfferToDataBase(offer);
        Rating rating = new Rating(offerId, voterId, AUTHOR_RATING_VALUE);
        String ratingId = dbms.insertNewRatingToDataBase(rating);
        Rating rating2 = new Rating(offerId, voterId2, 4);
        String ratingId2 = dbms.insertNewRatingToDataBase(rating2);
        dbms.deleteRatingFromDataBase(ratingId2);
        //act
        Map<String, Rating> resultMap = dbms.getRatingMap();
        //assert
        assertEquals(1, resultMap.size());
        assertTrue(resultMap.containsKey(ratingId));
        assertTrue(resultMap.containsValue(rating));
    }

    @Test
    public void shouldReturnCandidateMapFromDataBase() throws Exception {
        //arrange
        dbms.insertNewVoterToDataBase(voter);
        dbms.insertNewVoterToDataBase(voter2);
        Candidate candidate = new Candidate(voter);
        String candidateId = dbms.insertNewCandidateToDataBase(candidate);
        Candidate candidate2 = new Candidate(voter2);
        String candidateId2 = dbms.insertNewCandidateToDataBase(candidate2);
        dbms.deleteCandidateFromDataBase(candidateId2);
        //act
        Map<String, Candidate> resultMap = dbms.getCandidateMap();
        //assert
        assertEquals(1, resultMap.size());
        assertTrue(resultMap.containsKey(candidateId));
        assertTrue(resultMap.containsValue(candidate));
    }

    @Test
    public void shouldReturnProgramMapFromDataBase() throws Exception {
        //arrange
        String content = "TEST";
        String content2 = "TEST2";
        String voterId = dbms.insertNewVoterToDataBase(voter);
        Offer offer = new Offer(voterId, content);
        String offerId = dbms.insertNewOfferToDataBase(offer);
        Rating rating = new Rating(offerId, voterId, AUTHOR_RATING_VALUE);
        dbms.insertNewRatingToDataBase(rating);
        String voterId2 = dbms.insertNewVoterToDataBase(voter2);
        Offer offer2 = new Offer(voterId2, content2);
        String offerId2 = dbms.insertNewOfferToDataBase(offer2);
        Rating rating2 = new Rating(offerId2, voterId, AUTHOR_RATING_VALUE);
        dbms.insertNewRatingToDataBase(rating2);
        Candidate candidate = new Candidate(voter);
        String candidateId = dbms.insertNewCandidateToDataBase(candidate);
        CandidateProgram candidateProgram = new CandidateProgram(candidateId, offer);
        String candidateProgramId = dbms.insertCandidateProgram(candidateProgram);
        CandidateProgram candidateProgram2 = new CandidateProgram(candidateId, offer2);
        String candidateProgramId2 = dbms.insertCandidateProgram(candidateProgram2);
        dbms.deleteCandidateProgram(candidateProgramId2);
        //act
        Map<String, CandidateProgram> resultMap = dbms.getProgramMap();
        //assert
        assertEquals(1, resultMap.size());
        assertTrue(resultMap.containsKey(candidateProgramId));
        assertTrue(resultMap.containsValue(candidateProgram));
    }

    @Test
    public void shouldReturnNominateRequestsMapFromDataBase() throws Exception {
        //arrange
        String voterId = dbms.insertNewVoterToDataBase(voter);
        String voterId2 = dbms.insertNewVoterToDataBase(voter2);
        CandidateNominateRequest request = new CandidateNominateRequest(voterId2, voterId);
        String requestId = dbms.insertNewNominateRequest(request);
        CandidateNominateRequest request2 = new CandidateNominateRequest(voterId, voterId2);
        String requestId2 = dbms.insertNewNominateRequest(request2);
        dbms.deleteNominateRequest(requestId2);
        //act
        Map<String, CandidateNominateRequest> resultMap = dbms.getNominateRequests();
        //assert
        assertEquals(1, resultMap.size());
        assertTrue(resultMap.containsKey(requestId));
        assertTrue(resultMap.containsValue(request));
    }

    @Test
    public void shouldReturnVoterMapFromDataBase() throws Exception {
        //arrange
        dbms.insertNewVoterToDataBase(voter);
        dbms.insertNewVoterToDataBase(voter2);
        //act
        Map<String, Voter> voterMap = dbms.getVoterMap();
        //assert
        assertEquals(2, voterMap.size());
        assertEquals(db.getVoterMap(), voterMap);
    }

    @Test
    public void shouldReturnTokenMapFromDataBase() throws Exception {
        //arrange
        dbms.insertNewVoterToDataBase(voter);
        dbms.insertNewVoterToDataBase(voter2);
        //act
        Map<String, String> tokenMap = dbms.getTokenMap();
        //assert
        assertEquals(2, tokenMap.size());
        assertEquals(db.getTokenMap(), tokenMap);
    }

    @Test
    public void shouldReturnVoteMapFromDataBase() throws Exception {
        //arrange
        dbms.insertVoteToDataBase(new Vote("1", "2"));
        dbms.insertVoteToDataBase(new Vote("2", "1"));
        //act
        Map<String, Vote> voteMap = dbms.getVoteMap();
        //assert
        assertEquals(2, voteMap.size());
        assertEquals(db.getVoteMap(), voteMap);
    }
}
