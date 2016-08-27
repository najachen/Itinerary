package silver.reminder.itinerary.bo;

import android.content.Context;
import android.database.Cursor;

import java.util.List;

import silver.reminder.itinerary.dao.TaskDao;
import silver.reminder.itinerary.dao.TaskDaoImpl;
import silver.reminder.itinerary.javabean.Task;

/**
 * Created by Administrator on 2016/8/25.
 */
public class ItineraryBOImpl implements ItineraryBO {

    /**
     * 獨體模式
     */
    private static ItineraryBO itineraryBO;

    private ItineraryBOImpl(Context context){
        taskDao = TaskDaoImpl.getInstance(context);
    }

    public static ItineraryBO getInstance(Context context){
        if(itineraryBO == null){
            itineraryBO = new ItineraryBOImpl(context);
        }
        return itineraryBO;
    }

    private TaskDao taskDao;

    /*
        方法寫在下面 ======================================================
     */

    @Override
    public long createTask(Task task) {
        return 0;
    }

    @Override
    public void createTaskList(List<Task> taskList) {

    }

    @Override
    public int modifyTask(Task task) {
        return 0;
    }

    @Override
    public void modifyTaskList(List<Task> taskList) {

    }

    @Override
    public void removeTask(Integer id) {

    }

    @Override
    public void removeTaskList(List<Integer> taskIdList) {

    }

    @Override
    public Task findTaskById(Integer id) {
        return null;
    }

    @Override
    public Cursor findTaskList(Task task) {

//        this.taskDao.

        return null;
    }
}
