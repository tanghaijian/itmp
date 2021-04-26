package cn.pioneeruniverse.common.utils;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import org.springframework.util.StringUtils;

/**
 * 日期操作方法集。
 * 
 * @author Yang Yihua
 */

public class DateUtil {

	/**
	 * 默认日期格式，默认格式为<b>yyyy-MM-dd</b>
	 */
	public static String format = "yyyy-MM-dd";
	
	public static String fullFormat = "yyyy-MM-dd HH:mm:ss";
	/**
	 * 默认日期格式，默认格式为<b>"yyyy-MM-dd HH:mm:ss:SSS";</b>
	 */
	public static String SSS_Format = "yyyy-MM-dd HH:mm:ss:SSS";
	public static String formatTime = "MM/dd/yyyy HH:mm";
	public static String formatTime2 = "yyyy-MM-dd HH:mm";
	/**
	 * 使用默认格式来格式化日期。
	 * 
	 * @param date
	 *            需要格式化的日期。
	 * 
	 * @return 格式化后的日期字符串。如果date为null，则返回空字符串。
	 */
	public static String formatDate(Date date) {
		return formatDate(date, "");
	}

	/**
	 * 使用指定的格式来格式化日期。默认格式为<b>yyyy-MM-dd</b>
	 * 
	 * @param formatString
	 *            需要格式化的日期。
	 * 
	 * @param format
	 *            指定日期的格式化字符串。如果为空字符串或者null，则使用默认的格式来 格式化日期。
	 * 
	 * @return 格式化后的日期字符串。如果date为null，则返回空字符串。
	 */
	public static String formatDate(Date date, String formatString) {

		String result = "";
		SimpleDateFormat sdf = null;

		try {

			if ((formatString == null) || (formatString.length() <= 0)) {
				formatString = format;
			}

			sdf = new SimpleDateFormat(formatString);
			result = sdf.format(date);

		} catch (Exception e) {
			result = "";

		}
		return result;

	}

	/**
	 * 使用默认的字符串格式，解析字符串并生成日期对象。默认格式为<b>yyyy年MM月dd日</b>
	 * 
	 * @param parseString
	 *            需要解析的字符串。
	 * 
	 * @return 根据指定的字符串格式生成的日期对象。如果parseString为空字符串或者null， 返回日期对象为null。
	 */
	public static Date parseDate(String parseString) {
		return parseDate(parseString, "");
	}

	/**
	 * 根据指定的字符串格式，解析字符串并生成日期对象。
	 * 
	 * @param parseString
	 *            需要解析的字符串。
	 * 
	 * @param formatString
	 *            指定的字符串格式。
	 * 
	 * @return 根据指定的字符串格式生成的日期对象。如果parseString为空字符串或者null，
	 *         返回日期对象为null。如果formatString为空字符串或者null，则使用默认日期格式解析。
	 */
	public static Date parseDate(String parseString, String formatString) {

		Date result = null;
		SimpleDateFormat sdf = null;

		try {
			if ((parseString != null) && (parseString.length() > 0)) { // paesrString是正确的，准备解析

				if ((formatString == null) || (formatString.length() <= 0)) { // 没有指定格式，使用默认格式。
					formatString = format;
				}

				sdf = new SimpleDateFormat(formatString);
				sdf.setLenient(false);
				result = sdf.parse(parseString);
			}

		} catch (Exception e) {
			
		}
		return result;
	}
	   /**  @desc:  取得指定参数的日期对象
	     *  @param dateStr 时间字符串
	     *  @param format 格式
	     *  @return
	  	 *  Date
	  	 *  @author   lee
	     *  @version    2013-2-28 
	  	 */
	public static Date getDate(String dateStr, String format1) {
 		try {
 			if(format1==null){
 				format1=format;
 			}
			return new SimpleDateFormat(format1).parse(dateStr);
		} catch (Exception e) {
			return new Date();
		}
		
	}
	public static String getDateString(java.sql.Timestamp timestamp, String format1) {
 		try {
 			if(timestamp==null){
 				return "";
 			}
  			Date date=new java.util.Date(timestamp.getTime());
 			return new SimpleDateFormat(format1).format(date).toString();
		} catch (Exception e) {
			return "";
		}
 	}
	public static String getDateString(Date date, String format1) {
 		try {
 			if(date==null){
 				return "";
 			}
  			return new SimpleDateFormat(format1).format(date).toString();
		} catch (Exception e) {
			return "";
		}
 	}
	public static String getDateString(String format1) {
 		try {
 			if(format1==null){
 				format1=format;
 			}
  			return new SimpleDateFormat(format1).format(new Date()).toString();
		} catch (Exception e) {
			return "";
		}
 	} 
	public static String getTimestampString(java.sql.Timestamp timestamp, String format1) {
 		try {
 			if(timestamp==null){
 				return "";
 			}
  			Date date=new java.util.Date(timestamp.getTime());
 			return new SimpleDateFormat(format1).format(date).toString();
		} catch (Exception e) {
			return "";
		}
 	}
	public static String getDateString(java.sql.Date date, String format1) {
 		try {
 			if(date==null){
 				return "";
 			}
  			Date date1=new java.util.Date(date.getTime());
 			return new SimpleDateFormat(format1).format(date1).toString();
		} catch (Exception e) {
			return "";
		}
 	}
	   /**  @desc:  
	     *  @param date
	     *  @param format1
	     *  @param addDay
	     *  @return
	  	 *  String
	  	 *  @author   lee
	     *  @version    2013-8-16 
	  	 */
	public static String getDateString(java.sql.Date date, String format1,int addDay) {
 		try {
 			if(date==null){
 				return "";
 			}
 			if(format1==null){
 				format1=format;
 			}
 			long time=date.getTime();
 			if(addDay>0){
 				time=time+addDay*1000*60*60*24;
 			}else{
 				time=time-addDay*1000*60*60*24;
 			}
  			Date date1=new java.util.Date(time);
  			 
 			return new SimpleDateFormat(format1).format(date1).toString();
		} catch (Exception e) {
			return "";
		}
 	}
	public static String getDateStringUsingAddDay(java.util.Date date, String format1,int addDay) {
 		try {
 			if(date==null){
 				date=new java.util.Date();
 			}
 			if(format1==null){
 				format1=format;
 			}
 			long time=date.getTime();
 			if(addDay>0){
 				time=time+addDay*1000*60*60*24;
 			}else{
 				time=time-addDay*1000*60*60*24;
 			}
  			Date date1=new java.util.Date(time);
  			 
 			return new SimpleDateFormat(format1).format(date1).toString();
		} catch (Exception e) {
			return "";
		}
 	}
	public static java.sql.Timestamp  getAddDayTimestamp(java.sql.Timestamp timestamp,  int addDay) {
    		long time=timestamp.getTime();
 			if(addDay>0){
 				time=time+addDay*1000*60*60*24;
 			}else{
 				time=time-addDay*1000*60*60*24;
 			}
   			timestamp.setTime(time);
 			return timestamp;
  	}
	public static String getUtilDateString(java.util.Date date, String format1) {
 		try {
 			if(date==null){
 				date=new java.util.Date();
 			}
 			if(format1==null){
 				format1=format;
 			}
  			return new SimpleDateFormat(format1).format(date).toString();
		} catch (Exception e) {
			return "";
		}
		
	}
	public static String getCurrentTimeFormatStr( String format1) {
 		try {
  			if(format1==null){
 				format1=format;
 			}
  			return new SimpleDateFormat(format1).format(new java.util.Date()).toString();
		} catch (Exception e) {
			return "";
		}
 	}
	public static java.sql.Timestamp getTimestampByDateStr(String dateStr, String format1) {
 		try {
 			if(dateStr==null){
 				return null;
 			}
 			if(format1==null){
 				format1=format;
 			}
  			return  new java.sql.Timestamp(new SimpleDateFormat(format1).parse(dateStr).getTime());
		} catch (Exception e) {
 			return null;
		}
		
	}
	public static java.sql.Date getSqlDateByDateStr(String dateStr, String format1) {
 		try {
 			if(dateStr==null){
 				return null;
 			}
 			if(format1==null){
 				format1=format;
 			}
  			return  new java.sql.Date(new SimpleDateFormat(format1).parse(dateStr).getTime());
		} catch (Exception e) {
 			return null;
		}
		
	}
	   /**  @desc:  当前时间的时间戳
	     *  @return
	  	 *  java.sql.Timestamp
	  	 *  @author   lee
	     *  @version    2013-4-22 
	  	 */
	public static java.sql.Timestamp getCurrentTimestamp() {
 		try {
 			 
  			return  new java.sql.Timestamp(new Date().getTime());
		} catch (Exception e) {
 			return null;
		}
		
	}
	public static String dateDiff(Date beginDate, Date endDate) {

		String begin = formatDate(beginDate, "yyyyMMdd");

		String end = formatDate(endDate, "yyyyMMdd");

		if (begin.equals("") || end.equals("")) {

			return "";

		}

		double betweenyears = 0.0;

		GregorianCalendar begindate = new GregorianCalendar(Integer
				.parseInt(begin.substring(0, 4)), Integer.parseInt(begin
				.substring(4, 6)) - 1, Integer.parseInt(begin.substring(6, 8)));
		GregorianCalendar enddate = new GregorianCalendar(Integer.parseInt(end
				.substring(0, 4)), Integer.parseInt(end.substring(4, 6)) - 1,
				Integer.parseInt(end.substring(6, 8)));

		long begintime = begindate.getTime().getTime();

		long endtime = enddate.getTime().getTime();

		betweenyears = ((endtime - begintime) / (1000 * 60 * 60 * 24) + 0.5) / (30 * 12);

		DecimalFormat df = new DecimalFormat("###0.0");

		String result = df.format(betweenyears) + "年";

		return result;

	}

	public static String dateDiffYear(Date beginDate, Date endDate) {

		String begin = formatDate(beginDate, "yyyyMMdd");

		String end = formatDate(endDate, "yyyyMMdd");

		if (begin.equals("") || end.equals("")) {

			return "";

		}

//		double betweenyears = 0.0;

//		GregorianCalendar begindate = new GregorianCalendar(Integer
//				.parseInt(begin.substring(0, 4)), Integer.parseInt(begin
//				.substring(4, 6)) - 1, Integer.parseInt(begin.substring(6, 8)));
//		GregorianCalendar enddate = new GregorianCalendar(Integer.parseInt(end
//				.substring(0, 4)), Integer.parseInt(end.substring(4, 6)) - 1,
//				Integer.parseInt(end.substring(6, 8)));

//		long begintime = begindate.getTime().getTime();

//		long endtime = enddate.getTime().getTime();

//		betweenyears = (double) (((endtime - begintime) / (1000 * 60 * 60 * 24) + 0.5) / (30 * 12));

//		DecimalFormat df = new DecimalFormat("###0.0");
		int n = Integer.parseInt(end.substring(0, 4))
				- Integer.parseInt(begin.substring(0, 4));
		// if (Integer.parseInt(end.substring(4,6)) -
		// Integer.parseInt(begin.substring(4,6)) > 0
		// && Integer.parseInt(end.substring(6,8)) -
		// Integer.parseInt(begin.substring(6,8)) > 0
		// )
		if (Integer.parseInt(end.substring(4, 6))
				- Integer.parseInt(begin.substring(4, 6)) > 0) {
			n++;
		}
		// String result=df.format(betweenyears)+"年";
		String result = new Integer(n).toString();

		return result;

	}

	/**
	 * 从fromDate增加monthNum个月
	 * 
	 * @param fromDate
	 * @param monthNum
	 * @return
	 */
	public static Date addMonth(Date fromDate, int monthNum) {
		Calendar calendar = Calendar.getInstance();
		if (fromDate == null) {
			calendar.setTime(new Date());
		} else {
			calendar.setTime(fromDate);
		}

		calendar.add(Calendar.MONTH, monthNum);

		return calendar.getTime();
	}
	
	public static double countDays(Date begin,Date end){
		  double days = 0;	
		  Calendar c_b = Calendar.getInstance();
		  Calendar c_e = Calendar.getInstance();		  
		  try{
		   c_b.setTime(begin);
		   c_e.setTime(end);		   
		   while(c_b.before(c_e)){
		    days++;
		    c_b.add(Calendar.DAY_OF_YEAR, 1);
		   }
		  }catch(Exception pe){
			  System.out.println("日期格式错误");
		  }		  
		  return days; 
	}

	/**
	 * 返回时间差秒数
	 * @param begin
	 * @param end
	 * @return
	 */
	public static int substractTime(Date begin, Date end) {
	    long difference = end.getTime() - begin.getTime();
        return Long.valueOf(difference / 1000).intValue();
	}
	
	/**
	 * 在指定日历字段上增减指定时间量
	 * @param dest
	 * @param field
	 * @param amount
	 * @return
	 */
	public static Date addTime(Date dest, int field, int amount) {
	    Calendar calendar = Calendar.getInstance();
	    calendar.setTime(dest);
	    calendar.add(field, amount);
	    return calendar.getTime();
	}
	
	/**
	 * 将两个日期合并为【yyyy-MM-dd HH:mm~HH:mm】格式字符串
	 * @param startDate
	 * @param endDate
	 * @return
	 */
	public static String combineDatetime(Date startDate, Date endDate) {
	    String date = formatDate(startDate, "yyyy-MM-dd");
	    String timeFormat = "HH:mm";
	    String startTime = formatDate(startDate, timeFormat);
        String endTime = formatDate(endDate, timeFormat);
        return date + " " + startTime + "~" + endTime;
	}
	
	/**
	 * 获取当年yyyy年MM月
	 * @param startDate
	 * @param endDate
	 * @return
	 */
	public static Date getCurrYearMonth(String month) {
		String year = formatDate(new Date(), "yyyy")+"-";
	    Date yearDate = parseDate(year+month,"yyyy-MM");
        return yearDate;
	}
	
	/**
	 * 获取去年yyyy年MM月
	 * @param startDate
	 * @param endDate
	 * @return
	 */
	public static Date getLastYearMonth(String month) {
		String year = Integer.parseInt(formatDate(new Date(), "yyyy"))-1+"-";
	    Date yearDate = parseDate(year+month,"yyyy-MM");
        return yearDate;
	}
	
	   /**  @desc:  取系统时间
	     *  @return
	  	 *  long
	  	 *  @author   lee
	     *  @version    2013-4-15 
	  	 */
	public static  long getSystemTime() {
		 return new Date().getTime();
	}
	/**
	 * 获取年份下拉框
	 * @param type 指定上下多少年
	 * @return
	 */
	public static String getYears(int type) {
		StringBuffer years = new StringBuffer();
		Calendar ca = Calendar.getInstance();
		int yearl = ca.get(Calendar.YEAR);
		for (int i = -3; i <= type; i++) {
			if (i == 0) {
				years.append("<option value=\"" + yearl
								+ "\"  selected=\"selected\" >" + yearl
								+ "</option>\n");
			} else {
				years.append("<option value=\"" + (yearl + i) + "\" >"
						+ (yearl + i) + "</option>\n");
			}
		}
		return years.toString();
	}
	
	
	/**
	 * 获取年份下拉框
	 * type 指定上下多少年
	 * year指定被选中的年份
	 */
	
	public static String getSelectYears(int type,int year) {
		StringBuffer years = new StringBuffer();
		int yearl = year;
		for (int i = -3; i <= type; i++) {
			if (i == 0) {
				years.append("<option value=\"" + yearl
								+ "\"  selected=\"selected\" >" + yearl
								+ "</option>\n");
			} else {
				years.append("<option value=\"" + (yearl + i) + "\" >"
						+ (yearl + i) + "</option>\n");
			}
		}
		return years.toString();
	}
	
	/**
	 * 获取当月第一天
	 * @param
	 * @return
	 */
	public static String getFirstDayOfMonth(String dateS){
		String str = "";
		Calendar lastDate = Calendar.getInstance();
		if(!StringUtils.isEmpty(dateS)){
			String[] date = dateS.split("-");
			lastDate.set(Calendar.YEAR, Integer.valueOf(date[0]));
			lastDate.set(Calendar.MONTH, Integer.valueOf(date[1])-1);
		}
		lastDate.set(Calendar.DATE, 1);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		str = sdf.format(lastDate.getTime());
		return str;
	}
	
	/**
	 * 获取当月最后一天
	 * @param
	 * @return
	 */
	public static String getEndDayOfMonth(String dateS){
		String str = "";
		Calendar lastDate = Calendar.getInstance();
		if(!StringUtils.isEmpty(dateS)){
			String[] date = dateS.split("-");
			lastDate.set(Calendar.YEAR, Integer.valueOf(date[0]));
			lastDate.set(Calendar.MONTH, Integer.valueOf(date[1])-1);
		}
		lastDate.set(Calendar.DATE, 1);
		lastDate.add(Calendar.MONTH, 1);
		lastDate.add(Calendar.DATE, -1);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		str = sdf.format(lastDate.getTime());
		return str;
	}
	
	/**
	 * 获取上月第一天
	 * @param
	 * @return
	 */
	public static String getPreviousMonthFirst(String dateS){
		String str = "";
		Calendar lastDate = Calendar.getInstance();
		if(!StringUtils.isEmpty(dateS)){
			String[] date = dateS.split("-");
			lastDate.set(Calendar.YEAR, Integer.valueOf(date[0]));
//			lastDate.set(Calendar.MONTH, date[1].equals("12")?0:Integer.valueOf(date[1])-1);
			lastDate.set(Calendar.MONTH, Integer.valueOf(date[1])-1);
		}
		lastDate.set(Calendar.DATE, 1);
		lastDate.add(Calendar.MONTH, -1);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		str = sdf.format(lastDate.getTime());
		return str;
	}
	
	/**
	 * 获取上月最后一天
	 * @param
	 * @return
	 */
	public static String getPreviousMonthEnd(String dateS){
		String str = "";
		Calendar lastDate = Calendar.getInstance();
		if(!StringUtils.isEmpty(dateS)){
			String[] date = dateS.split("-");
			lastDate.set(Calendar.YEAR, Integer.valueOf(date[0]));
//			lastDate.set(Calendar.MONTH, date[1].equals("12")?0:Integer.valueOf(date[1])-1);
			lastDate.set(Calendar.MONTH, Integer.valueOf(date[1])-1);
		}
		lastDate.set(Calendar.DATE, 1);
		lastDate.add(Calendar.DATE, -1);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		str = sdf.format(lastDate.getTime());
		return str;
	}
	
	/**
	 * 获取下月第一天
	 * @param
	 * @return
	 */
	public static String getNextMonthFirst(){
		String str = "";
		Calendar lastDate = Calendar.getInstance();
		lastDate.set(Calendar.DATE, 1);
		lastDate.add(Calendar.MONTH, 1);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		str = sdf.format(lastDate.getTime());
		return str;
	}
	
	/**
	 * 获取该日期的上一天
	 * @param
	 * @return
	 */
	public static String getPreviousDate(String date){
		Date d = getDate(date,null);
		String str = "";
		Calendar lastDate = Calendar.getInstance();
//		lastDate.set(Calendar.DATE, d.getDate());
		lastDate.setTime(d);
		lastDate.add(Calendar.DATE, -1);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		str = sdf.format(lastDate.getTime());
		return str;
	}
	
	/**
	 * 获取下半月的日期
	 * @param
	 * @return
	 */
	public static String getNextMonth(String s){
		String str = "";
		String[] date = s.split("-");
		Calendar lastDate = Calendar.getInstance();
		lastDate.set(Calendar.YEAR, date[1].equals("12")?Integer.valueOf(date[0])+1:Integer.valueOf(date[0]));
		lastDate.set(Calendar.DATE, Integer.valueOf(date[2]));
		lastDate.set(Calendar.MONTH, date[1].equals("12")?0:Integer.valueOf(date[1]));
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		str = sdf.format(lastDate.getTime());
		return str;
	}
	   
	   /**  @desc:  通过加减小时，得到指定的格式字符串
	     *  @param format
	     *  @param hour
	     *  @return
	  	 *  String
	  	 *  @author   lee
	     *  @version    2014-2-26 
	  	 */
	public static String getDateFormatByHourAdd(String format,int hour){
		if(format==null){
			format="yyyy-MM-dd-HH";
		}
 		Calendar calendar = Calendar.getInstance();
 		calendar.setTime(new Date());
 		calendar.add(Calendar.HOUR, hour);
 		SimpleDateFormat sdf = new SimpleDateFormat(format);
 		return sdf.format(calendar.getTime());
 	}
 	   /**  @desc:  计算某一天是一年中的第几星期  :cal.get(Calendar.WEEK_OF_YEAR)
	     *  @param date
	     *  @return
	  	 *  int
	  	 *  @author   lee
	     *  @version    2014-2-26 
	  	 */
	public static int getWeekIndex(Date date){
		if(date==null){
			date=new Date();
		}
 		Calendar calendar = Calendar.getInstance();
 		calendar.setTime(date);
 		return calendar.get(Calendar.WEEK_OF_YEAR); 
  	}
	public static Date  getDateByWeekIndex(int weekIndex){
  		Calendar calendar = Calendar.getInstance();
  		calendar.set(Calendar.WEEK_OF_YEAR, weekIndex);
  		return calendar.getTime();
  	}
	public static  java.sql.Timestamp  getTimestampByWeekIndex(int weekIndex){
  		Calendar calendar = Calendar.getInstance();
  		calendar.set(Calendar.WEEK_OF_YEAR, weekIndex);
  		return new java.sql.Timestamp(calendar.getTime().getTime());
  	}
	public static  java.sql.Timestamp  getWeekBeginDayTimestamp(Date date){
		if(date==null){
			date=new Date();
		}
		Calendar calendar = Calendar.getInstance();
 		calendar.setTime(date);
 		int WEEK_OF_YEAR= calendar.get(Calendar.WEEK_OF_YEAR); 
 		int year=calendar.get(Calendar.YEAR); 
 		int moth=calendar.get(Calendar.MONTH); 
 		calendar.clear();
 		if(WEEK_OF_YEAR==1&&moth>10){
 			year++;
		}
 		calendar.set(Calendar.YEAR, year);
    	calendar.set(Calendar.WEEK_OF_YEAR, WEEK_OF_YEAR);
  		return new java.sql.Timestamp(calendar.getTime().getTime());
  	}
	public static  java.sql.Timestamp  getWeekEndDayTimestamp(Date date){
		if(date==null){
			date=new Date();
		}
		Calendar calendar = Calendar.getInstance();
 		calendar.setTime(date);
 		int WEEK_OF_YEAR= calendar.get(Calendar.WEEK_OF_YEAR); 
 		int year=calendar.get(Calendar.YEAR); 
 		int moth=calendar.get(Calendar.MONTH); 
 		calendar.clear();
 		if(WEEK_OF_YEAR==1&&moth>10){
 			year++;
		}
 		calendar.set(Calendar.YEAR, year);
    	calendar.set(Calendar.WEEK_OF_YEAR, WEEK_OF_YEAR);
  		return new java.sql.Timestamp(calendar.getTime().getTime()+7*24*60*60*1000-1);
  	}
	
	
	 /**
	    * 计算两个日期之间的天数
	    * @param param1  开始时间
	    * @param param2  结束时间
	    * @return
	    */
	   public static int getDays(String param1,String param2) throws Exception{
		   DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		   Date dateFrom = null;
		   Date dateTo = null;
		   try {
			dateFrom = dateFormat.parse(param1);
			dateTo = dateFormat.parse(param2);
			
			} catch (Exception e) {
				// TODO Auto-generated catch block
				throw new Exception(e);
			}
			int workDays = 0;
			Calendar cal = null;
			while(dateFrom.before(dateTo)||dateFrom.equals(dateTo)){
				cal = Calendar.getInstance();
				cal.setTime(dateFrom);
				workDays++	;
				cal.add(Calendar.DAY_OF_MONTH, 1);
				dateFrom = cal.getTime();
			}
			return workDays;
	   }
	   /**
	    * 计算开始日期days天数后日期 
	    * @param date 开始日期
	    * @param days 天数
	    * @return
	    * @throws Exception
	    */
	   public static String calculateDate(String date,Integer days) throws Exception{
		   DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		   java.sql.Date oldDate  = null;
		   String newDate = "";
		   try {
			dateFormat.setLenient(false);
			oldDate = new java.sql.Date(dateFormat.parse(date).getTime());
			Calendar cal = new GregorianCalendar();
			cal.setTime(oldDate);
			int year = cal.get(Calendar.YEAR);
			int month = cal.get(Calendar.MONTH);
			int day = cal.get(Calendar.DAY_OF_MONTH);
			int newDay = day + days;
			cal.set(Calendar.YEAR, year);
			cal.set(Calendar.MONTH, month);
			cal.set(Calendar.DAY_OF_MONTH, newDay);
			java.sql.Date param =  new java.sql.Date(cal.getTimeInMillis());
			newDate = param.toString();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		   return newDate;
	   }
	   /**
		 * 获取该日期的下一天
		 * @param
		 * @return
		 */
		public static String getNextDate(String date){
			Date d = getDate(date,null);
			String str = "";
			Calendar lastDate = Calendar.getInstance();
			lastDate.setTime(d);
			lastDate.add(Calendar.DAY_OF_MONTH, 1);
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			str = sdf.format(lastDate.getTime());
			return str;
		}
		
	
		
}
