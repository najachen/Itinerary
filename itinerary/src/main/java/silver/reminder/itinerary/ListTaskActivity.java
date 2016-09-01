package silver.reminder.itinerary;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

import silver.reminder.itinerary.dao.ItineraryDatabaseHelper;

/**
 * Created by Administrator on 2016/8/22.
 */
public class ListTaskActivity extends AppCompatActivity {

    private ListView tasklist;
    private Button backChoiceOutdoorAllOperation;
    private FloatingActionButton prePage;
    private FloatingActionButton addTask;
    private FloatingActionButton nextPage;

    /**
     * 每頁資料筆數與當前頁碼
     */
    private static final int PAGE_SIZE = 5;
    private int CURRENT_PAGE = 1;

    /**
     * task 欄位名稱 xxx
     */
    private static final String TASK_FIELD_NAME = "name";
    private static final String TASK_FIELD_SITE = "site";
    private static final String TASK_FIELD_TIME = "tm";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_task_activity);

        findViews();
    }

    private void findViews() {
        backChoiceOutdoorAllOperation = (Button) findViewById(R.id.backChoiceOutdoorAllOperation);
        backChoiceOutdoorAllOperation.setOnClickListener(this::backChoiceOutdoorAllOperation);
        tasklist = (ListView) findViewById(R.id.taskList);
        tasklist.setOnItemClickListener(this::onTaskItemClick);
        prePage = (FloatingActionButton) findViewById(R.id.prePage);
        prePage.setOnClickListener(this::prePage);
        addTask = (FloatingActionButton) findViewById(R.id.addTask);
        addTask.setOnClickListener(this::addTask);
        nextPage = (FloatingActionButton) findViewById(R.id.nextPage);
        nextPage.setOnClickListener(this::nextPage);
    }

    /**
     * 上一頁
     *
     * @param view
     */
    private void prePage(View view) {


    }

    /**
     * 下一頁
     *
     * @param view
     */
    private void nextPage(View view) {

    }

    /**
     * 新增行程
     *
     * @param view
     */
    private void addTask(View view) {
        Intent intent = new Intent(this, CreateOrEditTaskActivity.class);
        startActivity(intent);
    }

    /**
     * 進入單一行程明細頁面
     *
     * @param adapterView
     * @param view
     * @param i
     * @param l
     */
    private void onTaskItemClick(AdapterView<?> adapterView, View view, int i, long l) {

    }

    /**
     * 返回
     *
     * @param view
     */
    private void backChoiceOutdoorAllOperation(View view) {
        this.finish();
    }

    /**
     * 提供第Ｎ頁的內容
     *
     * @param pageNumber 要提供的資料所屬頁碼
     * @return 轉接器物件
     */
    private SimpleCursorAdapter getPagedAdapter(int pageNumber) {
        SQLiteDatabase db = ItineraryDatabaseHelper.getInstance(this).getReadableDatabase();

        int intPageNumStart = ((pageNumber - 1) * PAGE_SIZE) + 1;
        String strPageNumStart = String.valueOf(intPageNumStart);
        int intDataOffset = PAGE_SIZE - 1;
        String strDataOffset = String.valueOf(intDataOffset);

        Cursor cursor = db.rawQuery("select * from task limit ? offset ?", new String[]{strPageNumStart, strDataOffset});

        SimpleCursorAdapter simpleCursorAdapter = new SimpleCursorAdapter(this
                , R.layout.embedding_task_list
                , cursor
                ,new String[]{"name","site","tm"}
                ,new int[]{R.id.taskName, R.id.taskSite, R.id.taskTime}
                ,0){
            @Override
            public void bindView(View view, Context context, Cursor cursor) {
                if(view == null){
                    view  = LayoutInflater.from(context).inflate(R.layout.embedding_task_list, null, false);

                    TextView taskName = (TextView) findViewById(R.id.taskName);
                    TextView taskSite = (TextView) findViewById(R.id.taskSite);
                    TextView taskTime = (TextView) findViewById(R.id.taskTime);

                    //xxx 做到這裡
//                    String name = cursor.getString(cursor.getColumnIndex())
                }
            }
        };


        return null;
    }
}
