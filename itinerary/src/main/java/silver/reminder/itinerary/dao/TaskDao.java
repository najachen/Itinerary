package silver.reminder.itinerary.dao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Administrator on 2016/8/25.
 */
public class TaskDao {

    /**
     * 獨體模式
     */
    private static TaskDao taskDao;

    private TaskDao(Context context){
        SQLiteOpenHelper dbHelper = ItineraryDatabaseHelper.getInstance(context);
        readableDB = dbHelper.getReadableDatabase();
        writableDB = dbHelper.getWritableDatabase();
    }

    public static TaskDao getInstance(Context context){
        if(taskDao == null){
            taskDao = new TaskDao(context);
        }
        return taskDao;
    }

    private SQLiteDatabase readableDB;
    private SQLiteDatabase writableDB;

    /*
        方法寫在下面 ======================================================
     */

}
