package com.six.spacex.domain;

import java.util.Objects;
import java.util.Optional;

public final class Rocket {
    private final RocketId id;
    private final String name;
    private RocketStatus status;
    private MissionId missionId; // nullable: unassigned

    public Rocket(RocketId id, String name) {
        this.id = Objects.requireNonNull(id);
        this.name = Objects.requireNonNull(name);
        this.status = RocketStatus.ON_GROUND;
    }

    public RocketId id() { return id; }
    public String name() { return name; }
    public RocketStatus status() { return status; }
    public Optional<MissionId> missionId() { return Optional.ofNullable(missionId); }

    public void assignTo(MissionId missionId) {
        if (this.missionId != null) throw new DomainException("Rocket already assigned to a mission");
        this.missionId = Objects.requireNonNull(missionId);
    }

    public void unassign() {
        this.missionId = null;
    }

    public void setStatus(RocketStatus status) {
        this.status = Objects.requireNonNull(status);
    }
}
