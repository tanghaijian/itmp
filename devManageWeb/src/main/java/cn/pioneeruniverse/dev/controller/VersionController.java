package cn.pioneeruniverse.dev.controller;

import cn.pioneeruniverse.common.utils.CommonUtil;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.Map;

/**
 * 
* @ClassName: VersionController
* @Description: 版本管理
* @author author
* @date 2020年8月11日 下午9:24:33
*
 */
@RestController
@RequestMapping("versioncontrol")
public class VersionController {

	/**
	 * 
	* @Title: toSvn
	* @Description: SVN代码库管理
	* @author author
	* @param request
	* @return
	* @throws
	 */
    @RequestMapping(value = "toSvn", method = RequestMethod.GET)
    public ModelAndView toSvn(HttpServletRequest request) {
        ModelAndView view = new ModelAndView();
        view.addObject("currentUserId", CommonUtil.getCurrentUserId(request));
        view.addObject("token", CommonUtil.getToken(request));
        view.setViewName("codeBase/codeBase");
        return view;
    }

    /**
     * 
    * @Title: toGitLab
    * @Description: git代码库管理
    * @author author
    * @param request
    * @return
    * @throws
     */
    @RequestMapping(value = "toGitLab", method = RequestMethod.GET)
    public ModelAndView toGitLab(HttpServletRequest request) {
        ModelAndView view = new ModelAndView();
        view.addObject("token", CommonUtil.getToken(request));
        view.setViewName("codeBase/gitLabCodeBase");
        return view;
    }

    /**
     * 
    * @Title: getUserList
    * @Description: 获取代码库配置的人员列表
    * @author author
    * @param existUsers 页面也存在的用户
    * @param codeBaseType svn or git
    * @param request
    * @return
    * @throws
     */
    @RequestMapping(value = "getUserList", method = RequestMethod.GET)
    public ModelAndView getUserList(String existUsers, String codeBaseType, HttpServletRequest request) {
        ModelAndView view = new ModelAndView();
        view.setViewName("codeBase/codeBaseUser");
        Map currentUser = CommonUtil.getCurrentUser(request);
        if (currentUser != null && !currentUser.isEmpty()) {
            view.addObject("currentUserProjectGroupId", currentUser.get("myProjectGroupId"));
        }
        view.addObject("existUsers", existUsers);
        view.addObject("codeBaseType", codeBaseType);
        return view;
    }

    /**
     * 
    * @Title: getCodeSubmitConfigUser
    * @Description: 代码库人员提交配置选择页面
    * @author author
    * @param existUsers 已经配置的人员
    * @param existUsersName已经配置的人员名字
    * @param request
    * @return
    * @throws UnsupportedEncodingException
    * @throws
     */
    @RequestMapping(value = "getCodeSubmitConfigUser", method = RequestMethod.GET)
    public ModelAndView getCodeSubmitConfigUser(String existUsers, String existUsersName, HttpServletRequest request) throws UnsupportedEncodingException {
        ModelAndView view = new ModelAndView();
        Map currentUser = CommonUtil.getCurrentUser(request);
        if (currentUser != null && !currentUser.isEmpty()) {
            view.addObject("currentUserProjectGroupId", currentUser.get("myProjectGroupId"));
        }
        view.addObject("existUsers", existUsers);
        view.addObject("existUsersName", URLDecoder.decode(existUsersName, "UTF-8"));
        view.setViewName("codeBase/codeSubmitConfigUser");
        return view;
    }

    /**
     * 
    * @Title: createCodeBase
    * @Description: 新建代码库弹窗
    * @author author
    * @param scmType
    * @param systemId
    * @param treeId
    * @return
    * @throws
     */
    @RequestMapping(value = "createCodeBase", method = RequestMethod.GET)
    public ModelAndView createCodeBase(Integer scmType, Long systemId, @RequestParam(value = "treeId", required = false) String treeId) {
        ModelAndView view = new ModelAndView();
        view.addObject("scmType", scmType);
        view.addObject("systemId", systemId);
        view.addObject("treeId", treeId);
        view.setViewName("codeBase/createCodeBase");
        return view;
    }

    /**
     * 
    * @Title: codeSubmitConfig
    * @Description: 代码提交配置 弹窗
    * @author author
    * @param scmType
    * @param systemId
    * @return
    * @throws
     */
    @RequestMapping(value = "codeSubmitConfig", method = RequestMethod.GET)
    public ModelAndView codeSubmitConfig(Integer scmType, Long systemId) {
        ModelAndView view = new ModelAndView();
        view.addObject("scmType", scmType);
        view.addObject("systemId", systemId);
        view.setViewName("codeBase/codeSubmitConfig");
        return view;
    }

    /**
     *
     * @Title: codeSubmitConfig
     * @Description: 代码提交配置 弹窗
     * @author author
     * @param scmType
     * @param systemId
     * @return
     * @throws
     */
    @GetMapping(value = "codeSubmitConfig4Manager")
    public ModelAndView codeSubmitConfig4Manager(Integer scmType, Long systemId) {
        ModelAndView view = new ModelAndView();
        view.addObject("scmType", scmType);
        view.addObject("systemId", systemId);
        view.setViewName("codeBase/codeSubmitConfig4DevManager");
        return view;
    }

}
