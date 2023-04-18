package dev.totaltax.graviton.util.ultralight;

import com.labymedia.ultralight.UltralightLoadException;
import dev.totaltax.graviton.Graviton;
import net.minecraft.client.Minecraft;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Collections;
import java.util.Objects;

public class ResourceManager {

    private static final File clientDir = new File(Minecraft.getInstance().gameDirectory, Graviton.getInstance().getName());
    private static final File ultralightDir = new File(clientDir, "ultralight");
    private static final File binDir = new File(ultralightDir, "bin");
    private static final File resourceDir = new File(ultralightDir, "resources");

    public static void loadUltralight() throws URISyntaxException, UltralightLoadException, IOException {
        String rootPath = "/assets/" + Graviton.getInstance().getName();

        URI uri = Objects.requireNonNull(ResourceManager.class.getResource(rootPath)).toURI();
        try (FileSystem fileSystem = FileSystems.newFileSystem(uri, Collections.emptyMap())) {
            copyFolder(fileSystem.getPath(rootPath + "/pubic"), new File(ultralightDir, "public").toPath());
            copyFolder(fileSystem.getPath(rootPath + "/bin"), binDir.toPath());
            copyFolder(fileSystem.getPath(rootPath + "/resources"), resourceDir.toPath());
        } catch (IOException e) {
            Graviton.getInstance().getLogger().fatal("Failed to load resouces {}", e.getMessage());
        }
    }

    private static void copyFolder(Path source, Path target, CopyOption... options)
            throws IOException {
        Files.walkFileTree(source, new SimpleFileVisitor<Path>() {

            @Override
            public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs)
                    throws IOException {
                Files.createDirectories(target.resolve(source.relativize(dir).toString()));
                return FileVisitResult.CONTINUE;
            }

            @Override
            public FileVisitResult visitFile(Path file, BasicFileAttributes attrs)
                    throws IOException {
                Files.copy(file, target.resolve(source.relativize(file).toString()), options);
                return FileVisitResult.CONTINUE;
            }
        });
    }
}
