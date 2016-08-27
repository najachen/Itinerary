package silver.reminder.itinerary.bo;

import android.content.Context;

import silver.reminder.itinerary.dao.TaskDao;

/**
 * Created by Administrator on 2016/8/25.
 */
public class ItineraryBO {

    /**
     * 獨體模式
     */
    private static ItineraryBO itineraryBO;

    private ItineraryBO(Context context){
        taskDao = TaskDao.getInstance(context);
    }

    public static ItineraryBO getInstance(Context context){
        if(itineraryBO == null){
            itineraryBO = new ItineraryBO(context);
        }
        return itineraryBO;
    }

    private TaskDao taskDao;

    /*
        方法寫在下面 ======================================================
     */


}
