package cn.pioneeruniverse.system.service.dataDic.impl;

import java.sql.Timestamp;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Maps;

import cn.pioneeruniverse.common.annotion.DataSource;
import cn.pioneeruniverse.common.dto.TblDataDicDTO;
import cn.pioneeruniverse.common.entity.JqGridPage;
import cn.pioneeruniverse.common.utils.CollectionUtil;
import cn.pioneeruniverse.common.utils.CommonUtil;
import cn.pioneeruniverse.common.utils.JsonUtil;
import cn.pioneeruniverse.common.utils.RedisUtils;
import cn.pioneeruniverse.common.utils.SpringContextHolder;
import cn.pioneeruniverse.system.dao.mybatis.dataDic.DataDicDao;
import cn.pioneeruniverse.system.entity.TblDataDic;
import cn.pioneeruniverse.system.service.dataDic.IDataDicService;

/**
 * 
* @ClassName: DataDicServiceImpl
* @Description: 数据字典操作service，任何更新操作数据字典都需要刷新redis缓存
* @author author
* @date 2020年8月4日 下午10:26:41
*
 */
@Service("iDataDicService")
public class DataDicServiceImpl extends ServiceImpl<DataDicDao, TblDataDic> implements IDataDicService {

    @Autowired
    private DataDicDao dataDicDao;

    @Autowired
    private RedisUtils redisUtils;


    /**
     * 
    * @Title: getDataDicByTerm
    * @Description: 通过编码获取数据字典
    * @author author
    * @param termCode 数据字典编码
    * @return List<TblDataDic>数据字典列表
    * @throws
     */
    @Override
    @Transactional(readOnly = true)
    public List<TblDataDic> getDataDicByTerm(String termCode) {
        return dataDicDao.getDataDicByTerm(termCode);
    }


    /**
     * 
    * @Title: selectList
    * @Description: 通过查询条件获取数据字典
    * @author author
    * @param dic 搜索条件
    * @return List<TblDataDic>数据字典列表
    * @throws
     */
    @Override
    @Transactional(readOnly = true)
    public List<TblDataDic> selectList(TblDataDic dic) {
        return dataDicDao.selectList(new EntityWrapper<TblDataDic>(dic).eq("STATUS", 1).orderBy("TERM_CODE").orderBy("VALUE_SEQ"));
    }


    /**
     * 
    * @Title: getDataDictPage
    * @Description: 分页查询数据字典
    * @author author
    * @param jqGridPage 封装的JqGrid分页
    * @param tblDataDicDTO 封装的查询条件
    * @return JqGridPage<TblDataDicDTO>封装成jqgrid的数据字典
    * @throws Exception
    * @throws
     */
    @Override
    @Transactional(readOnly = true)
    public JqGridPage<TblDataDicDTO> getDataDictPage(JqGridPage<TblDataDicDTO> jqGridPage, TblDataDicDTO tblDataDicDTO) throws Exception {
        jqGridPage.filtersAttrToEntityField(tblDataDicDTO);
        PageHelper.startPage(jqGridPage.getJqGridPrmNames().getPage(), jqGridPage.getJqGridPrmNames().getRows());
        List<TblDataDicDTO> list = dataDicDao.selectDataDicPage(tblDataDicDTO);
        PageInfo<TblDataDicDTO> pageInfo = new PageInfo<>(list);
        jqGridPage.processDataForResponse(pageInfo);
        return jqGridPage;
    }

    /**
     * 
    * @Title: saveDataDictSubmit
    * @Description: 保存数据字典，由于数据字典存在于开发库和测试库中，因此需要在此切换数据源同时操作。
    * @author author
    * @param dataDics 需要保存的数据字典列表：[{"id":"383","termName":"任务类型","termCode":"JOB_TYPE","valueName":"构建","valueSeq":"1","valueCode":"1","status":"1"},{"id":"458","termName":"任务类型","termCode":"JOB_TYPE","valueName":"构建","valueSeq":"1","valueCode":"1","status":"1"},{"id":"384","termName":"任务类型","termCode":"JOB_TYPE","valueName":"部署","valueSeq":"2","valueCode":"2","status":"1"}]
    * @param dataDicMapForRedis 需要保存到redis的数据字典：{"JOB_TYPE":{"1":"构建","2":"部署"}}
    * @param request
    * @throws
     */
    @Override
    @DataSource(name = "itmpDataSource")
    @Transactional(readOnly = false, rollbackFor = Exception.class,propagation = Propagation.REQUIRED)
    public void saveDataDictSubmit(String dataDics,/* String delDataDicts,*/ String dataDicMapForRedis, HttpServletRequest request) {
        List<TblDataDic> dataDicList = JsonUtil.fromJson(dataDics, JsonUtil.createCollectionType(List.class, TblDataDic.class));
//        List<Long> delDataDicList = JsonUtil.fromJson(delDataDicts, JsonUtil.createCollectionType(List.class, Long.class));
        Map<String, Map<String, String>> dataDicForRedis = JsonUtil.fromJson(dataDicMapForRedis, Map.class);
        if (CollectionUtil.isNotEmpty(dataDicList)) {
            for (TblDataDic tblDataDic : dataDicList) {
                tblDataDic.preInsertOrUpdate(request);
                if (tblDataDic.getId() != null) {
                    //修改
                    dataDicDao.updateDataDict(tblDataDic);
                } else {
                    //新增
                    dataDicDao.addDataDict(tblDataDic);
                }
            }
        }
//        for (Long id : delDataDicList) {
//            //删除
//            TblDataDic tblDataDic = new TblDataDic();
//            tblDataDic.setId(id);
//            tblDataDic.preInsertOrUpdate(request);
//            tblDataDic.setStatus(tblDataDic.DEL_FLAG_DELETE);
//            dataDicDao.delDataDict(tblDataDic);
//        }
        Collections.sort(dataDicList);
        //同步更新redis
        /*for (Map.Entry<String, Map<String, String>> entry : dataDicForRedis.entrySet()) {
            if (entry.getValue().isEmpty()) {
                redisUtils.remove(entry.getKey());
            } else {
                redisUtils.set(entry.getKey(), JsonUtil.toJson(entry.getValue()));
            }
        }*/
//        Map<String,String> map = new HashMap<>();
        Map<String, String> map = Maps.newLinkedHashMap();
        if (CollectionUtil.isNotEmpty(dataDicList)) {
        	for (TblDataDic tblDataDic : dataDicList) {
        		if(tblDataDic.getStatus()!=2 && tblDataDic.getValueName()!=null && tblDataDic.getValueCode()!=null && tblDataDic.getValueSeq()!=null) {
        			map.put(tblDataDic.getValueCode(), tblDataDic.getValueName());
        		}
        	}
		}
        if(map==null || map.size()==0) {
        	 redisUtils.remove(dataDicList.get(0).getTermCode());
        }else {
        	redisUtils.set(dataDicList.get(0).getTermCode(), JSON.toJSONString(map));
        }
        //保存进测试库 注意AOP中调用另外一个AOP的方式
        SpringContextHolder.getBean(IDataDicService.class).saveDataDict(dataDics,dataDicMapForRedis,request);
    }
    
    
    /**
     * 
    * @Title: saveDataDict
    * @Description: 测试库更新数据字典，注意事务Propagation.REQUIRES_NEW
    * @author author
    * @param dataDics 需要保存的数据字典列表：[{"id":"383","termName":"任务类型","termCode":"JOB_TYPE","valueName":"构建","valueSeq":"1","valueCode":"1","status":"1"},{"id":"458","termName":"任务类型","termCode":"JOB_TYPE","valueName":"构建","valueSeq":"1","valueCode":"1","status":"1"},{"id":"384","termName":"任务类型","termCode":"JOB_TYPE","valueName":"部署","valueSeq":"2","valueCode":"2","status":"1"}]
    * @param dataDicMapForRedis 需要保存到redis的数据字典：{"JOB_TYPE":{"1":"构建","2":"部署"}}
    * @param request
    * @throws
     */
    @Override
    @DataSource(name = "tmpDataSource")
	@Transactional(readOnly=false,rollbackFor=Exception.class,propagation=Propagation.REQUIRES_NEW)
	public void saveDataDict(String dataDics, String dataDicMapForRedis, HttpServletRequest request) {
		// TODO Auto-generated method stub
    	List<TblDataDic> dataDicList = JsonUtil.fromJson(dataDics, JsonUtil.createCollectionType(List.class, TblDataDic.class));
//      List<Long> delDataDicList = JsonUtil.fromJson(delDataDicts, JsonUtil.createCollectionType(List.class, Long.class));
      Map<String, Map<String, String>> dataDicForRedis = JsonUtil.fromJson(dataDicMapForRedis, Map.class);
      if (CollectionUtil.isNotEmpty(dataDicList)) {
          for (TblDataDic tblDataDic : dataDicList) {
              tblDataDic.preInsertOrUpdate(request);
              if (tblDataDic.getId() != null) {
                  //修改
                  dataDicDao.updateDataDict(tblDataDic);
              } else {
                  //新增
                  dataDicDao.addDataDict(tblDataDic);
              }
          }
      }
	}

    /**
     * 
    * @Title: updateDataDictStatus
    * @Description: 更新数据字典状态，注意同时更新两个库。
    * @author author
    * @param tblDataDic 数据字典信息
    * @param request
    * @throws
     */
    @Override
    @DataSource(name = "itmpDataSource")
    @Transactional(readOnly = false, rollbackFor = Exception.class,propagation = Propagation.REQUIRED)
    public void updateDataDictStatus(TblDataDic tblDataDic, HttpServletRequest request) {
        tblDataDic.setLastUpdateBy(CommonUtil.getCurrentUserId(request));
        tblDataDic.setLastUpdateDate(new Timestamp(new Date().getTime()));
        dataDicDao.updateStatusByTermCode(tblDataDic);
        if (tblDataDic.getStatus().equals(tblDataDic.DEL_FLAG_NORMAL)) {
            //设为有效
            Map<String, String> map = new HashMap<>();
            for (TblDataDic tdd : dataDicDao.getDataDicByTerm(tblDataDic.getTermCode())) {
                map.put(tdd.getValueCode(), tdd.getValueName());
            }
            redisUtils.set(tblDataDic.getTermCode(), JsonUtil.toJson(map));
        } else if (tblDataDic.getStatus().equals(tblDataDic.DEL_FLAG_DELETE)) {
            //设为无效
            redisUtils.remove(tblDataDic.getTermCode());
        }
        //更新测试库，注意调用方式
        SpringContextHolder.getBean(IDataDicService.class).updateStatus(tblDataDic,request);
    }


    /**
     * 
    * @Title: updateStatus
    * @Description: 更新测试库中的数据字典，注意事务Propagation.REQUIRES_NEW
    * @author author
    * @param tblDataDic 数据字典信息
    * @param request
    * @throws
     */
	@Override
	@DataSource(name = "tmpDataSource")
	@Transactional(readOnly=false,rollbackFor=Exception.class,propagation=Propagation.REQUIRES_NEW)
	public void updateStatus(TblDataDic tblDataDic, HttpServletRequest request) {
		// TODO Auto-generated method stub
		tblDataDic.setLastUpdateBy(CommonUtil.getCurrentUserId(request));
        tblDataDic.setLastUpdateDate(new Timestamp(new Date().getTime()));
        dataDicDao.updateStatusByTermCode(tblDataDic);
        if (tblDataDic.getStatus().equals(tblDataDic.DEL_FLAG_NORMAL)) {
            //设为有效
            Map<String, String> map = new HashMap<>();
            for (TblDataDic tdd : dataDicDao.getDataDicByTerm(tblDataDic.getTermCode())) {
                map.put(tdd.getValueCode(), tdd.getValueName());
            }
            //更新缓存
            redisUtils.set(tblDataDic.getTermCode(), JsonUtil.toJson(map));
        } else if (tblDataDic.getStatus().equals(tblDataDic.DEL_FLAG_DELETE)) {
            //设为无效
            redisUtils.remove(tblDataDic.getTermCode());
        }
	}


	

}
