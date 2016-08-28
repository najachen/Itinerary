package silver.reminder.itinerary.bo;

import android.database.Cursor;

import java.util.List;

import silver.reminder.itinerary.javabean.Schedule;
import silver.reminder.itinerary.javabean.SoundFile;
/**
 * Sun Aug 28 21:57:45 CST 2016 by freemarker template
 */
public interface SoundDingDongBo {

    long createSchedule(Schedule schedule);

    void createScheduleList(List<Schedule> scheduleList);

    int modifySchedule(Schedule schedule);

    void modifyScheduleList(List<Schedule> scheduleList);

    void removeSchedule(Integer id);

    void removeScheduleList(List<Integer> scheduleIdList);

    Schedule findScheduleById(Integer id);

    Cursor findScheduleList(Schedule schedule);
    
    long createSoundFile(SoundFile soundFile);

    void createSoundFileList(List<SoundFile> soundFileList);

    int modifySoundFile(SoundFile soundFile);

    void modifySoundFileList(List<SoundFile> soundFileList);

    void removeSoundFile(Integer id);

    void removeSoundFileList(List<Integer> soundFileIdList);

    SoundFile findSoundFileById(Integer id);

    Cursor findSoundFileList(SoundFile soundFile);
    
}
