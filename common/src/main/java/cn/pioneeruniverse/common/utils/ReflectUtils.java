package cn.pioneeruniverse.common.utils;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
 
import org.springframework.util.StringUtils;

import cn.pioneeruniverse.common.bean.PropertyInfo;
import cn.pioneeruniverse.common.entity.BaseEntity;
 
/**
* 通过反射机制比较两个对象的属性是否修改 
* @author:tingting
* @version:2018年12月18日 下午2:57:25 
*/

public class ReflectUtils {
	/**
	 * Description: 获取修改内容  记录字段的全部属性
	 * source: 修改后的对象
	 * target: 修改前的对象
	 */
	public static String packageModifyAllContent(Object source, Object target) {
		StringBuffer modifyContent = new StringBuffer();
		if(null == source || null == target) {
			return "";
		}
		//取出source类
		Class<?> sourceClass = source.getClass();
		
		Field[] sourceFields = sourceClass.getDeclaredFields();
		for(Field srcField : sourceFields) {
			
			String srcName = srcField.getName();			
			//获取srcField值
			String srcValue = getFieldValue(source, srcName) == null ? "" : getFieldValue(source, srcName).toString();
			//获取对应的targetField值
			String targetValue = getFieldValue(target, srcName) == null ? "" : getFieldValue(target, srcName).toString();
			
			if(StringUtils.isEmpty(srcValue) && StringUtils.isEmpty(targetValue)) {
				continue;
			}
			if(!srcValue.equals(targetValue)) {
				PropertyInfo propertyInfo = srcField.getAnnotation(PropertyInfo.class);
				String name = propertyInfo.name();
				modifyContent.append(name + ":" + targetValue + "——>" + srcValue + ";");
			}
			
		}
		return modifyContent.toString();
	}
	/**
	 * Description: 获取修改内容  记录字段的部分属性（map）
	 * source: 修改后的对象
	 * target: 修改前的对象
	 * 返回值：String
	 */
	public static String packageModifyContent(Object source, Object target, Map<String,String> comparedProperties) {
		StringBuffer modifyContent = new StringBuffer();
		if(null == source || null == target) {
			return "";
		}
		//取出source类
		Class<?> sourceClass = source.getClass();
		
		Field[] sourceFields = sourceClass.getDeclaredFields();
		for(Field srcField : sourceFields) {
			String srcName = srcField.getName();
			//获取此属性值。条件: 1 比较所有属性; 2 比较的属性在比较集合中
			if(null == comparedProperties || (null != comparedProperties && comparedProperties.containsKey(srcName))) {
				
				//获取srcField值
				String srcValue = getFieldValue(source, srcName) == null ? "" : getFieldValue(source, srcName).toString();
				//获取对应的targetField值
				String targetValue = getFieldValue(target, srcName) == null ? "" : getFieldValue(target, srcName).toString();
				if(StringUtils.isEmpty(srcValue) && StringUtils.isEmpty(targetValue)) {
					continue;
				}
				if(!srcValue.equals(targetValue)) {
					PropertyInfo propertyInfo = srcField.getAnnotation(PropertyInfo.class);
					String name = propertyInfo.name();
					//comparedProperties.get(srcName)
					modifyContent.append(name + "：" +"&nbsp;&nbsp;“&nbsp;<b>"+ targetValue +"</b>&nbsp;”&nbsp;&nbsp;" + "修改为" + "&nbsp;&nbsp;“&nbsp;<b>"+srcValue + "</b>&nbsp;”&nbsp;&nbsp;"+"；");
				}
			}
		}
		return modifyContent.toString();
	}
	/**
	 * Description: 获取修改内容  记录字段的部分属性（map）
	 * source: 修改后的对象
	 * target: 修改前的对象
	 * 返回值：Map<String, String>
	 */
	public static Map<String, String> packageModifyContentReMap(Object source, Object target, Map<String,String> comparedProperties) {
		Map<String, String> modifyContent = new HashMap<>();
		if(null == source || null == target) {
			return null;
		}
		//取出source类
		Class<?> sourceClass = source.getClass();
		
		Field[] sourceFields = sourceClass.getDeclaredFields();
		for(Field srcField : sourceFields) {
			String srcName = srcField.getName();
			//获取此属性值。条件: 1 比较所有属性; 2 比较的属性在比较集合中
			if(null == comparedProperties || (null != comparedProperties && comparedProperties.containsKey(srcName))) {
				
				//获取srcField值
				String srcValue = getFieldValue(source, srcName) == null ? "" : getFieldValue(source, srcName).toString();
				//获取对应的targetField值
				String targetValue = getFieldValue(target, srcName) == null ? "" : getFieldValue(target, srcName).toString();
				if(StringUtils.isEmpty(srcValue) && StringUtils.isEmpty(targetValue)) {
					continue;
				}
				if(!srcValue.equals(targetValue)) {
					PropertyInfo propertyInfo = srcField.getAnnotation(PropertyInfo.class);
					String name = propertyInfo.name();
					//comparedProperties.get(srcName)
					String detail = "："+"&nbsp;&nbsp;“&nbsp;<b>"+targetValue+"</b>&nbsp;”&nbsp;&nbsp;" + "修改为" + "&nbsp;&nbsp;“&nbsp;<b>"+srcValue+"</b>&nbsp;”&nbsp;&nbsp;";
					modifyContent.put(name, detail);
				}
			}
		}
		return modifyContent;
	}

	/**
	 * Description: 获取Obj对象的fieldName属性的值
	 */
	private static Object getFieldValue(Object obj, String fieldName) {
		Object fieldValue = null;
		if(null == obj) {
			return null;
		}
		Method[] methods = obj.getClass().getDeclaredMethods();
		for (Method method : methods) {
			String methodName = method.getName();
			if(!methodName.startsWith("get")) {
				continue;
			}
			if(methodName.startsWith("get") && methodName.substring(3).toUpperCase(Locale.ENGLISH).equals(fieldName.toUpperCase(Locale.ENGLISH))) {
                 try {
                	 fieldValue = method.invoke(obj, new Object[] {});
                	 if (fieldValue instanceof Date) {
                		 DateFormat dFormat = new SimpleDateFormat("yyyy-MM-dd");
                		  fieldValue = dFormat.format(fieldValue);
					}
                 } catch (Exception e) {
                	System.out.println("取值出错，方法名 " + methodName);
                    continue;
                 }
			}
		}
		return fieldValue;
	}
	/*public static void main(String[] args) {
		BaseEntity baseEntity = new BaseEntity();
		baseEntity.setCreateBy(1L);
		baseEntity.setDescription("11111111111111");
		baseEntity.setId(2L);
		BaseEntity baseEntity2 = new BaseEntity();
		baseEntity2.setCreateBy(2L);
		baseEntity2.setDescription("21111");
		Map<String, String> map = new HashMap<>();
		map.put("id", "id");
		map.put("test", "test");
		//String string = ReflectUtils.packageModifyContent(baseEntity,baseEntity2,map);
		System.out.println();
		
	}*/
}
