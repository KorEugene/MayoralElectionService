package net.thumbtack.school.elections.serializerimpl;

import net.thumbtack.school.elections.model.Account;
import net.thumbtack.school.elections.model.Address;
import net.thumbtack.school.elections.model.FullName;
import net.thumbtack.school.elections.request.*;
import net.thumbtack.school.elections.response.AddCandidateDtoResponse;
import net.thumbtack.school.elections.response.AddOfferDtoResponse;
import net.thumbtack.school.elections.response.RegisterVoterDtoResponse;
import net.thumbtack.school.elections.server.DataBase;
import net.thumbtack.school.elections.server.DataBaseManagementSystem;
import net.thumbtack.school.elections.server.Server;
import net.thumbtack.school.elections.service.ElectionService;
import net.thumbtack.school.elections.service.VoterService;
import org.junit.After;
import org.junit.Test;

import java.io.File;

import static org.junit.Assert.*;

public class TestDataBaseJsonReader {

    private static Server server = Server.getInstance();
    private static VoterService voterService = new VoterService();
    private static ElectionService electionService = new ElectionService();
    private static DataBaseManagementSystem dbms = DataBaseManagementSystem.getInstance();
    private static FullName fullName1 = new FullName("Ivan", "Ivanov", "Ivanovich");
    private static Address address1 = new Address("Fedora Abramova", "15", "27");
    private static Account account1 = new Account("IVA", "123456");
    private static FullName fullName2 = new FullName("Petr", "Petrov", "Petrovich");
    private static Address address2 = new Address("Primorskaya", "35", "4");
    private static Account account2 = new Account("PETYA", "654321");

    @After
    public void cleanData() {
        DataBase.clearInstance();
    }

    @Test
    public void testLoadDataBase() throws Exception {
        //arrange
        RegisterVoterDtoRequest registerVoterDtoRequest1 = new RegisterVoterDtoRequest(fullName1, address1, account1);
        RegisterVoterDtoResponse voterResponse1 = voterService.registerNewVoter(registerVoterDtoRequest1);
        String token1 = voterResponse1.getToken();
        RegisterVoterDtoRequest registerVoterDtoRequest2 = new RegisterVoterDtoRequest(fullName2, address2, account2);
        RegisterVoterDtoResponse voterResponse2 = voterService.registerNewVoter(registerVoterDtoRequest2);
        String token2 = voterResponse2.getToken();
        String content1 = "use your brain!";
        AddOfferDtoRequest addOfferDtoRequest1 = new AddOfferDtoRequest(token1, content1);
        AddOfferDtoResponse offerResponse1 = voterService.addNewOffer(addOfferDtoRequest1);
        String offerId1 = offerResponse1.getOfferId();
        String content2 = "do not be stupid!";
        AddOfferDtoRequest addOfferDtoRequest2 = new AddOfferDtoRequest(token2, content2);
        AddOfferDtoResponse offerResponse2 = voterService.addNewOffer(addOfferDtoRequest2);
        String offerId2 = offerResponse2.getOfferId();
        RateOfferDtoRequest rateOfferDtoRequest1 = new RateOfferDtoRequest(token1, offerId2, "1");
        voterService.rateOffer(rateOfferDtoRequest1);
        RateOfferDtoRequest rateOfferDtoRequest3 = new RateOfferDtoRequest(token2, offerId1, "3");
        voterService.rateOffer(rateOfferDtoRequest3);
        AddCandidateDtoRequest request1 = new AddCandidateDtoRequest(token1, "2");
        voterService.registerNewCandidate(request1);
        AddCandidateDtoRequest request2 = new AddCandidateDtoRequest(token1, "1");
        AddCandidateDtoResponse response2 = voterService.registerNewCandidate(request2);
        String candidateId2 = response2.getId();
        VoteInElectionsDtoRequest voteRequest2 = new VoteInElectionsDtoRequest(token2, candidateId2);
        electionService.voteInElections(voteRequest2);
        File file = new File("testSaveData.txt").getAbsoluteFile();
        server.stopServer(file.getAbsolutePath());
        //act
        server.startServer(file.getAbsolutePath());
        //assert
        assertEquals(3, dbms.getVoterId());
        assertEquals(3, dbms.getOfferId());
        assertEquals(5, dbms.getRatingId());
        assertEquals(2, dbms.getCandidateId());
        assertEquals(2, dbms.getProgramId());
        assertEquals(2, dbms.getNominateRequestId());
        assertEquals(2, dbms.getVoteId());
        assertEquals(2, dbms.getTokenMap().size());
        assertEquals(2, dbms.getPersonMap().size());
        assertEquals(2, dbms.getVoterMap().size());
        assertEquals(2, dbms.getOfferMap().size());
        assertEquals(4, dbms.getRatingMap().size());
        assertEquals(1, dbms.getCandidateMap().size());
        assertEquals(1, dbms.getProgramMap().size());
        assertEquals(1, dbms.getNominateRequests().size());
        assertEquals(1, dbms.getVoteMap().size());
    }
}
