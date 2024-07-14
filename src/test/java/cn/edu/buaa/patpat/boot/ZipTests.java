package cn.edu.buaa.patpat.boot;

import cn.edu.buaa.patpat.boot.common.utils.Medias;
import cn.edu.buaa.patpat.boot.common.utils.Zips;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.io.IOException;
import java.nio.file.Path;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest
@ActiveProfiles("dev")
public class ZipTests {
    private static final Path TEMP_PATH = Path.of("temp");
    private static final Path SRC_PATH = Path.of("temp", "src");
    private static final Path ZIP_PATH = Path.of("temp", "test.zip");
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
    public void zipWithoutParentTest() throws IOException {
        Zips.zip(SRC_PATH, ZIP_PATH, false);
        Zips.unzip(ZIP_PATH, DEST_PATH);

        assertThat(Medias.exists(Path.of(DEST_PATH.toString(), "a.txt"))).isTrue();
        assertThat(Medias.exists(Path.of(DEST_PATH.toString(), "b.txt"))).isTrue();
    }

    @Test
    public void zipWithParentTest() throws IOException {
        Zips.zip(SRC_PATH, ZIP_PATH, true);
        Zips.unzip(ZIP_PATH, DEST_PATH);

        assertThat(Medias.exists(Path.of(DEST_PATH.toString(), "src", "a.txt"))).isTrue();
        assertThat(Medias.exists(Path.of(DEST_PATH.toString(), "src", "b.txt"))).isTrue();
    }
}
