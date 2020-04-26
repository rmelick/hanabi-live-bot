# hanabi-live-bot

http://hanabi.aiclash.com/domain.html
https://ieeexplore.ieee.org/document/8848008

Run with `./gradlew bootRun`

Or, to run locally with docker running the same image as it will deploy in production
```bash
RUN_PROJECT_ID=local-bot RUN_SERVICE_NAME=local-bot GITHUB_SHA=local ./gradlew jibDockerBuild
docker run --rm --name hlb -p=8080:8080 gcr.io/local-bot/local-bot:local
```

## Legacy
You can also build a different docker image directly, but the classpath is messed up becase
of Spring's fat jar
```bash
docker build -t hanabi-live-bot/hanabi-live-bot .
docker run --rm --name hlb -p=8080:8080 hanabi-live-bot/hanabi-live-bot
```

To manually run the same thing that happens inside of docker
```bash
./gradlew build && java -jar build/libs/hanabi-live-bot-1.0-SNAPSHOT.jar
```

