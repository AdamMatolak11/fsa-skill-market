package sk.posam.fsa.skill_market.domain.profile;

public enum UserRole {
    CLIENT,
    FREELANCER,
    ADMIN;

    public static UserRole from(String value) {
        return UserRole.valueOf(value.trim().toUpperCase());
    }
}
