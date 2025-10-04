package com.six.spacex;

import java.util.Objects;
import java.util.Optional;

final class Rocket {
    private final RocketId id;
    private final String name;
    private RocketStatus status;
    private MissionId missionId; // nullable: unassigned

    Rocket(RocketId id, String name) {
        this.id = Objects.requireNonNull(id);
        this.name = Objects.requireNonNull(name);
        this.status = RocketStatus.ON_GROUND;
    }

    RocketId id() { return id; }
    String name() { return name; }
    RocketStatus status() { return status; }
    Optional<MissionId> missionId() { return Optional.ofNullable(missionId); }

    void assignTo(MissionId missionId) {
        if (this.missionId != null) throw new DomainException("Rocket already assigned to a mission");
        this.missionId = Objects.requireNonNull(missionId);
    }

    void unassign() {
        this.missionId = null;
    }

    void setStatus(RocketStatus status) {
        this.status = Objects.requireNonNull(status);
    }
}
