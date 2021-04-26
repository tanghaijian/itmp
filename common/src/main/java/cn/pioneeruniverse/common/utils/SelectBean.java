package cn.pioneeruniverse.common.utils;

public class SelectBean {


	/**
	 * id
	 **/
	private String id;

	/**
	 * 名称
	 **/
	private String name;

	public SelectBean(String id,String name){
		this.id=id;
		this.name=name;
	}

	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
}
