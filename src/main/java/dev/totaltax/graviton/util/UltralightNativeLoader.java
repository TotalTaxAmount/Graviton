package dev.totaltax.graviton.util;


import dev.totaltax.graviton.Graviton;

import java.io.File;

public class UltralightNativeLoader {
    public static void loadWin(File binDir) {
        System.load(new File(binDir.toPath().toFile(), "UltralightCore.dll").toPath().toAbsolutePath().toString());
        System.load(new File(binDir.toPath().toFile(), "glib-2.0-0.dll").toPath().toAbsolutePath().toString());
        System.load(new File(binDir.toPath().toFile(), "gobject-2.0-0.dll").toPath().toAbsolutePath().toString());
        System.load(new File(binDir.toPath().toFile(), "gmodule-2.0-0.dll").toPath().toAbsolutePath().toString());
        System.load(new File(binDir.toPath().toFile(), "gio-2.0-0.dll").toPath().toAbsolutePath().toString());
        System.load(new File(binDir.toPath().toFile(), "gstreamer-full-1.0.dll").toPath().toAbsolutePath().toString());
        System.load(new File(binDir.toPath().toFile(), "WebCore.dll").toPath().toAbsolutePath().toString());
        System.load(new File(binDir.toPath().toFile(), "Ultralight.dll").toPath().toAbsolutePath().toString());
        System.load(new File(binDir.toPath().toFile(), "ultralight-java-gpu.dll").toPath().toAbsolutePath().toString());
        System.load(new File(binDir.toPath().toFile(), "AppCore.dll").toPath().toAbsolutePath().toString());
        System.load(new File(binDir.toPath().toFile(), "ultralight-java.dll").toPath().toAbsolutePath().toString());
    }

    public static void loadMac(File binDir) {
        Graviton.getInstance().getLogger().fatal("Mac OS is not supported as of right now");
        System.exit(0);
    }

    public static void loadLinux(File binDir) {
        System.load(new File(binDir.toPath().toFile(), "libAppCore.so").toPath().toAbsolutePath().toString());
        System.load(new File(binDir.toPath().toFile(), "libUltralight.so").toPath().toAbsolutePath().toString());
        System.load(new File(binDir.toPath().toFile(), "libUltralightCore.so").toPath().toAbsolutePath().toString());
        System.load(new File(binDir.toPath().toFile(), "libWebCore.so").toPath().toAbsolutePath().toString());

    }
}
