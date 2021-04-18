package net.thumbtack.school.elections.daoimpl;

import net.thumbtack.school.elections.dao.CandidateProgramDao;
import net.thumbtack.school.elections.model.CandidateProgram;
import net.thumbtack.school.elections.server.DataBaseManagementSystem;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class CandidateProgramDaoImpl implements CandidateProgramDao {

    private static volatile CandidateProgramDaoImpl instance;

    private DataBaseManagementSystem dbms = DataBaseManagementSystem.getInstance();

    private CandidateProgramDaoImpl() {
    }

    static {
        instance = new CandidateProgramDaoImpl();
    }

    public static CandidateProgramDaoImpl getInstance() {
        return instance;
    }

    @Override
    public String addCandidateProgram(CandidateProgram candidateProgram) {
        return dbms.insertCandidateProgram(candidateProgram);
    }

    @Override
    public CandidateProgram getCandidateProgramById(String candidateProgramId) {
        return dbms.getProgramMap().get(candidateProgramId);
    }

    @Override
    public String removeCandidateProgram(String candidateProgramId) {
        return dbms.deleteCandidateProgram(candidateProgramId);
    }

    @Override
    public List<CandidateProgram> getPrograms() {
        return new ArrayList<>(dbms.getProgramMap().values());
    }

    @Override
    public String getCandidateProgramIdByCandidateIdAndOfferId(String candidateId, String offerId) {
        return dbms.getProgramMap()
                .entrySet()
                .stream()
                .filter(entry -> entry.getValue().getCandidateId().equals(candidateId)
                        && entry.getValue().getOffer().getId().equals(offerId))
                .map(Map.Entry::getKey)
                .findFirst()
                .orElse(null);
    }
}
