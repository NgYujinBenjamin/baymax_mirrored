package authentication.json;

public class TokenSuccess implements JsonObject {

    private String token;

    public TokenSuccess(String token) {
        this.token = token;
    }

    public String getToken() {
        return token;
    }

}