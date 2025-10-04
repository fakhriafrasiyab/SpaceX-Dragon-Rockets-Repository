package com.six.spacex;

import java.util.*;

final class Mission {
    private final MissionId id;
    private final String name;
    private MissionStatus status;
    private final Set<RocketId> assignedRockets = new LinkedHashSet<>();

    Mission(MissionId id, String name) {
        this.id = Objects.requireNonNull(id);
        this.name = Objects.requireNonNull(name);
        this.status = MissionStatus.SCHEDULED;
    }

    MissionId id() { return id; }
    String name() { return name; }
    MissionStatus status() { return status; }
    Set<RocketId> assignedRockets() { return Collections.unmodifiableSet(assignedRockets); }

    void addRocket(RocketId rocketId) {
        assignedRockets.add(Objects.requireNonNull(rocketId));
    }

    void removeRocket(RocketId rocketId) {
        assignedRockets.remove(Objects.requireNonNull(rocketId));
    }

    void clearRockets() {
        assignedRockets.clear();
    }

    void setStatus(MissionStatus status) {
        this.status = Objects.requireNonNull(status);
    }
}
