package Standard;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class PasswordCrypt {

    private static final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    public static String hash(String senha) {
        return encoder.encode(senha);
    }

    public static boolean verify(String senhaDigitada, String hashArmazenado) {
        return encoder.matches(senhaDigitada, hashArmazenado);
    }

}
