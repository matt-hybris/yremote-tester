package com.roznet.ytester.api

import com.roznet.ytester.data.TestRunData
import com.roznet.ytester.data.TestRunResult

interface TestRunner {
    TestRunResult runTests(TestRunData testRunData)
}