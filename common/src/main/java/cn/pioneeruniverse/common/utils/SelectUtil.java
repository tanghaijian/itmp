package cn.pioneeruniverse.common.utils;

import java.util.List;

/**
 * 部门工具类
 * 
 * @author Dell
 *
 */
public class SelectUtil {
 	public static String SESSION_DEPT_SELECT="deptSelect";
   	public static String SESSION_COMPANYINFO_SELECT="CompanyInfoSelect";
   	public static String SESSION_TBLGROUPINFO_SELECT="GroupInfoSelect";
 	public static String SESSION_CHECK_PERIOD_SELECT="takeStockPeriodSelect";
	public static String SESSION_DEVICE_FUND_SOURCE_SELECT="fundSourceSelect";
	public static String SESSION_DIC_LINEN_LEVEL_SELECT="linenLevelSelect";
	public static String SESSION_TBLLINENTYPE_SELECT="linenTypeSelect";
	public static String SESSION_TBLLINENSPEC_SELECT="linenSpecSelect";
	public static String SESSION_DIC_TBL_LEASE_ORDER_ORDER_STATUS_SELECT="leaseOrderSelect";
	public static String SESSION_DIC_TBL_WASH_ORDER_ORDER_STATUS_SELECT="washOrderSelect";
	
	public static String SESSION_DIC_HOTEL_LINEN_WAREHOUSE_STATUS_SELECT="HotelWarehouseSelect";
	public static String SESSION_DIC_TBL_MAINTAIN_ORDER_STATUS_SELECT="MaintainOrderSelect";
	
	public static String SESSION_DIC_TBL_WASH_PURCHASE_PURCHASE_STATUS_SELECT="washPurchaseSelect";
	public static String SESSION_DIC_TBL_LOGISTICS_ORDER_ORDER_STATUS_SELECT="logisticsOrderSelect";
	public static String SESSION_DIC_TBL_LINEN_RFID_PURCHASE_TYPE_SELECT="purchaseTypeSelect";
	public static String SESSION_DIC_TBL_LINEN_RFID_PURCHASE_FROM_SELECT="purchaseFromSelect";
	public static String SESSION_DIC_LINEN_POSITION="linenPositionSelect";
	public static String SESSION_DIC_LINEN_STATUS="linenStatusSelect";
	
	 
	//通用getSelectOptions
	public static String getSelectOptions(List<SelectBean> list, String selectedId, boolean needPleaseOption) {
		StringBuffer bf = new StringBuffer();
		if(needPleaseOption){
			bf.append(NEED_PLEASE_OPTION);
		}
		boolean flag=false;
		if(list!=null&&list.size()>0){
			String[] ids=selectedId!=null?selectedId.split(","):null;
			for (SelectBean bean : list) {
				flag=false;
				if(selectedId!=null&&selectedId.trim().length()>0){
					if(ids!=null&&ids.length==1&&bean.getId().equals(selectedId.trim())){
						flag=true;
						bf.append(getOptionSelect(bean.getId(), bean.getName()));
					}else if(ids!=null&&ids.length>1){
 						for (String id : ids) {
							if(id!=null&&bean.getId().equals(id.trim())){
								bf.append(getOptionSelect(bean.getId(), bean.getName()));
								flag=true;
								break;
							}
						}
					}
					if(flag!=true){
						bf.append(getOption(bean.getId(), bean.getName()));
					}
  				}else{
					bf.append(getOption(bean.getId(), bean.getName()));
				}
			}
		}
		return bf.toString();
	}
	
	
	
	public static String NEED_PLEASE_OPTION = "<option value =''>请选择</option>";

	public static String getOption(String id, String name) {
		return "<option value='" + id.trim() + "'>" + name + "</option>";
	}

	public static String getOptionSelect(String id, String name) {
		return "<option value='" + id.trim()+ "'  "+SELECTED_STR+">" + name + "</option>";
	}
	public static String SELECTED_STR=" selected = 'selected'";
	public static String geSelectVlaue(String id) {
		return "value='" + id.trim()+ "'";
	}
}
