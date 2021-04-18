package net.thumbtack.school.elections.model;

import java.io.Serializable;

public class Rating implements Serializable {

    private String id;
    private String offerId;
    private String authorId;
    private int value;

    public Rating() {
    }

    public Rating(String offerId, String authorId, int value) {
        this.offerId = offerId;
        this.authorId = authorId;
        this.value = value;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getOfferId() {
        return offerId;
    }

    public void setOfferId(String offerId) {
        this.offerId = offerId;
    }

    public String getAuthorId() {
        return authorId;
    }

    public void setAuthorId(String authorId) {
        this.authorId = authorId;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return "Rating{" +
                "id='" + id + '\'' +
                ", offerId='" + offerId + '\'' +
                ", authorId='" + authorId + '\'' +
                ", value=" + value +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Rating rating = (Rating) o;

        if (value != rating.value) return false;
        if (id != null ? !id.equals(rating.id) : rating.id != null) return false;
        if (offerId != null ? !offerId.equals(rating.offerId) : rating.offerId != null) return false;
        return authorId != null ? authorId.equals(rating.authorId) : rating.authorId == null;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (offerId != null ? offerId.hashCode() : 0);
        result = 31 * result + (authorId != null ? authorId.hashCode() : 0);
        result = 31 * result + value;
        return result;
    }
}
