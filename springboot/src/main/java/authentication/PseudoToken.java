package main.java.authentication;

import java.util.*;
import java.text.*;

public class PseudoToken implements JsonObject{
    private String token;

    public PseudoToken(String token){
        this.token = token;
    }

    public String getToken(){
        return token;
    }
}