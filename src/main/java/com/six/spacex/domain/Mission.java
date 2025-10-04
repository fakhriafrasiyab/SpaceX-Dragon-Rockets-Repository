package com.six.spacex.domain;

import java.util.*;

public final class Mission {
    private final MissionId id;
    private final String name;
    private MissionStatus status;
    private final Set<RocketId> assignedRockets = new LinkedHashSet<>();

    public Mission(MissionId id, String name) {
        this.id = Objects.requireNonNull(id);
        this.name = Objects.requireNonNull(name);
        this.status = MissionStatus.SCHEDULED;
    }

    public MissionId id() { return id; }
    public String name() { return name; }
    public MissionStatus status() { return status; }
    public Set<RocketId> assignedRockets() { return Collections.unmodifiableSet(assignedRockets); }

    public void addRocket(RocketId rocketId) {
        assignedRockets.add(Objects.requireNonNull(rocketId));
    }

    public void removeRocket(RocketId rocketId) {
        assignedRockets.remove(Objects.requireNonNull(rocketId));
    }

    public void clearRockets() {
        assignedRockets.clear();
    }

    public void setStatus(MissionStatus status) {
        this.status = Objects.requireNonNull(status);
    }
}
