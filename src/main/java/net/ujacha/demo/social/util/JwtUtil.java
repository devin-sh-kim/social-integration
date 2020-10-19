package net.ujacha.demo.social.util;

import java.util.Base64;

public class JwtUtil {

    public static String getPayload(String jwtToken) {

        if (jwtToken != null) {
            String[] split_string = jwtToken.split("\\.");

            if (split_string.length >= 2) {

                Base64.Decoder decoder = Base64.getDecoder();
                byte[] decode = decoder.decode(split_string[1].getBytes());
                return new String(decode);
            }
        }
        return null;
    }

}
