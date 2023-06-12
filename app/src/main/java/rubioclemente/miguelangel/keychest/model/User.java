package rubioclemente.miguelangel.keychest.model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

public class User implements Parcelable {

    //ATRIBUTES
    private int id;
    private String email, passwd,token,tempPasswd;


    //CONSTRUCTORS
    public User(){

    }

    public User(String token){
        this.token = token;
    }
    public User(String email, String password){
        this.email = email;
        this.passwd = password;
    }

    public User(String token,String email, String password,String tempPasswd){
        this.token=token;
        this.email = email;
        this.passwd = password;
        this.tempPasswd=tempPasswd;
    }

    protected User(Parcel in) {
        id = in.readInt();
        email = in.readString();
        passwd = in.readString();
        token = in.readString();
        tempPasswd=in.readString();
    }

    //PARCELABLE
    public static final Creator<User> CREATOR = new Creator<User>() {
        @Override
        public User createFromParcel(Parcel in) {
            return new User(in);
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(email);
        dest.writeString(passwd);
        dest.writeString(token);
        dest.writeString(tempPasswd);
    }

    //GETTERS & SETTERS
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

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
    public String getPasswd() {
        return passwd;
    }

    public void setPasswd(String passwd) {
        this.passwd = passwd;
    }

    public String getTempPasswd() {
        return tempPasswd;
    }

    public void setTempPasswd(String tempPasswd) {
        this.tempPasswd = tempPasswd;
    }



}
