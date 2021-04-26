package cn.pioneeruniverse.common.artifactory;

import static org.jfrog.artifactory.client.model.impl.RepositoryTypeImpl.LOCAL;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.jfrog.artifactory.client.Artifactory;
import org.jfrog.artifactory.client.ArtifactoryClientBuilder;
import org.jfrog.artifactory.client.model.File;
import org.jfrog.artifactory.client.model.LightweightRepository;
import org.jfrog.artifactory.client.model.RepoPath;
import org.jfrog.artifactory.client.model.Repository;
import org.jfrog.artifactory.client.model.RepositoryType;
import org.jfrog.artifactory.client.model.repository.settings.impl.GenericRepositorySettingsImpl;


/**
 * @deprecated
* @ClassName: ArtifactoryUtils
* @Description: artifactory仓库使用工具类
* @author author
* @date 2020年9月4日 下午4:39:14
*
 */
public class ArtifactoryUtils {

    /**
     * This method creates an artifactory object
     */
    public static Artifactory createArtifactory(String username, String password, String artifactoryUrl) {
        if (StringUtils.isEmpty(username) || StringUtils.isEmpty(password) || StringUtils.isEmpty(artifactoryUrl)){
            throw new IllegalArgumentException("Arguments passed to createArtifactory are not valid");
        }

        return ArtifactoryClientBuilder.create()
                .setUrl(artifactoryUrl)
                .setUsername(username)
                .setPassword(password)
                .build();
    }

    /**
     * This method checks whether repository with supplied name exists or not, and creates new if required.
     */
    public static String createNewRepository(Artifactory artifactory, String repoName) {
        if (artifactory == null || StringUtils.isEmpty(repoName)){
            throw new IllegalArgumentException("Arguments passed to createNewRepository are not valid");
        }
        //获取本地所有artifactory
        List<LightweightRepository> repoList = artifactory.repositories().list(LOCAL);
      //获取所有的仓库名
        Set<String> repoNamesList = repoList.stream()
                .map(LightweightRepository::getKey)
                .collect(Collectors.toSet());

        String creationResult = null;
        if ( repoNamesList != null && !(repoNamesList.contains(repoName)) ){
            GenericRepositorySettingsImpl settings = new GenericRepositorySettingsImpl();
            settings.setRepoLayout("maven-2-default");//������Ĭ��Ϊsimple-default
            Repository repository = artifactory.repositories()
                    .builders()
                    .localRepositoryBuilder()
                    .key(repoName)
                    .description("new example local repository")
                    .repositorySettings(settings)
                    .build();
            creationResult = artifactory.repositories().create(2, repository);
        }

        return creationResult;
    }
    
    
    /**
     * Repository Info
     * */
    public static void getRepositoryInfo(Artifactory artifactory,String repoName) {
    	Repository repo = artifactory.repository(repoName).get();
        String repoKey = repo.getKey();
        String desc = repo.getDescription();
        String layout = repo.getRepoLayoutRef();
        RepositoryType repoClass = repo.getRclass();
        System.out.println("repo:"+repo +"  desc:"+desc+"  layout :"+layout+"  repoClass:"+repoClass);
        //repo:LocalRepositoryImpl{} desc:new example local repository  layout :simple-defaultrepo  Class:local

    }
    
    /**
     * This method update Repository
     * */
   /* public static String updateRepository(Artifactory artifactory,String repoName) {
    	if (artifactory == null || StringUtils.isEmpty(repoName)) {
			throw new IllegalArgumentException("Arguments passed to updateResitories are not valid");
		}
    	
    	Repository repository = artifactory.repository(repoName).get();
    	RepositorySettings settings = repository.getRepositorySettings();
    	if (PackageType.debian == settings.getPackageType()) {
			DebianRepositorySettingsImpl settingsForDebian = (DebianRepositorySettingsImpl) settings;
			settingsForDebian.setDebianTrivialLayout(false);
    	}
    	Repository updatedRepository = artifactory.repositories()
			.builders()
			.builderFrom(repository)
			.description("new_description")
			.build();

    	String result = artifactory.repositories().update(updatedRepository);
    	
    }*/
    
    
    /**
     * This method delete local Repository
     * */
    public static String deleteRepository(Artifactory artifactory,String repoName) {
    	if (artifactory == null || StringUtils.isEmpty(repoName)) {
			throw new IllegalArgumentException("Arguments passed to deleteRepository are not valid");
		}
    	String deleResult  = null;
        List<LightweightRepository> repoList = artifactory.repositories().list(LOCAL);
        Set<String> repoNamesList = repoList.stream()
                .map(LightweightRepository::getKey)
                .collect(Collectors.toSet());
    	String creationResult = null;
        if ( repoNamesList != null && !(repoNamesList.contains(repoName)) ){
        	deleResult = "该仓库不存在无法删除！！！";
        }else {
        	 deleResult  = artifactory.repository(repoName).delete();
        }
    	
    	return deleResult;
    	
    }
   
    /**
     * This method receives the uploaded file source and destination, performs the upload to artifactory
     */
    public static File uploadFile(Artifactory artifactory, String repo, String destPath, String fileNameToUpload) throws IOException {
        if (StringUtils.isEmpty(repo) || StringUtils.isEmpty(destPath) || StringUtils.isEmpty(fileNameToUpload) || artifactory == null){
            throw new IllegalArgumentException("Arguments passed to uploadFile are not valid");
        }

       // Path path = Paths.get(fileNameToUpload);
       // Files.write(path, Collections.singleton("This is an example line"), Charset.forName("UTF-8"));

        java.io.File file = new java.io.File(fileNameToUpload);

        return artifactory.repository(repo).upload(destPath, file).doUpload();
    }

    /**
     * Search for file by name in a specific repository, return the location of file
     */
    public static List<RepoPath> searchFile(Artifactory artifactory, String repoName, String fileToSearch) {
        if (artifactory == null || StringUtils.isEmpty(repoName) || StringUtils.isEmpty(fileToSearch)){
            throw new IllegalArgumentException("Arguments passed to serachFile are not valid");
        }

        return artifactory.searches()
                .repositories(repoName)
                .artifactsByName(fileToSearch)
                .doSearch();
    }

    /**
     * Download the required file from artifactory
     */
    public static java.io.File downloadFile(Artifactory artifactory, String repo, String filePath, String fileDownloadToLocation) throws Exception {
        if (artifactory == null || StringUtils.isEmpty(repo) || StringUtils.isEmpty(filePath)){
            throw new IllegalArgumentException("Arguments passed to downloadFile are not valid");
        }

        InputStream inputStream = artifactory.repository(repo)
                .download(filePath)
                .doDownload();

        java.io.File targetFile = new java.io.File(fileDownloadToLocation);
        FileUtils.copyInputStreamToFile(inputStream, targetFile);

        return targetFile;
    }
    
}
