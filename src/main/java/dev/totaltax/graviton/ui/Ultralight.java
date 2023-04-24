package dev.totaltax.graviton.ui;

import com.labymedia.ultralight.UltralightLoadException;
import com.labymedia.ultralight.UltralightPlatform;
import com.labymedia.ultralight.UltralightRenderer;
import com.labymedia.ultralight.config.FontHinting;
import com.labymedia.ultralight.config.UltralightConfig;
import dev.totaltax.graviton.Graviton;
import dev.totaltax.graviton.util.FileUtils;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;

public class Ultralight {

    private static UltralightRenderer renderer;
    private static File ultralightdir = FileUtils.createPath(System.getProperty("user.dir"), Graviton.getInstance().getName(), "ultralight");
    public static void init() {

//        UltralightPlatform platform = UltralightPlatform.instance();
//        platform.setConfig(new UltralightConfig()
//                .fontHinting(FontHinting.SMOOTH)
//        );
//        platform.usePlatformFontLoader();
//        platform.usePlatformFileSystem(ultralightdir.getPath());
 //       renderer = UltralightRenderer.create();
    }

    public static UltralightRenderer getRenderer() {
        return renderer;
    }
}
