package net.thumbtack.school.elections.converterimpl;

import net.thumbtack.school.elections.converter.DataConverter;
import net.thumbtack.school.elections.exceptionimpl.CandidateError;
import net.thumbtack.school.elections.exceptionimpl.CandidateException;
import net.thumbtack.school.elections.request.*;
import net.thumbtack.school.elections.response.AddCandidateDtoResponse;
import net.thumbtack.school.elections.response.RateOfferDtoResponse;
import net.thumbtack.school.elections.response.RegisterVoterDtoResponse;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class TestDataJsonConverter {

    private DataConverter dataConverter = new DataJsonConverter();

    @Test
    public void shouldReturnRegisterVoterDtoRequestFromJsonString() throws Exception {
        //arrange
        String jsonString = "{\"fullName\":{\"firstName\":\"Ivan\",\"lastName\":\"Ivanov\",\"patronymicName\":\"Ivanovich\"}," +
                "\"address\":{\"street\":\"Fedora Abramova\",\"house\":\"15\",\"flat\":\"27\"}," +
                "\"account\":{\"login\":\"IVA\",\"password\":\"123456\"}}";
        //act
        RegisterVoterDtoRequest dto = dataConverter.getRequestForRegisterVoter(jsonString);
        //assert
        assertEquals("Ivan", dto.getFullName().getFirstName());
        assertEquals("Ivanov", dto.getFullName().getLastName());
        assertEquals("Ivanovich", dto.getFullName().getPatronymicName());
        assertEquals("Fedora Abramova", dto.getAddress().getStreet());
        assertEquals("15", dto.getAddress().getHouse());
        assertEquals("27", dto.getAddress().getFlat());
        assertEquals("IVA", dto.getAccount().getLogin());
        assertEquals("123456", dto.getAccount().getPassword());
    }

    @Test
    public void shouldReturnAddOfferDtoRequest() throws Exception {
        //arrange
        String jsonString = "{\"token\":\"1234-5678-9012-3456\",\"content\":\"easy json\"}";
        //act
        AddOfferDtoRequest dto = dataConverter.getRequestForAddOffer(jsonString);
        //assert
        assertEquals("1234-5678-9012-3456", dto.getToken());
        assertEquals("easy json", dto.getContent());
    }

    @Test
    public void shouldReturnRateOfferDtoRequest() throws Exception {
        //arrange
        String jsonString = "{\"token\":\"1234-5678-9012-3456\",\"offerId\":\"100\",\"value\":\"3\"}";
        //act
        RateOfferDtoRequest dto = dataConverter.getRequestForRateOffer(jsonString);
        //assert
        assertEquals("1234-5678-9012-3456", dto.getToken());
        assertEquals("100", dto.getOfferId());
        assertEquals("3", dto.getValue());
    }

    @Test
    public void shouldReturnGetOffersByAverageRatingDtoRequest() throws Exception {
        //arrange
        String jsonString = "{\"token\":\"1234-5678-9012-3456\"}";
        //act
        GetOffersByAverageRatingDtoRequest dto = dataConverter.getRequestForOfferListWithAverageRating(jsonString);
        //assert
        assertEquals("1234-5678-9012-3456", dto.getToken());
    }

    @Test
    public void shouldReturnGetOfferListByAuthorDtoRequest() throws Exception {
        //arrange
        String jsonString = "{\"token\":\"1234-5678-9012-3456\",\"authorId\":[\"2\",\"3\"]}";
        //act
        GetOfferListByAuthorDtoRequest dto = dataConverter.getRequestForOfferListByAuthor(jsonString);
        String[] resultArray = dto.getAuthorId();
        //assert
        assertEquals("1234-5678-9012-3456", dto.getToken());
        assertEquals("2", resultArray[0]);
        assertEquals("3", resultArray[1]);
    }

    @Test
    public void shouldReturnAddCandidateDtoRequest() throws Exception {
        //arrange
        String jsonString = "{\"token\":\"1234-5678-9012-3456\",\"nomineeId\":\"2\"}";
        //act
        AddCandidateDtoRequest dto = dataConverter.getRequestForRegisterCandidate(jsonString);
        //assert
        assertEquals("1234-5678-9012-3456", dto.getToken());
        assertEquals("2", dto.getNomineeId());
    }

    @Test
    public void shouldReturnCheckRequestsDtoRequest() throws Exception {
        //arrange
        String jsonString = "{\"token\":\"1234-5678-9012-3456\"}";
        //act
        CheckRequestsDtoRequest dto = dataConverter.getRequestForCheckNominateRequests(jsonString);
        //assert
        assertEquals("1234-5678-9012-3456", dto.getToken());
    }

    @Test
    public void shouldReturnAnswerNominateRequestDtoRequest() throws Exception {
        //arrange
        String jsonString = "{\"token\":\"1234-5678-9012-3456\",\"nominateRequestId\":\"1\",\"consent\":\"true\"}";
        //act
        AnswerNominateRequestDtoRequest dto = dataConverter.getRequestForAnswerNominateRequest(jsonString);
        //assert
        assertEquals("1234-5678-9012-3456", dto.getToken());
        assertEquals("1", dto.getNominateRequestId());
        assertTrue(dto.isConsent());
    }

    @Test
    public void shouldReturnUpdateCandidateProgramDtoRequest() throws Exception {
        //arrange
        String jsonString = "{\"token\":\"1234-5678-9012-3456\",\"offerId\":\"1\",\"addition\":\"true\"}";
        //act
        UpdateCandidateProgramDtoRequest dto = dataConverter.getRequestForUpdateCandidateProgram(jsonString);
        //assert
        assertEquals("1234-5678-9012-3456", dto.getToken());
        assertEquals("1", dto.getOfferId());
        assertTrue(dto.isAddition());
    }

    @Test
    public void shouldReturnWithdrawCandidateDtoRequest() throws Exception {
        //arrange
        String jsonString = "{\"token\":\"1234-5678-9012-3456\"}";
        //act
        WithdrawCandidateDtoRequest dto = dataConverter.getRequestForWithdrawCandidate(jsonString);
        //assert
        assertEquals("1234-5678-9012-3456", dto.getToken());
    }

    @Test
    public void shouldGetCandidateListWithProgramsDtoRequest() throws Exception {
        //arrange
        String jsonString = "{\"token\":\"1234-5678-9012-3456\"}";
        //act
        GetCandidateListWithProgramsDtoRequest dto = dataConverter.getRequestForCandidateListWithPrograms(jsonString);
        //assert
        assertEquals("1234-5678-9012-3456", dto.getToken());
    }

    @Test
    public void shouldReturnLogOnDtoRequest() throws Exception {
        //arrange
        String jsonString = "{\"login\":\"loginator\",\"password\":\"123456\"}";
        //act
        LogOnDtoRequest dto = dataConverter.getRequestForVoterLogOn(jsonString);
        //assert
        assertEquals("loginator", dto.getLogin());
        assertEquals("123456", dto.getPassword());
    }

    @Test
    public void shouldReturnLogOutDtoRequest() throws Exception {
        //arrange
        String jsonString = "{\"token\":\"1234-5678-9012-3456\"}";
        //act
        LogOutDtoRequest dto = dataConverter.getRequestForVoterLogOut(jsonString);
        //assert
        assertEquals("1234-5678-9012-3456", dto.getToken());
    }

    @Test
    public void shouldReturnStartVotingDtoRequest() throws Exception {
        //arrange
        String jsonString = "{\"token\":\"1234-5678-9012-3456\"}";
        //act
        StartVotingDtoRequest dto = dataConverter.getRequestForStartVoting(jsonString);
        //assert
        assertEquals("1234-5678-9012-3456", dto.getToken());
    }

    @Test
    public void shouldReturnVoteInElectionsDtoRequest() throws Exception {
        //arrange
        String jsonString = "{\"token\":\"1234-5678-9012-3456\",\"candidateId\":\"1\"}";
        //act
        VoteInElectionsDtoRequest dto = dataConverter.getRequestForVoteInElections(jsonString);
        //assert
        assertEquals("1234-5678-9012-3456", dto.getToken());
        assertEquals("1", dto.getCandidateId());
    }

    @Test
    public void shouldReturnStopVotingDtoRequest() throws Exception {
        //arrange
        String jsonString = "{\"token\":\"1234-5678-9012-3456\"}";
        //act
        StopVotingDtoRequest dto = dataConverter.getRequestForStopVoting(jsonString);
        //assert
        assertEquals("1234-5678-9012-3456", dto.getToken());
    }

    @Test
    public void shouldConvertResponseToJson() throws Exception {
        //arrange
        RegisterVoterDtoResponse response1 = new RegisterVoterDtoResponse("1234-5678-9012-3456");
        AddCandidateDtoResponse response2 = new AddCandidateDtoResponse("nominateRequestId", "1");
        RateOfferDtoResponse response3 = new RateOfferDtoResponse("Existing rating has been updated!");
        //act
        String answer1 = dataConverter.convertResponse(response1);
        String answer2 = dataConverter.convertResponse(response2);
        String answer3 = dataConverter.convertResponse(response3);
        //assert
        assertTrue(answer1.contains(response1.getToken()));
        assertTrue(answer2.contains(response2.getId()));
        assertTrue(answer2.contains(response2.getType()));
        assertTrue(answer3.contains(response3.getMessage()));
    }

    @Test
    public void shouldConvertExceptionToJson() throws Exception {
        //arrange
        String exceptionMessage = CandidateError.CANDIDACY_ACTIVE.getErrorString();
        //act
        String answerMessage = dataConverter.convertException(new CandidateException(CandidateError.CANDIDACY_ACTIVE));
        //assert
        assertTrue(answerMessage.contains("error"));
        assertTrue(answerMessage.contains(exceptionMessage));
    }
}
