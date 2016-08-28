package silver.reminder.itinerary.javabean;
/**
 * Sun Aug 28 22:11:47 CST 2016 by freemarker template
 */
public class Schedule {

    private String tm;
    
    private Integer id;
    
    private Integer taskId;
    
    private Integer soundFileId;
    
    public void setTm(String tm){
        this.tm = tm;
    }
    public String getTm(){
        return this.tm;
    }
    
    public void setId(Integer id){
        this.id = id;
    }
    public Integer getId(){
        return this.id;
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