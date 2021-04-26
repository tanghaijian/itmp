package cn.pioneeruniverse.common.utils;

import cn.pioneeruniverse.common.bean.PropertyInfo;
import cn.pioneeruniverse.common.bean.TblRoleVo;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Description: 普通共同工具类
 * Author:liushan
 * Date: 2019/4/2 下午 12:33
 */
@Component()
public class CommonUtils {

    @Autowired
    private RedisUtils redisUtils;

    public static CommonUtils commonUtils;

    // 初始化
    @PostConstruct
    public void init() {
        commonUtils = this;
        commonUtils.redisUtils = this.redisUtils;
    }


    /**
     *  判断当前人角色是否为系统管理员
     * @param request
     * @return false 不是系统管理员；true 是系统管理员
     */
    public Boolean currentUserWithAdmin(HttpServletRequest request){
        Boolean flag = false;
        Object adminRole = commonUtils.redisUtils.get("adminRole");
        Map currentUser = (Map) commonUtils.redisUtils.get(CommonUtil.getToken(request));
        if(currentUser.containsKey("roles")){
            Object currentUserRoles = currentUser.get("roles");
            if(currentUserRoles != null && adminRole != null && !currentUserRoles.toString().equals("") && !adminRole.toString().equals("")){
                Gson gson = new Gson();
                TblRoleVo roleVo = gson.fromJson(gson.toJson(adminRole), TblRoleVo.class);
                List<String> roles = gson.fromJson(gson.toJson(currentUserRoles),new TypeToken<List<String>>(){}.getType());
                if(roles != null && roles.size() != 0){
                    flag = roles.contains(roleVo.getRoleCode());
                }
            }
        }
        return flag;
    }

    /**
     *  反射实体类属性，进行属性值比较，判断是否修改，
     * @param oldObject 旧数据
     * @param newObject 新数据
     * @param specificInformation 特殊的数据，无法反射得到，自己按格式封装
     * @return 返回json格式数据fieldName：字段名，oldValue：旧数据，newValue：新数据
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws NoSuchFieldException
     * @throws SecurityException
     * @throws ClassNotFoundException
     */
    public static  String updateFieledsReflect(Object oldObject, Object newObject, Map<String, Object> specificInformation) throws IllegalArgumentException, IllegalAccessException, NoSuchFieldException, SecurityException, ClassNotFoundException {
        Class<?> oldDemo = null;
        Class<?> newDemo = null;

        oldDemo = Class.forName(oldObject.getClass().getName());
        newDemo = Class.forName(newObject.getClass().getName());

        List<Map<String, Object>> list = new ArrayList<>();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat simpleTimeDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        if (specificInformation != null && !specificInformation.isEmpty()) {
            list.add(specificInformation);
        }

        Field[] oldFields = oldDemo.getDeclaredFields();
        for (Field oldField : oldFields) {
            PropertyInfo propertyInfo = oldField.getAnnotation(PropertyInfo.class);
            if(propertyInfo != null){
                if(propertyInfo.isFieldShow()){
                    oldField.setAccessible(true);
                    String oldDclareName = oldField.getName();
                    Object oldDeclareValue = oldField.get(oldObject);

                    Field newField = newDemo.getDeclaredField(oldDclareName);
                    newField.setAccessible(true);
                    Object newDeclareValue = newField.get(newObject);

                    // 对应数据字典value值
                    if(!propertyInfo.dicTermCode().equals("")){
                        Map<String, Object> dicMap = CommonUtil.getDic(propertyInfo.dicTermCode());
                        if(dicMap != null){
                            if(oldDeclareValue != null && dicMap.containsKey(oldDeclareValue.toString())){
                                oldDeclareValue = dicMap.get(oldDeclareValue.toString());
                            }
                            if(newDeclareValue != null && dicMap.containsKey(newDeclareValue.toString())){
                                newDeclareValue = dicMap.get(newDeclareValue.toString());
                            }
                        }
                    }

                    if (oldDeclareValue == null || newDeclareValue == null) {
                        if (oldDeclareValue == null && newDeclareValue == null) {
                            continue;
                        } else {
                            if(propertyInfo.isDefaultValue() == false){
                                newDeclareValue = "新增" + propertyInfo.name();
                            }
                            if (oldDeclareValue == null) {
                                if (oldField.getType().equals(java.util.Date.class)) {
                                    newDeclareValue = simpleDateFormat.format(newDeclareValue);
                                } else if(oldField.getType().equals(java.sql.Timestamp.class)){
                                    newDeclareValue = simpleTimeDateFormat.format(newDeclareValue);
                                }
                                // 新增日志操作
                                Map<String, Object> newMap = new HashMap<>();
                                newMap.put("fieldName", propertyInfo.name());
                                newMap.put("oldValue", "&nbsp;&nbsp;");
                                newMap.put("newValue", newDeclareValue.toString().equals("")?"&nbsp;&nbsp;":newDeclareValue.toString());
                                list.add(newMap);
                            } else {
                                if (oldField.getType().equals(java.util.Date.class)) {
                                    oldDeclareValue = simpleDateFormat.format(newDeclareValue);
                                } else if(oldField.getType().equals(java.sql.Timestamp.class)){
                                    oldDeclareValue = simpleTimeDateFormat.format(newDeclareValue);
                                }
                                // 删除日志操作
                                Map<String, Object> delMap = new HashMap<>();
                                delMap.put("fieldName", propertyInfo.name());
                                delMap.put("oldValue", oldDeclareValue.toString());
                                delMap.put("newValue", "&nbsp;&nbsp;");
                                list.add(delMap);
                            }
                            continue;
                        }
                    }
                    // 修改日志操作
                    if (!oldDeclareValue.toString().equals(newDeclareValue.toString())) {
                        if(propertyInfo.isDefaultValue() == false){
                            oldDeclareValue = propertyInfo.name()+"旧数据";
                            newDeclareValue = propertyInfo.name()+"新数据";
                        }
                        if (oldField.getType().equals(java.util.Date.class)) {
                            oldDeclareValue = simpleDateFormat.format(oldDeclareValue);
                            newDeclareValue = simpleDateFormat.format(newDeclareValue);
                        } else if(oldField.getType().equals(java.sql.Timestamp.class)){
                            oldDeclareValue = simpleTimeDateFormat.format(newDeclareValue);
                            newDeclareValue = simpleTimeDateFormat.format(newDeclareValue);
                        }
                        Map<String, Object> updateMap = new HashMap<>();
                        updateMap.put("fieldName", propertyInfo.name());
                        updateMap.put("oldValue", oldDeclareValue.toString().equals("")?"&nbsp;&nbsp;":oldDeclareValue.toString());
                        updateMap.put("newValue", newDeclareValue.toString().equals("")?"&nbsp;&nbsp;":newDeclareValue.toString());
                        list.add(updateMap);

                    }
                }
            }
        }
        return new Gson().toJson(list);
    }

}
