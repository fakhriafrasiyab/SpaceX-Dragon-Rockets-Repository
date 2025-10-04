package com.six.spacex;

import java.util.Objects;
import java.util.UUID;

public final class MissionId {
    private final UUID value;
    private MissionId(UUID value) { this.value = value; }
    public static MissionId random() { return new MissionId(UUID.randomUUID()); }
    public static MissionId of(UUID value) { return new MissionId(value); }
    public UUID value() { return value; }
    @Override public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof MissionId)) return false;
        MissionId missionId = (MissionId) o;
        return value.equals(missionId.value);
    }
    @Override public int hashCode() { return Objects.hash(value); }
    @Override public String toString() { return value.toString(); }
}
