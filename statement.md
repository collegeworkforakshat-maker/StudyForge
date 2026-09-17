# StudyForge — Project Statement

## Problem Statement
Traditional study planners generally treat syllabus topics as independent tasks. In practice, many topics have prerequisites, different workloads, different difficulty levels, and different deadlines. A student who studies a dependent topic before its prerequisite can waste time or develop knowledge gaps.

StudyForge solves this problem by representing a syllabus as a directed dependency graph and generating a dependency-aware study schedule.

## Scope
The project covers:
- Topic and prerequisite management
- Dependency graph validation
- Cycle detection
- Dependency ordering
- Daily schedule generation
- Progress tracking
- Adaptive schedule regeneration
- Local data persistence

The project is designed as a Java console application.

## Target Users
- College students
- Students preparing for examinations
- Learners managing technical subjects with prerequisite relationships

## High-Level Features
1. Create and manage syllabus topics.
2. Define prerequisite relationships.
3. Detect and reject circular dependencies.
4. Generate a dependency-valid study plan.
5. Select between two scheduling strategies.
6. Track topic completion status.
7. Recalculate the remaining schedule after progress changes.
8. Save and restore data.
