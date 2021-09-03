pipeline {
    agent any
    
    tools {
        maven 'mvn'
        
    }



    stages {
        
        stage('Source') {
            steps {
                // Get some code from a GitHub repository
                 sh  'git clone https://github.com/cloudperis/spring-petclinic-main.git'
                 sh 'ls'

            }
        }
        stage('Build') {
            steps{
                sh "mvn package"

            }

        }

                // Run Maven on a Unix agent.
                
                

                // To run Maven on a Windows agent, use
                // bat "mvn -Dmaven.test.failure.ignore=true clean package"
            
        stage('Test') {
            steps {
               junit '**/target/surefire-reports/TEST-*.xml' 
            }
        }

        stage('Archive') {
            steps {
                archiveArtifacts 'target/*.jar'
            }
        }

        stage('Deploy'){
            steps{
                echo "deploying application...."
            }
        }

            //post {
                // If Maven was able to run the tests, even if some of the test
                // failed, record the test results and archive the jar file.
               //success {
                    //junit '**/target/surefire-reports/TEST-*.xml'
                    //archiveArtifacts 'target/*.jar'
               // }
            //}
        
    }
}
