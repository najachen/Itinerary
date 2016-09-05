package silver.reminder.itinerary.model;
/**
 * Mon Sep 05 23:27:31 CST 2016 by freemarker template
 */
public class Note {

    private String noteContent;
    
    private String noteExplain;
    
    private Integer _id;
    
    private Integer taskId;
    
    public void setNoteContent(String noteContent){
        this.noteContent = noteContent;
    }
    public String getNoteContent(){
        return this.noteContent;
    }
    
    public void setNoteExplain(String noteExplain){
        this.noteExplain = noteExplain;
    }
    public String getNoteExplain(){
        return this.noteExplain;
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
    
}