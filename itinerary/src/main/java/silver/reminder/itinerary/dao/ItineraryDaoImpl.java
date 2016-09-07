package silver.reminder.itinerary.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import silver.reminder.itinerary.model.Note;
import silver.reminder.itinerary.model.Task;
import silver.reminder.itinerary.model.Shopping;
/**
 * Wed Sep 07 07:39:58 CST 2016 by freemarker template
 */
public class ItineraryDaoImpl implements ItineraryDao {

    /**
     * 獨體模式
     */
    private static ItineraryDao itineraryDao;

    private ItineraryDaoImpl(Context context) {
        SQLiteOpenHelper dbHelper = ItineraryDatabaseHelper.getInstance(context);
        readableDB = dbHelper.getReadableDatabase();
        writableDB = dbHelper.getWritableDatabase();
    }

    public static ItineraryDao getInstance(Context context) {
        if (itineraryDao == null) {
            itineraryDao = new ItineraryDaoImpl(context);
        }
        return itineraryDao;
    }

    private SQLiteDatabase readableDB;
    private SQLiteDatabase writableDB;

    /*
        方法寫在下面 ======================================================
     */
    @Override
    public long insertNote(Note note) {

        ContentValues contentValues = new ContentValues();
        contentValues.put("noteContent", note.getNoteContent());
        contentValues.put("noteExplain", note.getNoteExplain());
        contentValues.put("taskId", note.getTaskId());
        long rowId = this.writableDB.insert("note", null, contentValues);
        return rowId;
    }

    @Override
    public void insertNoteList(List<Note> noteList) {

        this.writableDB.beginTransaction();

        for (Note note : noteList) {
            this.insertNote(note);
        }
        this.writableDB.setTransactionSuccessful();
        this.writableDB.endTransaction();
    }

    @Override
    public int updateNote(Note note) {

        //這裡是要修改的資料
        ContentValues contentValues = new ContentValues();
        contentValues.put("noteContent", note.getNoteContent());
        contentValues.put("noteExplain", note.getNoteExplain());
        contentValues.put("taskId", note.getTaskId());
        int updateDataAmount = this.writableDB.updateWithOnConflict("note", contentValues, "_id = ?", new String[]{note.get_id().toString()}, SQLiteDatabase.CONFLICT_ROLLBACK);
        return updateDataAmount;
    }

    @Override
    public void updateNoteList(List<Note> noteList) {

        this.writableDB.beginTransaction();

        for (Note note : noteList) {
            this.updateNote(note);
        }
        this.writableDB.setTransactionSuccessful();
        this.writableDB.endTransaction();
    }

    @Override
    public void deleteNote(Integer _id) {

        int deleteDataAmount = this.writableDB.delete("note", "_id = ?", new String[]{_id.toString()});
        if (deleteDataAmount > 1) {
            Log.d("移除 note 異常!! 移除了 ", deleteDataAmount + " 筆");
        }
    }

    @Override
    public void deleteNoteList(List<Integer> noteIdList) {

        this.writableDB.beginTransaction();

        for (Integer noteId : noteIdList) {
            this.deleteNote(noteId);
        }
        this.writableDB.setTransactionSuccessful();
        this.writableDB.endTransaction();
    }

    @Override
    public Note selectNoteById(Integer _id) {

        Cursor noteCursor = this.readableDB.rawQuery("select * from note where _id = ?", new String[]{_id.toString()});

        if(noteCursor.getCount() == 0 || !noteCursor.moveToFirst()){
            return null;
        }

        Note result = new Note();

        String[] columnNames = noteCursor.getColumnNames();
        for (String columnName : columnNames) {
            int columnIndex = noteCursor.getColumnIndexOrThrow(columnName);

            switch (columnName) {
                case "noteContent":
                    result.setNoteContent(noteCursor.getString(columnIndex));
                    break;
                case "noteExplain":
                    result.setNoteExplain(noteCursor.getString(columnIndex));
                    break;
                case "_id":
                    result.set_id(noteCursor.getInt(columnIndex));
                    break;
                case "taskId":
                    result.setTaskId(noteCursor.getInt(columnIndex));
                    break;
                default:
                    Log.d("出現欄位名稱錯誤 => ", columnName + columnIndex);
            }
        }
        return result;
    }

    @Override
    public Cursor selectNoteList(Note note) {

        StringBuffer sqlWhere = new StringBuffer();
        sqlWhere.append(" 1=1 ");

        List<String> whereArgs = new ArrayList<String>();

        String noteContent = note.getNoteContent();
        if (noteContent != null && noteContent.toString().length() > 0) {
            sqlWhere.append(" and noteContent = ? ");
            whereArgs.add(noteContent.toString());
        }
        String noteExplain = note.getNoteExplain();
        if (noteExplain != null && noteExplain.toString().length() > 0) {
            sqlWhere.append(" and noteExplain = ? ");
            whereArgs.add(noteExplain.toString());
        }
        Integer _id = note.get_id();
        if (_id != null && _id.toString().length() > 0) {
            sqlWhere.append(" and _id = ? ");
            whereArgs.add(_id.toString());
        }
        Integer taskId = note.getTaskId();
        if (taskId != null && taskId.toString().length() > 0) {
            sqlWhere.append(" and taskId = ? ");
            whereArgs.add(taskId.toString());
        }
        String[] whereArgsStringArray = new String[whereArgs.size()];
        whereArgsStringArray = whereArgs.toArray(whereArgsStringArray);

        Cursor result = this.readableDB.query("note", null, sqlWhere.toString(), whereArgsStringArray, null, null, "_id");
        return result;
    }

    @Override
    public long insertTask(Task task) {

        ContentValues contentValues = new ContentValues();
        contentValues.put("site", task.getSite());
        contentValues.put("name", task.getName());
        contentValues.put("time", task.getTime());
        long rowId = this.writableDB.insert("task", null, contentValues);
        return rowId;
    }

    @Override
    public void insertTaskList(List<Task> taskList) {

        this.writableDB.beginTransaction();

        for (Task task : taskList) {
            this.insertTask(task);
        }
        this.writableDB.setTransactionSuccessful();
        this.writableDB.endTransaction();
    }

    @Override
    public int updateTask(Task task) {

        //這裡是要修改的資料
        ContentValues contentValues = new ContentValues();
        contentValues.put("site", task.getSite());
        contentValues.put("name", task.getName());
        contentValues.put("time", task.getTime());
        int updateDataAmount = this.writableDB.updateWithOnConflict("task", contentValues, "_id = ?", new String[]{task.get_id().toString()}, SQLiteDatabase.CONFLICT_ROLLBACK);
        return updateDataAmount;
    }

    @Override
    public void updateTaskList(List<Task> taskList) {

        this.writableDB.beginTransaction();

        for (Task task : taskList) {
            this.updateTask(task);
        }
        this.writableDB.setTransactionSuccessful();
        this.writableDB.endTransaction();
    }

    @Override
    public void deleteTask(Integer _id) {

        int deleteDataAmount = this.writableDB.delete("task", "_id = ?", new String[]{_id.toString()});
        if (deleteDataAmount > 1) {
            Log.d("移除 task 異常!! 移除了 ", deleteDataAmount + " 筆");
        }
    }

    @Override
    public void deleteTaskList(List<Integer> taskIdList) {

        this.writableDB.beginTransaction();

        for (Integer taskId : taskIdList) {
            this.deleteTask(taskId);
        }
        this.writableDB.setTransactionSuccessful();
        this.writableDB.endTransaction();
    }

    @Override
    public Task selectTaskById(Integer _id) {

        Cursor taskCursor = this.readableDB.rawQuery("select * from task where _id = ?", new String[]{_id.toString()});

        if(taskCursor.getCount() == 0 || !taskCursor.moveToFirst()){
            return null;
        }

        Task result = new Task();

        String[] columnNames = taskCursor.getColumnNames();
        for (String columnName : columnNames) {
            int columnIndex = taskCursor.getColumnIndexOrThrow(columnName);

            switch (columnName) {
                case "site":
                    result.setSite(taskCursor.getString(columnIndex));
                    break;
                case "name":
                    result.setName(taskCursor.getString(columnIndex));
                    break;
                case "_id":
                    result.set_id(taskCursor.getInt(columnIndex));
                    break;
                case "time":
                    result.setTime(taskCursor.getLong(columnIndex));
                    break;
                default:
                    Log.d("出現欄位名稱錯誤 => ", columnName + columnIndex);
            }
        }
        return result;
    }

    @Override
    public Cursor selectTaskList(Task task) {

        StringBuffer sqlWhere = new StringBuffer();
        sqlWhere.append(" 1=1 ");

        List<String> whereArgs = new ArrayList<String>();

        String site = task.getSite();
        if (site != null && site.toString().length() > 0) {
            sqlWhere.append(" and site = ? ");
            whereArgs.add(site.toString());
        }
        String name = task.getName();
        if (name != null && name.toString().length() > 0) {
            sqlWhere.append(" and name = ? ");
            whereArgs.add(name.toString());
        }
        Integer _id = task.get_id();
        if (_id != null && _id.toString().length() > 0) {
            sqlWhere.append(" and _id = ? ");
            whereArgs.add(_id.toString());
        }
        Long time = task.getTime();
        if (time != null && time.toString().length() > 0) {
            sqlWhere.append(" and time = ? ");
            whereArgs.add(time.toString());
        }
        String[] whereArgsStringArray = new String[whereArgs.size()];
        whereArgsStringArray = whereArgs.toArray(whereArgsStringArray);

        Cursor result = this.readableDB.query("task", null, sqlWhere.toString(), whereArgsStringArray, null, null, "_id");
        return result;
    }

    @Override
    public long insertShopping(Shopping shopping) {

        ContentValues contentValues = new ContentValues();
        contentValues.put("unitPrice", shopping.getUnitPrice());
        contentValues.put("quantity", shopping.getQuantity());
        contentValues.put("name", shopping.getName());
        contentValues.put("taskId", shopping.getTaskId());
        long rowId = this.writableDB.insert("shopping", null, contentValues);
        return rowId;
    }

    @Override
    public void insertShoppingList(List<Shopping> shoppingList) {

        this.writableDB.beginTransaction();

        for (Shopping shopping : shoppingList) {
            this.insertShopping(shopping);
        }
        this.writableDB.setTransactionSuccessful();
        this.writableDB.endTransaction();
    }

    @Override
    public int updateShopping(Shopping shopping) {

        //這裡是要修改的資料
        ContentValues contentValues = new ContentValues();
        contentValues.put("unitPrice", shopping.getUnitPrice());
        contentValues.put("quantity", shopping.getQuantity());
        contentValues.put("name", shopping.getName());
        contentValues.put("taskId", shopping.getTaskId());
        int updateDataAmount = this.writableDB.updateWithOnConflict("shopping", contentValues, "_id = ?", new String[]{shopping.get_id().toString()}, SQLiteDatabase.CONFLICT_ROLLBACK);
        return updateDataAmount;
    }

    @Override
    public void updateShoppingList(List<Shopping> shoppingList) {

        this.writableDB.beginTransaction();

        for (Shopping shopping : shoppingList) {
            this.updateShopping(shopping);
        }
        this.writableDB.setTransactionSuccessful();
        this.writableDB.endTransaction();
    }

    @Override
    public void deleteShopping(Integer _id) {

        int deleteDataAmount = this.writableDB.delete("shopping", "_id = ?", new String[]{_id.toString()});
        if (deleteDataAmount > 1) {
            Log.d("移除 shopping 異常!! 移除了 ", deleteDataAmount + " 筆");
        }
    }

    @Override
    public void deleteShoppingList(List<Integer> shoppingIdList) {

        this.writableDB.beginTransaction();

        for (Integer shoppingId : shoppingIdList) {
            this.deleteShopping(shoppingId);
        }
        this.writableDB.setTransactionSuccessful();
        this.writableDB.endTransaction();
    }

    @Override
    public Shopping selectShoppingById(Integer _id) {

        Cursor shoppingCursor = this.readableDB.rawQuery("select * from shopping where _id = ?", new String[]{_id.toString()});

        if(shoppingCursor.getCount() == 0 || !shoppingCursor.moveToFirst()){
            return null;
        }

        Shopping result = new Shopping();

        String[] columnNames = shoppingCursor.getColumnNames();
        for (String columnName : columnNames) {
            int columnIndex = shoppingCursor.getColumnIndexOrThrow(columnName);

            switch (columnName) {
                case "unitPrice":
                    result.setUnitPrice(shoppingCursor.getFloat(columnIndex));
                    break;
                case "quantity":
                    result.setQuantity(shoppingCursor.getInt(columnIndex));
                    break;
                case "name":
                    result.setName(shoppingCursor.getString(columnIndex));
                    break;
                case "_id":
                    result.set_id(shoppingCursor.getInt(columnIndex));
                    break;
                case "taskId":
                    result.setTaskId(shoppingCursor.getInt(columnIndex));
                    break;
                default:
                    Log.d("出現欄位名稱錯誤 => ", columnName + columnIndex);
            }
        }
        return result;
    }

    @Override
    public Cursor selectShoppingList(Shopping shopping) {

        StringBuffer sqlWhere = new StringBuffer();
        sqlWhere.append(" 1=1 ");

        List<String> whereArgs = new ArrayList<String>();

        Float unitPrice = shopping.getUnitPrice();
        if (unitPrice != null && unitPrice.toString().length() > 0) {
            sqlWhere.append(" and unitPrice = ? ");
            whereArgs.add(unitPrice.toString());
        }
        Integer quantity = shopping.getQuantity();
        if (quantity != null && quantity.toString().length() > 0) {
            sqlWhere.append(" and quantity = ? ");
            whereArgs.add(quantity.toString());
        }
        String name = shopping.getName();
        if (name != null && name.toString().length() > 0) {
            sqlWhere.append(" and name = ? ");
            whereArgs.add(name.toString());
        }
        Integer _id = shopping.get_id();
        if (_id != null && _id.toString().length() > 0) {
            sqlWhere.append(" and _id = ? ");
            whereArgs.add(_id.toString());
        }
        Integer taskId = shopping.getTaskId();
        if (taskId != null && taskId.toString().length() > 0) {
            sqlWhere.append(" and taskId = ? ");
            whereArgs.add(taskId.toString());
        }
        String[] whereArgsStringArray = new String[whereArgs.size()];
        whereArgsStringArray = whereArgs.toArray(whereArgsStringArray);

        Cursor result = this.readableDB.query("shopping", null, sqlWhere.toString(), whereArgsStringArray, null, null, "_id");
        return result;
    }

}
