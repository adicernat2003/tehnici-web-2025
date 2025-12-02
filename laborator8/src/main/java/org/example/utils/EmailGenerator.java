package org.example.utils;

import java.util.UUID;

public class EmailGenerator {

    public static String randomEmail() {
        return "user_" + UUID.randomUUID().toString().substring(0, 8) + "@example.com";
    }

}
