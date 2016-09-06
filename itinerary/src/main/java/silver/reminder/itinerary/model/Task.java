package silver.reminder.itinerary.model;
/**
 * Tue Sep 06 14:18:17 CST 2016 by freemarker template
 */
public class Task {

    private Integer date;

    private String site;

    private String name;

    private Integer _id;

    private Integer time;

    public void setDate(Integer date){
        this.date = date;
    }
    public Integer getDate(){
        return this.date;
    }

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

    public void setTime(Integer time){
        this.time = time;
    }
    public Integer getTime(){
        return this.time;
    }

}