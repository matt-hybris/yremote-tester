package com.roznet.ytester.test

import com.roznet.ytester.api.impl.WebTestRunner
import spock.lang.Specification

class BaseTest extends Specification {
    WebTestRunner webTestRunner = new WebTestRunner('localhost', 9001)
}
