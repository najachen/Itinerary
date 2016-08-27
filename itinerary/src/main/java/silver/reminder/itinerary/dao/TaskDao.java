package silver.reminder.itinerary.dao;

import android.database.Cursor;

import java.util.List;

import silver.reminder.itinerary.javabean.Task;

/**
 * Created by StanleyCheng on 2016/8/27.
 */
public interface TaskDao {

    long insertTask(Task task);

    void insertTaskList(List<Task> taskList);

    int updateTask(Task task);

    void updateTaskList(List<Task> taskList);

    void deleteTask(Integer id);

    void deleteTaskList(List<Integer> taskIdList);

    Task selectTaskById(Integer id);

    Cursor selectTaskList(Task task);
}
