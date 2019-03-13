package pl.dzalunin.github.gateway;

public class RepositoryInfo {

    private String fullName;
    private String description;
    private String cloneUrl;
    private Integer stars;
    private String createdAt;

    public RepositoryInfo(String fullName, String description, String cloneUrl, Integer stars, String createdAt) {
        this.fullName = fullName;
        this.description = description;
        this.cloneUrl = cloneUrl;
        this.stars = stars;
        this.createdAt = createdAt;
    }

    public String getFullName() {
        return fullName;
    }

    public String getDescription() {
        return description;
    }

    public String getCloneUrl() {
        return cloneUrl;
    }

    public Integer getStars() {
        return stars;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        RepositoryInfo that = (RepositoryInfo) o;

        if (fullName != null ? !fullName.equals(that.fullName) : that.fullName != null) return false;
        if (description != null ? !description.equals(that.description) : that.description != null) return false;
        if (cloneUrl != null ? !cloneUrl.equals(that.cloneUrl) : that.cloneUrl != null) return false;
        if (stars != null ? !stars.equals(that.stars) : that.stars != null) return false;
        return createdAt != null ? createdAt.equals(that.createdAt) : that.createdAt == null;
    }

    @Override
    public int hashCode() {
        int result = fullName != null ? fullName.hashCode() : 0;
        result = 31 * result + (description != null ? description.hashCode() : 0);
        result = 31 * result + (cloneUrl != null ? cloneUrl.hashCode() : 0);
        result = 31 * result + (stars != null ? stars.hashCode() : 0);
        result = 31 * result + (createdAt != null ? createdAt.hashCode() : 0);
        return result;
    }
}
