package cn.pioneeruniverse.common.gitlab.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import java.util.Date;
import java.util.List;


/**
 * 
* @ClassName: Project
* @Description: gitlab中的项目对象，
* 具体请参照https://docs.gitlab.com/ee/api/projects.html
* @author author
* @date 2020年8月17日 下午7:59:38
*
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonNaming(PropertyNamingStrategy.LowerCaseWithUnderscoresStrategy.class)
public class Project extends BaseEntity {

    private static final long serialVersionUID = 9102839611573246897L;

    private Integer id;
    private String description; //描述
    private String defaultBranch; //默认分支
    private String sshUrlToRepo;
    private String httpUrlToRepo;
    private String webUrl;
    private String readmeUrl;
    private List<String> tagList;
    private String name;
    private String nameWithNamespace;
    private String path;
    private String pathWithNamespace;
    private Date createdAt;
    private Date lastActivityAt;
    private Integer forksCount;
    private String avatarUrl;
    private Integer starCount;
    private Boolean archived;
    private String visibility;
    private String orderBy;
    private String sort;
    private String search;
    private Boolean simple;
    private Boolean owned;
    private Boolean membership;
    private Boolean starred;
    private Boolean statistics;
    private Boolean withCustomAttributes;
    private Boolean withIssuesEnabled;
    private Boolean withMergeRequestsEnabled;
    private String withProgrammingLanguage;
    private Boolean wikiChecksumFailed;
    private Boolean repositoryChecksumFailed;
    private Integer minAccessLevel;
    private Integer namespaceId;
    private Boolean initializeWithReadme;
    
    //扩展
    private Long systemId;


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDefaultBranch() {
        return defaultBranch;
    }

    public void setDefaultBranch(String defaultBranch) {
        this.defaultBranch = defaultBranch;
    }

    public String getSshUrlToRepo() {
        return sshUrlToRepo;
    }

    public void setSshUrlToRepo(String sshUrlToRepo) {
        this.sshUrlToRepo = sshUrlToRepo;
    }

    public String getHttpUrlToRepo() {
        return httpUrlToRepo;
    }

    public void setHttpUrlToRepo(String httpUrlToRepo) {
        this.httpUrlToRepo = httpUrlToRepo;
    }

    public String getWebUrl() {
        return webUrl;
    }

    public void setWebUrl(String webUrl) {
        this.webUrl = webUrl;
    }

    public String getReadmeUrl() {
        return readmeUrl;
    }

    public void setReadmeUrl(String readmeUrl) {
        this.readmeUrl = readmeUrl;
    }

    public List<String> getTagList() {
        return tagList;
    }

    public void setTagList(List<String> tagList) {
        this.tagList = tagList;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNameWithNamespace() {
        return nameWithNamespace;
    }

    public void setNameWithNamespace(String nameWithNamespace) {
        this.nameWithNamespace = nameWithNamespace;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getPathWithNamespace() {
        return pathWithNamespace;
    }

    public void setPathWithNamespace(String pathWithNamespace) {
        this.pathWithNamespace = pathWithNamespace;
    }

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", locale = "zh", timezone = "GMT+8")
    public Date getCreatedAt() {
        return createdAt;
    }

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", locale = "zh", timezone = "UTC")
    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", locale = "zh", timezone = "GMT+8")
    public Date getLastActivityAt() {
        return lastActivityAt;
    }

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", locale = "zh", timezone = "UTC")
    public void setLastActivityAt(Date lastActivityAt) {
        this.lastActivityAt = lastActivityAt;
    }

    public Integer getForksCount() {
        return forksCount;
    }

    public void setForksCount(Integer forksCount) {
        this.forksCount = forksCount;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }

    public Integer getStarCount() {
        return starCount;
    }

    public void setStarCount(Integer starCount) {
        this.starCount = starCount;
    }

    public Boolean getArchived() {
        return archived;
    }

    public void setArchived(Boolean archived) {
        this.archived = archived;
    }

    public String getVisibility() {
        return visibility;
    }

    public void setVisibility(String visibility) {
        this.visibility = visibility;
    }

    public String getOrderBy() {
        return orderBy;
    }

    public void setOrderBy(String orderBy) {
        this.orderBy = orderBy;
    }

    public String getSort() {
        return sort;
    }

    public void setSort(String sort) {
        this.sort = sort;
    }

    public String getSearch() {
        return search;
    }

    public void setSearch(String search) {
        this.search = search;
    }

    public Boolean getSimple() {
        return simple;
    }

    public void setSimple(Boolean simple) {
        this.simple = simple;
    }

    public Boolean getOwned() {
        return owned;
    }

    public void setOwned(Boolean owned) {
        this.owned = owned;
    }

    public Boolean getMembership() {
        return membership;
    }

    public void setMembership(Boolean membership) {
        this.membership = membership;
    }

    public Boolean getStarred() {
        return starred;
    }

    public void setStarred(Boolean starred) {
        this.starred = starred;
    }

    public Boolean getStatistics() {
        return statistics;
    }

    public void setStatistics(Boolean statistics) {
        this.statistics = statistics;
    }

    public Boolean getWithCustomAttributes() {
        return withCustomAttributes;
    }

    public void setWithCustomAttributes(Boolean withCustomAttributes) {
        this.withCustomAttributes = withCustomAttributes;
    }

    public Boolean getWithIssuesEnabled() {
        return withIssuesEnabled;
    }

    public void setWithIssuesEnabled(Boolean withIssuesEnabled) {
        this.withIssuesEnabled = withIssuesEnabled;
    }

    public Boolean getWithMergeRequestsEnabled() {
        return withMergeRequestsEnabled;
    }

    public void setWithMergeRequestsEnabled(Boolean withMergeRequestsEnabled) {
        this.withMergeRequestsEnabled = withMergeRequestsEnabled;
    }

    public String getWithProgrammingLanguage() {
        return withProgrammingLanguage;
    }

    public void setWithProgrammingLanguage(String withProgrammingLanguage) {
        this.withProgrammingLanguage = withProgrammingLanguage;
    }

    public Boolean getWikiChecksumFailed() {
        return wikiChecksumFailed;
    }

    public void setWikiChecksumFailed(Boolean wikiChecksumFailed) {
        this.wikiChecksumFailed = wikiChecksumFailed;
    }

    public Boolean getRepositoryChecksumFailed() {
        return repositoryChecksumFailed;
    }

    public void setRepositoryChecksumFailed(Boolean repositoryChecksumFailed) {
        this.repositoryChecksumFailed = repositoryChecksumFailed;
    }

    public Integer getMinAccessLevel() {
        return minAccessLevel;
    }

    public void setMinAccessLevel(Integer minAccessLevel) {
        this.minAccessLevel = minAccessLevel;
    }

    public Integer getNamespaceId() {
        return namespaceId;
    }

    public void setNamespaceId(Integer namespaceId) {
        this.namespaceId = namespaceId;
    }

    public Boolean getInitializeWithReadme() {
        return initializeWithReadme;
    }

    public void setInitializeWithReadme(Boolean initializeWithReadme) {
        this.initializeWithReadme = initializeWithReadme;
    }

	public Long getSystemId() {
		return systemId;
	}

	public void setSystemId(Long systemId) {
		this.systemId = systemId;
	}
    
    
}
