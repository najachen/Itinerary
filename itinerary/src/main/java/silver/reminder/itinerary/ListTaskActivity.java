package silver.reminder.itinerary;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

import java.util.Calendar;

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

    /*
       每頁資料筆數
       當前頁碼
       搜尋條件
     */
    private static final int PAGE_SIZE = 5;
    private int currentPage = 1;
    private int searchMode;
    private Calendar todayCalendar;

    /*
       task 欄位名稱
     */
    private static final String TASK_FIELD_ID = "id";
    private static final String TASK_FIELD_NAME = "name";
    private static final String TASK_FIELD_SITE = "site";
    private static final String TASK_FIELD_TIME = "tm";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_task_activity);

        findViews();

        searchMode = getIntent().getIntExtra(GlobalNaming.TASK_SEARCH_MODE, GlobalNaming.ERROR_CODE);
    }

    @Override
    protected void onResume() {
        super.onResume();

        this.todayCalendar = Calendar.getInstance();
    }

    private void findViews() {
        backChoiceOutdoorAllOperation = (Button) findViewById(R.id.backChoiceOutdoorAllOperation);
        backChoiceOutdoorAllOperation.setOnClickListener(this::backChoiceOutdoorAllOperation);
        tasklist = (ListView) findViewById(R.id.taskList);
        tasklist.setOnItemClickListener(this::onTaskClick);
        prePage = (FloatingActionButton) findViewById(R.id.prePageTask);
        prePage.setOnClickListener(this::prePage);
        addTask = (FloatingActionButton) findViewById(R.id.addTask);
        addTask.setOnClickListener(this::addTask);
        nextPage = (FloatingActionButton) findViewById(R.id.nextPageTask);
        nextPage.setOnClickListener(this::nextPage);
    }

    /**
     * 上一頁
     *
     * @param view
     */
    private void prePage(View view) {
        if (currentPage > 1) {
            currentPage--;
        }
        SimpleCursorAdapter simpleCursorAdapter = getPagedAdapter();
        tasklist.setAdapter(simpleCursorAdapter);
    }

    /**
     * 下一頁 xxx 這裡需要重構
     *
     * @param view
     */
    private void nextPage(View view) {
        SQLiteDatabase db = ItineraryDatabaseHelper.getInstance(this).getReadableDatabase();

        //判斷是否為最後一頁
        String todayString = GlobalNaming.getTmString(this.todayCalendar);
        String searchEndDateString = this.getSearchEndDateString();

        long count = DatabaseUtils.queryNumEntries(db, "task", "where tm >= ? and tm <= ?", new String[]{todayString, searchEndDateString});
        int totalPageNum = (int) count / PAGE_SIZE + 1;

        if (currentPage < totalPageNum) {
            currentPage++;
        }
        SimpleCursorAdapter simpleCursorAdapter = getPagedAdapter();
        tasklist.setAdapter(simpleCursorAdapter);
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
     * @param position
     * @param l
     */
    private void onTaskClick(AdapterView<?> adapterView, View view, int position, long l) {
        //取得被點選的item id傳給下一個Activity
        SimpleCursorAdapter adapter = (SimpleCursorAdapter) adapterView.getAdapter();
        Cursor cursor = adapter.getCursor();

        boolean isMoveToFirstSuccess = cursor.moveToFirst();
        boolean isMoveToPositionSuccess = cursor.moveToPosition(position);
        if(isMoveToFirstSuccess && isMoveToPositionSuccess){
            int taskId = cursor.getInt(cursor.getColumnIndexOrThrow(TASK_FIELD_ID));

            Intent intent = new Intent(this, ListTaskItemActivity.class);
            intent.putExtra(GlobalNaming.TASK_ID_CLICKED, taskId);
            startActivity(intent);
        }else{
            Log.d("選取cursor其中一筆資料的id有誤!!", "目前cursor位置 = "+cursor.getPosition());
        }
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
     * 提供第Ｎ頁的內容 xxx 這裡需要重構
     *
     * @return list 需要的 SimpleCursorAdapter
     */
    private SimpleCursorAdapter getPagedAdapter() {
        SQLiteDatabase db = ItineraryDatabaseHelper.getInstance(this).getReadableDatabase();

        /*
            xxx 這裡算bo
         */
        //當頁開始與結束是第幾筆資料
        int intPageNumStart = ((currentPage - 1) * PAGE_SIZE) + 1;
        String strPageNumStart = String.valueOf(intPageNumStart);
        int intDataOffset = PAGE_SIZE - 1;
        String strDataOffset = String.valueOf(intDataOffset);

        //依搜尋模式決定搜尋條件
        String todayString = GlobalNaming.getTmString(this.todayCalendar);
        String searchEndDateString = getSearchEndDateString();

        /*
            xxx 這裡算dao
         */
        //開始搜尋資料
        Cursor cursor = db.rawQuery("select * from task where tm >= ? and tm <= ? limit ? offset ?", new String[]{todayString, searchEndDateString, strPageNumStart, strDataOffset});

        /*
            xxx 這裡回到bo
         */
        SimpleCursorAdapter simpleCursorAdapter = new SimpleCursorAdapter(this
                , R.layout.embedding_task_list
                , cursor
                , new String[]{TASK_FIELD_NAME, TASK_FIELD_SITE, TASK_FIELD_TIME}
                , new int[]{R.id.taskName, R.id.taskSite, R.id.taskTime}
                , 0) {
            @Override
            public void bindView(View view, Context context, Cursor cursor) {

                if (view == null) {
                    view = LayoutInflater.from(context).inflate(R.layout.embedding_task_list, null, false);

                    TextView taskName = (TextView) view.findViewById(R.id.taskName);
                    String name = cursor.getString(cursor.getColumnIndex(TASK_FIELD_NAME));
                    taskName.setText(name);

                    TextView taskSite = (TextView) view.findViewById(R.id.taskSite);
                    String site = cursor.getString(cursor.getColumnIndex(TASK_FIELD_SITE));
                    taskSite.setText(site);

                    TextView taskTime = (TextView) view.findViewById(R.id.taskTime);
                    String tm = cursor.getString(cursor.getColumnIndex(TASK_FIELD_TIME));
                    taskTime.setText(GlobalNaming.transDateFormat(tm));
                }
            }
        };
        return simpleCursorAdapter;
    }

    /**
     * 取得搜尋日期範圍 結束點的字串
     *
     * @return 20160528121542
     */
    private String getSearchEndDateString() {

        Calendar searchEndCalendar = Calendar.getInstance();
        switch (searchMode) {
            case GlobalNaming.TASK_SEARCH_MODE_TODAY:
                //無動作
                break;
            case GlobalNaming.TASK_SEARCH_MODE_THIS_WEEK:
                searchEndCalendar.add(Calendar.DATE, 7);
                break;
            case GlobalNaming.TASK_SEARCH_MODE_THIS_MONTH:
                searchEndCalendar.add(Calendar.MONTH, 1);
                break;
            case GlobalNaming.TASK_SEARCH_MODE_THIS_SEASON:
                searchEndCalendar.add(Calendar.MONTH, 3);
                break;
            case GlobalNaming.TASK_SEARCH_MODE_THIS_YEAR:
                searchEndCalendar.add(Calendar.YEAR, 1);
                break;
            default:
                Log.d("searchMode 傳輸有誤", "其值為 " + searchMode);
        }
        return GlobalNaming.getTmString(searchEndCalendar);
    }
}
