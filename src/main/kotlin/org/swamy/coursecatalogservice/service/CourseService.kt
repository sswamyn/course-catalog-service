package org.swamy.coursecatalogservice.service

import mu.KLogging
import org.springframework.stereotype.Service
import org.swamy.coursecatalogservice.dto.CourseDTO
import org.swamy.coursecatalogservice.entity.Course
import org.swamy.coursecatalogservice.exception.CourseNotFoundException
import org.swamy.coursecatalogservice.exception.InstructorNotValidException
import org.swamy.coursecatalogservice.repository.CourseRepository

// THis class will hold the business logic, including validation
@Service
class CourseService(
    val courseRepository: CourseRepository,
    val instructorService: InstructorService // this will take care of the validation
) {

    companion object : KLogging()

    fun addCourse(courseDTO: CourseDTO): CourseDTO {
        logger.info("In CourseService.addCourse() with request param : $courseRepository")

        // Confirm that the instructorId provided is valid
        val instructorOptional = instructorService.findByInstructorId(courseDTO.instructorId!!)
        // If the instructor is not in the database
        if(!instructorOptional.isPresent) {
            throw InstructorNotValidException("Instructor not valid : ${courseDTO.instructorId}")
        }

        // we need to convert CourseDTO to an CourseEntity
        val courseEntity = courseDTO.let {
            Course(null, it.name, it.category, instructorOptional.get())
        }

        courseRepository.save(courseEntity)
        logger.info("Saved course is: \t $courseEntity")

        // convert the courseEntity object we get back from the Repository.save() back to DTO
        return courseEntity.let {
            CourseDTO(it.id, it.name, it.category, it.instructor?.id)
        }
    }

    fun retrieveAllCourses(courseName: String): List<CourseDTO> {

        val courses = courseName?.let {
            courseRepository.findCoursesByNameCustom(courseName)
        } ?: courseRepository.findAll()

        return courses
            .map {
                CourseDTO(it.id, it.name, it.category,it.instructor?.id)
            }
    }

    fun updateCourse(courseId: Int, courseDTO: CourseDTO): CourseDTO {
        // First let us check if the object with the given ID exsists
        val existingCourse = courseRepository.findById(courseId)

        // Since 'if' is an expression in Kotlin, we can assign the return value [last statement] as the functions return
        return if (existingCourse.isPresent) {
            existingCourse.get()
                .let {
                    it.name = courseDTO.name
                    it.category = courseDTO.category
                    courseRepository.save(it)
                    CourseDTO(it.id, it.name, it.category, it.instructor?.id)
                }

        } else {
            throw CourseNotFoundException("No course found for Id : $courseId; No Update performed")
        }

    }

    fun deleteCourse(courseId: Int) {
        // Similar to update function above
        val existingCourse = courseRepository.findById(courseId)

        return if (existingCourse.isPresent) {
            existingCourse.get()
                .let {
                    courseRepository.deleteById(courseId)
                }

        } else {
            throw CourseNotFoundException("No course found for Id : $courseId; Nothing deleted")
        }

    }
}