package com.deloitte.elrr.datasync;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertFalse;

import java.io.IOException;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockFilterChain;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.util.ReflectionTestUtils;

import com.deloitte.elrr.datasync.util.LogCapture;
import com.deloitte.elrr.datasync.util.LogCaptureExtension;

import jakarta.servlet.ServletException;

@ExtendWith({ MockitoExtension.class, LogCaptureExtension.class })
public class JSONRequestSizeLimitFilterTest {

    private WrappedHttp http;
    private JSONRequestSizeLimitFilter sl = new JSONRequestSizeLimitFilter();

    @Test
    void testSizeLimitTooBig(LogCapture logCapture) throws IOException,
            ServletException {

        logCapture.clear();

        ReflectionTestUtils.setField(sl, "maxSizeLimit", 2000000L);

        MockHttpServletRequest req = new MockHttpServletRequest();
        req.addParameter("anything", "goes");
        http = new WrappedHttp(req, "{Unwise: napping during work}");

        MockHttpServletResponse res = new MockHttpServletResponse();
        MockFilterChain chain = new MockFilterChain();
        sl.doFilter(http, res, chain);
        assertFalse(res.isCommitted());
        assertThat(logCapture.getLoggingEvents()).hasSize(0);

    }

}
