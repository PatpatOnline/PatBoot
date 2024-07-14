package cn.edu.buaa.patpat.boot;

import cn.edu.buaa.patpat.boot.common.utils.Medias;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@ActiveProfiles("dev")
public class FilesTests {
    private static final Path TEMP_PATH = Path.of("temp");
    private static final Path SRC_PATH = Path.of("temp", "src");
    private static final Path DEST_PATH = Path.of("temp", "dest");

    @AfterAll
    public static void tearDown() throws IOException {
        Medias.remove(TEMP_PATH);
    }

    @BeforeEach
    public void setUp() throws IOException {
        Medias.ensureEmptyPath(TEMP_PATH);
        Medias.ensurePath(SRC_PATH);
        Medias.save(Path.of(SRC_PATH.toString(), "a.txt"), "a");
        Medias.save(Path.of(SRC_PATH.toString(), "b.txt"), "b");
    }

    @Test
    public void moveWithoutCleanTest() throws IOException {
        Files.move(SRC_PATH, DEST_PATH, StandardCopyOption.REPLACE_EXISTING);
        Medias.ensurePath(SRC_PATH);
        Medias.save(Path.of(SRC_PATH.toString(), "a.txt"), "a");
        assertThrows(IOException.class, () -> Files.move(SRC_PATH, DEST_PATH, StandardCopyOption.REPLACE_EXISTING));
    }

    @Test
    public void moveWithCleanTest() throws IOException {
        Files.move(SRC_PATH, DEST_PATH, StandardCopyOption.REPLACE_EXISTING);
        Medias.ensurePath(SRC_PATH);
        Medias.removeSilently(DEST_PATH);
        Medias.save(Path.of(SRC_PATH.toString(), "a.txt"), "a");
        assertThat(Medias.exists(Path.of(DEST_PATH.toString(), "b.txt"))).isFalse();
    }
}
