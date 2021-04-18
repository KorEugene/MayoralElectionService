package net.thumbtack.school.elections.server;

import com.google.gson.*;
import net.thumbtack.school.elections.dao.VoterDao;
import net.thumbtack.school.elections.daoimpl.VoterDaoImpl;
import net.thumbtack.school.elections.model.FieldType;
import net.thumbtack.school.elections.model.ResponseStatusMessage;
import net.thumbtack.school.elections.model.Voter;
import net.thumbtack.school.elections.request.StartVotingDtoRequest;
import net.thumbtack.school.elections.response.StartVotingDtoResponse;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.File;
import java.io.IOException;

import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

public class TestServer {

    private static Gson gson = new Gson();
    private static Server server = Server.getInstance();
    private static VoterDao voterDao = VoterDaoImpl.getInstance();
    private static DataBaseManagementSystem dbms = DataBaseManagementSystem.getInstance();

    @BeforeClass
    public static void loadServerState() throws IOException {
        server.startServer(null);
    }

    @AfterClass
    public static void saveServerState() throws IOException {
        server.stopServer(null);
    }

    @Test
    public void testElectionServer() throws Exception {
        //arrange
        String jsonString1 = "{\"fullName\":{\"firstName\":\"Ivan\",\"lastName\":\"Ivanov\",\"patronymicName\":\"Ivanovich\"}," +
                "\"address\":{\"street\":\"Fedora Abramova\",\"house\":\"15\",\"flat\":\"27\"}," +
                "\"account\":{\"login\":\"IVA\",\"password\":\"123456\"}}";
        String jsonString2 = "{\"fullName\":{\"firstName\":\"Petr\",\"lastName\":\"Petrov\",\"patronymicName\":\"Petrovich\"}," +
                "\"address\":{\"street\":\"Primorskaya\",\"house\":\"7\",\"flat\":\"35\"}," +
                "\"account\":{\"login\":\"Petya27\",\"password\":\"654321\"}}";
        String jsonString3 = "{\"fullName\":{\"firstName\":\"Semen\",\"lastName\":\"Semenov\",\"patronymicName\":\"Semenovich\"}," +
                "\"address\":{\"street\":\"Compositorov\",\"house\":\"2\",\"flat\":\"11\"}," +
                "\"account\":{\"login\":\"Semena33\",\"password\":\"987654\"}}";
        String correctCreateRatingResponse = getCorrectResponseForChangeOfferRating(ResponseStatusMessage.RATING_STATUS_CREATED.getMessage());
        String correctDeleteRatingResponse = getCorrectResponseForChangeOfferRating(ResponseStatusMessage.RATING_STATUS_DELETED.getMessage());
        String correctUpdateRatingResponse = getCorrectResponseForChangeOfferRating(ResponseStatusMessage.RATING_STATUS_UPDATED.getMessage());

        //act

        //add voters
        String token1AsJsonString = server.registerVoter(jsonString1);
        String token1 = getFieldAsStringFromResponseAsJsonString(FieldType.TOKEN.getType(), token1AsJsonString);
        String voterId1 = voterDao.getVoterIdByToken(token1);
        String token2AsJsonString = server.registerVoter(jsonString2);
        String token2 = getFieldAsStringFromResponseAsJsonString(FieldType.TOKEN.getType(), token2AsJsonString);
        String voterId2 = voterDao.getVoterIdByToken(token2);
        String token3AsJsonString = server.registerVoter(jsonString3);
        String token3 = getFieldAsStringFromResponseAsJsonString(FieldType.TOKEN.getType(), token3AsJsonString);
        String voterId3 = voterDao.getVoterIdByToken(token3);
        //Assert. Check add voters
        assertEquals(4, dbms.getVoterId());
        assertEquals(3, dbms.getTokenMap().size());
        assertEquals(3, dbms.getVoterMap().size());
        assertEquals(3, dbms.getPersonMap().size());
        assertTrue(dbms.getPersonMap().containsKey("1"));
        assertTrue(dbms.getPersonMap().containsKey("2"));
        assertTrue(dbms.getPersonMap().containsKey("3"));
        //add offers
        String offer1Request = getRequestForAddOffer(token1, "use your brain!");
        String offerId1AsJsonString = server.addOffer(offer1Request);
        String offerId1 = getFieldAsStringFromResponseAsJsonString(FieldType.OFFER_ID.getType(), offerId1AsJsonString);
        String offer2Request = getRequestForAddOffer(token2, "do not be stupid!");
        String offerId2AsJsonString = server.addOffer(offer2Request);
        String offerId2 = getFieldAsStringFromResponseAsJsonString(FieldType.OFFER_ID.getType(), offerId2AsJsonString);
        String offer3Request = getRequestForAddOffer(token3, "do not be shy!");
        String offerId3AsJsonString = server.addOffer(offer3Request);
        String offerId3 = getFieldAsStringFromResponseAsJsonString(FieldType.OFFER_ID.getType(), offerId3AsJsonString);
        //Assert. Check add offers
        assertEquals(4, dbms.getOfferId());
        assertEquals(3, dbms.getOfferMap().size());
        assertTrue(dbms.getOfferMap().containsKey("1"));
        assertTrue(dbms.getOfferMap().containsKey("2"));
        assertTrue(dbms.getOfferMap().containsKey("3"));
        //rate offers
        String create_1_2 = server.rateOffer(getRequestForNewRating(token1, offerId2, "2"));
        String create_1_3 = server.rateOffer(getRequestForNewRating(token1, offerId3, "2"));
        String create_2_1 = server.rateOffer(getRequestForNewRating(token2, offerId1, "3"));
        String create_2_3 = server.rateOffer(getRequestForNewRating(token2, offerId3, "3"));
        String create_3_1 = server.rateOffer(getRequestForNewRating(token3, offerId1, "4"));
        String create_3_2 = server.rateOffer(getRequestForNewRating(token3, offerId2, "4"));
        String delete_1_2 = server.rateOffer(getRequestForNewRating(token1, offerId2, "0"));
        String delete_2_3 = server.rateOffer(getRequestForNewRating(token2, offerId3, "0"));
        String delete_3_1 = server.rateOffer(getRequestForNewRating(token3, offerId1, "0"));
        String update_1_3 = server.rateOffer(getRequestForNewRating(token1, offerId3, "3"));
        String update_2_1 = server.rateOffer(getRequestForNewRating(token2, offerId1, "2"));
        String update_3_2 = server.rateOffer(getRequestForNewRating(token3, offerId2, "1"));
        //Assert. Check ratings
        assertEquals(correctCreateRatingResponse, create_1_2);
        assertEquals(correctCreateRatingResponse, create_1_3);
        assertEquals(correctCreateRatingResponse, create_2_1);
        assertEquals(correctCreateRatingResponse, create_2_3);
        assertEquals(correctCreateRatingResponse, create_3_1);
        assertEquals(correctCreateRatingResponse, create_3_2);
        assertEquals(correctDeleteRatingResponse, delete_1_2);
        assertEquals(correctDeleteRatingResponse, delete_2_3);
        assertEquals(correctDeleteRatingResponse, delete_3_1);
        assertEquals(correctUpdateRatingResponse, update_1_3);
        assertEquals(correctUpdateRatingResponse, update_2_1);
        assertEquals(correctUpdateRatingResponse, update_3_2);
        long quantityOffer1Ratings = dbms.getRatingMap().values().stream().filter(v -> v.getOfferId().equals(offerId1)).count();
        long quantityOffer2Ratings = dbms.getRatingMap().values().stream().filter(v -> v.getOfferId().equals(offerId2)).count();
        long quantityOffer3Ratings = dbms.getRatingMap().values().stream().filter(v -> v.getOfferId().equals(offerId3)).count();
        assertEquals(2, quantityOffer1Ratings);
        assertEquals(2, quantityOffer2Ratings);
        assertEquals(2, quantityOffer3Ratings);
        //get offer list sorted by average rate
        String offerListSortedByAverageRate = server.getOffersSortedByAverageRate(getRequestForOfferListSortedByAverageRate(token1));
        //Assert. Check offer list sorted by average rate
        assertTrue(offerListSortedByAverageRate.contains(dbms.getOfferMap().get(offerId1).getContent()));
        assertTrue(offerListSortedByAverageRate.contains(dbms.getOfferMap().get(offerId2).getContent()));
        assertTrue(offerListSortedByAverageRate.contains(dbms.getOfferMap().get(offerId3).getContent()));
        assertFalse(offerListSortedByAverageRate.contains("i will be a mayor!"));
        //get offer list by author
        String offerListByAuthor = server.getOfferListByAuthor(getRequestForOfferListByAuthorId(token1, new String[]{"2", "3"}));
        //Assert. Check offer list by author
        assertFalse(offerListByAuthor.contains(dbms.getOfferMap().get(offerId1).getContent()));
        assertTrue(offerListByAuthor.contains(dbms.getOfferMap().get(offerId2).getContent()));
        assertTrue(offerListByAuthor.contains(dbms.getOfferMap().get(offerId3).getContent()));
        //log out voter
        Voter voter3 = voterDao.getVoterById("3");
        String login3 = voter3.getPerson().getAccount().getLogin();
        String password3 = voter3.getPerson().getAccount().getPassword();
        String logOutRequest3 = getRequestForLogOutVoterFromServer(token3);
        String logOutMessage3AsJsonString = server.logOutVoter(logOutRequest3);
        String logOutMessage3AsString = getFieldAsStringFromResponseAsJsonString(FieldType.MESSAGE.getType(), logOutMessage3AsJsonString);
        //Assert. Check log out candidate
        assertEquals(2, dbms.getTokenMap().size());
        assertEquals(3, dbms.getVoterMap().size());
        assertEquals(3, dbms.getOfferMap().size());
        assertEquals(4, dbms.getRatingMap().size());
        assertEquals(ResponseStatusMessage.SUCCESSFULLY_LOGGED_OUT.getMessage(), logOutMessage3AsString);
        //log on voter
        String logOnRequest3 = getRequestForLogOnVoterToServer(login3, password3);
        String logOnResponse3AsJsonString = server.logOnVoter(logOnRequest3);
        String newToken3 = getFieldAsStringFromResponseAsJsonString(FieldType.TOKEN.getType(), logOnResponse3AsJsonString);
        token3 = voterDao.getVoterById("3").getToken();
        String message3 = getFieldAsStringFromResponseAsJsonString(FieldType.MESSAGE.getType(), logOnResponse3AsJsonString);
        //Assert. Check log on candidate
        assertEquals(3, dbms.getVoterMap().size());
        assertEquals(3, dbms.getVoterMap().size());
        assertEquals(3, dbms.getOfferMap().size());
        assertEquals(4, dbms.getRatingMap().size());
        assertEquals(ResponseStatusMessage.SUCCESSFULLY_LOGGED_ON.getMessage(), message3);
        //nominate yourself to candidate
        String addCandidateRequest1 = getRequestForAddCandidate(token1, "1");
        String candidateId1AsJsonString = server.registerCandidate(addCandidateRequest1);
        String candidateId1 = getFieldAsStringFromResponseAsJsonString(FieldType.ID.getType(), candidateId1AsJsonString);
        //Assert. Check add candidate
        assertEquals(1, dbms.getCandidateMap().size());
        assertEquals("1", candidateId1);
        assertEquals(dbms.getVoterMap().get("1"), dbms.getCandidateMap().get("1").getVoter());
        assertEquals(1, dbms.getProgramMap().size());
        assertEquals("use your brain!", dbms.getProgramMap().get("1").getOffer().getContent());
        //nominate another voter for candidates
        String addCandidateRequest2 = getRequestForAddCandidate(token2, "3");
        String nominateCandidateResponse2 = server.registerCandidate(addCandidateRequest2);
        String nominateCandidateRequestId2 = getFieldAsStringFromResponseAsJsonString(FieldType.ID.getType(), nominateCandidateResponse2);
        String addCandidateRequest3 = getRequestForAddCandidate(newToken3, "2");
        String nominateCandidateResponse3 = server.registerCandidate(addCandidateRequest3);
        String nominateCandidateRequestId3 = getFieldAsStringFromResponseAsJsonString(FieldType.ID.getType(), nominateCandidateResponse3);
        //Assert. Check nominate candidate
        assertEquals(1, dbms.getCandidateMap().size());
        assertEquals(2, dbms.getNominateRequests().size());
        assertEquals("1", nominateCandidateRequestId2);
        assertEquals("2", nominateCandidateRequestId3);
        //check nominate request
        String newToken3AsJsonString = getTokenAsJsonString(newToken3);
        String checkNominateRequestsResponseAsJsonString = server.checkRequests(newToken3AsJsonString);
        String checkNominateRequestsResponseAsJsonString2 = server.checkRequests(token2AsJsonString);
        //Assert. Check nominate requests for voter
        assertTrue(checkNominateRequestsResponseAsJsonString.contains(dbms.getNominateRequests().get("1").getInitiatorId()));
        assertTrue(checkNominateRequestsResponseAsJsonString.contains(voterId3));
        assertTrue(checkNominateRequestsResponseAsJsonString2.contains(dbms.getNominateRequests().get("2").getInitiatorId()));
        assertTrue(checkNominateRequestsResponseAsJsonString2.contains(voterId2));
        //negative answer for nominate request
        String answerNominateRequest = getRequestForAnswerNominateRequest(newToken3, nominateCandidateRequestId3, false);
        String response = server.answerRequest(answerNominateRequest);
        String responseCandidateId = getFieldAsStringFromResponseAsJsonString(FieldType.CANDIDATE_ID.getType(), response);
        String responseRequestStatus = getFieldAsStringFromResponseAsJsonString(FieldType.MESSAGE.getType(), response);
        //Assert. Check answer for nominate request
        assertEquals(1, dbms.getNominateRequests().size());
        assertEquals(1, dbms.getCandidateMap().size());
        assertEquals("0", responseCandidateId);
        assertEquals(ResponseStatusMessage.NOMINATE_REQUEST_REMOVED.getMessage(), responseRequestStatus);
        //positive answer for nominate request
        String answerNominateRequest2 = getRequestForAnswerNominateRequest(token2, nominateCandidateRequestId2, true);
        String response2 = server.answerRequest(answerNominateRequest2);
        String candidateId2 = getFieldAsStringFromResponseAsJsonString(FieldType.CANDIDATE_ID.getType(), response2);
        String responseRequestStatus2 = getFieldAsStringFromResponseAsJsonString(FieldType.MESSAGE.getType(), response2);
        //Assert. Check answer for nominate request
        assertEquals(0, dbms.getNominateRequests().size());
        assertEquals(2, dbms.getCandidateMap().size());
        assertEquals("2", candidateId2);
        assertEquals(ResponseStatusMessage.NOMINATE_REQUEST_REMOVED.getMessage(), responseRequestStatus2);
        //add offers to candidate program
        String requestForAddOfferToProgram1 = getRequestForUpdateCandidateProgram(token1, offerId2, true);
        String responseMessageAsJsonString1 = server.updateCandidateProgram(requestForAddOfferToProgram1);
        String responseMessage1 = getFieldAsStringFromResponseAsJsonString(FieldType.MESSAGE.getType(), responseMessageAsJsonString1);
        String requestForAddOfferToProgram2 = getRequestForUpdateCandidateProgram(token1, offerId3, true);
        String responseMessageAsJsonString2 = server.updateCandidateProgram(requestForAddOfferToProgram2);
        String responseMessage2 = getFieldAsStringFromResponseAsJsonString(FieldType.MESSAGE.getType(), responseMessageAsJsonString2);
        //Assert. Check answer for update candidate program
        assertEquals(3, dbms.getOfferMap().size());
        assertEquals(4, dbms.getProgramMap().size());
        assertEquals("use your brain!", dbms.getProgramMap().get("1").getOffer().getContent());
        assertEquals("do not be stupid!", dbms.getProgramMap().get("2").getOffer().getContent());
        assertEquals("do not be stupid!", dbms.getProgramMap().get("3").getOffer().getContent());
        assertEquals("do not be shy!", dbms.getProgramMap().get("4").getOffer().getContent());
        assertEquals(ResponseStatusMessage.OFFER_ADDED_TO_CANDIDATE_PROGRAM.getMessage(), responseMessage1);
        assertEquals(ResponseStatusMessage.OFFER_ADDED_TO_CANDIDATE_PROGRAM.getMessage(), responseMessage2);
        //remove offer from candidate program
        String requestForAddOfferToProgram3 = getRequestForUpdateCandidateProgram(token1, offerId3, false);
        String responseMessageAsJsonString3 = server.updateCandidateProgram(requestForAddOfferToProgram3);
        String responseMessage3 = getFieldAsStringFromResponseAsJsonString(FieldType.MESSAGE.getType(), responseMessageAsJsonString3);
        //Assert. Check answer for update candidate program
        assertEquals(3, dbms.getOfferMap().size());
        assertEquals(3, dbms.getProgramMap().size());
        assertEquals("use your brain!", dbms.getProgramMap().get("1").getOffer().getContent());
        assertEquals("do not be stupid!", dbms.getProgramMap().get("2").getOffer().getContent());
        assertEquals("do not be stupid!", dbms.getProgramMap().get("3").getOffer().getContent());
        assertEquals(ResponseStatusMessage.OFFER_ADDED_TO_CANDIDATE_PROGRAM.getMessage(), responseMessage1);
        assertEquals(ResponseStatusMessage.OFFER_ADDED_TO_CANDIDATE_PROGRAM.getMessage(), responseMessage2);
        assertEquals(ResponseStatusMessage.OFFER_REMOVED_FROM_CANDIDATE_PROGRAM.getMessage(), responseMessage3);
        //withdraw candidate
        String requestForWithdrawCandidate = getRequestForWithdrawCandidate(token1);
        String responseWithdraw = server.withdrawCandidate(requestForWithdrawCandidate);
        String responseWithdrawMessage = getFieldAsStringFromResponseAsJsonString(FieldType.MESSAGE.getType(), responseWithdraw);
        //Assert. Check withdraw candidate
        assertEquals(1, dbms.getCandidateMap().size());
        assertEquals(ResponseStatusMessage.CANDIDACY_HAS_BEEN_WITHDRAWN.getMessage(), responseWithdrawMessage);
        //get candidate list with programs
        String requestForCandidateList = getRequestForCandidateListWithPrograms(token2);
        String responseAsCandidateList = server.getCandidates(requestForCandidateList);
        //Assert. Check candidate list
        assertTrue(responseAsCandidateList.contains(dbms.getProgramMap().get("2").getOffer().getContent()));
        assertTrue(responseAsCandidateList.contains(dbms.getCandidateMap().get(candidateId2).getId()));
        //Save DataBase to file
        server.stopServer(new File("SavedData.txt").getAbsolutePath());
        //Assert. Check DataBase after saving to file
        assertEquals(1, dbms.getVoterId());
        assertEquals(1, dbms.getOfferId());
        assertEquals(1, dbms.getRatingId());
        assertEquals(1, dbms.getCandidateId());
        assertEquals(1, dbms.getProgramId());
        assertEquals(1, dbms.getNominateRequestId());
        assertEquals(1, dbms.getVoteId());
        assertEquals(0, dbms.getTokenMap().size());
        assertEquals(0, dbms.getPersonMap().size());
        assertEquals(0, dbms.getVoterMap().size());
        assertEquals(0, dbms.getOfferMap().size());
        assertEquals(0, dbms.getRatingMap().size());
        assertEquals(0, dbms.getCandidateMap().size());
        assertEquals(0, dbms.getProgramMap().size());
        assertEquals(0, dbms.getNominateRequests().size());
        assertEquals(0, dbms.getVoteMap().size());
        //Load DataBase from file
        server.startServer(new File("SavedData.txt").getAbsolutePath());
        //Assert. Check DataBase after loading from file
        assertEquals(4, dbms.getVoterId());
        assertEquals(4, dbms.getOfferId());
        assertEquals(10, dbms.getRatingId());
        assertEquals(3, dbms.getCandidateId());
        assertEquals(5, dbms.getProgramId());
        assertEquals(3, dbms.getNominateRequestId());
        assertEquals(1, dbms.getVoteId());
        assertEquals(3, dbms.getTokenMap().size());
        assertEquals(3, dbms.getPersonMap().size());
        assertEquals(3, dbms.getVoterMap().size());
        assertEquals(3, dbms.getOfferMap().size());
        assertEquals(4, dbms.getRatingMap().size());
        assertEquals(1, dbms.getCandidateMap().size());
        assertEquals(1, dbms.getProgramMap().size());
        assertEquals(0, dbms.getNominateRequests().size());
        assertEquals(0, dbms.getVoteMap().size());
        //Start voting
        String requestForVoting = getRequestForStartVoting(token1);
        boolean statusBefore = server.isVotingStatus();
        String responseForVotingAsJsonString = server.startVoting(requestForVoting);
        String responseForVoting = getFieldAsStringFromResponseAsJsonString(FieldType.MESSAGE.getType(), responseForVotingAsJsonString);
        boolean statusAfter = server.isVotingStatus();
        //Assert. Check voting started
        assertFalse(statusBefore);
        assertEquals(ResponseStatusMessage.VOTING_STARTED.getMessage(), responseForVoting);
        assertTrue(statusAfter);
        //Voting
        String voteRequest1 = getRequestForVoteInElections(token1, "0");
        String voteResponse1AsJsonString = server.voteInElections(voteRequest1);
        String voteResponse1 = getFieldAsStringFromResponseAsJsonString(FieldType.MESSAGE.getType(), voteResponse1AsJsonString);
        String voteRequest2 = getRequestForVoteInElections(token2, "0");
        String voteResponse2AsJsonString = server.voteInElections(voteRequest2);
        String voteResponse2 = getFieldAsStringFromResponseAsJsonString(FieldType.MESSAGE.getType(), voteResponse2AsJsonString);
        String voteRequest3 = getRequestForVoteInElections(token3, candidateId2);
        String voteResponse3AsJsonString = server.voteInElections(voteRequest3);
        String voteResponse3 = getFieldAsStringFromResponseAsJsonString(FieldType.MESSAGE.getType(), voteResponse3AsJsonString);
        //Assert. Check voting
        assertEquals(3, dbms.getVoteMap().size());
        assertEquals(ResponseStatusMessage.VOTED.getMessage(), voteResponse1);
        assertEquals(ResponseStatusMessage.VOTED.getMessage(), voteResponse2);
        assertEquals(ResponseStatusMessage.VOTED.getMessage(), voteResponse3);
        //Stop voting
        String requestForStopVoting = getRequestForStopVoting(token2);
        String voteResponseStopVoting2AsJsonString = server.stopVoting(requestForStopVoting);
        String voteResponseStopVoting = getFieldAsStringFromResponseAsJsonString(FieldType.MESSAGE.getType(), voteResponseStopVoting2AsJsonString);
        //Assert. Check stop voting
        assertEquals(ResponseStatusMessage.NO_WINNERS.getMessage(), voteResponseStopVoting);
        assertEquals(0, dbms.getVoteMap().size());
        assertFalse(server.isVotingStatus());
    }

    private String getFieldAsStringFromResponseAsJsonString(String fieldTypeAsString, String inputJsonString) {
        JsonObject jsonObject = gson.fromJson(inputJsonString, JsonObject.class);
        return jsonObject.get(fieldTypeAsString).getAsString();
    }

    private String getRequestForAddOffer(String token, String content) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.add(FieldType.TOKEN.getType(), getFieldAsJsonElement(token));
        jsonObject.add(FieldType.CONTENT.getType(), getFieldAsJsonElement(content));
        return gson.toJson(jsonObject);
    }

    private String getCorrectResponseForChangeOfferRating(String ratingStatusMessage) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.add(FieldType.MESSAGE.getType(), getFieldAsJsonElement(ratingStatusMessage));
        return gson.toJson(jsonObject);
    }

    private String getRequestForNewRating(String token, String offerId, String value) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.add(FieldType.TOKEN.getType(), getFieldAsJsonElement(token));
        jsonObject.add(FieldType.OFFER_ID.getType(), getFieldAsJsonElement(offerId));
        jsonObject.add(FieldType.VALUE.getType(), getFieldAsJsonElement(value));
        return gson.toJson(jsonObject);
    }

    private String getRequestForOfferListSortedByAverageRate(String token) {
        return getTokenAsJsonString(token);
    }

    private String getRequestForOfferListByAuthorId(String token, String[] ids) {
        JsonArray authorId = new JsonArray();
        for (String idAsString : ids) {
            authorId.add(idAsString);
        }
        JsonObject jsonObject = new JsonObject();
        jsonObject.add(FieldType.TOKEN.getType(), getFieldAsJsonElement(token));
        jsonObject.add(FieldType.AUTHOR_ID.getType(), authorId);
        return gson.toJson(jsonObject);
    }

    private String getRequestForAddCandidate(String initiatorToken, String nomineeId) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.add(FieldType.TOKEN.getType(), getFieldAsJsonElement(initiatorToken));
        jsonObject.add(FieldType.NOMINEE_ID.getType(), getFieldAsJsonElement(nomineeId));
        return gson.toJson(jsonObject);
    }

    private String getRequestForAnswerNominateRequest(String token, String nominateRequestId, boolean consent) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.add(FieldType.TOKEN.getType(), getFieldAsJsonElement(token));
        jsonObject.add(FieldType.NOMINATE_REQUEST_ID.getType(), getFieldAsJsonElement(nominateRequestId));
        jsonObject.add(FieldType.CONSENT.getType(), getFieldAsJsonElement(consent));
        return gson.toJson(jsonObject);
    }

    private String getRequestForUpdateCandidateProgram(String token, String offerId, boolean addition) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.add(FieldType.TOKEN.getType(), getFieldAsJsonElement(token));
        jsonObject.add(FieldType.OFFER_ID.getType(), getFieldAsJsonElement(offerId));
        jsonObject.add(FieldType.ADDITION.getType(), getFieldAsJsonElement(addition));
        return gson.toJson(jsonObject);
    }

    private String getRequestForWithdrawCandidate(String token) {
        return getTokenAsJsonString(token);
    }

    private String getRequestForCandidateListWithPrograms(String token) {
        return getTokenAsJsonString(token);
    }

    private String getRequestForLogOnVoterToServer(String login, String password) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.add(FieldType.LOGIN.getType(), getFieldAsJsonElement(login));
        jsonObject.add(FieldType.PASSWORD.getType(), getFieldAsJsonElement(password));
        return gson.toJson(jsonObject);
    }

    private String getRequestForLogOutVoterFromServer(String token) {
        return getTokenAsJsonString(token);
    }

    private String getTokenAsJsonString(String token) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.add(FieldType.TOKEN.getType(), getFieldAsJsonElement(token));
        return gson.toJson(jsonObject);
    }

    private String getRequestForStartVoting(String token) {
        return getTokenAsJsonString(token);
    }

    private String getRequestForVoteInElections(String token, String candidateId) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.add(FieldType.TOKEN.getType(), getFieldAsJsonElement(token));
        jsonObject.add(FieldType.CANDIDATE_ID.getType(), getFieldAsJsonElement(candidateId));
        return gson.toJson(jsonObject);
    }

    private String getRequestForStopVoting(String token) {
        return getTokenAsJsonString(token);
    }

    private JsonElement getFieldAsJsonElement(String element) {
        return new JsonParser().parse(gson.toJson(element));
    }

    private JsonElement getFieldAsJsonElement(boolean element) {
        return new JsonParser().parse(gson.toJson(element));
    }
}
