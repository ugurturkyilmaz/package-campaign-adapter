pipeline {
  agent any
  options { timestamps() }

  parameters {
    choice(
      name: 'VERSION_TYPE',
      choices: ['none', 'patch', 'minor', 'major'],
      description: 'Version increment type'
    )
  }

  environment {
    DOCKER_PATH = "/Applications/Docker.app/Contents/Resources/bin:/usr/local/bin:/opt/homebrew/bin"
    IMAGE_NAME = "package-campaign-adapter"
    CONTAINER_NAME = "package-campaign-adapter"
  }

  stages {

    stage('Checkout') {
      steps { checkout scm }
    }

    stage('Version bump') {
      when {
        expression { params.VERSION_TYPE != 'none' }
      }
      steps {
        sh '''
          VERSION=$(grep "^version=" gradle.properties | cut -d= -f2)
          IFS='.' read -r MAJOR MINOR PATCH <<< "$VERSION"

          case "$VERSION_TYPE" in
            patch)
              PATCH=$((PATCH+1))
              ;;
            minor)
              MINOR=$((MINOR+1))
              PATCH=0
              ;;
            major)
              MAJOR=$((MAJOR+1))
              MINOR=0
              PATCH=0
              ;;
          esac

          NEW_VERSION="$MAJOR.$MINOR.$PATCH"
          echo "Version bumped: $VERSION → $NEW_VERSION"

          sed -i '' "s/^version=.*/version=$NEW_VERSION/" gradle.properties
          cat gradle.properties
        '''
      }
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

          VERSION=$(grep "^version=" gradle.properties | cut -d= -f2)
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
            -p 8081:8081 \
            ${IMAGE_NAME}:$VERSION

          sleep 2
          curl -i http://127.0.0.1:8081/v1/packages || true
        '''
      }
    }
  }
}
