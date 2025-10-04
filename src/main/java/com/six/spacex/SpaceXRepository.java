package com.six.spacex;

import java.util.List;
import java.util.Optional;

public interface SpaceXRepository {
    RocketId addRocket(String name);
    MissionId addMission(String name);
    void assignRocketToMission(RocketId rocketId, MissionId missionId);
    void assignRocketsToMission(MissionId missionId, List<RocketId> rocketIds);
    void changeRocketStatus(RocketId rocketId, RocketStatus status);
    void changeMissionStatus(MissionId missionId, MissionStatus status);
    List<MissionSummary> summarizeMissionsByAssignedRockets();

    // Convenience for tests/introspection
    Optional<RocketStatus> getRocketStatus(RocketId id);
    Optional<MissionStatus> getMissionStatus(MissionId id);
}
