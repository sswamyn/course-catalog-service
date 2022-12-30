package org.swamy.coursecatalogservice.controller

import com.ninjasquad.springmockk.MockkBean
import io.mockk.every
import io.mockk.just
import io.mockk.runs
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.test.util.AssertionErrors.assertEquals
import org.springframework.test.web.reactive.server.WebTestClient
import org.springframework.test.web.reactive.server.expectBody
import org.swamy.coursecatalogservice.dto.CourseDTO
import org.swamy.coursecatalogservice.entity.Course
import org.swamy.coursecatalogservice.service.CourseService
import util.courseDTO

@WebMvcTest(controllers = [CourseController::class])
@AutoConfigureWebTestClient
class CourseControllerUnitTest {
    @Autowired
    lateinit var webTestClient: WebTestClient

    //Since this is a Unit test, to validate the controller alone. We need to 'mock' the service layer
    @MockkBean
    lateinit var courseServiceMock: CourseService

    // Test addCourse() function
    @Test
    fun addCourse() {

        val courseDTO = CourseDTO(null, "Build Restful APIs using SpringBoot and Kotlin", "Swamy")

        every { courseServiceMock.addCourse(any()) } returns courseDTO(id = 1)

        val savedCourseDTO = webTestClient
            .post()
            .uri("/v1/courses")
            .bodyValue(courseDTO)
            .exchange()
            .expectStatus().isCreated
            .expectBody(CourseDTO::class.java)
            .returnResult()
            .responseBody

        Assertions.assertTrue {
            savedCourseDTO!!.id != null
        }
    }

    // Test the validators in addCourse() function
    @Test
    fun addCourse_validation() {

        val courseDTO = CourseDTO(null, "", "")

        every { courseServiceMock.addCourse(any()) } returns courseDTO(id = 1)

        val result = webTestClient
            .post()
            .uri("/v1/courses")
            .bodyValue(courseDTO)
            .exchange()
            .expectStatus().isBadRequest
            .expectBody(String::class.java)
            .returnResult()
            .responseBody

        assertEquals("don't know!! ","[\"courseDTO.category must not be blank\",\"courseDTO.name must not be blank\"]", result)//("\n Errors: \t courseDTO.category must not be blank, courseDTO.name must not be blank", result)

    }

    // Test the Global custom error handler exceptionhandler.GlobalErrorHandler()
    @Test
    fun addCourse_runtimeexception() {

        val courseDTO = CourseDTO(null, "Build Restful APIs using SpringBoot and Kotlin", "Swamy")

        val errorMessage = "Internal app error - Exception"
        every { courseServiceMock.addCourse(any()) } throws RuntimeException(errorMessage)

        val result = webTestClient
            .post()
            .uri("/v1/courses")
            .bodyValue(courseDTO)
            .exchange()
            .expectStatus().is5xxServerError
            .expectBody(String::class.java)
            .returnResult()
            .responseBody

        assertEquals("don't know!! ",errorMessage, result)

    }


    @Test
    fun retriveAllCourses() {

        every { courseServiceMock.retrieveAllCourses(any()) }.returnsMany(
            listOf(
                courseDTO(id = 1),
                courseDTO(id = 2, name = "Build Restful APIs using SpringBoot and Kotlin")
            )
        )

        val courseDTOs = webTestClient
            .get()
            .uri("/v1/courses")
            .exchange()
            .expectStatus().isOk
            .expectBodyList(CourseDTO::class.java)
            .returnResult()
            .responseBody
        Assertions.assertEquals(2, courseDTOs!!.size)

    }

    @Test
    fun updateCourse() {
        // We need the following:
        // 1. existing Course to update ;
        val course = Course(
            null,
            "Build RestFul APis using SpringBoot and Kotlin", "Development"
        )
        every { courseServiceMock.updateCourse(any(), any()) } returns CourseDTO(     100,
            "Build RestFul APis using SpringBoot and Kotlin-1.7", "Development")

        val updatedCourseDTO = CourseDTO(
            null,
            "Build RestFul APis using SpringBoot and Kotlin-1.7", "Development"
        )

        val updatedCourse = webTestClient
            .put()
            .uri("/v1/courses/{courseId}", 100)
            .bodyValue(updatedCourseDTO)
            .exchange()
            .expectStatus().isOk
            .expectBody(CourseDTO::class.java)
            .returnResult()
            .responseBody

        Assertions.assertEquals("Build RestFul APis using SpringBoot and Kotlin-1.7", updatedCourse!!.name)
    }

    @Test
    fun deleteCourse() {

        every { courseServiceMock.deleteCourse(any()) } just runs

        webTestClient
            .delete()
            .uri("/v1/courses/{courseId}", 100)
            .exchange()
            .expectStatus().isNoContent

    }


}