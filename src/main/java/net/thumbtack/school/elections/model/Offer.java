package net.thumbtack.school.elections.model;

public class Offer {

    private String id;
    private String authorId;
    private boolean copyrightsActive = true;
    private String content;

    public Offer() {
    }

    public Offer(String authorId, String content) {
        this.authorId = authorId;
        this.content = content;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAuthorId() {
        return authorId;
    }

    public void setAuthorId(String authorId) {
        this.authorId = authorId;
    }

    public boolean isCopyrightsActive() {
        return copyrightsActive;
    }

    public void setCopyrightsActive(boolean copyrightsActive) {
        this.copyrightsActive = copyrightsActive;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Override
    public String toString() {
        return "Offer{" +
                "id='" + id + '\'' +
                ", authorId='" + authorId + '\'' +
                ", copyrightsActive=" + copyrightsActive +
                ", content='" + content + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Offer offer = (Offer) o;

        if (copyrightsActive != offer.copyrightsActive) return false;
        if (id != null ? !id.equals(offer.id) : offer.id != null) return false;
        if (authorId != null ? !authorId.equals(offer.authorId) : offer.authorId != null) return false;
        return content != null ? content.equals(offer.content) : offer.content == null;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (authorId != null ? authorId.hashCode() : 0);
        result = 31 * result + (copyrightsActive ? 1 : 0);
        result = 31 * result + (content != null ? content.hashCode() : 0);
        return result;
    }
}
