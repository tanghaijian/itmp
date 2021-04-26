package cn.pioneeruniverse.common.sonar.client;


import com.sun.jersey.api.client.filter.HTTPBasicAuthFilter;

/**
 *
 * @ClassName:SonarQubeClientApi
 * @Description:sonarclientapi
 * @author author
 * @date 2020年8月19日
 *
 */
public class SonarQubeClientApi {

    private final String url;
    private final HTTPBasicAuthFilter auth;

    private ProjectsApi projectsApi;
    private MeasuresApi measuresApi;
    private UsersApi usersApi;
    private ComponentApi componentApi;
    private CeApi ceApi;
   
    private SourceApi sourceApi;

	private IssuesApi issuesApi;
    private QualityGatesApi qualityGatesApi;

    public SonarQubeClientApi(String url, String login, String password) {
        this.url = url;
        this.auth = new HTTPBasicAuthFilter(login, password);
    }

    public SonarQubeClientApi(String url, String securityToken) {
        this.url = url;
        this.auth = new HTTPBasicAuthFilter(securityToken, "");
    }

    public ProjectsApi getProjectsApi(){
        if(this.projectsApi == null){
            this.projectsApi = new ProjectsApi(url, auth);
        }
        return this.projectsApi;
    }
    
    public CeApi getCeApi(){
        if(this.ceApi == null){
            this.ceApi = new CeApi(url, auth);
        }
        return this.ceApi;
    }
    public ComponentApi getComponentApi(){
        if(this.componentApi == null){
            this.componentApi = new ComponentApi(url, auth);
        }
        return this.componentApi;
    }

    public MeasuresApi getMeasuresApi() {
    	if(this.measuresApi == null){
            this.measuresApi = new MeasuresApi(url, auth);
        }
		return this.measuresApi;
	}

    public SourceApi getSourceApi() {
    	if(this.sourceApi == null){
            this.sourceApi = new SourceApi(url, auth);
        }
		return this.sourceApi;
	}
    
    
    public UsersApi getUsersApi(){
        if(this.usersApi == null){
            this.usersApi = new UsersApi(url, auth);
        }
        return this.usersApi;
    }

    public IssuesApi getIssuesApi(){
        if(this.issuesApi == null){
            this.issuesApi = new IssuesApi(url, auth);
        }
        return this.issuesApi;
    }


    public QualityGatesApi getQualityGatesApi(){
        if(this.qualityGatesApi == null){
            this.qualityGatesApi = new QualityGatesApi(url, auth);
        }
        return this.qualityGatesApi;
    }


}
