package net.thumbtack.school.elections.daoimpl;

import net.thumbtack.school.elections.dao.CandidateDao;
import net.thumbtack.school.elections.dao.VoterDao;
import net.thumbtack.school.elections.model.*;
import net.thumbtack.school.elections.server.DataBase;
import net.thumbtack.school.elections.server.DataBaseManagementSystem;
import net.thumbtack.school.elections.server.TokenGenerator;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.*;

public class TestCandidateDaoImpl {

    private DataBaseManagementSystem dbms = DataBaseManagementSystem.getInstance();
    private VoterDao voterDao = VoterDaoImpl.getInstance();
    private CandidateDao candidateDao = CandidateDaoImpl.getInstance();
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
    public void shouldAddNewCandidateToDataBase() throws Exception {
        //act
        Candidate candidate1 = new Candidate(voter1);
        String candidateId1 = candidateDao.addNewCandidate(candidate1);
        //assert
        assertEquals(1, dbms.getCandidateMap().size());
        assertEquals("1", candidateId1);
    }

    @Test
    public void shouldReturnCandidateFromDataBaseById() throws Exception {
        //arrange
        Candidate candidate1 = new Candidate(voter1);
        String candidateId = candidateDao.addNewCandidate(candidate1);
        //act
        Candidate cloneCandidate1 = candidateDao.getCandidateById(candidateId);
        //assert
        assertEquals(candidate1, cloneCandidate1);
    }

    @Test
    public void shouldReturnCandidateFromDataBaseByToken() throws Exception {
        //arrange
        Candidate candidate1 = new Candidate(voter1);
        candidateDao.addNewCandidate(candidate1);
        //act
        String candidateToken1 = voter1.getToken();
        Candidate cloneCandidate1 = candidateDao.getCandidateByToken(candidateToken1);
        //assert
        assertEquals(candidate1, cloneCandidate1);
    }

    @Test
    public void shouldDeleteCandidateFromDataBase() throws Exception {
        //arrange
        Candidate candidate1 = new Candidate(voter1);
        String candidateId1 = candidateDao.addNewCandidate(candidate1);
        //act
        String responseMessage1 = candidateDao.deleteCandidate(candidateId1);
        //assert
        assertEquals(0, dbms.getCandidateMap().size());
        assertEquals(ResponseStatusMessage.CANDIDACY_HAS_BEEN_WITHDRAWN.getMessage(), responseMessage1);
    }

    @Test
    public void shouldReturnCandidateList() throws Exception {
        //arrange
        Candidate candidate1 = new Candidate(voter1);
        String candidateId1 = candidateDao.addNewCandidate(candidate1);
        Candidate candidate2 = new Candidate(voter2);
        String candidateId2 = candidateDao.addNewCandidate(candidate2);
        //act
        List<Candidate> candidates1 = candidateDao.getCandidates();
        //assert
        assertEquals(2, candidates1.size());
        assertEquals(dbms.getCandidateMap().get(candidateId1), candidates1.get(0));
        assertEquals(dbms.getCandidateMap().get(candidateId2), candidates1.get(1));
    }

    @Test
    public void shouldReturnTrueIfCandidateIsExist() throws Exception {
        //arrange
        Candidate candidate1 = new Candidate(voter1);
        candidateDao.addNewCandidate(candidate1);
        //act
        boolean expression1 = candidateDao.isRegisteredCandidate(voterId1);
        boolean expression2 = candidateDao.isRegisteredCandidate(voterId2);
        //assert
        assertTrue(expression1);
        assertFalse(expression2);
    }
}
