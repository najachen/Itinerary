package silver.reminder.itinerary.util;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

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
     * @param tm 14碼時間字串
     * @return 1980/10/25 07:40:55
     */
    public static String getDateFormat(String tm) {

        String year = tm.substring(0, 4);
        String month = tm.substring(4, 6);
        String date = tm.substring(6, 8);
        String hour = tm.substring(8, 10);
        String minute = tm.substring(10, 12);
        String second = tm.substring(12, 14);

        StringBuffer tmBuffer = new StringBuffer();
        tmBuffer.append(year).append("/").append(month).append("/").append(date)
                .append(" ")
                .append(hour).append(":").append(minute).append(":").append(second);

        return tmBuffer.toString();
    }

    /**
     * 取得日期的8碼整數
     *
     * @param calendar
     * @return
     */
    public static int getDateInt(Calendar calendar) {
        int year = calendar.get(Calendar.YEAR);
        String month = addZeroIfLessThanTen(calendar.get(Calendar.MONTH) + 1);
        String date = addZeroIfLessThanTen(calendar.get(Calendar.DATE));

        StringBuffer dateBuffer = new StringBuffer();
        dateBuffer.append(year).append(month).append(date);

        return Integer.parseInt(dateBuffer.toString());
    }

    /**
     * 取得時間的6碼整數
     *
     * @param calendar 要轉的日曆物件
     * @return 124055
     */
    public static int getTimeInt(Calendar calendar) {

        String hour = addZeroIfLessThanTen(calendar.get(Calendar.HOUR_OF_DAY));
        String minute = addZeroIfLessThanTen(calendar.get(Calendar.MINUTE));
        String second = addZeroIfLessThanTen(calendar.get(Calendar.SECOND));

        StringBuffer timeBuffer = new StringBuffer();
        timeBuffer.append(hour).append(minute).append(second);

        return Integer.parseInt(timeBuffer.toString());
    }

    /**
     * 如果小於10 開頭補0
     *
     * @param value 要驗證的數字
     * @return
     */
    private static String addZeroIfLessThanTen(int value) {
        return String.valueOf(value < 10 ? "0" + value : value);
    }

    /**
     * 清空字串中的特殊字 變成乾淨的14碼時間資料
     * 很糟的作法 我知道 但沒時間作更好
     *
     * @param timeString
     * @return
     */
    public static String cleanSpecCharTo14DigiCode(String timeString) {
        String[] timeStringArray = timeString.split("\\W"); //非文字一律視為界線 delimiter

        StringBuffer digiCode = new StringBuffer();
        for (String stringPiece : timeStringArray) {
            digiCode.append(stringPiece);
        }
        return digiCode.toString();
    }

    /**
     * 將14碼時間字串 轉成 Calendar
     *
     * @param tm
     * @return
     */
    public static Calendar getTmCalendar(String tm) {

        int year = Integer.parseInt(tm.substring(0, 2));

        //月份 0-base 特別處理
        String strMonth = tm.substring(2, 4);
        int month = Integer.parseInt(strMonth);
        month--;

        int date = Integer.parseInt(tm.substring(4, 6));
        int hour = Integer.parseInt(tm.substring(6, 8));
        int minute = Integer.parseInt(tm.substring(8, 10));
        int second = Integer.parseInt(tm.substring(10, 12));

        //設定時間
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month, date, hour, minute, second);

        return calendar;
    }

    /**
     * 取得設定鬧鐘用的pendingIntent
     *
     * @param scheduleId
     * @return 設定鬧鐘用的pendingIntent
     */
    public static PendingIntent getAlarmPendingIntent(Context context, long scheduleId) {

        Intent intent = new Intent();
        intent.setAction(GlobalNaming.INTENT_ACTION_NAME_ALARM_RECEIVER);

        //撈取要顯示在snackbar上的資訊
        SQLiteDatabase db = ItineraryDatabaseHelper.getInstance(context).getReadableDatabase();

        String strScheduleId = Long.toString(scheduleId);
        Cursor cursor = db.rawQuery("select name, site, soundFileId from task inner join schedule on task.id = schedule.taskId where schedule.id = ?", new String[]{strScheduleId});

        if (cursor.getCount() == 1 && cursor.moveToFirst()) {
            String taskName = cursor.getString(cursor.getColumnIndexOrThrow("name"));
            String taskSite = cursor.getString(cursor.getColumnIndexOrThrow("site"));
            int soundFileId = cursor.getInt(cursor.getColumnIndexOrThrow("soundFileId"));

            intent.putExtra(ALARM_INTENT_EXTRA_KEY_TASK_NAME, taskName);
            intent.putExtra(ALARM_INTENT_EXTRA_KEY_TASK_SITE, taskSite);
            intent.putExtra(ALARM_INTENT_EXTRA_KEY_SOUND_FILE_ID, soundFileId);

            intent.putExtra(ALARM_INTENT_EXTRA_KEY_SCHEDULE_ID, scheduleId);
        }

        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, (int) scheduleId, intent, PendingIntent.FLAG_ONE_SHOT);
        return pendingIntent;
    }
}
