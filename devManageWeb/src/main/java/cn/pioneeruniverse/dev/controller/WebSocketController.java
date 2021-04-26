package cn.pioneeruniverse.dev.controller;

import cn.pioneeruniverse.dev.WebSocketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

/**
 * @deprecated 没用
 * @Author: yaojiaxin [yaojiaxin@pioneerservice.cn]
 * @Description:
 * @Date: Created in 18:18 2019/3/5
 * @Modified By:
 */
@RestController
public class WebSocketController {

    @Autowired
    private WebSocketService socketService;

    @Autowired
    private SimpMessagingTemplate template;

    @RequestMapping("websocketTest")
    public ModelAndView toDeptManage() {
        ModelAndView view = new ModelAndView();
        view.setViewName("webSocketTest");
        return view;
    }

    @MessageMapping("hello")
    public void greetings() {
        socketService.AsyncTaskExcute();
        template.convertAndSend("/topic/greetings","异步任务执行开始");
    }

    @RequestMapping(value = "taskExecuteEnd", method = RequestMethod.POST)
    public String taskExecuteEnd() {
        socketService.InterruptThreadByName("longTimeAsyncTask1");
        return "1";
    }

}
