package com.roznet.ytester.spring

import com.google.common.base.Splitter
import com.roznet.ytester.data.TestCaseRunData
import com.roznet.ytester.data.TestRunResult
import com.roznet.ytester.data.TestSuiteRunData
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.springframework.http.HttpInputMessage
import org.springframework.http.HttpOutputMessage
import org.springframework.http.MediaType
import org.springframework.http.converter.AbstractHttpMessageConverter
import org.springframework.http.converter.HttpMessageNotReadableException
import org.springframework.http.converter.HttpMessageNotWritableException

import java.nio.charset.Charset

class TestResultHttpMessageConverter extends AbstractHttpMessageConverter<TestRunResult> {

    TestResultHttpMessageConverter() {
        this.supportedMediaTypes = [MediaType.TEXT_HTML]
    }

    @Override
    protected boolean supports(Class<?> clazz) {
        TestRunResult.class
    }

    @Override
    protected TestRunResult readInternal(Class<? extends TestRunResult> clazz, HttpInputMessage inputMessage) throws IOException, HttpMessageNotReadableException {
        Charset charset = getContentTypeCharset(inputMessage.getHeaders().getContentType())

        Document document = Jsoup.parse(inputMessage.getBody(), charset.name(), "")

        TestRunResult result = new TestRunResult()

        result.testSuiteRunData = []

        document.select('.toggleTestCases').each {

            def testSuiteName = it.parent().nextElementSibling().select('.re-run-test').text()

            boolean suitePassed = it.parent().parent().select('td').last().select('img').attr('alt').toBoolean()

            def runData = new TestSuiteRunData()

            runData.passed = suitePassed
            runData.testClass = testSuiteName

            result.testSuiteRunData << runData

            runData.testCaseRunData = []

            def cssClassForTest = getCssClassForTest testSuiteName

            it.parent().parent().parent().select(".$cssClassForTest").each { tdata ->

                def testCaseName = tdata.select('a.re-run-test').text()

                TestCaseRunData trRunData = new TestCaseRunData()
                trRunData.testCaseName = testCaseName

                boolean passed = tdata.select('img.testCaseIcon_true')

                trRunData.passed = passed
                runData.testCaseRunData << trRunData

                if (!passed) {
                    trRunData.errorMsg = tdata.select('td').last().attr('data-error')
                    trRunData.errorStackTrace = tdata.select('td').last().attr('data-stackTrace')
                }

            }

        }
        result
    }

    def getCssClassForTest(String testName) {
        def last = Splitter.on('.').splitToList(testName).last()

        last + "_testCases"
    }

    @Override
    protected void writeInternal(TestRunResult testRunResult, HttpOutputMessage outputMessage) throws IOException, HttpMessageNotWritableException {
        throw new UnsupportedOperationException('The converter only works for reading')
    }

    private Charset getContentTypeCharset(MediaType contentType) {
        if (contentType != null && contentType.getCharset() != null) {
            return contentType.getCharset()
        }
        getDefaultCharset()
    }
}
