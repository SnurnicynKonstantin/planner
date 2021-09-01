package xrm.extrim.planner.common;

import org.springframework.stereotype.Service;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import static xrm.extrim.planner.configuration.GravatarConfig.GRAVATAR_URL;

@Service
@SuppressWarnings({"PMD.ClassNamingConventions"})
public final class Gravatar {
    private static final String AVATAR_URL = "%s%s?s=%d&d=mp";

    private Gravatar() {
    }

    private static String hash(String input) {
        try {
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            md5.update(StandardCharsets.UTF_8.encode(input));
            return String.format("%032x", new BigInteger(1, md5.digest()));
        } catch (NoSuchAlgorithmException ex) {
            return null;
        }
    }

    public static String getAvatarURLFromMail(String mail, int size) {
        return String.format(AVATAR_URL, GRAVATAR_URL, hash(mail.toLowerCase()), size);
    }
}