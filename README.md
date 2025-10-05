# 🚀 SpaceX Dragon Rockets Repository

A simple **Java library** (not REST API or microservice) that manages SpaceX Dragon rockets and missions using an **in-memory store**.  
Built with **clean code**, **SOLID principles**, and **test-driven development**.

---

## 📘 Overview

The library tracks rockets and missions, their statuses, and relationships.  
It supports adding, assigning, updating statuses, and summarizing mission data.

---

## ⚙️ Features

| Functionality | Description |
|----------------|-------------|
| **Add rocket** | Creates a rocket with initial status `On ground`. |
| **Add mission** | Creates a mission with initial status `Scheduled`. |
| **Assign rocket** | Assigns a rocket to a mission (one rocket → one mission). |
| **Change statuses** | Rocket: `On ground`, `In space`, `In repair`, `In build`.<br>Mission: `Scheduled`, `Pending`, `In progress`, `Ended`. |
| **Auto mission status** | Automatically recalculates based on assigned rockets. |
| **Summary report** | Returns missions sorted by number of rockets (desc) and name (desc). |
| **Query API** | Fetch missions or rockets by ID or status. |
| **Unassign rocket** | Removes a rocket from a mission and updates statuses. |

---

## 🧩 Example Usage

### Run the demo
```bash
mvn -q compile exec:java -Dexec.mainClass="com.six.spacex.DemoApplication"


===== SpaceX Dragon Missions Summary =====
Transit - In progress - Dragons: 2
   o Red Dragon - On ground
   o Dragon XL - In space

Luna1 - Pending - Dragons: 2
   o Dragon 1 - In space
   o Dragon 2 - In repair
