# Process Workflow

```text
START
  |
  v
Add syllabus topics
  |
  v
Add prerequisites
  |
  v
Validate dependency
  |
  +---- cycle? ---- YES ---> Reject dependency
  |                          |
  NO                         |
  |<-------------------------+
  v
Choose scheduler
  |
  v
Topological/dependency validation
  |
  v
Generate daily sessions
  |
  v
Display study calendar
  |
  v
Update progress
  |
  v
Regenerate remaining schedule
  |
  v
END
```
