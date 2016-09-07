package silver.reminder.itinerary.util;

import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.widget.SimpleCursorAdapter;

import java.util.Calendar;

import silver.reminder.itinerary.ListSoundFileActivity;
import silver.reminder.itinerary.ListTaskActivity;
import silver.reminder.itinerary.ListTaskItemActivity;
import silver.reminder.itinerary.R;
import silver.reminder.itinerary.dao.ItineraryDatabaseHelper;
import silver.reminder.itinerary.model.Note;
import silver.reminder.itinerary.model.Shopping;

/**
 * 分頁器
 * Created by StanleyCheng on 2016/9/1.
 */
public class Pager {

    public static final boolean GO_FORWARD = true;
    public static final boolean GO_BACKWARD = false;

    private int pageSize;
    private Context context;
    private final SQLiteDatabase db;

    public Pager(Context context, int pageSize) {
        this.context = context;
        this.pageSize = pageSize;
        this.db = ItineraryDatabaseHelper.getInstance(context).getReadableDatabase();
    }

    /**
     * 提供第Ｎ頁的內容(日期搜尋專用)
     *
     * @param startCal
     * @param endCal
     * @param isGoForward
     * @return listView 需要的 Cursor
     */
    public Cursor getPagedCursorForTask(Calendar startCal, Calendar endCal, Boolean isGoForward) {
        ListTaskActivity activity = (ListTaskActivity) this.context;
        String tableName = "task";

        //預設值設定
        if (startCal == null || endCal == null) {
            Log.d("task 分頁條件不完全", startCal == null ? "startCal null" : "endCal null");
            return null;
        }

        //where 敘述
        String whereDesc = " time >= ? and time <= ? ";
        String strStartCal = String.valueOf(startCal.getTimeInMillis());
        String strEndCal = String.valueOf(endCal.getTimeInMillis());

        long count = DatabaseUtils.queryNumEntries(db, tableName, whereDesc, new String[]{strStartCal, strEndCal});

        int totalPageNum = 0;
        if (count % this.pageSize == 0) {
            totalPageNum = (int) count / this.pageSize;
        } else {
            totalPageNum = (int) count / this.pageSize + 1;
        }

        //test-
//        Log.d("strStartCal---", GlobalNaming.getDateString(startCal) + " " + GlobalNaming.getTimeString(startCal));
//        Log.d("strEndCal---", GlobalNaming.getDateString(endCal) + " " + GlobalNaming.getTimeString(endCal));
//        Log.d("count---", String.valueOf(count));
//        Log.d("totalPageNum---", String.valueOf(totalPageNum));

        if (isGoForward != null) {
            if (isGoForward && activity.currentPage < totalPageNum) { //下一頁
                activity.currentPage++;
            } else if (!isGoForward && activity.currentPage > 1) { //上一頁
                activity.currentPage--;
            }
        }

        //當頁開始與結束是第幾筆資料
        String strPageSize = String.valueOf(this.pageSize);
        int intDataStartPosition = (activity.currentPage - 1) * this.pageSize;
        String strDataStartPosition = String.valueOf(intDataStartPosition);

        //開始搜尋資料
        String searchSql = "select * from " + tableName + " where " + whereDesc + "limit ? offset ?";
        Cursor cursor = db.rawQuery(searchSql, new String[]{strStartCal, strEndCal, strPageSize, strDataStartPosition});

        //test-
//        Log.d("searchSql---", searchSql);
//        Log.d("currentPage---", String.valueOf(activity.currentPage));
//        Log.d("isGoForward null---", String.valueOf(isGoForward == null));
//        Log.d("isGoForward value---", String.valueOf(isGoForward));
//        Log.d("strPageSize---", strPageSize);
//        Log.d("strDataStartPosition---", strDataStartPosition);
//        Log.d("cursor.getCount()---", String.valueOf(cursor.getCount()));

        return cursor;
    }

    /**
     * @param isGoForward
     * @return int startId, int endId,
     */
    public Cursor getPagedCursorForMusicList(Boolean isGoForward) {

        ListSoundFileActivity activity = (ListSoundFileActivity) this.context;
        String tableName = "soundFile";

        long count = DatabaseUtils.queryNumEntries(db, tableName, null, null);

        int totalPageNum = 0;
        if (count % this.pageSize == 0) {
            totalPageNum = (int) count / this.pageSize;
        } else {
            totalPageNum = (int) count / this.pageSize + 1;
        }

        //test-
//        Log.d("count---", String.valueOf(count));
//        Log.d("totalPageNum---", String.valueOf(totalPageNum));

        if (isGoForward != null) {
            if (isGoForward && activity.currentPage < totalPageNum) { //下一頁
                activity.currentPage++;
            } else if (!isGoForward && activity.currentPage > 1) { //上一頁
                activity.currentPage--;
            }
        }

        //當頁開始與結束是第幾筆資料
        String strPageSize = String.valueOf(this.pageSize);
        int intDataStartPosition = (activity.currentPage - 1) * this.pageSize;
        String strDataStartPosition = String.valueOf(intDataStartPosition);

        //開始搜尋資料
        String searchSql = "select * from " + tableName + " limit ? offset ?";
        Cursor cursor = db.rawQuery(searchSql, new String[]{strPageSize, strDataStartPosition});

        //test-
//        Log.d("searchSql---", searchSql);
//        Log.d("currentPage---", String.valueOf(activity.currentPage));
//        Log.d("isGoForward null---", String.valueOf(isGoForward == null));
//        Log.d("isGoForward value---", String.valueOf(isGoForward));
//        Log.d("strPageSize---", strPageSize);
//        Log.d("strDataStartPosition---", strDataStartPosition);
//        Log.d("cursor.getCount()---", String.valueOf(cursor.getCount()));

        return cursor;
    }

    /**
     * 提供第Ｎ頁的內容
     *
     * @param taskId      行程Id 若為0回傳null
     * @param isGoForward 是否前進或後退(若為空值則不動)
     * @return 各資料表搜尋出來的資料List 內部的成員欄位全為 model類別
     */
    public SimpleCursorAdapter getPagedAdapterForTaskItem(int taskId, Boolean isGoForward) {

        //預設值設定
        if (taskId == 0) {
            Log.d("taskItem task id - ", String.valueOf(taskId));
            return null;
        }

        ListTaskItemActivity activity = (ListTaskItemActivity) this.context;

        String tableName = null;
        if (activity.isTaskItemTypeNote) {
            tableName = "note";
        } else {
            tableName = "shopping";
        }

        //where 敘述
        String whereDesc = " taskId = ? ";
        String strTaskId = String.valueOf(taskId);

        long count = DatabaseUtils.queryNumEntries(db, tableName, whereDesc, new String[]{strTaskId});

        int totalPageNum = 0;
        if (count % this.pageSize == 0) {
            totalPageNum = (int) count / this.pageSize;
        } else {
            totalPageNum = (int) count / this.pageSize + 1;
        }

        //test-
//        Log.d("tableName---", tableName);
//        Log.d("strTaskId---", strTaskId);
//        Log.d("count---", String.valueOf(count));
//        Log.d("totalPageNum---", String.valueOf(totalPageNum));

        if (isGoForward != null) {
            if (isGoForward && activity.currentPage < totalPageNum) { //下一頁
                activity.currentPage++;
            } else if (!isGoForward && activity.currentPage > 1) { //上一頁
                activity.currentPage--;
            }
        }

        //當頁開始與結束是第幾筆資料
        String strPageSize = String.valueOf(this.pageSize);
        int intDataStartPosition = (activity.currentPage - 1) * this.pageSize;
        String strDataStartPosition = String.valueOf(intDataStartPosition);

        //開始搜尋資料
        String searchSql = "select * from " + tableName + " where " + whereDesc + "limit ? offset ?";
        Cursor cursor = db.rawQuery(searchSql, new String[]{strTaskId, strPageSize, strDataStartPosition});

        //test-
//        Log.d("searchSql---", searchSql);
//        Log.d("currentPage---", String.valueOf(activity.currentPage));
//        Log.d("isGoForward null---", String.valueOf(isGoForward == null));
//        Log.d("isGoForward value---", String.valueOf(isGoForward));
//        Log.d("strPageSize---", strPageSize);
//        Log.d("strDataStartPosition---", strDataStartPosition);
//        Log.d("cursor.getCount()---", String.valueOf(cursor.getCount()));

        SimpleCursorAdapter result = null;

        if(activity.isTaskItemTypeNote){
            result = new SimpleCursorAdapter(this.context
                    , R.layout.embedding_task_detail_list_item_note
                    , cursor
                    , new String[]{"noteContent", "noteExplain"}
                    , new int[]{R.id.noteContent, R.id.noteExplain}
                    , 0);
        }else{
            result = new SimpleCursorAdapter(this.context
                    , R.layout.embedding_task_detail_list_item_shopping
                    , cursor
                    , new String[]{"name", "quantity", "unitPrice"}
                    , new int[]{R.id.name, R.id.quantity, R.id.unitPrice}
                    , 0);
        }

        return result;


        /*
            以下是原本雙cursor的分頁機制 因開發失敗棄用
            =======================================================================================
         */
        /*
            初始值處理
         */
//        if (taskId == 0) {
//            return null;
//        }

        /*
            開始分頁
         */
        //取得資料
//        ItineraryBo itineraryBo = ItineraryBoImpl.getInstance(this.context);

//        Note keyNote = new Note();
//        keyNote.setTaskId(taskId);
//        Cursor cursorNote = itineraryBo.findNoteList(keyNote);
//
//        Shopping keyShopping = new Shopping();
//        keyShopping.setTaskId(taskId);
//        Cursor cursorShopping = itineraryBo.findShoppingList(keyShopping);


//        String strTaskId = String.valueOf(taskId);
//        Cursor cursorNote = db.rawQuery("select * from note where taskId = ?", new String[]{strTaskId});
//        Cursor cursorShopping = db.rawQuery("select * from shopping where taskId = ?", new String[]{strTaskId});
//
//        //當前頁面與翻頁問題
//        int cursorNoteCount = cursorNote.getCount();
//        int cursorShoppingCount = cursorShopping.getCount();
//        int count = cursorNoteCount + cursorShoppingCount;


//        int totalPageNum = 0;
//        if (count % this.pageSize == 0) {
//            totalPageNum = (int) count / this.pageSize;
//        } else {
//            totalPageNum = (int) count / this.pageSize + 1;
//        }
//
//        if (isGoForward != null) {
//            if (isGoForward && activity.currentPage < totalPageNum) {
//                activity.currentPage++;
//            } else if (!isGoForward && activity.currentPage > 1) {
//                activity.currentPage--;
//            }
//        }
//
//        int pageDataStartIndex = (activity.currentPage - 1) * this.pageSize + 1;
//        int pageDataEndIndex = pageDataStartIndex + this.pageSize - 1;


        /*
            將需要的資料裝箱 記事先裝 購物後裝
         */
//        List<TaskItemHolder> taskItemHolderList = new ArrayList<TaskItemHolder>();
//
//        //裝箱規畫
//        if (pageDataStartIndex < cursorNoteCount && pageDataEndIndex <= cursorNoteCount) { //僅需打包 note cursor
//
//            boolean isMoveToPositionSuccess = cursorNote.moveToPosition(pageDataStartIndex - 1);
//
//            while (isMoveToPositionSuccess && cursorNote.getPosition() <= pageDataEndIndex && cursorNote.moveToNext()) {
//                TaskItemHolder taskItemHolder = new TaskItemHolder();
//                taskItemHolder.note = cursorToNote(cursorNote);
//                taskItemHolderList.add(taskItemHolder);
//            }
//
//        } else if (pageDataStartIndex <= cursorNoteCount && pageDataEndIndex > cursorNoteCount) { //兩個cursor都要打包
//            boolean isMoveToPositionSuccess = cursorNote.moveToPosition(pageDataStartIndex - 1);
//
//            while (isMoveToPositionSuccess && cursorNote.moveToNext()) {
//
//                TaskItemHolder taskItemHolder = new TaskItemHolder();
//                taskItemHolder.note = cursorToNote(cursorNote);
//                taskItemHolderList.add(taskItemHolder);
//            }
//
//            //shopping 應該存幾筆??
//            int shoppingDataCount = pageDataEndIndex - cursorNoteCount;
//
//            while (cursorShopping.getPosition() <= shoppingDataCount && cursorShopping.moveToNext()) {
//                TaskItemHolder taskItemHolder = new TaskItemHolder();
//                taskItemHolder.shopping = cursorToShopping(cursorShopping);
//                taskItemHolderList.add(taskItemHolder);
//            }
//
//        } else if (pageDataStartIndex > cursorNoteCount) { //僅打包 shopping cursor
//            int innerCursorStart = pageDataStartIndex - cursorNoteCount;
//            int innerCursorEnd = pageDataEndIndex - cursorNoteCount;
//
//            boolean isMoveToPositionSuccess = cursorShopping.moveToPosition(innerCursorStart - 1);
//
//            while (isMoveToPositionSuccess && cursorShopping.getPosition() <= innerCursorEnd && cursorShopping.moveToNext()) {
//                TaskItemHolder taskItemHolder = new TaskItemHolder();
//                taskItemHolder.shopping = cursorToShopping(cursorShopping);
//                taskItemHolderList.add(taskItemHolder);
//            }
//        }
//        return taskItemHolderList;
    }

    /**
     * for viewHolder 使用
     */
//    public static class TaskItemHolder {
//        public Note note;
//        public Shopping shopping;
//    }

    /**
     * cursor 轉 Note
     *
     * @param cursor
     * @return Note 物件
     */
    private static Note cursorToNote(Cursor cursor) {
        Note result = new Note();

        int id = cursor.getInt(cursor.getColumnIndexOrThrow("_id"));
        int taskId = cursor.getInt(cursor.getColumnIndexOrThrow("taskId"));
        String noteContent = cursor.getString(cursor.getColumnIndexOrThrow("noteContent"));
        String noteExplain = cursor.getString(cursor.getColumnIndexOrThrow("noteExplain"));

        result.set_id(id);
        result.setTaskId(taskId);
        result.setNoteContent(noteContent);
        result.setNoteExplain(noteExplain);

        return result;
    }

    /**
     * cursor 轉 Shopping
     *
     * @param cursor
     * @return Shopping 物件
     */
    private static Shopping cursorToShopping(Cursor cursor) {
        Shopping result = new Shopping();

        int id = cursor.getInt(cursor.getColumnIndexOrThrow("_id"));
        int taskId = cursor.getInt(cursor.getColumnIndexOrThrow("taskId"));
        String name = cursor.getString(cursor.getColumnIndexOrThrow("name"));
        int quantity = cursor.getInt(cursor.getColumnIndexOrThrow("quantity"));
        float unitPrice = cursor.getFloat(cursor.getColumnIndexOrThrow("unitPrice"));

        result.set_id(id);
        result.setTaskId(taskId);
        result.setName(name);
        result.setQuantity(quantity);
        result.setUnitPrice(unitPrice);

        return result;
    }
}
