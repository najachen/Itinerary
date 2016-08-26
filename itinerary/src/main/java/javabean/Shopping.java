package javabean;

/**
 *  @author 阿鬼
 */
public class Shopping {

    private Float unitPrice;
    private Integer quantity;
    private String name;
    private Integer id;
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