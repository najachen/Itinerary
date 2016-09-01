package silver.reminder.itinerary.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import silver.reminder.itinerary.javabean.Schedule;
import silver.reminder.itinerary.javabean.SoundFile;
/**
 * Wed Aug 31 22:02:29 CST 2016 by freemarker template
 */
public class SoundDingDongDaoImpl implements SoundDingDongDao {

    /**
     * 獨體模式
     */
    private static SoundDingDongDao soundDingDongDao;

    private SoundDingDongDaoImpl(Context context) {
        SQLiteOpenHelper dbHelper = ItineraryDatabaseHelper.getInstance(context);
        readableDB = dbHelper.getReadableDatabase();
        writableDB = dbHelper.getWritableDatabase();
    }

    public static SoundDingDongDao getInstance(Context context) {
        if (soundDingDongDao == null) {
            soundDingDongDao = new SoundDingDongDaoImpl(context);
        }
        return soundDingDongDao;
    }

    private SQLiteDatabase readableDB;
    private SQLiteDatabase writableDB;

    /*
        方法寫在下面 ======================================================
     */
    @Override
    public long insertSchedule(Schedule schedule) {
		
        ContentValues contentValues = new ContentValues();
		contentValues.put("tm", schedule.getTm());
		contentValues.put("taskId", schedule.getTaskId());
		contentValues.put("soundFileId", schedule.getSoundFileId());
        long rowId = this.writableDB.insert("schedule", null, contentValues);
        return rowId;
    }

    @Override
    public void insertScheduleList(List<Schedule> scheduleList) {

        this.writableDB.beginTransaction();

        for (Schedule schedule : scheduleList) {
            this.insertSchedule(schedule);
        }
        this.writableDB.setTransactionSuccessful();
        this.writableDB.endTransaction();
    }

    @Override
    public int updateSchedule(Schedule schedule) {

        //這裡是要修改的資料
        ContentValues contentValues = new ContentValues();
		contentValues.put("tm", schedule.getTm());
		contentValues.put("taskId", schedule.getTaskId());
		contentValues.put("soundFileId", schedule.getSoundFileId());
        int updateDataAmount = this.writableDB.updateWithOnConflict("schedule", contentValues, "id = ?", new String[]{schedule.getId().toString()}, SQLiteDatabase.CONFLICT_ROLLBACK);
        return updateDataAmount;
    }

    @Override
    public void updateScheduleList(List<Schedule> scheduleList) {

        this.writableDB.beginTransaction();

        for (Schedule schedule : scheduleList) {
            this.updateSchedule(schedule);
        }
        this.writableDB.setTransactionSuccessful();
        this.writableDB.endTransaction();
    }

    @Override
    public void deleteSchedule(Integer id) {

        int deleteDataAmount = this.writableDB.delete("schedule", "id = ?", new String[]{id.toString()});
        if (deleteDataAmount > 1) {
            Log.d("移除 schedule 異常!! 移除了 ", deleteDataAmount + " 筆");
        }
    }

    @Override
    public void deleteScheduleList(List<Integer> scheduleIdList) {

        this.writableDB.beginTransaction();

        for (Integer scheduleId : scheduleIdList) {
            this.deleteSchedule(scheduleId);
        }
        this.writableDB.setTransactionSuccessful();
        this.writableDB.endTransaction();
    }

    @Override
    public Schedule selectScheduleById(Integer id) {

        Cursor scheduleCursor = this.readableDB.rawQuery("select * from schedule where id = ?", new String[]{id.toString()});

        Schedule result = new Schedule();

        String[] columnNames = scheduleCursor.getColumnNames();
        for (String columnName : columnNames) {
            int columnIndex = scheduleCursor.getColumnIndexOrThrow(columnName);

            switch (columnName) {
                case "tm":
	                result.setTm(scheduleCursor.getString(columnIndex));
	                break;
                case "id":
	                result.setId(scheduleCursor.getInt(columnIndex));
	                break;
                case "taskId":
	                result.setTaskId(scheduleCursor.getInt(columnIndex));
	                break;
                case "soundFileId":
	                result.setSoundFileId(scheduleCursor.getInt(columnIndex));
	                break;
                default:
                    Log.d("出現欄位名稱錯誤 => ", columnName + columnIndex);
            }
        }
        return result;
    }

    @Override
    public Cursor selectScheduleList(Schedule schedule) {

        StringBuffer sqlWhere = new StringBuffer();
        sqlWhere.append(" 1=1 ");

        List<String> whereArgs = new ArrayList<String>();

		String tm = schedule.getTm();
        if (tm != null && tm.toString().length() > 0) {
            sqlWhere.append(" and tm = ? ");
            whereArgs.add(tm.toString());
        }
		Integer id = schedule.getId();
        if (id != null && id.toString().length() > 0) {
            sqlWhere.append(" and id = ? ");
            whereArgs.add(id.toString());
        }
		Integer taskId = schedule.getTaskId();
        if (taskId != null && taskId.toString().length() > 0) {
            sqlWhere.append(" and taskId = ? ");
            whereArgs.add(taskId.toString());
        }
		Integer soundFileId = schedule.getSoundFileId();
        if (soundFileId != null && soundFileId.toString().length() > 0) {
            sqlWhere.append(" and soundFileId = ? ");
            whereArgs.add(soundFileId.toString());
        }
        String[] whereArgsStringArray = new String[whereArgs.size()];
        whereArgsStringArray = whereArgs.toArray(whereArgsStringArray);

        Cursor result = this.readableDB.query("schedule", null, sqlWhere.toString(), whereArgsStringArray, null, null, "id");
        return result;
    }
    
    @Override
    public long insertSoundFile(SoundFile soundFile) {
		
        ContentValues contentValues = new ContentValues();
		contentValues.put("fileName", soundFile.getFileName());
        long rowId = this.writableDB.insert("soundFile", null, contentValues);
        return rowId;
    }

    @Override
    public void insertSoundFileList(List<SoundFile> soundFileList) {

        this.writableDB.beginTransaction();

        for (SoundFile soundFile : soundFileList) {
            this.insertSoundFile(soundFile);
        }
        this.writableDB.setTransactionSuccessful();
        this.writableDB.endTransaction();
    }

    @Override
    public int updateSoundFile(SoundFile soundFile) {

        //這裡是要修改的資料
        ContentValues contentValues = new ContentValues();
		contentValues.put("fileName", soundFile.getFileName());
        int updateDataAmount = this.writableDB.updateWithOnConflict("soundFile", contentValues, "id = ?", new String[]{soundFile.getId().toString()}, SQLiteDatabase.CONFLICT_ROLLBACK);
        return updateDataAmount;
    }

    @Override
    public void updateSoundFileList(List<SoundFile> soundFileList) {

        this.writableDB.beginTransaction();

        for (SoundFile soundFile : soundFileList) {
            this.updateSoundFile(soundFile);
        }
        this.writableDB.setTransactionSuccessful();
        this.writableDB.endTransaction();
    }

    @Override
    public void deleteSoundFile(Integer id) {

        int deleteDataAmount = this.writableDB.delete("soundFile", "id = ?", new String[]{id.toString()});
        if (deleteDataAmount > 1) {
            Log.d("移除 soundFile 異常!! 移除了 ", deleteDataAmount + " 筆");
        }
    }

    @Override
    public void deleteSoundFileList(List<Integer> soundFileIdList) {

        this.writableDB.beginTransaction();

        for (Integer soundFileId : soundFileIdList) {
            this.deleteSoundFile(soundFileId);
        }
        this.writableDB.setTransactionSuccessful();
        this.writableDB.endTransaction();
    }

    @Override
    public SoundFile selectSoundFileById(Integer id) {

        Cursor soundFileCursor = this.readableDB.rawQuery("select * from soundFile where id = ?", new String[]{id.toString()});

        SoundFile result = new SoundFile();

        String[] columnNames = soundFileCursor.getColumnNames();
        for (String columnName : columnNames) {
            int columnIndex = soundFileCursor.getColumnIndexOrThrow(columnName);

            switch (columnName) {
                case "fileName":
	                result.setFileName(soundFileCursor.getString(columnIndex));
	                break;
                case "id":
	                result.setId(soundFileCursor.getInt(columnIndex));
	                break;
                default:
                    Log.d("出現欄位名稱錯誤 => ", columnName + columnIndex);
            }
        }
        return result;
    }

    @Override
    public Cursor selectSoundFileList(SoundFile soundFile) {

        StringBuffer sqlWhere = new StringBuffer();
        sqlWhere.append(" 1=1 ");

        List<String> whereArgs = new ArrayList<String>();

		String fileName = soundFile.getFileName();
        if (fileName != null && fileName.toString().length() > 0) {
            sqlWhere.append(" and fileName = ? ");
            whereArgs.add(fileName.toString());
        }
		Integer id = soundFile.getId();
        if (id != null && id.toString().length() > 0) {
            sqlWhere.append(" and id = ? ");
            whereArgs.add(id.toString());
        }
        String[] whereArgsStringArray = new String[whereArgs.size()];
        whereArgsStringArray = whereArgs.toArray(whereArgsStringArray);

        Cursor result = this.readableDB.query("soundFile", null, sqlWhere.toString(), whereArgsStringArray, null, null, "id");
        return result;
    }
    
}
