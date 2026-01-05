# Pharma Industry Glossary for IT Professionals

This glossary is curated for IT professionals (Developers, BAs, PMs, Data Engineers) entering or working in the Pharmaceutical industry. It focuses on the intersection of technology, data, and regulatory compliance.

## 1. Regulatory & Compliance (The "Must-Knows")

*   **GxP (Good x Practice)**: An umbrella term for regulations and guidelines in the pharma industry. 'x' stands for the specific field:
    *   **GMP (Good Manufacturing Practice)**: Ensures products are consistently produced and controlled according to quality standards. Critical for Manufacturing IT (MES, SCADA).
    *   **GCP (Good Clinical Practice)**: Ethical and scientific quality standard for designing, conducting, recording, and reporting trials. Critical for Clinical IT (CTMS, EDC).
    *   **GLP (Good Laboratory Practice)**: Quality system for research laboratories and non-clinical safety studies. Critical for R&D IT (LIMS, ELN).
    *   **GDP (Good Distribution Practice)**: Ensures quality is maintained throughout the supply chain.
*   **21 CFR Part 11**: FDA regulation on **Electronic Records and Electronic Signatures (ERES)**. IT systems must ensure that electronic records are trustworthy, reliable, and equivalent to paper records. Key for *any* system storing regulated data.
*   **CSV (Computerized System Validation)**: The documented process of assuring that a computer system does exactly what it is designed to do in a consistent and reproducible manner. The traditional "V-Model" approach.
*   **CSA (Computer Software Assurance)**: A newer, risk-based approach to validation promoted by the FDA. It focuses more on critical thinking and testing rather than excessive documentation (moving away from "validation for validation's sake").
*   **GAMP 5 (Good Automated Manufacturing Practice)**: A risk-based approach to compliant GxP computerized systems. It categorizes software (Cat 1: Infrastructure to Cat 5: Custom Code) to determine validation effort.
*   **Audit Trail**: A secure, computer-generated, time-stamped electronic record that allows for reconstruction of the course of events relating to the creation, modification, or deletion of an electronic record. **Mandatory for Part 11 compliance.**

## 2. Data Integrity & Standards

*   **ALCOA+**: The gold standard for Data Integrity. Data must be:
    *   **A**ttributable (Who did it?)
    *   **L**egible (Can you read it?)
    *   **C**ontemporaneous (Recorded at the time work was performed)
    *   **O**riginal (First record)
    *   **A**ccurate (Correct)
    *   **+**: Complete, Consistent, Enduring, Available.
*   **CDISC (Clinical Data Interchange Standards Consortium)**: Global standards for clinical research data.
    *   **SDTM (Study Data Tabulation Model)**: Standard structure for submitting clinical trial data to regulators (FDA/EMA).
    *   **ADaM (Analysis Data Model)**: Standard for data used in statistical analysis.
*   **MedDRA (Medical Dictionary for Regulatory Activities)**: A standardized medical terminology used to classify adverse events and medical history.
*   **PII (Personally Identifiable Information) / PHI (Protected Health Information)**: Critical in clinical trials. Data must be de-identified or pseudonymized (GDPR/HIPAA compliance).

## 3. Key IT Systems in Pharma

### R&D / Lab
*   **LIMS (Laboratory Information Management System)**: Manages samples, test results, and workflows in the lab.
*   **ELN (Electronic Lab Notebook)**: Digital version of a paper lab notebook. Used by scientists to document experiments.

### Clinical Trials
*   **EDC (Electronic Data Capture)**: Software used to collect clinical trial data from patients/sites (e.g., Rave, Veeva Vault).
*   **CTMS (Clinical Trial Management System)**: Manages the operational aspects of a trial (site selection, budget, milestones).
*   **eTMF (Electronic Trial Master File)**: A content management system for all documents required to demonstrate GCP compliance (e.g., protocols, investigator brochures).
*   **eCOA / ePRO (Electronic Clinical Outcome Assessment / Patient Reported Outcomes)**: Apps/devices used by patients to report symptoms directly.

### Manufacturing & Supply Chain
*   **MES (Manufacturing Execution System)**: Controls and documents the transformation of raw materials to finished goods in real-time.
*   **Serialization / Track & Trace**: Assigning a unique serial number to each saleable unit to prevent counterfeiting (DSCSA in US, FMD in EU). IT systems must track this data across the supply chain.

## 4. Industry Players & Terms

*   **CRO (Contract Research Organization)**: A company hired by a pharma company (Sponsor) to run clinical trials.
*   **CDMO (Contract Development and Manufacturing Organization)**: A company hired to handle drug development and manufacturing.
*   **Sponsor**: The pharma company that owns the drug and funds the research.
*   **HCP (Health Care Professional)**: Doctors, nurses, pharmacists.
*   **KOL (Key Opinion Leader)**: Influential doctors/experts in a specific therapeutic area.
*   **Pharmacovigilance (PV)**: The science of detecting, assessing, understanding, and preventing adverse effects (side effects).
*   **SOP (Standard Operating Procedure)**: Detailed, written instructions to achieve uniformity of the performance of a specific function. IT must follow IT SOPs (e.g., Backup/Restore, User Access Management).

## 5. Drug Development Lifecycle (The "Pipeline")

1.  **Discovery**: Finding a molecule target.
2.  **Pre-clinical**: Testing in animals/lab (GLP).
3.  **Clinical Phase I**: Safety testing in small group of healthy volunteers.
4.  **Clinical Phase II**: Efficacy testing in patients.
5.  **Clinical Phase III**: Large scale testing for safety and efficacy (GCP).
6.  **NDA/BLA Submission**: Asking FDA for approval.
7.  **Commercialization**: Manufacturing (GMP) and Sales.
8.  **Phase IV**: Post-marketing surveillance (PV).
