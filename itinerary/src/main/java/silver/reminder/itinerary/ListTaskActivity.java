package silver.reminder.itinerary;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

import java.util.Calendar;

import silver.reminder.itinerary.util.GlobalNaming;
import silver.reminder.itinerary.util.Pager;

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

    /*
       task 欄位名稱
     */
    private static final String TASK_TABLE_NAME = "task";
    private static final String TASK_FIELD_ID = "_id";
    private static final String TASK_FIELD_NAME = "name";
    private static final String TASK_FIELD_SITE = "site";
    private static final String TASK_FIELD_TIME = "time";
    private static final String TASK_FIELD_DATE = "date";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_task_activity);

        findViews();

        this.searchMode = getIntent().getIntExtra(GlobalNaming.TASK_SEARCH_MODE, GlobalNaming.ERROR_CODE);
    }

    @Override
    protected void onResume() {
        super.onResume();

        //顯示第一頁清單
        showListView(null);
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
        showListView(Pager.GO_BACKWARD);
    }

    /**
     * 下一頁
     *
     * @param view
     */
    private void nextPage(View view) {
        showListView(Pager.GO_FORWARD);
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
     * <p>
     * 這裡的寫法看似很工程導向(就是很屌的意思)
     * 但是不切實際 過度複雜化問題
     * 僅學術練習用 下次不再這樣做
     *
     * @param adapterView
     * @param view
     * @param position
     * @param l
     */
    private void onTaskClick(AdapterView<?> adapterView, View view, int position, long l) {

        SimpleCursorAdapter adapter = (SimpleCursorAdapter) adapterView.getAdapter();
        Cursor cursor = adapter.getCursor();

        boolean isMoveToPositionSuccess = cursor.moveToPosition(position);
        if (isMoveToPositionSuccess) {
            int taskId = cursor.getInt(cursor.getColumnIndexOrThrow(TASK_FIELD_ID));

            Intent intent = new Intent(this, ListTaskItemActivity.class);
            intent.putExtra(GlobalNaming.TASK_ID, taskId);
            startActivity(intent);
        } else {
            Log.d("選取cursor其中一筆資料的id有誤!!", "目前cursor位置 = " + cursor.getPosition());
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
     * 以分頁機制顯示清單
     *
     * @param isGoForward 是否前進或後退或原地不動
     */
    private void showListView(Boolean isGoForward) {

        Calendar calendarToday = Calendar.getInstance();
        int startDate = GlobalNaming.getDateInt(calendarToday);
        int startTime = GlobalNaming.getTimeInt(calendarToday);

        Calendar endCalendar = getSearchEndDateString((Calendar) calendarToday.clone());
        int endDate = GlobalNaming.getDateInt(endCalendar);
        int endTime = GlobalNaming.getTimeInt(endCalendar);

        Pager pager = new Pager(this, PAGE_SIZE);
        Cursor cursor = pager.getPagedCursorForTask(startDate, startTime
                , endDate
                , endTime
                , this.currentPage
                , isGoForward);

        SimpleCursorAdapter myCursorAdapter = new SimpleCursorAdapter(this
                , R.layout.embedding_task_list
                , cursor
                , new String[]{TASK_FIELD_NAME, TASK_FIELD_SITE, TASK_FIELD_DATE, TASK_FIELD_TIME}
                , new int[]{R.id.taskName, R.id.taskSite, R.id.taskDate, R.id.taskTime}
                , 0);
        this.tasklist.setAdapter(myCursorAdapter);
    }

    /**
     * 取得搜尋日期範圍 結束點的字串
     *
     * @return 20160528121542
     */
    private Calendar getSearchEndDateString(Calendar searchEndCalendar) {

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
        searchEndCalendar.set(Calendar.HOUR_OF_DAY, 23);
        searchEndCalendar.set(Calendar.MINUTE, 59);
        searchEndCalendar.set(Calendar.SECOND, 59);

        return searchEndCalendar;
    }

    /**
     *
     */
//    class MyCursorAdapter extends SimpleCursorAdapter {
//
//        public MyCursorAdapter(Context context, int layout, Cursor c, String[] from, int[] to, int flags) {
//            super(context, layout, c, from, to, flags);
//        }
//
//        @Override
//        public void bindView(View view, Context context, Cursor cursor) {
//
//            if (view == null) {
//                view = LayoutInflater.from(context).inflate(R.layout.embedding_task_list, null, false);
//
//                TextView taskName = (TextView) view.findViewById(R.id.taskName);
//                String name = cursor.getString(cursor.getColumnIndex(TASK_FIELD_NAME));
//                taskName.setText(name);
//
//                TextView taskSite = (TextView) view.findViewById(R.id.taskSite);
//                String site = cursor.getString(cursor.getColumnIndex(TASK_FIELD_SITE));
//                taskSite.setText(site);
//
//                TextView taskTime = (TextView) view.findViewById(R.id.taskTime);
//                String tm = cursor.getString(cursor.getColumnIndex(TASK_FIELD_TIME));
//                taskTime.setText(GlobalNaming.getDateFormat(tm));
//            }
//
//            super.bindView(view, context, cursor);
//
//            TextView taskDate = (TextView) view.findViewById(R.id.taskDate);
//            int date = cursor.getInt(cursor.getColumnIndex(TASK_FIELD_DATE));
//            taskDate.setText(GlobalNaming.getDateFormat(tm));
//
//            TextView taskTime = (TextView) view.findViewById(R.id.taskTime);
//            String tm = cursor.getString(cursor.getColumnIndex(TASK_FIELD_TIME));
//            taskTime.setText(GlobalNaming.getDateFormat(tm));
//        }
//    }
}
