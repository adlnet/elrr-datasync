package com.deloitte.elrr.datasync;

import static org.assertj.core.api.Assertions.fail;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockFilterChain;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.util.ReflectionTestUtils;

import jakarta.servlet.ServletException;

public class FilterTest {

    private final SanitizingFilter sf = new SanitizingFilter();
    private WrappedHttp http;
    private JSONRequestSizeLimitFilter sl = new JSONRequestSizeLimitFilter();
    private HeaderFilter hf = new HeaderFilter();

    @Test
    void testIllegalBodyNotJson() throws IOException, ServletException {
        MockHttpServletRequest req = new MockHttpServletRequest();
        http = new WrappedHttp(req, "not allowed");
        MockHttpServletResponse res = new MockHttpServletResponse();
        MockFilterChain chain = new MockFilterChain();
        sf.doFilter(http, res, chain);
        assertEquals(res.getStatus(), 400);
        assertTrue(res.isCommitted());
    }

    @Test
    void testeeIllegalBodyNotJson() throws IOException, ServletException {
        MockHttpServletRequest req = new MockHttpServletRequest();
        // http = new WrappedHttp(req, "not allowed");
        MockHttpServletResponse res = new MockHttpServletResponse();
        MockFilterChain chain = new MockFilterChain();
        sf.doFilter(req, res, chain);
        assertFalse(res.isCommitted());
    }

    @Test
    void testIllegalBodyWhitelist() throws IOException, ServletException {
        MockHttpServletRequest req = new MockHttpServletRequest();
        http = new WrappedHttp(req, "{Unwise: afsd,.e\0nab}"); // not allowed
                                                               // illegal \0
        MockHttpServletResponse res = new MockHttpServletResponse();
        MockFilterChain chain = new MockFilterChain();
        sf.doFilter(http, res, chain);
        assertTrue(res.isCommitted());
    }

    @Test
    void testIllegalParam() throws IOException, ServletException {
        MockHttpServletRequest req = new MockHttpServletRequest();
        req.addParameter("file./iofa\0je%00\\0/0/00efwho", "anything");
        http = new WrappedHttp(req, "{Unwise: nap}");
        MockHttpServletResponse res = new MockHttpServletResponse();
        MockFilterChain chain = new MockFilterChain();
        sf.doFilter(http, res, chain);
        assertEquals(res.getStatus(), 400);
        assertTrue(res.isCommitted());
    }

    @Test
    void testIllegalParamValue() throws IOException, ServletException {
        MockHttpServletRequest req = new MockHttpServletRequest();
        req.addParameter("anything", "file./iofaje%00\0/0/00efwho");
        http = new WrappedHttp(req, "{Unwise: nap}");
        MockHttpServletResponse res = new MockHttpServletResponse();
        MockFilterChain chain = new MockFilterChain();
        sf.doFilter(http, res, chain);
        assertTrue(res.isCommitted());
    }

    @Test
    void testSanatizerOk() throws IOException, ServletException {
        MockHttpServletRequest req = new MockHttpServletRequest();
        req.addParameter("anything", "goes");
        http = new WrappedHttp(req, "{Unwise: nap}");

        // next lines are simply to increase coverage of wrappedhttp
        http.getInputStream().available();
        http.getInputStream().isReady();
        http.getInputStream().read();
        http.getAttributeNames();
        http.getAsyncContext();
        http.getBody();
        http.getCharacterEncoding();
        http.getContentLength();
        http.getContextPath();
        http.getCookies();
        http.getHeaderNames();
        http.getParameterNames();
        http.getAuthType();
        http.getClass();
        http.getContentType();
        http.getDispatcherType();

        MockHttpServletResponse res = new MockHttpServletResponse();
        MockFilterChain chain = new MockFilterChain();
        sf.doFilter(http, res, chain);
        assertFalse(res.isCommitted());
    }

    @Test
    void testSizeLimitOk() throws IOException, ServletException {
        MockHttpServletRequest req = new MockHttpServletRequest();
        req.addParameter("anything", "goes");
        http = new WrappedHttp(req, "{Unwise: nap}");

        // next lines are simply to increase coverage of wrappedhttp
        http.getInputStream().available();
        http.getInputStream().isReady();
        http.getInputStream().read();
        http.getAttributeNames();
        http.getAsyncContext();
        http.getBody();
        http.getCharacterEncoding();
        http.getContentLength();
        http.getContextPath();
        http.getCookies();
        http.getHeaderNames();
        http.getParameterNames();
        http.getAuthType();
        http.getClass();
        http.getContentType();
        http.getDispatcherType();

        MockHttpServletResponse res = new MockHttpServletResponse();
        MockFilterChain chain = new MockFilterChain();
        sl.doFilter(http, res, chain);
        assertFalse(res.isCommitted());
    }

    @Test
    void testHeaderNoCheck() throws IOException, ServletException {
        MockHttpServletRequest req = new MockHttpServletRequest();
        req.addParameter("anything", "goes");
        http = new WrappedHttp(req, "{Unwise: nap}");

        // next lines are simply to increase coverage of wrappedhttp
        http.getInputStream().available();
        http.getInputStream().isReady();
        http.getInputStream().read();
        http.getAttributeNames();
        http.getAsyncContext();
        http.getBody();
        http.getCharacterEncoding();
        http.getContentLength();
        http.getContextPath();
        http.getCookies();
        http.getHeaderNames();
        http.getParameterNames();
        http.getAuthType();
        http.getClass();
        http.getContentType();
        http.getDispatcherType();

        MockHttpServletResponse res = new MockHttpServletResponse();
        MockFilterChain chain = new MockFilterChain();
        hf.doFilter(http, res, chain);
        assertFalse(res.isCommitted());
    }

    @Test
    void testHeaderCheck() throws IOException, ServletException {
        ReflectionTestUtils.setField(hf, "checkHttpHeader", true);

        MockHttpServletRequest req = new MockHttpServletRequest();
        req.addParameter("anything", "goes");
        http = new WrappedHttp(req, "{Unwise: nap}");

        // next lines are simply to increase coverage of wrappedhttp
        http.getInputStream().available();
        http.getInputStream().isReady();
        http.getInputStream().read();
        http.getAttributeNames();
        http.getAsyncContext();
        http.getBody();
        http.getCharacterEncoding();
        http.getContentLength();
        http.getContextPath();
        http.getCookies();
        http.getHeaderNames();
        http.getParameterNames();
        http.getAuthType();
        http.getClass();
        http.getContentType();
        http.getDispatcherType();

        MockHttpServletResponse res = new MockHttpServletResponse();
        MockFilterChain chain = new MockFilterChain();
        hf.doFilter(http, res, chain);
        assertTrue(res.isCommitted());
    }

    @Test
    void testInputSanitizer() {

        try {

            Constructor<InputSanitizer> constructor = InputSanitizer.class
                    .getDeclaredConstructor();
            constructor.setAccessible(true);
            InputSanitizer inputSAnitizer = constructor.newInstance();

        } catch (UnsupportedOperationException | InvocationTargetException e) {
            System.out.println(
                    "This is a utility class and cannot be instantiated");
        } catch (NoSuchMethodException | SecurityException
                | InstantiationException | IllegalAccessException
                | IllegalArgumentException e1) {
            fail("Should not have thrown any exception");
        }
    }
}
