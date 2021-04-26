package cn.pioneeruniverse.project.service.itReAssetsLibrary.impl;

import java.sql.Timestamp;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang3.StringUtils;
import org.jasypt.encryption.pbe.StandardPBEStringEncryptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSONArray;
import com.google.gson.Gson;

import cn.pioneeruniverse.common.utils.CollectionUtil;
import cn.pioneeruniverse.common.utils.HTMLDecoderUtil;
import cn.pioneeruniverse.common.utils.PDFUtils;
import cn.pioneeruniverse.common.utils.S3Util;
import cn.pioneeruniverse.common.utils.WordUtil;
import cn.pioneeruniverse.project.dao.mybatis.assetLibrary.TblSystemDirectoryDocumentChaptersHistoryMapper;
import cn.pioneeruniverse.project.dao.mybatis.itReAssetLibrary.ReASystemDirectoryDocumentYiRanDao;
import cn.pioneeruniverse.project.dao.mybatis.itReAssetLibrary.ReTblSystemDirectoryDocumentChaptersMapper;
import cn.pioneeruniverse.project.dao.mybatis.itReAssetLibrary.ReTblSystemDirectoryDocumentMapper;
import cn.pioneeruniverse.project.dao.mybatis.itReAssetLibrary.ReTblSystemDirectoryDocumentRequirementMapper;
import cn.pioneeruniverse.project.entity.TblSystemDirectoryDocument;
import cn.pioneeruniverse.project.entity.TblSystemDirectoryDocumentChapters;
import cn.pioneeruniverse.project.entity.TblSystemDirectoryDocumentChaptersHistory;
import cn.pioneeruniverse.project.entity.TblSystemDirectoryDocumentChaptersRelation;
import cn.pioneeruniverse.project.service.itReAssetsLibrary.ItReDocumentChaptersService;
import cn.pioneeruniverse.project.vo.ZtreeVo;

/**
 * 
 * @author xukai
 *
 */
@Service
@Transactional(readOnly=true)
public class ItReDocumentChaptersServiceImpl implements ItReDocumentChaptersService{

	@Autowired
	private ReTblSystemDirectoryDocumentChaptersMapper tblSystemDirectoryDocumentChaptersMapper;
	@Autowired
	private ReTblSystemDirectoryDocumentMapper tblSystemDirectoryDocumentMapper;
	@Autowired
	private ReTblSystemDirectoryDocumentRequirementMapper systemDirectoryDocumentRequirementMapper;
	@Autowired
	private TblSystemDirectoryDocumentChaptersHistoryMapper chaptersHistoryMapper;
	@Autowired
    private S3Util s3Util;
	@Autowired
    private ReASystemDirectoryDocumentYiRanDao reASystemDirectoryDocumentYiRanDao;
	@Override
	public List<ZtreeVo> getChaptersTree(Long systemDirectoryDocumentId) {
		return tblSystemDirectoryDocumentChaptersMapper.getChaptersTreeByDocumentId(systemDirectoryDocumentId);
	}

	/**
	 * 分页获取文档列表
	 */
	@Override
	public List<TblSystemDirectoryDocument> selectDocumentByPage(TblSystemDirectoryDocument tblSystemDirectoryDocument) {
		List<TblSystemDirectoryDocument> list = tblSystemDirectoryDocumentMapper.getAllDocument(tblSystemDirectoryDocument);
		return list;
	}

	/**
	 * 获取关联的文档章节树和关联的文档
	 */
	@Override
	public Map<String, Object> getRelationDocumentAndChapters(TblSystemDirectoryDocumentChaptersRelation tblSystemDirectoryDocumentChaptersRelation) {
		Map<String, Object> map = new HashMap<>();
		List<TblSystemDirectoryDocument> documentList = tblSystemDirectoryDocumentMapper.getRelationDocument(tblSystemDirectoryDocumentChaptersRelation);
		List<ZtreeVo> chaptersList = tblSystemDirectoryDocumentChaptersMapper.getChaptersCheckedTreeByDocumentId(tblSystemDirectoryDocumentChaptersRelation);
		//文档信息
		map.put("documents", documentList);
		//章节信息
		map.put("chapters", chaptersList);
		return map;
	}

	/**
	 * 关联章节
	 */
	@Override
	@Transactional(readOnly=false)
	public void insertChaptersRelation(
			TblSystemDirectoryDocumentChaptersRelation tblSystemDirectoryDocumentChaptersRelation,String chaptersIdStr,String currentUserAccount,HttpServletRequest request) throws Exception {
			tblSystemDirectoryDocumentChaptersMapper.deleteRelation(tblSystemDirectoryDocumentChaptersRelation);
			List<TblSystemDirectoryDocumentChaptersRelation> list = new ArrayList<TblSystemDirectoryDocumentChaptersRelation>();
			if(!chaptersIdStr.isEmpty()) {
				for(String id:chaptersIdStr.split(",")) {
					TblSystemDirectoryDocumentChaptersRelation tblSystemDirectoryDocumentChaptersRelation1 = new TblSystemDirectoryDocumentChaptersRelation();
					BeanUtils.copyProperties(tblSystemDirectoryDocumentChaptersRelation1, tblSystemDirectoryDocumentChaptersRelation);
					tblSystemDirectoryDocumentChaptersRelation1.setSystemDirectoryDocumentChapterId2(Long.valueOf(id));
					StandardPBEStringEncryptor encryptor = new StandardPBEStringEncryptor();  
					//IT全流程通过url传送的时候有加密，因此需要在此处解密
					encryptor.setPassword("ccic");  
					//加密后的特殊字符+进行处理
					currentUserAccount = currentUserAccount.replaceAll(" ","+");
					String userAccount=encryptor.decrypt(currentUserAccount);
					Long userId  = reASystemDirectoryDocumentYiRanDao.selectUserId(userAccount);
					tblSystemDirectoryDocumentChaptersRelation1.setCreateBy(userId);
					tblSystemDirectoryDocumentChaptersRelation1.setCreateDate(new Timestamp(new Date().getTime()));
					tblSystemDirectoryDocumentChaptersRelation1.setLastUpdateBy(userId);
					tblSystemDirectoryDocumentChaptersRelation1.setLastUpdateDate(new Timestamp(new Date().getTime()));
					tblSystemDirectoryDocumentChaptersRelation1.setStatus(1);
					list.add(tblSystemDirectoryDocumentChaptersRelation1);
				}
			}
			if(!list.isEmpty()) {
				tblSystemDirectoryDocumentChaptersMapper.insertDocumentRelation(list);
			}
	}

	/**
	 * 
	* @Title: getAllRelationChapters
	* @Description: 获取所有某个文档所有的关联章节
	* @author author
	* @param systemDirectoryDocumentId1 文档ID
	* @return Map<String, Object>
	 */
	@Override
	public Map<String, Object> getAllRelationChapters(Long systemDirectoryDocumentId1) {
		Map<String, Object> map = new HashMap<>();
		List<TblSystemDirectoryDocumentChapters> list = tblSystemDirectoryDocumentChaptersMapper.getRelationChapters(systemDirectoryDocumentId1);
		//章节信息
		map.put("rows", list);
		return map;
	}

	/**
	 * 生成章节正文word
	 */
	@Override
	public void export(Long systemDirectoryDocumentId,String idStr,String type, HttpServletRequest request, HttpServletResponse response) throws Exception {
		// TODO Auto-generated method stub
		if(!StringUtils.isEmpty(idStr)) {
			List<String> list = JSONArray.parseArray(idStr,String.class);
			if(systemDirectoryDocumentId != null) {
				List<TblSystemDirectoryDocumentChapters> chapters = tblSystemDirectoryDocumentChaptersMapper.getChaptersByDocumentId(systemDirectoryDocumentId);
				String htmlString = getHtmlString(chapters,null,null,list);
				String filename = tblSystemDirectoryDocumentMapper.selectNameById(systemDirectoryDocumentId);
				if(type.equals("word")) {
					WordUtil.htmlToWord(htmlString,filename, response);
				}else if(type.equals("pdf")) {
					PDFUtils.htmlToPDF(htmlString,filename,request, response);
				}
			}
		}
	}

	/**
	 * 
	* @Title: getVersionContrast
	* @Description: 需求视角-文档版本对比
	* @author author
	* @param systemDirectoryDocumentId 文档ID
	* @param requirementCode 需求编码
	* @param featureCode 开发任务编码
	* @param request
	* @param response
	* @return Map<String, Object>
	* @throws Exception
	 */
	@Override
	public Map<String, Object> getVersionContrast(Long systemDirectoryDocumentId,String requirementCode, String featureCode,HttpServletRequest request, HttpServletResponse response) throws Exception{
		Map<String, Object> result = new HashMap<>();
		//新增章节数
		int insertNum = 0;
		List<Map<String,String>> insertList = new ArrayList<>();
		//更新章节数
		int updateNum = 0;
		List<Map<String,String>> updateList = new ArrayList<>();
		//删除数
		int deleteNum = 0;
		List<Map<String,String>> deleteList = new ArrayList<>();

		// 1、通过需求ID，在tbl_system_directory_document_requirement表找出对应的文档版本号
		Long minVersion = systemDirectoryDocumentRequirementMapper.getMinVersion(systemDirectoryDocumentId,requirementCode);
		// 2、通过文档版本号中最小的版本号，在tbl_system_directory_document_chapters_history表中找出原始的章节ID
        List<Long> chaptersHistoryIds = chaptersHistoryMapper.getChaptersHistoryBySystemDirectoryDocumentIdAndVersion(systemDirectoryDocumentId,minVersion);
		// 3、通过需求ID，在tbl_system_directory_document_chapters_requirement表找出，关联的章节ID
		List<TblSystemDirectoryDocumentChapters> chapters = tblSystemDirectoryDocumentChaptersMapper.getChaptersByRequirementId(systemDirectoryDocumentId,requirementCode);;
        Set<Long> chapterIds = new HashSet(CollectionUtil.extractToList(chapters,"id"));
		Map<Object,Object> chapterMap = CollectionUtil.listToMapObj(chapters,"id");
        // 合计 需求关联的文档的最大版本号，在tbl_system_directory_document_chapters_history中查询小于等于最大版本号的章节ID总数
		int sumNum = chaptersHistoryMapper.getreqChaptersCount(systemDirectoryDocumentId,requirementCode);
		if(sumNum != 0){
			Collection chapterId_collect = new ArrayList<Long>(chapterIds);
			// 获取不同的id（不存在）
			Collection diffentIds = CollectionUtil.getDiffentNoDuplicate(chaptersHistoryIds,chapterIds);
			diffentIds.retainAll(chapterId_collect);
			// 4、新增章节：3在2中不存在的,并且状态是正常的
			// 不计入统计的章节：章节ID 3在2中不存在，并且该章节最后的版本状态是删除
			Map<String,String> OptMap = new HashMap<>();
			for (Object dId: diffentIds) {
				TblSystemDirectoryDocumentChapters chapters_Insert_Opt = new Gson().fromJson(chapterMap.get(dId).toString(), TblSystemDirectoryDocumentChapters.class);
				if(chapters_Insert_Opt.getStatus().intValue() == 1){
					TblSystemDirectoryDocumentChapters maxVersionStatus = tblSystemDirectoryDocumentChaptersMapper.getMaxVersionStatusById((Long) dId,requirementCode);
					if(maxVersionStatus.getStatus().intValue() == 1){
						insertNum++;
						OptMap.put("id",dId.toString());
						OptMap.put("chapterName",chapters_Insert_Opt.getChaptersName());
						insertList.add(OptMap);
					} else{
						sumNum--;
					}
				}
				OptMap = new HashMap<>();
			}

			// 获取相同的id(3,2交集,存在)
			Collection someIds = new ArrayList<Long>(chaptersHistoryIds);
			someIds.retainAll(chapterId_collect);
			for (Object sId: someIds) {
				// 需求关联的历史数据章节的最后状态
				TblSystemDirectoryDocumentChaptersHistory chaptersHistory = chaptersHistoryMapper.selectReqChaptersStatus((Long) sId,requirementCode);
				OptMap.put("id",sId.toString());
				OptMap.put("chapterName",chaptersHistory.getChaptersName());
				// 5、修改章节：3在2中存在的，并且状态是正常的
				if(chaptersHistory.getStatus().intValue() == 1){
					updateNum++;
					updateList.add(OptMap);
				} else {
					// 6、删除章节：3在2中存在的，并且状态是删除的，
					deleteNum++;
					deleteList.add(OptMap);
				}
				OptMap = new HashMap<>();
			}
		}
		NumberFormat numberFormat = NumberFormat.getInstance();
		numberFormat.setMaximumFractionDigits(2);
		//新增数
        result.put("insertNum",insertNum);
        //新增明细
        result.put("insertList",insertList);
        //新增占比
        result.put("insertPer",sumNum!=0?numberFormat.format((float) insertNum / (float) sumNum * 100)+"%":"0.00%");

        //更新数
        result.put("updateNum",updateNum);
        //更新明细
        result.put("updateList",updateList);
        //更新占比
        result.put("updatePer",sumNum!=0?numberFormat.format((float) updateNum / (float) sumNum * 100)+"%":"0.00%");

        //删除数
        result.put("deleteNum",deleteNum);
        //删除明细
        result.put("deleteList",deleteList);
        //删除占比
        result.put("deletePer",sumNum!=0?numberFormat.format((float) deleteNum / (float) sumNum * 100)+"%":"0.00%");

        //总计
        result.put("sumNum",sumNum);
        result.put("sumPer",sumNum!=0?numberFormat.format(((float)(insertNum+updateNum+deleteNum) / (float) sumNum*100))+"%":"0.00%");
		return result;
	}
	
	/**
	 * 
	* @Title: exportByDocId
	* @Description: 根据文档ID导出章节信息
	* @author author
	* @param systemDirectoryDocumentId 文档ID
	* @param type 导出类型1word,2pdf
	* @param request
	* @param response
	* @throws Exception
	* @throws
	 */
	@Override
	public void exportByDocId(Long systemDirectoryDocumentId,String type, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		// TODO Auto-generated method stub
		if(systemDirectoryDocumentId != null) {
			List<TblSystemDirectoryDocumentChapters> chapters = tblSystemDirectoryDocumentChaptersMapper.getChaptersByDocumentId(systemDirectoryDocumentId);
			String htmlString = getHtmlString(chapters,null,null,null);
			String filename = tblSystemDirectoryDocumentMapper.selectNameById(systemDirectoryDocumentId);
			if(type.equals("word")) {
				WordUtil.htmlToWord(htmlString,filename, response);
			}else if(type.equals("pdf")) {
				PDFUtils.htmlToPDF(htmlString,filename,request, response);
			}
		}
	}
	
	/**
	 * 获取拼接的html
	 * @param chapters
	 * @param num
	 * @param version(版本号)
	 * @param idList(筛选的id)
	 * @return
	 */
	private String getHtmlString(List<TblSystemDirectoryDocumentChapters> chapters,String num,Integer version,List<String> idList) {
		String htmlString = "";
		int level = 0;
		String newNum = "";
		for(int i = 0;i < chapters.size();i++) {
			if(num == null) {
				level = 1;
				newNum = (i+1)+"";
			}else {
				level = num.split("\\.").length+1;
				newNum = num + "." + (i+1);
			}
			String titleString = "<h"+level+">"+newNum+" "+ chapters.get(i).getChaptersName()+"</h"+level+">";
			String html = "";
			if(StringUtils.isNotEmpty(chapters.get(i).getChaptersS3Bucket()) && StringUtils.isNotEmpty(chapters.get(i).getChaptersS3Key2())) {
				html = s3Util.getStringByS3(chapters.get(i).getChaptersS3Bucket(), chapters.get(i).getChaptersS3Key2());
				html = html.equals("null")?"":html;
			}
			html = HTMLDecoderUtil.unescapeHtml(html);
			if((version != null && chapters.get(i).getDocumentVersion() != null && version >= chapters.get(i).getDocumentVersion())
					|| (idList!=null && idList.contains(chapters.get(i).getId().toString()))
					|| (version == null && idList == null)) {
				htmlString += titleString+html;
			}
			if(chapters.get(i).getChildChapters() != null) {
				htmlString += getHtmlString(chapters.get(i).getChildChapters(),newNum,version,idList);
			}
		}
		return htmlString;
	}

	/**
	 * 导出历史版本
	 */
	@Override
	public void exportByDocIdAndVersion(Long systemDirectoryDocumentId, Integer version,String type, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		// TODO Auto-generated method stub
		if(systemDirectoryDocumentId != null) {
			List<TblSystemDirectoryDocumentChapters> chapters = tblSystemDirectoryDocumentChaptersMapper.getChaptersByDocumentId(systemDirectoryDocumentId);
			String htmlString = getHtmlString(chapters,null,version,null);
			String filename = tblSystemDirectoryDocumentMapper.selectNameById(systemDirectoryDocumentId);
			if(type.equals("word")) {
				WordUtil.htmlToWord(htmlString,filename, response);
			}else if(type.equals("pdf")) {
				PDFUtils.htmlToPDF(htmlString,filename,request, response);
			}
				
		}
	}

}
