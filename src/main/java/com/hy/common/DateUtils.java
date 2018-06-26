package com.hy.common;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * @Description 日期工具类
 */
public class DateUtils {

    /**
     * 日期对象转字符串
     * @author xp
     * @param date
     * @param format
     * @return
     */

    private static final SimpleDateFormat DF_YM = new SimpleDateFormat(
            "yyyyMM");

    private static final SimpleDateFormat DF_D = new SimpleDateFormat(
            "dd");

    private static final SimpleDateFormat DF_YMD = new SimpleDateFormat(
            "yyyyMMdd");

    private static final SimpleDateFormat DF_YMDHMS2 = new SimpleDateFormat(
            "yyyyMMddHHmmss");

    private static final SimpleDateFormat HHMISS=new SimpleDateFormat("HHmmss");

    private static final SimpleDateFormat YYYYMMDDHHMISS=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public static String formatDate(Date date,String format){
        String result="";
        SimpleDateFormat sdf=new SimpleDateFormat(format);
        if(date!=null){
            result=sdf.format(date);
        }
        return result;
    }

    /**
     * 按照yyyyMMddhhmmss 获取当前日期
     * @return
     */
    public static String getCurrentDateStr() {
        Date date=new Date();
        SimpleDateFormat sdf=new SimpleDateFormat("yyyyMMddhhmmss");
        return sdf.format(date);
    }

    /**
     * 获得当前date时间
     *
     * @return yyyyMMddHHmmss
     */
    public static String getDisplayYMDHHMMSS() {
        try {
            Calendar calendar = Calendar.getInstance();
            return ((SimpleDateFormat) DF_YMDHMS2.clone()).format(calendar
                    .getTime());
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 获取标准时间格式 YYYY-MM-DD HH:mi:ss
     * @return
     */
    public static String getStandardTime(){

        return DateUtils.YYYYMMDDHHMISS.format(new Date());

    }

    /**
     * 获得date时间
     *
     * @param date
     * @return yyyyMMdd
     */
    public static String getDisplayYMD(Date date) {
        try {
            if (null == date) {
                return null;
            }
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            return ((SimpleDateFormat) DF_YMD.clone()).format(calendar
                    .getTime());
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 获取当前的时间 HHmiss
     * @return
     */
    public static String getTodayTime(){

        return DateUtils.HHMISS.format(new Date());
    }

    /**
     * 获取当前星期
     * @return
     */
    public static int getWeek() {
        Calendar cal = Calendar.getInstance();
        int week=(cal.get(Calendar.DAY_OF_WEEK))-1;
        if(week==0){
            week=7;
        }
        return week;
    }

    /**
     * 获取当前日期前一天的日期
     * @return
     */
    public static String getPreDay(){
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, -1);    //得到前一天
        return getDisplayYMD(calendar.getTime());
    }

    /**
     * 根据输入参数，获取当前日期前N天的日期
     * @return
     */
    public static String getParamPreDay(int i){
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, i);    //得到前一天
        return getDisplayYMD(calendar.getTime());
    }

    /**
     * 获取当前日期后一天的日期
     * @return
     */
    public static String getPreDays(){
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, +1);    //得到后一天
        return getDisplayYMD(calendar.getTime());
    }

    /**
     * 得到当前日期字符串
     * @return
     */
    public static String getTodayToString(){
        return DateUtils.DF_YMD.format(new Date());
    }

    /**
     * 获取清算日
     * @return
     */
    public static String getPreDtToString(){

        Calendar cal = Calendar.getInstance();

        if(getWeek()==7){
            cal.add(Calendar.DATE, -2);
            return getDisplayYMD(cal.getTime());
        }else if(getWeek()==6){
            cal.add(Calendar.DATE, -1);
            return getDisplayYMD(cal.getTime());
        }else{
            return DateUtils.DF_YMD.format(cal.getTime());
        }

    }

    /**
     * 返回年月
     * @return
     */
    public static String getYYYYMM(){
        return DateUtils.DF_YM.format(new Date());
    }

    /**
     * 返回当日
     * @return
     */
    public static String getDD(){
        return DateUtils.DF_D.format(new Date());
    }

    public static String getSession(String  date,int size) throws ParseException{
        Calendar calendar = Calendar.getInstance();
        Date a=DF_YMDHMS2.parse(date);
        calendar.setTime(a);
        calendar.add(Calendar.SECOND, +size);
        return ((SimpleDateFormat) DF_YMDHMS2.clone()).format(calendar
                .getTime());
    }

    /**
     * 获得指定日期的前一天
     * @param specifiedDay
     * @return
     * @throws Exception
     */
    public static String getSpecifiedDayBefore(String specifiedDay){
        //SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Calendar c = Calendar.getInstance();
        Date date=null;
        try {
            date = new SimpleDateFormat("yyyyMMdd").parse(specifiedDay);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        c.setTime(date);
        int day=c.get(Calendar.DATE);
        c.set(Calendar.DATE,day-1);

        String dayBefore=new SimpleDateFormat("yyyyMMdd").format(c.getTime());
        return dayBefore;
    }

    public static Date addDate(Date date,int i){
        Calendar calendar = Calendar.getInstance();
         calendar.setTime(date);
         calendar.add(Calendar.DAY_OF_MONTH, i);
        return calendar.getTime();
    }

}
