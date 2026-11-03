# Selenium Java TestNG Hybrid Automation Framework

[![Java](https://img.shields.io/badge/Java-17%2F21-orange.svg)](https://www.oracle.com/java/)
[![Selenium](https://img.shields.io/badge/Selenium-4.28.1-green.svg)](https://www.selenium.dev/)
[![TestNG](https://img.shields.io/badge/TestNG-7.10.2-red.svg)](https://testng.org/)
[![Maven](https://img.shields.io/badge/Maven-3.9+-blue.svg)](https://maven.apache.org/)
[![ExtentReports](https://img.shields.io/badge/ExtentReports-5.1.2-brightgreen.svg)](https://extentreports.com/)
[![Allure](https://img.shields.io/badge/Allure-2.29.0-yellow.svg)](https://docs.qameta.io/allure/)

---

## 📌 Overview on QA Automation Framework

This is an enterprise-grade **Hybrid Automation Framework** that seamlessly integrates **UI (Selenium 4)** and **API (REST Assured)** testing into a single, scalable, and thread-safe solution.

The framework strictly adheres to the **Page Object Model (POM)** design pattern for UI automation, decoupling test logic from page locators and user interactions. In addition, it provides robust, modular utilities for API requests, database queries, externalized test data management, multi-format reporting, automatic failure screenshots, test retries, and comprehensive CI/CD support.

---

## 🚀 Key Features

* **UI Automation**: Powered by **Selenium WebDriver 4** with Page Object Model (POM) and automatic driver binary management via **WebDriverManager**.
* **API Automation**: Fully integrated **REST Assured** client for RESTful services with automatic schema verification and Allure request/response logging filters.
* **Unified Framework**: Combines UI and API automation under a single test runner and single build tool (`pom.xml`).
* **Thread-Safe Parallel Execution**: Uses `ThreadLocal<WebDriver>` and `ThreadLocal<ExtentTest>` to ensure zero collisions during parallel test execution across classes and methods.
* **Smart Synchronization & Fallbacks**: Dedicated explicit and fluent wait wrappers (`WaitUtils`, `ElementActions`) with automatic fallback to JavaScript executions on intercepted clicks.
* **Comprehensive TestNG Listeners**:
  * `TestListener`: Hooks into test lifecycle, logs execution, captures screenshots on failure, and feeds both ExtentReports and Custom Tabular Reports.
  * `AnnotationTransformer`: Automatically binds `RetryAnalyzer` dynamically to all `@Test` annotations.
  * `RetryAnalyzer`: Automatically retries failed tests based on configurable threshold (`max.retry.count`).
  * `AllureListener`: Injects screenshots and logs directly into Allure test artifacts.
* **Multi-Tier Reporting**:
  1. **ExtentReports 5**: Interactive Spark HTML report with test hierarchy, category tags, author tagging, execution metrics, and embedded Base64 failure screenshots (`reports/extent-report.html`).
  2. **Custom Tabular Reports**:
     * `summary.html`: Executive dashboard with total, pass, fail, skip counts, pass percentage, duration, and environment metadata.
     * `detailed_report.html`: Tabular test log detailing test class, method, duration, status badges, and expandable stack traces.
  3. **Allure Reports**: Rich interactive visual dashboard with step-by-step histories, attachments, and defect tracking (`mvn allure:serve`).
* **Configuration & Environment Management**: Multi-environment properties support (`config.properties`, `config-qa.properties`, `config-dev.properties`) with `-Denv=qa` switching and command-line property overrides (`-Dbrowser=firefox`, `-Dheadless=true`).
* **Externalized Test Data**:
  * JSON test data management via **Jackson Databind** (`JsonUtils`).
  * Excel test data reader via **Apache POI** (`ExcelUtils`) for `@DataProvider` test cases.
* **Database Support**: Built-in JDBC query execution utility (`DatabaseUtils`) for DB state validation and test assertions.
* **CI/CD Integration**: Pre-configured **Maven Profiles** (`smoke`, `regression`, `ui`, `api`, `all`), **Jenkinsfile** (with Allure & HTML Publisher), and **azure-pipelines.yml**.

---

## 🛠️ Prerequisites

Ensure you have the following installed on your machine:

* **Java JDK**: Version 17 or 21 (Verify with `java -version`)
* **Apache Maven**: Version 3.9+ (Verify with `mvn -version`)
* **Web Browsers**: Google Chrome, Mozilla Firefox, or Microsoft Edge
* **Allure Commandline** *(Optional for local CLI report viewing)*:
  ```bash
  # Windows via Scoop
  scoop install allure
  # Mac via Homebrew
  brew install allure
  ```

---

## ⚙️ Setup Instructions

### 1. Clone the Repository
```bash
git clone <repository_url>
cd selenium-testng-framework
```

### 2. Configure Corporate Proxy *(If required)*
If working behind a corporate proxy, configure Maven's `settings.xml` (located at `~/.m2/settings.xml` or `%USERPROFILE%\.m2\settings.xml`):

```xml
<settings>
  <proxies>
    <proxy>
      <id>corporate-proxy</id>
      <active>true</active>
      <protocol>http</protocol>
      <host>proxy.abc.com</host>
      <port>8080</port>
      <nonProxyHosts>localhost|127.0.0.1</nonProxyHosts>
    </proxy>
  </proxies>
</settings>
```

### 3. Install Dependencies & Compile
```bash
mvn clean test-compile
```

---

## 🧪 Running Tests

Tests can be executed via Maven profiles, suite XML files, or custom tags/groups.

### 1. Execute by Maven Profiles

| Target | Command |
| :--- | :--- |
| **Run All Tests (UI + API)** | `mvn test -Pall` |
| **Run Smoke Tests** | `mvn test -Psmoke` |
| **Run Regression Tests** | `mvn test -Pregression` |
| **Run UI Tests Only** | `mvn test -Pui` |
| **Run API Tests Only** | `mvn test -Papi` |

### 2. Browser & Environment Overrides via CLI
Pass system variables directly through Maven:
```bash
# Run UI regression tests in Firefox headless mode on QA environment
mvn test -Pregression -Dbrowser=firefox -Dheadless=true -Denv=qa

# Run in Edge browser headed mode
mvn test -Psmoke -Dbrowser=edge -Dheadless=false
```

### 3. Running Specific Test Classes
```bash
# Run CarWale UI test specifically
mvn test -Dtest=CarWaleTest

# Run User API test specifically
mvn test -Dtest=UserApiTest
```

---

## 📊 Reports & Logs

### 1. ExtentReports 5
After execution, open the interactive Spark report in any browser:
* Path: `reports/extent-report.html`
* Features: Pass/Fail metrics, category distribution, time duration, and failure screenshots directly embedded inline.

### 2. Custom Tabular Reports
Automatically generated after every test run under `reports/`:
* **Executive Summary**: `reports/summary.html`
* **Detailed Execution Table**: `reports/detailed_report.html`

### 3. Allure Reports
Allure results are written to `target/allure-results`.

To generate and view the interactive Allure dashboard:
```bash
# Spin up live local Allure web server
mvn allure:serve

# Or generate a single standalone folder report
mvn allure:report
```

### 4. Application & Execution Logs
All log messages (timestamps, thread IDs, log levels, HTTP payloads, and element actions) are captured simultaneously:
* **Console Output**: Real-time colored execution log.
* **File Appender**: Rolling file logs stored at `logs/automation.log`.

---

## 📁 Project Structure

```text
selenium-testng-framework/
├── pom.xml                                   # Project Object Model with dependencies and profiles
├── Jenkinsfile                               # Jenkins Declarative Pipeline with Allure/HTML publishers
├── azure-pipelines.yml                       # Azure DevOps Pipeline definition
├── .gitignore                                # Excludes target, allure-results, logs, etc.
├── README.md                                 # Complete documentation
│
├── src/
│   ├── main/
│   │   └── java/com/automation/
│   │       ├── constants/
│   │       │   └── FrameworkConstants.java   # File paths, timeouts, report destinations
│   │       ├── driver/
│   │       │   ├── DriverFactory.java        # Chrome/Firefox/Edge local & remote grid setups
│   │       │   └── DriverManager.java        # ThreadLocal WebDriver manager
│   │       ├── pages/
│   │       │   ├── base/
│   │       │   │   └── BasePage.java         # Base page object with common browser operations
│   │       │   ├── carwale/
│   │       │   │   ├── CarWaleHomePage.java  # CarWale search & navigation POM
│   │       │   │   └── NewCarsPage.java      # New cars & brand selection POM
│   │       │   └── saucedemo/
│   │       │       ├── LoginPage.java        # SauceDemo authentication POM
│   │       │       └── ProductsPage.java     # Product catalogue, sorting & cart POM
│   │       └── utils/
│   │           ├── api/
│   │           │   ├── ApiClient.java        # REST Assured specification & Allure filters
│   │           │   └── RestUtils.java        # HTTP GET, POST, PUT, DELETE, PATCH helpers
│   │           ├── config/
│   │           │   └── ConfigReader.java     # Properties loader with CLI & environment overrides
│   │           ├── data/
│   │           │   ├── JsonUtils.java        # Jackson JSON reader & parser
│   │           │   └── ExcelUtils.java       # Apache POI Excel reader for TestNG DataProviders
│   │           ├── database/
│   │           │   └── DatabaseUtils.java    # JDBC query & update executor
│   │           ├── logging/
│   │           │   └── Log.java              # Log4j2 thread-safe logging wrapper
│   │           └── ui/
│   │               ├── ElementActions.java   # Click, type, hover, select, and JS fallbacks
│   │               ├── JavaScriptUtils.java  # JS executor, scrolls, highlights
│   │               ├── ScreenshotUtils.java  # File, Base64, and Byte screenshot captures
│   │               └── WaitUtils.java        # Explicit and fluent wait synchronization
│   │
│   └── test/
│       ├── java/com/automation/
│       │   ├── listeners/
│       │   │   ├── AllureListener.java       # Allure test step & screenshot attachments
│       │   │   ├── AnnotationTransformer.java# Dynamic binding of RetryAnalyzer to tests
│       │   │   ├── RetryAnalyzer.java        # Test failure retry handler
│       │   │   └── TestListener.java         # Extent, Allure, Tabular report listener
│       │   ├── reports/
│       │   │   ├── CustomTabularReportGenerator.java # summary.html & detailed_report.html generator
│       │   │   ├── ExtentManager.java        # ExtentReports Spark configuration
│       │   │   └── ExtentTestManager.java    # ThreadLocal ExtentTest logger
│       │   └── tests/
│       │       ├── base/
│       │       │   ├── BaseTest.java         # Driver setup/teardown hooks (@BeforeMethod/@AfterMethod)
│       │       │   └── BaseApiTest.java      # REST Assured base URI & filter initialization
│       │       ├── api/
│       │       │   └── UserApiTest.java      # REST API tests (GET, POST, PUT, DELETE)
│       │       └── ui/
│       │           ├── CarWaleTest.java      # CarWale search & catalog UI tests
│       │           ├── LoginTest.java        # Data-driven authentication tests
│       │           └── ProductsTest.java     # Catalog, cart badge & logout tests
│       │
│       └── resources/
│           ├── allure.properties             # Allure results folder & issue links
│           ├── log4j2.xml                    # Console and rolling file logging configuration
│           ├── config/
│           │   ├── config.properties         # Default application settings, browser, timeouts
│           │   ├── config-dev.properties     # DEV environment overrides
│           │   └── config-qa.properties      # QA environment overrides
│           ├── suites/
│           │   ├── testng-all.xml            # Full regression suite (UI + API parallel)
│           │   ├── testng-smoke.xml          # Fast sanity/smoke suite
│           │   ├── testng-regression.xml     # Comprehensive regression suite
│           │   ├── testng-ui.xml             # UI tests only
│           │   └── testng-api.xml            # API tests only
│           └── testdata/
│               ├── carData.json              # CarWale brand search data
│               └── loginData.json            # Login credentials and error messages
```

---

## ⚙️ Jenkins CI/CD Setup

1. **Prerequisites in Jenkins**:
   * Install Jenkins Plugins: **Allure Jenkins Plugin**, **HTML Publisher Plugin**, **Pipeline Maven Integration**.
   * Configure Tools:
     * JDK: `JDK-21`
     * Maven: `Maven-3.9.10`
     * Allure Commandline: Under *Global Tool Configuration* -> *Allure Commandline*.
2. **Create New Pipeline Job**:
   * Create a new Pipeline item pointing to this repository.
   * Jenkins will automatically detect `Jenkinsfile`.
3. **Execution**:
   * Click **Build with Parameters**.
   * Choose `MAVEN_PROFILE` (`smoke`, `regression`, `ui`, `api`, `all`), `BROWSER` (`chrome`, `firefox`, `edge`), and target `ENVIRONMENT` (`qa`, `dev`, `staging`).
   * When finished, the build sidebar will feature **Allure Report** and **Automation Test Execution Reports** (Extent & Tabular).
