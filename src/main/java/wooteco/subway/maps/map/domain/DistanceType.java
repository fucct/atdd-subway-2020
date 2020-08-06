package wooteco.subway.maps.map.domain;

import java.util.function.IntBinaryOperator;

public enum DistanceType {
    ZERO((distance, extraFare) -> 0),
    NEAR((distance, extraFare) -> 1250 + extraFare),
    DISTANT((distance, extraFare) -> 1250 + (int)((Math.ceil((distance - 1) / 5) + 1) * 100) + extraFare),
    FAR((distance, extraFare) -> 1250 + (int)((Math.ceil((distance - 1) / 8) + 1) * 100) + extraFare);

    private final IntBinaryOperator operator;

    DistanceType(final IntBinaryOperator operator) {
        this.operator = operator;
    }

    public static DistanceType of(int distance) {
        if (distance == 0) {
            return ZERO;
        }
        if (distance <= 10) {
            return NEAR;
        }
        if (distance <= 50) {
            return DISTANT;
        }
        return FAR;
    }

    public int calculate(int distance, int extraFare) {
        return this.operator.applyAsInt(distance, extraFare);
    }
}
