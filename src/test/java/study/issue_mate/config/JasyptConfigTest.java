package study.issue_mate.config;

import org.jasypt.encryption.StringEncryptor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class JasyptConfigTest {

    @Autowired
    private StringEncryptor jasyptStringEncryptor;

    @Test
    public void test1() throws Exception {
        String encryptedKey = jasyptStringEncryptor.encrypt("encryptKey");
        System.out.println("encryptedText = " + encryptedKey);

        String decryptedKey = jasyptStringEncryptor.decrypt(encryptedKey);
        System.out.println("decryptedText = " + decryptedKey);

        String encryptedId = jasyptStringEncryptor.encrypt("root");
        System.out.println("encryptedId = " + encryptedId);

        String decryptedId = jasyptStringEncryptor.decrypt(encryptedId);
        System.out.println("decryptedId = " + decryptedId);


        String encryptedPW = jasyptStringEncryptor.encrypt("root");
        System.out.println("encryptedPw = " + encryptedPW);

        String decryptedPW = jasyptStringEncryptor.decrypt(encryptedPW);
        System.out.println("decryptedPw = " + decryptedPW);
    }
}