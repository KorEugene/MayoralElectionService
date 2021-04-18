package net.thumbtack.school.elections.daoimpl;

import net.thumbtack.school.elections.dao.NominateRequestDao;
import net.thumbtack.school.elections.model.CandidateNominateRequest;
import net.thumbtack.school.elections.server.DataBaseManagementSystem;

import java.util.List;
import java.util.stream.Collectors;

public class NominateRequestDaoImpl implements NominateRequestDao {

    private static volatile NominateRequestDaoImpl instance;

    private DataBaseManagementSystem dbms = DataBaseManagementSystem.getInstance();

    private NominateRequestDaoImpl() {
    }

    static {
        instance = new NominateRequestDaoImpl();
    }

    public static NominateRequestDaoImpl getInstance() {
        return instance;
    }

    @Override
    public String addNominateRequest(CandidateNominateRequest request) {
        return dbms.insertNewNominateRequest(request);
    }

    @Override
    public List<CandidateNominateRequest> getNominateRequestsByVoterId(String voterId) {
        return dbms.getNominateRequests()
                .values()
                .stream()
                .filter(o -> o.getNomineeId().equals(voterId))
                .collect(Collectors.toList());
    }

    @Override
    public String removeNominateRequest(String nominateRequestId) {
        return dbms.deleteNominateRequest(nominateRequestId);
    }

    @Override
    public boolean isRegisteredNominateRequest(String nomineeId) {
        return dbms.getNominateRequests()
                .values()
                .stream()
                .anyMatch(o -> o.getNomineeId().equals(nomineeId));
    }
}
