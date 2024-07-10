package cn.edu.buaa.patpat.boot.modules.course.controllers;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/student")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Student", description = "Student management API")
public class StudentController {

}
