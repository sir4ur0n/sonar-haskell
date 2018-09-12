This project is highly inspired by https://github.com/uartois/sonar-haskell but with a massive rewrite.

# SonarQube plugin for Haskell

The purpose of this project is to build a code analyzer for [Haskell](https://www.haskell.org)

It integrates `hlint` reports into SonarQube dashboard.

The user must generate a JSON HLint report for his code. This report is thus integrated to SonarQube using sonar-scanner.

# Installation

- Download the latest version of the artifact
- Stop Sonarqube server
- Copy the jar file in $SONAR_PATH/extensions/plugins
- Start Sonarqube server
    
# Use the plugin
- Create a sonar-project.properties file
```
sonar.projectKey=my:project
sonar.projectName=My project
sonar.projectVersion=1.0
sonar.hlint.reportPath=hlintReport.json
sonar.sources=./
```

- Install HLint using your favorite tool
```
cabal update
cabal install hlint
```
or
```
stack update
stack install hlint
```

- Produce the HLint report of the code to be analyzed, and save it in a file named with the same name as property `sonar.hlint.reportPath` (default `hlintReport.json`)   
```
hlint YOUR_CODE.hs --json > hlintReport.json
hlint . --json > hlintReport.json
```

- Start the analysis with sonar scanner 
```
sonar-scanner
```

# Development
## Build
```
mvn clean install
```

## Run (using Docker)
```
# Start SonarQube instance on localhost:9000 (admin credentials are admin/admin)
docker container run \
--name=sonarqube \
-p 9000:9000 \
-v $(pwd)/target/sonar-haskell-plugin-1.0.jar:/opt/sonarqube/extensions/plugins/sonar-haskell-plugin-1.0.jar \
sonarqube
# Run analysis on Haskell project
docker container run \
--rm \
--link sonarqube \
-v <path to Haskell project, e.g. /Users/Julien/code/my-haskell-project>:/root/src \
-w="/root/src" \
newtmitch/sonar-scanner:3.2.0 \
sonar-scanner \
-Dsonar.host.url=http://sonarqube:9000
```

## Debug
Since a Sonar plugin is composed of 3 pieces (Server, Compute Engine and Scanner), there are 3 debug ports.
```
# 1) Start SonarQube instance on localhost:9000 (admin credentials are admin/admin)
docker container run \
--name=sonarqube \
-p 9000:9000 -p 5005:5005 -p 5006:5006 \
-v $(pwd)/target/sonar-haskell-plugin-1.0.jar:/opt/sonarqube/extensions/plugins/sonar-haskell-plugin-1.0.jar \
sonarqube:7.1 \
-Dsonar.web.javaAdditionalOpts=-agentlib:jdwp=transport=dt_socket,server=y,suspend=y,address=5005 \
-Dsonar.ce.javaAdditionalOpts=-agentlib:jdwp=transport=dt_socket,server=y,suspend=y,address=5006
# 2) Run analysis on Haskell project
docker container run \
--rm \
--link sonarqube \
-p 5007:5007 \
-e "SONAR_SCANNER_OPTS=-agentlib:jdwp=transport=dt_socket,server=y,suspend=y,address=5007" \
-v <path to Haskell project, e.g. /Users/Julien/code/my-haskell-project>:/root/src \
-w="/root/src" \
newtmitch/sonar-scanner:3.2.0 \
sonar-scanner \
-Dsonar.host.url=http://sonarqube:9000
```
During SonarQube startup (step 1) you will first have to attach to port 5005 (Server), then port 5006 (Compute Engine).
During Sonar Scanner analysis (step 2) you will have to attach to port 5007 (Scanner).
