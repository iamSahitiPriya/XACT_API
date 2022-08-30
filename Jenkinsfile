pipeline {
    agent any

    tools { nodejs "nodejs" }
    stages {
        stage("Security check"){
                          steps{
                                script{
                                       try{
                                          sh "set +e"
                                          sh 'docker rm $(docker ps -a -q)'
                                          sh "docker run --rm -v ${env.WORKSPACE}:${env.WORKSPACE} 730911736748.dkr.ecr.ap-south-1.amazonaws.com/xact-common git file://${env.WORKSPACE} --debug"
                                          ERROR_COUNT = sh(returnStdout: true, script: "docker run -v ${env.WORKSPACE}:${env.WORKSPACE} 730911736748.dkr.ecr.ap-south-1.amazonaws.com/xact-common git file://${env.WORKSPACE} --json | grep -c commit")
                                          if(ERROR_COUNT > 4){
                                                throw new Exception("Build failed due to security issues. Please check the above logs.")
                                          }
                                       }
                                        catch(Exception e){
                                              if(e.getMessage() == "Build failed due to security issues. Please check the above logs."){
                                                        currentBuild.result = "FAILURE"
                                                        echo "${e}"
                                                        sh "false"
                                              }
                                        }
                                }

                          }
                }
        stage('Build') {
            steps {
                sh "aws s3 cp s3://xact-artifacts/ap-south-1-bundle.pem ap-south-1-bundle.pem"
                sh "cp ap-south-1-bundle.pem src/main/resources/certs/ap-south-1-bundle.pem"
                sh './gradlew clean build'
            }
        }

        stage("SonarQube analysis") {
            steps {
              withSonarQubeEnv('XACT_SONAR') {
                sh './gradlew sonarqube'
              }
            }
        }
        stage("Quality Gate") {
            steps {
              timeout(time: 1, unit: 'HOURS') {
                waitForQualityGate abortPipeline: true
              }
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
                sh 'aws ecr get-login-password --region ap-south-1 | docker login --username AWS --password-stdin 730911736748.dkr.ecr.ap-south-1.amazonaws.com'
                sh "./gradlew jib --image 730911736748.dkr.ecr.ap-south-1.amazonaws.com/xact:${env.GIT_COMMIT}"
                sh "./gradlew jib --image 730911736748.dkr.ecr.ap-south-1.amazonaws.com/xact:latest"

            }
        }

        stage('Deploy To Dev') {
            steps {
                sh 'aws ecs update-service --cluster xact-backend-cluster --service xact-service --force-new-deployment'
            }
        }
        stage('Archive & Cleanup') {
                steps {
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
                    cleanWs notFailBuild: true
                }
        }
    }

}