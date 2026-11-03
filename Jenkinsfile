pipeline {
    agent any

    tools {
        maven 'Maven-3.9.10'
        jdk 'JDK-21'
    }

    parameters {
        choice(name: 'MAVEN_PROFILE', choices: ['all', 'smoke', 'regression', 'ui', 'api'], description: 'Maven profile / Suite to execute')
        choice(name: 'BROWSER', choices: ['chrome', 'firefox', 'edge'], description: 'Browser for UI tests')
        booleanParam(name: 'HEADLESS', defaultValue: true, description: 'Run browser in headless mode')
        choice(name: 'ENVIRONMENT', choices: ['qa', 'dev', 'staging'], description: 'Target test environment')
    }

    environment {
        ALLURE_RESULTS_DIR = 'target/allure-results'
    }

    stages {
        stage('Checkout Code') {
            steps {
                checkout scm
            }
        }

        stage('Compile & Validate') {
            steps {
                bat 'mvn clean compile test-compile'
            }
        }

        stage('Execute Tests') {
            steps {
                // Run tests using selected profile, browser, and environment
                bat """
                    mvn test -P${params.MAVEN_PROFILE} ^
                             -Denv=${params.ENVIRONMENT} ^
                             -Dbrowser=${params.BROWSER} ^
                             -Dheadless=${params.HEADLESS}
                """
            }
        }
    }

    post {
        always {
            // 1. Generate Allure Report
            allure([
                includeProperties: false,
                jdk: '',
                properties: [],
                reportBuildPolicy: 'ALWAYS',
                results: [[path: 'target/allure-results']]
            ])

            // 2. Publish Extent & Tabular HTML Reports
            publishHTML([
                allowMissing: false,
                alwaysLinkToLastBuild: true,
                keepAll: true,
                reportDir: 'reports',
                reportFiles: 'summary.html, detailed_report.html, extent-report.html',
                reportName: 'Automation Test Execution Reports',
                reportTitles: 'Summary, Detailed, Extent'
            ])

            // 3. Archive logs and screenshots
            archiveArtifacts artifacts: 'logs/**, reports/screenshots/**', allowEmptyArchive: true
        }
        success {
            echo "Test suite execution completed successfully!"
        }
        failure {
            echo "Test execution encountered failures. Please review the Allure / Extent reports."
        }
    }
}
