package com.roznet.ytester.test

import com.roznet.ytester.data.TestCaseData
import com.roznet.ytester.data.TestCaseRunData
import com.roznet.ytester.data.TestRunData
import com.roznet.ytester.data.TestRunResult
import com.roznet.ytester.data.TestSuiteData
import com.roznet.ytester.data.TestSuiteRunData
import org.junit.Ignore

class RealWebTest extends BaseTest {
    @Ignore // run manually - requires running server
    def "make actual http request"() {
        setup:

        TestRunData testRunData = new TestRunData()

        testRunData.testSuiteData = []

        testRunData.testSuiteData << new TestSuiteData()

        def testSuiteData = testRunData.testSuiteData[0]

        testSuiteData.testClass = 'de.hybris.platform.acceleratorfacades.device.populators.ResponsiveImagePopulatorTest'
        testSuiteData.testCaseData = []
        TestCaseData tcData = new TestCaseData()
        tcData.testCaseName = 'testForImproperRegex'
        testSuiteData.testCaseData << tcData

        when:

        TestRunResult result = webTestRunner.runTests testRunData

        then:

        result

        result.testSuiteRunData.size() == 1

        def suiteRunData = result.testSuiteRunData.first()

        suiteRunData.passed
        suiteRunData.testCaseRunData.size() == 1

        def caseRunData = suiteRunData.testCaseRunData.first()

        caseRunData.passed

    }
}
