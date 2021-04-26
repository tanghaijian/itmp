package cn.pioneeruniverse.common.constants;


import java.util.HashMap;
import java.util.Map;

public class Constants {

    public static String ITMP_RETURN_SUCCESS = "1"; //controller成功返回

    public static String ITMP_RETURN_FAILURE = "2";//controller失败返回

    public static Long ITMP_TOKEN_TIMEOUT = 6 * 60 * 60L;//秒

    public static String ITMP_TOKEN_HS_KEY = "ITMP_T"; //JWT加密串

    public static String ITMP_USER_PREFIX = "itmp_user_";//未用

    public static String ITMP_DEPT_PREFIX = "DEPT_";//未用

    public static String ITMP_COMPANY_PREFIX = "COMPANY_";//未用

    public static String ITMP_DEVICE_PREFIX = "DVCI_";//未用

    public static String ITMP_ROLE_PREFIX = "ROLE_";//角色前缀，未用

    public static String ITMP_EMP_PREFIX = "EMP_";//员工编号前缀，未用

    public static String ITMP_DEV_TASK_CODE = "DTSK-";//开发任务编号

    public static String ITMP_DEV_TASK_CODE_DATA = "DTSKD-";//同步开发任务编号

    public static String ITMP_WORK_TASK_CODE = "WTSK-";//开发工作任务编号前缀

    public static String ITMP_TESTWORK_TASK_CODE = "W-";//测试工作任务编号前缀

    public static String ITMP_TESTSET_SET_CODE = "TS-";//redis Key 测试集编号前缀

    public static String ITMP_TEST_TASK_CODE = "T-";//测试任务编号

    public static String TMP_TEST_DEFECT_CODE = "B-";// 缺陷编号

    public static String TMP_TEST_REQ_CODE = "REQ-";// 需求编号

    public static String TMP_CASE_INFO_NUMBER = "TC-";// redis Key 案例编号

    public static String TEST_MANAGE_UI_URL = "/testManageui";

    public static String DEV_MANAGE_UI_URL = "/devManageui";

    public static String PROJECT_MANAGE_UI_URL = "/projectManageui";

    public static String SYSTEM_MANAGE_UI_URL = "/systemManageui";
    
    public static String CURRENT_SESSION_USER = "currentSessionUser";

    /**
     * redis角色菜单URL权限key
     */
    public static String ITMP_REDIS_ROLE_MENU_PREFIX = "TBL_ROLE_INFO-";

    /**
     * redis数据字典
     */
    public static String ITMP_REDIS_TBL_DATA_DIC = "tbl_data_dic";

    public static final int MAX_ALLOW_PACKET = 3796000;


    public static final int RET_SUC = 1;
    public static final int RET_ERR = 2;
    public static String DEFAULT_PASS = "e10adc3949ba59abbe56e057f20f883e";
    public static String ITMP_DES_KEY = "itmp";


    /**
     * svn更新状态字符
     */
    public static final String ADD_FILE = "A "; //条目添加到版本库
    public static final String ADD_COPY_FILE = "A+"; //条目复制添加到版本库（TODO 包含文件及文件夹）
    public static final String DEL_FILE = "D "; //条目从版本库删除
    public static final String UPDATE_FILE_CONTENT = "U "; //文件内容改变了
    public static final String UPDATE_FILE_ATTR = "_U"; //条目属性改变了
    public static final String UPDATE_FILE_CONTENT_ATTR = "UU"; //文件内容和属性修改了


    /**
     * 环境常量
     */
    public static final String PRIVATE_DEV = "1";
    public static final String PUBLIC_DEV = "2";
    public static final String PRIVATE_XICE = "3";
    public static final String PUBLIC_XICE = "4";
    public static final String PRIVATE_UAT = "5";
    public static final String PUBLIC_UAT = "6";
    public static final String PRIVATE_BANCE = "7";
    public static final String PUBLIC_BANCE = "8";
    public static final String PRDIN = "9";
    public static final String PRDOUT = "10";

    /**
     * 部署状态
     */

    public static final String DEPLOY_SUCCESS = "2";
    public static final String DEPLOY_FAIL = "3";
    /**
     * 服务类型
     */
    public static final String SERVER_MICRO_TYPE = "1"; //多模块
    public static final String SERVER_TRO_TYPE = "2";//单模块


    /**
     * 创建方式
     */
    public static final String CREATE_TYPE_AUTO = "1";//自动
    public static final String CREATE_TYPE_MANUAL = "2";//手动

    /**
     * 部署方式
     */
    public static final String DEPLOY_TYPE_SCM = "1";//源码部署
    public static final String DEPLOY_TYPE_PACKAGE = "2";//制品部署


    /**
     * 任务类型
     */
    public static final String JOB_TYPE_BUILD = "1";//构建
    public static final String JOB_TYPE_DEPLOY = "2";//部署

    //仓库类型
    public static final Integer SCM_TYPE_SVN = 1;
    public static final Integer SCM_TYPE_GIT = 2;
    //系测 版测
    public static final String XICE = "系测-";
    public static final String BANCE = "版测-";
    
    //消息队列
    public static String SEND_EMAIL_MESSAGE = "SEND_EMAIL_MESSAGE";//邮件
    public static String SEND_WECHAT_MESSAGE = "SEND_WECHAT_MESSAGE";//微信

    public static final Map<Integer, String> ACCESS_PROTOCOL_MAP = new HashMap<Integer, String>() {{
        put(1, "svn://");
        put(2, "git@");
        put(3, "http://");
        put(4, "https://");
    }};

    public interface TaskPlugin {
        String REDIS_SUFFIX = "TASK_PLUGIN_";
        String SUCCESS_CODE = "0000";
        String FAILURE_CODE = "9999";
        String ABNORMAL_CODE = "0009";
    }

    public interface System {
        String INDEX_URL = "/systemui/index";//首页url
        String LOGOUT_URL = "/systemui/logout";//登出url
        String[] DEFAULT_URL = {"/", "/systemui/"};
        String TOKEN_NAME = "itmpToken";
    }

    public interface GitLab {
        String PROJECT_VISIBILITY_PRIVATE = "private";
        String PROJECT_VISIBILITY_INTERNAL = "internal";
        String PROJECT_VISIBILITY_PUBLIC = "public";
        String NO_HOOK_INTERCEPT_COMMIT_ID = "0000000000000000000000000000000000000000";//hook不拦截commit SHA
        String ADD_FILE = "A"; //文件添加到版本库
        String DEL_FILE = "D";//文件从版本库删除
        String MODIFY_FILE = "M";//修改版本库中文件
        String RENAME_FILE = "R";//文件重命名

    }
    
    public interface Jenkins {
        String STAGE_VIEW = "stageView_";//日志频繁查询保存redis
    }

    /**
     * 本系统导出的EXCEL版本
     **/
    public static final String EXPORT_EXCEL_VERSION = ".xlsx";

    /**
     * 本系统导出的EXCEL文件名称时间格式
     **/
    public static final String EXPORT_EXCEL_TIME_FORMAT = "yyyyMMddHHmmss";


}
