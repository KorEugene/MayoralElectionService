package net.thumbtack.school.elections.server;

import net.thumbtack.school.elections.converter.DataConverter;
import net.thumbtack.school.elections.converterimpl.DataJsonConverter;
import net.thumbtack.school.elections.exceptionimpl.*;
import net.thumbtack.school.elections.request.*;
import net.thumbtack.school.elections.response.*;
import net.thumbtack.school.elections.serializer.DataBaseReader;
import net.thumbtack.school.elections.serializer.DataBaseWriter;
import net.thumbtack.school.elections.serializerimpl.DataBaseJsonReader;
import net.thumbtack.school.elections.serializerimpl.DataBaseJsonWriter;
import net.thumbtack.school.elections.service.ElectionService;
import net.thumbtack.school.elections.service.VoterService;
import net.thumbtack.school.elections.validators.ElectionValidator;
import org.apache.commons.cli.*;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;

import static java.util.Objects.isNull;

public class Server {

    private static volatile Server instance;

    private VoterService voterService = new VoterService();
    private ElectionService electionService = new ElectionService();
    private boolean votingStatus = false;
    private DataConverter dataConverter = new DataJsonConverter();
    private ElectionValidator electionValidator = new ElectionValidator();
    private DataBaseWriter dataBaseWriter = new DataBaseJsonWriter();
    private DataBaseReader dataBaseReader = new DataBaseJsonReader();

    private Server() {
    }

    static {
        instance = new Server();
    }

    public static Server getInstance() {
        return instance;
    }

    public void startServer(String savedDataFileName) throws IOException {
        if (!StringUtils.isBlank(savedDataFileName)) {
            dataBaseReader.loadDataBase(savedDataFileName);
        }
    }

    public void stopServer(String saveDataFileName) throws IOException {
        if (StringUtils.isBlank(saveDataFileName)) {
            DataBase.clearInstance();
        } else {
            dataBaseWriter.saveDataBase(saveDataFileName);
            DataBase.clearInstance();
        }
    }

    public VoterService getVoterService() {
        return voterService;
    }

    public void setVoterService(VoterService voterService) {
        this.voterService = voterService;
    }

    public ElectionService getElectionService() {
        return electionService;
    }

    public void setElectionService(ElectionService electionService) {
        this.electionService = electionService;
    }

    public boolean isVotingStatus() {
        return votingStatus;
    }

    public void setVotingStatus(boolean votingStatus) {
        this.votingStatus = votingStatus;
    }

    public String registerVoter(String requestString) {
        RegisterVoterDtoRequest request;
        RegisterVoterDtoResponse response;
        try {
            electionValidator.validateVotingStarted(votingStatus);
            request = dataConverter.getRequestForRegisterVoter(requestString);
            response = voterService.registerNewVoter(request);
        } catch (VoterException | ElectionException exception) {
            return dataConverter.convertException(exception);
        }
        return dataConverter.convertResponse(response);
    }

    public String addOffer(String requestString) {
        AddOfferDtoRequest request;
        AddOfferDtoResponse response;
        try {
            electionValidator.validateVotingStarted(votingStatus);
            request = dataConverter.getRequestForAddOffer(requestString);
            response = voterService.addNewOffer(request);
        } catch (VoterException | OfferException | RatingException | ElectionException exception) {
            return dataConverter.convertException(exception);
        }
        return dataConverter.convertResponse(response);
    }

    public String rateOffer(String requestString) {
        RateOfferDtoRequest request;
        RateOfferDtoResponse response;
        try {
            electionValidator.validateVotingStarted(votingStatus);
            request = dataConverter.getRequestForRateOffer(requestString);
            response = voterService.rateOffer(request);
        } catch (VoterException | OfferException | RatingException | ElectionException exception) {
            return dataConverter.convertException(exception);
        }
        return dataConverter.convertResponse(response);
    }

    public String getOffersSortedByAverageRate(String requestString) {
        GetOffersByAverageRatingDtoRequest request = dataConverter.getRequestForOfferListWithAverageRating(requestString);
        GetOffersByAverageRatingDtoResponse response;
        try {
            response = voterService.getOffersSortedByAverageRate(request);
        } catch (VoterException exception) {
            return dataConverter.convertException(exception);
        }
        return dataConverter.convertResponse(response);
    }

    public String getOfferListByAuthor(String requestString) {
        GetOfferListByAuthorDtoRequest request = dataConverter.getRequestForOfferListByAuthor(requestString);
        GetOfferListByAuthorDtoResponse response;
        try {
            response = voterService.getOfferListByAuthor(request);
        } catch (VoterException exception) {
            return dataConverter.convertException(exception);
        }
        return dataConverter.convertResponse(response);
    }

    public String registerCandidate(String requestString) {
        AddCandidateDtoRequest request;
        AddCandidateDtoResponse response;
        try {
            electionValidator.validateVotingStarted(votingStatus);
            request = dataConverter.getRequestForRegisterCandidate(requestString);
            response = voterService.registerNewCandidate(request);
        } catch (VoterException | CandidateException | ElectionException exception) {
            return dataConverter.convertException(exception);
        }
        return dataConverter.convertResponse(response);
    }

    public String checkRequests(String requestString) {
        CheckRequestsDtoRequest request;
        CheckRequestsDtoResponse response;
        try {
            electionValidator.validateVotingStarted(votingStatus);
            request = dataConverter.getRequestForCheckNominateRequests(requestString);
            response = voterService.checkRequests(request);
        } catch (VoterException | ElectionException exception) {
            return dataConverter.convertException(exception);
        }
        return dataConverter.convertResponse(response);
    }

    public String answerRequest(String requestString) {
        AnswerNominateRequestDtoRequest request;
        AnswerNominateRequestDtoResponse response;
        try {
            electionValidator.validateVotingStarted(votingStatus);
            request = dataConverter.getRequestForAnswerNominateRequest(requestString);
            response = voterService.answerToNominateRequest(request);
        } catch (VoterException | CandidateException | ElectionException exception) {
            return dataConverter.convertException(exception);
        }
        return dataConverter.convertResponse(response);
    }

    public String updateCandidateProgram(String requestString) {
        UpdateCandidateProgramDtoRequest request;
        UpdateCandidateProgramDtoResponse response;
        try {
            electionValidator.validateVotingStarted(votingStatus);
            request = dataConverter.getRequestForUpdateCandidateProgram(requestString);
            response = voterService.updateCandidateProgram(request);
        } catch (VoterException | CandidateException | ElectionException exception) {
            return dataConverter.convertException(exception);
        }
        return dataConverter.convertResponse(response);
    }

    public String withdrawCandidate(String requestString) {
        WithdrawCandidateDtoRequest request;
        WithdrawCandidateDtoResponse response;
        try {
            electionValidator.validateVotingStarted(votingStatus);
            request = dataConverter.getRequestForWithdrawCandidate(requestString);
            response = voterService.withdrawCandidate(request);
        } catch (VoterException | CandidateException | ElectionException exception) {
            return dataConverter.convertException(exception);
        }
        return dataConverter.convertResponse(response);
    }

    public String getCandidates(String requestString) {
        GetCandidateListWithProgramsDtoRequest request = dataConverter.getRequestForCandidateListWithPrograms(requestString);
        GetCandidateListWithProgramsDtoResponse response;
        try {
            response = voterService.getCandidatesWithPrograms(request);
        } catch (VoterException exception) {
            return dataConverter.convertException(exception);
        }
        return dataConverter.convertResponse(response);
    }

    public String logOnVoter(String requestString) {
        LogOnDtoRequest request;
        LogOnDtoResponse response;
        try {
            electionValidator.validateVotingStarted(votingStatus);
            request = dataConverter.getRequestForVoterLogOn(requestString);
            response = voterService.logOnVoter(request);
        } catch (VoterException | ElectionException exception) {
            return dataConverter.convertException(exception);
        }
        return dataConverter.convertResponse(response);
    }

    public String logOutVoter(String requestString) {
        LogOutDtoRequest request;
        LogOutDtoResponse response;
        try {
            electionValidator.validateVotingStarted(votingStatus);
            request = dataConverter.getRequestForVoterLogOut(requestString);
            response = voterService.logOutVoter(request);
        } catch (VoterException | CandidateException | ElectionException exception) {
            return dataConverter.convertException(exception);
        }
        return dataConverter.convertResponse(response);
    }

    public String startVoting(String requestString) {
        StartVotingDtoRequest request;
        StartVotingDtoResponse response;
        try {
            electionValidator.validateVotingStarted(votingStatus);
            request = dataConverter.getRequestForStartVoting(requestString);
            response = electionService.startVoting(request);
        } catch (VoterException | ElectionException exception) {
            return dataConverter.convertException(exception);
        }
        return dataConverter.convertResponse(response);
    }

    public String voteInElections(String requestString) {
        VoteInElectionsDtoRequest request;
        VoteInElectionsDtoResponse response;
        try {
            electionValidator.validateVotingNotStarted(votingStatus);
            request = dataConverter.getRequestForVoteInElections(requestString);
            response = electionService.voteInElections(request);
        } catch (VoterException | ElectionException exception) {
            return dataConverter.convertException(exception);
        }
        return dataConverter.convertResponse(response);
    }

    public String stopVoting(String requestString) {
        StopVotingDtoRequest request;
        StopVotingDtoResponse response;
        try {
            electionValidator.validateVotingNotStarted(votingStatus);
            request = dataConverter.getRequestForStopVoting(requestString);
            response = electionService.stopVoting(request);
        } catch (VoterException | ElectionException exception) {
            return dataConverter.convertException(exception);
        }
        return dataConverter.convertResponse(response);
    }
}
