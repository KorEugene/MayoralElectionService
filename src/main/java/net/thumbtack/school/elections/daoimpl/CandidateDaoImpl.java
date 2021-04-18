package net.thumbtack.school.elections.daoimpl;

import net.thumbtack.school.elections.dao.CandidateDao;
import net.thumbtack.school.elections.model.Candidate;
import net.thumbtack.school.elections.server.DataBaseManagementSystem;

import java.util.ArrayList;
import java.util.List;

public class CandidateDaoImpl implements CandidateDao {

    private static volatile CandidateDaoImpl instance;

    private DataBaseManagementSystem dbms = DataBaseManagementSystem.getInstance();

    private CandidateDaoImpl() {
    }

    static {
        instance = new CandidateDaoImpl();
    }

    public static CandidateDaoImpl getInstance() {
        return instance;
    }

    @Override
    public String addNewCandidate(Candidate candidate) {
        return dbms.insertNewCandidateToDataBase(candidate);
    }

    @Override
    public Candidate getCandidateById(String candidateId) {
        return dbms.getCandidateMap().get(candidateId);
    }

    @Override
    public Candidate getCandidateByToken(String token) {
        return dbms.getCandidateMap()
                .values()
                .stream()
                .filter(o -> o.getVoter().getToken().equals(token))
                .findFirst()
                .orElse(null);
    }

    @Override
    public String deleteCandidate(String candidateId) {
        return dbms.deleteCandidateFromDataBase(candidateId);
    }

    @Override
    public List<Candidate> getCandidates() {
        return new ArrayList<>(dbms.getCandidateMap().values());
    }

    @Override
    public boolean isRegisteredCandidate(String voterId) {
        return dbms.getCandidateMap()
                .values()
                .stream()
                .anyMatch(o -> o.getVoter().getPerson().getId().equals(voterId));
    }
}
