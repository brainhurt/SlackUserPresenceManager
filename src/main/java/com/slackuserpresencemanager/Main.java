package com.slackuserpresencemanager;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Created by Tharaka on 4/15/2017.
 * Project: SlackUserPresenceManager
 */
public class Main {

	private static final String PROPERTY_FILE_NAME = "build.properties";

	public static void main (String[] args) {
		Properties properties = new Properties();
		InputStream inputStream = Main.class.getClassLoader().getResourceAsStream(PROPERTY_FILE_NAME);
		if (inputStream == null) {
			throw new IllegalArgumentException("The config.properties file was not found!");
		} else {
			try {
				properties.load(inputStream);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		new WindowsSessionManager(properties);
	}
}
