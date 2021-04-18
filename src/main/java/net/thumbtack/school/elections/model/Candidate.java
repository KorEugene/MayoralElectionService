package net.thumbtack.school.elections.model;

public class Candidate {

    private String id;
    private Voter voter;

    public Candidate() {
    }

    public Candidate(Voter voter) {
        this.voter = voter;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Voter getVoter() {
        return voter;
    }

    public void setVoter(Voter voter) {
        this.voter = voter;
    }

    @Override
    public String toString() {
        return "Candidate{" +
                "id='" + id + '\'' +
                ", voter=" + voter +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Candidate candidate = (Candidate) o;

        if (id != null ? !id.equals(candidate.id) : candidate.id != null) return false;
        return voter != null ? voter.equals(candidate.voter) : candidate.voter == null;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (voter != null ? voter.hashCode() : 0);
        return result;
    }
}
