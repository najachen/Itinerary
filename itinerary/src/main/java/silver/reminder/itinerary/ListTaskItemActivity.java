package silver.reminder.itinerary;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

import java.util.Calendar;

import silver.reminder.itinerary.bo.ItineraryBo;
import silver.reminder.itinerary.bo.ItineraryBoImpl;
import silver.reminder.itinerary.javabean.Task;

public class ListTaskItemActivity extends AppCompatActivity {

    private FloatingActionButton prePageTaskItem;
    private FloatingActionButton fabAddNoteOrShopping;
    private FloatingActionButton nextPageTaskItem;
    private Button backTaskList;
    private Button editOrDeleteTask;
    private Button setReminder;
    private ListView taskItemList;
    private TextView taskName;
    private TextView taskTime;
    private TextView taskSite;

    /**
     * 上一頁被點擊的 taskId
     */
    private int taskId;

    /*
        請求碼
     */
    private static final int REQUEST_CODE_EDIT_TASK_ITEM = 1;

    /*
      欄位名稱
    */
    private static final String COMMON_FIELD_ID = "id";
    private static final String COMMON_FIELD_TASK_ID = "taskId";

    // shopping 欄位名稱
    private static final String SHOPPING_FIELD_NAME = "name";
    private static final String SHOPPING_FIELD_QUANTITY = "quantity";
    private static final String SHOPPING_FIELD_UNITPRICE = "unitPrice";

    // note 欄位名稱
    private static final String SHOPPING_FIELD_NOTE_CONTENT = "noteContent";
    private static final String SHOPPING_FIELD_NOTE_EXPLAIN = "noteExplain";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_task_item_activity);

        findViews();

        this.taskId = getIntent().getIntExtra(GlobalNaming.TASK_ID_CLICKED, GlobalNaming.ERROR_CODE);

        /*
            顯示明細與清單
         */
        ItineraryBo itineraryBo = ItineraryBoImpl.getInstance(this);
        //顯示明細
        Task task = itineraryBo.findTaskById(this.taskId);
        this.taskName.setText(task.getName());
        this.taskTime.setText(GlobalNaming.getDateFormat(task.getTm()));
        this.taskSite.setText(task.getSite());

        //xxx 清單
        showListView(null);
    }

    private void findViews() {
        prePageTaskItem = (FloatingActionButton) findViewById(R.id.prePageTaskItem);
        prePageTaskItem.setOnClickListener(this::prePageTaskItem);
        fabAddNoteOrShopping = (FloatingActionButton) findViewById(R.id.fabAddNoteOrShopping);
        fabAddNoteOrShopping.setOnClickListener(this::fabAddNoteOrShopping);
        nextPageTaskItem = (FloatingActionButton) findViewById(R.id.nextPageTaskItem);
        nextPageTaskItem.setOnClickListener(this::nextPageTaskItem);

        backTaskList = (Button) findViewById(R.id.backTaskList);
        backTaskList.setOnClickListener(this::backTaskList);
        editOrDeleteTask = (Button) findViewById(R.id.editOrDeleteTask);
        editOrDeleteTask.setOnClickListener(this::editOrDeleteTask);
        setReminder = (Button) findViewById(R.id.setReminder);
        setReminder.setOnClickListener(this::setReminder);

        taskItemList = (ListView) findViewById(R.id.taskItemList);
        taskItemList.setOnItemClickListener(this::onTaskItemClick);

        taskName = (TextView) findViewById(R.id.taskName);
        taskTime = (TextView) findViewById(R.id.taskTime);
        taskSite = (TextView) findViewById(R.id.taskSite);
    }

    /**
     * 購物或備忘記事被點擊
     *
     * @param adapterView
     * @param view
     * @param position
     * @param rowId
     */
    private void onTaskItemClick(AdapterView<?> adapterView, View view, int position, long rowId) {

        SimpleCursorAdapter adapter = (SimpleCursorAdapter) adapterView.getAdapter();
        Cursor cursor = adapter.getCursor();

        boolean isMoveToFirstSuccess = cursor.moveToFirst();
        boolean isMoveToPositionSuccess = cursor.moveToPosition(position);
        if(isMoveToFirstSuccess && isMoveToPositionSuccess){
            clickedTaskItemId = cursor.getInt(cursor.getColumnIndexOrThrow(COMMON_FIELD_ID));
        }

        new AlertDialog.Builder(this)
                .setMessage("編輯或是刪除??")
                .setPositiveButton("編輯", ListTaskItemActivity.this::editTaskItem)
                .setNegativeButton("刪除", ListTaskItemActivity.this::deleteTaskItem)
                .setNeutralButton("取消", null)
                .show();
    }

    /**
     * 編輯清單細項
     * @param dialogInterface
     * @param which
     */
    private void editTaskItem(DialogInterface dialogInterface, int which){
        Intent intent = new Intent(this, CreateOrEditTaskItemActivity.class);
        intent.putExtra(GlobalNaming.TASK_ITEM_ID_CLICKED, clickedTaskItemId);
        startActivityForResult(intent, REQUEST_CODE_EDIT_TASK_ITEM);
    }

    /**
     * 刪除清單細項
     * @param dialogInterface
     * @param which
     */
    private void deleteTaskItem(DialogInterface dialogInterface, int which){
        ItineraryBo itineraryBo = ItineraryBoImpl.getInstance(this);

        itineraryBo.
    }

    /**
     * 設置提醒
     *
     * @param view
     */
    private void setReminder(View view) {


    }

    /**
     * 編輯或修改行程
     *
     * @param view
     */
    private void editOrDeleteTask(View view) {
        new AlertDialog.Builder(this)
                .setMessage("編輯或新增行程??")
                .setPositiveButton("編輯", ListTaskItemActivity.this::editTask)
                .setNegativeButton("刪除", ListTaskItemActivity.this::deleteTask)
                .setNeutralButton("取消", null)
                .show();
    }

    /**
     * 刪除行程
     *
     * @param dialogInterface
     * @param which
     */
    private void deleteTask(DialogInterface dialogInterface, int which) {

    }

    /**
     * 編輯行程
     *
     * @param dialogInterface
     * @param which
     */
    private void editTask(DialogInterface dialogInterface, int which) {

    }

    /**
     * 回到行程清單
     *
     * @param view
     */
    private void backTaskList(View view) {
        finish();
    }

    /**
     * 下一頁
     *
     * @param view
     */
    private void nextPageTaskItem(View view) {


    }

    /**
     * 新增記事或購物事項
     *
     * @param view
     */
    private void fabAddNoteOrShopping(View view) {


    }

    /**
     * 上一頁
     *
     * @param view
     */
    private void prePageTaskItem(View view) {


    }

    /**
     * 以分頁機制顯示清單
     *
     * @param isGoForward 是否前進或後退或原地不動
     */
    private void showListView(Boolean isGoForward) {

        Calendar calendarToday = Calendar.getInstance();
        String startTm = GlobalNaming.getTmString(calendarToday);
        String endTm = this.getSearchEndDateString((Calendar) calendarToday.clone());

        Pager pager = new Pager(this, TASK_TABLE_NAME, TASK_FIELD_TIME, PAGE_SIZE);
        Cursor cursor = pager.getPagedCursor(startTm, endTm, this.currentPage, isGoForward);

        MyCursorAdapter myCursorAdapter = new MyCursorAdapter(this
                , R.layout.embedding_task_list
                , cursor
                , new String[]{TASK_FIELD_NAME, TASK_FIELD_SITE, TASK_FIELD_TIME}
                , new int[]{R.id.taskName, R.id.taskSite, R.id.taskTime}
                , 0);
        this.tasklist.setAdapter(myCursorAdapter);
    }
}
