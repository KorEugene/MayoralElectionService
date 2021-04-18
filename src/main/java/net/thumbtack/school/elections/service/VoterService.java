package net.thumbtack.school.elections.service;

import net.thumbtack.school.elections.dao.*;
import net.thumbtack.school.elections.daoimpl.*;
import net.thumbtack.school.elections.model.*;
import net.thumbtack.school.elections.request.*;
import net.thumbtack.school.elections.response.*;
import net.thumbtack.school.elections.server.TokenGenerator;
import net.thumbtack.school.elections.validators.*;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import static java.util.Objects.isNull;

public class VoterService {

    private static final String ZERO = "0";
    private static final int AUTHOR_RATING_VALUE = 5;

    private VoterDao voterDao = VoterDaoImpl.getInstance();
    private OfferDao offerDao = OfferDaoImpl.getInstance();
    private RatingDao ratingDao = RatingDaoImpl.getInstance();
    private CandidateDao candidateDao = CandidateDaoImpl.getInstance();
    private NominateRequestDao nominateRequestDao = NominateRequestDaoImpl.getInstance();
    private CandidateProgramDao candidateProgramDao = CandidateProgramDaoImpl.getInstance();
    private RegisterVoterDtoValidator registerVoterDtoValidator = new RegisterVoterDtoValidator();
    private TokenValidator tokenValidator = new TokenValidator();
    private ObjectSavedToDataBaseValidator objectSavedToDataBaseValidator = new ObjectSavedToDataBaseValidator();
    private RateOfferDtoValidator rateOfferDtoValidator = new RateOfferDtoValidator();
    private NominateCandidateValidator nominateCandidateValidator = new NominateCandidateValidator();
    private CandidateValidator candidateValidator = new CandidateValidator();
    private LogOnVoterValidator logOnVoterValidator = new LogOnVoterValidator();

    public RegisterVoterDtoResponse registerNewVoter(RegisterVoterDtoRequest request) {
        registerVoterDtoValidator.validateRegisterDtoRequest(request);
        Voter voter = new Voter(new Person(request.getFullName(), request.getAddress(), request.getAccount()));
        voter.setToken(TokenGenerator.generateNewToken());
        String voterId = voterDao.addNewVoter(voter);
        objectSavedToDataBaseValidator.validateVoterId(voterId);
        return new RegisterVoterDtoResponse(voter.getToken());
    }

    public AddOfferDtoResponse addNewOffer(AddOfferDtoRequest request) {
        tokenValidator.validateToken(request.getToken());
        String authorId = voterDao.getVoterIdByToken(request.getToken());
        Offer offer = new Offer(authorId, request.getContent());
        String offerId = offerDao.addNewOffer(offer);
        Rating rating = new Rating(offerId, authorId, AUTHOR_RATING_VALUE);
        String ratingId = ratingDao.addNewRating(rating);
        objectSavedToDataBaseValidator.validateOfferId(offerId);
        objectSavedToDataBaseValidator.validateRatingId(ratingId);
        return new AddOfferDtoResponse(offerId);
    }

    public RateOfferDtoResponse rateOffer(RateOfferDtoRequest request) {
        tokenValidator.validateToken(request.getToken());
        rateOfferDtoValidator.validateRateOfferDtoRequest(request);
        String token = request.getToken();
        String offerId = request.getOfferId();
        String value = request.getValue();
        String voterId = voterDao.getVoterIdByToken(token);
        String ratingId = ratingDao.getRatingIdByAuthorIdAndOfferId(voterId, offerId);
        String result;
        if (isNull(ratingId)) {
            String newRatingId = ratingDao.addNewRating(new Rating(offerId, voterId, Integer.parseInt(value)));
            objectSavedToDataBaseValidator.validateRatingId(newRatingId);
            result = ResponseStatusMessage.RATING_STATUS_CREATED.getMessage();
        } else {
            if (value.equals(ZERO)) {
                result = ratingDao.deleteExistingRating(ratingId);
            } else {
                result = ratingDao.updateExistingRating(ratingId, Integer.parseInt(value));
            }
        }
        return new RateOfferDtoResponse(result);
    }

    public GetOffersByAverageRatingDtoResponse getOffersSortedByAverageRate(GetOffersByAverageRatingDtoRequest request) {
        tokenValidator.validateToken(request.getToken());
        List<Offer> offerList = offerDao.getOffers();
        List<Rating> ratingList = ratingDao.getRatings();
        GetOffersByAverageRatingDtoResponse response = new GetOffersByAverageRatingDtoResponse();
        double averageRating;
        for (Offer offer : offerList) {
            List<Rating> currentOfferRatings = ratingList
                    .stream()
                    .filter(r -> r.getOfferId().equals(offer.getId()))
                    .collect(Collectors.toList());
            averageRating = currentOfferRatings
                    .stream()
                    .mapToDouble(Rating::getValue)
                    .average()
                    .orElse(0.0);
            response.createOfferWithAverageRating(averageRating, offer);
        }
        response
                .getOffers()
                .sort(Comparator
                        .comparing(GetOffersByAverageRatingDtoResponse.OfferWithAverageRating::getAverageRating)
                        .reversed());
        return response;
    }

    public GetOfferListByAuthorDtoResponse getOfferListByAuthor(GetOfferListByAuthorDtoRequest request) {
        tokenValidator.validateToken(request.getToken());
        GetOfferListByAuthorDtoResponse response = new GetOfferListByAuthorDtoResponse();
        for (String authorId : request.getAuthorId()) {
            if (voterDao.isRegisteredVoter(authorId)) {
                response.getOffers().add(offerDao.getOffersByAuthorId(authorId));
            }
        }
        return response;
    }

    public AddCandidateDtoResponse registerNewCandidate(AddCandidateDtoRequest request) {
        String token = request.getToken();
        tokenValidator.validateToken(token);
        nominateCandidateValidator.validateRegisterCandidateRequest(request.getNomineeId());
        String nomineeId = request.getNomineeId();
        AddCandidateDtoResponse response;
        String voterId = voterDao.getVoterIdByToken(token);
        if (nomineeId.equals(voterId)) {
            Voter voter = voterDao.getVoterById(nomineeId);
            Candidate candidate = new Candidate(voter);
            String candidateId = candidateDao.addNewCandidate(candidate);
            List<Offer> offerList = offerDao.getOffersByAuthorId(nomineeId);
            offerList.forEach(offer -> candidateProgramDao.addCandidateProgram(new CandidateProgram(candidateId, offer)));
            objectSavedToDataBaseValidator.validateCandidateId(candidateId);
            response = new AddCandidateDtoResponse(FieldType.CANDIDATE_ID.getType(), candidateId);
        } else {
            CandidateNominateRequest candidateNominateRequest = new CandidateNominateRequest(nomineeId, voterId);
            String nominateRequestId = nominateRequestDao.addNominateRequest(candidateNominateRequest);
            objectSavedToDataBaseValidator.validateNominateRequestId(nominateRequestId);
            response = new AddCandidateDtoResponse(FieldType.NOMINATE_REQUEST_ID.getType(), nominateRequestId);
        }
        return response;
    }

    public CheckRequestsDtoResponse checkRequests(CheckRequestsDtoRequest request) {
        String token = request.getToken();
        tokenValidator.validateToken(token);
        String voterId = voterDao.getVoterIdByToken(token);
        List<CandidateNominateRequest> requests = nominateRequestDao.getNominateRequestsByVoterId(voterId);
        return new CheckRequestsDtoResponse(requests);
    }

    public AnswerNominateRequestDtoResponse answerToNominateRequest(AnswerNominateRequestDtoRequest request) {
        String token = request.getToken();
        tokenValidator.validateToken(token);
        AnswerNominateRequestDtoResponse response;
        if (request.isConsent()) {
            String voterId = voterDao.getVoterIdByToken(token);
            Voter voter = voterDao.getVoterById(voterId);
            Candidate candidate = new Candidate(voter);
            String candidateId = candidateDao.addNewCandidate(candidate);
            List<Offer> offerList = offerDao.getOffersByAuthorId(voterId);
            offerList.forEach(offer -> candidateProgramDao.addCandidateProgram(new CandidateProgram(candidateId, offer)));
            objectSavedToDataBaseValidator.validateCandidateId(candidateId);
            String message = nominateRequestDao.removeNominateRequest(request.getNominateRequestId());
            response = new AnswerNominateRequestDtoResponse(candidateId, message);
        } else {
            String message = nominateRequestDao.removeNominateRequest(request.getNominateRequestId());
            response = new AnswerNominateRequestDtoResponse(ZERO, message);
        }
        return response;
    }

    public UpdateCandidateProgramDtoResponse updateCandidateProgram(UpdateCandidateProgramDtoRequest request) {
        String token = request.getToken();
        tokenValidator.validateToken(token);
        UpdateCandidateProgramDtoResponse response;
        Candidate candidate = candidateDao.getCandidateByToken(token);
        String offerId = request.getOfferId();
        Offer offer = offerDao.getOfferById(offerId);
        candidateValidator.validateCandidateIsExist(candidate);
        String voterId = candidate.getVoter().getPerson().getId();
        String authorId = offer.getAuthorId();
        candidateValidator.validateAuthorForCandidateProgramModification(voterId, authorId);
        if (request.isAddition()) {
            CandidateProgram candidateProgram = new CandidateProgram(candidate.getId(), offer);
            String candidateProgramId = candidateProgramDao.addCandidateProgram(candidateProgram);
            objectSavedToDataBaseValidator.validateCandidateProgramId(candidateProgramId);
            response = new UpdateCandidateProgramDtoResponse(ResponseStatusMessage.OFFER_ADDED_TO_CANDIDATE_PROGRAM.getMessage());
        } else {
            String candidateProgramId = candidateProgramDao.getCandidateProgramIdByCandidateIdAndOfferId(candidate.getId(), offerId);
            response = new UpdateCandidateProgramDtoResponse(candidateProgramDao.removeCandidateProgram(candidateProgramId));
        }

        return response;
    }

    public WithdrawCandidateDtoResponse withdrawCandidate(WithdrawCandidateDtoRequest request) {
        String token = request.getToken();
        tokenValidator.validateToken(token);
        WithdrawCandidateDtoResponse response;
        Candidate candidate = candidateDao.getCandidateByToken(token);
        candidateValidator.validateCandidateIsExist(candidate);
        candidateProgramDao.getPrograms()
                .stream()
                .filter(p -> p.getCandidateId().equals(candidate.getId()))
                .forEach(p -> candidateProgramDao.removeCandidateProgram(p.getId()));
        response = new WithdrawCandidateDtoResponse(candidateDao.deleteCandidate(candidate.getId()));

        return response;
    }

    public GetCandidateListWithProgramsDtoResponse getCandidatesWithPrograms(GetCandidateListWithProgramsDtoRequest request) {
        String token = request.getToken();
        tokenValidator.validateToken(token);
        List<Candidate> candidates = candidateDao.getCandidates();
        List<CandidateProgram> programs = candidateProgramDao.getPrograms();
        GetCandidateListWithProgramsDtoResponse response = new GetCandidateListWithProgramsDtoResponse();
        for (Candidate candidate : candidates) {
            List<CandidateProgram> currentCandidatePrograms = programs
                    .stream()
                    .filter(p -> p.getCandidateId().equals(candidate.getId()))
                    .collect(Collectors.toList());
            response.createCandidateWithProgram(candidate.getId(), candidate.getVoter().getPerson().getFullName(), currentCandidatePrograms);
        }
        return response;
    }

    public LogOnDtoResponse logOnVoter(LogOnDtoRequest request) {
        Voter voter = voterDao.getVoters()
                .stream()
                .filter(v -> v.getPerson().getAccount().getLogin().equals(request.getLogin()))
                .findFirst()
                .orElse(null);
        logOnVoterValidator.validateLogOnData(voter, request.getPassword());
        String newToken = TokenGenerator.generateNewToken();
        if (voter != null) {
            voterDao.updateToken(voter.getPerson().getId(), newToken);
        }
        return new LogOnDtoResponse(newToken, ResponseStatusMessage.SUCCESSFULLY_LOGGED_ON.getMessage());
    }

    public LogOutDtoResponse logOutVoter(LogOutDtoRequest request) {
        String token = request.getToken();
        tokenValidator.validateToken(token);
        String voterId = voterDao.getVoterIdByToken(token);
        boolean candidacyStatus = candidateDao.isRegisteredCandidate(voterId);
        candidateValidator.validateCandidacyIsActive(candidacyStatus);
        nominateRequestDao.getNominateRequestsByVoterId(voterId)
                .forEach(req -> nominateRequestDao.removeNominateRequest(req.getId()));
        offerDao.getOffers()
                .stream()
                .filter(o -> o.getAuthorId().equals(voterId))
                .forEach(o -> offerDao.updateOfferCopyrights(o.getId(), false));
        ratingDao.getRatings()
                .stream()
                .filter(r -> r.getAuthorId().equals(voterId))
                .forEach(r -> ratingDao.deleteExistingRating(r.getId()));
        voterDao.removeToken(token);
        return new LogOutDtoResponse(ResponseStatusMessage.SUCCESSFULLY_LOGGED_OUT.getMessage());
    }
}
