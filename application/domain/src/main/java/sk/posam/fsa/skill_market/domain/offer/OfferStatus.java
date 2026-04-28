package sk.posam.fsa.skill_market.domain.offer;

public enum OfferStatus {
    PENDING,
    ACCEPTED,
    REJECTED;

    public static OfferStatus from(String value) {
        return OfferStatus.valueOf(value.trim().toUpperCase());
    }
}
