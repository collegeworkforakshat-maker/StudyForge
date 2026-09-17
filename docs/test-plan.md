# Test Plan

| Test | Expected Result |
|---|---|
| Add unique topic | Topic is stored |
| Add duplicate topic ID | Operation rejected |
| Add valid dependency | Edge is added |
| Add self dependency | Operation rejected |
| Add dependency creating cycle | Operation rejected |
| Topological sort | Prerequisites appear before dependents |
| Generate greedy plan | Dependency-valid sessions are produced |
| Generate CPM plan | Critical-path-weighted sessions are produced |
| Mark topic completed | Topic excluded from regenerated plan |
| Save/load | Topic state and dependencies persist |
