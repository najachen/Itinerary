package silver.reminder.itinerary.model;
/**
 * Mon Sep 05 23:27:31 CST 2016 by freemarker template
 */
public class SoundFile {

    private String fileName;
    
    private Integer _id;
    
    public void setFileName(String fileName){
        this.fileName = fileName;
    }
    public String getFileName(){
        return this.fileName;
    }
    
    public void set_id(Integer _id){
        this._id = _id;
    }
    public Integer get_id(){
        return this._id;
    }
    
}