package cn.edu.buaa.patpat.boot;

import cn.edu.buaa.patpat.boot.common.utils.Mappers;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest
@ActiveProfiles("dev")
public class MapperTests {
    @Autowired
    private Mappers mappers;

    @Test
    public void jsonMapperTest() throws JsonProcessingException {
        String json = "{\"name\":\"John\",\"age\":30,\"car\":null}";

        Object object = mappers.fromJson(json);
        String json2 = mappers.toJson(object);

        assertThat(json).isEqualTo(json2);
    }
}
