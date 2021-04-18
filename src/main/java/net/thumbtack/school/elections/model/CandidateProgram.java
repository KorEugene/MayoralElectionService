package net.thumbtack.school.elections.model;

public class CandidateProgram {

    private String id;
    private String candidateId;
    private Offer offer;

    public CandidateProgram() {
    }

    public CandidateProgram(String candidateId, Offer offer) {
        this.candidateId = candidateId;
        this.offer = offer;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCandidateId() {
        return candidateId;
    }

    public void setCandidateId(String candidateId) {
        this.candidateId = candidateId;
    }

    public Offer getOffer() {
        return offer;
    }

    public void setOffer(Offer offer) {
        this.offer = offer;
    }

    @Override
    public String toString() {
        return "CandidateProgram{" +
                "id='" + id + '\'' +
                ", candidateId='" + candidateId + '\'' +
                ", offer=" + offer +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        CandidateProgram that = (CandidateProgram) o;

        if (id != null ? !id.equals(that.id) : that.id != null) return false;
        if (candidateId != null ? !candidateId.equals(that.candidateId) : that.candidateId != null) return false;
        return offer != null ? offer.equals(that.offer) : that.offer == null;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (candidateId != null ? candidateId.hashCode() : 0);
        result = 31 * result + (offer != null ? offer.hashCode() : 0);
        return result;
    }
}
