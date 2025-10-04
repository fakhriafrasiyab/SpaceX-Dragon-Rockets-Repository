package com.six.spacex;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class SpaceXRepositoryTest {

    @Test
    void addRocket_defaultsToOnGround() {
        SpaceXRepository repo = new InMemorySpaceXRepository();
        RocketId r = repo.addRocket("Dragon 1");
        assertEquals(RocketStatus.ON_GROUND, repo.getRocketStatus(r).orElseThrow());
    }

    @Test
    void addMission_defaultsToScheduled() {
        SpaceXRepository repo = new InMemorySpaceXRepository();
        MissionId m = repo.addMission("Mars");
        assertEquals(MissionStatus.SCHEDULED, repo.getMissionStatus(m).orElseThrow());
    }

    @Test
    void assignRocket_toMission_and_autoStatusRecalc() {
        SpaceXRepository repo = new InMemorySpaceXRepository();
        RocketId r1 = repo.addRocket("Dragon 1");
        MissionId m1 = repo.addMission("Luna1");

        repo.assignRocketToMission(r1, m1);
        // One assigned rocket, none in repair -> In progress
        assertEquals(MissionStatus.IN_PROGRESS, repo.getMissionStatus(m1).orElseThrow());
    }

    @Test
    void changeRocketStatus_toInRepair_makesMissionPending() {
        SpaceXRepository repo = new InMemorySpaceXRepository();
        RocketId r1 = repo.addRocket("Dragon 1");
        RocketId r2 = repo.addRocket("Dragon 2");
        MissionId m1 = repo.addMission("Luna1");
        repo.assignRocketToMission(r1, m1);
        repo.assignRocketToMission(r2, m1);

        repo.changeRocketStatus(r2, RocketStatus.IN_REPAIR);
        assertEquals(MissionStatus.PENDING, repo.getMissionStatus(m1).orElseThrow());
    }

    @Test
    void cannotAssignRocketTwice() {
        SpaceXRepository repo = new InMemorySpaceXRepository();
        RocketId r1 = repo.addRocket("Dragon 1");
        MissionId m1 = repo.addMission("Luna1");
        MissionId m2 = repo.addMission("Transit");
        repo.assignRocketToMission(r1, m1);

        DomainException ex = assertThrows(DomainException.class,
                () -> repo.assignRocketToMission(r1, m2));
        assertTrue(ex.getMessage().contains("already assigned"));
    }

    @Test
    void cannotAssignToEndedMission() {
        SpaceXRepository repo = new InMemorySpaceXRepository();
        MissionId m = repo.addMission("Ended Mission");
        // end it (must have 0 assigned)
        repo.changeMissionStatus(m, MissionStatus.ENDED);

        RocketId r = repo.addRocket("Dragon X");
        DomainException ex = assertThrows(DomainException.class, () -> repo.assignRocketToMission(r, m));
        assertTrue(ex.getMessage().toLowerCase().contains("ended"));
    }

    @Test
    void cannotEndMissionWithAssignedRockets() {
        SpaceXRepository repo = new InMemorySpaceXRepository();
        MissionId m = repo.addMission("Luna1");
        RocketId r = repo.addRocket("Dragon 1");
        repo.assignRocketToMission(r, m);

        DomainException ex = assertThrows(DomainException.class,
                () -> repo.changeMissionStatus(m, MissionStatus.ENDED));
        assertTrue(ex.getMessage().toLowerCase().contains("cannot end mission"));
    }

    @Test
    void cannotSetInSpaceWithoutMission() {
        SpaceXRepository repo = new InMemorySpaceXRepository();
        RocketId r = repo.addRocket("Dragon 1");
        DomainException ex = assertThrows(DomainException.class,
                () -> repo.changeRocketStatus(r, RocketStatus.IN_SPACE));
        assertTrue(ex.getMessage().contains("without an assigned mission"));
    }

    @Test
    void cannotSetInBuildWhenAssigned() {
        SpaceXRepository repo = new InMemorySpaceXRepository();
        RocketId r = repo.addRocket("Dragon 1");
        MissionId m = repo.addMission("Luna1");
        repo.assignRocketToMission(r, m);

        DomainException ex = assertThrows(DomainException.class,
                () -> repo.changeRocketStatus(r, RocketStatus.IN_BUILD));
        assertTrue(ex.getMessage().contains("In build"));
    }

    @Test
    void summary_ordersByDragonCountDesc_thenNameDesc() {
        SpaceXRepository repo = new InMemorySpaceXRepository();

        MissionId transit = repo.addMission("Transit");
        MissionId luna1 = repo.addMission("Luna1");
        MissionId vertical = repo.addMission("Vertical Landing");
        MissionId mars = repo.addMission("Mars");
        MissionId luna2 = repo.addMission("Luna2");
        MissionId doubleLanding = repo.addMission("Double Landing");

        RocketId red = repo.addRocket("Red Dragon");
        RocketId xl = repo.addRocket("Dragon XL");
        RocketId heavy = repo.addRocket("Falcon Heavy");
        repo.assignRocketToMission(red, transit);
        repo.assignRocketToMission(xl, transit);
        repo.assignRocketToMission(heavy, transit);
        repo.changeRocketStatus(xl, RocketStatus.IN_SPACE);
        repo.changeRocketStatus(heavy, RocketStatus.IN_SPACE);

        RocketId d1 = repo.addRocket("Dragon 1");
        RocketId d2 = repo.addRocket("Dragon 2");
        repo.assignRocketToMission(d1, luna1);
        repo.assignRocketToMission(d2, luna1);
        repo.changeRocketStatus(d1, RocketStatus.IN_SPACE);
        repo.changeRocketStatus(d2, RocketStatus.IN_REPAIR);

        repo.changeMissionStatus(vertical, MissionStatus.ENDED);

        List<MissionSummary> summary = repo.summarizeMissionsByAssignedRockets();

        // Order expectations: Transit(3), Luna1(2), Vertical Landing(0), Mars(0), Luna2(0), Double Landing(0)
        assertEquals("Transit", summary.get(0).missionName);
        assertEquals(3, summary.get(0).dragonsCount);
        assertEquals("Luna1", summary.get(1).missionName);
        assertEquals(2, summary.get(1).dragonsCount);
        assertEquals("Vertical Landing", summary.get(2).missionName);
        assertEquals("Mars", summary.get(3).missionName);
        assertEquals("Luna2", summary.get(4).missionName);
        assertEquals("Double Landing", summary.get(5).missionName);

        // Spot check formatted text contains the exact status labels
        String formatted = summary.get(0).toString();
        assertTrue(formatted.contains("Transit - In progress - Dragons: 3"));
        assertTrue(formatted.contains("Dragon XL - In space"));
        assertTrue(formatted.contains("Falcon Heavy - In space"));
        assertTrue(formatted.contains("Red Dragon - On ground"));
    }
}
