package cn.pioneeruniverse.dev.service.task.impl;

import cn.pioneeruniverse.common.utils.CollectionUtil;
import cn.pioneeruniverse.common.utils.RedisUtils;
import cn.pioneeruniverse.dev.dao.mybatis.TblRequirementFeatureDeployStatusMapper;
import cn.pioneeruniverse.dev.dao.mybatis.TblRequirementFeatureMapper;
import cn.pioneeruniverse.dev.entity.DevTaskVo;
import cn.pioneeruniverse.dev.entity.TblRequirementFeatureDeployStatus;
import cn.pioneeruniverse.dev.service.task.DevTaskService;
import cn.pioneeruniverse.dev.vo.task.DevTaskReq;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static cn.pioneeruniverse.common.utils.JsonUtil.GsonToMaps;

/**
 * @author by qingcheng
 * @version 2020/8/17 10:29
 * @description
 */
@Service
public class DevTaskServiceImpl implements DevTaskService {

    private static final Logger log = LoggerFactory.getLogger(DevTaskServiceImpl.class);

    //======================================================
    // 属性
    private RedisUtils redisUtils;
    private TblRequirementFeatureMapper requirementFeatureMapper;
    private TblRequirementFeatureDeployStatusMapper deployStatusMapper;
    private Map<String, String> dataDices;
    private Map<String, String> deployNames;

    //======================================================
    // 方法
    // 初始化方法
    @PostConstruct
    public void inti() {
        // 从redis初始化开发任务状态枚举表
        Object result = this.redisUtils.get("TBL_REQUIREMENT_FEATURE_REQUIREMENT_FEATURE_STATUS");
        if (result != null) {
            this.dataDices = GsonToMaps(result.toString());
        }
        // 部署状态预加载
        result = this.redisUtils.get("TBL_REQUIREMENT_FEATURE_DEPLOY_STATUS");
        if (result != null) {
            this.deployNames = GsonToMaps(result.toString());
        }
    }

    /**
     * 查询投产窗口开发任务
     *
     * @param req 查询参数
     * @return 开发任务列表
     */
    @Override
    public List<DevTaskVo> findDeployReqFeature(DevTaskReq req) {
        List<DevTaskVo> tasks = requirementFeatureMapper.findDeployReqFeature(req);
        Map<Long, String> names = this.findDeployNameByParam(req);
        /*// 状态替换枚举值
        for (DevTaskVo vo: tasks){
            if(StringUtils.isNotBlank(vo.getStatusName())){
                String stateName = dataDices.get(vo.getStatusName());
                vo.setStatusName(stateName == null ? vo.getStatusName() : stateName);
                vo.setDeployName(names.get(vo.getId()));
            }
        }*/
        // 状态替换枚举值 lambda 简写
        tasks.stream()
                .filter(vo -> StringUtils.isNotBlank(vo.getStatusName()))
                .forEach(vo -> {
                    String stateName = dataDices.get(vo.getStatusName());
                    vo.setStatusName(stateName == null ? vo.getStatusName() : stateName);
                    vo.setDeployName(names.get(vo.getId()));
                });
        return tasks;
    }

    /**
     * 查询开发任务状态
     *
     * @param req 查询参数
     */
    @Override
    public Map<Long, String> findDeployNameByParam(DevTaskReq req) {
        List<TblRequirementFeatureDeployStatus> statuses = deployStatusMapper.findByReqParam(req);
        Map<Long, String> map = new HashMap<>();
        if (CollectionUtil.isNotEmpty(statuses)) {
            statuses.forEach(state -> {
                String str = map.get(state.getRequirementFeatureId());
                // 增加分隔符
                str = str == null ? "" : str + ", ";
                // 拼接部署状态
                str += deployNames.get(state.getDeployStatu() + "") + " | " + getTime(state.getDeployTime());
                map.put(state.getRequirementFeatureId(), str);
            });

        }
        return map;
    }

    /**
     * 格式化时间戳
     *
     * @param ts 时间戳
     * @return yyyy/MM/dd HH:mm:ss
     */
    private String getTime(Timestamp ts) {
        String tsStr = "";
        if (ts == null) {
            return tsStr;
        }
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        try {
            tsStr = sdf.format(ts);
        } catch (Exception e) {
            log.warn("日期格式化异常", e);
        }
        return tsStr;
    }

    //======================================================
    // GETTER & SETTER
    @Autowired
    public void setRedisUtils(RedisUtils redisUtils) {
        this.redisUtils = redisUtils;
    }

    @Autowired
    public void setRequirementFeatureMapper(TblRequirementFeatureMapper requirementFeatureMapper) {
        this.requirementFeatureMapper = requirementFeatureMapper;
    }

    @Autowired
    public void setDeployStatusMapper(TblRequirementFeatureDeployStatusMapper deployStatusMapper) {
        this.deployStatusMapper = deployStatusMapper;
    }

}
