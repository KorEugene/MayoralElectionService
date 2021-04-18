package net.thumbtack.school.elections.validators;

import net.thumbtack.school.elections.dao.*;
import net.thumbtack.school.elections.daoimpl.*;
import net.thumbtack.school.elections.exceptionimpl.CandidateError;
import net.thumbtack.school.elections.exceptionimpl.CandidateException;
import net.thumbtack.school.elections.exceptionimpl.VoterError;
import net.thumbtack.school.elections.exceptionimpl.VoterException;
import net.thumbtack.school.elections.model.Account;
import net.thumbtack.school.elections.model.Address;
import net.thumbtack.school.elections.model.Candidate;
import net.thumbtack.school.elections.model.CandidateNominateRequest;
import net.thumbtack.school.elections.model.CandidateProgram;
import net.thumbtack.school.elections.model.FullName;
import net.thumbtack.school.elections.model.Offer;
import net.thumbtack.school.elections.model.Person;
import net.thumbtack.school.elections.model.Rating;
import net.thumbtack.school.elections.model.Voter;
import net.thumbtack.school.elections.request.AddCandidateDtoRequest;
import net.thumbtack.school.elections.server.DataBase;
import net.thumbtack.school.elections.server.DataBaseManagementSystem;
import net.thumbtack.school.elections.server.TokenGenerator;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class TestNominateCandidateValidator {

    private static final int AUTHOR_RATING_VALUE = 5;

    private DataBaseManagementSystem dbms = DataBaseManagementSystem.getInstance();
    private VoterDao voterDao = VoterDaoImpl.getInstance();
    private OfferDao offerDao = OfferDaoImpl.getInstance();
    private RatingDao ratingDao = RatingDaoImpl.getInstance();
    private CandidateDao candidateDao = CandidateDaoImpl.getInstance();
    private CandidateProgramDao candidateProgramDao = CandidateProgramDaoImpl.getInstance();
    private NominateRequestDao nominateRequestDao = NominateRequestDaoImpl.getInstance();
    private NominateCandidateValidator validator = new NominateCandidateValidator();
    private static FullName fullName = new FullName("Ivan", "Ivanov", "Ivanovich");
    private static Address address = new Address("Fedora Abramova", "15", "27");
    private static Account account = new Account("IVA", "123456");
    private static Voter voter = new Voter(new Person(fullName, address, account));
    private static String content = "use your brain";

    @Before
    public void generateTokens() {
        voter.setToken(TokenGenerator.generateNewToken());
    }

    @After
    public void cleanData() {
        DataBase.clearInstance();
    }

    @Test
    public void shouldReturnVoterExceptionWhenValidateRegisterCandidateRequestForAbsentVoter() throws Exception {
        //arrange
        String voterId = voterDao.addNewVoter(voter);
        String token = dbms.getVoterMap().get(voterId).getToken();
        Offer offer = new Offer(voterId, content);
        String offerId = offerDao.addNewOffer(offer);
        Rating rating = new Rating(offerId, voterId, AUTHOR_RATING_VALUE);
        String message = ratingDao.addNewRating(rating);
        //act
        AddCandidateDtoRequest request = new AddCandidateDtoRequest(token, "2");
        try {
            validator.validateRegisterCandidateRequest(request.getNomineeId());
        } catch (VoterException ex) {
            //assert
            assertEquals(1, dbms.getVoterMap().size());
            assertEquals(VoterError.VOTER_DOES_NOT_EXIST, ex.getErrorCode());
        }
    }

    @Test
    public void shouldReturnCandidateExceptionWhenValidateRegisterCandidateRequestForExistingCandidate() throws Exception {
        //arrange
        String voterId = voterDao.addNewVoter(voter);
        dbms.getVoterMap().get(voterId).getToken();
        Offer offer = new Offer(voterId, content);
        String offerId = offerDao.addNewOffer(offer);
        Rating rating = new Rating(offerId, voterId, AUTHOR_RATING_VALUE);
        ratingDao.addNewRating(rating);
        Candidate candidate = new Candidate(voter);
        String candidateId = candidateDao.addNewCandidate(candidate);
        CandidateProgram candidateProgram = new CandidateProgram(candidateId, offer);
        candidateProgramDao.addCandidateProgram(candidateProgram);
        //act
        String initiatorToken = voterDao.getVoterTokenById(voterId);
        AddCandidateDtoRequest request = new AddCandidateDtoRequest(initiatorToken, voterId);
        try {
            validator.validateRegisterCandidateRequest(request.getNomineeId());
        } catch (CandidateException ex) {
            //assert
            assertEquals(1, dbms.getCandidateMap().size());
            assertEquals(candidateId, dbms.getCandidateMap().get(candidateId).getId());
            assertEquals(CandidateError.CANDIDATE_ALREADY_REGISTERED, ex.getErrorCode());
        }
    }

    @Test
    public void shouldReturnCandidateExceptionWhenValidateNominateCandidateRequestForExistingNominateRequest() throws Exception {
        //arrange
        String voterId = voterDao.addNewVoter(voter);
        Offer offer = new Offer(voterId, content);
        String offerId = offerDao.addNewOffer(offer);
        Rating rating = new Rating(offerId, voterId, AUTHOR_RATING_VALUE);
        ratingDao.addNewRating(rating);
        FullName fullName2 = new FullName("Petr", "Petrov", "Petrovich");
        Address address2 = new Address("Primorskaya", "35", "4");
        Account account2 = new Account("PETYA", "654321");
        Voter voter2 = new Voter(new Person(fullName2, address2, account2));
        voter2.setToken(TokenGenerator.generateNewToken());
        String content2 = "do not be stupid!";
        String voterId2 = voterDao.addNewVoter(voter2);
        Offer offer2 = new Offer(voterId2, content2);
        String offerId2 = offerDao.addNewOffer(offer2);
        Rating rating2 = new Rating(offerId2, voterId2, AUTHOR_RATING_VALUE);
        ratingDao.addNewRating(rating2);
        String nomineeToken = voterDao.getVoterTokenById(voterId2);
        CandidateNominateRequest candidateNominateRequest = new CandidateNominateRequest(nomineeToken, voterId);
        String nominateRequestId = nominateRequestDao.addNominateRequest(candidateNominateRequest);
        //act
        String initiatorToken = voterDao.getVoterTokenById(voterId);
        AddCandidateDtoRequest request = new AddCandidateDtoRequest(initiatorToken, voterId2);
        try {
            validator.validateRegisterCandidateRequest(request.getNomineeId());
        } catch (CandidateException ex) {
            //assert
            assertEquals(1, dbms.getNominateRequests().size());
            assertEquals(nominateRequestId, dbms.getNominateRequests().get(nominateRequestId).getId());
            assertEquals(CandidateError.NOMINATE_REQUEST_ALREADY_REGISTERED, ex.getErrorCode());
        }
    }
}
