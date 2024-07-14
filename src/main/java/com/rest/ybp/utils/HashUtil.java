package com.rest.ybp.utils;

import com.password4j.Hash;
import com.password4j.Password;

public class HashUtil {

    public static String hashPlanPassword(String planPassword) {
        String reuslt = null;
        try {
            Hash hash = Password.hash(planPassword).withBcrypt();    
            reuslt = hash.getResult();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return reuslt;
    }

    public static boolean isPwdMatchHashPwd(String password, String hashedPassword) {
        return Password.check(password, hashedPassword).withBcrypt();
    }
}
