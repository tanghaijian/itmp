package cn.pioneeruniverse.common.sonar.client;

import javax.ws.rs.core.MultivaluedMap;

import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.filter.HTTPBasicAuthFilter;
import com.sun.jersey.core.util.MultivaluedMapImpl;

import cn.pioneeruniverse.common.sonar.bean.SonarQubeException;
import cn.pioneeruniverse.common.sonar.constants.SonarAPIConstants;
/**
 *
 * @ClassName:QualityGatesApi
 * @Description:质量门禁api
 * @author author
 * @date 2020年8月19日
 *
 */
public class QualityGatesApi extends AbstractApi {

	public QualityGatesApi(String url, HTTPBasicAuthFilter auth) {
		super(url, auth);
	}

	/**
	 * 查询 扫描质量门禁结果
	 * 
	 * @param projectKey
	 * @param sonarUrl 
	 * @return String
	 * @throws SonarQubeException
	 */
	public String getProjectStatus(String projectKey, String sonarUrl) throws SonarQubeException {

		MultivaluedMap<String, String> params = new MultivaluedMapImpl();
		params.add("projectKey", projectKey);
		ClientResponse clientResponse = RestApi.get(sonarUrl,
				SonarAPIConstants.SONAR_QUALITYGATES_PROJECT_STATUS, getAuth(), params);
		String output = clientResponse.getEntity(String.class);
		return output;
	}

}
