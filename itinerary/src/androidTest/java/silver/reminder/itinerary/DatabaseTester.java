package silver.reminder.itinerary;

import android.content.Context;
import android.database.Cursor;
import android.test.AndroidTestCase;
import android.test.mock.MockContext;
import android.util.Log;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import silver.reminder.itinerary.dao.TaskDao;
import silver.reminder.itinerary.dao.TaskDaoImpl;
import silver.reminder.itinerary.javabean.Task;

/**
 * 2016/08/27 阿鬼 資料庫存取物件 與 商業邏輯物件 模板開發測試
 */
public class DatabaseTester extends AndroidTestCase{

    private Context context;

    private TaskDao taskDao;

    @Before
    public void setUp() {

//        context = new RenamingDelegatingContext(getContext(), "test_");
        context = new MockContext();
        Assert.assertNotNull(context);

        taskDao = TaskDaoImpl.getInstance(context);
        Assert.assertNotNull(taskDao);

        for (int i = 0; i < 6; i++) {
            Task task = new Task();
            task.setTm("2016082709270" + i);
            task.setSite("台北 " + i + " 號");
            task.setName("辦理 " + i + " 事");

            taskDao.insertTask(task);
        }
    }

    @After
    public void tearDown() {

    }

    @Test
    public void databaseTest() throws Exception {

//        Assert.assertEquals(4, 2 + 2);

        Cursor allTaskCursor = taskDao.selectTaskList(new Task());
        String[] colunnNames = allTaskCursor.getColumnNames();
        for(String columnName:colunnNames){
            int columnIndex = allTaskCursor.getColumnIndex(columnName);
            int columnDataType = allTaskCursor.getType(columnIndex);

            switch (columnDataType){
                case Cursor.FIELD_TYPE_INTEGER:
                    int intResultValue = allTaskCursor.getInt(columnIndex);
                    Log.d("欄位名稱 --- ", columnName);
                    Log.d("欄位值 --- ", String.valueOf(intResultValue));
                    break;
                case Cursor.FIELD_TYPE_STRING:
                    String strResultValue = allTaskCursor.getString(columnIndex);
                    Log.d("欄位名稱 --- ", columnName);
                    Log.d("欄位值 --- ", strResultValue);
                    break;
                default:
                    Log.d("沒撈到資料", "資料類型錯誤!!");
            }
        }
    }
}