package cn.pioneeruniverse.dev.velocity.directive.codeReview;

import cn.pioneeruniverse.common.utils.SpringContextHolder;
import cn.pioneeruniverse.dev.feignInterface.DevManageWebToDevManageInterface;
import org.apache.velocity.context.InternalContextAdapter;
import org.apache.velocity.exception.MethodInvocationException;
import org.apache.velocity.exception.ParseErrorException;
import org.apache.velocity.exception.ResourceNotFoundException;
import org.apache.velocity.runtime.directive.Directive;
import org.apache.velocity.runtime.parser.node.Node;

import java.io.IOException;
import java.io.Writer;
import java.util.Map;

/**
 * @Author: yaojiaxin [yaojiaxin@pioneerservice.cn]
 * @Description:
 * @Date: Created in 11:53 2019/3/20
 * @Modified By:
 */
public class GetCommentsCountDirect extends Directive {

    private static DevManageWebToDevManageInterface devManageWebToDevManageInterface = SpringContextHolder.getBean(DevManageWebToDevManageInterface.class);

    @Override
    public String getName() {
        return "getCommentsCount";
    }

    @Override
    public int getType() {
        return LINE;
    }

    @Override
    public boolean render(InternalContextAdapter internalContextAdapter, Writer writer, Node node) throws IOException, ResourceNotFoundException, ParseErrorException, MethodInvocationException {
        Map<String, Object> result = devManageWebToDevManageInterface.getFileCommentsCountByFileId(Long.valueOf(node.jjtGetChild(0).value(internalContextAdapter).toString()),
                Integer.valueOf(node.jjtGetChild(1).value(internalContextAdapter).toString()));
        if (result != null && !result.isEmpty()) {
            if ((Boolean) result.get("flag")) {
                writer.write(result.get("data") == null ? "" : result.get("data").toString());
            } else {
                writer.write(((Map) result.get("data")).get("message").toString());
            }
        }
        return true;
    }
}
