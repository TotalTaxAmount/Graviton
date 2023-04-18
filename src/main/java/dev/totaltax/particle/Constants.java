package dev.totaltax.particle;

public class Constants {
    public static class system {
        public static String OS = System.getProperty("os.name").toLowerCase();
        public static boolean IS_WINDOWS = OS.contains("win");
        public static boolean IS_MAC = OS.contains("mac") || OS.contains("darwin");
        public static boolean IS_UNIX = OS.contains("nix") || OS.contains("nux") || OS.indexOf("aix") > 0;
    }
}

