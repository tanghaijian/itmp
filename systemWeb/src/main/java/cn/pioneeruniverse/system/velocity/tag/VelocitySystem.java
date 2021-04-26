package cn.pioneeruniverse.system.velocity.tag;

import cn.pioneeruniverse.common.dto.TblDataDicDTO;
import cn.pioneeruniverse.common.utils.SpringContextHolder;
import cn.pioneeruniverse.system.feignInterface.dic.DataDicInterface;
import cn.pioneeruniverse.system.feignInterface.menu.MenuInterface;
import org.apache.commons.lang3.StringUtils;

import java.util.List;
import java.util.Map;

/**
 * 
* @ClassName: VelocitySystem
* @Description: 页面自定义函数$system 具体实现
* @author author
* @date 2020年9月4日 上午10:02:00
*
 */
public class VelocitySystem {

    private static DataDicInterface dataDicInterface = SpringContextHolder.getBean(DataDicInterface.class);

    private static MenuInterface menuInterface = SpringContextHolder.getBean(MenuInterface.class);

    /**
     * 
    * @Title: getDataDicListBytermCode
    * @Description: 通过数据字典编码获取数据字典列表
    * @author author
    * @param termCode 数据字典编码
    * @return List<TblDataDicDTO> 数据字典编码
     */
    public List<TblDataDicDTO> getDataDicListBytermCode(String termCode) {
        return dataDicInterface.getDataDicByTermCode(termCode);
    }

   /**
    * 
   * @Title: getMenuByCode
   * @Description: 通过编码获取菜单
   * @author author
   * @param code 菜单编码
   * @return  TblMenuButtonInfo的map形式
    */
    public Map<String, Object> getMenuByCode(String code) {
        return menuInterface.getMenuByCode(code);
    }

    /**
     * 
    * @Title: getMyMenuList
    * @Description: 获取userId此用户所拥有的菜单
    * @author author
    * @param userId
    * @return List<Map<String,Object>>  ：TblMenuButtonInfo转换成map并以列表返回
     */
    public List<Map<String, Object>> getMyMenuList(String userId) {
        if (StringUtils.isNotEmpty(userId)) {
            return menuInterface.getUserMenu(Long.valueOf(userId));
        } else {
            return null;
        }
    }
}
