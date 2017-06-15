package com.slackuserpresencemanager;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.*;
import java.util.Properties;

/**
 * Created by Tharaka on 4/15/2017.
 * Project: SlackUserPresenceManager
 */
public class Main {

    private static final Logger LOGGER = LogManager.getLogger(Main.class);

    private static final String PROPERTY_FILE_NAME = "SlackUserPresenceManager.properties";

    private static Properties properties = new Properties();

    static String getProperty(String key) {
        return properties.getProperty(key, "");
    }

    public static void main(String[] args) throws IOException {
        File file = new File(PROPERTY_FILE_NAME);
        if (!file.exists()) {
            throw new FileNotFoundException(PROPERTY_FILE_NAME.concat(" was not found!"));
        }
        FileInputStream inputStream = new FileInputStream(file);
        properties.load(inputStream);
        WindowsStateManager.RegisterShutdownListener();
        new WindowsSessionManager();
    }
}
