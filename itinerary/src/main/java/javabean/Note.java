package javabean;

/**
 *  @author 阿鬼
 */
public class Note {

    private String noteContent;
    private String noteExplain;
    private Integer id;
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
}