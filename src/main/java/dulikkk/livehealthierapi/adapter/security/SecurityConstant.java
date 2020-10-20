package dulikkk.livehealthierapi.adapter.security;

import lombok.Getter;

public enum SecurityConstant {

    ACCESS_TOKEN_NAME("Access_token"),

    REFRESH_TOKEN_NAME("Refresh_token"),

    TOKEN_PREFIX("Bearer "),

    TOKEN_ISSUER("book-management-user"),

    TOKEN_AUDIENCE("book-management-app"),

    ACCESS_TOKEN_EXPIRATION_TIME_IN_MILLISECONDS(10 * 1000); // 10 minutes

    SecurityConstant(String constant) {
        this.constant = constant;
    }

    SecurityConstant(int number) {
        this.number = number;
    }

    @Getter
    String constant;

    @Getter
    int number;
}
