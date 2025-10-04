package com.six.spacex.dto;

import com.six.spacex.domain.RocketStatus;

import java.util.Objects;
import java.util.Optional;

public final class RocketDetails {
    public final String name;
    public final RocketStatus status;
    public final Optional<String> missionName;

    public RocketDetails(String name, RocketStatus status, Optional<String> missionName) {
        this.name = Objects.requireNonNull(name);
        this.status = Objects.requireNonNull(status);
        this.missionName = Objects.requireNonNull(missionName);
    }

    @Override
    public String toString() {
        return name + " - " + format(status) +
                missionName.map(m -> " (mission: " + m + ")").orElse("");
    }

    private static String format(RocketStatus s) {
        return switch (s) {
            case ON_GROUND -> "On ground";
            case IN_SPACE -> "In space";
            case IN_REPAIR -> "In repair";
            case IN_BUILD -> "In build";
        };
    }
}

