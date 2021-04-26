package cn.pioneeruniverse.project.service.assetsLibrary.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.pioneeruniverse.project.dao.mybatis.RequirementFeatureMapper;
import cn.pioneeruniverse.project.dao.mybatis.RequirementMapper;
import cn.pioneeruniverse.project.dao.mybatis.RequirmentSystemMapper;
import cn.pioneeruniverse.project.dao.mybatis.SystemInfoMapper;
import cn.pioneeruniverse.project.dao.mybatis.assetLibrary.TblSystemDirectoryDocumentAttachmentMapper;
import cn.pioneeruniverse.project.dao.mybatis.assetLibrary.TblSystemDirectoryDocumentChaptersMapper;
import cn.pioneeruniverse.project.dao.mybatis.assetLibrary.TblSystemDirectoryDocumentChaptersRequirementFeatureMapper;
import cn.pioneeruniverse.project.dao.mybatis.assetLibrary.TblSystemDirectoryDocumentChaptersRequirementMapper;
import cn.pioneeruniverse.project.dao.mybatis.assetLibrary.TblSystemDirectoryDocumentHistoryMapper;
import cn.pioneeruniverse.project.dao.mybatis.assetLibrary.TblSystemDirectoryDocumentMapper;
import cn.pioneeruniverse.project.dao.mybatis.assetLibrary.TblSystemDirectoryMapper;
import cn.pioneeruniverse.project.entity.AssetsZtree;
import cn.pioneeruniverse.project.entity.TblRequirementFeature;
import cn.pioneeruniverse.project.entity.TblRequirementInfo;
import cn.pioneeruniverse.project.entity.TblRequirementSystem;
import cn.pioneeruniverse.project.entity.TblSystemDirectory;
import cn.pioneeruniverse.project.entity.TblSystemDirectoryDocument;
import cn.pioneeruniverse.project.entity.TblSystemDirectoryDocumentAttachment;
import cn.pioneeruniverse.project.entity.TblSystemDirectoryDocumentChapters;
import cn.pioneeruniverse.project.entity.TblSystemInfo;
import cn.pioneeruniverse.project.service.assetsLibrary.IAssetsLibraryRqService;
@Transactional(readOnly = true)
@Service("iAssetsLibraryRqService")
public class AssetsLibraryRqServiceImpl implements IAssetsLibraryRqService {
    @Autowired
    private TblSystemDirectoryDocumentMapper tblSystemDirectoryDocumentMapper;
    @Autowired
    private RequirementMapper tblRequirementInfoMapper;
    @Autowired
    private RequirementFeatureMapper tblRequirementFeatureMapper;
    @Autowired
    private SystemInfoMapper tblSystemInfoMapper;
    @Autowired
    private TblSystemDirectoryMapper tblSystemDirectoryMapper;
    @Autowired
    private TblSystemDirectoryDocumentHistoryMapper tblSystemDirectoryDocumentHistoryMapper;
    @Autowired
    private TblSystemDirectoryDocumentAttachmentMapper tblSystemDirectoryDocumentAttachmentMapper;
    @Autowired
    private TblSystemDirectoryDocumentChaptersRequirementMapper chaptersRequirementMapper;
    @Autowired
    private RequirmentSystemMapper tblRequirmentSystemMapper;
    @Autowired
    private TblSystemDirectoryDocumentChaptersMapper tblSystemDirectoryDocumentChaptersMapper;


    /**
     * 系统目录文档章节开发任务
     **/
    @Autowired
    private TblSystemDirectoryDocumentChaptersRequirementFeatureMapper chaptersRequirementFeatureMapper;
    public final static Logger logger = LoggerFactory.getLogger(AssetsLibraryRqServiceImpl.class);


    /**
     *  部门视角目录
     * @param  requireIds 搜索需求id
     * @param  systemIds  搜索系统id
     * @param  reqTaskIds 搜索任务id
     * @param  type       目录层级 1部门 2需求
     * @param  znodeId    一层目录id
     * @param  search     是否为搜索
     * @return Map<String, Object> key:znodes value:List<AssetsZtree>
     *
     */
    @Override
    public Map<String, Object> getDeptdirectory(Map<String, Object> result, long[] requireIds, long[] systemIds, long[] reqTaskIds,
                                                String type, String znodeId,String search) {
        //获取第三层固定数据
//        Object redisDocType = redisUtils.get("DOCUMENT_TYPE1");
//        Map<String, Object> docTypeMap = JSON.parseObject(redisDocType.toString());
        Map<String, Object> docTypeMap =getDocumentType();
        List<AssetsZtree> znodes = new ArrayList<>();
       // requireIds = getRequiredIds(requireIds, systemIds, reqTaskIds);
        Map<String, Object> mapParam = new HashMap<>();
        //查询条件 search:false没有查询条件，true有查询
        mapParam.put("requirementIds", requireIds);//需求
        mapParam.put("systemIds", systemIds);//系统
        mapParam.put("reqTaskIds",reqTaskIds);//开发任务
        if (type.equals("1") && search.equals("false")) {//单独查询部门
            List<Map<String, Object>> deptlists = tblSystemDirectoryDocumentMapper.getDeptdirectory(mapParam);
            addDeptNodes(deptlists,znodes);
        } else if(type.equals("2")&& search.equals("false")) {//查询部门下需求
            mapParam.put("deptId", znodeId);
            List<Map<String, Object>> requiredirectorylists = tblSystemDirectoryDocumentMapper.getRequiredirectory(mapParam);
            addDeptReqNodes( requiredirectorylists, znodes, znodeId,  docTypeMap);
        }else if(type.equals("1") && search.equals("true")) {//搜索框查询
            List<Map<String, Object>> deptlists = tblSystemDirectoryDocumentMapper.getDeptdirectory(mapParam);
            for (Map<String, Object> map : deptlists) {
                String id = map.get("APPLY_DEPT_ID").toString();
                AssetsZtree pNode=new AssetsZtree();
                pNode.setId(id);
                pNode.setpId("0");
                pNode.setName(map.get("DEPT_NAME").toString());
                pNode.setLevel("1");
                pNode.setRealId(id);
                pNode.setParent(true);
                pNode.setType("2");
                znodes.add(pNode);
                Map<String, Object> deParam = new HashMap<>();
                //查询第三层
                deParam.put("deptId", id);
                deParam.put("requirementIds", requireIds);
                deParam.put("systemIds", systemIds);
                deParam.put("reqTaskIds",reqTaskIds);
                List<Map<String, Object>> requiredirectorylists = tblSystemDirectoryDocumentMapper.getRequiredirectory(deParam);
                addDeptReqNodes( requiredirectorylists, znodes, id,  docTypeMap);
            }
        }
        //部门树形结构
        result.put("znodes", znodes);
        return result;
    }

    /**
     * 
    * @Title: getDocumentType
    * @Description: 获取文档类型
    * @author author
    * @return Map<String,Object> key:id 文档类型ID， value:文档类型
     */
    private Map<String, Object> getDocumentType(){

       Map<String, Object> docTypeMap=new HashMap<>();
       List< Map<String, Object>> list= tblSystemDirectoryDocumentMapper.getDocumentType();
       for(Map<String, Object> map:list){
           String key=map.get("id").toString();
           String value=map.get("documentType").toString();
           docTypeMap.put(key,value);
       }

      return docTypeMap;
    }

    /**
     * 
    * @Title: getAllDocumentType
    * @Description: 获取所有文档类型
    * @author author
    * @return Map<String,Object> key:id 文档类型ID， value:文档类型
     */
    private Map<String, Object> getAllDocumentType(){

        Map<String, Object> docTypeMap=new HashMap<>();
        List< Map<String, Object>> list= tblSystemDirectoryDocumentMapper.getAllDocumentType();
        for(Map<String, Object> map:list){
            String key=map.get("id").toString();
            String value=map.get("documentType").toString();
            docTypeMap.put(key,value);
        }

        return docTypeMap;
    }
    /**
     *  系统视角目录
     * @param  requireIds 搜索需求id
     * @param  systemIds  搜索系统id
     * @param  reqTaskIds 搜索任务id
     * @param  type       目录层级 1部门 2需求
     * @param  znodeId    一层目录id
     * @param  search     是否为搜索
     * @return Map<String, Object> key:znodes value:List<AssetsZtree>
     */
    @Override
    public Map<String, Object> getSystemDirectory(Map<String, Object> result, long[] requireIds,
                                                  long[] systemIds, long[] reqTaskIds, String type, String znodeId,String search) {
        //获取第三层固定数据
//        Object redisEnvType = redisUtils.get("DOCUMENT_TYPE1");
//        Map<String, Object> docTypeMap = JSON.parseObject(redisEnvType.toString());
        Map<String, Object> docTypeMap =getDocumentType();
        List<AssetsZtree> znodes = new ArrayList<>();
       // requireIds = getRequiredIds(requireIds, systemIds, reqTaskIds);
        Map<String, Object> mapParam = new HashMap<>();
        //查询条件
        mapParam.put("requirementIds", requireIds);
        mapParam.put("systemIds", systemIds);
        mapParam.put("reqTaskIds",reqTaskIds);
        if (type.equals("1") &&  search.equals("false")) {//单独系统
        	//获取系统列表
            List<Map<String,Object>> systemInfos = tblSystemDirectoryDocumentMapper.getSystemDirectory(mapParam);
           //组装树形结构
            addSystemNodes(systemInfos,  znodes);

        } else if(type.equals("2") &&  search.equals("false")){//单独需求
            Map<String, Object> param = new HashMap<>();
            long[] systemId = new long[1];
            systemId[0] = Long.parseLong(znodeId);
            param.put("systemIds", systemId);
            //获取需求列表
            List<Map<String, Object>> systemReqIds = tblRequirementInfoMapper.getReqidBysystemId(param);
            //组装树形结构
            addSystemReqNodes(systemReqIds, znodes, znodeId, docTypeMap);

        }else if(type.equals("1") &&   search.equals("true")){//搜索框 搜索转化为系统

            List<Map<String,Object>> systemInfos = tblSystemDirectoryDocumentMapper.getSystemDirectorySearch(mapParam);
            for (Map<String,Object> map : systemInfos) {
                String id = map.get("id").toString();
                AssetsZtree pNode=new AssetsZtree();
                pNode.setId(id);
                pNode.setpId("0");
                pNode.setName(map.get("systemName").toString());
                pNode.setLevel("1");
                pNode.setRealId(id);
                pNode.setType("2");
                pNode.setParent(true);
                znodes.add(pNode);
                Map<String,Object> systemMap=new HashMap<>();
                long[] systemId = new long[1];
                systemId[0] = Long.parseLong(id);
                systemMap.put("systemIds", systemId);
                systemMap.put("requirementIds", requireIds);
                systemMap.put("reqTaskIds",reqTaskIds);
                List<Map<String, Object>> systemReqIds = tblRequirementInfoMapper.getReqidBysystemId(systemMap);
                addSystemReqNodes(systemReqIds, znodes, id,  docTypeMap);


            }
        }
        result.put("znodes", znodes);
        return result;


    }



    //测试
    private void getRequiredir( List<AssetsZtree> znodes, String requireId,String pid,String key,String systemId) {

        Map<String,Object> param=new HashMap<>();
        param.put("STATUS",1);
        param.put("PROJECT_TYPE",1);
        param.put("DIR_NAME","需求说明书");
        param.put("TIER_NUMBER",2);
        param.put("SYSTEM_ID",systemId);
        List<TblSystemDirectory> tblSystemDirectories=tblSystemDirectoryMapper.selectByMap(param);
        for(TblSystemDirectory tblSystemDirectory:tblSystemDirectories){
            String id = tblSystemDirectory.getId().toString();
            if(isExist(String.valueOf(id))){
                recursionDir(Long.parseLong(requireId),id,pid,znodes,key,systemId);
            }

        }

    }





    /**
     * 
    * @Title: getRelationInfo
    * @Description: 文档关联信息
    * @author author
    * @param result
    * @param documetId 文档ID
    * @return Map<String, Object> key:znodes value:List<AssetsZtree>
     */
    @Override
    public Map<String, Object> getRelationInfo(Map<String, Object> result, String documetId) {
        Map<String, Object> docTypeMap =getAllDocumentType();
        List<AssetsZtree> reqZnodes = new ArrayList<>();
        String requireId="";
        Map<String,Object> param=new HashMap<>();
        //通过文档ID获取需求id
        List<String>  requireIds=tblSystemDirectoryDocumentMapper.getRequireBydocuId(documetId);
        if(requireIds!=null && requireIds.size()>0){
            requireId=requireIds.get(0);
        }
        param.put("requirementId",requireId);
        //获取该需求下的所有文档
        List<TblSystemDirectoryDocument> lists=tblSystemDirectoryDocumentMapper.getDocmentByRequire(param);
        for(TblSystemDirectoryDocument td:lists){
            if(td.getDocumentType()!=null){
                AssetsZtree pNode=new AssetsZtree();
                pNode.setId(String.valueOf(td.getId()));
                pNode.setpId("0");
                pNode.setName(td.getDocumentName());
                pNode.setDocType(docTypeMap.get(td.getDocumentType().toString()).toString());
                pNode.setParent(false);
                pNode.setType("doc");//文档
                reqZnodes.add(pNode);
            }

        }
        //查询此需求单相关信息
        TblRequirementInfo tblRequirementInfo= tblRequirementInfoMapper.findRequirementById(Long.parseLong(requireId));
        AssetsZtree pNode=new AssetsZtree();
        pNode.setId("require"+"_"+tblRequirementInfo.getId());
        pNode.setpId("0");
        pNode.setName(tblRequirementInfo.getRequirementName());
        pNode.setCode(tblRequirementInfo.getRequirementCode());
        pNode.setRealId(String.valueOf(tblRequirementInfo.getId()));
        pNode.setType("require");
        //查询出此需求开发任务
        List<TblRequirementFeature> features=selectRequirementFeature(Long.parseLong(requireId));
        if(features!=null && features.size()>0){
            pNode.setParent(true);
        }else{
            pNode.setParent(false);
        }
        reqZnodes.add(pNode);
        //查询关联系统
        List<TblRequirementSystem> systems=tblRequirmentSystemMapper.getReqSystemByReqId(tblRequirementInfo.getId());
        if(systems!=null && systems.size()>0){
            for(TblRequirementSystem tblRequirementSystem:systems){
            if(tblRequirementSystem.getSystemId()!=null){
                TblSystemInfo tblSystemInfo=tblSystemInfoMapper.selectById(tblRequirementSystem.getSystemId());
               AssetsZtree cNode=new AssetsZtree();
               cNode.setId("system"+"_"+tblSystemInfo.getId());
               cNode.setpId("require"+"_"+tblRequirementInfo.getId());
               cNode.setName(tblSystemInfo.getSystemName());
               cNode.setCode(tblSystemInfo.getSystemCode());
               cNode.setRealId(String.valueOf(tblSystemInfo.getId()));
               cNode.setType("system");
               cNode.setParent(false);
               reqZnodes.add(cNode);
            }
            }
        }
        for(TblRequirementFeature tblRequirementFeature:features){
        	//组装开发任务
            AssetsZtree cNode=new AssetsZtree();
            cNode.setId("feature"+"_"+tblRequirementFeature.getId());
            cNode.setpId("require"+"_"+tblRequirementInfo.getId());
            cNode.setName(tblRequirementFeature.getRequirementName());
            cNode.setCode(tblRequirementFeature.getFeatureCode());
            cNode.setRealId(String.valueOf(tblRequirementInfo.getId()));
            cNode.setType("feature");

            //查询开发工作任务
            List<Map<String,Object>> devList= selectDevtask(tblRequirementFeature.getId());
            if(devList!=null && devList.size()>0){
                cNode.setParent(true);
            }else{
                cNode.setParent(false);
            }
            reqZnodes.add(cNode);
            //组装开发工作任务
            for(Map<String,Object> map:devList){
                AssetsZtree ccNode=new AssetsZtree();
                ccNode.setId("devtask"+"_"+map.get("id").toString());
                ccNode.setpId("feature"+"_"+tblRequirementFeature.getId());
                ccNode.setName(map.get("DEV_TASK_NAME").toString());
                ccNode.setCode(map.get("DEV_TASK_CODE").toString());
                ccNode.setRealId(map.get("id").toString());
                ccNode.setType("devtask");
                
                //代码提交记录
                List<Map<String,Object>> list=codeRecord(Long.parseLong(map.get("id").toString()));
                if(list!=null&& list.size()>0){
                    ccNode.setParent(true);
                }else{
                    ccNode.setParent(false);
                }
                reqZnodes.add(ccNode);
                //处理代码提交记录
               addCodeRecord(reqZnodes,list,"devtask"+"_"+map.get("id").toString());



            }

        }


        result.put("reqZnodes",reqZnodes);
        return result;
    }

    /**
     * 
    * @Title: codeRecord
    * @Description: 获取开发工作任务下的代码提交记录
    * @author author
    * @param devTaskId 开发工作任务ID
    * @return
    * @throws
     */
    private List<Map<String,Object>> codeRecord(Long devTaskId){
//       Map<String,Object> map= projectToDevInterface.getCodeFilesByDevTaskId(devTaskId);
//       Map<String,Object> map=getCodeFilesByDevTaskId(devTaskId);
       Map<String,Object> map=getCodeFilesByDevTaskId(devTaskId);
       List<Map<String,Object>> svnList= (List<Map<String, Object>>) map.get("svnFiles");
       List<Map<String,Object>> gitList= (List<Map<String, Object>>) map.get("gitFiles");
       svnList.addAll(gitList);
       return svnList;



    }

   //获取代码提交记录
    private Map<String, Object> getCodeFilesByDevTaskId(Long devTaskId) {
        Map<String, Object> result = new HashMap<>();
        result.put("svnFiles", tblSystemDirectoryDocumentMapper.getSvnFilesByDevTaskId(devTaskId));
        result.put("gitFiles", tblSystemDirectoryDocumentMapper.getGitFilesByDevTaskId(devTaskId));
        return result;
    }
    //将代码提交记录组装进ztree
    private void addCodeRecord(List<AssetsZtree> reqZnodes,List<Map<String,Object>> list,String pid){
        //排序
        Collections.sort(list, new Comparator<Map<String,Object>>() {

            @Override
            public int compare( Map<String,Object> o1, Map<String,Object> o2) {
                return o1.get("CREATE_DATE").toString().compareToIgnoreCase(o2.get("CREATE_DATE").toString());
            }
        });

       for(Map<String,Object> map:list){
           AssetsZtree ccNode=new AssetsZtree();
           ccNode.setId("scm"+"_"+map.get("ID").toString());
           ccNode.setpId(pid);
           ccNode.setName(map.get("COMMIT_FILE").toString());
           ccNode.setParent(false);
           ccNode.setRealId(map.get("ID").toString());
           ccNode.setType("scm");
           reqZnodes.add(ccNode);
       }


    }

    /**
     * 查询文档历史
     * @param result
     * @param documetId 文档id
     * @return
     */
    @Override
    public Map<String, Object> getDocumentHistory(Map<String, Object> result, String documetId) {
        List<Map<String,Object>> lists=tblSystemDirectoryDocumentHistoryMapper.getDocumentHistory(Long.parseLong(documetId));
        //获取关联表

        for(Map<String,Object> map:lists){
            if(map.get("documentVersion")!=null){
                Map<String,Object> param=new HashMap<>();
                param.put("documentVersion",Integer.parseInt(map.get("documentVersion").toString()));
                param.put("systemDirectoryDocumentId",Long.parseLong(map.get("systemDirectoryDocumentId").toString()));
                List<Map<String,Object>> maps= tblSystemDirectoryDocumentHistoryMapper.getRequireIdBydocIdAndVersion(param);
                if(maps!=null && maps.size()>0){
                    map.put("requirementName",maps.get(0).get("requirementName").toString());
                }else{
                    map.put("requirementName","");
                }
            }else{

                map.put("requirementName","");

            }
//            Map<String,Object> param=new HashMap<>();
//
//            List<Map<String,Object>> list=tblSystemDirectoryDocumentHistoryMapper.getRequireIdBydocId(Long.parseLong(map.get("systemDirectoryDocumentId").toString()));
//            String requireName="";
//            if(list!=null && list.size()>0) {
//                requireName = list.get(0).get("requirementNames").toString();
//            }
//            map.put("requirementNames",requireName);
        }

        result.put("docHistoryList",lists);

        return result;
    }
    /**
     * 获取附件
     * @author weiji
     * @param documetId 文档id
     * @return Map<String, Object>
     */

    @Override
    public Map<String, Object> getAttachments(Map<String, Object> result, String documetId) {
        Map<String,Object> param=new HashMap<>();
        param.put("STATUS",1);
        param.put("SYSTEM_DIRECTORY_DOCUMENT_ID",documetId);
        List<TblSystemDirectoryDocumentAttachment> tblSystemDirectoryDocumentAttachments=tblSystemDirectoryDocumentAttachmentMapper.selectByMap(param);
        result.put("attachments",tblSystemDirectoryDocumentAttachments);
        return result;
    }

    /**
     *  获取文档下的附件
     * @param result
     * @param documetId 文档id
     * @param chapterId 章节id
     * @return Map<String, Object>
     */
    @Override
    public Map<String, Object> getAttachmentsByDocumetid(Map<String, Object> result, String documetId,String chapterId) {
        Map<String,Object> param=new HashMap<>();
        param.put("STATUS",1);
        param.put("SYSTEM_DIRECTORY_DOCUMENT_ID",documetId);
        param.put("SYSTEM_DIRECTORY_DOCUMENT_CHAPTERS_ID",chapterId);
        List<TblSystemDirectoryDocumentAttachment> tblSystemDirectoryDocumentAttachments=tblSystemDirectoryDocumentAttachmentMapper.selectByMap(param);
        result.put("attachments",tblSystemDirectoryDocumentAttachments);
        return result;
    }

    /**
     *  获取附件
     * @param result
     * @param chapterId 章节id
     * @return
     */
    @Override
    public Map<String, Object> getAttachmentsByChapter(Map<String, Object> result, String chapterId) {
        Map<String,Object> param=new HashMap<>();
        param.put("STATUS",1);
        param.put("SYSTEM_DIRECTORY_DOCUMENT_CHAPTERS_ID",chapterId);
        List<TblSystemDirectoryDocumentAttachment> tblSystemDirectoryDocumentAttachments=tblSystemDirectoryDocumentAttachmentMapper.selectByMap(param);
        result.put("attachments",tblSystemDirectoryDocumentAttachments);
        return result;
    }

    //通过需求ID获取开发任务
    private List<TblRequirementFeature> selectRequirementFeature(long requireId){
        List<Map<String,Object>> list=new ArrayList<>();
        List<TblRequirementFeature> result=new ArrayList<>();
        list=  tblRequirementFeatureMapper.getFeatureByReId(requireId);
        for(Map<String,Object> map:list){
            TblRequirementFeature tblRequirementFeature=new TblRequirementFeature();
            tblRequirementFeature.setId(Long.parseLong( map.get("ID").toString()));
            tblRequirementFeature.setFeatureName(map.get("FEATURE_NAME").toString());
            tblRequirementFeature.setFeatureCode(map.get("FEATURE_CODE").toString());
            result.add(tblRequirementFeature);
        }
        return result;
    }

    //获取开发工作任务
    private List<Map<String,Object>> selectDevtask(long requireFeatureId){
        List<Map<String,Object>> list=new ArrayList<>();
        list= tblRequirementFeatureMapper.getDevTaskByreqTaskId(requireFeatureId);
        return list;
    }



    private void  recursionDir(long requireId,String parenetId,String ztreePid,List<AssetsZtree> znodes,String key,String systemId) {
        Map<String, Object> param = new HashMap<>();
        param.put("PARENTdownObject_ID",parenetId);
        param.put("PROJECT_TYPE",1);
        param.put("status",1);
        List<TblSystemDirectory> tblSystemDirectories =tblSystemDirectoryMapper.selectByMap(param);
        if(tblSystemDirectories!=null){
            for(TblSystemDirectory tblSystemDirectory:tblSystemDirectories) {
                AssetsZtree cNode=new AssetsZtree();
                cNode.setId(ztreePid+"_"+tblSystemDirectory.getId());
                cNode.setpId(ztreePid);
                cNode.setName(tblSystemDirectory.getDirName());
                cNode.setRealId((String.valueOf(tblSystemDirectory.getId())));
                cNode.setRequireId(String.valueOf(requireId));
                cNode.setDocType(key);
                cNode.setSystemId(systemId);
                if(isExist((String.valueOf(tblSystemDirectory.getId())))){
                    cNode.setParent(true);
                    znodes.add(cNode);
                    recursionDir(requireId,tblSystemDirectory.getId().toString(),ztreePid+"_"+tblSystemDirectory.getId(),znodes,key,systemId);
                }else{
                     cNode.setParent(false);
                     znodes.add(cNode);
                }

            }
        }


    }

    private boolean isExist(String parenetId){
        Map<String, Object> param = new HashMap<>();
        param.put("PARENT_ID",parenetId);
        param.put("PROJECT_TYPE",1);
        param.put("status",1);
        List<TblSystemDirectory>  tblSystemDirectories=tblSystemDirectoryMapper.selectByMap(param);
        if(tblSystemDirectories!=null && tblSystemDirectories.size()>0){
            return true;
        }else{
            return false;
        }

    }


    private boolean isExistXq(){
        Map<String,Object> param=new HashMap<>();
        param.put("status",1);
        param.put("PROJECT_TYPE",1);
        param.put("DIR_NAME","需求说明书");
        param.put("TIER_NUMBER",2);
        List<TblSystemDirectory> tblSystemDirectories=tblSystemDirectoryMapper.selectByMap(param);
        if(tblSystemDirectories!=null && tblSystemDirectories.size()>0){
            return true;
        }else{
            return false;
        }

    }

    /**
     *  获取文档
     * @param result
     * @param requireId 需求id
     * @param documentType 文档类型
     * @param systemId 系统id
     * @return Map<String, Object>
     */
     @Override
    public  Map<String, Object> getDocuments(Map<String, Object> result,long requireId,String documentType,String systemId){
        Map<String, Object> docType= getDocumentType();
        Map<String,Object> param=new HashMap<>();
        param.put("requirementId",requireId);
        //TblRequirementInfo tblRequirementInfo= tblRequirementInfoMapper.selectById(requireId);
         TblRequirementInfo tblRequirementInfo= tblRequirementInfoMapper.findRequirementById1(requireId);
        param.put("documentType",documentType);
         List<TblSystemDirectoryDocument> documents=new ArrayList<>();
        documents= tblSystemDirectoryDocumentMapper.getDocumentsByRequire(param);

        for(TblSystemDirectoryDocument tblSystemDirectoryDocument :documents){
            tblSystemDirectoryDocument.setRequirementCode(tblRequirementInfo.getRequirementCode());
        }
        result.put("documents",documents);
        return result;

    }
//3级目录数据
    public void detailThrid(Map<String, Object> docTypeMap, List<AssetsZtree> znodes,String pld,String requireId){
        for(String key:docTypeMap.keySet()){

            //查询此目录下的数目
            Map<String,Object> param=new HashMap<>();
            param.put("requirementId",requireId);
            param.put("documentType",key);
            List<TblSystemDirectoryDocument> documents=new ArrayList<>();
            documents= tblSystemDirectoryDocumentMapper.getDocumentsByRequire(param);
            if(documents!=null && documents.size()>0){
            AssetsZtree tNode=new AssetsZtree();
            tNode.setId(pld + "_" + key);
            tNode.setpId(pld);
            tNode.setName(docTypeMap.get(key).toString()+"("+documents.size()+")");
            tNode.setRealId(key);
            tNode.setRequireId(requireId);
            tNode.setDocType(key);
            tNode.setLevel("3");
            tNode.setType("4");
//            if(docTypeMap.get(key).toString().equals("需求说明书")){
//                if(isExistXq()){
//                    tNode.setParent(true);
//                    znodes.add(tNode);
//                    //查询该需求下系统
//                    List<Map<String,Object>> list=tblRequirementInfoMapper.getSystemByReqId(Long.parseLong(requireId));
//                    for(Map<String,Object>  map:list){
//                        if(map!=null) {
//                            AssetsZtree systemNode = new AssetsZtree();
//                            systemNode.setId(map.get("id").toString()+"_"+requireId + "_system");
//                            systemNode.setpId(pld + "_" + key);
//                            systemNode.setName(map.get("systemName").toString());
//                            systemNode.setRealId(map.get("id").toString());
//                            systemNode.setRequireId(requireId);
//                            systemNode.setParent(true);
//                            systemNode.setSystemId(map.get("id").toString());
//                            znodes.add(systemNode);
//                            getRequiredir(znodes, requireId,map.get("id").toString()+"_"+requireId + "_system", key, map.get("id").toString());
//                        }
//
//                    }
//                   // getRequiredir(znodes,requireId,pld + "_" + key,key);
//                }else{
//                    tNode.setParent(false);
//                    znodes.add(tNode);
//                }
//
//
//            }else{
                tNode.setParent(false);
                znodes.add(tNode);
           // }
        }else{

    }
    }
    }



   //系统视角3层目录数据
    public void detailThridSystem(Map<String, Object> docTypeMap, List<AssetsZtree> znodes,String pld,String requireId,String systemId){
        for(String key:docTypeMap.keySet()){



            //查询此目录下的数目
            Map<String,Object> param=new HashMap<>();
            param.put("requirementId",requireId);
            param.put("documentType",key);
            List<TblSystemDirectoryDocument> documents=new ArrayList<>();
            documents= tblSystemDirectoryDocumentMapper.getDocumentsByRequire(param);
            if(documents!=null && documents.size()>0){


            AssetsZtree tNode=new AssetsZtree();
            tNode.setId(pld + "_" + key);
            tNode.setpId(pld);
            tNode.setName(docTypeMap.get(key).toString()+"("+documents.size()+")");
            tNode.setRealId(key);
            tNode.setRequireId(requireId);
            tNode.setDocType(key);
            tNode.setLevel("3");
            tNode.setType("4");
            tNode.setSystemId(systemId);
//            if(docTypeMap.get(key).toString().equals("需求说明书")){
//                if(isExistXq()){
//                    tNode.setParent(true);
//                    znodes.add(tNode);
//                    getRequiredir(znodes, requireId,pld + "_" + key, key, systemId);
//
//                }else{
//                    tNode.setParent(false);
//                    znodes.add(tNode);
//                }
//
//
//            }else{
                tNode.setParent(false);
                znodes.add(tNode);
            //}
        }
        }
    }


    private long[] getRequiredIds(long[] requireIds, long[] systemIds, long[] reqTaskIds) {
        int flag=0;
        long[] systemReq=new long[10];
        long[] reqTaskReq=new long[10];
        if(requireIds.length>0){
            flag=flag+1;
        }
        if (systemIds.length > 0) {
            flag=flag+1;
            Map<String, Object> systemMap = new HashMap<>();
            systemMap.put("systemIds", systemIds);
            List<Map<String, Object>> systemReqIds = tblRequirementInfoMapper.getReqidBysystemId(systemMap);
            for (int i = 0; i < systemReqIds.size() - 1; i++) {
                systemReq[i] = Long.parseLong(systemReqIds.get(i).get("REQUIREMENT_ID").toString());
            }

        }
        if (reqTaskIds.length > 0) {
            flag=flag+1;
            Map<String, Object> reqTaskMap = new HashMap<>();
            reqTaskMap.put("reqTaskIds", reqTaskIds);
            List<Map<String, Object>> reqTakReqIds = tblRequirementFeatureMapper.getReqidByreqTaskId(reqTaskMap);
            for (int i = 0; i < reqTakReqIds.size() - 1; i++) {
                reqTaskReq[i] = Long.parseLong(reqTakReqIds.get(i).get("REQUIREMENT_ID").toString());
            }

        }


        return reqTaskIds;


    }

    //将部门信息组装成ztree
    private void addDeptNodes(List<Map<String, Object>> deptlists,List<AssetsZtree> znodes){
        for (Map<String, Object> map : deptlists) {

            String id = map.get("APPLY_DEPT_ID").toString();
            logger.info("======="+id);
            AssetsZtree pNode=new AssetsZtree();
            pNode.setId(id);
            pNode.setpId("0");
            pNode.setName(map.get("DEPT_NAME").toString());
            pNode.setLevel("1");
            pNode.setRealId(id);
            pNode.setParent(true);
            pNode.setType("2");
            znodes.add(pNode);
        }
    }
    
  //将部门视角下的需求信息组装成ztree
    private void addDeptReqNodes(List<Map<String, Object>> requiredirectorylists,List<AssetsZtree> znodes,String znodeId, Map<String, Object> docTypeMap){
        for (Map<String, Object> rqMap : requiredirectorylists) {
            String id=znodeId + "_" + rqMap.get("REQUIREMENT_ID").toString();
            AssetsZtree cNode=new AssetsZtree();
            cNode.setId(id);
            cNode.setpId(znodeId);
            cNode.setName(rqMap.get("REQUIREMENT_NAME").toString());
            cNode.setLevel("2");
            cNode.setRealId(rqMap.get("REQUIREMENT_ID").toString());
            cNode.setParent(true);
            cNode.setType("3");
            znodes.add(cNode);
            detailThrid(docTypeMap,znodes,id,rqMap.get("REQUIREMENT_ID").toString());
        }

    }




  //将系统信息组装成ztree
    private void addSystemNodes(List<Map<String,Object>> systemInfos, List<AssetsZtree> znodes){
        for (Map<String,Object> map : systemInfos) {
            String id = map.get("id").toString();
            AssetsZtree pNode=new AssetsZtree();
            pNode.setId(id);
            pNode.setpId("0");
            pNode.setName(map.get("systemName").toString());
            pNode.setLevel("1");
            pNode.setRealId(id);
            pNode.setType("2");
            pNode.setParent(true);
            znodes.add(pNode);
        }
    }
//将系统下的需求信息组装成ztree
    private void addSystemReqNodes(List<Map<String, Object>> systemReqIds,List<AssetsZtree> znodes,String znodeId, Map<String, Object> docTypeMap){
        for (Map<String, Object> rqMap : systemReqIds) {
            String id=znodeId + "_" + rqMap.get("REQUIREMENT_ID").toString();
            AssetsZtree cNode=new AssetsZtree();
            cNode.setId(id);
            cNode.setpId(znodeId);
            cNode.setName(rqMap.get("REQUIREMENT_NAME").toString());
            cNode.setRealId(rqMap.get("REQUIREMENT_ID").toString());
            cNode.setParent(true);
            cNode.setType("3");
            cNode.setSystemId(znodeId);
            znodes.add(cNode);
            detailThridSystem(docTypeMap,znodes,id,rqMap.get("REQUIREMENT_ID").toString(),znodeId);

        }

    }


    /**
     *  查询关联章节
     * @param result
     * @param chapterId 章节id
     * @return Map<String, Object>
     */

    @Override
    public Map<String, Object> getChapterIdRelation(Map<String, Object> result, String chapterId) {
        Map<String, Object> docTypeMap =getAllDocumentType();
        List<AssetsZtree> znodes = new ArrayList<>();
        List<AssetsZtree> reqZnodes = new ArrayList<>();
        Map<String,Object> param=new HashMap<>();
        //获取需求id
        List<TblRequirementInfo>  requireIds=chaptersRequirementMapper.selectRequirementByChaptersId(Long.parseLong(chapterId),null);
        param.put("requirementId",requireIds.get(0));
        List<TblSystemDirectoryDocument> lists=tblSystemDirectoryDocumentMapper.getDocmentByRequire(param);
        for(TblSystemDirectoryDocument td:lists){
            if(td.getDocumentType()!=null){
                AssetsZtree pNode=new AssetsZtree();
                pNode.setId(String.valueOf(td.getId()));
                pNode.setpId("0");
                pNode.setName(td.getDocumentName());
                pNode.setDocType(docTypeMap.get(td.getDocumentType().toString()).toString());
                pNode.setParent(false);
                pNode.setType("doc");
                reqZnodes.add(pNode);
            }
        }
        //查询此需求单相关信息
        TblRequirementInfo tblRequirementInfo= tblRequirementInfoMapper.findRequirementById(requireIds.get(0).getId());
        AssetsZtree pNode=new AssetsZtree();
        pNode.setId("require"+"_"+tblRequirementInfo.getId());
        pNode.setpId("0");
        pNode.setName(tblRequirementInfo.getRequirementName());
        pNode.setCode(tblRequirementInfo.getRequirementCode());
        pNode.setRealId(String.valueOf(tblRequirementInfo.getId()));
        pNode.setType("require");
        //查询出此需求开发任务
        List<TblRequirementFeature> features=chaptersRequirementFeatureMapper.selectRequirementFeatureByChaptersId(Long.parseLong(chapterId),null);
        if(features!=null && features.size()>0){
            pNode.setParent(true);
        }else{
            pNode.setParent(false);
        }
        reqZnodes.add(pNode);
        for(TblRequirementFeature tblRequirementFeature:features){
            AssetsZtree cNode=new AssetsZtree();
            cNode.setId("feature"+"_"+tblRequirementFeature.getId());
            cNode.setpId("require"+"_"+tblRequirementInfo.getId());
            cNode.setName(tblRequirementFeature.getRequirementName());
            cNode.setCode(tblRequirementFeature.getFeatureCode());
            cNode.setRealId(String.valueOf(tblRequirementInfo.getId()));
            cNode.setType("feature");
            //查询开发工作任务
            List<Map<String,Object>> devList= selectDevtask(tblRequirementFeature.getId());
            if(devList!=null && devList.size()>0){
                cNode.setParent(true);
            }else{
                cNode.setParent(false);
            }
            reqZnodes.add(cNode);
            for(Map<String,Object> map:devList){
                AssetsZtree ccNode=new AssetsZtree();
                ccNode.setId("devtask"+"_"+map.get("id").toString());
                ccNode.setpId("feature"+"_"+tblRequirementFeature.getId());
                ccNode.setName(map.get("DEV_TASK_NAME").toString());
                ccNode.setCode(map.get("DEV_TASK_CODE").toString());
                ccNode.setRealId(map.get("id").toString());
                ccNode.setType("devtask");
                List<Map<String,Object>> list=codeRecord(Long.parseLong(map.get("id").toString()));
                if(list!=null&& list.size()>0){
                    ccNode.setParent(true);
                }else{
                    ccNode.setParent(false);
                }
                reqZnodes.add(ccNode);
                //处理代码提交记录
                addCodeRecord(reqZnodes,list,"devtask"+"_"+map.get("id").toString());
          }

        }


        result.put("reqZnodes",reqZnodes);
        return result;
    }

    /**
     * 
    * @Title: getChapterRelation
    * @Description: 获取章节关联信息
    * @author author
    * @param chapterId 章节ID
    * @return
    * @throws
     */
    @Override
    public Map<String, Object> getChapterRelation(String chapterId) {
        Map<String,Object> result=new HashMap<>();
        //查询此章节id关联信息
       List<Map<String, Object>> maps= tblSystemDirectoryDocumentMapper.getChapterRelation( chapterId);
       List<AssetsZtree>  list=new ArrayList<>();
       for(Map<String, Object> map:maps){
    	   //关联的文档
           String SYSTEM_DIRECTORY_DOCUMENT_ID2=map.get("SYSTEM_DIRECTORY_DOCUMENT_ID2").toString();
           //关联的文档章节
           String SYSTEM_DIRECTORY_DOCUMENT_CHAPTER_ID2=map.get("SYSTEM_DIRECTORY_DOCUMENT_CHAPTER_ID2").toString();
           TblSystemDirectoryDocument tblSystemDirectoryDocument=   tblSystemDirectoryDocumentMapper.selectById(SYSTEM_DIRECTORY_DOCUMENT_ID2);
           AssetsZtree pNode=new AssetsZtree();
           pNode.setId("document"+SYSTEM_DIRECTORY_DOCUMENT_ID2);
           pNode.setName(tblSystemDirectoryDocument.getDocumentName());
           pNode.setpId("0");
           list.add(pNode);
           List<TblSystemDirectoryDocumentChapters> cs=new ArrayList<>();
           for(String cId:SYSTEM_DIRECTORY_DOCUMENT_CHAPTER_ID2.split(",")){
               TblSystemDirectoryDocumentChapters tblSystemDirectoryDocumentChapters=
                      tblSystemDirectoryDocumentChaptersMapper.selectByPrimaryKey(Long.parseLong(cId));
               cs.add(tblSystemDirectoryDocumentChapters);
//               List<String> pareIds=Arrays.asList(tblSystemDirectoryDocumentChapters.getParentIds().split(","));
//               AssetsZtree cNode=new AssetsZtree();
//               cNode.setId(tblSystemDirectoryDocumentChapters.getId()+"");
//               cNode.setName(tblSystemDirectoryDocumentChapters.getChaptersName());
//               cNode.setpId("document"+SYSTEM_DIRECTORY_DOCUMENT_ID2);
//               cNode.setParentIds(tblSystemDirectoryDocumentChapters.getParentIds());
//               list.add(cNode);
           }
          list= detailList(cs,list,"document"+SYSTEM_DIRECTORY_DOCUMENT_ID2);
       }

       result.put("list",list);
       return result;

    }

    //组装文档章节为ztree
  private List<AssetsZtree>  detailList(
          List<TblSystemDirectoryDocumentChapters> cs, List<AssetsZtree> trees, String pid) {

       List<String> list = new ArrayList<>();
       for (TblSystemDirectoryDocumentChapters tblSystemDirectoryDocumentChapters : cs) {
       list.add(tblSystemDirectoryDocumentChapters.getId().toString());
       }

      for (TblSystemDirectoryDocumentChapters tblSystemDirectoryDocumentChapters : cs) {
      AssetsZtree cNode = new AssetsZtree();
      cNode.setId(tblSystemDirectoryDocumentChapters.getId() + "");
      cNode.setName(tblSystemDirectoryDocumentChapters.getChaptersName());
      // 查询出是否有pid

      if (tblSystemDirectoryDocumentChapters.getParentIds() != null) {
        List<String> ids =
            Arrays.asList(tblSystemDirectoryDocumentChapters.getParentIds().split(","));
          List<String> maxid=new ArrayList<>();
          for(String cpid:ids)
          {
              if(list.contains(cpid)){
                  maxid.add(cpid);
              }
          }
          if(maxid.size()>0){
              String id = Collections.max(maxid);
              cNode.setpId(id);
          }else{
              cNode.setpId(pid);
          }

      }else{
          cNode.setpId(pid);

      }

      trees.add(cNode);
    }

    return trees;
        }

}
