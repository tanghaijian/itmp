package cn.pioneeruniverse.common.nexus;

import java.util.List;

/**
 * Nexus的组件封装类
 * { "id": "bWF2ZW4tcmVsZWFzZXM6NmRmZGEwYTg0M2Y0MjAxZDdjODMxNzMxYjI2MDlhZWU",
 * "repository": "maven-releases", "format": "maven2", "group": "com.zd",
 * "name": "test", "version": "0.0.1", "assets": [ { "downloadUrl":
 * "http://localhost:8081/nexus/repository/maven-releases/com/zd/test/0.0.1/test-0.0.1.pom",
 * "path": "com/zd/test/0.0.1/test-0.0.1.pom", "id":
 * "bWF2ZW4tcmVsZWFzZXM6NTgxODAwNWRlMmViMmJkMTliY2ExNDRkMzZkMGZmNTA",
 * "repository": "maven-releases", "format": "maven2", "checksum": { "sha1":
 * "9c86db3e205a91575a941d4493347fd896a20588", "md5":
 * "58be8de527ab9e32089ad9a9cab4fada" } },......
 * 
 * @author zhoudu
 *
 */
public class NexusComponentBO {

	private String id;
	//创建名称
	private String repository;
	//格式
	private String format;
	//groupId信息
	private String group;
	//包描述名
	private String name;
	//版本
	private String version;
	//Nexus的对应组件下asset列表内容
	private List<NexusAssetBO> assets;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
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

	public List<NexusAssetBO> getAssets() {
		return assets;
	}

	public void setAssets(List<NexusAssetBO> assets) {
		this.assets = assets;
	}

}
