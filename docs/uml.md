# UML Design Notes

## Use Cases
- Manage Topics
- Define Dependencies
- Generate Study Plan
- Track Progress
- Save/Load Data

## Main Classes
- Topic
- StudySession
- StudyPlan
- SyllabusGraph
- Scheduler
- GreedyScheduler
- CPMScheduler
- ProgressTracker
- PersistenceManager
- Main

## Key Relationships
- SyllabusGraph aggregates Topic objects.
- StudyPlan aggregates StudySession objects.
- StudySession references a Topic.
- GreedyScheduler and CPMScheduler implement Scheduler.
- ProgressTracker operates on SyllabusGraph.
- PersistenceManager serializes/deserializes SyllabusGraph data.
