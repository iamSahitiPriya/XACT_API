pipeline {
    agent any

    environment {
        AWS_ACCESS_KEY_ID     = credentials('AWS_ACCESS_KEY_ID')
        AWS_SECRET_ACCESS_KEY = credentials('AWS_SECRET_ACCESS_KEY')
    }

    tools { nodejs "nodejs" }

    stages {

        stage('Build') {
            steps {
                sh './gradlew clean build'
            }
        }

        stage('Dependency Check') {
            steps {
                sh './gradlew dependencyCheckAnalyze'
                sh './gradlew dependencyUpdates -Drevision=release'
            }
        }

        stage('Deploy To Dev') {
            steps {
                sh 'npm install'
                sh './gradlew deploy -Pstage=dev'
            }
        }
    }
    post {
        always {
            archiveArtifacts artifacts: 'build/libs/**/*.jar', fingerprint: true
            archiveArtifacts artifacts: 'build/dependencyUpdates/report.txt', fingerprint: true
            publishHTML (target : [allowMissing: false,
                                   alwaysLinkToLastBuild: true,
                                   keepAll: true,
                                   reportDir: './build/reports/tests/test',
                                   reportFiles: 'index.html',
                                   reportName: 'Unit Test Reports',
                                   reportTitles: 'Unit Test Report'])
            publishHTML (target : [allowMissing: false,
                                   alwaysLinkToLastBuild: true,
                                   keepAll: true,
                                   reportDir: './build/reports/',
                                   reportFiles: 'dependency-check-report.html',
                                   reportName: 'Dependency Check Reports',
                                   reportTitles: 'Dependency Check Report'])
        }
    }
}
