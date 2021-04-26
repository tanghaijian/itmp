package cn.pioneeruniverse.project.feignFallback;

import cn.pioneeruniverse.common.dto.TblCompanyInfoDTO;
import cn.pioneeruniverse.common.dto.TblDeptInfoDTO;
import cn.pioneeruniverse.project.entity.TblDeptInfo;
import cn.pioneeruniverse.project.entity.TblUserInfo;
import cn.pioneeruniverse.project.feignInterface.ProjectManageWebToSystemInterface;
import feign.hystrix.FallbackFactory;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * @Author:
 * @Description: projectManagemWeb模块请求system模块微服务接口熔断处理
 * @Date: Created in 15:11 2020/08/24
 * @Modified By:
 */
@Component
public class ProjectManageWebToSystemFallback implements FallbackFactory<ProjectManageWebToSystemInterface> {
    @Override
    public ProjectManageWebToSystemInterface create(Throwable throwable) {
        return new ProjectManageWebToSystemInterface() {
            @Override
            public List<TblDeptInfoDTO> getAllDeptInfo() {
                return null;
            }
            @Override
            public List<TblCompanyInfoDTO> getAllCompanyInfo() {
                return null;
            }
            @Override
            public List<TblDeptInfo> getDept() {
                return null;
            }
            @Override
            public List<TblUserInfo> getUser() {
                return null;
            }
            @Override
            public Map<String, Object> findUserById(Long userId) {
                return null;
            }
            @Override
            public Map<String, Object> selectDeptById(Long id) {
                return null;
            }
        };
    }
}
