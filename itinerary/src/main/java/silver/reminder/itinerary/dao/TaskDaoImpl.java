package silver.reminder.itinerary.dao;

import android.content.Context;
import android.database.Cursor;

import javabean.Task;
import silver.reminder.itinerary.util.SqlEngine;

/**
 * Created by Administrator on 2016/8/25.
 */
public class TaskDaoImpl implements TaskDao{

    private static TaskDao taskDao;

    private TaskDaoImpl(Context context){
        dbHelper = ItineraryDatabaseHelper.getInstance(context);
    }

    public static TaskDao getInstance(Context context){
        if(taskDao == null){
            taskDao = new TaskDaoImpl(context);
        }
        return taskDao;
    }

    private ItineraryDatabaseHelper dbHelper;

    @Override
    public Cursor findTaskList(Task task) {

        StringBuffer sql = new StringBuffer();
        sql.append("select * from task");

        String whereDesc = SqlEngine.editSqlWhereArgs(task, Task.class);
        sql.append(whereDesc);

        Cursor result = dbHelper.getReadableDatabase().rawQuery(sql.toString(), null);
        return result;
    }

    @Override
    public Cursor findTaskList(Task task, int pageNum, int pageSize) {

        StringBuffer sql = new StringBuffer();
        sql.append("select * from task");

        String whereDesc = SqlEngine.editSqlWhereArgs(task, Task.class);
        sql.append(whereDesc);

        sql.append(SqlEngine.limit).append(SqlEngine.space)
                .append(String.valueOf(pageNum*pageSize)).append(SqlEngine.space)
                .append(SqlEngine.offset).append(SqlEngine.space)
                .append(String.valueOf(pageSize)).append(SqlEngine.space);

        Cursor result = dbHelper.getReadableDatabase().rawQuery(sql.toString(), null);
        return result;
    }

    @Override
    public Cursor findTaskById(int id) {
        Cursor result = dbHelper.getReadableDatabase().rawQuery("select * from task where id = "+String.valueOf(id), null);
        return result;
    }

    @Override
    public int createTask(Task task) {



//        dbHelper.getWritableDatabase().insert("task", null, )




        return 0;
    }

    @Override
    public int updateTask(Task task) {
        return 0;
    }

    @Override
    public int deleteTask(int id) {
        return 0;
    }
}
