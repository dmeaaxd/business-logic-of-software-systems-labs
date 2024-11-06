package ru.danmax.app.utils;

import at.favre.lib.crypto.bcrypt.BCrypt;

public class BCryptPasswordEncoder {

    private static final BCryptPasswordEncoder INSTANCE = new BCryptPasswordEncoder();

    private BCryptPasswordEncoder() {
    }

    public static BCryptPasswordEncoder getInstance() {
        return INSTANCE;
    }

    public String encode(String password) {
        int cost = 12;
        return BCrypt.withDefaults().hashToString(cost, password.toCharArray());
    }

    public boolean matches(String rawPassword, String encodedPassword){
        BCrypt.Result result = BCrypt.verifyer().verify(rawPassword.toCharArray(), encodedPassword);
        return result.verified;
    }
}
