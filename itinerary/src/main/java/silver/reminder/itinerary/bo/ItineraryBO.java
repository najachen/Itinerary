package silver.reminder.itinerary.bo;

import android.database.Cursor;

import java.util.List;

import silver.reminder.itinerary.javabean.Task;

/**
 * Created by StanleyCheng on 2016/8/27.
 */
public interface ItineraryBO {

    long createTask(Task task);

    void createTaskList(List<Task> taskList);

    int modifyTask(Task task);

    void modifyTaskList(List<Task> taskList);

    void removeTask(Integer id);

    void removeTaskList(List<Integer> taskIdList);

    Task findTaskById(Integer id);

    Cursor findTaskList(Task task);
}
