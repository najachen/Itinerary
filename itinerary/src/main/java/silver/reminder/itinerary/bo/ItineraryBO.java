package silver.reminder.itinerary.bo;

import android.content.Context;

import silver.reminder.itinerary.dao.TaskDao;
import silver.reminder.itinerary.dao.TaskDaoImpl;

/**
 * Created by Administrator on 2016/8/25.
 */
public class ItineraryBO {

    private static ItineraryBO itineraryBO;

    private ItineraryBO(Context context){
        taskDao = TaskDaoImpl.getInstance(context);
    }

    public static ItineraryBO getInstance(Context context){
        if(itineraryBO == null){
            itineraryBO = new ItineraryBO(context);
        }
        return itineraryBO;
    }

    private TaskDao taskDao;

    /*
        方法寫在下面======================================================
     */


}
