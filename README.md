# SlackUserPresenceManager

How to use:

1) Go to https://api.slack.com/custom-integrations/legacy-tokens and generate a new token for the team you prefer to use the application on.
2) Put the token in the src/main/resource/build.properties file in the resources file. This would also be the place where you update your custom statuses and emojis.
3) Build the runnable jar by executing "gradlew clean build". The runnable jar will be created at ./build/libs/ folder.
4) Run the jar file by executing the following command: "java -jar SlackUserPresenceManager-1.0.jar"

This will start a listener which is going to listen on your windows session change events. Currently locking and unlocking will trigger your status and prescence according to the slack token provided.

5) To stop the listener, press ctrl + c.
