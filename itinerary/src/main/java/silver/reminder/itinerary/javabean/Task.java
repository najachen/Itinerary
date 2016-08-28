package silver.reminder.itinerary.javabean;
/**
 * Sun Aug 28 21:57:45 CST 2016 by freemarker template
 */
public class Task {

    private String site;
    
    private String name;
    
    private String tm;
    
    private Integer id;
    
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
    
    public void setId(Integer id){
        this.id = id;
    }
    public Integer getId(){
        return this.id;
    }
    
}