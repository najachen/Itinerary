package silver.reminder.itinerary.util;

import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import silver.reminder.itinerary.bo.ItineraryBo;
import silver.reminder.itinerary.bo.ItineraryBoImpl;
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

    private Context context;
    private int pageSize;

    public Pager(Context context, int pageSize) {
        this.context = context;
        this.pageSize = pageSize;
    }

    /**
     * 提供第Ｎ頁的內容(日期搜尋專用)
     *
     * @param conditionStart 搜尋條件開始值
     * @param conditionEnd   搜尋條件結束值
     * @param currentPage    當前在第幾頁
     * @param isGoForward    前進或後退 若為空值則停在原地
     * @return listView 需要的 Cursor
     */
    public Cursor getPagedCursorBySearchingCondition(String tableName, String conditionFieldName, String conditionStart, String conditionEnd, int currentPage, Boolean isGoForward) {
        SQLiteDatabase db = ItineraryDatabaseHelper.getInstance(this.context).getReadableDatabase();

        //預設值設定
        conditionStart = conditionStart == null || conditionStart.length() == 0 ? "0" : conditionStart;
        conditionEnd = conditionEnd == null || conditionEnd.length() == 0 ? String.valueOf(Integer.MAX_VALUE) : conditionEnd;
        currentPage = currentPage == 0 ? 1 : currentPage;

        //判斷是否為第一頁或最後一頁
        String whereDesc = " where " + conditionFieldName + " >= ? and " + conditionFieldName + " <= ? ";
        long count = DatabaseUtils.queryNumEntries(db, tableName, whereDesc, new String[]{conditionStart, conditionEnd});
        int totalPageNum = (int) count / this.pageSize + 1;

        int targetPage = 0;

        if (isGoForward != null) {
            if (isGoForward && currentPage < totalPageNum) { //下一頁
                targetPage = currentPage + 1;
            } else if (!isGoForward && currentPage > 1) { //上一頁
                targetPage = currentPage - 1;
            }
        } else {
            targetPage = currentPage;
        }

        //當頁開始與結束是第幾筆資料
        int intDataNumStart = ((targetPage - 1) * this.pageSize) + 1;
        String strDataNumStart = String.valueOf(intDataNumStart);
        int intDataOffset = this.pageSize - 1;
        String strDataOffset = String.valueOf(intDataOffset);

        //開始搜尋資料
        String searchSql = "select * from " + tableName + whereDesc + "limit ? offset ?";
        Cursor cursor = db.rawQuery(searchSql, new String[]{conditionStart, conditionEnd, strDataNumStart, strDataOffset});

        return cursor;
    }

    /**
     * 提供第Ｎ頁的內容(ViewHolder專用)
     *
     * @param taskId      行程Id 若為0回傳null
     * @param currentPage 當前在第幾頁 若為0 當作是第一頁
     * @param isGoForward 是否前進或後退(若為空值則不動)
     * @return 各資料表搜尋出來的資料List 內部的成員欄位全為 model類別
     */
    public List<TaskItemHolder> getPagedTaskItemHolderListForViewHolder(int taskId, int currentPage, Boolean isGoForward) {

        /*
            初始值處理
         */
        if (taskId == 0) {
            return null;
        }
        currentPage = currentPage == 0 ? 1 : currentPage;

        /*
            開始分頁
         */
        //取得資料
        ItineraryBo itineraryBo = ItineraryBoImpl.getInstance(this.context);

        Note keyNote = new Note();
        keyNote.setTaskId(taskId);
        Cursor cursorNote = itineraryBo.findNoteList(keyNote);

        Shopping keyShopping = new Shopping();
        keyShopping.setTaskId(taskId);
        Cursor cursorShopping = itineraryBo.findShoppingList(keyShopping);

        //當前頁面與翻頁問題
        int cursorNoteCount = cursorNote.getCount();
        int cursorShoppingCount = cursorShopping.getCount();
        int itemCount = cursorNoteCount + cursorShoppingCount;
        int totalPageNum = itemCount / this.pageSize + 1;

        int targetPage = 1;
        if (isGoForward != null) {
            if (isGoForward && currentPage < totalPageNum) {
                targetPage = currentPage + 1;
            } else if (!isGoForward && currentPage > 1) {
                targetPage = currentPage - 1;
            }
        } else {
            targetPage = currentPage;
        }

        int pageDataStartIndex = (targetPage - 1) * this.pageSize + 1;
        int pageDataEndIndex = pageDataStartIndex + this.pageSize - 1;

        /*
            將需要的資料裝箱 記事先裝 購物後裝
         */
        List<TaskItemHolder> taskItemHolderList = new ArrayList<TaskItemHolder>();

        //裝箱規畫
        if (pageDataStartIndex < cursorNoteCount && pageDataEndIndex <= cursorNoteCount) { //僅需打包 note cursor

            boolean isMoveToPositionSuccess = cursorNote.moveToPosition(pageDataStartIndex - 1);

            while (isMoveToPositionSuccess && cursorNote.getPosition() <= pageDataEndIndex && cursorNote.moveToNext()) {
                TaskItemHolder taskItemHolder = new TaskItemHolder();
                taskItemHolder.note = cursorToNote(cursorNote);
                taskItemHolderList.add(taskItemHolder);
            }

        } else if (pageDataStartIndex <= cursorNoteCount && pageDataEndIndex > cursorNoteCount) { //兩個cursor都要打包
            boolean isMoveToPositionSuccess = cursorNote.moveToPosition(pageDataStartIndex - 1);

            while (isMoveToPositionSuccess && cursorNote.moveToNext()) {

                TaskItemHolder taskItemHolder = new TaskItemHolder();
                taskItemHolder.note = cursorToNote(cursorNote);
                taskItemHolderList.add(taskItemHolder);
            }

            //shopping 應該存幾筆??
            int shoppingDataCount = pageDataEndIndex - cursorNoteCount;

            while (cursorShopping.getPosition() <= shoppingDataCount && cursorShopping.moveToNext()) {
                TaskItemHolder taskItemHolder = new TaskItemHolder();
                taskItemHolder.shopping = cursorToShopping(cursorShopping);
                taskItemHolderList.add(taskItemHolder);
            }

        } else if (pageDataStartIndex > cursorNoteCount) { //僅打包 shopping cursor
            int innerCursorStart = pageDataStartIndex - cursorNoteCount;
            int innerCursorEnd = pageDataEndIndex - cursorNoteCount;

            boolean isMoveToPositionSuccess = cursorShopping.moveToPosition(innerCursorStart - 1);

            while (isMoveToPositionSuccess && cursorShopping.getPosition() <= innerCursorEnd && cursorShopping.moveToNext()) {
                TaskItemHolder taskItemHolder = new TaskItemHolder();
                taskItemHolder.shopping = cursorToShopping(cursorShopping);
                taskItemHolderList.add(taskItemHolder);
            }
        }
        return taskItemHolderList;
    }

    /**
     * for viewHolder 使用
     */
    public static class TaskItemHolder {
        public Note note;
        public Shopping shopping;
    }

    /**
     * cursor 轉 Note
     *
     * @param cursor
     * @return Note 物件
     */
    private static Note cursorToNote(Cursor cursor) {
        Note result = new Note();

        int id = cursor.getInt(cursor.getColumnIndexOrThrow("id"));
        int taskId = cursor.getInt(cursor.getColumnIndexOrThrow("taskId"));
        String noteContent = cursor.getString(cursor.getColumnIndexOrThrow("noteContent"));
        String noteExplain = cursor.getString(cursor.getColumnIndexOrThrow("noteExplain"));

        result.setId(id);
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

        int id = cursor.getInt(cursor.getColumnIndexOrThrow("id"));
        int taskId = cursor.getInt(cursor.getColumnIndexOrThrow("taskId"));
        String name = cursor.getString(cursor.getColumnIndexOrThrow("name"));
        int quantity = cursor.getInt(cursor.getColumnIndexOrThrow("quantity"));
        float unitPrice = cursor.getFloat(cursor.getColumnIndexOrThrow("unitPrice"));

        result.setId(id);
        result.setTaskId(taskId);
        result.setName(name);
        result.setQuantity(quantity);
        result.setUnitPrice(unitPrice);

        return result;
    }
}
