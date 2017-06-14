package com.slackuserpresencemanager;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Created by Tharaka on 4/15/2017.
 * Project: SlackUserPresenceManager
 */
public class Main {

    private static final Logger LOGGER = LogManager.getLogger(Main.class);

    private static final String PROPERTY_FILE_NAME = "build.properties";

    private static Properties properties = new Properties();

    static String getProperty(String key) {
        return properties.getProperty(key, "");
    }

    public static void main(String[] args) throws IOException {
        InputStream inputStream = Main.class.getClassLoader().getResourceAsStream(PROPERTY_FILE_NAME);
        if (inputStream == null) {
            throw new FileNotFoundException("The config.properties file was not found!");
        }
        properties.load(inputStream);
        new WindowsSessionManager();
    }
}
