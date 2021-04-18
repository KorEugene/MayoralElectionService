package net.thumbtack.school.elections.validators;

import net.thumbtack.school.elections.exceptionimpl.VoterError;
import net.thumbtack.school.elections.exceptionimpl.VoterException;
import net.thumbtack.school.elections.request.AddOfferDtoRequest;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

public class TestTokenValidator {

    private static TokenValidator validator = new TokenValidator();

    @Test
    public void shouldReturnVoterExceptionWhenValidateSomeRequestWithInvalidToken() {
        //arrange
        AddOfferDtoRequest request = new AddOfferDtoRequest("123", "some test");
        try {
            //act
            validator.validateToken(request.getToken());
            fail();
        } catch (VoterException ex) {
            //assert
            assertEquals(VoterError.VOTER_NOT_VALID_TOKEN, ex.getErrorCode());
        }
    }
}
