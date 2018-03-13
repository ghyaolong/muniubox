package com.taoding.common.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.apache.commons.lang3.time.DateFormatUtils;

/**
 * 日期工具类, 继承org.apache.commons.lang.time.DateUtils类
 */
public class DateUtils extends org.apache.commons.lang3.time.DateUtils {
	
	private static String[] parsePatterns = {
		"yyyy-MM-dd", "yyyy-MM-dd HH:mm:ss", "yyyy-MM-dd HH:mm", "yyyy-MM", 
		"yyyy/MM/dd", "yyyy/MM/dd HH:mm:ss", "yyyy/MM/dd HH:mm", "yyyy/MM",
		"yyyy.MM.dd", "yyyy.MM.dd HH:mm:ss", "yyyy.MM.dd HH:mm", "yyyy.MM",
		"yyyy年MM月dd日"};

	/**
	 * 得到当前日期字符串 格式（yyyy-MM-dd）
	 */
	public static String getDate() {
		return getDate("yyyy-MM-dd");
	}
	
	/**
	 * 得到当前日期字符串 格式（yyyy-MM-dd） pattern可以为："yyyy-MM-dd" "HH:mm:ss" "E"
	 */
	public static String getDate(String pattern) {
		return DateFormatUtils.format(new Date(), pattern);
	}
	
	/**
	 * 得到日期字符串 默认格式（yyyy-MM-dd） pattern可以为："yyyy-MM-dd" "HH:mm:ss" "E"
	 */
	public static String formatDate(Date date, Object... pattern) {
		String formatDate = null;
		if (pattern != null && pattern.length > 0) {
			formatDate = DateFormatUtils.format(date, pattern[0].toString());
		} else {
			formatDate = DateFormatUtils.format(date, "yyyy-MM-dd");
		}
		return formatDate;
	}
	
	/**
	 * 得到日期时间字符串，转换格式（yyyy-MM-dd HH:mm:ss）
	 */
	public static String formatDateTime(Date date) {
		return formatDate(date, "yyyy-MM-dd HH:mm:ss");
	}

	/**
	 * 得到当前时间字符串 格式（HH:mm:ss）
	 */
	public static String getTime() {
		return formatDate(new Date(), "HH:mm:ss");
	}

	/**
	 * 得到当前日期和时间字符串 格式（yyyy-MM-dd HH:mm:ss）
	 */
	public static String getDateTime() {
		return formatDate(new Date(), "yyyy-MM-dd HH:mm:ss");
	}

	/**
	 * 得到当前年份字符串 格式（yyyy）
	 */
	public static String getYear() {
		return formatDate(new Date(), "yyyy");
	}

	/**
	 * 得到当前月份字符串 格式（MM）
	 */
	public static String getMonth() {
		return formatDate(new Date(), "MM");
	}

	/**
	 * 得到当天字符串 格式（dd）
	 */
	public static String getDay() {
		return formatDate(new Date(), "dd");
	}

	/**
	 * 得到当前星期字符串 格式（E）星期几
	 */
	public static String getWeek() {
		return formatDate(new Date(), "E");
	}
	
	/**
	 * 日期型字符串转化为日期 格式
	 * { "yyyy-MM-dd", "yyyy-MM-dd HH:mm:ss", "yyyy-MM-dd HH:mm", 
	 *   "yyyy/MM/dd", "yyyy/MM/dd HH:mm:ss", "yyyy/MM/dd HH:mm",
	 *   "yyyy.MM.dd", "yyyy.MM.dd HH:mm:ss", "yyyy.MM.dd HH:mm" }
	 */
	public static Date parseDate(Object str) {
		if (str == null){
			return null;
		}
		try {
			return parseDate(str.toString(), parsePatterns);
		} catch (ParseException e) {
			return null;
		}
	}

	/**
	 * 获取过去的天数
	 * @param date
	 * @return
	 */
	public static long pastDays(Date date) {
		long t = new Date().getTime()-date.getTime();
		return t/(24*60*60*1000);
	}

	/**
	 * 获取过去的小时
	 * @param date
	 * @return
	 */
	public static long pastHour(Date date) {
		long t = new Date().getTime()-date.getTime();
		return t/(60*60*1000);
	}
	
	/**
	 * 获取过去的分钟
	 * @param date
	 * @return
	 */
	public static long pastMinutes(Date date) {
		long t = new Date().getTime()-date.getTime();
		return t/(60*1000);
	}
	
	/**
	 * 转换为时间（天,时:分:秒.毫秒）
	 * @param timeMillis
	 * @return
	 */
    public static String formatDateTime(long timeMillis){
		long day = timeMillis/(24*60*60*1000);
		long hour = (timeMillis/(60*60*1000)-day*24);
		long min = ((timeMillis/(60*1000))-day*24*60-hour*60);
		long s = (timeMillis/1000-day*24*60*60-hour*60*60-min*60);
		long sss = (timeMillis-day*24*60*60*1000-hour*60*60*1000-min*60*1000-s*1000);
		return (day>0?day+",":"")+hour+":"+min+":"+s+"."+sss;
    }
	
	/**
	 * 获取两个日期之间的天数
	 * 
	 * @param before
	 * @param after
	 * @return
	 */
	public static double getDistanceOfTwoDate(Date before, Date after) {
		long beforeTime = before.getTime();
		long afterTime = after.getTime();
		return (afterTime - beforeTime) / (1000 * 60 * 60 * 24);
	}
	
	/**
	 * 获取当月第一天<br>
	 * 精确到毫秒
	 * @return
	 */
	public static Date getFirstDayOfMonth() {
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.DAY_OF_MONTH, 1);
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);
		return calendar.getTime();
	}
	
	/**
	 * 获取目标日期的月第一天<br>
	 * 精确到毫秒
	 * @param date 目标日期
	 * @param value 距离目标日期的月份  正值为加 负值为减
	 * @return 
	 */
	public static Date getFirstDayOfMonth(Date date, int value) {
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.MONTH, value);
		calendar.set(Calendar.DAY_OF_MONTH, 1);
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);
		return calendar.getTime();
	}
	
	/**
	 * 获取当月最后一天</br>
	 * 精确到毫秒
	 * @return
	 */
	public static Date getLastDayOfMonth() {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(getFirstDayOfMonth());
		calendar.add(Calendar.MONTH, 1);
		calendar.set(Calendar.MILLISECOND, -1);
		return calendar.getTime();
	}
	
	/**
	 * 获取指定时间的最后一天</br>
	 * 精确到毫秒
	 * @param date
	 * @return
	 */
	public static Date getLastDayOfMonth(Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.add(Calendar.MONTH, 1);
		calendar.set(Calendar.DAY_OF_MONTH, 1);
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, -1);
		return calendar.getTime();
	}
	
	/**
     * 
     * 描述:获取下一个月的第一天.
     * 
     * @return
     */
    public static String getPerFirstDayOfMonth() {
        SimpleDateFormat dft = new SimpleDateFormat("yyyy-MM-dd");
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MONTH, 1);
        calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMinimum(Calendar.DAY_OF_MONTH));
        return dft.format(calendar.getTime());
    }
    
    /**
     * 
     * 描述:获取当前日期的下一个月第一天.
     * 
     * @return
     */
    public static String getPerFirstDayOfMonth(Date date) {
    	SimpleDateFormat dft = new SimpleDateFormat("yyyy-MM-dd");
    	Calendar calendar = Calendar.getInstance();
    	calendar.setTime(date);
    	calendar.add(Calendar.MONTH, 1);
    	calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMinimum(Calendar.DAY_OF_MONTH));
    	return dft.format(calendar.getTime());
    }
    
    /**
     * 
     * 描述:获取当前日期的第一天.
     * 
     * @return
     */
    public static String getFirstDayOfMonth(Date date) {
    	SimpleDateFormat dft = new SimpleDateFormat("yyyy-MM-dd");
    	Calendar calendar = Calendar.getInstance();    
    	calendar.setTime(date);
    	calendar.add(Calendar.MONTH, 0);
    	calendar.set(Calendar.DAY_OF_MONTH,1);//设置为1号,当前日期既为本月第一天 
        return dft.format(calendar.getTime());
    }
    
    /**
     * 获取当前日期最后一天
     * 
     * @param date
     * @return
     * @throws ParseException
     */
    public static String getLastMonthDate(Date date) {
        SimpleDateFormat dft = new SimpleDateFormat("yyyy-MM-dd");
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
        return dft.format(calendar.getTime());
    }
    
    /**
     * 获取当前日期最后一天
     * 
     * @param date
     * @return
     * @throws ParseException
     */
    public static String getLastMonthDate(String strDate) {
    	String pattern = "yyyy-MM-dd";
    	if(strDate == null){
    		return null ;
    	}
    	try {
    		SimpleDateFormat dft = new SimpleDateFormat(pattern);
    		Calendar calendar = Calendar.getInstance();
			calendar.setTime(parseDate(strDate, pattern));
			calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
			return dft.format(calendar.getTime());
		} catch (ParseException e) {
			e.printStackTrace();
		}
    	return null ;
    }
    
    /**
     * String 转 时间
     * 2017年11月30日 上午9:57:39
     * @param date
     * @param patten
     * @return
     */
    public static Date StringToDate(String dateStr,String patten){
    	SimpleDateFormat dft = new SimpleDateFormat(patten);
    	try {
			return dft.parse(dateStr);
		} catch (ParseException e) {
			e.printStackTrace();
		}
    	return null;
    }
    
    /**
     * 
    * @Description: TODO(获取两个日期的相差的月份) 
    * @param date1 
    * @param date2
    * @param patten
    * @return
    * @throws ParseException int 返回类型    
    * @throws 
    * @author lixc
    * @date 2017年12月5日
     */
    
    public static int getMonthsBetween(Date date1, Date date2){

    	if(null == date1 && null == date2) return -1;
        int result = 0;

        Calendar c1 = Calendar.getInstance();
        Calendar c2 = Calendar.getInstance();
        c1.setTime(date1);
        c2.setTime(date2);
        result = c1.get(Calendar.MONTH) - c2.get(Calendar.MONTH);
        result =result+ (c1.get(Calendar.YEAR) - c2.get(Calendar.YEAR))* 12;  
        return result;
    }
    
    /**
     * 获取当年第一天的时间
     * @return
     */
    public static Date getFirstDayOfYear() {
    	Calendar calendar = Calendar.getInstance();
    	calendar.setTime(new Date());
    	calendar.set(Calendar.MONTH, 0);
    	calendar.set(Calendar.DAY_OF_MONTH, 1);
    	calendar.set(Calendar.HOUR_OF_DAY, 0);
    	calendar.set(Calendar.MINUTE, 0);
    	calendar.set(Calendar.SECOND, 0);
    	calendar.set(Calendar.MILLISECOND, 0);
    	return calendar.getTime();
    }
    
    /**
     * 获取当年的最后一天时间
     * @return
     */
    public static Date getLastDayOfYear() {
    	Calendar calendar = Calendar.getInstance();
    	calendar.setTime(new Date());
    	calendar.add(Calendar.YEAR, 1);
    	calendar.set(Calendar.MONTH, 0);
    	calendar.set(Calendar.DAY_OF_MONTH, 1);
    	calendar.set(Calendar.HOUR_OF_DAY, 0);
    	calendar.set(Calendar.MINUTE, 0);
    	calendar.set(Calendar.SECOND, 0);
    	calendar.set(Calendar.MILLISECOND, -1);
    	return calendar.getTime();
    }
    
    
    /**
     * 获取当前日期之前几个月日期 返回Date
     * 2017年12月28日 上午9:34:09
     * @param date
     * @param preMonth
     * @return
     */
    public static Date getPreTime(Date date, int preMonth) {
    	Calendar calendar = Calendar.getInstance();
    	calendar.setTime(date);
    	calendar.add(Calendar.MONTH, preMonth);
    	calendar.set(Calendar.DAY_OF_MONTH, 1);
    	calendar.set(Calendar.HOUR_OF_DAY, 0);
    	calendar.set(Calendar.MINUTE, 0);
    	calendar.set(Calendar.SECOND, 0);
    	calendar.set(Calendar.MILLISECOND, 0);
    	return calendar.getTime();
    }
    
    
    /**
     * 获取当前日期之前几个月日期 返回String
     * 
     * @param date
     * @return
     * @throws ParseException
     */
    public static String getPre2Time(String dateStr ,int preMonth) {
        SimpleDateFormat dft = new SimpleDateFormat("yyyy-MM-dd");
        Calendar calendar = Calendar.getInstance();
        try {
			calendar.setTime(dft.parse(dateStr));
			calendar.add(Calendar.MONTH, preMonth);
			calendar.set(Calendar.DAY_OF_MONTH,1);//设置为1号,当前日期既为本月第一天 
		} catch (ParseException e) {
			e.printStackTrace();
		}
        return dft.format(calendar.getTime());
    }
    
	/**
	 * @param args
	 * @throws ParseException
	 */
	public static void main(String[] args) throws ParseException {
//		System.out.println(formatDate(parseDate("2010/3/6")));
//		System.out.println(getDate("yyyy年MM月dd日 E"));
//		long time = new Date().getTime()-parseDate("2012-11-19").getTime();
//		System.out.println(time/(24*60*60*1000));
	
//		System.out.println(getMonthsBetween(new Date(), DateUtils.parseDate("2015-02-3")));
//		System.out.println(formatDate(getFirstDayOfMonth(new Date(), -1), "yyyy-MM-dd HH:mm:ss"));
		Date date = DateUtils.parseDate("2020-02-01", "yyyy-MM-dd");
		System.out.println(getLastMonthDate(date));
		System.out.println(getLastMonthDate("2020-02-01"));
		System.out.println(getPreTime(new Date(), -6));
	}
}
