package silver.reminder.itinerary.util;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import silver.reminder.itinerary.dao.ItineraryDatabaseHelper;

/**
 * Created by Administrator on 2016/8/31.
 */
public class GlobalNaming {

    /*
        常用特殊符號
     */
    public static final String DOT = ".";
    public static final String SPACE = " ";

    /*
        intent 錯誤碼
     */
    public static final int ERROR_CODE = 0x0000;

    /*
        任務清單搜尋
     */
    public static final String TASK_SEARCH_MODE = GlobalNaming.class.getName() + DOT + "TASK_SEARCH_MODE";
    public static final int TASK_SEARCH_MODE_TODAY = 0x0001;
    public static final int TASK_SEARCH_MODE_THIS_WEEK = 0x0010;
    public static final int TASK_SEARCH_MODE_THIS_MONTH = 0x0100;
    public static final int TASK_SEARCH_MODE_THIS_SEASON = 0x1000;
    public static final int TASK_SEARCH_MODE_THIS_YEAR = 0x10000;

    /**
     * 被點擊的單一行程 id 傳送到listTaskItemActivity
     */
    public static final String TASK_ID = GlobalNaming.class.getName() + DOT + "TASK_ID";
    public static final String TASK_ITEM_ID = GlobalNaming.class.getName() + DOT + "TASK_ITEM_ID";
    public static final String TASK_ITEM_VIEW_TYPE = GlobalNaming.class.getName() + DOT + "TASK_ITEM_VIEW_TYPE";

    /*
        提醒檔
     */
    public static final String SCHEDULE_FIELD_TO_SAVE_TM = GlobalNaming.class.getName() + DOT + "SCHEDULE_FIELD_TO_SAVE_TM";
    public static final String SCHEDULE_FIELD_TO_SAVE_SOUND_FILE_ID = GlobalNaming.class.getName() + DOT + "SCHEDULE_FIELD_TO_SAVE_SOUND_FILE_ID";

    /*
        音效檔
     */
    public static final String SOUND_FILE_ID = GlobalNaming.class.getName() + DOT + "SOUND_FILE_ID";
    public static final String SOUND_FILE_NAME = GlobalNaming.class.getName() + DOT + "SOUND_FILE_NAME";

    /**
     * 鬧鐘專用intent action名稱
     */
    public static final String INTENT_ACTION_NAME_ALARM_RECEIVER = GlobalNaming.class.getName() + DOT + "INTENT_ACTION_NAME_ALARM_RECEIVER";

    /*
        鬧鐘專用的 intent extra key
     */
    public static final String ALARM_INTENT_EXTRA_KEY_TASK_NAME = "taskName";
    public static final String ALARM_INTENT_EXTRA_KEY_TASK_SITE = "taskSite";
    public static final String ALARM_INTENT_EXTRA_KEY_SOUND_FILE_ID = "soundFileId";
    public static final String ALARM_INTENT_EXTRA_KEY_SCHEDULE_ID = "scheduleId";

    /**
     * 將時間欄位14碼 轉為日期格式
     *
     * @param dateTimeString 14碼時間字串
     * @return 1980/10/25 07:40:55
     */
    public static Calendar getCalendar(String dateTimeString) {

        int year = Integer.parseInt(dateTimeString.substring(0, 4));
        int month = Integer.parseInt(dateTimeString.substring(4, 6)) - 1;
        int date = Integer.parseInt(dateTimeString.substring(6, 8));
        int hour = Integer.parseInt(dateTimeString.substring(8, 10));
        int minute = Integer.parseInt(dateTimeString.substring(10, 12));
        int second = Integer.parseInt(dateTimeString.substring(12, 14));

        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month, date, hour, minute, second);

        //保險
        calendar.set(Calendar.HOUR_OF_DAY, hour);

        return calendar;
    }

    /**
     *
     * @param tmStr
     * @return
     */
    public static String cleanTimeString(String tmStr){
        String[] tmStrArray = tmStr.split("\\W");

        StringBuffer clearTmStr = new StringBuffer();
        for(String piece:tmStrArray){
            clearTmStr.append(piece);
        }

        //not enough length add zero
        String result = clearTmStr.toString();
        if(result.length() == 12){
            clearTmStr.append("00");
        }
        return clearTmStr.toString();
    }

    /**
     * 取得日期
     *
     * @param calendar
     * @return
     */
    public static String getDateString(Calendar calendar) {
        return new SimpleDateFormat("yyyy/MM/dd").format(calendar.getTime());
    }

    /**
     * 取得時間
     *
     * @param calendar
     * @return 20124055
     */
    public static String getTimeString(Calendar calendar) {
        return new SimpleDateFormat("HH:mm:ss").format(calendar.getTime());
    }

    /**
     * 取得日期與時間字串14碼
     *
     * @param calendar
     * @return
     */
    public static String getDateTimeString(Calendar calendar) {
        return new SimpleDateFormat("yyyyMMddHHmmss").format(calendar.getTime());
    }

    /**
     * 取得設定鬧鐘用的pendingIntent
     *
     * @param scheduleId
     * @return 設定鬧鐘用的pendingIntent
     */
    public static PendingIntent getAlarmPendingIntent(Context context, int scheduleId) {

        Intent intent = new Intent();
        intent.setAction(GlobalNaming.INTENT_ACTION_NAME_ALARM_RECEIVER);

        //撈取要顯示在snackbar上的資訊
        SQLiteDatabase db = ItineraryDatabaseHelper.getInstance(context).getReadableDatabase();

        String strScheduleId = String.valueOf(scheduleId);
        Cursor cursor = db.rawQuery("select name, site, soundFileId from task inner join schedule on task._id = schedule.taskId where schedule._id = ?", new String[]{strScheduleId});

        if (cursor.getCount() == 1 && cursor.moveToFirst()) {
            String taskName = cursor.getString(cursor.getColumnIndexOrThrow("name"));
            String taskSite = cursor.getString(cursor.getColumnIndexOrThrow("site"));
            int soundFileId = cursor.getInt(cursor.getColumnIndexOrThrow("soundFileId"));

            intent.putExtra(ALARM_INTENT_EXTRA_KEY_TASK_NAME, taskName);
            intent.putExtra(ALARM_INTENT_EXTRA_KEY_TASK_SITE, taskSite);
            intent.putExtra(ALARM_INTENT_EXTRA_KEY_SOUND_FILE_ID, soundFileId);

            intent.putExtra(ALARM_INTENT_EXTRA_KEY_SCHEDULE_ID, scheduleId);
        }

        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, scheduleId, intent, PendingIntent.FLAG_ONE_SHOT);
        return pendingIntent;
    }
}
