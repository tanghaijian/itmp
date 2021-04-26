package cn.pioneeruniverse.common.artifactory;

import java.io.IOException;
import java.util.List;

import org.jfrog.artifactory.client.Artifactory;
import org.jfrog.artifactory.client.model.File;
import org.jfrog.artifactory.client.model.RepoPath;

/**
* 测试
* @author:tingting
* @version:2018年10月24日 上午11:03:00 
*/

public class TestArtifactory {
	
    public static String repoName = "artifactory_client_repo";
    public static String fileNameToUpload = "C:\\Users\\tingting\\Desktop\\current\\project\\projectPG\\PGproject.war";
    public static String fileUploadToLocation = "pgproject/PGproject.war";
   // public static String fileDownloadToLocation = "ex_download_1.txt";
    public static String fileDownloadToLocation = "C:\\a\\a.jar";

//	public static void main(String[] args) {
//		//create artifactory object
//        Artifactory artifactory = ArtifactoryUtils.createArtifactory(ArtifactoryConstants.ARTIFACTORY_UDERNAME, ArtifactoryConstants.ARTIFACTORY_PASSWORD, ArtifactoryConstants.ARTIFACTORY_URL);
//
//        if (artifactory == null){
//            throw new RuntimeException("artifactory creation failed");
//        }
//        System.out.println("artifactory"+artifactory);
//        
//      //create repository
//       // String repositoryCreationResult = ArtifactoryUtils.createNewRepository(artifactory, repoName);
//       // System.out.println("repository===="+repositoryCreationResult);
//        
//        //delete repository
//      //  String deleteResult = ArtifactoryUtils.deleteRepository(artifactory,repoName);
//       // System.out.println(deleteResult);
//
//        //create and upload a file
//       /* try {
//			File uploadedFile = ArtifactoryUtils.uploadFile(artifactory, repoName, fileUploadToLocation, fileNameToUpload);
//			
//        } catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//*/
//        //search for file
//        //List<RepoPath> searchResult = ArtifactoryUtils.searchFile(artifactory, "libs-release-local" , "antlr.jar");
//        //System.out.println("search"+searchResult);//search[RepoPathImpl{repoKey='artifactory_client_repo', itemPath='ex_fold1/ex_upload_1.txt'}]
//
//        //download file from artifactory
//        try {
//			java.io.File downloadedFile = ArtifactoryUtils.downloadFile(artifactory, "libs-release-local", "antlr/antlr/2.7.2/antlr-2.7.2.jar", fileDownloadToLocation);
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//        
//
//        System.out.print("Test finished.");
//	}

}
