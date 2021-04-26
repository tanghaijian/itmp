package cn.pioneeruniverse.common.nexus;

/**
 * Nexus查询搜索封装
 * http://localhost:8081/nexus/service/rest/v1/search?
 * q=aaa&repository=bbb&format=ccc&group=ddd&name=eee&version=fff&md5=ggg&sha1=hhh&sha256=iii&sha512=jjj
 * &maven.groupId=111&maven.artifactId=222&maven.baseVersion=333&maven.extension=444&maven.classifier=555
 * 
 * @author zhoudu
 *
 */
public class NexusSearchVO {

	/**
	 * 关键字，用，|或者空格分割。
	 */
	private String q;
	/**
	 * 仓库名：比如：maven-releases,maven-snapshots
	 */
	private String repository;
	/**
	 * Query by format一般无用
	 */
	private String format;

	/**
	 * Component group和groupId相同
	 */
	private String group;
	/**
	 * 组件名，一般和artifactId相同
	 */
	private String name;
	/**
	 * Component version:标准版本，快照为：1.0.0-20190626.083036-1
	 */
	private String version;
	private String md5;//包件md5
	private String sha1;//包件sha1
	private String sha256;//包件sha256
	private String sha512;//包件sha512

	/** 基于Maven的参数 */
	private String groupId;//包件groupId
	private String artifactId;//包件artifactId
	private String baseVersion;//基础版本，快照为：1.0.0-SNAPSHOT
	/**
	 * 后缀标识：jar,war...
	 */
	private String extension;//包件extension
	private String classifier;//包件classifier

	public String getQ() {
		return q;
	}

	public void setQ(String q) {
		this.q = q;
	}

	public String getRepository() {
		return repository;
	}

	public void setRepository(String repository) {
		this.repository = repository;
	}

	public String getFormat() {
		return format;
	}

	public void setFormat(String format) {
		this.format = format;
	}

	public String getGroup() {
		return group;
	}

	public void setGroup(String group) {
		this.group = group;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public String getMd5() {
		return md5;
	}

	public void setMd5(String md5) {
		this.md5 = md5;
	}

	public String getSha1() {
		return sha1;
	}

	public void setSha1(String sha1) {
		this.sha1 = sha1;
	}

	public String getSha256() {
		return sha256;
	}

	public void setSha256(String sha256) {
		this.sha256 = sha256;
	}

	public String getSha512() {
		return sha512;
	}

	public void setSha512(String sha512) {
		this.sha512 = sha512;
	}

	public String getGroupId() {
		return groupId;
	}

	public void setGroupId(String groupId) {
		this.groupId = groupId;
	}

	public String getArtifactId() {
		return artifactId;
	}

	public void setArtifactId(String artifactId) {
		this.artifactId = artifactId;
	}

	public String getBaseVersion() {
		return baseVersion;
	}

	public void setBaseVersion(String baseVersion) {
		this.baseVersion = baseVersion;
	}

	public String getExtension() {
		return extension;
	}

	public void setExtension(String extension) {
		this.extension = extension;
	}

	public String getClassifier() {
		return classifier;
	}

	public void setClassifier(String classifier) {
		this.classifier = classifier;
	}
	
	public void setAllFieldEmptyToNull() {
		if ("".equals(q)) {q = null;}
		if ("".equals(repository)) {repository = null;}
		if ("".equals(format)) {format = null;}
		if ("".equals(group)) {group = null;}
		if ("".equals(name)) {name = null;}
		if ("".equals(version)) {version = null;}
		if ("".equals(md5)) {md5 = null;}
		if ("".equals(sha1)) {sha1 = null;}
		if ("".equals(sha256)) {sha256 = null;}
		if ("".equals(sha512)) {sha512 = null;}
		if ("".equals(groupId)) {groupId = null;}
		if ("".equals(artifactId)) {artifactId = null;}
		if ("".equals(baseVersion)) {baseVersion = null;}
		if ("".equals(extension)) {extension = null;}
		if ("".equals(classifier)) {classifier = null;}
	}

}
