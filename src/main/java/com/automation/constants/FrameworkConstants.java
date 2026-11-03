package com.automation.constants;

import java.io.File;

/**
 * Global framework constants containing file paths, timeouts, and report configurations.
 */
public final class FrameworkConstants {

    private FrameworkConstants() {
        // Prevent instantiation
    }

    private static final String USER_DIR = System.getProperty("user.dir");
    public static final String RESOURCES_PATH = USER_DIR + File.separator + "src" + File.separator + "test" + File.separator + "resources";
    
    // Config paths
    public static final String CONFIG_DIR = RESOURCES_PATH + File.separator + "config";
    public static final String DEFAULT_CONFIG_FILE = CONFIG_DIR + File.separator + "config.properties";

    // Test data paths
    public static final String TEST_DATA_DIR = RESOURCES_PATH + File.separator + "testdata";
    public static final String LOGIN_DATA_JSON = TEST_DATA_DIR + File.separator + "loginData.json";
    public static final String CAR_DATA_JSON = TEST_DATA_DIR + File.separator + "carData.json";
    public static final String EXCEL_TEST_DATA = TEST_DATA_DIR + File.separator + "testdata.xlsx";

    // Output & Reports paths
    public static final String REPORTS_DIR = USER_DIR + File.separator + "reports";
    public static final String EXTENT_REPORT_PATH = REPORTS_DIR + File.separator + "extent-report.html";
    public static final String SCREENSHOTS_DIR = REPORTS_DIR + File.separator + "screenshots";
    public static final String TABULAR_SUMMARY_REPORT = REPORTS_DIR + File.separator + "summary.html";
    public static final String TABULAR_DETAILED_REPORT = REPORTS_DIR + File.separator + "detailed_report.html";

    // Timeouts (seconds)
    public static final int EXPLICIT_WAIT_TIMEOUT = 15;
    public static final int PAGE_LOAD_TIMEOUT = 30;
    public static final int IMPLICIT_WAIT_TIMEOUT = 10;
}
