package org.swamy.coursecatalogservice.controller

import mu.KLogging
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController
import org.swamy.coursecatalogservice.dto.CourseDTO
import org.swamy.coursecatalogservice.service.CourseService

@RestController
@RequestMapping("/v1/courses")
class CourseController(val courseService: CourseService) {

    companion object : KLogging()

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun addCourse(@RequestBody courseDTO: CourseDTO) : CourseDTO {
    logger.info("In the controller, about to call Service.addCourse()")
        return courseService.addCourse(courseDTO)
    }
}