package com.deloitte.elrr.test.datasync.test.util;

import java.io.File;

public class TestFileUtils {
  public static File getJsonTestFile(String filename) {
    String path = String.format("src/test/java/%s.json", filename);
    return new File(path);
  }
}
