package cn.pioneeruniverse.project.vo;

/**
 *
 *
 * @Description:  Ztree树形节点
 * @author author
 * @date 2020年8月10日 下午14:27:12
 */
public class ZtreeVo {

	// id
	private String id;
	//是否选中
	private Boolean checked;
	//章节名称
	private String chaptersName;
	//父节点id
	private String pId;
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Boolean getChecked() {
		return checked;
	}

	public void setChecked(Boolean checked) {
		this.checked = checked;
	}

	public String getChaptersName() {
		return chaptersName;
	}

	public void setChaptersName(String chaptersName) {
		this.chaptersName = chaptersName;
	}

	public String getpId() {
		return pId;
	}

	public void setpId(String pId) {
		this.pId = pId;
	}
}
