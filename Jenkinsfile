pipeline {
    agent { docker { image 'maven:3.3.3' } }
  
    stages {
        stage('checkout') {
            steps {
                checkout scm
            }
        }
        
        stage('build') {
            steps {
                sh 'mvn clean install'
            }
        }
    }
}
