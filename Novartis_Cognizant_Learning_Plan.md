# 学习与入职计划：从金融IT向制药行业应用运维交付的转型 (6周冲刺)

**目标职位**：Delivery Manager - Application Support (Cognizant for Novartis)
**申请人背景**：19年花旗银行(Citi)金融IT经验，具备深厚的高合规性、高可用性系统管理经验。

---

## 1. 核心策略：迁移与重塑 (Transfer & Adapt)

虽然我没有直接的制药行业经验，但金融(Finance)与制药(Pharma)在IT本质上高度相似：**强监管(Regulated)**、**数据完整性(Data Integrity)**、**审计追踪(Audit Trail)**以及对**高可用性(High Availability)**的极致追求。

本计划旨在利用我在金融领域积累的“流程纪律”和“风险意识”，在6周内快速补齐“行业领域知识”和“运维特定流程”的短板。

---

## 阶段 0：入职前准备 (Pre-boarding) - 建立认知框架
**目标**：扫盲行业术语，建立运维思维，确保Day 1能听懂“行话”。

### 重点 1：制药行业领域知识 (Domain Knowledge)
*   **法规与合规 (GxP)**:
    *   深入学习 **GxP** (GMP, GCP, GLP) 概念，理解其对IT系统的具体要求（如：为何生产系统不能随意重启）。
    *   研读 **21 CFR Part 11** (电子记录与电子签名) 和 **Data Integrity (ALCOA+原则)**。这是制药IT的“宪法”。
    *   了解 **CSV (计算机系统验证)** 与 **CSA (计算机软件保障)** 的区别，理解验证(Validation)在制药运维中的核心地位。
*   **业务流程概览**:
    *   学习药物生命周期：研发(R&D) -> 临床(Clinical) -> 制造(Manufacturing) -> 商业化(Commercial)。
    *   了解诺华(Novartis)的主要业务板块（创新药、仿制药等）及近期数字化转型战略。

### 重点 2：IT服务管理 (ITSM) 理论复习
*   **ITIL v4 框架**: 重温 Incident(事故), Problem(问题), Change(变更) 管理的标准流程。
*   **运维核心概念**: 熟悉 SLA (服务级别协议), OLA (运营级别协议), RTO/RPO (恢复时间/点目标)。

**输出**: 完成一份《制药行业IT术语速查表》（已着手准备）。

---

## 阶段 1：第 1-2 周 (入职初期) - 沉浸与摸底 (Immersion & Discovery)
**目标**：弄清“有什么系统”、“谁负责什么”、“红线在哪里”。

### 1. 应用系统图谱构建 (Application Landscape)
*   **系统盘点**: 梳理诺华的应用清单(Inventory)。区分关键等级(Business Criticality)：
    *   哪些是 **GxP系统**？（动任何配置都需要走严格变更流程）
    *   哪些是 **SOX系统**？（涉及财务报表）
*   **技术栈摸底**: 确认主要技术栈（Java, AWS, SQL, SAP, Veeva等）及当前的架构拓扑图。

### 2. 运维流程与工具 (Process & Tools)
*   **工具链上手**: 熟练使用 ITSM 工具 (如 ServiceNow, JIRA, Remedy)。
*   **知识库(KB) 审查**: 阅读现有的 SOP (标准作业程序) 和 Runbooks。
    *   *关键动作*：找出最近3个月的 Top 10 Incidents，复盘其处理过程，理解常见痛点。
*   **合规培训**: 完成诺华内部所有的强制性合规培训 (Mandatory Training)，拿到系统访问权限。

### 3. 团队与干系人 (Stakeholders)
*   **建立连接**: 与 Cognizant 内部团队、诺华的 Service Owner、以及下游 Vendor 建立联系。
*   **理解期望**: 与老板确认首月 KPI 和客户最痛的痛点 (Pain Points)。

---

## 阶段 2：第 3-4 周 (中期) - 掌控与稳定 (Stabilization & Execution)
**目标**：接手方向盘，从“旁观者”变为“驾驶员”，确保系统由稳转优。

### 1. 运维移交管理 (Transition Management)
*   **KT (Knowledge Transfer)**: 如果是新接手的项目，执行严格的 KT 计划。
    *   *策略*：采用 **Reverse Shadowing (反向影子)** 模式——我来操作，原负责人再旁观察纠错，确保真正掌握。
*   **门禁检查 (Gate Review)**: 检查文档是否齐全（架构图、灾备预案、账号清单），确保没有“隐形炸弹”。

### 2. 流程执行与优化 (Process Execution)
*   **事件管理**: 开始主持每日站会 (Daily Standup) 和 P1/P2 故障复盘会 (RCA Meeting)。
*   **变更管理 (Change Management)**: 亲自走一遍 CAB (Change Advisory Board) 流程，体验从提单到批准的全过程，特别是 GxP 系统的变更审批链条。
*   **供应商管理 (Vendor Coordination)**: 梳理现有 Vendor 的 SLA 达成率，识别配合中的瓶颈。

### 3. 系统稳定性治理
*   **监控覆盖**: 检查监控工具 (如 Splunk, Dynatrace) 是否覆盖了核心业务链路。是否存在“监控盲区”？
*   **老化工单清理 (Aging Ticket Drive)**: 针对积压的工单发起清理行动，快速建立客户信心。

---

## 阶段 3：第 5-6 周 (后期) - 优化与增值 (Optimization & Value Add)
**目标**：不仅要“维持运行(KTLO)”，更要展现“持续改进(CSI)”的价值。

### 1. 持续服务改进 (CSI - Continuous Service Improvement)
*   **自动化机会**: 识别重复性高的人工操作（如定期重启、日志抓取），提出自动化脚本方案 (Automation)。
*   **左移策略 (Shift-Left)**: 将 L2/L3 的常见问题整理成文档，移交给 L1 或 Service Desk，降低专家团队负载。

### 2. 客户预期管理 (Client Expectation)
*   **月度服务回顾 (MSR)**: 准备并尝试主导一次与诺华客户的月度汇报。用数据说话（SLA达标率、MTTR缩短趋势）。
*   **风险评估**: 输出一份《系统健康度与风险评估报告》，向客户展示我们对系统的深度理解及改进计划。

### 3. 跨界融合
*   **金融经验赋能**: 将金融行业的高并发处理经验、极速响应机制引入当前的运维体系，提升团队的应急响应能力。

---

## 总结
这个计划的核心是从**合规(Compliance)**入手，以**稳定(Stability)**为基石，最终通过**效率(Efficiency)**体现价值。我有信心在6周内，将我在花旗19年的深厚功力，成功转化为诺华项目所需的运维领导力。
