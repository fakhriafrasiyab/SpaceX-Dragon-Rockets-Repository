package com.six.spacex;

import java.util.Objects;
import java.util.UUID;

public final class RocketId {
    private final UUID value;
    private RocketId(UUID value) { this.value = value; }
    public static RocketId random() { return new RocketId(UUID.randomUUID()); }
    public static RocketId of(UUID value) { return new RocketId(value); }
    public UUID value() { return value; }
    @Override public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof RocketId)) return false;
        RocketId rocketId = (RocketId) o;
        return value.equals(rocketId.value);
    }
    @Override public int hashCode() { return Objects.hash(value); }
    @Override public String toString() { return value.toString(); }
}
