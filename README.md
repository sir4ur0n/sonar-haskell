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
-p9000:9000 \
-v $(pwd)/target/sonar-haskell-plugin-1.0-SNAPSHOT.jar:/opt/sonarqube/extensions/plugins/sonar-haskell-plugin-1.0-SNAPSHOT.jar \
sonarqube
```

## Debug
