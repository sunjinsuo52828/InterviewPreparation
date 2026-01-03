# SCB Interview Story Cards (STAR)

> 用法：每张卡练 3 个版本：30s / 60s / 2min。最后补 3 个数字（影响范围/节省时间/风险降低）。

## Card 1 — Project Rescue (Delivery Under Pressure)
**Theme:** Turnaround, execution, team rhythm, stakeholder management

**S (Situation):** A project was near failure: new hires + new tech stack, working 7×16, daily escalations, morale dropping. I was brought in to stabilize and deliver.

**T (Task):** Restore a sustainable delivery rhythm quickly, reduce escalations, and get the project back to an on-time trajectory without compromising quality.

**A (Actions):**
- Reset operating model: clarified roles (dev vs test/support) and established a daily blocker-clearing routine.
- Rebuilt confidence: explicitly protected recovery time (e.g., no Sunday overtime) to prevent burnout and regain focus.
- Strengthened technical backbone: identified and empowered domain/technical experts to provide “second-line support” for tough issues.
- Brought testing forward: shifted left on validation, prioritized top risk paths, and forced a single source of truth for release readiness.

**R (Results):**
- Within ~1 week, the team returned to a sustainable rhythm and escalations dropped.
- Project stabilized and delivered on time.

**Follow-ups to prepare:**
- What were the top 3 root causes?
- How did you measure recovery (MTTR, defect leakage, escape rate)?
- What did you stop doing to regain control?

---

## Card 2 — Compliance Go-Live in 45 Days (Cross-Committee Delivery)
**Theme:** Governance, controls, planning against approval windows, risk closure

**S (Situation):** A compliance-driven project had a hard deadline of 45 days. Multiple approvals (Security/Legal/Brand/global committees) had fixed weekly windows—missing one window could fail the deadline.

**T (Task):** Deliver on time and ensure control evidence is audit-ready.

**A (Actions):**
- Treated approvals as the critical path: reverse-planned from committee windows; locked owners for each approval artifact.
- Prepared “one-shot” packs: requirements, data flow, risk assessment, and mitigations—minimizing ping-pong.
- Ran a tight RAID loop: tracked risks/issues/assumptions/dependencies daily; escalated early.
- Closed security gaps fast (e.g., encryption in transit): aligned TISO/infra/vendor on a feasible implementation and produced evidence.

**R (Results):**
- Launched before the deadline with compliance sign-off.
- Reduced audit/compliance risk by producing clean evidence and traceability.

**Follow-ups to prepare:**
- What were the top 2-3 risks and mitigations?
- How did you handle scope changes?
- What controls/evidence did you produce?

---

## Card 3 — Platform Upgrade Driven by CAP/Audit (Structured Risk Reduction)
**Theme:** Controls, audit remediation, platform modernization, stakeholder alignment

**S (Situation):** A core platform needed an upgrade (e.g., 8→9) with multiple CAP findings/audit observations. Business wanted features; risk teams wanted closure.

**T (Task):** Upgrade safely, reduce findings, and avoid production instability.

**A (Actions):**
- Built a remediation backlog mapped to findings: each item had owner, due date, and evidence definition.
- Created a controlled release plan: risk-based testing, regression scope, and phased rollout.
- Enforced change governance: freeze rules and CR gating to protect the upgrade.
- Improved observability: logging/metrics/tracing to detect regressions early.

**R (Results):**
- Upgrade completed with reduced findings and a cleaner control posture.
- Improved stability and shortened incident diagnosis time.

**Follow-ups to prepare:**
- How did you prove closure to auditors?
- What were the hardest compatibility issues?
- How did you avoid feature work derailing the upgrade?

---

## Card 4 — Building a Delivery Site (0 → 90+)
**Theme:** Leadership, hiring, onboarding, productivity ramp, operating model

**S (Situation):** Needed to build a new delivery site/team from scratch and make it productive quickly.

**T (Task):** Scale sustainably with clear roles, onboarding pipeline, and predictable delivery.

**A (Actions):**
- Phased hiring: recruited core leads first (tech lead/QA/devops/BA), then scaled teams in waves.
- Standardized onboarding: pre-created access/equipment checklists; partnered with HR/IT/vendor.
- Training camp + buddy system: structured weekly goals and hands-on tasks; measured time-to-first-PR and time-to-independent-delivery.
- Set delivery rituals: sprint cadence, written status, dependency board, and escalation paths.

**R (Results):**
- Grew from 0 to 90+ and formed multiple teams capable of independent delivery.

**Follow-ups to prepare:**
- How did you handle performance issues at scale?
- How did you maintain quality (gates, test strategy) while scaling?
- How did you integrate vendors?

---

## Card 5 — Stakeholder Conflict & Priority Trade-offs
**Theme:** Business partnering, prioritization, negotiation, transparency

**S (Situation):** Multiple business units needed urgent features, but capacity was limited and the timeline fixed.

**T (Task):** Enable an explicit decision with minimum politics and maximum transparency.

**A (Actions):**
- Translated asks into comparable units: value, regulatory impact, operational risk, complexity, and delivery confidence.
- Offered options (A/B/C): full feature 1, full feature 2, or MVP for both with clearly stated risks.
- Protected the team: avoided “silent overtime as a strategy”; negotiated scope/resources explicitly.

**R (Results):**
- Achieved a decision with shared ownership and reduced late-stage conflict.

**Follow-ups to prepare:**
- What data did you use?
- How did you handle an escalation to your boss?
- What did you do when stakeholders disagreed?

