package com.deloitte.elrr.datasync;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.IOException;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpHeaders;
import org.springframework.mock.web.MockFilterChain;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.util.ReflectionTestUtils;

import jakarta.servlet.ServletException;

@ExtendWith(MockitoExtension.class)
public class JSONRequestSizeLimitFilterTest {

    private WrappedHttp http;
    private JSONRequestSizeLimitFilter sl = new JSONRequestSizeLimitFilter();

    @Test
    @WithMockUser
    void testSizeLimit() throws IOException, ServletException {

        ReflectionTestUtils.setField(sl, "maxSizeLimit", 2000000L);
        ReflectionTestUtils.setField(sl, "checkMediaTypeJson", false);

        MockHttpServletRequest req = new MockHttpServletRequest();
        req.addHeader(HttpHeaders.CONTENT_TYPE, "application/json");
        req.addParameter("anything", "goes");
        String requestBody = "{Unwise: napping during work}";
        req.setContent(requestBody.getBytes());
        http = new WrappedHttp(req, requestBody);
        MockHttpServletResponse res = new MockHttpServletResponse();
        MockFilterChain chain = new MockFilterChain();
        sl.doFilter(http, res, chain);
        assertEquals(res.getErrorMessage(), null);

    }

    @Test
    @WithMockUser
    void testOverSizeLimit() throws IOException, ServletException {

        ReflectionTestUtils.setField(sl, "maxSizeLimit", 1L);
        ReflectionTestUtils.setField(sl, "checkMediaTypeJson", false);

        MockHttpServletRequest req = new MockHttpServletRequest();
        req.addHeader(HttpHeaders.CONTENT_TYPE, "application/json");
        req.addParameter("anything", "goes");
        String requestBody = "{Unwise: napping during work}";
        req.setContent(requestBody.getBytes());
        http = new WrappedHttp(req, requestBody);
        MockHttpServletResponse res = new MockHttpServletResponse();
        MockFilterChain chain = new MockFilterChain();
        sl.doFilter(http, res, chain);
        assertEquals(res.getErrorMessage(), "Request size exceeds the limit.");

    }

    @Test
    @WithMockUser
    void testMimeType() throws IOException, ServletException {

        ReflectionTestUtils.setField(sl, "maxSizeLimit", 2000000L);
        ReflectionTestUtils.setField(sl, "checkMediaTypeJson", true);

        MockHttpServletRequest req = new MockHttpServletRequest();
        req.addHeader(HttpHeaders.CONTENT_TYPE, "application/json");
        req.addParameter("anything", "goes");
        String requestBody = "{Unwise: napping during work}";
        req.setContent(requestBody.getBytes());
        http = new WrappedHttp(req, requestBody);
        MockHttpServletResponse res = new MockHttpServletResponse();
        MockFilterChain chain = new MockFilterChain();
        sl.doFilter(http, res, chain);
        assertEquals(res.getErrorMessage(), null);

    }

}
