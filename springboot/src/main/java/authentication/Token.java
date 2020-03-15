package authentication;

import com.auth0.jwt.JWT;
import main.java.connection.userscon;
import main.java.authentication.json.users.*;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;
import java.util.Base64;
import java.util.Date;

import static com.auth0.jwt.algorithms.Algorithm.HMAC512;

// Token class in charge of creating and verifying JWTs.

public class Token {

    private static final String SECRET = "baymax";
    private static final userscon conn = new userscon();

    public String createToken(String username) {
        long EXPIRATION_TIME = 5 * 60 * 60 * 1000;
        String token = JWT.create()
                .withSubject((username))
                .withExpiresAt(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .sign(HMAC512(SECRET.getBytes()));

        return token;
    }

    public void validateToken(String user_token) throws SQLException, ClassNotFoundException, NullPointerException {
        String username = getJWTSubject(user_token);
    }

    public String getJWTSubject(String token) {
        return JWT.require(HMAC512(SECRET.getBytes()))
                .build()
                .verify(token)
                .getSubject();
    }

    //modify code - inform Ben
    //@MODIFY - add staffid
    public UserCredentials retrieveUserObject(String user_token) throws Exception {
        String username = getJWTSubject(user_token);
        UserCredentials userObject = conn.getUserCredentials(username);
        return userObject;
    }

    public String generateMD5Hash(String input) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");

            byte[] messageDigest = md.digest(input.getBytes());

            BigInteger no = new BigInteger(1, messageDigest);

            String hashText = no.toString(16);
            while (hashText.length() < 32) {
                hashText = "0" + hashText;
            }

            return hashText;
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    public static String base64encode(String to_encode) {
        String encodedString = Base64.getEncoder().encodeToString(to_encode.getBytes());
        return encodedString;
    }

    public static String base64decode(String to_decode) {
        byte[] decodedStringByte = Base64.getDecoder().decode(to_decode);
        String decodedString = new String(decodedStringByte);
        return decodedString;
    }

    public static String getDate() {
        String date = (java.time.LocalDate.now().toString());
        return date;
    }

}