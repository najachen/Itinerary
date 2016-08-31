package silver.reminder.itinerary;

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

    /*
        音效檔
     */
    public static final String SCHEDULE_SETUP_FEEDBACK = GlobalNaming.class.getName() + DOT + "SCHEDULE_SETUP_FEEDBACK";

}
