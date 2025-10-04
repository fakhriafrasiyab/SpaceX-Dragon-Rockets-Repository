# SpaceX Dragon Rockets Repository (library)

A simple **in-memory library** that manages SpaceX dragons (rockets) and missions.

> Exercise highlights: No frameworks, TDD, SOLID, clean code, and clear commit history.
> This README records assumptions and decisions.

## Requirements Mapping (recap)

**Operations**:
1. Add rocket (default status: `On ground`)
2. Assign rocket to mission (a rocket can be in **one** mission at a time)
3. Change rocket status (statuses below)
4. Add mission (default status: `Scheduled`)
5. Assign **multiple** rockets to a mission
6. Change mission status
7. Mission summary sorted by number of assigned rockets (desc), then mission name (Z→A).

**Statuses**
- Rocket: `On ground`, `In space`, `In repair`, `In build`
- Mission: `Scheduled`, `Pending`, `In Progress`, `Ended`

## Assumptions & Clarifications

Some statements in the prompt conflict with the example. To keep behavior practical and match the example:
- **Assigned rocket with `On ground`** is allowed (example shows a mission listing a rocket *On ground*). So `On ground` does **not** strictly imply “unassigned”; it’s simply “not in space.”
- `In space` rockets **must be assigned** to a mission (disallowed otherwise).
- Changing a rocket to `In build` is allowed only when **not assigned**.
- Mission status is **automatically recalculated** whenever assignments or rocket statuses change **unless** the mission is `Ended`.
  - `Scheduled` ⇢ zero assigned rockets
  - `Pending` ⇢ ≥1 assigned **and** at least one rocket is `In repair`
  - `In Progress` ⇢ ≥1 assigned **and** none are `In repair`
  - `Ended` ⇢ explicit terminal status; **forbidden** if any rocket is assigned; also **no further assignments** allowed.
- Assigning to an `Ended` mission is forbidden.
- Assigning a rocket that is already assigned is forbidden.
- **Bulk assignment** is atomic: either all succeed or none.

No persistence: **all in memory**.

## Public API (high level)

```java
SpaceXRepository repo = new InMemorySpaceXRepository();
RocketId r1 = repo.addRocket("Dragon XL");
MissionId m1 = repo.addMission("Transit");
repo.assignRocketToMission(r1, m1);
repo.changeRocketStatus(r1, RocketStatus.IN_SPACE);
List<MissionSummary> summary = repo.summarizeMissionsByAssignedRockets();
```

## Build & Test

```bash
mvn -q -e -DskipTests=false test
```

## AI usage

Parts of the initial project scaffolding and tests were drafted with help from an AI assistant to speed up repetitive setup. Design choices, domain rules, and code were reviewed and adjusted by me.
