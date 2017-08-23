package com.roznet.ytester.test

import com.roznet.ytester.data.TestCaseData
import com.roznet.ytester.data.TestRunData
import com.roznet.ytester.data.TestSuiteData
import spock.lang.Unroll

class UrlBuilderTests extends BaseTest {

    def "test suite only"() {
        setup:


        TestRunData testRunData = new TestRunData()

        testRunData.testSuiteData = []

        testRunData.testSuiteData << new TestSuiteData()

        def testSuiteData = testRunData.testSuiteData[0]

        testSuiteData.testClass = 'testy.test.Test123'

        when:

        def url = webTestRunner.buildUrl testRunData

        then:

        url == 'http://localhost:9001/test/run/testsuites?testSuites=testy.test.Test123'

    }

    @Unroll
    def "test case to url"() {

        when:

        def result = webTestRunner.buildTestCaseUrl(className, testCaseName)

        then:

        result == expected

        where:

        className              | testCaseName  | expected
        'com.foo.dummy.Hello'  | 'bob'         | 'com.foo.dummy.Hello#bob(com.foo.dummy.Hello)'
        'com.foo.dummy.Hello'  | 'Test 123'    | 'com.foo.dummy.Hello#Test 123(com.foo.dummy.Hello)'

    }

    def "test run data to URL"() {
        setup:

        TestRunData testRunData = new TestRunData()

        testRunData.testSuiteData = []

        testRunData.testSuiteData << new TestSuiteData()

        def testSuiteData = testRunData.testSuiteData[0]

        testSuiteData.testClass = 'com.foo.dummy.Hello'
        testSuiteData.testCaseData = []

        def tc1 = new TestCaseData()
        def tc2 = new TestCaseData()

        tc1.testCaseName = 'test with spaces'
        tc2.testCaseName = 'test_with_no_spaces'

        testSuiteData.testCaseData << tc1
        testSuiteData.testCaseData << tc2

        when:

        def url = webTestRunner.buildUrl(testRunData)

        then:

        url == 'http://localhost:9001/test/run/testsuites?testCases=com.foo.dummy.Hello%23test%20with%20spaces(com.foo.dummy.Hello)&testCases=com.foo.dummy.Hello%23test_with_no_spaces(com.foo.dummy.Hello)'
    }
}