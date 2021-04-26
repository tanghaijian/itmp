package cn.pioneeruniverse.project.service.assetsLibrary.impl;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSONArray;
import com.google.gson.Gson;

import cn.pioneeruniverse.common.utils.CollectionUtil;
import cn.pioneeruniverse.common.utils.CommonUtil;
import cn.pioneeruniverse.common.utils.HTMLDecoderUtil;
import cn.pioneeruniverse.common.utils.PDFUtils;
import cn.pioneeruniverse.common.utils.S3Util;
import cn.pioneeruniverse.common.utils.WordUtil;
import cn.pioneeruniverse.project.dao.mybatis.assetLibrary.TblSystemDirectoryDocumentChaptersHistoryMapper;
import cn.pioneeruniverse.project.dao.mybatis.assetLibrary.TblSystemDirectoryDocumentChaptersMapper;
import cn.pioneeruniverse.project.dao.mybatis.assetLibrary.TblSystemDirectoryDocumentMapper;
import cn.pioneeruniverse.project.dao.mybatis.assetLibrary.TblSystemDirectoryDocumentRequirementMapper;
import cn.pioneeruniverse.project.entity.TblSystemDirectoryDocument;
import cn.pioneeruniverse.project.entity.TblSystemDirectoryDocumentChapters;
import cn.pioneeruniverse.project.entity.TblSystemDirectoryDocumentChaptersHistory;
import cn.pioneeruniverse.project.entity.TblSystemDirectoryDocumentChaptersRelation;
import cn.pioneeruniverse.project.service.assetsLibrary.DocumentChaptersService;
import cn.pioneeruniverse.project.vo.ZtreeVo;

@Service
@Transactional(readOnly=true)
public class DocumentChaptersServiceImpl implements DocumentChaptersService{

	@Autowired
	private TblSystemDirectoryDocumentChaptersMapper tblSystemDirectoryDocumentChaptersMapper;
	@Autowired
	private TblSystemDirectoryDocumentMapper tblSystemDirectoryDocumentMapper;
	@Autowired
	private TblSystemDirectoryDocumentRequirementMapper systemDirectoryDocumentRequirementMapper;
	@Autowired
	private TblSystemDirectoryDocumentChaptersHistoryMapper chaptersHistoryMapper;
	@Autowired
    private S3Util s3Util;
	
	/**
	 * 
	* @Title: getChaptersTree
	* @Description: 通过文档ID获取章节树
	* @author author
	* @param systemDirectoryDocumentId
	* @return List<ZtreeVo> 页面树形要求格式返回
	 */
	@Override
	public List<ZtreeVo> getChaptersTree(Long systemDirectoryDocumentId) {
		return tblSystemDirectoryDocumentChaptersMapper.getChaptersTreeByDocumentId(systemDirectoryDocumentId);
	}

	/**
	 * 
	* @Title: selectDocumentByPage
	* @Description: 分页获取文档
	* @author author
	* @param tblSystemDirectoryDocument 封装的查询条件
	* @return List<TblSystemDirectoryDocument> 文档列表
	 */
	@Override
	public List<TblSystemDirectoryDocument> selectDocumentByPage(TblSystemDirectoryDocument tblSystemDirectoryDocument) {
		List<TblSystemDirectoryDocument> list = tblSystemDirectoryDocumentMapper.getAllDocument(tblSystemDirectoryDocument);
		return list;
	}

	/**
	 * 
	* @Title: getRelationDocumentAndChapters
	* @Description: 获取关联的文档以及关联的章节
	* @author author
	* @param tblSystemDirectoryDocumentChaptersRelation 封装的查询条件
	* @return map key documents :关联的文档
	*                 chapters: 关联的章节
	* @throws
	 */
	@Override
	public Map<String, Object> getRelationDocumentAndChapters(TblSystemDirectoryDocumentChaptersRelation tblSystemDirectoryDocumentChaptersRelation) {
		Map<String, Object> map = new HashMap<>();
		List<TblSystemDirectoryDocument> documentList = tblSystemDirectoryDocumentMapper.getRelationDocument(tblSystemDirectoryDocumentChaptersRelation);
		List<ZtreeVo> chaptersList = tblSystemDirectoryDocumentChaptersMapper.getChaptersCheckedTreeByDocumentId(tblSystemDirectoryDocumentChaptersRelation);
		map.put("documents", documentList);
		map.put("chapters", chaptersList);
		return map;
	}

	/**
	 * 
	* @Title: insertChaptersRelation
	* @Description: 关联章节
	* @author author
	* @param tblSystemDirectoryDocumentChaptersRelation 需要关联的章节信息
	* @param chaptersIdStr 章节ID，以,隔开
	* @param request
	* @throws Exception
	* @throws
	 */
	@Override
	@Transactional(readOnly=false)
	public void insertChaptersRelation(
			TblSystemDirectoryDocumentChaptersRelation tblSystemDirectoryDocumentChaptersRelation,String chaptersIdStr,HttpServletRequest request) throws Exception {
			
		    //删除原有章节关联信息
		    tblSystemDirectoryDocumentChaptersMapper.deleteRelation(tblSystemDirectoryDocumentChaptersRelation);
			List<TblSystemDirectoryDocumentChaptersRelation> list = new ArrayList<TblSystemDirectoryDocumentChaptersRelation>();
			if(!chaptersIdStr.isEmpty()) {
				//被关联章节ID
				for(String id:chaptersIdStr.split(",")) {
					TblSystemDirectoryDocumentChaptersRelation tblSystemDirectoryDocumentChaptersRelation1 = new TblSystemDirectoryDocumentChaptersRelation();
					BeanUtils.copyProperties(tblSystemDirectoryDocumentChaptersRelation1, tblSystemDirectoryDocumentChaptersRelation);
					tblSystemDirectoryDocumentChaptersRelation1.setSystemDirectoryDocumentChapterId2(Long.valueOf(id));
					CommonUtil.setBaseValue(tblSystemDirectoryDocumentChaptersRelation1, request);
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
	* @Description: 获取该文档下所有关联的章节
	* @author author
	* @param systemDirectoryDocumentId1 目录ID
	* @return
	* @throws
	 */
	@Override
	public Map<String, Object> getAllRelationChapters(Long systemDirectoryDocumentId1) {
		Map<String, Object> map = new HashMap<>();
		List<TblSystemDirectoryDocumentChapters> list = tblSystemDirectoryDocumentChaptersMapper.getRelationChapters(systemDirectoryDocumentId1);
		map.put("rows", list);
		return map;
	}

	/**
	 * 
	* @Title: export
	* @Description: 章节信息导出，支持pdf和word格式
	* @author author
	* @param systemDirectoryDocumentId
	* @param idStr id字符串以,隔开
	* @param type
	* @param request
	* @param response
	* @throws Exception
	* @throws
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
	 * 需求视角-文档版本对比
	 * @param requirementCode 需求编号
	 * @param request
	 * @param response
	 * @return
	 * liushan
	 */
	@Override
	public Map<String, Object> getVersionContrast(Long systemDirectoryDocumentId,String requirementCode, HttpServletRequest request, HttpServletResponse response) throws Exception{
		Map<String, Object> result = new HashMap<>();
		int insertNum = 0;
		List<Map<String,String>> insertList = new ArrayList<>();
		int updateNum = 0;
		List<Map<String,String>> updateList = new ArrayList<>();
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
        result.put("insertNum",insertNum);
        result.put("insertList",insertList);
        result.put("insertPer",sumNum!=0?numberFormat.format((float) insertNum / (float) sumNum * 100)+"%":"0.00%");

        result.put("updateNum",updateNum);
        result.put("updateList",updateList);
        result.put("updatePer",sumNum!=0?numberFormat.format((float) updateNum / (float) sumNum * 100)+"%":"0.00%");

        result.put("deleteNum",deleteNum);
        result.put("deleteList",deleteList);
        result.put("deletePer",sumNum!=0?numberFormat.format((float) deleteNum / (float) sumNum * 100)+"%":"0.00%");

        result.put("sumNum",sumNum);
        result.put("sumPer",sumNum!=0?numberFormat.format(((float)(insertNum+updateNum+deleteNum) / (float) sumNum*100))+"%":"0.00%");
		return result;
	}
	
	/**
	 * 
	* @Title: exportByDocId
	* @Description: 根据文档ID导出章节
	* @author author
	* @param systemDirectoryDocumentId 文档ID
	* @param type word和pdf
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
