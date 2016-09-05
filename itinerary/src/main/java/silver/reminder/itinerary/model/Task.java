package silver.reminder.itinerary.model;
/**
 * Mon Sep 05 23:27:31 CST 2016 by freemarker template
 */
public class Task {

    private String site;
    
    private String name;
    
    private String tm;
    
    private Integer _id;
    
    public void setSite(String site){
        this.site = site;
    }
    public String getSite(){
        return this.site;
    }
    
    public void setName(String name){
        this.name = name;
    }
    public String getName(){
        return this.name;
    }
    
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
    
}