package silver.reminder.itinerary.model;
/**
 * Mon Sep 05 23:27:31 CST 2016 by freemarker template
 */
public class Schedule {

    private String tm;
    
    private Integer _id;
    
    private Integer taskId;
    
    private Integer soundFileId;
    
    public void setTm(String tm){
        this.tm = tm;
    }
    public String getTm(){
        return this.tm;
    }
    
    public void set_id(Integer _id){
        this._id = _id;
    }
    public Integer get_id(){
        return this._id;
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