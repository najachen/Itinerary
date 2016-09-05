package silver.reminder.itinerary.dao;

import android.database.Cursor;

import java.util.List;

import silver.reminder.itinerary.model.Schedule;
import silver.reminder.itinerary.model.SoundFile;
/**
 * Mon Sep 05 23:52:20 CST 2016 by freemarker template
 */
public interface SoundDingDongDao {

    long insertSchedule(Schedule schedule);

    void insertScheduleList(List<Schedule> scheduleList);

    int updateSchedule(Schedule schedule);

    void updateScheduleList(List<Schedule> scheduleList);

    void deleteSchedule(Integer _id);

    void deleteScheduleList(List<Integer> scheduleIdList);

    Schedule selectScheduleById(Integer _id);

    Cursor selectScheduleList(Schedule schedule);

    long insertSoundFile(SoundFile soundFile);

    void insertSoundFileList(List<SoundFile> soundFileList);

    int updateSoundFile(SoundFile soundFile);

    void updateSoundFileList(List<SoundFile> soundFileList);

    void deleteSoundFile(Integer _id);

    void deleteSoundFileList(List<Integer> soundFileIdList);

    SoundFile selectSoundFileById(Integer _id);

    Cursor selectSoundFileList(SoundFile soundFile);

}
