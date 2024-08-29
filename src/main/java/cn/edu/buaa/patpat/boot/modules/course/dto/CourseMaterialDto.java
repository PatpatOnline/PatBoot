/*
 * Copyright (C) Patpat Online 2024
 * Made with love by Tony Skywalker
 */

package cn.edu.buaa.patpat.boot.modules.course.dto;

import cn.edu.buaa.patpat.boot.common.Globals;
import cn.edu.buaa.patpat.boot.common.models.HasCreatedAndUpdated;
import cn.edu.buaa.patpat.boot.modules.bucket.api.BucketApi;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class CourseMaterialDto extends HasCreatedAndUpdated {
    private int id;
    @JsonIgnore
    private int courseId;
    private String filename;
    private String comment;

    private String url;

    public void initUrl(BucketApi api) {
        String record = api.toRecord(Globals.COURSE_TAG, String.valueOf(courseId), filename);
        url = api.recordToUrl(record);
    }
}
