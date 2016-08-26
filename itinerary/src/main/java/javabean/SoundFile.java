package javabean;

/**
 *  @author 阿鬼
 */
public class SoundFile {

    private String fileName;
    private Integer id;
    
    public void setFileName(String fileName){
        this.fileName = fileName;
    }
    public String getFileName(){
        return this.fileName;
    }
    
    public void setId(Integer id){
        this.id = id;
    }
    public Integer getId(){
        return this.id;
    }
}