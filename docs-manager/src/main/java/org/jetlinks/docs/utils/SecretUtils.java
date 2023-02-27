package org.jetlinks.docs.utils;

import org.apache.commons.codec.binary.Base64;

import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;

/**
 * 输入描述.
 *
 * @author zhangji 2023/2/21
 */
public class SecretUtils {

    public static String parseSecret(String secret,
                                     byte[] payload) {
        byte[] sign = SecretUtils.encryptHMAC(secret, payload);
        return new String(Base64.encodeBase64(sign));
    }

    public static byte[] encryptHMAC(String secret,
                                     byte[] data) {
        byte[] bytes = null;

        try {
            SecretKey secretKey = new SecretKeySpec(secret.getBytes(StandardCharsets.UTF_8), "HmacSha256");
            Mac mac = Mac.getInstance(secretKey.getAlgorithm());
            mac.init(secretKey);
            bytes = mac.doFinal(data);
        } catch (Exception var5) {
            var5.printStackTrace(System.err);
        }

        return bytes;
    }
}
