pipeline {
    agent any

    environment {
        AWS_ACCESS_KEY_ID     = credentials('AWS_ACCESS_KEY_ID')
        AWS_SECRET_ACCESS_KEY = credentials('AWS_SECRET_ACCESS_KEY')
        DB_USER_DEV     = credentials('DB_USER_DEV')
        DB_PWD_DEV = credentials('DB_PWD_DEV')
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

        stage('Build & Push to artifactory') {
                    steps {
                        sh "./gradlew jib --image 730911736748.dkr.ecr.ap-south-1.amazonaws.com/xact"
                    }
        }

        stage('Deploy To Dev') {
            steps {
                sh 'aws help'
            }
        }

        stage('Cleanup') {
            steps {
                cleanWs notFailBuild: true
            }
        }
    }
    post {
        always {
            archiveArtifacts artifacts: 'build/libs/**/*.jar', fingerprint: true
            archiveArtifacts artifacts: 'build/classes/**/**/META-INF/swagger/swagger.yml', fingerprint: true
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
            publishHTML (target : [allowMissing: false,
                                               alwaysLinkToLastBuild: true,
                                               keepAll: true,
                                               reportDir: './build/reports/jacoco/test/html',
                                               reportFiles: 'index.html',
                                               reportName: 'Coverage Reports',
                                               reportTitles: 'Coverage Report'])
        }
    }
}
