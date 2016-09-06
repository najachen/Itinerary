package silver.reminder.itinerary.model;
/**
 * Tue Sep 06 14:18:17 CST 2016 by freemarker template
 */
public class Schedule {

    private Integer date;

    private Integer _id;

    private Integer time;

    private Integer taskId;

    private Integer soundFileId;

    public void setDate(Integer date){
        this.date = date;
    }
    public Integer getDate(){
        return this.date;
    }

    public void set_id(Integer _id){
        this._id = _id;
    }
    public Integer get_id(){
        return this._id;
    }

    public void setTime(Integer time){
        this.time = time;
    }
    public Integer getTime(){
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