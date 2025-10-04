package com.six.spacex.repository;

import com.six.spacex.domain.MissionId;
import com.six.spacex.domain.MissionStatus;
import com.six.spacex.domain.RocketId;
import com.six.spacex.domain.RocketStatus;
import com.six.spacex.dto.RocketDetails;
import com.six.spacex.util.MissionSummaryPrinter;
import org.junit.jupiter.api.Test;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

class SpaceXRepositoryExtraTest {

    @Test
    void getRocket_and_getMission_work() {
        SpaceXRepository repo = new InMemorySpaceXRepository();
        RocketId r1 = repo.addRocket("Dragon 1");
        MissionId m1 = repo.addMission("LunaX");
        repo.assignRocketToMission(r1, m1);

        var rocket = repo.getRocket(r1).orElseThrow();
        assertEquals("Dragon 1", rocket.name);
        assertTrue(rocket.missionName.isPresent());

        var mission = repo.getMission(m1).orElseThrow();
        assertEquals("LunaX", mission.missionName);
        assertEquals(1, mission.dragonsCount);
    }

    @Test
    void listRocketsByStatus_filtersCorrectly() {
        SpaceXRepository repo = new InMemorySpaceXRepository();
        RocketId r1 = repo.addRocket("Dragon A");
        RocketId r2 = repo.addRocket("Dragon B");
        MissionId m1 = repo.addMission("TestMission");
        repo.assignRocketToMission(r1, m1);
        repo.changeRocketStatus(r1, RocketStatus.IN_SPACE);

        List<RocketDetails> inSpace = repo.listRocketsByStatus(RocketStatus.IN_SPACE);
        assertEquals(1, inSpace.size());
        assertEquals("Dragon A", inSpace.get(0).name);

        List<RocketDetails> onGround = repo.listRocketsByStatus(RocketStatus.ON_GROUND);
        assertEquals(1, onGround.size());
        assertEquals("Dragon B", onGround.get(0).name);
    }

    @Test
    void unassignRocket_updatesMissionStatus() {
        SpaceXRepository repo = new InMemorySpaceXRepository();
        RocketId r1 = repo.addRocket("Dragon 1");
        MissionId m1 = repo.addMission("Luna1");
        repo.assignRocketToMission(r1, m1);

        repo.unassignRocket(r1);

        assertEquals(MissionStatus.SCHEDULED, repo.getMissionStatus(m1).orElseThrow());
    }

    @Test
    void missionSummaryPrinter_formatsLikeExample() {
        SpaceXRepository repo = new InMemorySpaceXRepository();
        MissionId transit = repo.addMission("Transit");
        RocketId red = repo.addRocket("Red Dragon");
        RocketId xl = repo.addRocket("Dragon XL");
        repo.assignRocketToMission(red, transit);
        repo.assignRocketToMission(xl, transit);
        repo.changeRocketStatus(xl, RocketStatus.IN_SPACE);

        var summaries = repo.summarizeMissionsByAssignedRockets();
        String output = MissionSummaryPrinter.printSummaries(summaries);

        assertTrue(output.contains("Transit - In progress - Dragons: 2"));
        assertTrue(output.contains("Red Dragon - On ground"));
        assertTrue(output.contains("Dragon XL - In space"));
    }
}

