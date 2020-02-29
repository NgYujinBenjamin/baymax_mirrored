package authentication;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;
import java.math.BigInteger;
import java.time.LocalDate;
import java.util.Base64;
import java.util.Date;

import authentication.connection.mysqlcon;
import authentication.json.users.User;
import com.auth0.jwt.JWT;
import static com.auth0.jwt.algorithms.Algorithm.HMAC512;

//import connection.*;

// Token class in charge of creating and verifying JWTs.

public class Token {
    
    private static final String SECRET = "baymax";
    private static final mysqlcon conn = new mysqlcon();

    public String createToken(String username){
        long EXPIRATION_TIME = 5 * 60 * 60 * 1000;
        return JWT.create()
                .withSubject((username))
                .withExpiresAt(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .sign(HMAC512(SECRET.getBytes()));
    }

    public boolean isTokenValid(String user_token) throws SQLException, ClassNotFoundException, NullPointerException{
        String username = JWT.require(HMAC512(SECRET.getBytes()))
                    .build()
                    .verify(user_token)
                    .getSubject();
        User userObject = conn.getUser(username);
        if (userObject == null){
            throw new NullPointerException();
        }
        String dbusername = userObject.getUsername();
        return dbusername.equals(username);
    }

    //modify code - inform Ben
    //@MODIFY - add staffid
    public User retrieveUserObject(String user_token) throws Exception{
        String username = JWT.require(HMAC512(SECRET.getBytes()))
        .build()
        .verify(user_token)
        .getSubject();
        try{
            User userObject = conn.getUser(username);
        return userObject;
        }  catch (SQLException | ClassNotFoundException e) {
            throw e;
        }
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
        return Base64.getEncoder().encodeToString(to_encode.getBytes());
    }

    public static String base64decode(String to_decode){
        byte[] decodedStringByte = Base64.getDecoder().decode(to_decode);
        return new String(decodedStringByte);
    }

    public static String getDate(){
        return (LocalDate.now().toString());
    }

}