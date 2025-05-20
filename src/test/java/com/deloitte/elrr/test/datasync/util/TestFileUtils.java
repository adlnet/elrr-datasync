package com.deloitte.elrr.test.datasync.util;

import java.io.File;
import java.io.IOException;

import org.springframework.core.io.ClassPathResource;

public final class TestFileUtils {
    /**
     * @param filename
     * @return file
     * @throws IOException
     */
    public static File getJsonTestFile(String filename) throws IOException {

        File file;

        try {
            file = new ClassPathResource(filename).getFile();
        } catch (IOException e) {
            e.printStackTrace();
            throw e;
        }

        return file;
    }

    private TestFileUtils() {
        throw new UnsupportedOperationException(
                "This is a utility class and cannot be instantiated");
    }
}
