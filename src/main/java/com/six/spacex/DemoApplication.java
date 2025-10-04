package com.six.spacex;

import com.six.spacex.domain.*;
import com.six.spacex.dto.MissionSummary;
import com.six.spacex.repository.InMemorySpaceXRepository;
import com.six.spacex.repository.SpaceXRepository;
import com.six.spacex.util.MissionSummaryPrinter;

import java.util.List;

public class DemoApplication {
    public static void main(String[] args) {
        SpaceXRepository repo = new InMemorySpaceXRepository();

        // Create missions
        MissionId transit = repo.addMission("Transit");
        MissionId luna1 = repo.addMission("Luna1");

        // Create rockets
        RocketId red = repo.addRocket("Red Dragon");
        RocketId xl = repo.addRocket("Dragon XL");
        RocketId d1 = repo.addRocket("Dragon 1");
        RocketId d2 = repo.addRocket("Dragon 2");

        // Assign rockets
        repo.assignRocketToMission(red, transit);
        repo.assignRocketToMission(xl, transit);
        repo.assignRocketToMission(d1, luna1);
        repo.assignRocketToMission(d2, luna1);

        // Change statuses
        repo.changeRocketStatus(xl, RocketStatus.IN_SPACE);
        repo.changeRocketStatus(d1, RocketStatus.IN_SPACE);
        repo.changeRocketStatus(d2, RocketStatus.IN_REPAIR);

        // Summarize
        List<MissionSummary> summaries = repo.summarizeMissionsByAssignedRockets();

        // Print
        System.out.println("===== SpaceX Dragon Missions Summary =====");
        System.out.println(MissionSummaryPrinter.printSummaries(summaries));
    }
}
