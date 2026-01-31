pipeline {
  agent any
  options { timestamps() }

  environment {
    DOCKER_PATH = "/Applications/Docker.app/Contents/Resources/bin:/usr/local/bin:/opt/homebrew/bin"
    IMAGE_NAME = "package-campaign-adapter"
    CONTAINER_NAME = "package-campaign-adapter"
  }

  stages {
    stage('Checkout') {
      steps { checkout scm }
    }

    stage('Build') {
      steps {
        sh '''
          chmod +x ./gradlew

          export JAVA_HOME=$(/usr/libexec/java_home -v 21)
          export PATH="$JAVA_HOME/bin:$PATH"

          java -version
          ./gradlew clean :adapter-service:bootJar
        '''
      }
    }

    stage('Docker build') {
      steps {
        sh '''
          export PATH="${DOCKER_PATH}:$PATH"

          VERSION=$(grep "^version=" gradle.properties | cut -d= -f2 | tr -d '[:space:]')
          test -n "$VERSION" || (echo "version empty in gradle.properties" && exit 1)
          echo "VERSION=$VERSION"


          docker build -t ${IMAGE_NAME}:${VERSION} -f adapter-service/Dockerfile adapter-service
          echo "$VERSION" > .jenkins_version
        '''
      }
    }

    stage('Docker deploy') {
      steps {
        sh '''
          export PATH="${DOCKER_PATH}:$PATH"
          VERSION=$(cat .jenkins_version)

          docker rm -f ${CONTAINER_NAME} >/dev/null 2>&1 || true

          docker run -d --name ${CONTAINER_NAME} \
            -p 8081 \
            ${IMAGE_NAME}:$VERSION

          sleep 2
          curl -i http://127.0.0.1:${MS1_PORT}/v1/packages || true
        '''
      }
    }
  }
}
