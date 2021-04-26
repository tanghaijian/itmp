package cn.pioneeruniverse.system.feignInterface.dic;

import java.util.List;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import cn.pioneeruniverse.common.dto.TblDataDicDTO;
import cn.pioneeruniverse.system.feignFallback.dic.DataDicFallback;

@FeignClient(value="system",fallbackFactory=DataDicFallback.class)
public interface DataDicInterface {

    /**
    * @author author
    * @Description 调用数据字典接口：根据编号获取数据字典
    * @Date 2020/9/3
    * @param termCode 数据字典编码
    * @return java.util.List<cn.pioneeruniverse.common.dto.TblDataDicDTO>
    **/
	@RequestMapping(value="dataDic/getDataDicByTermCode",method=RequestMethod.POST)
    List<TblDataDicDTO> getDataDicByTermCode(@RequestParam("termCode") String termCode);

}
