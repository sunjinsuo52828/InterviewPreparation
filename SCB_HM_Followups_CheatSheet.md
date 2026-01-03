# SCB Hiring Manager Follow-ups (Cheat Sheet)

> 目标：每题准备 2-3 个要点 + 1 个数字 + 1 个 trade-off。

## 1) Delivery / Execution
- How did you ensure the plan was realistic?
- What did you *stop* doing to regain control?
- What were the 3 biggest risks and how did you mitigate them?
- What metrics did you track weekly (burn-down, defect leakage, cycle time, MTTR)?
- What was your rollback plan?

## 2) Governance / Controls / Audit Evidence
- What controls did you put in place (maker-checker, approvals, logging, access)?
- How did you prove closure (evidence, screenshots, tickets, sign-offs)?
- How do you handle change requests during UAT or code-freeze?
- How do you run CAB/CCB decisions?

## 3) Settlement / Validation / Trade Control (Answer Framework)
- Describe the process from trade capture to settlement and reconciliation.
- Where are the highest-risk failure points and what controls apply?
- How do you handle exceptions/repair queues and SLA breaches?
- What does "good" look like (STP rate, breaks backlog, cut-off adherence)?

## 4) Technology Decisions
- Why this architecture? What alternatives did you consider?
- How do you ensure resiliency (timeouts, retries, circuit breaker, DLQ)?
- How do you observe issues (logs/traces/metrics) and reduce MTTR?
- How do you ensure backward compatibility across markets?

## 5) Quality & Release Readiness
- What is your test strategy (pyramid, contract tests, UAT)?
- What are your release gates? Who can override them?
- How do you prevent defect leakage?
- Describe a postmortem you ran and what changed afterward.

## 6) Security & Data
- What would block a release? Who signs risk acceptance?
- How do you handle encryption in transit/at rest?
- How do you handle data residency and cross-border flows?
- What is your approach to secrets, RBAC, and least privilege?

## 7) Leadership & People
- How do you scale a team from 0 to 90+ without chaos?
- How do you handle a low performer? What is your coaching/PIP structure?
- How do you onboard new hires and reduce time-to-productivity?
- How do you handle cross-cultural constraints and still deliver?

## 8) Stakeholders & Conflict
- Tell me about a time you said "no" to Business.
- How do you handle competing priorities across BUs?
- How do you manage escalations upward?
- How do you ensure shared ownership of trade-offs?

