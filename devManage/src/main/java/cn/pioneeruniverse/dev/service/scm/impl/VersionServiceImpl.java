package cn.pioneeruniverse.dev.service.scm.impl;

import cn.pioneeruniverse.common.bean.PageResult;
import cn.pioneeruniverse.common.utils.CollectionUtil;
import cn.pioneeruniverse.dev.dao.mybatis.TblSystemScmSubmitMapper;
import cn.pioneeruniverse.dev.dao.mybatis.TblUserInfoMapper;
import cn.pioneeruniverse.dev.entity.TblSystemScmSubmit;
import cn.pioneeruniverse.dev.entity.TblSystemScmSubmitLog;
import cn.pioneeruniverse.dev.entity.TblUserInfo;
import cn.pioneeruniverse.dev.service.scm.VersionService;
import cn.pioneeruniverse.dev.vo.scm.VersionReq;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author by dyx11
 * @version 2020/8/22 18:40
 * @description
 */
@Service
public class VersionServiceImpl implements VersionService {

    //======================================================
    // 属性
    private final static Logger log = LoggerFactory.getLogger(VersionServiceImpl.class);
    private TblSystemScmSubmitMapper tblSystemScmSubmitMapper;
    private TblUserInfoMapper tblUserInfoMapper;

    //======================================================
    // 方法

    /**
     * 查询资源库配置信息
     *
     * @param systemId 系统id
     * @param scmType  资源库类型
     * @return 配置信息列表
     */
    @Override
    public List<TblSystemScmSubmit> findScmConfig(Long systemId, Integer scmType) {
        List<TblSystemScmSubmit> tblSystemScmSubmitList = tblSystemScmSubmitMapper.getSystemScmSubmits(systemId, scmType);
        Set<String> userIds = new HashSet<>();
        tblSystemScmSubmitList.forEach(scm -> {
            if (CollectionUtil.isNotEmpty(scm.getSubmitUserNamesCollection())) {
                userIds.addAll(scm.getSubmitUserNamesCollection());
            }
            if (CollectionUtil.isNotEmpty(scm.getSubmitSuperUserNamesCollection())) {
                userIds.addAll(scm.getSubmitSuperUserNamesCollection());
            }
        });
        if (CollectionUtil.isEmpty(userIds)) {
            return tblSystemScmSubmitList;
        }

        Map<String, String> userIdsMap = findUserInfo(userIds);

        for (TblSystemScmSubmit tblSystemScmSubmit : tblSystemScmSubmitList) {
            tblSystemScmSubmit.setSubmitUserRealNames(assembleUserNames(userIdsMap, tblSystemScmSubmit.getSubmitUserNamesCollection()));
            tblSystemScmSubmit.setSubmitSuperUserRealNames(assembleUserNames(userIdsMap, tblSystemScmSubmit.getSubmitSuperUserNamesCollection()));
        }
        return tblSystemScmSubmitList;
    }

    /**
     * 查询系统用户
     *
     * @param systemId 系统id
     */
    @Override
    public List<TblUserInfo> findProjectUser(Long systemId, String userName) {
        return tblUserInfoMapper.findBySystemId(systemId, userName + '%');
    }

    /**
     * 查询操作日志
     *
     * @param systemId 系统id
     */
    @Override
    public PageResult<TblSystemScmSubmitLog> findOperationLog(Long systemId, int page) {
        long count = tblSystemScmSubmitMapper.findOperationLogCount(systemId);
        PageResult<TblSystemScmSubmitLog> pageResult = new PageResult<>(10);
        if(count == 0){
            return pageResult;
        }
        pageResult.setTotalCount(count);
        List<TblSystemScmSubmitLog> logs = tblSystemScmSubmitMapper.findOperationLog(systemId, (page - 1) * 10);
        pageResult.setResult(logs);
        Set<String> userIds = new HashSet<>();
        logs.forEach(l -> {
            try {
                userIds.add(l.getOperation());
                if (CollectionUtil.isNotEmpty(l.getNewDataList())) {
                    userIds.addAll(l.getNewDataList());
                }
                if (CollectionUtil.isNotEmpty(l.getOldDataList())) {
                    userIds.addAll(l.getOldDataList());
                }
            } catch (Exception e) {
                log.warn("提交用户为空", e);
            }
        });
        Map<String, String> userIdsMap = findUserInfo(userIds);
        logs.forEach(l -> {
            l.setNewDataName(assembleUserNames(userIdsMap, l.getNewDataList()));
            l.setOldDataName(assembleUserNames(userIdsMap, l.getOldDataList()));
            l.setOperationName(userIdsMap.get(l.getOperation()));
        });
        return pageResult;
    }

    /**
     * 修改提交用户配置
     *
     * @param req 配置列表
     */
    @Override
    @Transactional
    public int updateSubmitUser(List<VersionReq> req, Long operation) {
        int count = 0;
        for (VersionReq vo : req) {
            operationLog(vo, operation);
            count += tblSystemScmSubmitMapper.updateSubmitUser(vo.getId(), vo.getSubmitStatus(), vo.getUserIds());
        }
        return count - req.size();

    }

    private void operationLog(VersionReq vo, Long operation) {
        try {
            String subStatus = vo.getSubmitStatus() == null ? "" : vo.getSubmitStatus() == 1 ? "是" : "否";
            tblSystemScmSubmitMapper.insertOperationLog(vo.getId(), vo.getUserIds(), subStatus, operation, vo.getSystemId());
        } catch (Exception e) {
            log.error("scm配置信息操作日志保存异常", e);
        }
    }

    private Map<String, String> findUserInfo(Set<String> userIds) {
        EntityWrapper<TblUserInfo> wrapper = new EntityWrapper<>();
        wrapper.eq("STATUS", 1);
        wrapper.in("ID", userIds);
        List<TblUserInfo> userList = tblUserInfoMapper.selectList(wrapper);

        Map<String, String> userIdsMap = null;
        if (CollectionUtil.isNotEmpty(userList)) {
            userIdsMap = userList.stream().collect(Collectors.toMap(u -> u.getId().toString(), TblUserInfo::getUserName));
        }
        return userIdsMap;
    }

    private String assembleUserNames(Map<String, String> userIdsMap, Collection<String> nameTemp) {
        if (CollectionUtil.isEmpty(nameTemp)) {
            return null;
        }
        StringBuilder sb = new StringBuilder();
        nameTemp.forEach(id -> {
            if (userIdsMap.containsKey(id)) {
                sb.append(userIdsMap.get(id)).append(",");
            }
        });
        if (sb.length() > 0) {
            sb.setLength(sb.length() - 1);
        }
        return sb.toString();
    }

    //======================================================
    // GETTER & SETTER
    @Autowired
    public void setTblSystemScmSubmitMapper(TblSystemScmSubmitMapper tblSystemScmSubmitMapper) {
        this.tblSystemScmSubmitMapper = tblSystemScmSubmitMapper;
    }

    @Autowired
    public void setTblUserInfoMapper(TblUserInfoMapper tblUserInfoMapper) {
        this.tblUserInfoMapper = tblUserInfoMapper;
    }

}
