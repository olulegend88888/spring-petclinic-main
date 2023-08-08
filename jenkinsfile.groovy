pipeline {
    agent any
    
    tools {
        maven 'mvn'
        
    }



    stages {
        
        stage('Build') {
            steps{
                slackSend channel: 'bode-alert', color: '#2211d9', message: "STARTED ${env.JOB_NAME} at #${env.BUILD_NUMBER}:\n${env.BUILD_URL}"
                sh "mvn package"

            }

        }

            
        stage('Test') {
            steps {
               junit '**/target/surefire-reports/TEST-*.xml'
               sh 'cd target & cd surefire-reports & ls ' 
            }
        }

        stage('Archive') {
            steps {
                archiveArtifacts 'target/*.jar'
            }
        }

        stage('Publish-Artifact'){
            steps{
                s3Upload acl: 'Private', bucket: 'olulegends3', cacheControl: '', excludePathPattern: '', file: 'target/*.jar', includePathPattern: '', metadatas: [''], redirectLocation: '', sseAlgorithm: '', tags: '', text: '', workingDir: ''
            }
        }

       stage('Deploy-Dev'){
            steps{
                echo 'deploying application updates....'
                withCredentials([[
                      $class: 'AmazonWebServicesCredentialsBinding',
                      credentialsId: "jenkinss3",
                      accessKeyVariable: 'AWS_ACCESS_KEY_ID',
                      secretKeyVariable: 'AWS_SECRET_ACCESS_KEY']]) {

                          
                          sh "aws ec2 reboot-instances --instance-ids i-0e1862772a5afb538 --region us-east-2"

                      }


            }
        }

        
    }
    post{
        success{
           slackSend channel: 'bode-alert', color: '439FE0', message: "SUCCESS ${currentBuild.fullDisplayName} at ${currentBuild.durationString[0..-13]}" 
        }
        failure{
            slackSend channel: 'bode-alert', color: '#fc0303', message: "FAILURE ${currentBuild.fullDisplayName} at ${currentBuild.durationString[0..-13]}"
        }
    }
}