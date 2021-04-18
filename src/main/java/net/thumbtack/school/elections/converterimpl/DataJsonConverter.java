package net.thumbtack.school.elections.converterimpl;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import net.thumbtack.school.elections.converter.DataConverter;
import net.thumbtack.school.elections.exception.ServiceException;
import net.thumbtack.school.elections.model.FieldType;
import net.thumbtack.school.elections.request.*;

public class DataJsonConverter implements DataConverter {

    private Gson gson = new Gson();

    @Override
    public RegisterVoterDtoRequest getRequestForRegisterVoter(String requestString) {
        return gson.fromJson(requestString, RegisterVoterDtoRequest.class);
    }

    @Override
    public AddOfferDtoRequest getRequestForAddOffer(String requestString) {
        return gson.fromJson(requestString, AddOfferDtoRequest.class);
    }

    @Override
    public RateOfferDtoRequest getRequestForRateOffer(String requestString) {
        return gson.fromJson(requestString, RateOfferDtoRequest.class);
    }

    @Override
    public GetOffersByAverageRatingDtoRequest getRequestForOfferListWithAverageRating(String requestString) {
        return gson.fromJson(requestString, GetOffersByAverageRatingDtoRequest.class);
    }

    @Override
    public GetOfferListByAuthorDtoRequest getRequestForOfferListByAuthor(String requestString) {
        return gson.fromJson(requestString, GetOfferListByAuthorDtoRequest.class);
    }

    @Override
    public AddCandidateDtoRequest getRequestForRegisterCandidate(String requestString) {
        return gson.fromJson(requestString, AddCandidateDtoRequest.class);
    }

    @Override
    public CheckRequestsDtoRequest getRequestForCheckNominateRequests(String requestString) {
        return gson.fromJson(requestString, CheckRequestsDtoRequest.class);
    }

    @Override
    public AnswerNominateRequestDtoRequest getRequestForAnswerNominateRequest(String requestString) {
        return gson.fromJson(requestString, AnswerNominateRequestDtoRequest.class);
    }

    @Override
    public UpdateCandidateProgramDtoRequest getRequestForUpdateCandidateProgram(String requestString) {
        return gson.fromJson(requestString, UpdateCandidateProgramDtoRequest.class);
    }

    @Override
    public WithdrawCandidateDtoRequest getRequestForWithdrawCandidate(String requestString) {
        return gson.fromJson(requestString, WithdrawCandidateDtoRequest.class);
    }

    @Override
    public GetCandidateListWithProgramsDtoRequest getRequestForCandidateListWithPrograms(String requestString) {
        return gson.fromJson(requestString, GetCandidateListWithProgramsDtoRequest.class);
    }

    @Override
    public LogOnDtoRequest getRequestForVoterLogOn(String requestString) {
        return gson.fromJson(requestString, LogOnDtoRequest.class);
    }

    @Override
    public LogOutDtoRequest getRequestForVoterLogOut(String requestString) {
        return gson.fromJson(requestString, LogOutDtoRequest.class);
    }

    @Override
    public StartVotingDtoRequest getRequestForStartVoting(String requestString) {
        return gson.fromJson(requestString, StartVotingDtoRequest.class);
    }

    @Override
    public VoteInElectionsDtoRequest getRequestForVoteInElections(String requestString) {
        return gson.fromJson(requestString, VoteInElectionsDtoRequest.class);
    }

    @Override
    public StopVotingDtoRequest getRequestForStopVoting(String requestString) {
        return gson.fromJson(requestString, StopVotingDtoRequest.class);
    }

    @Override
    public String convertResponse(Object inputObject) {
        return gson.toJson(new JsonParser().parse(gson.toJson(inputObject)).getAsJsonObject());
    }

    @Override
    public String convertException(ServiceException exception) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.add(FieldType.ERROR.getType(), getFieldAsJsonElement(exception.getErrorCode().getErrorString()));
        return gson.toJson(jsonObject);
    }

    private JsonElement getFieldAsJsonElement(String element) {
        return new JsonParser().parse(gson.toJson(element));
    }
}
