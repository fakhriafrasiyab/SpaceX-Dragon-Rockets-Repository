package com.six.spacex.util;

import com.six.spacex.domain.MissionStatus;
import com.six.spacex.domain.RocketStatus;
import com.six.spacex.dto.MissionSummary;

import java.util.List;

public final class MissionSummaryPrinter {

    private MissionSummaryPrinter() {}

    public static String printSummaries(List<MissionSummary> summaries) {
        StringBuilder sb = new StringBuilder();
        for (MissionSummary ms : summaries) {
            sb.append(ms.missionName)
                    .append(" - ")
                    .append(format(ms.missionStatus))
                    .append(" - Dragons: ")
                    .append(ms.dragonsCount)
                    .append(System.lineSeparator());
            for (MissionSummary.RocketView r : ms.rockets) {
                sb.append("   o ").append(r.name)
                        .append(" - ").append(format(r.status))
                        .append(System.lineSeparator());
            }
            sb.append(System.lineSeparator());
        }
        return sb.toString().trim();
    }

    private static String format(MissionStatus s) {
        return switch (s) {
            case SCHEDULED -> "Scheduled";
            case PENDING -> "Pending";
            case IN_PROGRESS -> "In progress";
            case ENDED -> "Ended";
        };
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

