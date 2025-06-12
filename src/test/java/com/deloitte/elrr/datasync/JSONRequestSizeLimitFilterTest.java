package com.deloitte.elrr.datasync;

import static org.junit.jupiter.api.Assertions.assertFalse;

import java.io.IOException;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpHeaders;
import org.springframework.mock.web.MockFilterChain;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.util.ReflectionTestUtils;

import jakarta.servlet.ServletException;

@ExtendWith(MockitoExtension.class)
public class JSONRequestSizeLimitFilterTest {

    private WrappedHttp http;
    private JSONRequestSizeLimitFilter sl = new JSONRequestSizeLimitFilter();

    @Test
    void testSizeLimit() throws IOException, ServletException {

        ReflectionTestUtils.setField(sl, "maxSizeLimit", 2000000L);
        ReflectionTestUtils.setField(sl, "checkMediaTypeJson", false);

        MockHttpServletRequest req = new MockHttpServletRequest();
        req.addHeader(HttpHeaders.CONTENT_TYPE, "application/json");
        req.addParameter("anything", "goes");
        http = new WrappedHttp(req, "{Unwise: napping during work}");

        MockHttpServletResponse res = new MockHttpServletResponse();
        MockFilterChain chain = new MockFilterChain();
        sl.doFilter(http, res, chain);
        assertFalse(res.isCommitted());

    }

    @Test
    void testMimeType() throws IOException, ServletException {

        ReflectionTestUtils.setField(sl, "maxSizeLimit", 2000000L);
        ReflectionTestUtils.setField(sl, "checkMediaTypeJson", true);

        MockHttpServletRequest req = new MockHttpServletRequest();
        req.addHeader(HttpHeaders.CONTENT_TYPE, "application/json");
        req.addParameter("anything", "goes");
        http = new WrappedHttp(req, "{Unwise: napping during work}");

        MockHttpServletResponse res = new MockHttpServletResponse();
        MockFilterChain chain = new MockFilterChain();
        sl.doFilter(http, res, chain);
        assertFalse(res.isCommitted());

    }

}
