package net.thumbtack.school.elections.daoimpl;

import net.thumbtack.school.elections.dao.NominateRequestDao;
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
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class TestNominateRequestDaoImpl {

    private DataBaseManagementSystem dbms = DataBaseManagementSystem.getInstance();
    private VoterDao voterDao = VoterDaoImpl.getInstance();
    private NominateRequestDao nominateRequestDao = NominateRequestDaoImpl.getInstance();
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

    @Before
    public void generateData() {
        voter1.setToken(TokenGenerator.generateNewToken());
        voter2.setToken(TokenGenerator.generateNewToken());
        voterId1 = voterDao.addNewVoter(voter1);
        voterId2 = voterDao.addNewVoter(voter2);
    }

    @After
    public void cleanData() {
        DataBase.clearInstance();
    }

    @Test
    public void shouldAddNewNominateRequestToDataBase() throws Exception {
        //act
        CandidateNominateRequest candidateNominateRequest1 = new CandidateNominateRequest(voterId2, voterId1);
        nominateRequestDao.addNominateRequest(candidateNominateRequest1);
        CandidateNominateRequest candidateNominateRequest2 = new CandidateNominateRequest(voterId1, voterId2);
        nominateRequestDao.addNominateRequest(candidateNominateRequest2);
        //assert
        assertEquals(0, dbms.getCandidateMap().size());
        assertEquals(2, dbms.getNominateRequests().size());
        assertTrue(dbms.getNominateRequests().containsValue(candidateNominateRequest1));
        assertTrue(dbms.getNominateRequests().containsValue(candidateNominateRequest2));
    }

    @Test
    public void shouldReturnListOfRequestsToVoterByToken() throws Exception {
        //arrange
        CandidateNominateRequest candidateNominateRequest1 = new CandidateNominateRequest(voterId2, voterId1);
        String requestId1 = nominateRequestDao.addNominateRequest(candidateNominateRequest1);
        CandidateNominateRequest candidateNominateRequest2 = new CandidateNominateRequest(voterId1, voterId2);
        String requestId2 = nominateRequestDao.addNominateRequest(candidateNominateRequest2);
        //act
        List<CandidateNominateRequest> requestList1 = nominateRequestDao.getNominateRequestsByVoterId(voterId2);
        List<CandidateNominateRequest> requestList2 = nominateRequestDao.getNominateRequestsByVoterId(voterId1);
        //assert
        assertEquals(1, requestList1.size());
        assertEquals(1, requestList2.size());
        assertTrue(requestList1.contains(dbms.getNominateRequests().get(requestId1)));
        assertTrue(requestList2.contains(dbms.getNominateRequests().get(requestId2)));
    }

    @Test
    public void shouldDeleteNominateRequestFromDataBaseAndReturnMessageByAnswer() throws Exception {
        //arrange
        CandidateNominateRequest candidateNominateRequest1 = new CandidateNominateRequest(voterId2, voterId1);
        String requestId1 = nominateRequestDao.addNominateRequest(candidateNominateRequest1);
        CandidateNominateRequest candidateNominateRequest2 = new CandidateNominateRequest(voterId1, voterId2);
        nominateRequestDao.addNominateRequest(candidateNominateRequest2);
        //act
        String message1 = nominateRequestDao.removeNominateRequest(requestId1);
        //assert
        assertEquals(1, dbms.getNominateRequests().size());
        assertEquals(ResponseStatusMessage.NOMINATE_REQUEST_REMOVED.getMessage(), message1);
    }

    @Test
    public void shouldReturnTrueIfNominateRequestIsExist() throws Exception {
        //arrange
        CandidateNominateRequest candidateNominateRequest1 = new CandidateNominateRequest(voterId2, voterId1);
        nominateRequestDao.addNominateRequest(candidateNominateRequest1);
        //act
        boolean expression1 = nominateRequestDao.isRegisteredNominateRequest(voterId1);
        boolean expression2 = nominateRequestDao.isRegisteredNominateRequest(voterId2);
        //assert
        assertFalse(expression1);
        assertTrue(expression2);
    }
}
