node {
   def mvnHome
   stage('Preparation') {
      git 'https://github.com/bloxbean/aion4j-code-generator.git'
      mvnHome = tool 'M3'
   }
   stages {
       stage('Build') {
          // Run the maven build
          withEnv(["MVN_HOME=$mvnHome"]) {
             if (isUnix()) {
                sh '"$MVN_HOME/bin/mvn" -Dmaven.test.failure.ignore clean package'
             } else {
                bat(/"%MVN_HOME%\bin\mvn" -Dmaven.test.failure.ignore clean package/)
             }
          }

          stage("Release") {
                 withCredentials([string(credentialsId: 'gpg.passphrase', variable: 'gpg.passphrase')]) {
                      sh '"$MVN_HOME/bin/mvn" release:clean release:prepare release:perform -DskipITs -Darguments=-Dgpg.passphrase=${gpg.passphrase}  -Prelease'
                 }
          }

          stage('Results') {
             junit '**/target/surefire-reports/TEST-*.xml'
             archiveArtifacts 'target/*.jar'
          }
       }

   }
}