package silver.reminder.itinerary.model;
/**
 * Tue Sep 06 20:53:48 CST 2016 by freemarker template
 */
public class Schedule {

    private Integer _id;

    private Long time;

    private Integer taskId;

    private Integer soundFileId;

    public void set_id(Integer _id){
        this._id = _id;
    }
    public Integer get_id(){
        return this._id;
    }

    public void setTime(Long time){
        this.time = time;
    }
    public Long getTime(){
        return this.time;
    }

    public void setTaskId(Integer taskId){
        this.taskId = taskId;
    }
    public Integer getTaskId(){
        return this.taskId;
    }

    public void setSoundFileId(Integer soundFileId){
        this.soundFileId = soundFileId;
    }
    public Integer getSoundFileId(){
        return this.soundFileId;
    }

}