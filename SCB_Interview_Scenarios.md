# SCB Senior Development Manager 面试场景题库

## 快速入口
- STAR 故事卡片：`SCB_Story_Cards_STAR.md`
- HM 追问速查：`SCB_HM_Followups_CheatSheet.md`

> 使用建议：按面试高频把下面题目当“口袋卡片”练。
> - 开场 3 题：Lead-08（自我介绍延展/团队规模）、Biz-06（Scope creep/CR）、Gov-06（资源冲突/取舍）
> - 技术深挖 3 题：Tech-05（质量危机）、Tech-06（安全红线）、Tech-07（性能/排查）
> - 业务流程 2 题：Biz-07（Settlement/Validation 思路）、Biz-08（Trade control/controls）

## 1. 技术架构与稳定性 (Tech)

### Tech-01: 遗留系统改造 (Legacy Modernization)
**场景:** "We have a monolithic Java application that is 10 years old. It's fragile and hard to deploy. Business wants new features fast. How do you handle this?"
*   **H (Headline):** 用渐进式改造替代重写，把发布风险拆小。
*   **E (Effect):** 缩短发布周期、降低 blast radius，同时不影响业务持续交付。
*   **R (Rationale):** 单体重写=长周期+高不确定性；选择绞杀者模式，先从低耦合边缘模块开始，逐步替换。
*   **O (Operations):**
    1.  **No Big Bang:** 明确重写风险不可控，先做可观测与发布流程基线。
    2.  **Strangler Fig:** 识别边缘模块，按业务能力拆分为微服务，分阶段上线。
    3.  **ACL:** 新老系统之间加防腐层，隔离旧模型与脏数据。
    4.  **Release 改善:** Canary/灰度 + 自动化回归 + 回滚预案，做到“小步快跑”。

### Tech-02: 生产环境性能瓶颈 (Performance Spike)
**场景:** "During a market volatility event (e.g., Non-Farm Payrolls), our trading system latency spiked to 5 seconds. Traders are furious."
*   **H (Headline):** 先止血把延迟拉回可用区间，再定位根因并做结构性修复。
*   **E (Effect):** 保障交易链路可用（SLA/客户体验），避免波动期间扩大损失。
*   **R (Rationale):** 高波动下先恢复服务能力；根因必须用证据（trace/metrics）而不是猜测。
*   **O (Operations):**
    1.  **Mitigation:** 启用降级开关/限流，关闭非核心功能（如实时报表）。
    2.  **Evidence:** 用 OpenTelemetry 拆分时间，区分 DB 锁/连接池耗尽/GC/下游慢。
    3.  **Backpressure:** 对下游慢依赖加背压、超时、熔断与队列上限，防止级联。
    4.  **Hotspot:** Redis 缓存热点数据或预计算，减少高峰 DB 压力。

### Tech-03: 跨国数据合规 (Data Residency)
**场景:** "We need to deploy a feature for Singapore users, but the data cannot leave Singapore. Our servers are in Hong Kong."
*   **Action:**
    1.  **架构调整:** 采用 **多区域部署 (Multi-Region Deployment)**，在新加坡建立独立的数据存储节点。
    2.  **数据路由:** 在 API Gateway 层根据用户归属地路由流量。
    3.  **合规审查:** 与 Legal/Compliance 团队确认数据分类 (Data Classification)。

### Tech-04: 技术选型 (Why Spring Boot?)
**场景:** "Why did you choose Spring Boot for microservices? Why not Go or Node.js?"
*   **Action:**
    1.  **生态成熟度:** Spring Boot/Cloud/Integration/Batch 在企业里“可治理、可运维”，上手快且标准化程度高。
    2.  **工程化:** Actuator/Health check/Observability 接入成熟，配合 CI/CD 和容器化可以快速形成流水线交付。
    3.  **人才和风险:** Java 人才密度高、交接成本低；在强监管环境下，稳定性与可控性优先。

### Tech-05: 线上故障频发 (Quality Crisis)
**场景:** "The team is stuck in a 'fire-fighting' mode. Production bugs happen every week, and there is no time to write automated tests."
*   **H (Headline):** 用质量闸门 + 技术债配额，停止“每周救火”的循环。
*   **E (Effect):** 降低 defect leakage、减少生产事故、恢复交付确定性。
*   **R (Rationale):** 不设门槛=持续累债；短期“快”换来长期更慢，必须先把质量变成交付系统的一部分。
*   **O (Operations):**
    1.  **Quality Gate:** 关键路径必须有测试/检查才能合并（分层做，不是一刀切全覆盖）。
    2.  **RCA:** 复盘近 5 次事故，抽象出共性模式（例如缺集成/契约测试、环境差异）。
    3.  **Capacity:** 每个 Sprint 固定 20% 用于补齐核心链路自动化与稳定性改造。

### Tech-06: 紧急安全漏洞 (Security Blocker)
**场景:** "One day before the big launch, a Pen Test reveals a critical security vulnerability. Fixing it requires a major refactor. Business says 'Go Live' is non-negotiable."
*   **H (Headline):** 安全红线问题不带病上线；要么修复，要么正式风险接受。
*   **E (Effect):** 避免重大合规/监管风险与声誉损失，保护牌照与客户信任。
*   **R (Rationale):** 风险不可由 IT 单方面承担；必须用控制与证据让决策可审计。
*   **O (Operations):**
    1.  **Triage:** 明确漏洞等级与攻击面，评估是否影响核心交易链路。
    2.  **Mitigation:** 看是否可用 WAF/网络隔离/配置降权临时降低风险。
    3.  **Fix vs Delay:** 能修就修并补测；不能修就推动延期。
    4.  **Risk Acceptance:** 若业务坚持，要求 Business Head/风险负责人签署风险接受与补救计划（含截止日期与证据）。

### Tech-07: 线上事故排查与止血 (Incident Debugging)
**场景:** "After a release, one market (e.g., AU) reports the feature is broken, but other markets look fine. Test data is hard to create, and business wants an immediate fix."
*   **Action:**
    1.  **先止血:** Feature flag / rule-based routing，先把影响范围控制在最小（按 market/segment 隔离）。
    2.  **日志与证据:** 先看日志与链路（log/trace/metrics），定位真实受影响的请求路径和条件分支。
    3.  **临时方案 + 永久修复:** 给出 workaround（配置/规则）保证业务可用，同时安排补测与补丁。
    4.  **改进机制:** 强化影响分析、code review 与“market coverage matrix”；引入 diff report/配置差异报告避免再次漏测。

---

## 2. 金融业务与风险 (Biz)

### Biz-01: 业务强行上线 (Push to Prod)
**场景:** "The Business Head insists on deploying a feature this Friday evening because 'clients need it'. QA testing is only 80% complete."
*   **Action:**
    1.  **坚守底线:** 引用银行的 **Change Management Policy**，周五严禁变更。
    2.  **量化风险:** "如果上线失败，周末无法回滚，周一开市将导致 $X 的潜在损失。"
    3.  **提供选项:** "我们可以周五在 UAT 环境给客户演示 (Demo)，周一早晨第一批次上线 (Canary Release)。"

### Biz-02: 审计发现项逾期 (Overdue Audit Finding)
**场景:** "An internal audit finding regarding 'Privileged Access Management' is overdue. The team is fully booked with project work."
*   **Action:**
    1.  **最高优先级:** 立即暂停部分 Feature 开发 (De-scope)。
    2.  **短期修复:** 手动收回权限，保留工单记录作为临时证据。
    3.  **长期修复:** 接入银行统一的 IAM 系统 (如 CyberArk)，实现自动化管理。

### Biz-03: 需求蔓延 (Scope Creep)
**场景:** "We are in the final phase of the project (UAT), but the Business keeps adding 'small changes'. The deadline is fixed."
*   **Action:**
    1.  **冻结变更 (Change Freeze):** 宣布进入 "Code Freeze" 阶段，只修 Bug，不加 Feature。
    2.  **变更请求流程 (CR Process):** 任何新需求必须走正式 CR 流程，评估对 Timeline 的影响。
    3.  **谈判 (Negotiation):** "我们可以加这个功能，但必须把另一个同等大小的功能移到 Phase 2 (Swap Scope)。"

### Biz-04: 利益冲突协调 (Stakeholder Conflict)
**场景:** "Two different Business Units (FX and Equities) both want their features to be Top Priority (P0) on your shared platform. You can't do both."
*   **Action:**
    1.  **透明化 (Transparency):** 建立统一的 Backlog 看板，让双方都看到资源的有限性。
    2.  **建立治理委员会 (Steering Committee):** 召开联合会议，让两个 BU 的老板面对面讨论。你的角色是“主持人”和“数据提供者”，而不是“裁判”。
    3.  **建议方案:** "我们可以先做 FX 的后端逻辑和 Equities 的前端页面，下个 Sprint 再互补。"（寻找共赢点）。

### Biz-05: 合规项目高压交付 (45-day Compliance Go-Live)
**场景:** "A compliance-driven project must go live in 45 days. It needs approvals from Security, Legal, Brand, and multiple global committees with fixed weekly windows. One delay means failure."
*   **Action:**
    1.  **关键路径拆解:** 把“审批窗口”当成关键路径（Critical Path），倒排计划；先锁定审批材料和 owner。
    2.  **主动协调:** 提前预约评审、一次性准备材料；建立 escalation path（找对人、用对渠道）。
    3.  **风险闭环:** 审计发现（如传输未加密）立刻拉齐 TISO/Infra/Vendor 评估方案，选可落地且可审计的方案。
*   **Result:** 在 deadline 前按时上线，确保合规；可补充：项目获得认可/奖项（如 Platinum Award）。

### Biz-06: 需求蔓延与变更治理 (Scope Creep & Change Control)
**场景:** "We are in UAT/final phase, but the Business keeps requesting 'small changes'. The deadline is fixed and risk is rising."
*   **H (Headline):** 进入 UAT 后执行变更冻结：只做 bugfix，需求走 CR 并由业务做取舍。
*   **E (Effect):** 防止上线风险失控，保证 cutover 成功率与时间窗口。
*   **R (Rationale):** “小改动”叠加会显著拉高回归与回滚复杂度；取舍权必须回到业务侧。
*   **O (Operations):**
    1.  **Freeze:** Code Freeze 只收 bugfix；所有新需求进入 CR 队列。
    2.  **Impact:** 每个 CR 标准化影响评估（dev/test/security/cutover/rollback）。
    3.  **One-in-One-out:** 新增必须对应 de-scope/phase2；必要时上升到 Steering/CAB。

### Biz-07: Settlement/Validation 思路（不装懂也能打）
**场景:** "Explain how you understand settlement operation model and the validation process."
*   **Action (答题框架):**
    1.  **先画流程:** Trade capture → enrichment → validation → matching/confirmation → settlement instruction → settlement → reconciliation。
    2.  **再说校验点:** 静态数据校验（SSI/账户/币种/限额）、时效校验（cut-off）、一致性校验（duplicate/mismatch）、异常处理（repair queue）。
    3.  **最后谈控制:** 四眼原则（maker-checker）、审计留痕（evidence）、回滚/补偿、对账与例外管理。

### Biz-08: Trade Control 视角下的风控 (Controls & Measures)
**场景:** "Given an operational process (trade control/settlement), how do you identify key issues and put controls in place?"
*   **Action (答题框架):**
    1.  **找高风险节点:** 人工介入、跨系统 handoff、批处理窗口、外部对手方接口、手工更改数据。
    2.  **控制手段:** 校验/闸门（validation gate）、权限分离、阈值告警、强制对账、异常队列、SLA 与升级机制。
    3.  **度量与治理:** 缺陷泄漏率、例外队列 backlog、STP rate、MTTR、审计发现项 closure。

---

## 3. 交付与质量管控 (Gov)

### Gov-01: 供应商交付质量差 (Vendor Quality)
**场景:** "Our vendor delivered the code late, and it's full of bugs. We have a deadline in 2 weeks."
*   **Action:**
    1.  **启动 Plan B:** 调派内部 Senior Dev 进行 **Code Review** 和 **Pair Programming**，接管核心逻辑。
    2.  **商业施压:** 依据合同 SLA 条款，正式发函警告，要求对方更换资深开发人员。
    3.  **长期策略:** 识别该模块为核心能力，制定 **In-sourcing (自研)** 计划。

### Gov-02: 跨团队依赖阻塞 (Dependency Hell)
**场景:** "Your team (Frontend) cannot finish the task because the Backend team (another department) hasn't exposed the API yet."
*   **Action:**
    1.  **契约测试 (CDC):** 先定义好 API 契约 (Swagger/OpenAPI)，双方达成一致。
    2.  **Mock Server:** 前端基于契约使用 Mock 数据开发，不等待后端。
    3.  **SoS 会议:** 在 Scrum of Scrums 会议上升级该依赖风险，要求后端给出确切交付日期。

### Gov-03: 预算削减 (Budget Cut)
**场景:** "Management cut your budget by 20% for next year. How do you handle the roadmap?"
*   **Action:**
    1.  **MoSCoW 排序:** 与业务方坐下来，重新划分 Must have / Should have。
    2.  **FinOps:** 审查云资源账单，关闭闲置环境，优化实例类型，节省 10% 成本。
    3.  **自动化:** 投资自动化测试，减少外包测试人员的人力成本。

### Gov-04: 资源冲突 (Resource Constraint)
**场景:** "Business wants Feature A and Feature B delivered next month. You only have resources for one. Both are 'Critical'."
*   **Action:**
    1.  **量化价值 (Value Quantification):** "Feature A 带来 $1M 营收，Feature B 减少 $500k 风险。哪个更重要？"
    2.  **寻找替代方案:** "我们可以先做 Feature A 的 MVP 版本，同时做 Feature B 的核心逻辑。"
    3.  **升级决策 (Escalation):** 如果无法达成一致，将决策升级到 Steering Committee，让业务高层决定。

### Gov-05: 流程混乱治理 (Chaos to Order)
**场景:** "You joined a team where developers commit directly to master, there are no Code Reviews, and deployments are manual and error-prone."
*   **Action:**
    1.  **定义工作流 (GitFlow/Trunk-based):** 立即确立分支管理策略，禁止直推 Master。
    2.  **强制 Code Review:** 在 GitLab/GitHub 设置保护分支，必须有 1 个以上 Senior Dev 批准才能合并。
    3.  **自动化流水线:** 搭建最基础的 CI/CD 流水线，至少实现“一键构建”和“一键部署到 UAT”，消除人工操作失误。

### Gov-06: 资源受限下的取舍 (2 Features but Only 1 Capacity)
**场景:** "Business wants two features, but you only have capacity for one in the given timeline. What do you do?"
*   **H (Headline):** 我不替业务拍板，用数据把决策透明化并给出可落地选项。
*   **E (Effect):** 降低政治成本与反复拉扯，确保交付可预测且团队可持续。
*   **R (Rationale):** “两个都要”会导致两个都交付不了；必须把 trade-off 显性化。
*   **O (Operations):**
    1.  **Quantify:** 统一量化维度：收益、监管影响、运营风险、客户影响、复杂度、交付信心。
    2.  **Options:** A 全量做 1；B 全量做 2；C 两个 MVP（明确 scope 与风险）。
    3.  **Governance:** 达不成一致就走 Steering/CAB 决策并记录原因。
    4.  **Sustainability:** 不用“隐性加班”解决问题；若要加速则谈资源或 de-scope。

### Gov-07: 预算与成本（Budget Process）
**场景:** "You are involved in project budgeting. How do you run the budget process and keep control?"
*   **Action:**
    1.  **拆解成本:** 人力（FTE/Vendor）、环境与工具、测试与安全、上线与运维。
    2.  **建立基线:** 以里程碑/交付物建立 baseline，滚动预测（rolling forecast）。
    3.  **透明化与纠偏:** 每周 burn 追踪；发现偏差立刻做 trade-off（de-scope/延后/追加资源）。

---

## 4. 领导力与人员管理 (Lead)

### Lead-01: 天才混蛋 (Brilliant Jerk)
**场景:** "Your best coder is toxic. He insults junior devs in code reviews but delivers the most complex features."
*   **Action:**
    1.  **私下反馈:** "你的技术很强，但你的行为正在伤害团队的心理安全感 (Psychological Safety)。这是不可接受的。"
    2.  **设定红线:** 明确告知，如果行为不改，技术再好也会影响绩效 (Values rating)。
    3.  **劝退:** 如果持续不改，为了保护团队文化，启动 PIP 或劝退流程。

### Lead-02: 团队士气低落 (Low Morale)
**场景:** "The team is tired of maintaining a 15-year-old legacy system. They feel they are learning nothing new."
*   **Action:**
    1.  **创新时间:** 设立 **"10% Innovation Time"**，允许周五下午研究新技术。
    2.  **工具引入:** 引入 **GitHub Copilot** 或自动化工具，减少重复劳动。
    3.  **愿景对齐:** 解释维护老系统的商业价值，同时制定长期的迁移路线图，让大家看到希望。

### Lead-03: 向上管理 (Managing Up)
**场景:** "Your boss (CIO) promises a delivery date to the CEO without consulting you. It's impossible to achieve."
*   **Action:**
    1.  **不直接说 No:** "我理解这个日期的重要性。"
    2.  **展示数据:** "基于目前的团队速率 (Velocity) 和范围 (Scope)，这是我们的燃尽图 (Burndown Chart)。"
    3.  **提供权衡 (Trade-off):** "要达到这个日期，我们需要：A) 增加 3 个高级人力；或者 B) 砍掉 40% 的非核心功能。您怎么选？"

### Lead-04: 员工激励与辅导 (Coaching & Motivation)
**场景:** "You have a junior dev who is eager but buggy, and a senior dev who is capable but disengaged (hard to find)."
*   **Action:**
    1.  **针对 Junior (技能辅导):**
        *   **结对编程 (Pair Programming):** 安排 Senior 带他写核心代码。
        *   **Code Review:** 严格把控代码质量，不仅仅是指出错误，更是解释 *为什么*。
        *   **自动化检查:** 引入 Linter 和 SonarQube，用工具拦截低级错误。
    2.  **针对 Senior (意愿激励):**
        *   **赋予使命感:** 让他负责架构设计或技术难题攻关，而不是简单的 CRUD。
        *   **导师角色:** 明确要求他指导 Junior，并将 "Mentorship" 纳入绩效考核。
        *   **灵活办公:** 如果他只是不喜欢坐班，可以给予一定的远程办公自由度，只要产出达标。
    3.  **团队建设 (Team Building):**
        *   **团建活动:** 定期组织技术分享、团建活动，增强团队凝聚力。
        *   **跨功能合作:** 鼓励与其他团队合作，拓宽视野的同时，增强对业务的理解。
        *   **心理安全:** 营造开放的氛围，鼓励团队成员表达想法和担忧，而不必担心被批评。

### Lead-05: 接手低绩效团队 (Turnaround Strategy)
**场景:** "You just took over a team that is known for missing deadlines and low quality. Morale is low. What is your 90-day plan?"
*   **Action:**
    1.  **Day 1-30 (观察与倾听):** 与每个成员进行 1:1 访谈，找出痛点（是技术太烂？流程太繁琐？还是管理层乱指挥？）。
    2.  **Day 31-60 (速赢 Quick Wins):** 解决 1-2 个最让团队头疼的阻碍（比如申请不到服务器、构建太慢），建立信任。
    3.  **Day 61-90 (建立新标准):** 设定明确的 KPI/OKR，引入敏捷仪式，淘汰 1 名明显的“破坏者”以立威，提拔 1 名“潜力股”以立信。

### Lead-06: 甩锅文化治理 (Blame Culture)
**场景:** "Whenever a production incident happens, the team starts pointing fingers: 'It's the Network team's fault', 'The DB team messed up'."
*   **Action:**
    1.  **免责复盘 (Blameless Post-mortem):** 建立规则——复盘会只谈 Process 和 System，不谈 People。禁止说 "Who did this?"，只能说 "How did the system allow this to happen?"
    2.  **领导担责:** 作为经理，对外（向业务/高层）承担所有责任，对内保护团队。
    3.  **跨团队协作:** 邀请 Network/DB 团队一起参加复盘，建立协作而非对抗的关系。

### Lead-07: 从 0 到 90+ 的团队搭建 (Build a Site from Scratch)
**场景:** "You need to build a new delivery site from scratch and make it productive quickly. What do you do?"
*   **Action:**
    1.  **分批招聘 + 角色结构:** 先招 10 个核心骨干（Tech lead/QA/DevOps/BA），再扩到 20、40，形成梯队。
    2.  **Onboarding 倒排:** 以入职日倒推申请 ID/设备/权限/软件；配合 HR/Vendor/TI 形成标准流程。
    3.  **四周训练营 + 导师制:** 每周计划、实战任务、buddy/mentor，缩短 time-to-productivity。
*   **Result:** 团队从 0 扩展到 90+，形成多支可独立交付的核心团队。

### Lead-08: 跨区域团队协作（尊重 + 流程）
**场景:** "How do you manage a global team with different cultures/timezones and personal constraints (e.g., prayer time)?"
*   **Action:**
    1.  **尊重先行:** 尊重信仰/文化差异，明确“可接受的工作方式”边界（不影响 SLA/交付）。
    2.  **流程与透明:** 统一节奏（standup/weekly sync）、异步沟通（written design/status）、清晰 escalation。
    3.  **信任建立:** 以结果和可靠性建立信任，避免微观管理。

### Lead-09: 救火项目恢复节奏 (Project Rescue)
**场景:** "A project is close to failure. New hires, new tech stack, overtime 7x16, morale collapsed, escalations every day. You're asked to rescue it."
*   **H (Headline):** 先把团队从 7x16 的崩溃节奏拉回可持续交付，并快速降级 escalations。
*   **E (Effect):** 稳住士气与交付节奏，避免持续升级影响客户与管理层信任。
*   **R (Rationale):** 救火不是更拼命，而是重建系统：职责清晰、测试前置、blocker 透明。
*   **O (Operations):**
    1.  **Reset rhythm:** 明确边界与节奏（例如承诺周日不加班），先止住 burnout。
    2.  **Re-org:** 拆清 dev 与 test-support 职责，建立“关键专家”二线支持。
    3.  **Shift-left:** 测试前置，daily standup 聚焦 blocker 清除与优先级。
*   **Result:** 一周内恢复节奏，升级减少，最终按期交付。

### Lead-10: 业务方管理（数据驱动的变更决策）
**场景:** "Business requests a last-minute change or asks to restore a deprecated feature during go-live."
*   **Action:**
    1.  **不情绪化对抗:** 先做影响评估（dev/test/security/cutover）。
    2.  **用数据说话:** 用访问数据/漏斗数据证明影响范围与最优方案。
    3.  **给出折中方案:** 临时 workaround + 长期方案（兼顾短期诉求与长期架构）。

### Lead-11: Site Governance（本地治理与合规）
**场景:** "As a site manager, how do you ensure local governance, information security, and operational discipline?"
*   **Action:**
    1.  **政策落地:** 关键政策（InfoSec/Compliance）培训与抽查；违规零容忍。
    2.  **运营机制:** 访客接待、设备与权限管理、供应商与合同管理、日常运营支持。
    3.  **文化与影响力:** Tech talk/hackathon/校园合作，提升凝聚力与人才管道。
