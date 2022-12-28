package org.swamy.coursecatalogservice.controller

import jakarta.validation.Valid
import mu.KLogging
import org.springframework.http.HttpStatus
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*
import org.swamy.coursecatalogservice.dto.CourseDTO
import org.swamy.coursecatalogservice.service.CourseService

@RestController
@RequestMapping("/v1/courses")
@Validated
class CourseController(val courseService: CourseService) {

    companion object : KLogging()

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun addCourse(@RequestBody @Valid courseDTO: CourseDTO): CourseDTO {
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

    //Delete course
    @DeleteMapping("/{course_id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun deleteCourse(@PathVariable("course_id") courseId : Int)
    = courseService.deleteCourse(courseId)

}