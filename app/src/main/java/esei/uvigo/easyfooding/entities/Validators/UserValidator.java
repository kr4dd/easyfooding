package esei.uvigo.easyfooding.entities.Validators;

import java.util.regex.Pattern;

import esei.uvigo.easyfooding.R;

public class UserValidator {
    public static boolean validarUsuario(String input) {
        Pattern p = Pattern.compile("^[a-zA-Z0-9]{3,40}$");
        return checkValidation(p, input);
    }

    public static boolean validarPass(String input) {
        Pattern p = Pattern.compile("^[a-zA-Z0-9]{3,40}$");
        return checkValidation(p, input);
    }

    public static boolean validarNombreReal(String input) {
        Pattern p = Pattern.compile("^[a-zA-Z]{3,40}$");
        return checkValidation(p, input);
    }

    public static boolean validarApellidos(String input) {
        Pattern p = Pattern.compile("^[a-zA-Z\\s]{2,60}$");
        return checkValidation(p, input);
    }

    public static boolean validarCorreo(String input) {
        Pattern p = Pattern.compile("^[a-zA-Z0-9_.+-]+@[a-zA-Z0-9-]+\\.[a-zA-Z0-9-.]+$");
        return checkValidation(p, input);
    }

    public static boolean validarTlfno(String input) {
        Pattern p = Pattern.compile("^[0-9]{9}$");
        return checkValidation(p, input);
    }

    public static boolean validarDireccion(String input) {
        Pattern p = Pattern.compile("^[a-zA-Zº0-9,.\\s-]{4,60}$");
        return checkValidation(p, input);
    }

    public static boolean validarLocalidad(String input) {
        Pattern p = Pattern.compile("^[a-zA-Z\\s]{4,35}$");
        return checkValidation(p, input);
    }

    public static boolean validarCodigoPostal(String input) {
        Pattern p = Pattern.compile("^[0-9]{5}$");
        return checkValidation(p, input);
    }

    private static boolean checkValidation(Pattern p, String input) {
        return !p.matcher(input).matches();
    }
}
