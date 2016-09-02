package silver.reminder.itinerary;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import silver.reminder.itinerary.bo.ItineraryBo;
import silver.reminder.itinerary.bo.ItineraryBoImpl;
import silver.reminder.itinerary.dao.ItineraryDatabaseHelper;
import silver.reminder.itinerary.model.Note;
import silver.reminder.itinerary.model.Shopping;
import silver.reminder.itinerary.model.Task;

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

    /**
     * 這一頁被點擊的taskItemId
     */
    private int taskItemId;
    private int taskItemViewType;

    /**
     * 清單每頁幾筆資料
     */
    private static final int PAGE_SIZE = 5;
    private int currentPage = 1;

    /*
        請求碼
     */
    private static final int REQUEST_CODE_EDIT_TASK_ITEM = 0x0001;
    private static final int REQUEST_CODE_EDIT_TASK = 0x0010;
    private static final int REQUEST_CODE_CREATE_TASK_ITEM = 0x0100;

    /**
     * bo
     */
    private ItineraryBo itineraryBo;

    /*
      欄位名稱
    */
//    private static final String COMMON_FIELD_ID = "id";
//    private static final String COMMON_FIELD_TASK_ID = "taskId";

    // shopping 欄位名稱
//    private static final String SHOPPING_FIELD_NAME = "name";
//    private static final String SHOPPING_FIELD_QUANTITY = "quantity";
//    private static final String SHOPPING_FIELD_UNITPRICE = "unitPrice";

    // note 欄位名稱
//    private static final String SHOPPING_FIELD_NOTE_CONTENT = "noteContent";
//    private static final String SHOPPING_FIELD_NOTE_EXPLAIN = "noteExplain";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_task_item_activity);

        findViews();

        this.taskId = getIntent().getIntExtra(GlobalNaming.TASK_ID, GlobalNaming.ERROR_CODE);

        this.itineraryBo = ItineraryBoImpl.getInstance(this);
    }

    @Override
    protected void onResume() {
        super.onResume();

        /*
            顯示明細與清單
         */
        //顯示明細
        Task task = itineraryBo.findTaskById(this.taskId);
        this.taskName.setText(task.getName());
        this.taskTime.setText(GlobalNaming.getDateFormat(task.getTm()));
        this.taskSite.setText(task.getSite());
        //顯示清單
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
        MyViewHolderAdapter adapter = (MyViewHolderAdapter) adapterView.getAdapter();
        Pager.TaskItemHolder taskItemHolder = adapter.getItem(position);
        taskItemViewType = adapter.getItemViewType(position);

        if (this.taskItemViewType == MyViewHolderAdapter.NOTE_VIEW) {
            this.taskItemId = taskItemHolder.note.getId();
        } else {
            this.taskItemId = taskItemHolder.shopping.getId();
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
     *
     * @param dialogInterface
     * @param which
     */
    private void editTaskItem(DialogInterface dialogInterface, int which) {
        Intent intent = new Intent(this, CreateOrEditTaskItemActivity.class);
        intent.putExtra(GlobalNaming.TASK_ITEM_ID, this.taskItemId);
        intent.putExtra(GlobalNaming.TASK_ITEM_VIEW_TYPE, this.taskItemViewType);
        startActivityForResult(intent, REQUEST_CODE_EDIT_TASK_ITEM);

        //清空
        this.taskItemId = 0;
        this.taskItemViewType = 0;
    }

    /**
     * 刪除清單細項
     *
     * @param dialogInterface
     * @param which
     */
    private void deleteTaskItem(DialogInterface dialogInterface, int which) {

        if (this.taskItemViewType == MyViewHolderAdapter.NOTE_VIEW) {
            itineraryBo.removeNote(this.taskItemId);
        } else {
            itineraryBo.removeShopping(this.taskItemId);
        }

        //清空
        this.taskItemId = 0;
        this.taskItemViewType = 0;
    }

    /**
     * 設置提醒 xxx 等錄音檔好了再弄
     *
     * @param view
     */
    private void setReminder(View view) {


    }

    /**
     * 編輯或刪除行程
     *
     * @param view
     */
    private void editOrDeleteTask(View view) {
        new AlertDialog.Builder(this)
                .setMessage("編輯或刪除行程??")
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
        //設置交易管理
        SQLiteDatabase db = ItineraryDatabaseHelper.getInstance(this).getWritableDatabase();
        db.beginTransaction();

        /*
            開始操作DB
         */


        //移除task
        itineraryBo.removeTask(this.taskId);

        //移除提醒 xxx

        //移除note群
        Note keyNote = new Note();
        keyNote.setTaskId(this.taskId);
        Cursor noteCursorDelete = itineraryBo.findNoteList(keyNote);

        List<Integer> noteIdListDelete = new ArrayList<Integer>();
        while (noteCursorDelete.moveToNext()) {
            int noteId = noteCursorDelete.getInt(noteCursorDelete.getColumnIndexOrThrow("id"));
            noteIdListDelete.add(noteId);
        }
        itineraryBo.removeNoteList(noteIdListDelete);

        //移除shopping群
        Shopping keyShopping = new Shopping();
        keyShopping.setTaskId(this.taskId);
        Cursor shoppingCursorDelete = itineraryBo.findShoppingList(keyShopping);

        List<Integer> shoppingIdListDelete = new ArrayList<Integer>();
        while (shoppingCursorDelete.moveToNext()) {
            int shoppingId = shoppingCursorDelete.getInt(shoppingCursorDelete.getColumnIndexOrThrow("id"));
            shoppingIdListDelete.add(shoppingId);
        }
        itineraryBo.removeShoppingList(shoppingIdListDelete);

        db.setTransactionSuccessful();
        db.endTransaction();
    }

    /**
     * 編輯行程
     *
     * @param dialogInterface
     * @param which
     */
    private void editTask(DialogInterface dialogInterface, int which) {
        Intent intent = new Intent(this, CreateOrEditTaskActivity.class);
        intent.putExtra(GlobalNaming.TASK_ID, this.taskId);
        startActivityForResult(intent, REQUEST_CODE_EDIT_TASK);
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
        showListView(Pager.GO_FORWARD);
    }

    /**
     * 新增記事或購物事項
     *
     * @param view
     */
    private void fabAddNoteOrShopping(View view) {
        Intent intent = new Intent(this, CreateOrEditTaskItemActivity.class);
        intent.putExtra(GlobalNaming.TASK_ID, this.taskId);
        startActivityForResult(intent, REQUEST_CODE_CREATE_TASK_ITEM);
    }

    /**
     * 上一頁
     *
     * @param view
     */
    private void prePageTaskItem(View view) {
        showListView(Pager.GO_BACKWARD);
    }

    /**
     * 以分頁機制顯示清單
     *
     * @param isGoForward 是否前進或後退或原地不動
     */
    private void showListView(Boolean isGoForward) {

        //開始跟結束條件
        Pager pager = new Pager(this, PAGE_SIZE);
        List<Pager.TaskItemHolder> list = pager.getPagedTaskItemHolderListForViewHolder(this.taskId, this.currentPage, isGoForward);
        MyViewHolderAdapter adapter = new MyViewHolderAdapter(this, list);
        this.taskItemList.setAdapter(adapter);
    }

    /**
     * ViewHolder 專用的adapter
     */
    class MyViewHolderAdapter extends BaseAdapter {

        /*
            View類型
         */
        private static final int NOTE_VIEW = 0x0001;
        private static final int SHOPPING_VIEW = 0x0010;

        private List<Pager.TaskItemHolder> dataList;
        private Context context;

        private ViewHolder viewHolder;

        /**
         * @param dataList
         */
        public MyViewHolderAdapter(Context context, List<Pager.TaskItemHolder> dataList) {
            this.context = context;
            this.dataList = dataList;
        }

        /**
         * ViewHolder 內含兩種layout的所有欄位
         */
        class ViewHolder {
            /*
                Note 物件
             */
//            protected TextView noteId;
            protected TextView noteContent;
            protected TextView noteExplain;
            /*
                Shopping 物件
             */
//            protected TextView shoppingId;
            protected TextView name;
            protected TextView quantity;
            protected TextView unitPrice;
        }

        @Override
        public int getCount() {
            return this.dataList.size();
        }

        @Override
        public Pager.TaskItemHolder getItem(int position) {
            return this.dataList.get(position);
        }

        @Override
        public long getItemId(int position) {
            Pager.TaskItemHolder taskItemHolder = this.getItem(position);
            long noteId = taskItemHolder.note == null ? 0 : taskItemHolder.note.getId();
            long shoppingId = taskItemHolder.shopping == null ? 0 : taskItemHolder.shopping.getId();
            return noteId + shoppingId;
        }

        @Override
        public int getItemViewType(int position) {
            Pager.TaskItemHolder taskItemHolder = this.getItem(position);

            int viewType = 0;
            if (taskItemHolder.note != null && taskItemHolder.shopping == null) {
                viewType = NOTE_VIEW;
            } else {
                viewType = SHOPPING_VIEW;
            }
            return viewType;
        }

        @Override
        public int getViewTypeCount() {
            return 2;
        }

        @Override
        public View getView(int position, View view, ViewGroup parent) {

            if (view == null) {
                this.viewHolder = new ViewHolder();

                Pager.TaskItemHolder taskItemHolder = this.getItem(position);

                //區分項目layout
                int viewType = this.getItemViewType(position);
                switch (viewType) {

                    case NOTE_VIEW:
                        view = LayoutInflater.from(this.context).inflate(R.layout.embedding_task_detail_list_item_note, parent, false);
                        Note note = taskItemHolder.note;

//                        this.viewHolder.noteId = (TextView) view.findViewById(R.id.noteId);
                        this.viewHolder.noteContent = (TextView) view.findViewById(R.id.noteContent);
                        this.viewHolder.noteExplain = (TextView) view.findViewById(R.id.noteExplain);

//                        this.viewHolder.noteId.setText(note.getId());
                        this.viewHolder.noteContent.setText(note.getNoteContent());
                        this.viewHolder.noteExplain.setText(note.getNoteExplain());

                        break;

                    case SHOPPING_VIEW:
                        view = LayoutInflater.from(this.context).inflate(R.layout.embedding_task_detail_list_item_shopping, parent, false);
                        Shopping shopping = taskItemHolder.shopping;

//                        this.viewHolder.shoppingId = (TextView) view.findViewById(R.id.shoppingId);
                        this.viewHolder.name = (TextView) view.findViewById(R.id.name);
                        this.viewHolder.quantity = (TextView) view.findViewById(R.id.quantity);
                        this.viewHolder.unitPrice = (TextView) view.findViewById(R.id.unitPrice);

//                        this.viewHolder.shoppingId.setText(shopping.getId());
                        this.viewHolder.name.setText(shopping.getName());
                        this.viewHolder.quantity.setText(shopping.getQuantity());
                        this.viewHolder.unitPrice.setText(shopping.getUnitPrice().toString());

                        break;
                }
                view.setTag(this.viewHolder);
            } else {
//                this.viewHolder = (ViewHolder) view.getTag();
            }
            return view;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        switch (requestCode) {
            case REQUEST_CODE_EDIT_TASK:


                // xxx

                break;
            case REQUEST_CODE_CREATE_TASK_ITEM:
                break;
            case REQUEST_CODE_EDIT_TASK_ITEM:
                break;
            default:
                Log.d("onActivityResult 錯誤 -> ", "當前requestCode = " + requestCode);
        }
    }
}
