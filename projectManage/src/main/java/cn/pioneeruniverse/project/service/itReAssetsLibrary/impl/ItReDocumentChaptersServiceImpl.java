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
	 * ????????????????????????
	 */
	@Override
	public List<TblSystemDirectoryDocument> selectDocumentByPage(TblSystemDirectoryDocument tblSystemDirectoryDocument) {
		List<TblSystemDirectoryDocument> list = tblSystemDirectoryDocumentMapper.getAllDocument(tblSystemDirectoryDocument);
		return list;
	}

	/**
	 * ????????????????????????????????????????????????
	 */
	@Override
	public Map<String, Object> getRelationDocumentAndChapters(TblSystemDirectoryDocumentChaptersRelation tblSystemDirectoryDocumentChaptersRelation) {
		Map<String, Object> map = new HashMap<>();
		List<TblSystemDirectoryDocument> documentList = tblSystemDirectoryDocumentMapper.getRelationDocument(tblSystemDirectoryDocumentChaptersRelation);
		List<ZtreeVo> chaptersList = tblSystemDirectoryDocumentChaptersMapper.getChaptersCheckedTreeByDocumentId(tblSystemDirectoryDocumentChaptersRelation);
		//????????????
		map.put("documents", documentList);
		//????????????
		map.put("chapters", chaptersList);
		return map;
	}

	/**
	 * ????????????
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
					//IT???????????????url??????????????????????????????????????????????????????
					encryptor.setPassword("ccic");  
					//????????????????????????+????????????
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
	* @Description: ?????????????????????????????????????????????
	* @author author
	* @param systemDirectoryDocumentId1 ??????ID
	* @return Map<String, Object>
	 */
	@Override
	public Map<String, Object> getAllRelationChapters(Long systemDirectoryDocumentId1) {
		Map<String, Object> map = new HashMap<>();
		List<TblSystemDirectoryDocumentChapters> list = tblSystemDirectoryDocumentChaptersMapper.getRelationChapters(systemDirectoryDocumentId1);
		//????????????
		map.put("rows", list);
		return map;
	}

	/**
	 * ??????????????????word
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
	* @Description: ????????????-??????????????????
	* @author author
	* @param systemDirectoryDocumentId ??????ID
	* @param requirementCode ????????????
	* @param featureCode ??????????????????
	* @param request
	* @param response
	* @return Map<String, Object>
	* @throws Exception
	 */
	@Override
	public Map<String, Object> getVersionContrast(Long systemDirectoryDocumentId,String requirementCode, String featureCode,HttpServletRequest request, HttpServletResponse response) throws Exception{
		Map<String, Object> result = new HashMap<>();
		//???????????????
		int insertNum = 0;
		List<Map<String,String>> insertList = new ArrayList<>();
		//???????????????
		int updateNum = 0;
		List<Map<String,String>> updateList = new ArrayList<>();
		//?????????
		int deleteNum = 0;
		List<Map<String,String>> deleteList = new ArrayList<>();

		// 1???????????????ID??????tbl_system_directory_document_requirement?????????????????????????????????
		Long minVersion = systemDirectoryDocumentRequirementMapper.getMinVersion(systemDirectoryDocumentId,requirementCode);
		// 2???????????????????????????????????????????????????tbl_system_directory_document_chapters_history???????????????????????????ID
        List<Long> chaptersHistoryIds = chaptersHistoryMapper.getChaptersHistoryBySystemDirectoryDocumentIdAndVersion(systemDirectoryDocumentId,minVersion);
		// 3???????????????ID??????tbl_system_directory_document_chapters_requirement???????????????????????????ID
		List<TblSystemDirectoryDocumentChapters> chapters = tblSystemDirectoryDocumentChaptersMapper.getChaptersByRequirementId(systemDirectoryDocumentId,requirementCode);;
        Set<Long> chapterIds = new HashSet(CollectionUtil.extractToList(chapters,"id"));
		Map<Object,Object> chapterMap = CollectionUtil.listToMapObj(chapters,"id");
        // ?????? ?????????????????????????????????????????????tbl_system_directory_document_chapters_history?????????????????????????????????????????????ID??????
		int sumNum = chaptersHistoryMapper.getreqChaptersCount(systemDirectoryDocumentId,requirementCode);
		if(sumNum != 0){
			Collection chapterId_collect = new ArrayList<Long>(chapterIds);
			// ???????????????id???????????????
			Collection diffentIds = CollectionUtil.getDiffentNoDuplicate(chaptersHistoryIds,chapterIds);
			diffentIds.retainAll(chapterId_collect);
			// 4??????????????????3???2???????????????,????????????????????????
			// ?????????????????????????????????ID 3???2????????????????????????????????????????????????????????????
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

			// ???????????????id(3,2??????,??????)
			Collection someIds = new ArrayList<Long>(chaptersHistoryIds);
			someIds.retainAll(chapterId_collect);
			for (Object sId: someIds) {
				// ????????????????????????????????????????????????
				TblSystemDirectoryDocumentChaptersHistory chaptersHistory = chaptersHistoryMapper.selectReqChaptersStatus((Long) sId,requirementCode);
				OptMap.put("id",sId.toString());
				OptMap.put("chapterName",chaptersHistory.getChaptersName());
				// 5??????????????????3???2???????????????????????????????????????
				if(chaptersHistory.getStatus().intValue() == 1){
					updateNum++;
					updateList.add(OptMap);
				} else {
					// 6??????????????????3???2??????????????????????????????????????????
					deleteNum++;
					deleteList.add(OptMap);
				}
				OptMap = new HashMap<>();
			}
		}
		NumberFormat numberFormat = NumberFormat.getInstance();
		numberFormat.setMaximumFractionDigits(2);
		//?????????
        result.put("insertNum",insertNum);
        //????????????
        result.put("insertList",insertList);
        //????????????
        result.put("insertPer",sumNum!=0?numberFormat.format((float) insertNum / (float) sumNum * 100)+"%":"0.00%");

        //?????????
        result.put("updateNum",updateNum);
        //????????????
        result.put("updateList",updateList);
        //????????????
        result.put("updatePer",sumNum!=0?numberFormat.format((float) updateNum / (float) sumNum * 100)+"%":"0.00%");

        //?????????
        result.put("deleteNum",deleteNum);
        //????????????
        result.put("deleteList",deleteList);
        //????????????
        result.put("deletePer",sumNum!=0?numberFormat.format((float) deleteNum / (float) sumNum * 100)+"%":"0.00%");

        //??????
        result.put("sumNum",sumNum);
        result.put("sumPer",sumNum!=0?numberFormat.format(((float)(insertNum+updateNum+deleteNum) / (float) sumNum*100))+"%":"0.00%");
		return result;
	}
	
	/**
	 * 
	* @Title: exportByDocId
	* @Description: ????????????ID??????????????????
	* @author author
	* @param systemDirectoryDocumentId ??????ID
	* @param type ????????????1word,2pdf
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
	 * ???????????????html
	 * @param chapters
	 * @param num
	 * @param version(?????????)
	 * @param idList(?????????id)
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
	 * ??????????????????
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
