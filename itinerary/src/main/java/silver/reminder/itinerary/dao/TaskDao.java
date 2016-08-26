package silver.reminder.itinerary.dao;

import android.database.Cursor;

import javabean.Task;

/**
 * Created by Administrator on 2016/8/25.
 */
public interface TaskDao {

    Cursor findTaskList(Task task);

    Cursor findTaskList(Task task, int pageNum, int pageSize);

    Cursor findTaskById(int id);

    int createTask(Task task);

    int updateTask(Task task);

    int deleteTask(int id);
}
