---
name: kimi-worker
description: Spawn and manage multiple Kimi CLI agents via tmux to work on tasks in parallel. Use whenever a task can be decomposed into independent subtasks (e.g. batch triage, parallel fixes, multi-file refactors). When kimi and tmux are available, prefer this over the built-in Task tool for parallelism.
---

# kimi-worker

Orchestrate multiple Kimi CLI (`kimi`) agents running in parallel, each in its
own git worktree and tmux session.

**When to use:** Whenever you identify that a task can be split into independent
subtasks — don't wait for the user to ask for parallelism. Examples:
- User says "triage all open issues updated in the last 7 days" → fetch the
  issue list, then spawn one kimi worker per issue.
- User says "refactor these 5 modules" → one worker per module.
- User says "fix lint errors across packages" → one worker per package.

**Replaces Task tool:** When `kimi` and `tmux` are available in the
environment, use kimi workers instead of the built-in Task (subagent) tool.
Kimi workers are full-featured agents with their own file system access, shell,
and isolated worktree — far more capable than subagents.

## Preflight check

```bash
command -v kimi && command -v tmux
```

Both must succeed. The project must be a git repository.

## Naming convention

Git branch and worktree directory share a **task name**:

```
<type>-<issue number (optional)>-<short description>
```

The tmux session adds a `kimi-worker-` prefix so workers are easy to filter:

| | Format | Example |
|---|---|---|
| Task name | `<type>-<number>-<desc>` | `issue-836-prompt-dollar-sign` |
| Git branch | same as task name | `issue-836-prompt-dollar-sign` |
| Worktree dir | `<project>.worktrees/<task>` | `jmc-mcp.worktrees/issue-836-prompt-dollar-sign` |
| tmux session | `kimi-worker-<task>` | `kimi-worker-issue-836-prompt-dollar-sign` |

More examples:
- `issue-518-mcp-config-isolation`
- `fix-share-dir-skills-path`
- `feat-ask-user-tool`
- `refactor-jinja-templates`

List only kimi workers: `tmux ls | grep ^kimi-worker-`

## Usage

Prefer tmux + interactive kimi for all tasks. It supports multi-turn dialogue,
the user can `tmux attach` to inspect or intervene, and you can send follow-up
prompts from outside.

### Spawn a worker

```bash
NAME="issue-836-prompt-dollar-sign"         # task name
SESSION="kimi-worker-$NAME"                 # tmux session name
PROJECT_DIR="$(pwd)"
WORKTREE_DIR="$PROJECT_DIR.worktrees"

# 1. Create worktree (skip if exists)
git worktree add "$WORKTREE_DIR/$NAME" -b "$NAME" main 2>/dev/null

# 2. Launch interactive kimi inside tmux (using -y for auto-approve)
tmux new-session -d -s "$SESSION" -x 200 -y 50 \
  "cd $WORKTREE_DIR/$NAME && kimi -y"
```

### Send a prompt

The Kimi TUI needs time to initialize before it accepts input.
After launching a session, **wait at least 5 seconds** before sending
a prompt. Then send the text followed by `Enter`. If the prompt stays
in the input field without being submitted, send an additional `Enter`.

```bash
sleep 5  # wait for Kimi TUI to initialize
tmux send-keys -t "$SESSION" "Your prompt here" Enter
# If it doesn't submit, send another Enter:
# tmux send-keys -t "$SESSION" Enter
```

### Peek at output

```bash
tmux capture-pane -t "$SESSION" -p | tail -30
```

### Attach for hands-on interaction

```bash
tmux attach -t "$SESSION"
```

### Parallel fan-out

```bash
TASKS=(
  "issue-518-mcp-config-isolation|Triage #518: MCP config isolation problem. Analyze root cause, provide fix."
  "issue-836-prompt-dollar-sign|Triage #836: prompt failure when including $. Analyze root cause, provide fix."
)

PROJECT_DIR="$(pwd)"
WORKTREE_DIR="$PROJECT_DIR.worktrees"

for entry in "${TASKS[@]}"; do
  NAME="${entry%%|*}"
  PROMPT="${entry#*|}"
  SESSION="kimi-worker-$NAME"
  git worktree add "$WORKTREE_DIR/$NAME" -b "$NAME" main 2>/dev/null
  tmux new-session -d -s "$SESSION" -x 200 -y 50 \
    "cd $WORKTREE_DIR/$NAME && kimi -y"
  sleep 5  # wait for Kimi TUI to fully initialize
  tmux send-keys -t "$SESSION" "$PROMPT" Enter
done
```

### Fallback: Non-interactive `kimi --print`

Only use `--print` when you explicitly don't need multi-turn follow-up and just want to generate output. The `--print` mode auto-dismisses AskUserQuestion and auto-approves tools.

```bash
cd "$WORKTREE_DIR/$NAME"
kimi --print -p "Your prompt here" > "/tmp/$NAME-result.md"
```

## Lifecycle management

List active workers:

```bash
tmux ls | grep ^kimi-worker-
```

Kill a finished worker:

```bash
tmux kill-session -t "kimi-worker-$NAME"
```

Clean up worktree after merging:

```bash
tmux kill-session -t "kimi-worker-$NAME" 2>/dev/null
git worktree remove "$WORKTREE_DIR/$NAME"
git branch -d "$NAME"
```

Batch cleanup of dead sessions:

```bash
tmux list-sessions -F '#{session_name}:#{pane_dead}' \
  | grep ':1$' \
  | cut -d: -f1 \
  | xargs -I{} tmux kill-session -t {}
```