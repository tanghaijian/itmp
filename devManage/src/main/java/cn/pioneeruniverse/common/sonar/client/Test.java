package cn.pioneeruniverse.common.sonar.client;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import cn.pioneeruniverse.common.sonar.bean.Component;
import cn.pioneeruniverse.common.sonar.bean.SonarQubeException;
import cn.pioneeruniverse.common.sonar.constants.SonarAPIConstants;



public class Test {
//	public static void main(String[] args) throws SonarQubeException {
//		
//		
//	   SonarQubeClientApi sonar=new SonarQubeClientApi(SonarAPIConstants.SONAR_URL, SonarAPIConstants.TOKEN);
//	 /*  String result=  sonar.getMeasuresApi().search("ji:12367", "alert_status,bugs");*/
//	   /*String result=  sonar.getIssuesApi().search("ji:12367", "BUG");*/
//	  /* String result=  sonar.getSourceApi().search("ji:12367:src/main/java/com/ruoyi/common/exception/user/CaptchaException.java");*/
//	   
//	   
//	 /* String result= 	sonar.getComponentApi().Search_projects("10");*/
//	   /*String result= 	sonar.getMeasuresApi().componenttreeSerarch("we:12", "ncloc,code_smells,bugs,vulnerabilities,coverage,duplicated_lines_density,alert_status");*/
//	 /* sonar.getMeasuresApi().searchProjectByKey("weiji900");*/
//	   /*sonar.getIssuesApi().searchByPage("weiji900", "BUG,CODE_SMELL,VULNERABILITY", "5", "100");*/
//	   /*sonar.getProjectsApi().search();*/
//	  /* sonar.getSourceApi().search("we:12:test/common/src/main/java/cn/pioneeruniverse/common/bean/BasePage.java");*/
//	   sonar.getProjectsApi().create("weiji","ceshi12");
//	   
//	   /*ProjectsApi projectsApi=	sonar.getProjectsApi();
//	   List<Component>  list= projectsApi.search();*/
//	 /*  JSONObject json=JSON.parseObject(result);
//	   String sources=json.getString("sources");
//	   List<HashMap> list =JSON.parseArray(sources, HashMap.class);
//	    for(int i=0;i<list.size();i++){
//	      System.out.println(list.get(i).get("code"));
//	    }
//*/
//	   
//	   
//	 /*JSONArray jsonArray= JSONArray.parseArray(json.toString());
//	 StringBuffer stringBuffer=new StringBuffer();
//	 for (Iterator iterator = jsonArray.iterator(); iterator.hasNext();) { 
//         JSONObject jsonObject = (JSONObject) iterator.next(); 
//         stringBuffer.append(jsonObject.getString("code"));
//} */
//
//	 
//	  
//	 /* String s1= JSON.parseArray(result).getJSONObject(1).toString();*/
//	  /* System.out.println(stringBuffer);*/
//	    }

}
