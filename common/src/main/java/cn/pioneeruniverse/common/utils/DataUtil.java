package cn.pioneeruniverse.common.utils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.alibaba.fastjson.JSONObject;

public class DataUtil {
	//汉语中数字大写
    private static final String[] CN_UPPER_NUMBER = { "零", "壹", "贰", "叁", "肆","伍", "陆", "柒", "捌", "玖" };
    //汉语中货币单位大写，这样的设计类似于占位符
    private static final String[] CN_UPPER_MONETRAY_UNIT = { "分", "角", "元", "拾", "佰", "仟", "万", 
    	"拾", "佰", "仟", "亿", "拾", "佰", "仟", "兆", "拾","佰", "仟" };
    //特殊字符：整
    private static final String CN_FULL = "整";
    //特殊字符：负
    private static final String CN_NEGATIVE = "负";
    //金额的精度，默认值为2
    private static final int MONEY_PRECISION = 2;
    //特殊字符：零元整
    private static final String CN_ZEOR_FULL = "零元" + CN_FULL;
    
	public static double div(Double v1, Double v2, int scale) {
		if (scale < 0) {
			throw new IllegalArgumentException(
					"the scale must be a positive integer or zero");
		}
		BigDecimal b1 = new BigDecimal(v1.toString());
		BigDecimal b2 = new BigDecimal(v2.toString());
		return b1.divide(b2, scale, BigDecimal.ROUND_HALF_UP).doubleValue();
	}

	public static double add(Double v1, Double v2) {
		BigDecimal b1 = new BigDecimal(v1.toString());
		BigDecimal b2 = new BigDecimal(v2.toString());
		return b1.add(b2).doubleValue();
	}
	
	public static double mul(Double v1, Double v2, int scale) {
		if (scale < 0) {
			throw new IllegalArgumentException(
					"the scale must be a positive integer or zero");
		}
		BigDecimal b1 = new BigDecimal(v1.toString());
		BigDecimal b2 = new BigDecimal(v2.toString());
		return round(b1.multiply(b2).doubleValue(),scale);
	}
	
	public static double round(double v,int scale){
		if(scale <0){
			throw new IllegalArgumentException(
			"the scale must be a positive integer or zero");
		}
		BigDecimal b = new BigDecimal(Double.toString(v));
		BigDecimal one = new BigDecimal("1");
		return b.divide(one, scale, BigDecimal.ROUND_HALF_UP).doubleValue();
	}

	public static List<String> setSort(Set<String> set){
		List<String> list = new ArrayList(Arrays.asList(set.toArray()));
		Collections.sort(list);
		return list;
	}
	
	
	/**
	 * RMB金额转换简单版：998:玖玖捌，-2017:负贰零壹柒,
	 * @param amount
	 * @return
	 */
	public static String amount2CN(String amount) {
		char[] amountStr = amount.toCharArray();
		String amountValue = "";
		for (int i = 0; i < amountStr.length; i++) {
			if (amountStr[i] == '-') {
				amountValue += "负";
			} else if (amountStr[i] == '.') {
				amountValue += "点";
			} else {
				amountValue += CN_UPPER_NUMBER[Integer.parseInt(String.valueOf(amountStr[i]))];
			}
		}
		return amountValue;
	}
	
    /**
     * RMB金额转换标准版：-8080.99:负捌仟零捌拾元零玖角玖分,20.0:贰拾元整
     * @param numberOfMoney 输入的金额
     * @return 对应的汉语大写
     */
    public static String number2CNMontrayUnit(BigDecimal numberOfMoney) {
        StringBuffer sb = new StringBuffer();
        // -1, 0, or 1 as the value of this BigDecimal is negative, zero, or
        // positive.
        int signum = numberOfMoney.signum();
        // 零元整的情况
        if (signum == 0) {
            return CN_ZEOR_FULL;
        }
        //这里会进行金额的四舍五入
        long number = numberOfMoney.movePointRight(MONEY_PRECISION)
                .setScale(0, 4).abs().longValue();
        // 得到小数点后两位值
        long scale = number % 100;
        int numUnit = 0;
        int numIndex = 0;
        boolean getZero = false;
        // 判断最后两位数，一共有四中情况：00 = 0, 01 = 1, 10, 11
        if (!(scale > 0)) {
            numIndex = 2;
            number = number / 100;
            getZero = true;
        }
        if ((scale > 0) && (!(scale % 10 > 0))) {
            numIndex = 1;
            number = number / 10;
            getZero = true;
        }
        int zeroSize = 0;
        while (true) {
            if (number <= 0) {
                break;
            }
            // 每次获取到最后一个数
            numUnit = (int) (number % 10);
            if (numUnit > 0) {
                if ((numIndex == 9) && (zeroSize >= 3)) {
                    sb.insert(0, CN_UPPER_MONETRAY_UNIT[6]);
                }
                if ((numIndex == 13) && (zeroSize >= 3)) {
                    sb.insert(0, CN_UPPER_MONETRAY_UNIT[10]);
                }
                sb.insert(0, CN_UPPER_MONETRAY_UNIT[numIndex]);
                sb.insert(0, CN_UPPER_NUMBER[numUnit]);
                getZero = false;
                zeroSize = 0;
            } else {
                ++zeroSize;
                if (!(getZero)) {
                    sb.insert(0, CN_UPPER_NUMBER[numUnit]);
                }
                if (numIndex == 2) {
                    if (number > 0) {
                        sb.insert(0, CN_UPPER_MONETRAY_UNIT[numIndex]);
                    }
                } else if (((numIndex - 2) % 4 == 0) && (number % 1000 > 0)) {
                    sb.insert(0, CN_UPPER_MONETRAY_UNIT[numIndex]);
                }
                getZero = true;
            }
            // 让number每次都去掉最后一个数
            number = number / 10;
            ++numIndex;
        }
        // 如果signum == -1，则说明输入的数字为负数，就在最前面追加特殊字符：负
        if (signum == -1) {
            sb.insert(0, CN_NEGATIVE);
        }
        // 输入的数字小数点后两位为"00"的情况，则要在最后追加特殊字符：整
        if (!(scale > 0)) {
            sb.append(CN_FULL);
        }
        return sb.toString();
    }
    
    public static Map parserToMap(String s){  
        Map map=new HashMap();  
        JSONObject json=JSONObject.parseObject(s);  
        Iterator keys=json.keySet().iterator();  
        while(keys.hasNext()){  
            String key=(String) keys.next();  
            String value=json.get(key).toString();  
            if(value.startsWith("{")&&value.endsWith("}")){  
                map.put(key, parserToMap(value));  
            }else{  
                map.put(key, value);  
            }  
      
        }  
        return map;  
    } 


}
