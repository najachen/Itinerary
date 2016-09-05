package silver.reminder.itinerary.model;
/**
 * Mon Sep 05 23:27:31 CST 2016 by freemarker template
 */
public class Shopping {

    private Float unitPrice;
    
    private Integer quantity;
    
    private String name;
    
    private Integer _id;
    
    private Integer taskId;
    
    public void setUnitPrice(Float unitPrice){
        this.unitPrice = unitPrice;
    }
    public Float getUnitPrice(){
        return this.unitPrice;
    }
    
    public void setQuantity(Integer quantity){
        this.quantity = quantity;
    }
    public Integer getQuantity(){
        return this.quantity;
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
    
    public void setTaskId(Integer taskId){
        this.taskId = taskId;
    }
    public Integer getTaskId(){
        return this.taskId;
    }
    
}