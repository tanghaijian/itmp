package cn.pioneeruniverse.common.nexus;

/**
 * Neuxs仓库Repository封装类
 * { "name": "maven-snapshots", "format": "maven2", "type": "hosted", "url":
 * "http://localhost:8081/nexus/repository/maven-snapshots" }
 * 
 * @author zhoudu
 *
 */
public class NexusRepositoryBO {
	//创建名称
	private String name;
	//格式
	private String format;
	//类型
	private String type;
	//仓库url
	private String url;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getFormat() {
		return format;
	}

	public void setFormat(String format) {
		this.format = format;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

}
