package br.com.unitins.censohgp.models.enums;

public enum Gender {

    FEMALE(1, "FEMALE"),
    MALE(2, "MALE");

    private final int code;
    private final String description;

    Gender(int code, String description) {
        this.code = code;
        this.description = description;
    }

    public int getCode() {
        return code;
    }

    public String getDescription() {
        return description;
    }

    public static Gender toEnum(Integer code) {
        if (code == null) {
            return null;
        }

        for (Gender x : Gender.values()) {
            if (code.equals(x.getCode())) {
                return x;
            }
        }

        throw new IllegalArgumentException("Invalid code: " + code);
    }
}
