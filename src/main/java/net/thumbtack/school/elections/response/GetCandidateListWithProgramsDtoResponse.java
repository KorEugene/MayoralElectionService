package net.thumbtack.school.elections.response;

import net.thumbtack.school.elections.model.CandidateProgram;
import net.thumbtack.school.elections.model.FullName;

import java.util.ArrayList;
import java.util.List;

public class GetCandidateListWithProgramsDtoResponse {

    private List<CandidateWithProgram> candidates;

    public GetCandidateListWithProgramsDtoResponse() {
        this.candidates = new ArrayList<>();
    }

    public static class CandidateWithProgram {

        private String candidateId;
        private FullName candidateFullName;
        private List<CandidateProgram> programs;

        public CandidateWithProgram(String candidateId, FullName candidateFullName, List<CandidateProgram> programs) {
            this.candidateId = candidateId;
            this.candidateFullName = candidateFullName;
            this.programs = programs;
        }

        public String getCandidateId() {
            return candidateId;
        }

        public void setCandidateId(String candidateId) {
            this.candidateId = candidateId;
        }

        public FullName getCandidateFullName() {
            return candidateFullName;
        }

        public void setCandidateFullName(FullName candidateFullName) {
            this.candidateFullName = candidateFullName;
        }

        public List<CandidateProgram> getPrograms() {
            return programs;
        }

        public void setPrograms(List<CandidateProgram> programs) {
            this.programs = programs;
        }
    }

    public List<CandidateWithProgram> getCandidates() {
        return candidates;
    }

    public void setCandidates(List<CandidateWithProgram> candidates) {
        this.candidates = candidates;
    }

    public void createCandidateWithProgram(String candidateId, FullName candidateFullName, List<CandidateProgram> programs) {
        CandidateWithProgram candidateWithProgram = new CandidateWithProgram(candidateId, candidateFullName, programs);
        candidates.add(candidateWithProgram);
    }
}
