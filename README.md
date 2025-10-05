# 🚀 SpaceX Dragon Rockets Repository

A simple **Java library** (not REST API or microservice) that manages **SpaceX Dragon rockets and missions** using an **in-memory store**.  
Built with **clean code**, **SOLID principles**, and **test-driven development**.

---

## 📘 Overview

The library tracks rockets and missions, their statuses, and their relationships.  
It supports **adding, assigning, updating statuses**, and **summarizing mission data**.

---

## ⚙️ Features

| Functionality       | Description |
|----------------------|-------------|
| **Add rocket** | Creates a rocket with initial status `On ground`. |
| **Add mission** | Creates a mission with initial status `Scheduled`. |
| **Assign rocket** | Assigns a rocket to a mission (one rocket → one mission). |
| **Change statuses** | Rocket: `On ground`, `In space`, `In repair`, `In build`.<br>Mission: `Scheduled`, `Pending`, `In progress`, `Ended`. |
| **Auto mission status** | Automatically recalculates mission status based on assigned rockets. |
| **Summary report** | Returns missions sorted by number of rockets (desc) and name (desc). |
| **Query API** | Fetch missions or rockets by ID or status. |
| **Unassign rocket** | Removes a rocket from a mission and updates statuses. |

---

## 🧠 Example Usage

### Run the demo

```bash
mvn -q compile exec:java -Dexec.mainClass="com.six.spacex.DemoApplication"
```

**Sample Output:**
```
===== SpaceX Dragon Missions Summary =====
Transit - In progress - Dragons: 2
  o Red Dragon - On ground
  o Dragon XL - In space

Lunal - Pending - Dragons: 2
  o Dragon 1 - In space
  o Dragon 2 - In repair
```
---

## 🗂️ Project Structure

```
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
 ├── DemoApplication.java
 │
 └── test
     ├── SpaceXRepositoryTest.java
     └── SpaceXRepositoryExtraTest.java
```

---

## ✅ Tests

All functionality is covered with **JUnit 5 tests**.

- `SpaceXRepositoryTest` → Core features and rule validation  
- `SpaceXRepositoryExtraTest` → Query APIs, unassigning logic, and printer formatting  

Run all tests:
```bash
mvn test -q
```

---

## 📋 Requirements Coverage

| PDF Requirement | Implementation | Test |
|-----------------|----------------|------|
| **Add new rocket (On ground)** | `Rocket`, `InMemorySpaceXRepository.addRocket` | `addRocket_defaultsToOnGround` |
| **Assign rocket (one mission)** | `assignRocketToMission` | `cannotAssignRocketTwice` |
| **Change rocket status** | `changeRocketStatus` | `cannotSetInSpaceWithoutMission`, `changeRocketStatus_toInRepair_makesMissionPending` |
| **Add new mission (Scheduled)** | `Mission`, `addMission` | `addMission_defaultsToScheduled` |
| **Assign multiple rockets** | `assignRocketsToMission` | `summary_ordersByDragonCountDesc_thenNameDesc` |
| **Change mission status (auto)** | `changeMissionStatus`, auto-status recalculation | `cannotEndMissionWithAssignedRockets`, `autoStatusRecalc` |
| **Get summary** | `MissionSummaryPrinter`, `MissionSummary` | `summary_ordersByDragonCountDesc_thenNameDesc` |

---

## 🧩 Notes

- This is a **library**, not a REST API.
- The in-memory store can be replaced later with a database or external storage.
- Designed to demonstrate **domain modeling**, **clean architecture**, and **unit test coverage**.

---
