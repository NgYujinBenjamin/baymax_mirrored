package authentication.json.users;

import authentication.json.JsonObject;

public class LoginDetails implements JsonObject{
    private String username;
    private String password;

    public LoginDetails(String username, String password){
        this.username = username;
        this.password = password;
    }

    public String getUsername(){
        return username;
    }
    public String getPassword(){
        return password;
    }
}