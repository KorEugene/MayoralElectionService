package net.thumbtack.school.elections.service;

import net.thumbtack.school.elections.exceptionimpl.CandidateError;
import net.thumbtack.school.elections.exceptionimpl.CandidateException;
import net.thumbtack.school.elections.exceptionimpl.ElectionError;
import net.thumbtack.school.elections.exceptionimpl.ElectionException;
import net.thumbtack.school.elections.model.Account;
import net.thumbtack.school.elections.model.Address;
import net.thumbtack.school.elections.model.FullName;
import net.thumbtack.school.elections.model.ResponseStatusMessage;
import net.thumbtack.school.elections.request.*;
import net.thumbtack.school.elections.response.*;
import net.thumbtack.school.elections.server.DataBase;
import net.thumbtack.school.elections.server.DataBaseManagementSystem;
import net.thumbtack.school.elections.server.Server;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class TestElectionService {

    private static Server server = Server.getInstance();
    private static VoterService voterService = server.getVoterService();
    private static ElectionService electionService = server.getElectionService();
    private static DataBaseManagementSystem dbms = DataBaseManagementSystem.getInstance();
    private static FullName fullName1 = new FullName("Ivan", "Ivanov", "Ivanovich");
    private static Address address1 = new Address("Fedora Abramova", "15", "27");
    private static Account account1 = new Account("IVA", "123456");
    private static FullName fullName2 = new FullName("Petr", "Petrov", "Petrovich");
    private static Address address2 = new Address("Primorskaya", "35", "4");
    private static Account account2 = new Account("PETYA", "654321");
    private static FullName fullName3 = new FullName("Semen", "Semenov", "Semenovich");
    private static Address address3 = new Address("Compositorov", "2", "11");
    private static Account account3 = new Account("Semena33", "987654");
    private static FullName fullName4 = new FullName("Vasya", "Vasilev", "Vasilevich");
    private static Address address4 = new Address("Kosmonavtov", "99", "116");
    private static Account account4 = new Account("VasyaKosmos", "kosmos");
    private static FullName fullName5 = new FullName("Misha", "Mishkin", "Mikhailovich");
    private static Address address5 = new Address("Veselyh Nosorogov", "124", "56");
    private static Account account5 = new Account("Michail44", "nosorogi47");
    private static FullName fullName6 = new FullName("Pontiy", "Pilatov");
    private static Address address6 = new Address("Istoricheskaya", "11", "21");
    private static Account account6 = new Account("historic33", "stupid");
    private static String token1;
    private static String token2;
    private static String token3;
    private static String token4;
    private static String token5;
    private static String token6;

    @Before
    public void loadData() {
        //voters
        RegisterVoterDtoRequest registerVoterDtoRequest1 = new RegisterVoterDtoRequest(fullName1, address1, account1);
        RegisterVoterDtoResponse voterResponse1 = voterService.registerNewVoter(registerVoterDtoRequest1);
        token1 = voterResponse1.getToken();
        RegisterVoterDtoRequest registerVoterDtoRequest2 = new RegisterVoterDtoRequest(fullName2, address2, account2);
        RegisterVoterDtoResponse voterResponse2 = voterService.registerNewVoter(registerVoterDtoRequest2);
        token2 = voterResponse2.getToken();
        RegisterVoterDtoRequest registerVoterDtoRequest3 = new RegisterVoterDtoRequest(fullName3, address3, account3);
        RegisterVoterDtoResponse voterResponse3 = voterService.registerNewVoter(registerVoterDtoRequest3);
        token3 = voterResponse3.getToken();
        RegisterVoterDtoRequest registerVoterDtoRequest4 = new RegisterVoterDtoRequest(fullName4, address4, account4);
        RegisterVoterDtoResponse voterResponse4 = voterService.registerNewVoter(registerVoterDtoRequest4);
        token4 = voterResponse4.getToken();
        RegisterVoterDtoRequest registerVoterDtoRequest5 = new RegisterVoterDtoRequest(fullName5, address5, account5);
        RegisterVoterDtoResponse voterResponse5 = voterService.registerNewVoter(registerVoterDtoRequest5);
        token5 = voterResponse5.getToken();
        RegisterVoterDtoRequest registerVoterDtoRequest6 = new RegisterVoterDtoRequest(fullName6, address6, account6);
        RegisterVoterDtoResponse voterResponse6 = voterService.registerNewVoter(registerVoterDtoRequest6);
        token6 = voterResponse6.getToken();
        //offers
        String content1 = "use your brain!";
        AddOfferDtoRequest addOfferDtoRequest1 = new AddOfferDtoRequest(token1, content1);
        AddOfferDtoResponse offerResponse1 = voterService.addNewOffer(addOfferDtoRequest1);
        String offerId1 = offerResponse1.getOfferId();
        String content2 = "do not be stupid!";
        AddOfferDtoRequest addOfferDtoRequest2 = new AddOfferDtoRequest(token2, content2);
        AddOfferDtoResponse offerResponse2 = voterService.addNewOffer(addOfferDtoRequest2);
        String offerId2 = offerResponse2.getOfferId();
        String content3 = "do not be shy!";
        AddOfferDtoRequest addOfferDtoRequest3 = new AddOfferDtoRequest(token3, content3);
        AddOfferDtoResponse offerResponse3 = voterService.addNewOffer(addOfferDtoRequest3);
        String offerId3 = offerResponse3.getOfferId();
        //ratings
        RateOfferDtoRequest rateOfferDtoRequest1 = new RateOfferDtoRequest(token1, offerId2, "1");
        voterService.rateOffer(rateOfferDtoRequest1);
        RateOfferDtoRequest rateOfferDtoRequest2 = new RateOfferDtoRequest(token1, offerId3, "2");
        voterService.rateOffer(rateOfferDtoRequest2);
        RateOfferDtoRequest rateOfferDtoRequest3 = new RateOfferDtoRequest(token2, offerId1, "3");
        voterService.rateOffer(rateOfferDtoRequest3);
        RateOfferDtoRequest rateOfferDtoRequest4 = new RateOfferDtoRequest(token2, offerId3, "4");
        voterService.rateOffer(rateOfferDtoRequest4);
        RateOfferDtoRequest rateOfferDtoRequest5 = new RateOfferDtoRequest(token3, offerId1, "5");
        voterService.rateOffer(rateOfferDtoRequest5);
    }

    @After
    public void clearData() {
        DataBase.clearInstance();
        server.setVotingStatus(false);
    }

    @Test
    public void testStartVoting() throws Exception {
        //arrange
        AddCandidateDtoRequest request1 = new AddCandidateDtoRequest(token1, "1");
        voterService.registerNewCandidate(request1);
        AddCandidateDtoRequest request2 = new AddCandidateDtoRequest(token2, "2");
        voterService.registerNewCandidate(request2);
        AddCandidateDtoRequest request6 = new AddCandidateDtoRequest(token6, "6");
        voterService.registerNewCandidate(request6);
        //act
        StartVotingDtoRequest request = new StartVotingDtoRequest(token3);
        boolean statusBefore = server.isVotingStatus();
        StartVotingDtoResponse response = electionService.startVoting(request);
        String responseMessage = response.getMessage();
        boolean statusAfter = server.isVotingStatus();
        //assert
        assertEquals(ResponseStatusMessage.VOTING_STARTED.getMessage(), responseMessage);
        assertFalse(statusBefore);
        assertTrue(statusAfter);
        assertEquals(2, dbms.getCandidateMap().size());
        assertTrue(dbms.getCandidateMap().containsKey("1"));
        assertTrue(dbms.getCandidateMap().containsKey("2"));
        assertFalse(dbms.getCandidateMap().containsKey("3"));
    }

    @Test
    public void testVoteInElections() throws Exception {
        //arrange
        AddCandidateDtoRequest candidateRequest1 = new AddCandidateDtoRequest(token1, "1");
        AddCandidateDtoResponse candidateResponse1 = voterService.registerNewCandidate(candidateRequest1);
        String candidateId1 = candidateResponse1.getId();
        AddCandidateDtoRequest candidateRequest2 = new AddCandidateDtoRequest(token2, "2");
        AddCandidateDtoResponse candidateResponse2 = voterService.registerNewCandidate(candidateRequest2);
        String candidateId2 = candidateResponse2.getId();
        //act
        VoteInElectionsDtoRequest voteRequest1 = new VoteInElectionsDtoRequest(token1, "0");
        VoteInElectionsDtoResponse voteResponse1 = electionService.voteInElections(voteRequest1);
        String voteMessage1 = voteResponse1.getMessage();
        VoteInElectionsDtoRequest voteRequest2 = new VoteInElectionsDtoRequest(token2, candidateId1);
        VoteInElectionsDtoResponse voteResponse2 = electionService.voteInElections(voteRequest2);
        String voteMessage2 = voteResponse2.getMessage();
        VoteInElectionsDtoRequest voteRequest3 = new VoteInElectionsDtoRequest(token3, candidateId1);
        VoteInElectionsDtoResponse voteResponse3 = electionService.voteInElections(voteRequest3);
        String voteMessage3 = voteResponse3.getMessage();
        VoteInElectionsDtoRequest voteRequest4 = new VoteInElectionsDtoRequest(token4, candidateId1);
        VoteInElectionsDtoResponse voteResponse4 = electionService.voteInElections(voteRequest4);
        String voteMessage4 = voteResponse4.getMessage();
        VoteInElectionsDtoRequest voteRequest5 = new VoteInElectionsDtoRequest(token5, candidateId2);
        VoteInElectionsDtoResponse voteResponse5 = electionService.voteInElections(voteRequest5);
        String voteMessage5 = voteResponse5.getMessage();
        VoteInElectionsDtoRequest voteRequest6 = new VoteInElectionsDtoRequest(token6, "6");
        String voteMessage6 = "Test failed!";
        try {
            electionService.voteInElections(voteRequest6);
        } catch (CandidateException ex) {
            voteMessage6 = ex.getErrorCode().getErrorString();
        }
        VoteInElectionsDtoRequest voteRequest7 = new VoteInElectionsDtoRequest(token3, candidateId2);
        String voteMessage7 = "Test failed!";
        try {
            electionService.voteInElections(voteRequest7);
        } catch (ElectionException ex) {
            voteMessage7 = ex.getErrorCode().getErrorString();
        }
        //assert
        assertEquals(5, dbms.getVoteMap().size());
        assertEquals(ResponseStatusMessage.VOTED.getMessage(), voteMessage1);
        assertEquals(ResponseStatusMessage.VOTED.getMessage(), voteMessage2);
        assertEquals(ResponseStatusMessage.VOTED.getMessage(), voteMessage3);
        assertEquals(ResponseStatusMessage.VOTED.getMessage(), voteMessage4);
        assertEquals(ResponseStatusMessage.VOTED.getMessage(), voteMessage5);
        assertEquals(CandidateError.CANDIDATE_NOT_REGISTERED.getErrorString(), voteMessage6);
        assertEquals(ElectionError.ALREADY_VOTED.getErrorString(), voteMessage7);
    }

    @Test
    public void testStopVoting() throws Exception {
        //arrange
        AddCandidateDtoRequest candidateRequest1 = new AddCandidateDtoRequest(token1, "1");
        AddCandidateDtoResponse candidateResponse1 = voterService.registerNewCandidate(candidateRequest1);
        String candidateId1 = candidateResponse1.getId();
        AddCandidateDtoRequest candidateRequest2 = new AddCandidateDtoRequest(token2, "2");
        AddCandidateDtoResponse candidateResponse2 = voterService.registerNewCandidate(candidateRequest2);
        String candidateId2 = candidateResponse2.getId();
        VoteInElectionsDtoRequest voteRequest1 = new VoteInElectionsDtoRequest(token1, "0");
        electionService.voteInElections(voteRequest1);
        VoteInElectionsDtoRequest voteRequest2 = new VoteInElectionsDtoRequest(token2, candidateId1);
        electionService.voteInElections(voteRequest2);
        VoteInElectionsDtoRequest voteRequest3 = new VoteInElectionsDtoRequest(token3, candidateId1);
        electionService.voteInElections(voteRequest3);
        VoteInElectionsDtoRequest voteRequest4 = new VoteInElectionsDtoRequest(token4, candidateId1);
        electionService.voteInElections(voteRequest4);
        VoteInElectionsDtoRequest voteRequest5 = new VoteInElectionsDtoRequest(token5, candidateId2);
        electionService.voteInElections(voteRequest5);
        VoteInElectionsDtoRequest voteRequest6 = new VoteInElectionsDtoRequest(token6, "0");
        electionService.voteInElections(voteRequest6);
        //act
        StopVotingDtoRequest stopRequest = new StopVotingDtoRequest(token1);
        StopVotingDtoResponse stopResponse = electionService.stopVoting(stopRequest);
        String resultMessage = stopResponse.getMessage();
        //assert
        assertEquals(0, dbms.getVoteMap().size());
        assertEquals(fullName1 + ResponseStatusMessage.WINNER.getMessage(), resultMessage);
        assertFalse(server.isVotingStatus());
    }
}
