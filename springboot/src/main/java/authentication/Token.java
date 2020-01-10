package authentication;

import java.util.*;
import java.security.*;
import java.math.BigInteger;
import java.time.LocalDate;

public class Token {
    
    public String createToken(String username, String password){
        String credentials = username + password + getDate();
        String encoded = base64encode(credentials);
        String token = generateMD5Hash(encoded);
        return token;
    }

    public boolean authenticateToken(String user_token, String username, String password){
        String credentials = username + password + getDate();
        String encoded = base64encode(credentials);
        String hashed = generateMD5Hash(encoded);
        
        if (user_token.equals(hashed)){
            // user is authenticated successfully
            return true;
        } else {
            // user is not authenticated successfully
            return false;
        }
    }

    private static String generateMD5Hash(String input){
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

    private static String base64encode(String to_encode){
        String encodedString = Base64.getEncoder().encodeToString(to_encode.getBytes());
        return encodedString;
    }

    private static String base64decode(String to_decode){
        byte[] decodedStringByte = Base64.getDecoder().decode(to_decode);
        String decodedString = new String(decodedStringByte);
        return decodedString;
    }

    private static String getDate(){
        String date = (java.time.LocalDate.now().toString());
        return date;
    }

}