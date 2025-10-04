package com.six.spacex.repository;

import com.six.spacex.domain.MissionId;
import com.six.spacex.domain.MissionStatus;
import com.six.spacex.domain.RocketId;
import com.six.spacex.domain.RocketStatus;
import com.six.spacex.dto.MissionSummary;
import com.six.spacex.dto.RocketDetails;

import java.util.List;
import java.util.Optional;

public interface SpaceXRepository {
    RocketId addRocket(String name);
    MissionId addMission(String name);
    void assignRocketToMission(RocketId rocketId, MissionId missionId);
    void assignRocketsToMission(MissionId missionId, List<RocketId> rocketIds);
    void changeRocketStatus(RocketId rocketId, RocketStatus status);
    void changeMissionStatus(MissionId missionId, MissionStatus status);
    void unassignRocket(RocketId rocketId);
    List<MissionSummary> summarizeMissionsByAssignedRockets();

    // Extra queries
    Optional<RocketDetails> getRocket(RocketId id);
    Optional<MissionSummary> getMission(MissionId id);
    List<RocketDetails> listRocketsByStatus(RocketStatus status);
    List<MissionSummary> listMissionsByStatus(MissionStatus status);

    // Existing simple queries
    Optional<RocketStatus> getRocketStatus(RocketId id);
    Optional<MissionStatus> getMissionStatus(MissionId id);
}

