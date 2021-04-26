package cn.pioneeruniverse.project.common;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.gridfs.GridFsResource;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import org.springframework.stereotype.Repository;
/**
 *
 * @ClassName: MongoDB
 * @Description: MongoDB连接,从IT全流程同步需求附件等场景使用
 * @author author
 * @date 2020年8月21日 下午1:23:58
 *
 */
@Repository
public class MongoDB{
	
	//由于IT全流程方采用的主从形式的mongodb，而本系统版本原因，直接将主从mongodb拆分成两个独立的mongodb
	@Autowired
	private GridFsTemplate oldGridFsTemplate;
	@Autowired
	private GridFsTemplate newGridFsTemplate;

	static Logger logger = Logger.getLogger(MongoDB.class);


	public String add(byte[] stream) throws IOException {
		InputStream inputStream = new ByteArrayInputStream(stream);
		String fileName = UUID.randomUUID().toString();
		newGridFsTemplate.store(inputStream, fileName);
		return fileName;
	}

	public InputStream getInput(String file) { // url
		InputStream inputStream = null;
		try {
			GridFsResource gridFsResource= oldGridFsTemplate.getResource(file);
			if(gridFsResource != null){
				inputStream = gridFsResource.getInputStream();
			}else{
				System.out.println("无附件");
			}
		}catch(Exception e) {
			e.printStackTrace();
		}
		return inputStream;
	}

	public void delete(String attachmentPath) {
		newGridFsTemplate.delete(new Query(Criteria.where("filename").is(attachmentPath)));
	}
}

