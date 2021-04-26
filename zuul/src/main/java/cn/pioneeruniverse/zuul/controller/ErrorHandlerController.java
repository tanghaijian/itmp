package cn.pioneeruniverse.zuul.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.web.ErrorController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.netflix.zuul.exception.ZuulException;

@RestController
public class ErrorHandlerController implements ErrorController {

    Logger log = LoggerFactory.getLogger(ErrorHandlerController.class);

    /**
     * 出异常后进入该方法，交由下面的方法处理
     */
    @Override
    public String getErrorPath() {
        return "/error";
    }

    @RequestMapping("/error")
    public String error(HttpServletRequest request) {
        log.info("ErrorHandlerController.error");
        //接受SendErrorFilter的传过来的请求参数
        Integer statusCode = (Integer) request.getAttribute("javax.servlet.error.status_code");
        ZuulException exception = (ZuulException) request.getAttribute("javax.servlet.error.exception");
        String message = (String) request.getAttribute("javax.servlet.error.message");
        switch (statusCode) {
            case HttpServletResponse.SC_BAD_REQUEST:
                return getErrorMessage(statusCode, "BAD_REQUEST", "无效请求!");
            case HttpServletResponse.SC_NOT_FOUND:
                return getErrorMessage(statusCode, "Page Not Found", "你访问的页面不存在!");
            case HttpServletResponse.SC_INTERNAL_SERVER_ERROR:
                return getErrorMessage(statusCode, "Internal_Server_Error", "系统内部错误!");
            default:
                return getErrorMessage(statusCode, exception.errorCause, message);

        }
    }

    private String getErrorMessage(int statusCode, String statusKey, String statusValue) {
        String msg = "<div class=\"page-content\">\n" +
                "	<div class=\"row\">\n" +
                "		<div class=\"col-xs-12\">\n" +
                "			 <div class=\"error-container\">\n" +
                "				  <div class=\"well\">\n" +
                "						<h1 class=\"grey lighter smaller\" style=\"color:#4682B4\">\n" +
                "							<span class=\"blue bigger-125\">\n" +
                "								<i class=\"icon-sitemap\"></i>\n" +
                "								" + statusCode + "\n" +
                "							</span>\n" +
                "							" + statusKey + "\n" +
                "						</h1>\n" +
                "						<hr />\n" +
                "						<h3 class=\"lighter smaller\" style=\"color:#B0C4DE\">" + statusValue + "</h3>\n" +
                "						<h4 class=\"lighter smaller\" style=\"color:#B0C4DE\">请联系管理员!</h4>\n" +
                "				</div>\n" +
                "			</div><!-- PAGE CONTENT ENDS -->\n" +
                "		</div><!-- /.col -->\n" +
                "	</div><!-- /.row -->\n" +
                "</div><!-- /.page-content -->";
        return msg;
    }

}


