package wooteco.subway.maps.map.domain;

import java.util.function.IntUnaryOperator;

public enum AgeType {
    CHILD(fare -> (int)((fare - 350) * 0.5)), TEENAGER(fare -> (int)((fare - 350) * 0.8)), ADULT(fare -> fare);

    private final IntUnaryOperator operator;

    AgeType(final IntUnaryOperator operator) {
        this.operator = operator;
    }

    public static AgeType of(int age) {
        if (age >= 13 && age < 19) {
            return TEENAGER;
        }
        if (age >= 6 && age < 13) {
            return CHILD;
        }
        return ADULT;
    }

    public int calculate(int fare) {
        return operator.applyAsInt(fare);
    }
}
