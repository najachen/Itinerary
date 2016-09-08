package silver.reminder.itinerary;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AlertDialog.Builder;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import silver.reminder.itinerary.bo.ItineraryBo;
import silver.reminder.itinerary.bo.ItineraryBoImpl;
import silver.reminder.itinerary.bo.SoundDingDongBo;
import silver.reminder.itinerary.bo.SoundDingDongBoImpl;
import silver.reminder.itinerary.dao.ItineraryDatabaseHelper;
import silver.reminder.itinerary.model.Note;
import silver.reminder.itinerary.model.Schedule;
import silver.reminder.itinerary.model.Shopping;
import silver.reminder.itinerary.model.Task;
import silver.reminder.itinerary.util.GlobalNaming;
import silver.reminder.itinerary.util.Pager;

public class ListTaskItemActivity extends AppCompatActivity {

    private FloatingActionButton prePageTaskItem;
    private FloatingActionButton fabAddNoteOrShopping;
    private FloatingActionButton nextPageTaskItem;
    private Button backTaskList;
    private Button editOrDeleteTask;
    private ListView taskItemList;
    private TextView taskName;
    private TextView taskTime;
    private TextView taskSite;

    /*
       頁面物件
     */
    private Task task;

    /**
     * 這一頁被點擊的taskItemId(暫存)
     */
    private int taskItemId;
//    private int taskItemViewType;

    /**
     * 清單每頁幾筆資料
     */
    private static final int PAGE_SIZE = 5;
    public int currentPage = 1;
    //    public int currentPageShopping = 1;
    public boolean isTaskItemTypeNote = true;

    /**
     * bo
     */
    private ItineraryBo itineraryBo;
    private SoundDingDongBo soundDingDongBo;
    private Button switchListType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_task_item_activity);

        findViews();

        itineraryBo = ItineraryBoImpl.getInstance(this);
        soundDingDongBo = SoundDingDongBoImpl.getInstance(this);
    }

    @Override
    protected void onResume() {
        super.onResume();

        /*
            準備頁面物件
         */
        int taskId = getIntent().getIntExtra(GlobalNaming.TASK_ID, GlobalNaming.ERROR_CODE);

        //
        task = itineraryBo.findTaskById(taskId);
        task = task == null ? new Task() : task;

        //test-
//        Log.d("task name", String.valueOf(task.getName()));
//        Log.d("task time", String.valueOf(task.getTime()));
//        Log.d("task site", String.valueOf(task.getSite()));

        /*
            顯示明細與清單
         */
        Calendar taskCal = Calendar.getInstance();
        taskCal.setTimeInMillis(task.getTime() == null ? 0 : task.getTime());

        //顯示明細
        this.taskName.setText(task.getName());
        this.taskTime.setText(GlobalNaming.getDateString(taskCal) + GlobalNaming.SPACE + GlobalNaming.getTimeString(taskCal));
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

        switchListType = (Button) findViewById(R.id.switchListType);
        switchListType.setOnClickListener(this::switchListType);
        backTaskList = (Button) findViewById(R.id.backTaskList);
        backTaskList.setOnClickListener(this::backTaskList);
        editOrDeleteTask = (Button) findViewById(R.id.editOrDeleteTask);
        editOrDeleteTask.setOnClickListener(this::editOrDeleteTask);

        taskItemList = (ListView) findViewById(R.id.taskItemList);
        taskItemList.setOnItemClickListener(this::onTaskItemClick);

        taskName = (TextView) findViewById(R.id.taskName);
        taskTime = (TextView) findViewById(R.id.taskTime);
        taskSite = (TextView) findViewById(R.id.taskSite);
    }

    /**
     * @param view
     */
    private void switchListType(View view) {
        isTaskItemTypeNote = !isTaskItemTypeNote;

        if (isTaskItemTypeNote) {
            switchListType.setText(R.string.seeShoppingList);
        } else {
            switchListType.setText(R.string.seeNoteList);
        }
        showListView(null);
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

        if (cursor.moveToPosition(position)) {
            this.taskItemId = cursor.getInt(cursor.getColumnIndexOrThrow("_id"));
        }

        //test-
        Log.v("item click taskItemId", String.valueOf(taskItemId));

        new Builder(this)
                .setMessage(getString(R.string.queEditOrDelete))
                .setPositiveButton(getString(R.string.edit), ListTaskItemActivity.this::editTaskItem)
                .setNegativeButton(getString(R.string.delete), ListTaskItemActivity.this::deleteTaskItem)
                .setNeutralButton(getString(R.string.cancel), null)
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
        intent.putExtra(GlobalNaming.TASK_ITEM_VIEW_TYPE, isTaskItemTypeNote);
        startActivity(intent);

        //清空
        taskItemId = 0;
    }

    /**
     * 刪除清單細項
     *
     * @param dialogInterface
     * @param which
     */
    private void deleteTaskItem(DialogInterface dialogInterface, int which) {

        if (isTaskItemTypeNote) {
            itineraryBo.removeNote(this.taskItemId);
        } else {
            itineraryBo.removeShopping(this.taskItemId);
        }

        //清空
        taskItemId = 0;

        showListView(null);
    }

    /**
     * 編輯或刪除行程
     *
     * @param view
     */
    private void editOrDeleteTask(View view) {
        new AlertDialog.Builder(this)
                .setMessage(getString(R.string.queEditOrDelete))
                .setPositiveButton(getString(R.string.edit), ListTaskItemActivity.this::editTask)
                .setNegativeButton(getString(R.string.delete), ListTaskItemActivity.this::deleteTask)
                .setNeutralButton(getString(R.string.cancel), null)
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
        itineraryBo.removeTask(task.get_id());

        //移除提醒
        Schedule keySchedule = new Schedule();
        keySchedule.setTaskId(task.get_id());
        Cursor cursorSchedule = soundDingDongBo.findScheduleList(keySchedule);

        if (cursorSchedule.getCount() == 1 && cursorSchedule.moveToFirst()) {
            int scheduleId = cursorSchedule.getInt(cursorSchedule.getColumnIndexOrThrow("_id"));
            soundDingDongBo.removeSchedule(scheduleId);
        }

        //移除note群
        Note keyNote = new Note();
        keyNote.setTaskId(task.get_id());
        Cursor noteCursorDelete = itineraryBo.findNoteList(keyNote);

        List<Integer> noteIdListDelete = new ArrayList<Integer>();
        while (noteCursorDelete.moveToNext()) {
            int noteId = noteCursorDelete.getInt(noteCursorDelete.getColumnIndexOrThrow("_id"));
            noteIdListDelete.add(noteId);
        }
        itineraryBo.removeNoteList(noteIdListDelete);

        //移除shopping群
        Shopping keyShopping = new Shopping();
        keyShopping.setTaskId(task.get_id());
        Cursor shoppingCursorDelete = itineraryBo.findShoppingList(keyShopping);

        List<Integer> shoppingIdListDelete = new ArrayList<Integer>();
        while (shoppingCursorDelete.moveToNext()) {
            int shoppingId = shoppingCursorDelete.getInt(shoppingCursorDelete.getColumnIndexOrThrow("_id"));
            shoppingIdListDelete.add(shoppingId);
        }
        itineraryBo.removeShoppingList(shoppingIdListDelete);

        db.setTransactionSuccessful();
        db.endTransaction();

        finish();
    }

    /**
     * 編輯行程
     *
     * @param dialogInterface
     * @param which
     */
    private void editTask(DialogInterface dialogInterface, int which) {
        Intent intent = new Intent(this, CreateOrEditTaskActivity.class);
        intent.putExtra(GlobalNaming.TASK_ID, task.get_id());
        startActivity(intent);
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
        intent.putExtra(GlobalNaming.TASK_ID, task.get_id());
        startActivity(intent);
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

        int taskId = task.get_id() == null ? 0 : task.get_id();
        //開始跟結束條件
        Pager pager = new Pager(this, PAGE_SIZE);
        SimpleCursorAdapter adapter = pager.getPagedAdapterForTaskItem(taskId, isGoForward);
        taskItemList.setAdapter(adapter);
    }
}

/**
 * ViewHolder 專用的adapter
 */
//    class MyViewHolderAdapter extends BaseAdapter {
//
//        /*
//            View類型
//         */
//        public static final int VIEW_NOTE = 0x0001;
//        public static final int VIEW_SHOPPING = 0x0010;
//
//        private List<Pager.TaskItemHolder> dataList;
//        private Context context;
//
//        private ViewHolder viewHolder;
//
//        /**
//         * @param dataList
//         */
//        public MyViewHolderAdapter(Context context, List<Pager.TaskItemHolder> dataList) {
//            this.context = context;
//            this.dataList = dataList;
//        }
//
//        /**
//         * ViewHolder 內含兩種layout的所有欄位
//         */
//        class ViewHolder {
//            /*
//                Note 物件
//             */
////            protected TextView noteId;
//            protected TextView noteContent;
//            protected TextView noteExplain;
//            /*
//                Shopping 物件
//             */
////            protected TextView shoppingId;
//            protected TextView name;
//            protected TextView quantity;
//            protected TextView unitPrice;
//        }
//
//        @Override
//        public int getCount() {
//            return this.dataList.size();
//        }
//
//        @Override
//        public Pager.TaskItemHolder getItem(int position) {
//            return this.dataList.get(position);
//        }
//
//        @Override
//        public long getItemId(int position) {
//            Pager.TaskItemHolder taskItemHolder = this.getItem(position);
//            long noteId = taskItemHolder.note == null ? 0 : taskItemHolder.note.get_id();
//            long shoppingId = taskItemHolder.shopping == null ? 0 : taskItemHolder.shopping.get_id();
//            return noteId + shoppingId;
//        }
//
//        @Override
//        public int getItemViewType(int position) {
//            Pager.TaskItemHolder taskItemHolder = this.getItem(position);
//
//            int viewType = 0;
//            if (taskItemHolder.note != null && taskItemHolder.shopping == null) {
//                viewType = VIEW_NOTE;
//            } else {
//                viewType = VIEW_SHOPPING;
//            }
//            return viewType;
//        }
//
//        @Override
//        public int getViewTypeCount() {
//            return 2;
//        }
//
//        @Override
//        public View getView(int position, View view, ViewGroup parent) {
//
//            if (view == null) {
//                this.viewHolder = new ViewHolder();
//
//                Pager.TaskItemHolder taskItemHolder = this.getItem(position);
//
//                //區分項目layout
//                int viewType = this.getItemViewType(position);
//                switch (viewType) {
//
//                    case VIEW_NOTE:
//                        view = LayoutInflater.from(this.context).inflate(R.layout.embedding_task_detail_list_item_note, parent, false);
//                        Note note = taskItemHolder.note;
//
////                        this.viewHolder.noteId = (TextView) view.findViewById(R.id.noteId);
//                        this.viewHolder.noteContent = (TextView) view.findViewById(R.id.noteContent);
//                        this.viewHolder.noteExplain = (TextView) view.findViewById(R.id.noteExplain);
//
////                        this.viewHolder.noteId.setText(note.getId());
//                        this.viewHolder.noteContent.setText(note.getNoteContent());
//                        this.viewHolder.noteExplain.setText(note.getNoteExplain());
//
//                        break;
//
//                    case VIEW_SHOPPING:
//                        view = LayoutInflater.from(this.context).inflate(R.layout.embedding_task_detail_list_item_shopping, parent, false);
//                        Shopping shopping = taskItemHolder.shopping;
//
////                        this.viewHolder.shoppingId = (TextView) view.findViewById(R.id.shoppingId);
//                        this.viewHolder.name = (TextView) view.findViewById(R.id.name);
//                        this.viewHolder.quantity = (TextView) view.findViewById(R.id.quantity);
//                        this.viewHolder.unitPrice = (TextView) view.findViewById(R.id.unitPrice);
//
////                        this.viewHolder.shoppingId.setText(shopping.getId());
//                        this.viewHolder.name.setText(shopping.getName());
//                        this.viewHolder.quantity.setText(shopping.getQuantity().toString());
//                        this.viewHolder.unitPrice.setText(shopping.getUnitPrice().toString());
//
//                        break;
//                }
//                view.setTag(this.viewHolder);
//            } else {
////                this.viewHolder = (ViewHolder) view.getTag();
//            }
//            return view;
//        }
//    }
//}
