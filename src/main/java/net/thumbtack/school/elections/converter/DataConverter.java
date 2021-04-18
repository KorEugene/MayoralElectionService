package net.thumbtack.school.elections.converter;

import net.thumbtack.school.elections.exception.ServiceException;
import net.thumbtack.school.elections.request.*;

public interface DataConverter {

    RegisterVoterDtoRequest getRequestForRegisterVoter(String requestString);

    AddOfferDtoRequest getRequestForAddOffer(String requestString);

    RateOfferDtoRequest getRequestForRateOffer(String requestString);

    GetOffersByAverageRatingDtoRequest getRequestForOfferListWithAverageRating(String requestString);

    GetOfferListByAuthorDtoRequest getRequestForOfferListByAuthor(String requestString);

    AddCandidateDtoRequest getRequestForRegisterCandidate(String requestString);

    CheckRequestsDtoRequest getRequestForCheckNominateRequests(String requestString);

    AnswerNominateRequestDtoRequest getRequestForAnswerNominateRequest(String requestString);

    UpdateCandidateProgramDtoRequest getRequestForUpdateCandidateProgram(String requestString);

    WithdrawCandidateDtoRequest getRequestForWithdrawCandidate(String requestString);

    GetCandidateListWithProgramsDtoRequest getRequestForCandidateListWithPrograms(String requestString);

    LogOnDtoRequest getRequestForVoterLogOn(String requestString);

    LogOutDtoRequest getRequestForVoterLogOut(String requestString);

    StartVotingDtoRequest getRequestForStartVoting(String requestString);

    VoteInElectionsDtoRequest getRequestForVoteInElections(String requestString);

    StopVotingDtoRequest getRequestForStopVoting(String requestString);

    String convertResponse(Object inputObject);

    String convertException(ServiceException exception);
}
