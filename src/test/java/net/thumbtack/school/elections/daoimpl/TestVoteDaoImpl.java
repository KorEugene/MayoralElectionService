package net.thumbtack.school.elections.daoimpl;

import net.thumbtack.school.elections.dao.CandidateDao;
import net.thumbtack.school.elections.dao.VoteDao;
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

public class TestVoteDaoImpl {

    private DataBaseManagementSystem dbms = DataBaseManagementSystem.getInstance();
    private VoteDao voteDao = VoteDaoImpl.getInstance();
    private VoterDao voterDao = VoterDaoImpl.getInstance();
    private CandidateDao candidateDao = CandidateDaoImpl.getInstance();
    private static FullName fullName1 = new FullName("Ivan", "Ivanov", "Ivanovich");
    private static Address address1 = new Address("Fedora Abramova", "15", "27");
    private static Account account1 = new Account("IVA", "123456");
    private static Voter voter1 = new Voter(new Person(fullName1, address1, account1));
    private static FullName fullName2 = new FullName("Petr", "Petrov", "Petrovich");
    private static Address address2 = new Address("Primorskaya", "35", "4");
    private static Account account2 = new Account("PETYA", "654321");
    private static Voter voter2 = new Voter(new Person(fullName2, address2, account2));

    @Before
    public void generateTokens() {
        voter1.setToken(TokenGenerator.generateNewToken());
        voter2.setToken(TokenGenerator.generateNewToken());
    }

    @After
    public void cleanData() {
        DataBase.clearInstance();
    }

    @Test
    public void shouldAddVoteToDataBase() throws Exception {
        //arrange
        String voterId1 = voterDao.addNewVoter(voter1);
        voterDao.addNewVoter(voter2);
        Candidate candidate2 = new Candidate(voter2);
        String candidateId2 = candidateDao.addNewCandidate(candidate2);
        Vote vote1 = new Vote(voterId1, candidateId2);
        //act
        String voteId1 = voteDao.addVote(vote1);
        //assert
        assertEquals(1, dbms.getVoteMap().size());
        assertEquals("1", voteId1);
    }

    @Test
    public void shouldReturnTrueIfVoteIsExist() throws Exception {
        //arrange
        String voterId1 = voterDao.addNewVoter(voter1);
        String voterId2 = voterDao.addNewVoter(voter2);
        Candidate candidate2 = new Candidate(voter2);
        String candidateId2 = candidateDao.addNewCandidate(candidate2);
        Vote vote1 = new Vote(voterId1, candidateId2);
        voteDao.addVote(vote1);
        //act
        boolean expression1 = voteDao.isExistVote(voterId1);
        boolean expression2 = voteDao.isExistVote(voterId2);
        //arrange
        assertTrue(expression1);
        assertFalse(expression2);
    }

    @Test
    public void shouldReturnVoteListFromDataBase() throws Exception {
        //arrange
        String voterId1 = voterDao.addNewVoter(voter1);
        String voterId2 = voterDao.addNewVoter(voter2);
        Candidate candidate1 = new Candidate(voter1);
        String candidateId1 = candidateDao.addNewCandidate(candidate1);
        Candidate candidate2 = new Candidate(voter2);
        String candidateId2 = candidateDao.addNewCandidate(candidate2);
        Vote vote1 = new Vote(voterId1, candidateId2);
        voteDao.addVote(vote1);
        Vote vote2 = new Vote(voterId2, candidateId1);
        voteDao.addVote(vote2);
        //act
        List<Vote> resultList = voteDao.getVotes();
        //assert
        assertEquals(2, resultList.size());
        assertTrue(resultList.contains(vote1));
        assertTrue(resultList.contains(vote2));
    }

    @Test
    public void shouldDeleteAllVotesFromDataBase() throws Exception {
        //arrange
        String voterId1 = voterDao.addNewVoter(voter1);
        String voterId2 = voterDao.addNewVoter(voter2);
        Candidate candidate1 = new Candidate(voter1);
        String candidateId1 = candidateDao.addNewCandidate(candidate1);
        Candidate candidate2 = new Candidate(voter2);
        String candidateId2 = candidateDao.addNewCandidate(candidate2);
        Vote vote1 = new Vote(voterId1, candidateId2);
        voteDao.addVote(vote1);
        Vote vote2 = new Vote(voterId2, candidateId1);
        voteDao.addVote(vote2);
        //act
        String message = voteDao.deleteAllVotes();
        //assert
        assertEquals(0, dbms.getVoteMap().size());
        assertEquals(ResponseStatusMessage.ALL_VOTES_HAVE_BEEN_DELETED.getMessage(), message);
    }
}
