package com.automation.listeners;

import com.automation.utils.config.ConfigReader;
import com.automation.utils.logging.Log;
import org.testng.IRetryAnalyzer;
import org.testng.ITestResult;

/**
 * Automatically re-runs failed test cases up to the configured max retry count.
 */
public class RetryAnalyzer implements IRetryAnalyzer {

    private int retryCount = 0;
    private static final int MAX_RETRY_COUNT = ConfigReader.getInt("max.retry.count", 1);

    @Override
    public boolean retry(ITestResult result) {
        if (retryCount < MAX_RETRY_COUNT) {
            retryCount++;
            Log.warn("Retrying failed test '%s' (Attempt %d of %d)", result.getName(), retryCount, MAX_RETRY_COUNT);
            return true;
        }
        return false;
    }
}
