package org.swamy.coursecatalogservice.controller

import mu.KLogging
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*
import org.swamy.coursecatalogservice.dto.CourseDTO
import org.swamy.coursecatalogservice.service.CourseService

@RestController
@RequestMapping("/v1/courses")
class CourseController(val courseService: CourseService) {

    companion object : KLogging()

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun addCourse(@RequestBody courseDTO: CourseDTO): CourseDTO {
        logger.info("In the controller, about to call Service.addCourse()")
        return courseService.addCourse(courseDTO)
    }

    @GetMapping
    fun retrieveAllCourses(): List<CourseDTO> = courseService.retrieveAllCourses()

    //update based on courseId
    @PutMapping("/{course_id}")
    fun updateCourse(
        @RequestBody courseDTO: CourseDTO,
        @PathVariable("course_id") courseId: Int
    ) = courseService.updateCourse(courseId, courseDTO)
}