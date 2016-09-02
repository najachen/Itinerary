package silver.reminder.itinerary;

import java.util.Calendar;

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
    public static final String SCHEDULE_OBJECT = GlobalNaming.class.getName() + DOT + "SCHEDULE_OBJECT";
    public static final String SCHEDULE_ID = GlobalNaming.class.getName() + DOT + "SCHEDULE_ID";

    /*
        音效檔
     */
    public static final String SOUND_FILE_ID = GlobalNaming.class.getName() + DOT + "SOUND_FILE_ID";
    public static final String SOUND_FILE_NAME = GlobalNaming.class.getName() + DOT + "SOUND_FILE_NAME";;

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
     * 取得時間的14碼字串
     *
     * @param calendar 要轉的日曆物件
     * @return 20160901124055
     */
    public static String getTmString(Calendar calendar) {

        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH) + 1;
        int date = calendar.get(Calendar.DATE);
        int hour = calendar.get(Calendar.HOUR);
        int minute = calendar.get(Calendar.MINUTE);
        int second = calendar.get(Calendar.SECOND);

        StringBuffer tmBuffer = new StringBuffer();
        tmBuffer.append(year).append(month).append(date).append(hour).append(minute).append(second);

        return tmBuffer.toString();
    }
}
