pipeline {
    agent any
    environment{
        CURRENT_BRANCH = "${env.GIT_BRANCH}"
    }

    stages {

        stage('Prepare') {
                steps {
                     sh 'aws ecr get-login-password --region ap-south-1 | docker login --username AWS --password-stdin 730911736748.dkr.ecr.ap-south-1.amazonaws.com'

                }
        }

        stage("Security check"){
                          steps{
                                script{
                                       try{
                                          sh "set +e"
                                          sh 'docker stop $(docker ps -a -q)'
                                          sh 'docker rm $(docker ps -a -q)'
                                          sh "docker run --rm -v ${env.WORKSPACE}:${env.WORKSPACE} 730911736748.dkr.ecr.ap-south-1.amazonaws.com/xact-common git file://${env.WORKSPACE} --debug"
                                          ERROR_COUNT = sh(returnStdout: true, script: "docker run -v ${env.WORKSPACE}:${env.WORKSPACE} 730911736748.dkr.ecr.ap-south-1.amazonaws.com/xact-common git file://${env.WORKSPACE} --json | grep -c commit")
                                          if(ERROR_COUNT > 0){
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
                sh "docker run --name postgres -e POSTGRES_USER=${USER} -e POSTGRES_PASSWORD=${USER} -p 5432:5432 -v /data:/var/lib/postgresql/data -d 730911736748.dkr.ecr.ap-south-1.amazonaws.com/postgres-test:latest"
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
            when {
                  equals(actual: (CURRENT_BRANCH.contains('release') || CURRENT_BRANCH == 'develop'), expected: true)
            }
            steps {
                sh "./gradlew jib --image 730911736748.dkr.ecr.ap-south-1.amazonaws.com/xact:${env.GIT_COMMIT}"
                sh "./gradlew jib --image 730911736748.dkr.ecr.ap-south-1.amazonaws.com/xact:latest"

            }
        }

        stage('Deploy To Dev') {
            when {
                  equals(actual: (CURRENT_BRANCH.contains('release') || CURRENT_BRANCH == 'develop'), expected: true)
            }
            steps {
                sh 'aws ecs update-service --cluster xact-backend-cluster --service xact-service --force-new-deployment'
            }
        }
        stage('Archive & Cleanup') {
                steps {
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
      post {
                        always {
                        publishHTML (target : [allowMissing: false,
                                                                               alwaysLinkToLastBuild: true,
                                                                               keepAll: true,
                                                                               reportDir: './build/reports/tests/test',
                                                                               reportFiles: 'index.html',
                                                                               reportName: 'Test Report',
                                                                               reportTitles: 'Test Report'])
                        sh "set +e"
                        sh 'docker stop $(docker ps -a -q)'
                        sh 'docker rm $(docker ps -a -q)'
                        cleanWs notFailBuild: true
                        }
                }

}
