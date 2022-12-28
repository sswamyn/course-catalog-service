package org.swamy.coursecatalogservice.controller

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController
import org.swamy.coursecatalogservice.dto.CourseDTO

@RestController
@RequestMapping("/v1/courses")
class CourseController {

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun addCourse(courseDTO: CourseDTO) {

    }
}