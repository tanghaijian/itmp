package cn.pioneeruniverse.dev;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

/**
 * @Author: yaojiaxin [yaojiaxin@pioneerservice.cn]
 * @Description:
 * @Date: Created in 14:40 2019/3/6
 * @Modified By:
 */
@Service
public class WebSocketService {

    private static final Logger logger = LoggerFactory.getLogger(WebSocketService.class);
    private static final int BuildTaskTimeOut = 1 * 3 * 60 * 1000;//默认任务构建超时时间

    @Autowired
    private SimpMessagingTemplate template;

    @Async("buildTaskAsync")
    public void AsyncTaskExcute(){
        Thread.currentThread().setName("longTimeAsyncTask1");
        logger.info(Thread.currentThread().getName() + "开始异步任务");
         try {
             Thread.currentThread().sleep(BuildTaskTimeOut);
             template.convertAndSend("/topic/greetings","异步任务执行超时");
         }catch (InterruptedException e) {
             logger.info(Thread.currentThread().getName() + "异步任务完成");
             template.convertAndSend("/topic/greetings","异步任务执行完成");
         }

    }


    /**
     * @param name
     * @return void
     * @Description 打断睡眠中的线程
     * @MethodName InterruptThreadByName
     * @author yaojiaxin [yaojiaxin@pioneerservice.cn]
     * @Date 2018/11/2 14:07
     */
    public void InterruptThreadByName(String name) {
        ThreadGroup currentGroup = Thread.currentThread().getThreadGroup();
        int noThreads = currentGroup.activeCount();
        Thread[] lstThreads = new Thread[noThreads];
        currentGroup.enumerate(lstThreads);
        logger.info("现有线程数" + noThreads);
        for (int i = 0; i < noThreads; i++) {
            String nm = lstThreads[i].getName();
            logger.info("线程号：" + i + " = " + nm);
            if (nm.equals(name)) {
                lstThreads[i].interrupt();
            }
        }
    }




}
