package authentication;

import java.util.*;
import java.security.*;
import java.math.BigInteger;
import java.time.LocalDate;

import main.java.authentication.json.User;

import com.auth0.jwt.JWT;
import static com.auth0.jwt.algorithms.Algorithm.HMAC512;

import connection.*;

// Token class in charge of creating and verifying JWTs.

public class Token {
    
    private static final String SECRET = "baymax";
    private static final mysqlcon conn = new mysqlcon();

    public String createToken(String username){
        long EXPIRATION_TIME = 5 * 60 * 60 * 1000;
        String token = JWT.create()
                .withSubject((username))
                .withExpiresAt(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .sign(HMAC512(SECRET.getBytes()));
            
        return token;
    }

    public boolean isTokenValid(String user_token){
        String username = JWT.require(HMAC512(SECRET.getBytes()))
                    .build()
                    .verify(user_token)
                    .getSubject();

        User userObject = conn.getUser(username);
        String dbusername = userObject.getUsername();
        if (dbusername.equals(username)){
            return true;
        }
        return false;        
    }

    //modify code - inform Ben
    //@MODIFY - add staffid
    public User retrieveUserObject(String user_token){
        String username = JWT.require(HMAC512(SECRET.getBytes()))
        .build()
        .verify(user_token)
        .getSubject();

        User userObject = conn.getUser(username);
        String staffid = userObject.getStaff_id();
        String dbusername = userObject.getUsername();
        String firstname = userObject.getFirstname();
        String lastname = userObject.getLastname();
        String department = userObject.getDepartment();
        String role = userObject.getRole();
        return new User(staffid, dbusername, firstname, lastname, department, role);
    }

    public String generateMD5Hash(String input){
        try{
            MessageDigest md = MessageDigest.getInstance("MD5");

            byte[] messageDigest = md.digest(input.getBytes());

            BigInteger no = new BigInteger(1, messageDigest); 
    
            String hashText = no.toString(16); 
            while (hashText.length() < 32) { 
                hashText = "0" + hashText; 
            } 

            return hashText;
        } catch (NoSuchAlgorithmException e){
            throw new RuntimeException(e);
        }
    }

    public static String base64encode(String to_encode){
        String encodedString = Base64.getEncoder().encodeToString(to_encode.getBytes());
        return encodedString;
    }

    public static String base64decode(String to_decode){
        byte[] decodedStringByte = Base64.getDecoder().decode(to_decode);
        String decodedString = new String(decodedStringByte);
        return decodedString;
    }

    public static String getDate(){
        String date = (java.time.LocalDate.now().toString());
        return date;
    }

}