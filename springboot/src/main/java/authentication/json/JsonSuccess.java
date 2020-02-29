package authentication.json;

public class JsonSuccess implements JsonObject{

    private String message;
    
    public JsonSuccess(String message){
        this.message = message;
    }

    public String getMessage(){
        return message;
    }

}