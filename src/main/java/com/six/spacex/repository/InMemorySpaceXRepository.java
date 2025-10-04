package com.six.spacex.repository;

import com.six.spacex.domain.*;
import com.six.spacex.dto.MissionSummary;
import com.six.spacex.dto.RocketDetails;

import java.util.*;
import java.util.stream.Collectors;

public final class InMemorySpaceXRepository implements SpaceXRepository {

    private final Map<RocketId, Rocket> rockets = new LinkedHashMap<>();
    private final Map<MissionId, Mission> missions = new LinkedHashMap<>();

    @Override
    public RocketId addRocket(String name) {
        Objects.requireNonNull(name, "name");
        RocketId id = RocketId.random();
        rockets.put(id, new Rocket(id, name));
        return id;
    }

    @Override
    public MissionId addMission(String name) {
        Objects.requireNonNull(name, "name");
        MissionId id = MissionId.random();
        missions.put(id, new Mission(id, name));
        return id;
    }

    @Override
    public void assignRocketToMission(RocketId rocketId, MissionId missionId) {
        Rocket rocket = requireRocket(rocketId);
        Mission mission = requireMission(missionId);

        if (mission.status() == MissionStatus.ENDED) {
            throw new DomainException("Cannot assign rockets to an ended mission");
        }
        if (rocket.missionId().isPresent()) {
            throw new DomainException("Rocket already assigned to a mission");
        }

        rocket.assignTo(missionId);
        mission.addRocket(rocketId);

        recalcMissionStatus(mission);
    }

    @Override
    public void assignRocketsToMission(MissionId missionId, List<RocketId> rocketIds) {
        Mission mission = requireMission(missionId);
        if (mission.status() == MissionStatus.ENDED) {
            throw new DomainException("Cannot assign rockets to an ended mission");
        }
        for (RocketId rid : rocketIds) {
            Rocket r = requireRocket(rid);
            if (r.missionId().isPresent()) {
                throw new DomainException("Rocket already assigned to a mission: " + rid);
            }
        }
        for (RocketId rid : rocketIds) {
            Rocket r = requireRocket(rid);
            r.assignTo(missionId);
            mission.addRocket(rid);
        }
        recalcMissionStatus(mission);
    }

    @Override
    public void changeRocketStatus(RocketId rocketId, RocketStatus status) {
        Rocket rocket = requireRocket(rocketId);
        Mission mission = rocket.missionId().map(this::requireMission).orElse(null);

        if (status == RocketStatus.IN_SPACE && mission == null) {
            throw new DomainException("Rocket cannot be 'In space' without an assigned mission");
        }
        if (status == RocketStatus.IN_BUILD && mission != null) {
            throw new DomainException("Assigned rocket cannot be set to 'In build'");
        }

        rocket.setStatus(status);

        if (mission != null && mission.status() != MissionStatus.ENDED) {
            recalcMissionStatus(mission);
        }
    }

    @Override
    public void changeMissionStatus(MissionId missionId, MissionStatus status) {
        Mission mission = requireMission(missionId);
        Set<RocketId> assigned = mission.assignedRockets();

        switch (status) {
            case ENDED -> {
                if (!assigned.isEmpty()) {
                    throw new DomainException("Cannot end mission while rockets are assigned");
                }
                mission.setStatus(MissionStatus.ENDED);
            }
            case SCHEDULED -> {
                if (!assigned.isEmpty()) {
                    throw new DomainException("Scheduled requires zero assigned rockets");
                }
                mission.setStatus(MissionStatus.SCHEDULED);
            }
            case PENDING, IN_PROGRESS -> {
                if (assigned.isEmpty()) {
                    throw new DomainException("Mission must have at least one assigned rocket");
                }
                boolean anyInRepair = assigned.stream().map(this::requireRocket)
                        .anyMatch(r -> r.status() == RocketStatus.IN_REPAIR);
                if (status == MissionStatus.PENDING && !anyInRepair) {
                    throw new DomainException("Pending requires at least one rocket in repair");
                }
                if (status == MissionStatus.IN_PROGRESS && anyInRepair) {
                    throw new DomainException("In Progress cannot have rockets in repair");
                }
                mission.setStatus(status);
            }
        }
    }

    @Override
    public void unassignRocket(RocketId rocketId) {
        Rocket rocket = requireRocket(rocketId);
        Mission mission = rocket.missionId().map(this::requireMission).orElseThrow(
                () -> new DomainException("Rocket is not assigned to any mission")
        );

        rocket.unassign();
        mission.removeRocket(rocketId);
        recalcMissionStatus(mission);
    }

    @Override
    public List<MissionSummary> summarizeMissionsByAssignedRockets() {
        List<MissionSummary> summaries = missions.values().stream()
                .map(m -> {
                    List<MissionSummary.RocketView> rv = m.assignedRockets().stream()
                            .map(this::requireRocket)
                            .map(r -> new MissionSummary.RocketView(r.name(), r.status()))
                            .collect(Collectors.toList());
                    return new MissionSummary(m.name(), m.status(), m.assignedRockets().size(), rv);
                })
                .collect(Collectors.toList());

        summaries.sort(Comparator
                .comparingInt((MissionSummary s) -> s.dragonsCount).reversed()
                .thenComparing((MissionSummary s) -> s.missionName, Comparator.reverseOrder())
        );
        return summaries;
    }

    @Override
    public Optional<RocketDetails> getRocket(RocketId id) {
        Rocket r = rockets.get(id);
        if (r == null) return Optional.empty();
        Optional<String> missionName = r.missionId().map(mid -> requireMission(mid).name());
        return Optional.of(new RocketDetails(r.name(), r.status(), missionName));
    }

    @Override
    public Optional<MissionSummary> getMission(MissionId id) {
        Mission m = missions.get(id);
        if (m == null) return Optional.empty();
        List<MissionSummary.RocketView> rv = m.assignedRockets().stream()
                .map(this::requireRocket)
                .map(r -> new MissionSummary.RocketView(r.name(), r.status()))
                .collect(Collectors.toList());
        return Optional.of(new MissionSummary(m.name(), m.status(), m.assignedRockets().size(), rv));
    }

    @Override
    public List<RocketDetails> listRocketsByStatus(RocketStatus status) {
        return rockets.values().stream()
                .filter(r -> r.status() == status)
                .map(r -> new RocketDetails(r.name(), r.status(),
                        r.missionId().map(mid -> requireMission(mid).name())))
                .collect(Collectors.toList());
    }

    @Override
    public List<MissionSummary> listMissionsByStatus(MissionStatus status) {
        return missions.values().stream()
                .filter(m -> m.status() == status)
                .map(m -> {
                    List<MissionSummary.RocketView> rv = m.assignedRockets().stream()
                            .map(this::requireRocket)
                            .map(r -> new MissionSummary.RocketView(r.name(), r.status()))
                            .collect(Collectors.toList());
                    return new MissionSummary(m.name(), m.status(), m.assignedRockets().size(), rv);
                })
                .collect(Collectors.toList());
    }

    @Override
    public Optional<RocketStatus> getRocketStatus(RocketId id) {
        Rocket r = rockets.get(id);
        return r == null ? Optional.empty() : Optional.of(r.status());
    }

    @Override
    public Optional<MissionStatus> getMissionStatus(MissionId id) {
        Mission m = missions.get(id);
        return m == null ? Optional.empty() : Optional.of(m.status());
    }

    // Helpers
    private Rocket requireRocket(RocketId id) {
        Rocket r = rockets.get(id);
        if (r == null) throw new DomainException("Unknown rocket: " + id);
        return r;
    }

    private Mission requireMission(MissionId id) {
        Mission m = missions.get(id);
        if (m == null) throw new DomainException("Unknown mission: " + id);
        return m;
    }

    private void recalcMissionStatus(Mission mission) {
        if (mission.status() == MissionStatus.ENDED) return;

        int assigned = mission.assignedRockets().size();
        if (assigned == 0) {
            mission.setStatus(MissionStatus.SCHEDULED);
            return;
        }
        boolean anyRepair = mission.assignedRockets().stream()
                .map(this::requireRocket)
                .anyMatch(r -> r.status() == RocketStatus.IN_REPAIR);
        if (anyRepair) {
            mission.setStatus(MissionStatus.PENDING);
        } else {
            mission.setStatus(MissionStatus.IN_PROGRESS);
        }
    }
}
