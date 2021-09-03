pipeline {
    agent any
    
    tools {
        maven 'mvn'
        
    }



    stages {
        
        stage('check distro'){
           steps {
               sh "java -version"
           }
        }
        stage('Build') {
            steps {
                // Get some code from a GitHub repository
                 git 'https://github.com/cloudperis/spring-petclinic-main.git'

                // Run Maven on a Unix agent.
                sh "ls"
                sh "pwd"
                sh "mvn package"

                // To run Maven on a Windows agent, use
                // bat "mvn -Dmaven.test.failure.ignore=true clean package"
            }
        stage('Test') {
            steps {
               junit '**/target/surefire-reports/TEST-*.xml' 
            }
        }

        stage('Deploy') {
            steps {
                archiveArtifacts 'target/*.jar'
            }
        }

            post {
                // If Maven was able to run the tests, even if some of the test
                // failed, record the test results and archive the jar file.
               success {
                    junit '**/target/surefire-reports/TEST-*.xml'
                    archiveArtifacts 'target/*.jar'
                }
            }
        }
    }
}