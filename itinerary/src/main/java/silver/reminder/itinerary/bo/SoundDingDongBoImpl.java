package silver.reminder.itinerary.bo;

import android.content.Context;
import android.database.Cursor;

import java.util.List;

import silver.reminder.itinerary.dao.SoundDingDongDao;
import silver.reminder.itinerary.dao.SoundDingDongDaoImpl;
import silver.reminder.itinerary.javabean.Schedule;
import silver.reminder.itinerary.javabean.SoundFile;
/**
 * Sun Aug 28 22:11:47 CST 2016 by freemarker template
 */
public class SoundDingDongBoImpl implements SoundDingDongBo {

    /**
     * 獨體模式
     */
    private static SoundDingDongBo soundDingDongBo;

    private SoundDingDongBoImpl(Context context){
        soundDingDongDao = SoundDingDongDaoImpl.getInstance(context);
    }

    public static SoundDingDongBo getInstance(Context context){
        if(soundDingDongBo == null){
            soundDingDongBo = new SoundDingDongBoImpl(context);
        }
        return soundDingDongBo;
    }

    private SoundDingDongDao soundDingDongDao;

    /*
        方法寫在下面 ======================================================
     */

    @Override
    public long createSchedule(Schedule schedule) {
        long rowId = this.soundDingDongDao.insertSchedule(schedule);
        return rowId;
    }

    @Override
    public void createScheduleList(List<Schedule> scheduleList) {
        this.soundDingDongDao.insertScheduleList(scheduleList);
    }

    @Override
    public int modifySchedule(Schedule schedule) {
        int modifyDataAmount = this.soundDingDongDao.updateSchedule(schedule);
        return modifyDataAmount;
    }

    @Override
    public void modifyScheduleList(List<Schedule> scheduleList) {
        this.soundDingDongDao.updateScheduleList(scheduleList);
    }

    @Override
    public void removeSchedule(Integer id) {
        this.soundDingDongDao.deleteSchedule(id);
    }

    @Override
    public void removeScheduleList(List<Integer> scheduleIdList) {
        this.soundDingDongDao.deleteScheduleList(scheduleIdList);
    }

    @Override
    public Schedule findScheduleById(Integer id) {
        Schedule result = this.soundDingDongDao.selectScheduleById(id);
        return result;
    }

    @Override
    public Cursor findScheduleList(Schedule schedule) {
        Cursor result = this.soundDingDongDao.selectScheduleList(schedule);
        return result;
    }
    
    @Override
    public long createSoundFile(SoundFile soundFile) {
        long rowId = this.soundDingDongDao.insertSoundFile(soundFile);
        return rowId;
    }

    @Override
    public void createSoundFileList(List<SoundFile> soundFileList) {
        this.soundDingDongDao.insertSoundFileList(soundFileList);
    }

    @Override
    public int modifySoundFile(SoundFile soundFile) {
        int modifyDataAmount = this.soundDingDongDao.updateSoundFile(soundFile);
        return modifyDataAmount;
    }

    @Override
    public void modifySoundFileList(List<SoundFile> soundFileList) {
        this.soundDingDongDao.updateSoundFileList(soundFileList);
    }

    @Override
    public void removeSoundFile(Integer id) {
        this.soundDingDongDao.deleteSoundFile(id);
    }

    @Override
    public void removeSoundFileList(List<Integer> soundFileIdList) {
        this.soundDingDongDao.deleteSoundFileList(soundFileIdList);
    }

    @Override
    public SoundFile findSoundFileById(Integer id) {
        SoundFile result = this.soundDingDongDao.selectSoundFileById(id);
        return result;
    }

    @Override
    public Cursor findSoundFileList(SoundFile soundFile) {
        Cursor result = this.soundDingDongDao.selectSoundFileList(soundFile);
        return result;
    }
    
}
