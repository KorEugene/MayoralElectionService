package net.thumbtack.school.elections.validators;

import net.thumbtack.school.elections.exceptionimpl.ElectionError;
import net.thumbtack.school.elections.exceptionimpl.ElectionException;
import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

public class TestElectionValidator {

    private ElectionValidator validator = new ElectionValidator();

    @Test
    public void shouldReturnElectionExceptionIfGotRequestWhenVotingStarted() throws Exception {
        try {
            //act
            validator.validateVotingStarted(true);
            fail();
        } catch (ElectionException ex) {
            //assert
            assertEquals(ElectionError.VOTING_ALREADY_STARTED, ex.getErrorCode());
        }
    }

    @Test
    public void shouldReturnElectionExceptionIfGotRequestWhenVotingNotStarted() throws Exception {
        try {
            //act
            validator.validateVotingNotStarted(false);
            fail();
        } catch (ElectionException ex) {
            //assert
            assertEquals(ElectionError.VOTING_NOT_STARTED, ex.getErrorCode());
        }
    }

    @Test
    public void shouldReturnElectionExceptionIfCandidateListIsEmpty() throws Exception {
        try {
            //act
            validator.validateCandidateList(new ArrayList<>());
            fail();
        } catch (ElectionException ex) {
            //assert
            assertEquals(ElectionError.NO_CANDIDATES_FOR_MAYOR, ex.getErrorCode());
        }
    }

    @Test
    public void shouldReturnElectionExceptionIfVoterAlreadyVoted() throws Exception {
        try {
            //act
            validator.validateVote(true);
            fail();
        } catch (ElectionException ex) {
            //assert
            assertEquals(ElectionError.ALREADY_VOTED, ex.getErrorCode());
        }
    }

    @Test
    public void shouldReturnElectionExceptionIfDataBaseWasNotCleaned() throws Exception {
        try {
            //act
            validator.validateDataBaseCleanup("Fail!");
        } catch (ElectionException ex) {
            //assert
            assertEquals(ElectionError.DATABASE_WAS_NOT_CLEANED, ex.getErrorCode());
        }
    }
}
