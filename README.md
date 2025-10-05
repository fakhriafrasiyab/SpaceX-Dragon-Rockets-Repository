🚀 SpaceX Dragon Rockets Repository

A simple Java library (not REST API or microservice) that manages SpaceX Dragon rockets and missions using an in-memory store.
Built with clean code, SOLID principles, and test-driven development.

📘 Overview

The library tracks rockets and missions, their statuses, and relationships.
It supports adding, assigning, updating statuses, and summarizing mission data.

⚙️ Features
Functionality	Description
Add rocket	Creates a rocket with initial status On ground.
Add mission	Creates a mission with initial status Scheduled.
Assign rocket	Assigns a rocket to a mission (one rocket → one mission).
Change statuses	Rocket: On ground, In space, In repair, In build.
Mission: Scheduled, Pending, In progress, Ended.
Auto mission status	Automatically recalculates based on assigned rockets.
Summary report	Returns missions sorted by number of rockets (desc) and name (desc).
Query API	Fetch missions or rockets by ID or status.
Unassign rocket	Removes a rocket from a mission and updates statuses.
🧩 Example Usage
Run the demo:
mvn -q compile exec:java -Dexec.mainClass="com.six.spacex.DemoApplication"

Example output:
===== SpaceX Dragon Missions Summary =====
Transit - In progress - Dragons: 2
   o Red Dragon - On ground
   o Dragon XL - In space

Luna1 - Pending - Dragons: 2
   o Dragon 1 - In space
   o Dragon 2 - In repair

🧱 Project Structure
com.six.spacex
├── domain
│   ├── Mission.java
│   ├── MissionId.java
│   ├── MissionStatus.java
│   ├── Rocket.java
│   ├── RocketId.java
│   ├── RocketStatus.java
│   └── DomainException.java
│
├── dto
│   ├── MissionSummary.java
│   └── RocketDetails.java
│
├── repository
│   ├── SpaceXRepository.java
│   └── InMemorySpaceXRepository.java
│
├── util
│   └── MissionSummaryPrinter.java
│
└── DemoApplication.java

🧪 Tests

All functionality is covered with JUnit 5 tests.

Test Class	Purpose
SpaceXRepositoryTest	Core features and rule validation
SpaceXRepositoryExtraTest	Query APIs, unassigning, and printer formatting

Run tests:

mvn -q test

✅ Requirements Coverage
PDF Requirement	Implementation	Test
Add new rocket (On ground)	Rocket, InMemorySpaceXRepository.addRocket	addRocket_defaultsToOnGround
Assign rocket (one mission)	assignRocketToMission	cannotAssignRocketTwice
Change rocket status	changeRocketStatus	cannotSetInSpaceWithoutMission, changeRocketStatus_toInRepair_makesMissionPending
Add new mission (Scheduled)	Mission, addMission	addMission_defaultsToScheduled
Assign multiple rockets	assignRocketsToMission	summary_ordersByDragonCountDesc_thenNameDesc
Change mission status	changeMissionStatus, auto-status recalculation	cannotEndMissionWithAssignedRockets, autoStatusRecalc
Get summary (sorted)	summarizeMissionsByAssignedRockets	summary_ordersByDragonCountDesc_thenNameDesc
Mission/Rocket constraints	Validations in repository	All rule-based tests
Pretty output	MissionSummaryPrinter	missionSummaryPrinter_formatsLikeExample

All requirements are satisfied and verified through automated tests.

🧰 Build

Requires Java 17+ and Maven 3.9+.

mvn clean package
