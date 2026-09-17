# System Architecture

```text
                +----------------------+
                |       Main CLI       |
                +----------+-----------+
                           |
          +----------------+----------------+
          |                |                |
          v                v                v
 +----------------+ +-------------+ +------------------+
 | Syllabus Graph | |  Progress   | |   Persistence    |
 |    Module      | |   Tracker   | |     Module       |
 +-------+--------+ +------+------+ +--------+---------+
         |                 |                 |
         v                 v                 v
 +-----------------------------------------------------+
 |                    Domain Models                    |
 | Topic | StudySession | StudyPlan | Status           |
 +-----------------------------------------------------+
         |
         v
 +------------------------+
 | Scheduling Abstraction |
 |      Scheduler         |
 +-----------+------------+
             |
       +-----+------+
       |            |
       v            v
 +-----------+ +-------------+
 |  Greedy   | |    CPM      |
 | Scheduler | |  Scheduler  |
 +-----------+ +-------------+
```

## Data Flow
User input -> Topic/Dependency Model -> Graph Validation -> Scheduling Algorithm -> StudyPlan -> Console Output.

Progress changes -> ProgressTracker -> Regenerate Scheduler -> Updated StudyPlan.
