package net.thumbtack.school.elections.dao;

import net.thumbtack.school.elections.model.CandidateProgram;

import java.util.List;

public interface CandidateProgramDao {

    String addCandidateProgram(CandidateProgram candidateProgram);

    CandidateProgram getCandidateProgramById(String candidateProgramId);

    String removeCandidateProgram(String candidateProgramId);

    List<CandidateProgram> getPrograms();

    String getCandidateProgramIdByCandidateIdAndOfferId(String candidateId, String offerId);
}
