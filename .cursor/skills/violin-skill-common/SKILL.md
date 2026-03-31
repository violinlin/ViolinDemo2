---
name: violin-skill-common
description: Implement common ViolinDemo feature pages using Fragment architecture. Use when creating new demo pages in this project, especially when user asks to avoid Activity and follow ProtoDemoFragment-style BaseFragment + ViewBinding patterns.
---

# Violin Common Feature Skill

## Purpose

Apply the project's preferred implementation pattern for new demo features in ViolinDemo.

## Core Rules

1. Do not create a new Activity unless the user explicitly asks for Activity.
2. Prefer Fragment entry pages, and register them through existing list/navigation flow.
3. New Fragment should follow `ProtoDemoFragment` style:
   - Inherit `BaseFragment<VB>`
   - Implement `createBinding(inflater, container)`
   - Use `binding` in `initView()` for view setup
4. Layout must be implemented with ViewBinding-compatible XML under module `res/layout`.
5. Keep implementation inside the appropriate feature module; avoid cross-module leakage.
6. Reuse existing project infrastructure (e.g. `DetailActivityData`, `SchemeUtils`, existing base utils) instead of introducing a new routing pattern.

## Default Implementation Checklist

- [ ] Add/adjust layout XML in target feature module.
- [ ] Create Fragment class extending `BaseFragment<...Binding>`.
- [ ] Implement `createBinding` and `initView`.
- [ ] Wire click events and demo logic in Fragment.
- [ ] Add entry in `MainActivity.loadMainList()` (or existing module list page).
- [ ] Keep changes minimal and focused on the requested feature.

## Reference Pattern

Use this file as the baseline reference structure:

- `/Users/wanghuilin/violin/myworkspace/ViolinDemo2/features/common/src/main/java/com/violin/features/common/proto/ProtoDemoFragment.kt`

## Notes

- If IDE temporarily reports unresolved generated binding classes/resources, keep code aligned with module namespace and ViewBinding conventions first, then rely on Gradle sync/index refresh.
- When uncertain about structure, choose consistency with existing Fragment demos over introducing new architecture.
