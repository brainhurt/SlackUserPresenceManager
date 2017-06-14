# SlackUserPresenceManager

How to use:

1) Go to https://api.slack.com/custom-integrations/legacy-tokens and generate a new token for the team you prefer to use the application on.
2) To clean any previous builds, run "gradlew clean".
3) To build the runnable jar by executing "gradlew build". The runnable jar along with the properties file will be created at ./build/libs/ folder.
**The properties file has to be in the same folder as the jar file!**
4) Put the token in the src/main/resource/build.properties file in the resources file. This would also be the place where you update your custom statuses and emojis.
**Multiple tokens should be comma separated.**
5) Run the jar file by executing the following command: "java -jar SlackUserPresenceManager-1.x.jar"

This will start a listener which is going to listen on your windows session change events. Currently locking and unlocking will trigger your status and presence according to the slack token provided.

6) To stop the listener, press ctrl + c.
