package com.six.spacex;

import java.util.List;
import java.util.Objects;

public final class MissionSummary {
    public final String missionName;
    public final MissionStatus missionStatus;
    public final int dragonsCount;
    public final List<RocketView> rockets;

    public MissionSummary(String missionName, MissionStatus missionStatus, int dragonsCount, List<RocketView> rockets) {
        this.missionName = Objects.requireNonNull(missionName);
        this.missionStatus = Objects.requireNonNull(missionStatus);
        this.dragonsCount = dragonsCount;
        this.rockets = List.copyOf(rockets);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(missionName).append(" - ").append(formatStatus(missionStatus)).append(" - Dragons: ").append(dragonsCount);
        for (RocketView r : rockets) {
            sb.append(System.lineSeparator()).append("  o ").append(r.name).append(" - ").append(formatStatus(r.status));
        }
        return sb.toString();
    }

    private static String formatStatus(MissionStatus s) {
        return switch (s) {
            case SCHEDULED -> "Scheduled";
            case PENDING -> "Pending";
            case IN_PROGRESS -> "In progress";
            case ENDED -> "Ended";
        };
    }

    private static String formatStatus(RocketStatus s) {
        return switch (s) {
            case ON_GROUND -> "On ground";
            case IN_SPACE -> "In space";
            case IN_REPAIR -> "In repair";
            case IN_BUILD -> "In build";
        };
    }

    public static final class RocketView {
        public final String name;
        public final RocketStatus status;
        public RocketView(String name, RocketStatus status) {
            this.name = Objects.requireNonNull(name);
            this.status = Objects.requireNonNull(status);
        }
    }
}
