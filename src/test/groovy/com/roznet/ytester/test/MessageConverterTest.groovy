package com.roznet.ytester.test

import com.roznet.ytester.data.TestRunResult
import com.roznet.ytester.data.TestSuiteRunData
import com.roznet.ytester.spring.TestResultHttpMessageConverter
import com.roznet.ytester.test.util.StringHttpInputMessage

class MessageConverterTest extends BaseTest {

    def "test passing"() {

        setup:

        def inputMessage = new StringHttpInputMessage(getClass().getResourceAsStream('/sample-html/passing.html'))

        TestResultHttpMessageConverter converter = new TestResultHttpMessageConverter()

        when: "Read passing html"

        def result = converter.read(TestRunResult.class, inputMessage)

        then:

        result

        result.testSuiteRunData.size() == 1
        def first = result.testSuiteRunData.first()
        first.passed
        first.testCaseRunData.size() == 2

        first.testCaseRunData.each {
            assert it.passed
        }

    }


    def "test failing"() {

        setup:

        def inputMessage = new StringHttpInputMessage(getClass().getResourceAsStream('/sample-html/failing.html'))

        TestResultHttpMessageConverter converter = new TestResultHttpMessageConverter()

        when: "Read failing html"

        def result = converter.read(TestRunResult.class, inputMessage)

        then:

        result

        result.testSuiteRunData.size() == 1
        def first = result.testSuiteRunData.first()
        first.testCaseRunData.size() == 3

        !first.passed

        first.testCaseRunData.each {
            assert !it.passed
        }

    }

    def "test mixed"() {

        setup:

        def inputMessage = new StringHttpInputMessage(getClass().getResourceAsStream('/sample-html/passing-and-failing.html'))

        TestResultHttpMessageConverter converter = new TestResultHttpMessageConverter()

        when: "Read mixed html"

        def result = converter.read(TestRunResult.class, inputMessage)

        then:

        result

        result.testSuiteRunData.size() == 1
        def first = result.testSuiteRunData.first()
        first.testCaseRunData.size() == 2

        !first.passed

        first.testCaseRunData[0].passed
        !first.testCaseRunData[1].passed
    }

    def "test multi passed"() {
        setup:

        def inputMessage = new StringHttpInputMessage(getClass().getResourceAsStream('/sample-html/multi-passed.html'))

        TestResultHttpMessageConverter converter = new TestResultHttpMessageConverter()

        when: "Read multi html"

        def result = converter.read(TestRunResult.class, inputMessage)

        then:

        result

        def testSuiteRunData = result.testSuiteRunData
        testSuiteRunData.size() == 3

        testSuiteRunData.each {
            assert it.passed
        }

        testSuiteRunData[0].testClass == 'de.hybris.platform.commercefacades.product.converters.populator.ProductBasicPopulatorTest'
        testSuiteRunData[1].testClass == 'de.hybris.platform.commercefacades.product.converters.populator.ProductCategoriesPopulatorTest'
        testSuiteRunData[2].testClass == 'de.hybris.platform.commercefacades.product.converters.populator.ProductClassificationPopulatorTest'


        testSuiteRunData[0].testCaseRunData.size() == 3

        testSuiteRunData[0].testCaseRunData.each {
            assert it.passed
        }

        testSuiteRunData[0].testCaseRunData[0].testCaseName == 'testPopulateNotVariantTyped'
        testSuiteRunData[0].testCaseRunData[1].testCaseName == 'testPopulateAttributeFallback'
        testSuiteRunData[0].testCaseRunData[2].testCaseName == 'testPopulate'

        testSuiteRunData[1].testCaseRunData.size() == 1
        testSuiteRunData[1].testCaseRunData[0].testCaseName == 'testPopulate'

        testSuiteRunData[2].testCaseRunData.size() == 1
        testSuiteRunData[2].testCaseRunData[0].testCaseName == 'testPopulate'

    }
}
