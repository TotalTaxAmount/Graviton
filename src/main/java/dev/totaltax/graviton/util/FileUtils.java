package dev.totaltax.graviton.util;

import java.io.File;
import java.util.Arrays;

public class FileUtils {
    public static File createPath(String... paths) {
        final String[] path = {""};
        Arrays.stream(paths).forEach((p) -> {
            path[0] = path[0] + File.separator + p;
        });

        return new File(path[0]);
    }
}
