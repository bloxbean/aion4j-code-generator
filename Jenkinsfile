pipeline {
    agent any

    parameters {
        choice(
                choices: ['BUILD_ONLY' , 'SNAPSHOT_RELEASE', 'RELEASE'],
                description: '',
                name: 'BUILD_TYPE')
    }

    tools {
        maven 'M3'
        jdk 'jdk-11'
    }

    environment {
        gpg_passphrase = credentials("gpg_passphrase")
    }

    stages {

        stage('Build') {
            steps {
                sh """
                    mvn clean package
                """
            }
        }

        stage('Unit Tests') {
            steps {

                sh 'mvn -B test'

            }
            post {
                always {
                    junit '**/target/surefire-reports/*.xml'
                }
            }
        }

        stage('Integration Tests') {
            steps {

                sh 'mvn -B integration-test'

            }
            post {
                always {
                    junit '**/target/failsafe-reports/*.xml'
                }
            }
        }

        stage("Snapshot Release") {
            when {
                expression { params.BUILD_TYPE == 'SNAPSHOT_RELEASE' }
            }
            steps {
                configFileProvider(
                        [configFile(fileId: 'maven-settings', variable: 'MAVEN_SETTINGS')]) {

                    sh 'mvn -s "$MAVEN_SETTINGS" clean deploy -Dgpg.passphrase=${gpg_passphrase} -DskipITs -Prelease'

                }
            }
        }

        stage("Release") {
            when {
                expression { params.BUILD_TYPE == 'RELEASE' }
            }
            steps {
                configFileProvider(
                        [configFile(fileId: 'maven-settings', variable: 'MAVEN_SETTINGS')]) {

                    sh 'mvn -s "$MAVEN_SETTINGS" clean deploy -Dgpg.passphrase=${gpg_passphrase} -DskipITs -Prelease'

                }
            }
        }
    }
}
