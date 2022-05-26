/*
 *  Copyright (c) 2022 - Thoughtworks Inc. All rights reserved.
 */

package integration;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

public class ResourceFileUtil {

    public static final String NEW_LINE_CHAR = "\n";

    String getJsonString(String path) throws IOException {
        ClassLoader classLoader = getClass().getClassLoader();
        File jsonFile = new File(classLoader.getResource(path).getFile());

        String expectedResponse = Files.readString(jsonFile.toPath()).replace(NEW_LINE_CHAR, "");
        return expectedResponse;
    }
}


