package rubioclemente.miguelangel.keychest;

public class User {
    private int id;
    private String email, passwd;

    public User(){

    }
    public User(String email, String password){
        this.email = email;
        this.passwd = password;
    }
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    /*public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
*/
    public String getPasswd() {
        return passwd;
    }

    public void setPasswd(String passwd) {
        this.passwd = passwd;
    }


}
