package rubioclemente.miguelangel.keychest;

import android.widget.EditText;

import java.nio.charset.Charset;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.crypto.Cipher;

public class Utilities {
    //Patron para validar email
    private static final Pattern patternEmail = Pattern.compile("^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
            + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$");
    //Patron para validar contraseña
    private static final Pattern patternPswd = Pattern.compile("^(?=.*\\d)(?=.*[a-z])(?=.*[A-Z]).{8,25}$");
    public static boolean validarDatos(EditText userId, EditText userPswd) {
        Matcher matchEmail =patternEmail.matcher(userId.getText().toString());
        Matcher matchPswd =patternPswd.matcher(userPswd.getText().toString());
        if (userId.getText().toString().isEmpty()) {
            userId.setError("Your email is empty");
            return false;
        }
        if(!matchEmail.find()){
            userId.setError("Your email is incorrect");
            return false;
        }
        if(userId.getText().length() >= 45){
            userId.setError("Your email is too long");
            return false;
        }
        if(userPswd.getText().toString().isEmpty()){
            userPswd.setError("Your password is empty");
            return false;
        }
        if(userPswd.getText().length() < 8){
            userPswd.setError("Your password must be at least 8 caracters");
            return false;
        }
        if(!matchPswd.find()){
            userPswd.setError("your password does not satisfy the current policy requirements. Example@123");
            return false;
        }

        return true;
    }

    public static boolean validarDatos(EditText userId, EditText userPswd, EditText userVerifyPassword) {
        Matcher matchEmail =patternEmail.matcher(userId.getText().toString());
        Matcher matchPswd =patternPswd.matcher(userPswd.getText().toString());
        if (userId.getText().toString().isEmpty()) {
            userId.setError("Your email is empty");
            return false;
        }
        if(!matchEmail.find()){
            userId.setError("Your email is incorrect");
            return false;
        }
        if(userId.getText().length() >= 45){
            userId.setError("Your email is too long");
            return false;
        }
        if(userPswd.getText().toString().isEmpty()){
            userPswd.setError("Your password is empty");
            return false;
        }
        if(userPswd.getText().length() < 8){
            userPswd.setError("Your password must be at least 8 caracters");
            return false;
        }
        if(!matchPswd.find()){
            userPswd.setError("your password does not satisfy the current policy requirements. Example@123");
            return false;
        }
        if(userVerifyPassword.getText().toString().isEmpty()){
            userVerifyPassword.setError("Your password is empty");
            return false;
        }
        if(!userVerifyPassword.getText().toString().equals(userPswd.getText().toString())){
            userVerifyPassword.setError("Your password must be the same");
            return false;
        }

        return true;
    }

    public static boolean validarEmail(EditText userEmail){
        Matcher matchEmail =patternEmail.matcher(userEmail.getText().toString());
        if (userEmail.getText().toString().isEmpty()) {
            userEmail.setError("Your email is empty");
            return false;
        }
        if(!matchEmail.find()){
            userEmail.setError("Your email is incorrect");
            return false;
        }
        return true;
    }


    public static String randomText() {
        int leftLimit = 48; // numeral '0'
        int rightLimit = 122; // letter 'z'
        int targetStringLength = 14;
        Random random = new Random();
        String generatedString = random.ints(leftLimit, rightLimit + 1)
                .filter(i -> (i <= 57 || i >= 60) && (i <= 90 || i >= 97))
                .limit(targetStringLength)
                .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                .toString();

        return generatedString;
    }
}
