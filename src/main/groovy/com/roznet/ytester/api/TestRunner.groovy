package com.roznet.ytester.api

import com.roznet.ytester.data.TestRunData
import com.roznet.ytester.data.TestRunResult

interface TestRunner {
    /**
     * Runs tests data on a default configured server.
     * @param testRunData Test suites and test cases to run
     * @return Test results and errors for failures
     */
    TestRunResult runTests(TestRunData testRunData)
}