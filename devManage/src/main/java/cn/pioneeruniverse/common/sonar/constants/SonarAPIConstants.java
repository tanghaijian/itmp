package cn.pioneeruniverse.common.sonar.constants;
/**
 *
 * @ClassName:SonarAPIConstants
 * @Description:常用sonar常量
 * @author author
 * @date 2020年8月20日
 *
 */
public class SonarAPIConstants {
	public static String TOKEN = "84e9b24e7b49b7d6f198952bf10319d49f560864"; //生产
//	public static String TOKEN = "354b3a289da5e9fd050d6e42c1b0ef71568c3269";//测试

	public static String SONAR_URL = "http://localhost:9000/";//生产
//	public static String SONAR_URL = "http://192.168.1.188:9000/";//测试

	public static String SONAR_PROJECT_SEARCH = "api/projects/search";//查询
	
	public static String SONAR_PROJECT_CREATE = "api/projects/create";//新建
	public static String SONAR_MEASURES_SEARCH = "api/measures/search";//查询此项目的度量详细信息 如 bug 重复率等信息
	public static String SONAR_MEASURES_COMPONET = "api/measures/component";
	
	public static String SONAR_ISSUES_SEARCH = "api/issues/search";//查询此项目的bug等信息
	public static String SONAR_SOURCES_LINES = "api/sources/lines";
	public static String SONAR_SEARCH_PEORJECT = "api/components/search_projects";
	public static String SONAR_COMPONET_TREE = "api/measures/component_tree";//以树形结构显示数据（如有bug的文件以树形显示）
	public static String SONAR_CE_ACTIVITY = "api/ce/activity";//查询此项目是否正在运行
	public static String SONAR_CE_COMPONENT = "api/ce/component";//查询此项目的componentId
	
	public static String SONAR_QUALITYGATES_PROJECT_STATUS = "api/qualitygates/project_status";
	

	
	
	
}
