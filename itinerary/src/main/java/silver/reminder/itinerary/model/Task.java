package silver.reminder.itinerary.model;
/**
 * Tue Sep 06 20:53:48 CST 2016 by freemarker template
 */
public class Task {

    private String site;

    private String name;

    private Integer _id;

    private Long time;

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

}