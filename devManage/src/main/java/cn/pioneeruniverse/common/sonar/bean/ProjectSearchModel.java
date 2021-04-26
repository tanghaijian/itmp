package cn.pioneeruniverse.common.sonar.bean;

import java.util.List;

/**
 *
 * @ClassName:ProjectSearchModel
 * @deprecated
 * @author author
 * @date 2020年8月19日
 *
 */

public class ProjectSearchModel {

	 private Paging paging;
	    private List<Component> components;

	    public Paging getPaging() {
	        return paging;
	    }

	    public List<Component> getComponents() {
	        return components;
	    }
}