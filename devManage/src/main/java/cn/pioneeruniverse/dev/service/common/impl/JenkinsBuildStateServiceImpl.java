package cn.pioneeruniverse.dev.service.common.impl;

import cn.pioneeruniverse.common.utils.CollectionUtil;
import cn.pioneeruniverse.dev.dao.mybatis.TblSystemJenkinsMapper;
import cn.pioneeruniverse.dev.dao.mybatis.TblToolInfoMapper;
import cn.pioneeruniverse.dev.entity.TblSystemJenkins;
import cn.pioneeruniverse.dev.entity.TblToolInfo;
import cn.pioneeruniverse.dev.service.build.IJenkinsBuildService;
import cn.pioneeruniverse.dev.service.common.JenkinsBuildStateService;
import cn.pioneeruniverse.dev.vo.common.request.JenkinsJobBuildStateQuery;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Component
public class JenkinsBuildStateServiceImpl implements JenkinsBuildStateService {

    @Autowired
    TblSystemJenkinsMapper tblSystemJenkinsMapper;

    @Autowired
    TblToolInfoMapper tblToolInfoMapper;

    @Autowired
    IJenkinsBuildService iJenkinsBuildService;

    @Override
    public boolean isBuildding(JenkinsJobBuildStateQuery query) {
        List<TblSystemJenkins> tblSystemJenkinsList = tblSystemJenkinsMapper.findByBuildStatusQuery(query);
        if(CollectionUtil.isNotEmpty(tblSystemJenkinsList)){

            //定时表达式校验
            tblSystemJenkinsList = tblSystemJenkinsList.stream()
                    .filter(tblSystemJenkins -> {
                        if(StringUtils.isBlank(tblSystemJenkins.getJobCron())){
                            return false;
                        }
                        return tblSystemJenkins.getJobCron().length() > 8;
                    }).collect(Collectors.toList());

            if(CollectionUtil.isEmpty(tblSystemJenkinsList)){
                return false;
            }

            TblSystemJenkins tblSystemJenkins = tblSystemJenkinsList.get(0);

            TblToolInfo jenkinsTool = tblToolInfoMapper.selectByPrimaryKey(tblSystemJenkins.getToolId());
            try {
                return iJenkinsBuildService.isJenkinsBuilding(jenkinsTool,"/",tblSystemJenkins.getCronJobName());
            } catch (Exception e) {
                log.error(e.getMessage(),e);
            }
        }
        // 防止jenkins查询接口异常导致无法构建/部署
        return false;
    }

}
