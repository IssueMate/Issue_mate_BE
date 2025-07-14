package study.issue_mate.util;

public class MaskUtil {
    public static String maskEmail(String email) {
        int idx = email.indexOf('@');
        if (idx <= 2) return "***" + email.substring(idx);
        String head = email.substring(0, 2);
        return head + "*".repeat(idx - 2) + email.substring(idx);
    }
}
