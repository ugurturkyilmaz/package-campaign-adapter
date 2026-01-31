pipeline {
  agent any

  options { timestamps() }

  parameters {
    choice(
      name: 'VERSION_BUMP',
      choices: ['none', 'patch', 'minor', 'major'],
      description: 'Version bump type'
    )
  }

  environment {
    IMAGE_NAME = "ms1-adapter"
    CONTAINER_NAME = "ms1-adapter"
  }

  stages {

    stage('Verify project') {
      steps {
        sh '''
          test -d "${PROJECT_DIR}" || (echo "PROJECT_DIR not found: ${PROJECT_DIR}" && exit 1)
          ls -la "${PROJECT_DIR}"
        '''
      }
    }

    stage('Version bump') {
      when { expression { params.VERSION_BUMP != 'none' } }
      steps {
        dir("${params.PROJECT_DIR}") {
          sh '''
            chmod +x ./gradlew
            if [ "${VERSION_BUMP}" = "patch" ]; then ./gradlew bumpPatch; fi
            if [ "${VERSION_BUMP}" = "minor" ]; then ./gradlew bumpMinor; fi
            if [ "${VERSION_BUMP}" = "major" ]; then ./gradlew bumpMajor; fi
          '''
        }
      }
    }

    stage('Build (bootJar)') {
      steps {
        dir("${params.PROJECT_DIR}") {
          sh '''
            chmod +x ./gradlew
            ./gradlew clean :adapter-service:bootJar
          '''
        }
      }
    }

    stage('Docker build') {
      steps {
        dir("${params.PROJECT_DIR}") {
          sh '''
            VERSION=$(grep "^version=" gradle.properties | cut -d= -f2 | tr -d '[:space:]')
            echo "Project version: $VERSION"
            echo "$VERSION" > .jenkins_version


            docker --version
            docker build -t ${IMAGE_NAME}:$VERSION -f adapter-service/Dockerfile adapter-service
          '''
        }
      }
    }

    stage('Docker run (deploy)') {
      steps {
        dir("${params.PROJECT_DIR}") {
          sh '''
            VERSION=$(cat .jenkins_version)
            echo "Deploying ${IMAGE_NAME}:$VERSION on port 8081"


            docker rm -f ${CONTAINER_NAME} >/dev/null 2>&1 || true


            docker run -d --name ${CONTAINER_NAME} \
              -p 8081 \
              ${IMAGE_NAME}:$VERSION
          '''
        }
      }
    }
  }
}
