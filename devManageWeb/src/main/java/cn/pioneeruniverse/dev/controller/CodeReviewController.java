package cn.pioneeruniverse.dev.controller;

import cn.pioneeruniverse.common.constants.Constants;
import cn.pioneeruniverse.common.utils.CommonUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;

/**
 * 
* @ClassName: CodeReviewController
* @Description: 代码评审页面跳转Controller
* @author author
* @date 2020年8月11日 下午8:42:46
*
 */
@RestController
@RequestMapping("codeReviewControl")
public class CodeReviewController {

    private final static Logger logger = LoggerFactory.getLogger(CodeReviewController.class);

    /**
     * 
    * @Title: toCodeReViewManage
    * @Description: 首页
    * @author author
    * @param request
    * @param devTaskCode 开发工作任务编码
    * @return
    * @throws
     */
    @RequestMapping(value = "toCodeReViewManage", method = RequestMethod.GET)
    public ModelAndView toCodeReViewManage(HttpServletRequest request,@RequestParam(value = "devTaskCode", required = false) String devTaskCode) {
        ModelAndView view = new ModelAndView();
        view.addObject("currentUserId", CommonUtil.getCurrentUserId(request));
        view.addObject("devTaskCode", devTaskCode);
        view.addObject("token", CommonUtil.getToken(request));
        view.setViewName("codeReview/codeReviewManage");
        return view;
    }

    /**
     * 
    * @Title: toReviewPage
    * @Description: 评审详情页
    * @author author
    * @param devTaskScmId 开发工作任务源码管理ID
    * @param devTaskId 开发工作任务ID
    * @param scmType 仓库类型（1=SVN；2=GIT）
    * @param codeReviewOrViewResult
    * @return
    * @throws
     */
    @RequestMapping(value = "toReviewPage", method = RequestMethod.GET)
    public ModelAndView toReviewPage(@RequestParam(value = "devTaskScmId", required = false) Long devTaskScmId, @RequestParam("devTaskId") Long devTaskId,
                                     @RequestParam(value = "scmType", required = false) Integer scmType, @RequestParam("codeReviewOrViewResult") Integer codeReviewOrViewResult) {
        ModelAndView view = new ModelAndView();
        view.addObject("devTaskId", devTaskId);
        view.addObject("devTaskScmId", devTaskScmId);
        view.addObject("scmType", scmType);
        view.addObject("codeReviewOrViewResult", codeReviewOrViewResult);//(1.代码评审，2.查看评审结果)
        view.addObject("ADD_FILE", Constants.ADD_FILE);
        view.addObject("DEL_FILE", Constants.DEL_FILE);
        view.addObject("UPDATE_FILE_CONTENT", Constants.UPDATE_FILE_CONTENT);
        view.addObject("UPDATE_FILE_ATTR", Constants.UPDATE_FILE_ATTR);
        view.addObject("UPDATE_FILE_CONTENT_ATTR", Constants.UPDATE_FILE_CONTENT_ATTR);
        view.addObject("GIT_ADD_FILE", Constants.GitLab.ADD_FILE);
        view.addObject("GIT_DEL_FILE", Constants.GitLab.DEL_FILE);
        view.addObject("GIT_MODIFY_FILE", Constants.GitLab.MODIFY_FILE);
        view.addObject("GIT_RENAME_FILE", Constants.GitLab.RENAME_FILE);
        view.setViewName("codeReview/reviewPage");
        return view;
    }

    /**
     * 
    * @Title: getDevUser
    * @Description: 人员弹框
    * @author author
    * @param existUsers：已经选择的人员
    * @return
    * @throws
     */
    @RequestMapping(value = "getDevUser", method = RequestMethod.GET)
    public ModelAndView getDevUser(String existUsers) {
        ModelAndView view = new ModelAndView();
        view.addObject("existUsers", existUsers);
        view.setViewName("codeReview/devUser");
        return view;
    }


}
