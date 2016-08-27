package silver.reminder.itinerary.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import silver.reminder.itinerary.javabean.Task;

/**
 * Created by Administrator on 2016/8/25.
 */
public class TaskDaoImpl implements TaskDao {

    /**
     * 獨體模式
     */
    private static TaskDao taskDao;

    private TaskDaoImpl(Context context) {
        SQLiteOpenHelper dbHelper = ItineraryDatabaseHelper.getInstance(context);
        readableDB = dbHelper.getReadableDatabase();
        writableDB = dbHelper.getWritableDatabase();
    }

    public static TaskDao getInstance(Context context) {
        if (taskDao == null) {
            taskDao = new TaskDaoImpl(context);
        }
        return taskDao;
    }

    private SQLiteDatabase readableDB;
    private SQLiteDatabase writableDB;

    /*
        方法寫在下面 ======================================================
     */

    @Override
    public long insertTask(Task task) {

        ContentValues contentValues = new ContentValues();
        contentValues.put("tm", task.getTm());
        contentValues.put("name", task.getName());
        contentValues.put("site", task.getSite());

        long rowId = this.writableDB.insert("task", "site", contentValues);
        return rowId;
    }

    @Override
    public void insertTaskList(List taskList) {

    }

    @Override
    public int updateTask(Task task) {

        //這裡是要修改的資料
        ContentValues contentValues = new ContentValues();
        contentValues.put("tm", task.getTm());
        contentValues.put("name", task.getName());
        contentValues.put("site", task.getSite());

        int updateDataAmount = this.writableDB.updateWithOnConflict("task", contentValues, "id = ?", new String[]{task.getId().toString()}, SQLiteDatabase.CONFLICT_ROLLBACK);
        return updateDataAmount;
    }

    @Override
    public void updateTaskList(List taskList) {

    }

    @Override
    public void deleteTask(Integer id) {

        int deleteDataAmount = this.writableDB.delete("task", "id = ?", new String[]{id.toString()});
        if (deleteDataAmount > 1) {
            Log.d("用 id 移除 task 異常!! 移除了 ", deleteDataAmount + " 筆");
        }
    }

    @Override
    public void deleteTaskList(List<Integer> taskIdList) {

    }

    @Override
    public Task selectTaskById(Integer id) {

        Cursor taskCursor = this.readableDB.rawQuery("select * from task where id = ?", new String[]{id.toString()});

        Task result = new Task();

        String[] columnNames = taskCursor.getColumnNames();
        for (String columnName : columnNames) {
            int columnIndex = taskCursor.getColumnIndexOrThrow(columnName);

            switch (columnName) {
                case "id":
                    result.setId(taskCursor.getInt(columnIndex));
                    break;
                case "tm":
                    result.setTm(taskCursor.getString(columnIndex));
                    break;
                case "name":
                    result.setName(taskCursor.getString(columnIndex));
                    break;
                case "site":
                    result.setSite(taskCursor.getString(columnIndex));
                    break;
                default:
                    Log.d("出現欄位名稱錯誤 => ", columnName + columnIndex);
            }
        }
        return result;
    }

    @Override
    public Cursor selectTaskList(Task task) {

        StringBuffer sqlWhere = new StringBuffer();
        sqlWhere.append(" 1=1 ");

        List<String> whereArgs = new ArrayList<String>();

        String tm = task.getTm();
        if (tm != null && tm.length() > 0) {
            sqlWhere.append(" and tm = ? ");
            whereArgs.add(tm);
        }
        String name = task.getName();
        if (name != null && name.length() > 0) {
            sqlWhere.append(" and name = ? ");
            whereArgs.add(name);
        }
        String site = task.getSite();
        if (site != null && site.length() > 0) {
            sqlWhere.append(" and site = ? ");
            whereArgs.add(site);
        }

        String[] whereArgsStringArray = new String[whereArgs.size()];
        whereArgsStringArray = whereArgs.toArray(whereArgsStringArray);

        Cursor result = this.readableDB.query("task", null, sqlWhere.toString(), whereArgsStringArray, null, null, "id");
        return result;
    }
}
