package com.tencent.customdialog_up_to_down;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.DatePicker;
import android.widget.TimePicker;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;

//import de.keyboardsurfer.android.widget.crouton.Crouton;
//import de.keyboardsurfer.android.widget.crouton.Style;


/**
 * Created by jiang_yan on 2017/5/13.
 */

public class UserUtils {

    private static boolean isShowlog = true;

    /**
     * 打印log
     *
     * @param TAG
     * @param content
     */
    public static void Log(String TAG, String content) {
        if (isShowlog) {
            Log.e(TAG, content);
        }
    }

    /**
     * 以友好的方式显示时间 ,几秒前/几分钟前/几小时前/昨天/前天/天前/日期......
     *
     * @param strDate 1423224342字符串毫秒值
     * @return
     */
    public static String friendly_time(String strDate) {
        long oldTime = new Date(Long.parseLong(strDate)).getTime();
        String ftime = "";
        int minite = 0;
        Calendar cal = Calendar.getInstance();
        // 判断是否是同一天
        String curDate = formatDate(cal.getTime().getTime(), "yyyy-MM-dd");
        String paramDate = formatDate(oldTime, "yyyy-MM-dd");
        ;
        if (curDate.equals(paramDate)) {
            int hour = (int) ((cal.getTimeInMillis() - oldTime) / 3600000);
            if (hour == 0) {
                // 判断是否为同一分钟内
                minite = (int) ((cal.getTimeInMillis() - oldTime) / 60000);
                if (minite == 0) {
                    ftime = Math.max(
                            (cal.getTimeInMillis() - oldTime) / 1000,
                            1)
                            + "秒前";
                } else {
                    ftime = Math
                            .max((cal.getTimeInMillis() - oldTime) / 60000,
                                    1)
                            + "分钟前";
                }

            } else {
                ftime = hour + "小时前";
            }
            return ftime;
        }

        long lt = oldTime / 86400000;
        long ct = cal.getTimeInMillis() / 86400000;
        int days = (int) (ct - lt);
        if (days == 0) {
            int hour = (int) ((cal.getTimeInMillis() - oldTime) / 3600000);
            if (hour == 0) {
                // 判断是否为同一分钟内
                minite = (int) ((cal.getTimeInMillis() - oldTime) / 60000);
                if (minite == 0) {
                    ftime = Math.max(
                            (cal.getTimeInMillis() - oldTime) / 1000,
                            1)
                            + "秒前";
                } else {
                    ftime = Math
                            .max((cal.getTimeInMillis() - oldTime) / 60000,
                                    1)
                            + "分钟前";
                }
            } else {
                ftime = hour + "小时前";
            }
        } else if (days == 1) {
            ftime = "昨天";
        } else if (days == 2) {
            ftime = "前天 ";
        } else if (days > 2 && days < 31) {
            ftime = days + "天前";
        } else if (days >= 31 && days <= 2 * 31) {
            ftime = "一个月前";
        } else if (days > 2 * 31 && days <= 3 * 31) {
            ftime = "2个月前";
        } else if (days > 3 * 31 && days <= 4 * 31) {
            ftime = "3个月前";
        } else {
            ftime = formatDate(oldTime, "yyyy-MM-dd");
        }
        return ftime;
    }

    /**
     * 1413131232 -->一分钟之内,几分钟前,几小时前,日期2016-12-12 13:10
     *
     * @param strDate
     * @return
     */
    public static String friendly_time_2(String strDate) {
        long oldTime = new Date(Long.parseLong(strDate)).getTime();
        String ftime = "";
        int minite;
        Calendar cal = Calendar.getInstance();
        // 判断是否是同一天
        String curDate = formatDate(cal.getTime().getTime(), "yyyy-MM-dd");
        String paramDate = formatDate(oldTime, "yyyy-MM-dd");

        if (curDate.equals(paramDate)) {
            int hour = (int) ((cal.getTimeInMillis() - oldTime) / 3600000);
            if (hour == 0) {
                // 判断是否为同一分钟内
                minite = (int) ((cal.getTimeInMillis() - oldTime) / 60000);
                if (minite == 0) {
//					ftime = Math.max(
//							(cal.getTimeInMillis() - oldTime) / 1000,
//							1)
//							+ "秒前";
                    ftime = "1分钟内";
                } else {
                    ftime = Math
                            .max((cal.getTimeInMillis() - oldTime) / 60000,
                                    1)
                            + "分钟前";
                }

            } else {
                ftime = hour + "小时前";
            }
            return ftime;
        }

        long lt = oldTime / 86400000;
        long ct = cal.getTimeInMillis() / 86400000;
        int days = (int) (ct - lt);
        if (days == 0) {
            int hour = (int) ((cal.getTimeInMillis() - oldTime) / 3600000);
            if (hour == 0) {
                // 判断是否为同一分钟内
                minite = (int) ((cal.getTimeInMillis() - oldTime) / 60000);
                if (minite == 0) {
//					ftime = Math.max(
//							(cal.getTimeInMillis() - oldTime) / 1000,
//							1)
//							+ "秒前";
                    ftime = "1分钟内";
                } else {
                    ftime = Math
                            .max((cal.getTimeInMillis() - oldTime) / 60000,
                                    1)
                            + "分钟前";
                }
            } else {
                ftime = hour + "小时前";
            }
        } else {
            ftime = formatDate(oldTime, "yyyy-MM-dd HH:mm");
        }
        return ftime;
    }

    /**
     * 毫秒时间转换成一定格式的日期,14002020304--->2017-12-12 14:12:43
     *
     * @param timeMillis  时间戳毫秒值
     * @param datePattern yyyy-MM-dd yyyy-MM-dd HH:mm:ss
     * @return
     */
    @SuppressLint("SimpleDateFormat")
    public static String formatDate(long timeMillis, String datePattern) {
        SimpleDateFormat simpleDateFormat = getSimpleDateFormat(datePattern);

        String dateString = simpleDateFormat.format(new Date(timeMillis));
        return dateString;
    }

    /**
     * 一定格式的日期转换成毫秒时间,--->2017-12-12 14:12:43---->1412331433
     *
     * @param date        日期
     * @param datePattern yyyy-MM-dd yyyy-MM-dd HH:mm:ss
     * @return
     */
    @SuppressLint("SimpleDateFormat")
    public static long formatDateToTimeMillis(String date, String datePattern) {
        SimpleDateFormat simpleDateFormat = getSimpleDateFormat(datePattern);
        try {
            Date dateParse = simpleDateFormat.parse(date);
            return dateParse.getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return 0;
    }

    /**
     * 获取现在的时间毫秒值
     *
     * @return
     */
    public static long getTodayTimeMillis() {
        return new Date().getTime();
    }

    /**
     * 获取现在的时间日期的字符串  2017-12-12 12:22等
     *
     * @return 2017-12-12 12:22等
     */
    public static String getTodayDateString(String datePattern) {
        SimpleDateFormat simpleDateFormat = getSimpleDateFormat(datePattern);
        String dateString = simpleDateFormat.format(new Date().getTime());
        return dateString;
    }

    /**
     * 从一种日期格式转换成另一种
     * 2017-12-12 12:33:22  -->  2019-12-30等
     *
     * @param datePatternStart 本来的格式
     * @param datePatternEnd   转换成的格式
     * @return
     */
    public static String transDateSToDateS(String date, String datePatternStart, String datePatternEnd) {
        SimpleDateFormat simpleDateFormatS = getSimpleDateFormat(datePatternStart);
        SimpleDateFormat simpleDateFormatE = getSimpleDateFormat(datePatternEnd);
        try {
            Date parse = simpleDateFormatS.parse(date);
            String dateS_ = simpleDateFormatE.format(parse);
            return dateS_;

        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 获取 SimpleDateFormat
     *
     * @param datePattern
     * @return
     */
    @SuppressLint("SimpleDateFormat")
    private static SimpleDateFormat getSimpleDateFormat(String datePattern) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(datePattern);
        return simpleDateFormat;
    }

    /**
     * 获取 SimpleDateFormat
     *
     * @return
     */
    @SuppressLint("SimpleDateFormat")
    private SimpleDateFormat getSimpleDateFormat() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat();
        return simpleDateFormat;
    }

    /**
     * 弹出键盘
     *
     * @param context
     * @param view
     */
    public static void showSoftInput(Context context, View view) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
//        imm.hideSoftInputFromWindow(view.getWindowToken(), 0); //强制隐藏键盘
//        imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);//显示键盘
        imm.showSoftInput(view, 0);
        //imm.showSoftInput(view, InputMethodManager.SHOW_FORCED);
    }

    /**
     * 隐藏键盘
     *
     * @param context
     * @param view
     */
    public static void hideSoftInput(Context context, View view) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0); //强制隐藏键盘
    }

    /**
     * 随机颜色
     *
     * @return
     */
    public static int randomColor() {
        Random random = new Random();
        int r = random.nextInt(255);
        int g = random.nextInt(255);
        int b = random.nextInt(255);
        int rgb = Color.rgb(r, g, b);
        return rgb;
    }

    /**
     * 显示吐司
     *
     * @param content
     */
//    public static void showCrouton(Activity context, String content) {
//        Crouton.makeText(context, content, Style.ALERT).show();
//    }

    private static Toast toast;

    /**
     * 显示吐司
     *
     * @param context
     * @param content
     */
    public static void showToast(Context context, String content) {
        if (toast == null) {
            toast = Toast.makeText(context, content, Toast.LENGTH_SHORT);
        }
        toast.setText(content);
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.show();
    }


    private static String time;
    private static String date;
    private static int years;
    private static int month;
    private static int day;
    private static int hours;
    private static int minth;

    /**
     * 先要执行setDateAndTimeListen 设置监听
     * 返回时间串  2017-12-3 13:12:49
     * 获取时间
     *
     * @return
     */
    public static String getDateAndTime() {
        return date + " " + time;
    }

    /**
     * 设置时间和日期的监听
     *
     * @param datePicker
     * @param timePicker
     */
    public static void setDateAndTimeListen(DatePicker datePicker, TimePicker timePicker) {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int monthOfYear = calendar.get(Calendar.MONTH);
        int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
        years = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);
        hours = calendar.get(Calendar.HOUR_OF_DAY);
        minth = calendar.get(Calendar.MINUTE);
        timePicker.setCurrentHour(hours);
        timePicker.setCurrentMinute(minth);
        date = year + "-" + String.format("%02d", monthOfYear + 1) + "-" + String.format("%02d", dayOfMonth);
        time = String.format("%02d", calendar.get(Calendar.HOUR_OF_DAY)) + ":" + String.format("%02d", calendar.get(Calendar.MINUTE));
        datePicker.init(year, monthOfYear, dayOfMonth, new DatePicker.OnDateChangedListener() {
            public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                years = year;
                month = monthOfYear;
                day = dayOfMonth;
                date = year + "-" + String.format("%02d", monthOfYear + 1) + "-" + String.format("%02d", dayOfMonth);
            }

        });
        timePicker.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {
            @Override
            public void onTimeChanged(TimePicker arg0, int arg1, int arg2) {
                hours = arg1;
                minth = arg2;
                time = String.format("%02d", arg1) + ":" + String.format("%02d", arg2);
            }
        });

    }

    /**
     * 对话框
     *
     * @param activity
     * @param title
     * @param message
     * @return
     */
    public static AlertDialog.Builder normalDialogBuilder(Activity activity, String title, String message) {

        AlertDialog.Builder normalDialog = new AlertDialog.Builder(activity, 3);
        normalDialog.setIcon(R.mipmap.icon_delete);
        normalDialog.setTitle(title);
        normalDialog.setMessage(message);
        return normalDialog;
    }
}
