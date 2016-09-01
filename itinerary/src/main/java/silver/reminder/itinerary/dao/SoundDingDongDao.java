package silver.reminder.itinerary.dao;

import android.database.Cursor;

import java.util.List;

import silver.reminder.itinerary.javabean.Schedule;
import silver.reminder.itinerary.javabean.SoundFile;
/**
 * Wed Aug 31 22:02:29 CST 2016 by freemarker template
 */
public interface SoundDingDongDao {

    long insertSchedule(Schedule schedule);

    void insertScheduleList(List<Schedule> scheduleList);

    int updateSchedule(Schedule schedule);

    void updateScheduleList(List<Schedule> scheduleList);

    void deleteSchedule(Integer id);

    void deleteScheduleList(List<Integer> scheduleIdList);

    Schedule selectScheduleById(Integer id);

    Cursor selectScheduleList(Schedule schedule);
    
    long insertSoundFile(SoundFile soundFile);

    void insertSoundFileList(List<SoundFile> soundFileList);

    int updateSoundFile(SoundFile soundFile);

    void updateSoundFileList(List<SoundFile> soundFileList);

    void deleteSoundFile(Integer id);

    void deleteSoundFileList(List<Integer> soundFileIdList);

    SoundFile selectSoundFileById(Integer id);

    Cursor selectSoundFileList(SoundFile soundFile);
    
}
