# StudyForge — Dependency-Aware Adaptive Study Planner

## 1. Overview
StudyForge treats a syllabus as a dependency graph instead of a simple to-do list. Students enter topics, prerequisites, difficulty, estimated study time, and deadlines. The application validates the dependency graph and generates a dependency-aware daily study plan.

The plan can be regenerated after progress changes, allowing the schedule to adapt when topics are completed or skipped.

## 2. Core Features
- Topic management
- Prerequisite/dependency management
- Adjacency-list graph representation
- DFS cycle detection
- Kahn's topological sorting
- Greedy deadline-first scheduling
- Critical-path-weighted scheduling
- Progress tracking
- Adaptive rescheduling
- File-based persistence
- Input validation and error handling
- Automated tests

## 3. Functional Modules
1. **Syllabus Graph Module** — models topics and prerequisite relationships.
2. **Scheduling Module** — generates plans using selectable algorithms.
3. **Progress Tracking Module** — records completion state and supports recalculation.
4. **Persistence Module** — saves and loads project data.

## 4. Non-Functional Requirements
- **Usability:** menu-driven CLI with clear prompts and output.
- **Reliability:** invalid dependencies and circular relationships are rejected.
- **Performance:** graph algorithms use adjacency-list representation; topological sorting is O(V+E).
- **Maintainability:** responsibilities are separated into packages and classes.
- **Error Handling:** invalid IDs, values, dates, and dependency operations are reported.
- **Persistence:** data can be stored and restored between executions.

## 5. Technologies
- Java
- Object-Oriented Programming
- Collections Framework
- Graph Algorithms
- File I/O
- Java Time API

## 6. Project Structure
```text
src/
  model/
  graph/
  algorithm/
  service/
  persistence/
  exception/
  Main.java
tests/
docs/
README.md
statement.md
```

## 7. Requirements
- Java JDK 17 or later

## 8. Compile
From the project root:

### Windows PowerShell
```powershell
javac -d out src\model\*.java src\graph\*.java src\algorithm\*.java src\service\*.java src\persistence\*.java src\exception\*.java src\Main.java
```

## 9. Run
```powershell
java -cp out Main
```

## 10. Run Tests
Compile:
```powershell
javac -d out src\model\*.java src\graph\*.java src\algorithm\*.java src\service\*.java src\persistence\*.java src\exception\*.java tests\StudyForgeTest.java
```

Run:
```powershell
java -ea -cp out StudyForgeTest
```

Expected:
```text
ALL TESTS PASSED
```

## 11. Algorithmic Concepts
### Adjacency List
Each topic stores a set of dependent topics. This represents the syllabus as a directed graph.

### Cycle Detection
DFS tracks the current recursion path. A back-edge indicates a cycle, so invalid dependencies are rejected.

### Topological Sort
Kahn's algorithm repeatedly selects zero-indegree vertices to create an order that respects prerequisites.

### Greedy Scheduler
Among currently available topics, the scheduler prioritizes earlier deadlines, then higher difficulty.

### Critical-Path-Weighted Scheduler
Each topic receives a downstream path weight based on its own workload plus the largest downstream workload. Available topics with larger weights are prioritized.

### Adaptive Scheduling
When the user marks topics completed or skipped, those topics are excluded from the next generated schedule. Regenerating the plan therefore adapts the remaining workload.

## 12. Example Workflow
1. Add "Java Basics".
2. Add "OOP".
3. Add "Collections".
4. Add dependencies:
   - Java Basics -> OOP
   - OOP -> Collections
5. Generate a plan.
6. Mark Java Basics as completed.
7. Generate again.
8. StudyForge creates a plan for the remaining dependency-valid topics.

## 13. Design-to-Rubric Mapping
| Requirement | Implementation |
|---|---|
| 3+ functional modules | Graph, Scheduling, Progress, Persistence |
| 4+ non-functional requirements | Usability, Reliability, Performance, Maintainability, Error Handling |
| 5–10 meaningful classes/files | Multiple model, graph, algorithm, service and persistence classes |
| Algorithms | DFS, Kahn's algorithm, greedy scheduling, critical-path weighting |
| Testing | StudyForgeTest.java |
| Documentation | README.md and statement.md |
| Version control | Git-ready project structure |

## 14. Important Note
The scheduling algorithms are heuristic/priority-based approaches. The application should not claim that a generated plan is mathematically optimal for every possible syllabus.
