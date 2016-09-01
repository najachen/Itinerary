package silver.reminder.itinerary;

import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;

import silver.reminder.itinerary.dao.ItineraryDatabaseHelper;

/**
 * Created by StanleyCheng on 2016/9/1.
 */
public class Pager {

    public static final boolean GO_FORWARD = true;
    public static final boolean GO_BACKWARD = false;

    private Context context;
    private String tableName;
    private String conditionFieldName;
    private int pageSize;

    public Pager(Context context,
                 String tableName,
                 String conditionFieldName,
                 int pageSize) {
        this.context = context;
        this.tableName = tableName;
        this.conditionFieldName = conditionFieldName;
        this.pageSize = pageSize;
    }

    /**
     * 提供第Ｎ頁的內容
     *
     * @param conditionStart 搜尋條件開始值
     * @param conditionEnd   搜尋條件結束值
     * @param currentPage
     * @param isGoForward    前進或後退 若為空值則停在原地
     * @return listView 需要的 Cursor
     */
    public Cursor getPagedCursor(String conditionStart, String conditionEnd, int currentPage, Boolean isGoForward) {
        SQLiteDatabase db = ItineraryDatabaseHelper.getInstance(this.context).getReadableDatabase();

        //預設值設定
        conditionStart = conditionStart == null || conditionStart.length() == 0 ? "0" : conditionStart;
        conditionEnd = conditionEnd == null || conditionEnd.length() == 0 ? String.valueOf(Integer.MAX_VALUE) : conditionEnd;
        currentPage = currentPage == 0 ? 1 : currentPage;

        //判斷是否為第一頁或最後一頁
        String whereDesc = " where " + this.conditionFieldName + " >= ? and " + this.conditionFieldName + " <= ? ";
        long count = DatabaseUtils.queryNumEntries(db, this.tableName, whereDesc, new String[]{conditionStart, conditionEnd});
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
        String searchSql = "select * from " + this.tableName + whereDesc + "limit ? offset ?";
        Cursor cursor = db.rawQuery(searchSql, new String[]{conditionStart, conditionEnd, strDataNumStart, strDataOffset});

        return cursor;
    }
}
