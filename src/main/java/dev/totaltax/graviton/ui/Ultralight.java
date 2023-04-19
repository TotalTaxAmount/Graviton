package dev.totaltax.graviton.ui;

import com.labymedia.ultralight.UltralightLoadException;
import com.labymedia.ultralight.UltralightPlatform;
import com.labymedia.ultralight.UltralightRenderer;
import com.labymedia.ultralight.config.FontHinting;
import com.labymedia.ultralight.config.UltralightConfig;

import java.io.IOException;
import java.net.URISyntaxException;

public class Ultralight {

    private static UltralightRenderer renderer;
    public static void init() {
        try {
            ResourceManager.loadUltralight();
        } catch (URISyntaxException | UltralightLoadException | IOException e) {
            throw new RuntimeException(e);
        }

        UltralightPlatform platform = UltralightPlatform.instance();
        platform.setConfig(new UltralightConfig()
                .fontHinting(FontHinting.SMOOTH)
        );
        platform.usePlatformFontLoader();
        platform.usePlatformFileSystem(ResourceManager.ultralightDir.getAbsolutePath());
        renderer = UltralightRenderer.create();
    }

    public static UltralightRenderer getRenderer() {
        return renderer;
    }
}
