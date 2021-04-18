package net.thumbtack.school.elections.daoimpl;

import net.thumbtack.school.elections.dao.CandidateDao;
import net.thumbtack.school.elections.dao.CandidateProgramDao;
import net.thumbtack.school.elections.dao.OfferDao;
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

public class TestCandidateProgramDaoImpl {

    private DataBaseManagementSystem dbms = DataBaseManagementSystem.getInstance();
    private VoterDao voterDao = VoterDaoImpl.getInstance();
    private OfferDao offerDao = OfferDaoImpl.getInstance();
    private CandidateDao candidateDao = CandidateDaoImpl.getInstance();
    private CandidateProgramDao candidateProgramDao = CandidateProgramDaoImpl.getInstance();
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
    private static Candidate candidate1 = new Candidate(voter1);
    private static String candidateId1;

    @Before
    public void generateData() {
        voter1.setToken(TokenGenerator.generateNewToken());
        voter2.setToken(TokenGenerator.generateNewToken());
        voterId1 = voterDao.addNewVoter(voter1);
        voterId2 = voterDao.addNewVoter(voter2);
        offerId1 = offerDao.addNewOffer(offer1);
        offerId2 = offerDao.addNewOffer(offer2);
        candidateId1 = candidateDao.addNewCandidate(candidate1);
    }

    @After
    public void cleanData() {
        DataBase.clearInstance();
    }

    @Test
    public void shouldAddOfferToCandidateProgram() throws Exception {
        //act
        CandidateProgram candidateProgram1 = new CandidateProgram(candidateId1, offer1);
        String candidateProgramId1 = candidateProgramDao.addCandidateProgram(candidateProgram1);
        CandidateProgram candidateProgram2 = new CandidateProgram(candidateId1, offer2);
        String candidateProgramId2 = candidateProgramDao.addCandidateProgram(candidateProgram2);
        //assert
        assertEquals(2, dbms.getOfferMap().size());
        assertEquals(2, dbms.getProgramMap().size());
        assertEquals(offerId1, candidateProgramId1);
        assertEquals(offerId2, candidateProgramId2);
        assertTrue(dbms.getProgramMap().containsValue(candidateProgram1));
        assertTrue(dbms.getProgramMap().containsValue(candidateProgram2));
    }

    @Test
    public void shouldReturnCandidateProgramByIdFromDataBase() throws Exception {
        //arrange
        CandidateProgram candidateProgram1 = new CandidateProgram(candidateId1, offer1);
        String candidateProgramId1 = candidateProgramDao.addCandidateProgram(candidateProgram1);
        CandidateProgram candidateProgram2 = new CandidateProgram(candidateId1, offer2);
        String candidateProgramId2 = candidateProgramDao.addCandidateProgram(candidateProgram2);
        //act
        CandidateProgram cloneCandidateProgram1 = candidateProgramDao.getCandidateProgramById(candidateProgramId1);
        CandidateProgram cloneCandidateProgram2 = candidateProgramDao.getCandidateProgramById(candidateProgramId2);
        //assert
        assertEquals(candidateProgram1, cloneCandidateProgram1);
        assertEquals(candidateProgram2, cloneCandidateProgram2);
    }

    @Test
    public void shouldRemoveOfferFromCandidateProgram() throws Exception {
        //arrange
        CandidateProgram candidateProgram1 = new CandidateProgram(candidateId1, offer1);
        String candidateProgramId1 = candidateProgramDao.addCandidateProgram(candidateProgram1);
        CandidateProgram candidateProgram2 = new CandidateProgram(candidateId1, offer2);
        String candidateProgramId2 = candidateProgramDao.addCandidateProgram(candidateProgram2);
        //act
        String responseMessage1 = candidateProgramDao.removeCandidateProgram(candidateProgramId1);
        String responseMessage2 = candidateProgramDao.removeCandidateProgram(candidateProgramId2);
        //assert
        assertEquals(2, dbms.getOfferMap().size());
        assertEquals(0, dbms.getProgramMap().size());
        assertEquals(ResponseStatusMessage.OFFER_REMOVED_FROM_CANDIDATE_PROGRAM.getMessage(), responseMessage1);
        assertEquals(ResponseStatusMessage.OFFER_REMOVED_FROM_CANDIDATE_PROGRAM.getMessage(), responseMessage2);
        assertFalse(dbms.getProgramMap().containsValue(candidateProgram1));
        assertFalse(dbms.getProgramMap().containsValue(candidateProgram2));
    }

    @Test
    public void shouldReturnProgramList() throws Exception {
        //arrange
        CandidateProgram candidateProgram1 = new CandidateProgram(candidateId1, offer1);
        String candidateProgramId1 = candidateProgramDao.addCandidateProgram(candidateProgram1);
        CandidateProgram candidateProgram2 = new CandidateProgram(candidateId1, offer2);
        String candidateProgramId2 = candidateProgramDao.addCandidateProgram(candidateProgram2);
        //act
        List<CandidateProgram> programs1 = candidateProgramDao.getPrograms();
        //assert
        assertEquals(2, programs1.size());
        assertTrue(programs1.contains(dbms.getProgramMap().get(candidateProgramId1)));
        assertTrue(programs1.contains(dbms.getProgramMap().get(candidateProgramId2)));
    }

    @Test
    public void shouldReturnProgramIdByCandidateIdAndOfferId() throws Exception {
        //arrange
        CandidateProgram candidateProgram1 = new CandidateProgram(candidateId1, offer1);
        String candidateProgramId1 = candidateProgramDao.addCandidateProgram(candidateProgram1);
        CandidateProgram candidateProgram2 = new CandidateProgram(candidateId1, offer2);
        String candidateProgramId2 = candidateProgramDao.addCandidateProgram(candidateProgram2);
        //act
        String programId1 = candidateProgramDao.getCandidateProgramIdByCandidateIdAndOfferId(candidateId1, offerId1);
        String programId2 = candidateProgramDao.getCandidateProgramIdByCandidateIdAndOfferId(candidateId1, offerId2);
        //assert
        assertEquals(2, dbms.getProgramMap().size());
        assertEquals(candidateProgramId1, programId1);
        assertEquals(candidateProgramId2, programId2);
    }
}
