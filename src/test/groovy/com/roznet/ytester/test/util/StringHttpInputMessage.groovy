package com.roznet.ytester.test.util

import org.springframework.http.HttpHeaders
import org.springframework.http.HttpInputMessage
import org.springframework.http.MediaType
import org.springframework.util.StreamUtils

import java.nio.charset.Charset

class StringHttpInputMessage implements HttpInputMessage {

    final String html

    StringHttpInputMessage(String html) {
        this.html = html
    }

    StringHttpInputMessage(InputStream is) {
        this.html = StreamUtils.copyToString(is, Charset.forName('UTF-8'))
    }

    @Override
    InputStream getBody() throws IOException {
        new ByteArrayInputStream(html.getBytes('UTF-8'))
    }

    @Override
    HttpHeaders getHeaders() {
        HttpHeaders headers = new HttpHeaders()
        headers.setContentType(new MediaType(MediaType.TEXT_HTML, Charset.forName('UTF-8')))
        headers
    }
}
