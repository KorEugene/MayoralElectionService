package net.thumbtack.school.elections.validators;

import net.thumbtack.school.elections.exceptionimpl.*;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

public class TestObjectSavedToDataBaseValidator {

    private ObjectSavedToDataBaseValidator validator = new ObjectSavedToDataBaseValidator();

    @Test
    public void shouldReturnVoterExceptionIfVoterWasNotSavedToDataBase() throws Exception {
        try {
            //act
            validator.validateVoterId(null);
            fail();
        } catch (VoterException ex) {
            //assert
            assertEquals(VoterError.VOTER_WAS_NOT_SAVED, ex.getErrorCode());
        }
    }

    @Test
    public void shouldReturnOfferExceptionIfOfferWasNotSavedToDataBase() throws Exception {
        try {
            //act
            validator.validateOfferId(null);
            fail();
        } catch (OfferException ex) {
            //assert
            assertEquals(OfferError.OFFER_WAS_NOT_SAVED, ex.getErrorCode());
        }
    }

    @Test
    public void shouldReturnCandidateExceptionIfCandidateWasNotSavedToDataBase() throws Exception {
        try {
            //act
            validator.validateCandidateId(null);
            fail();
        } catch (CandidateException ex) {
            //assert
            assertEquals(CandidateError.CANDIDATE_WAS_NOT_SAVED, ex.getErrorCode());
        }
    }

    @Test
    public void shouldReturnCandidateExceptionIfNominateRequestWasNotSavedToDataBase() throws Exception {
        try {
            //act
            validator.validateNominateRequestId(null);
            fail();
        } catch (CandidateException ex) {
            //assert
            assertEquals(CandidateError.NOMINATE_REQUEST_WAS_NOT_SAVED, ex.getErrorCode());
        }
    }

    @Test
    public void shouldReturnCandidateExceptionIfCandidateProgramWasNotSaved() throws Exception {
        try {
            //act
            validator.validateCandidateProgramId(null);
            fail();
        } catch (CandidateException ex) {
            //assert
            assertEquals(CandidateError.CANDIDATE_PROGRAM_WAS_NOT_SAVED, ex.getErrorCode());
        }
    }

    @Test
    public void shouldReturnElectionExceptionIfVoteWasNotSaved() throws Exception {
        try {
            //act
            validator.validateVoteId(null);
            fail();
        } catch (ElectionException ex) {
            //assert
            assertEquals(ElectionError.VOTE_WAS_NOT_SAVED, ex.getErrorCode());
        }
    }
}
