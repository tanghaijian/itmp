package cn.pioneeruniverse.dev.feignInterface.worktask;

import org.springframework.cloud.netflix.feign.FeignClient;

import cn.pioneeruniverse.dev.feignFallback.worktask.WorktaskFallback;


/**
* @author author
* @Description 非使用文件
* @Date 2020/9/4
* @return
**/
@FeignClient(value="devManage",fallbackFactory=WorktaskFallback.class)
public interface WorktaskInterface {

}
