package net.thumbtack.school.elections.validators;

import net.thumbtack.school.elections.exceptionimpl.CandidateError;
import net.thumbtack.school.elections.exceptionimpl.CandidateException;
import org.junit.Test;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.fail;

public class TestCandidateValidator {

    private CandidateValidator validator = new CandidateValidator();

    @Test
    public void shouldReturnCandidateExceptionWhenValidateCandidateWhichOneDoesNotExist() throws Exception {
        //act
        try {
            validator.validateCandidateIsExist(null);
            fail();
        } catch (CandidateException ex) {
            //assert
            assertEquals(CandidateError.CANDIDATE_NOT_REGISTERED, ex.getErrorCode());
        }
    }

    @Test
    public void shouldReturnCandidateExceptionWhenCandidateTryRejectHisOwnOffers() throws Exception {
        //act
        try {
            validator.validateAuthorForCandidateProgramModification("1", "1");
            fail();
        } catch (CandidateException ex) {
            //assert
            assertEquals(CandidateError.AUTHOR_REJECTS_HIS_OFFERS, ex.getErrorCode());
        }
    }

    @Test
    public void shouldReturnCandidateExceptionWhenValidateRegisteredCandidate() throws Exception {
        //act
        try {
            validator.validateCandidacyIsActive(true);
            fail();
        } catch (CandidateException ex) {
            //assert
            assertEquals(CandidateError.CANDIDACY_ACTIVE, ex.getErrorCode());
        }
    }
}
