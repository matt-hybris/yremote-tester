package com.roznet.ytester.api.impl

import com.roznet.ytester.api.TestRunner
import com.roznet.ytester.data.TestRunData
import com.roznet.ytester.data.TestRunResult
import com.roznet.ytester.spring.TestResultHttpMessageConverter
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.web.client.RestTemplate
import org.springframework.web.util.UriComponentsBuilder
import org.springframework.web.util.UriTemplateHandler

class WebTestRunner implements TestRunner {

    final private Logger LOG = LoggerFactory.getLogger(WebTestRunner.class)

    final String hostname
    final int port
    final RestTemplate restTemplate

    WebTestRunner(String hostname, int port) {
        this.hostname = hostname
        this.port = port
        restTemplate = new RestTemplate()

        restTemplate.setMessageConverters(
                [new TestResultHttpMessageConverter() /*, new StringHttpMessageConverter()*/])

        restTemplate.setUriTemplateHandler(new UriTemplateHandler() {
            /*
            Setting a no-op URI template handler since the default one forces encoding which
            hybris doesn't like. Maybe not the best way to do it, but after trying to let the
            library do the encoding, nothing seems to pass, so I've done it manually here
            and disable further encoding which will be done in RestTemplate.
             */
            @Override
            URI expand(String uriTemplate, Map<String, ?> uriVariables) {
                return URI.create(uriTemplate)
            }

            @Override
            URI expand(String uriTemplate, Object... uriVariables) {
                return URI.create(uriTemplate)
            }
        })
    }

    @Override
    TestRunResult runTests(TestRunData testRunData) {

        String urlString = buildUrl(testRunData)

        def entity = restTemplate.getForEntity(urlString, TestRunResult.class)

        if (entity.statusCode != HttpStatus.OK) {
            // TODO throw some exception here
        }

        entity.body
    }

    String buildUrl(TestRunData testRunData) {
        def uriBuilder = UriComponentsBuilder.newInstance().scheme('http')
                .port(port).host(hostname).path('/test/run/testsuites')

        testRunData.testSuiteData.each { tsd ->
            if (tsd.testCaseData) {
                tsd.testCaseData.each { td ->
                    uriBuilder.queryParam 'testCases', buildTestCaseUrl(tsd.testClass, td.testCaseName)
                }
            } else { // run whole test suite
                uriBuilder.queryParam 'testSuites', tsd.testClass
            }
        }

        def urlString = uriBuilder.build().encode().toUriString()
        LOG.debug("Generated URL {}", urlString)
        urlString
    }

    def buildTestCaseUrl(String className, String testCase) {
        def testCaseConverted = testCase
        "$className#$testCaseConverted($className)"
    }
}
