package net.thumbtack.school.elections.validators;

import net.thumbtack.school.elections.dao.CandidateDao;
import net.thumbtack.school.elections.dao.NominateRequestDao;
import net.thumbtack.school.elections.dao.VoterDao;
import net.thumbtack.school.elections.daoimpl.CandidateDaoImpl;
import net.thumbtack.school.elections.daoimpl.NominateRequestDaoImpl;
import net.thumbtack.school.elections.daoimpl.VoterDaoImpl;
import net.thumbtack.school.elections.exceptionimpl.CandidateError;
import net.thumbtack.school.elections.exceptionimpl.CandidateException;
import net.thumbtack.school.elections.exceptionimpl.VoterError;
import net.thumbtack.school.elections.exceptionimpl.VoterException;

public class NominateCandidateValidator {

    private VoterDao voterDao = VoterDaoImpl.getInstance();
    private NominateRequestDao nominateRequestDao = NominateRequestDaoImpl.getInstance();
    private CandidateDao candidateDao = CandidateDaoImpl.getInstance();

    public void validateRegisterCandidateRequest(String nomineeId) {
        validateRequestedNominee(nomineeId);
        validateNominateCandidateRequest(nomineeId);
        validateCandidate(nomineeId);
    }

    private void validateRequestedNominee(String nomineeId) {
        if (!voterDao.isRegisteredVoter(nomineeId)) {
            throw new VoterException(VoterError.VOTER_DOES_NOT_EXIST);
        }
    }

    private void validateNominateCandidateRequest(String nomineeToken) {
        if (nominateRequestDao.isRegisteredNominateRequest(nomineeToken)) {
            throw new CandidateException(CandidateError.NOMINATE_REQUEST_ALREADY_REGISTERED);
        }
    }

    private void validateCandidate(String voterId) {
        if (candidateDao.isRegisteredCandidate(voterId)) {
            throw new CandidateException(CandidateError.CANDIDATE_ALREADY_REGISTERED);
        }
    }
}
