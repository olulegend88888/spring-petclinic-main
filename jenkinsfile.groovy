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
                s3Upload consoleLogLevel: 'INFO', dontSetBuildResultOnFailure: false, dontWaitForConcurrentBuildCompletion: false, entries: [[bucket: 'pet-clinic-shay', excludedFile: '', flatten: false, gzipFiles: false, keepForever: false, managedArtifacts: false, noUploadOnFailure: true, selectedRegion: 'us-east-1', showDirectlyInBrowser: false, sourceFile: 'target/*.jar', storageClass: 'STANDARD', uploadFromSlave: false, useServerSideEncryption: false]], pluginFailureResultConstraint: 'FAILURE', profileName: 'Jenkins-user', userMetadata: []
            }
        }

            //post {
               
               //success {
                   // s3Upload consoleLogLevel: 'INFO', dontSetBuildResultOnFailure: false, dontWaitForConcurrentBuildCompletion: false, entries: [[bucket: 'pet-clinic-shay', excludedFile: '', flatten: false, gzipFiles: false, keepForever: false, managedArtifacts: false, noUploadOnFailure: true, selectedRegion: 'us-east-1', showDirectlyInBrowser: false, sourceFile: 'target/*.jar', storageClass: 'STANDARD', uploadFromSlave: false, useServerSideEncryption: false]], pluginFailureResultConstraint: 'FAILURE', profileName: 'Jenkins-user', userMetadata: []
               //}
           // }
        
    }
}
