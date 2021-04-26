package cn.pioneeruniverse.dev.dao.mybatis;

import cn.pioneeruniverse.dev.entity.TblRequirementFeatureDeployStatus;
import cn.pioneeruniverse.dev.vo.task.DevTaskReq;
import org.apache.ibatis.annotations.Param;
import java.util.List;

public interface TblRequirementFeatureDeployStatusMapper {

    /**
    * @author author
    * @Description 获取某个开发任务的部署状态
    * @Date 2020/9/24
    * @param featureId
    * @return java.util.List<cn.pioneeruniverse.dev.entity.TblRequirementFeatureDeployStatus>
    **/
    List<TblRequirementFeatureDeployStatus> findByReqFeatureId(Long featureId);

    /**
    * @author author
    * @Description 查询开发任务部署状态
    * @Date 2020/9/24
    * @param req
    * @return java.util.List<cn.pioneeruniverse.dev.entity.TblRequirementFeatureDeployStatus>
    **/
    List<TblRequirementFeatureDeployStatus> findByReqParam(DevTaskReq req);

    TblRequirementFeatureDeployStatus selectByPrimaryKey(Long id);

    TblRequirementFeatureDeployStatus findByReqFeatureIdDeployStatu(@Param(value = "featureId")Long featureId,
                                                                    @Param(value = "deployStatu")Integer deployStatu);

    /**
    * @author author
    * @Description 保存开发任务部署状态
    * @Date 2020/9/24
    * @param featureDeployStatus
    * @return void
    **/
    void insertFeatureDeployStatus(TblRequirementFeatureDeployStatus featureDeployStatus);

    /**
    * @author author
    * @Description 更新开发任务部署状态
    * @Date 2020/9/24
    * @param featureDeployStatus
    * @return void
    **/
    void updateFeatureDeployStatus(TblRequirementFeatureDeployStatus featureDeployStatus);

    /**
    * @author author
    * @Description 逻辑删除开发任务部署状态
    * @Date 2020/9/24
    * @param featureId
    * @return void
    **/
    void deleteByFeatureId(Long featureId);
}