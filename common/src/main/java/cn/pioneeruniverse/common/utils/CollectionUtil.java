package cn.pioneeruniverse.common.utils;

import com.google.gson.Gson;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.apache.commons.lang3.StringUtils;

import java.lang.reflect.InvocationTargetException;
import java.util.*;

/**
 * @Author: yaojiaxin [yaojiaxin@pioneerservice.cn]
 * @Description:集合工具类
 * @Date: Created in 17:33 2018/11/20
 * @Modified By:
 */
@SuppressWarnings("rawtypes")
public class CollectionUtil extends CollectionUtils {

    /**
     * 提取集合中的对象的两个属性(通过Getter函数), 组合成Map.
     *
     * @param collection        来源集合.
     * @param keyPropertyName   要提取为Map中的Key值的属性名.
     * @param valuePropertyName 要提取为Map中的Value值的属性名.
     */
    @SuppressWarnings("unchecked")
    public static Map extractToMap(final Collection collection, final String keyPropertyName,
                                   final String valuePropertyName) throws IllegalAccessException, NoSuchMethodException, InvocationTargetException {
        Map map = new HashMap(collection.size());
            for (Object obj : collection) {
                map.put(PropertyUtils.getProperty(obj, keyPropertyName),
                        PropertyUtils.getProperty(obj, valuePropertyName));
            }
        return map;
    }

    /**
     * 提取集合中的对象的一个属性(通过Getter函数), 组合成List.
     *
     * @param collection   来源集合.
     * @param propertyName 要提取的属性名.
     */
    @SuppressWarnings("unchecked")
    public static List extractToList(final Collection collection, final String propertyName) throws IllegalAccessException, NoSuchMethodException, InvocationTargetException {
        List list = new ArrayList(collection.size());
            for (Object obj : collection) {
                list.add(PropertyUtils.getProperty(obj, propertyName));
            }
        return list;
    }

    /**
     * 提取集合中的对象的一个属性(通过Getter函数), 组合成由分割符分隔的字符串.
     *
     * @param collection   来源集合.
     * @param propertyName 要提取的属性名.
     * @param separator    分隔符.
     */
    public static String extractToString(final Collection collection, final String propertyName, final String separator) throws IllegalAccessException, NoSuchMethodException, InvocationTargetException {
        List list = extractToList(collection, propertyName);
        return StringUtils.join(list, separator);
    }

    /**
     * 转换Collection所有元素(通过toString())为String, 中间以 separator分隔。
     */
    public static String convertToString(final Collection collection, final String separator) {
        return StringUtils.join(collection, separator);
    }

    /**
     * 转换Collection所有元素(通过toString())为String, 每个元素的前面加入prefix，后面加入postfix，如<div>mymessage</div>。
     */
    public static String convertToString(final Collection collection, final String prefix, final String postfix) {
        StringBuilder builder = new StringBuilder();
        for (Object o : collection) {
            builder.append(prefix).append(o).append(postfix);
        }
        return builder.toString();
    }

    /**
     * 判断是否为空.
     */
    public static boolean isEmpty(Collection collection) {
        return (collection == null || collection.isEmpty());
    }

    /**
     * 取得Collection的第一个元素，如果collection为空返回null.
     */
    public static <T> T getFirst(Collection<T> collection) {
        if (isEmpty(collection)) {
            return null;
        }

        return collection.iterator().next();
    }

    /**
     * 获取Collection的最后一个元素 ，如果collection为空返回null.
     */
    public static <T> T getLast(Collection<T> collection) {
        if (isEmpty(collection)) {
            return null;
        }

        //当类型为List时，直接取得最后一个元素 。
        if (collection instanceof List) {
            List<T> list = (List<T>) collection;
            return list.get(list.size() - 1);
        }

        //其他类型通过iterator滚动到最后一个元素.
        Iterator<T> iterator = collection.iterator();
        while (true) {
            T current = iterator.next();
            if (!iterator.hasNext()) {
                return current;
            }
        }
    }

    /**
     * 从一个对象集合中过滤出只符合要求的对象
     */
    public static <T> List<T> filterByProperty(Collection<T> collection, final String propertyName, final Object propertyValue) {
        org.apache.commons.collections.CollectionUtils.filter(collection, new Predicate() {
            @Override
            public boolean evaluate(Object o) {
                try {
                    return PropertyUtils.getProperty(o, propertyName).equals(propertyValue);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                    throw new RuntimeException(e);
                } catch (InvocationTargetException e) {
                    e.printStackTrace();
                    throw new RuntimeException(e);
                } catch (NoSuchMethodException e) {
                    e.printStackTrace();
                    throw new RuntimeException(e);
                }
            }
        });
        return (List<T>) collection;
    }

	/**
	 * 获取两个集合的不同元素
	 * @param <T>
	 * 
	 * @param collmax
	 * @param collmin
	 * @return
	 */
	public static <T> Collection<T> getDiffent(Collection<T> collmax, Collection<T> collmin) {
		// 使用LinkeList防止差异过大时,元素拷贝
		Collection<T> csReturn = new LinkedList<T>();
		Collection max = collmax;
		Collection min = collmin;
		// 先比较大小,这样会减少后续map的if判断次数
		if (collmax.size() < collmin.size()) {
			max = collmin;
			min = collmax;
		}
		// 直接指定大小,防止再散列
		Map<Object, Integer> map = new HashMap<Object, Integer>(max.size());
		for (Object object : max) {
			map.put(object, 1);
		}
		for (Object object : min) {
			if (map.get(object) == null) {
				csReturn.add((T) object);
			} else {
				map.put(object, 2);
			}
		}
		for (Map.Entry<Object, Integer> entry : map.entrySet()) {
			if (entry.getValue() == 1) {
				csReturn.add((T) entry.getKey());
			}
		}
		return csReturn;
	}

	/**
	 * 获取两个集合的不同元素,去除重复
	 * 
	 * @param collmax
	 * @param collmin
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static Collection getDiffentNoDuplicate(Collection collmax, Collection collmin) {
		return new HashSet(getDiffent(collmax, collmin));
	}

    /**
     *@author liushan
     *@Description 判断两个list的内容以及顺序是否完全相同
     *@Date 2019/8/9
     *@Param [listA, listB]
     *@return java.lang.Boolean
     **/
    public static Boolean getIsEquals(List<Object> listA,List<Object> listB){
        try {
            if(listA.size() != listB.size()) {
                return false;
            }
            for(int i = 0,len = listA.size(); i < len;i++){
                if(!listA.get(i).toString().equals(listB.get(i).toString())) {
                    return false;
                }
            }
        } catch (Exception e){
            return false;
        }
        return true;
    }

    /**
     *@author liushan
     *@Description list转成map
     *@Date 2020/02/18
     *@Param [collection, key,value]
     *@return Map
     **/
    public static Map<Object,Object> listToMap(final Collection collection, final String key,final String value) throws IllegalAccessException, NoSuchMethodException, InvocationTargetException {
        Map<Object,Object> result = new HashMap<>();
        for (Object obj: collection) {
            result.put(PropertyUtils.getProperty(obj, key),PropertyUtils.getProperty(obj, value));
        }
        return result;
    }

    /**
     *@author liushan
     *@Description list转成map key value是对象
     *@Date 2020/02/18
     *@Param [collection, key,value]
     *@return Map
     **/
    public static Map<Object,Object> listToMapObj(final Collection collection, final String key) throws IllegalAccessException, NoSuchMethodException, InvocationTargetException {
        Map<Object,Object> result = new HashMap<>();
        for (Object obj: collection) {
            result.put(PropertyUtils.getProperty(obj, key),new Gson().toJson(obj));
        }
        return result;
    }

}
