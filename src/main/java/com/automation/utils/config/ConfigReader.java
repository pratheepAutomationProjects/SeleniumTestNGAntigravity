package com.automation.utils.config;

import com.automation.constants.FrameworkConstants;
import com.automation.utils.logging.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

/**
 * Utility to load framework and environment configurations.
 * Supports environment switching (-Denv=qa) and system property overrides (-Dbrowser=firefox).
 */
public final class ConfigReader {

    private static final Properties properties = new Properties();

    static {
        loadProperties();
    }

    private ConfigReader() {}

    private static void loadProperties() {
        // 1. Load base/default config
        File defaultFile = new File(FrameworkConstants.DEFAULT_CONFIG_FILE);
        if (defaultFile.exists()) {
            try (FileInputStream fis = new FileInputStream(defaultFile)) {
                properties.load(fis);
                Log.info("Loaded base configuration from: %s", defaultFile.getAbsolutePath());
            } catch (IOException e) {
                Log.error("Failed to load default properties: " + e.getMessage(), e);
            }
        }

        // 2. Check for environment override (e.g. -Denv=qa -> config-qa.properties)
        String env = System.getProperty("env");
        if (env != null && !env.trim().isEmpty()) {
            File envFile = new File(FrameworkConstants.CONFIG_DIR + File.separator + "config-" + env.trim().toLowerCase() + ".properties");
            if (envFile.exists()) {
                try (FileInputStream fis = new FileInputStream(envFile)) {
                    properties.load(fis);
                    Log.info("Loaded environment specific configuration from: %s", envFile.getAbsolutePath());
                } catch (IOException e) {
                    Log.error("Failed to load env properties from " + envFile.getName() + ": " + e.getMessage(), e);
                }
            } else {
                Log.warn("Environment config file not found: %s. Using default configs.", envFile.getAbsolutePath());
            }
        }
    }

    /**
     * Retrieve property value. System properties take precedence over properties file.
     */
    public static String get(String key) {
        String systemVal = System.getProperty(key);
        if (systemVal != null && !systemVal.trim().isEmpty()) {
            return systemVal.trim();
        }
        String propVal = properties.getProperty(key);
        return propVal != null ? propVal.trim() : null;
    }

    public static String get(String key, String defaultValue) {
        String val = get(key);
        return val != null ? val : defaultValue;
    }

    public static int getInt(String key, int defaultValue) {
        String val = get(key);
        if (val != null) {
            try {
                return Integer.parseInt(val);
            } catch (NumberFormatException ignored) {}
        }
        return defaultValue;
    }

    public static boolean getBoolean(String key, boolean defaultValue) {
        String val = get(key);
        if (val != null) {
            return Boolean.parseBoolean(val);
        }
        return defaultValue;
    }
}
