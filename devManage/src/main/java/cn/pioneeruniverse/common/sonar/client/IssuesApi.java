package cn.pioneeruniverse.common.sonar.client;


import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.ws.rs.core.MultivaluedMap;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.filter.HTTPBasicAuthFilter;
import com.sun.jersey.core.util.MultivaluedMapImpl;

import cn.pioneeruniverse.common.sonar.bean.Issue;
import cn.pioneeruniverse.common.sonar.bean.SonarQubeException;
import cn.pioneeruniverse.common.sonar.constants.SonarAPIConstants;


/**
 *
 * @ClassName:IssuesApi
 * @Description:问题api
 * @author author
 * @date 2020年8月19日
 *
 */
public class IssuesApi extends AbstractApi {

    public IssuesApi(String url, HTTPBasicAuthFilter auth) {
        super(url, auth);
    }
    /**
       * 查询 项目问题  显示每个问题详细位置路径 (获取所有问题)不分页
   * @param  componentKeys,types(types可选BUG, VULNERABILITY,CODE_SMELL)
   * @return String
   * @throws SonarQubeException 
   */
    public String  search(String componentKeys,String types) throws SonarQubeException{
    	
         MultivaluedMap<String, String> params = new MultivaluedMapImpl();
         params.add("componentKeys", componentKeys);
         params.add("s", "FILE_LINE");//可取值CREATION_DATE,UPDATE_DATE等
         params.add("types", types);
         params.add("additionalFields", "_all");
         params.add("facets","severities,types"); //验证性 和 错误类型
         params.add("resolved", "false");///未解决的
         ClientResponse clientResponse = RestApi.get(SonarAPIConstants.SONAR_URL,
            		 SonarAPIConstants.SONAR_ISSUES_SEARCH,getAuth(),params);
         String result = clientResponse.getEntity(String.class);
         List<Issue> list=new ArrayList<>();
         JSONObject jsonObject = JSONObject.parseObject(result);
         JSONArray jsonArray= jsonObject.getJSONArray("issues");
        
        for (int i = 0; i < jsonArray.size(); i++) {
            Issue issue=new Issue();
            JSONObject jobject = jsonArray.getJSONObject(i);
            
           String component= jobject.getString("component");
           String key= jobject.getString("key");
           String line= jobject.getString("line");
           String message= jobject.getString("message");
           String project= jobject.getString("project");
           String severity= jobject.getString("severity");
           String type= jobject.getString("type");
           issue.setComponent(component);
           issue.setKey(key);
           issue.setLine(line);
           issue.setMessage(message);
           issue.setProject(project);
           issue.setSeverity(severity);
           issue.setType(type);
           list.add(issue);
          
         
         
           
            
        }
         
         return result;
    	
    }
    
    
    
    
    /**
       * 查询 项目问题  显示每个问题详细位置路径 (获取所有问题)
     * @param  componentKeys,types(types可选BUG, VULNERABILITY,CODE_SMELL),page页数 pageSize每页显示条数
     * @return String
     * @throws SonarQubeException 
     */
    public List<Issue>  searchByPage(String componentKeys,String types,String page,String pageSize) throws SonarQubeException{
    	
         MultivaluedMap<String, String> params = new MultivaluedMapImpl();
         params.add("componentKeys", componentKeys);//key
         params.add("s", "FILE_LINE");//可取值CREATION_DATE,UPDATE_DATE等
         params.add("types", types);
         params.add("additionalFields", "_all");
         params.add("facets","severities,types"); //验证性 和 错误类型
         params.add("resolved", "false");///未解决的
         params.add("ps", pageSize);//每页显示条数
         params.add("p", page);//页数
         /*params.add("metricKeys",metricKeys);*/
         ClientResponse clientResponse = RestApi.get(SonarAPIConstants.SONAR_URL,
            		 SonarAPIConstants.SONAR_ISSUES_SEARCH,getAuth(),params);
         String result = clientResponse.getEntity(String.class);
         //解析json
         
         List<Issue> list=new ArrayList<>();
         JSONObject jsonObject = JSONObject.parseObject(result);
        JSONArray jsonArray= jsonObject.getJSONArray("issues");
        
        for (int i = 0; i < jsonArray.size(); i++) {//可通过sonar api查看对应的含义
            Issue issue=new Issue();
            JSONObject jobject = jsonArray.getJSONObject(i);
            
           String component= jobject.getString("component");
           String key= jobject.getString("key");
           String line= jobject.getString("line");
           String message= jobject.getString("message");
           String project= jobject.getString("project");
           String severity= jobject.getString("severity");
           String type= jobject.getString("type");
          issue.setComponent(component);
          issue.setKey(key);
          issue.setLine(line);
          issue.setMessage(message);
          issue.setProject(project);
          issue.setSeverity(severity);
          issue.setType(type);
          list.add(issue);
         
         
           
            
        }
        
           
         
         return list;
    	
    }
    
    
    /**
     * 查询指标作者
   * @param  componentKeys,types(types可选BUG, VULNERABILITY,CODE_SMELL),facets可选authors,length显示数据条数
   * @return String
   * @throws SonarQubeException 
   */
//    public String  searchIssueAuthor(String url,String type,String componentKeys,String facets,String length) throws SonarQubeException{
//    	DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//        MultivaluedMap<String, String> params = new MultivaluedMapImpl();
//        params.add("additionalFields", "_all");
//        params.add("componentKeys", componentKeys);
//        params.add("ps", "100");
//        params.add("s", "FILE_LINE");
//        params.add("types", type);
//        params.add("facets","authors"); //作者数据
//        //params.add("resolved", "false");//未解决的
//        //params.add("createdAfter", sdf.format(System.currentTimeMillis()));
////      String time=  sdf.format(System.currentTimeMillis());
//        String time=  "2018-11-29 14:16:43";
////        String time2= "2018-1129 19:37:47";
//        time= time.replace(" ", "T");
//        time=time+"+0800";
////      time2= time2.replace(" ", "T");
////      time2=time2+"+0800";
//        params.add("createdBefore", time );
////        params.add("createdAfter", time );
//        ClientResponse clientResponse = RestApi.get(url,
//        		SonarAPIConstants.SONAR_ISSUES_SEARCH,getAuth(),params);
//        String result = clientResponse.getEntity(String.class);
//        System.out.print(result);
//        //解析数据
//      StringBuffer authorResult=new StringBuffer();  
//     JSONArray jsonArray=   JSONObject.parseObject(result).getJSONArray("facets");
//     for(int y = 0; y < jsonArray.size(); y++) {
//    	    JSONObject jobject = jsonArray.getJSONObject(y);
//			String data=jobject.getString("property");
//			if(data.equals("authors")) {
//				
//				JSONArray authors=jobject.getJSONArray("values");
//				for(int x=0;x<authors.size();x++) {
//					JSONObject jsobject 		=authors.getJSONObject(x);
//					String authorAndCount=jsobject.getString("val")+":"+jsobject.getString("count")+",";
//					authorResult.append(authorAndCount);
//				}
//				
//				
//			}else {
//				continue;
//			}
//    	 
//     }
//     if(authorResult.length()>0) {
//    authorResult=authorResult.deleteCharAt(authorResult.length()-1);
//     }
//      
//        
//        return authorResult.toString();
//   	
//   }
    
    
    
    
    /**
     * 查询指标作者
   * @param  componentKeys,types(types可选BUG, VULNERABILITY,CODE_SMELL),p显示页数
   * @return String
   * @throws SonarQubeException 
   */
    public String  searchIssueAuthor(String url,String type,String componentKeys,String p,String time) throws SonarQubeException{
        MultivaluedMap<String, String> params = new MultivaluedMapImpl();
        String sonarUrl="";
        if(type.equals("tests")){//tests的数据 与 其他数据（ bugs Vulnerabilities Code Smells ） 的数据格式不同 ，所以要 区分 ，单独处理
            params.add("metricSortFilter", "withMeasuresOnly");
            params.add("metricSort", "tests");
            params.add("ps", "100");//每页显示数
            params.add("p", p);//页数
            params.add("s", "metric,name");
            params.add("baseComponentKey", componentKeys);
            params.add("metricKeys","tests"); //作者数据
            params.add("strategy", "leaves");//未解决的
            sonarUrl=SonarAPIConstants.SONAR_COMPONET_TREE;
        }else{
            params.add("additionalFields", "_all");
            params.add("componentKeys", componentKeys);
            params.add("ps", "100");//每页显示数
            params.add("p", p);//页数
            params.add("s", "FILE_LINE");
            params.add("types", type);
            params.add("facets","authors"); //作者数据
            params.add("resolved", "false");//未解决的
            sonarUrl=SonarAPIConstants.SONAR_ISSUES_SEARCH;
        }
        ClientResponse clientResponse = RestApi.get(url,
                sonarUrl,getAuth(),params);


      
//        params.add("createdAt", "2018-12-03T11:20:59+0800" );
//        params.add("createdAfter", time );

        String result = clientResponse.getEntity(String.class);
        System.out.print(result);
        return result;
   }
   


}
