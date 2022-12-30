package org.swamy.coursecatalogservice.controller.controller

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.web.reactive.server.WebTestClient
import org.springframework.web.util.UriComponentsBuilder
import org.swamy.coursecatalogservice.dto.CourseDTO
import org.swamy.coursecatalogservice.entity.Course
import org.swamy.coursecatalogservice.repository.CourseRepository
import util.courseEntityList

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@AutoConfigureWebTestClient
class CourseControllerIntgTest {

    @Autowired
    lateinit var webTestClient: WebTestClient

    @Autowired
    lateinit var courseRepository: CourseRepository

    @BeforeEach
    fun setup() {
        courseRepository.deleteAll()
        val courses = courseEntityList()
        courseRepository.saveAll(courses)

    }

    @Test
    fun addCourse() {

        val courseDTO = CourseDTO(null, "Build Restful APIs using SpringBoot and Kotlin", "Swamy")

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

    @Test
    fun retriveAllCourses() {
        val courseDTOs = webTestClient
            .get()
            .uri("/v1/courses")
            .exchange()
            .expectStatus().isOk
            .expectBodyList(CourseDTO::class.java)
            .returnResult()
            .responseBody
        assertEquals(3, courseDTOs!!.size)

    }

    @Test
    fun retriveAllCourses_ByName() {
        val uri = UriComponentsBuilder.fromUriString("/v1/courses")
            .queryParam("course_name", "Kotlin")
            .toUriString()

        val courseDTOs = webTestClient
            .get()
            .uri(uri)
            .exchange()
            .expectStatus().isOk
            .expectBodyList(CourseDTO::class.java)
            .returnResult()
            .responseBody
        assertEquals(1, courseDTOs!!.size)

    }

    @Test
    fun updateCourse() {
        // We need the following:
        // 1. existing Course to update ;
        val course = Course(
            null,
            "Build RestFul APis using SpringBoot and Kotlin", "Development"
        )
        courseRepository.save(course)

        //  2. courseId ; and 3. Updated courseDTO
        val updatedCourseDTO = CourseDTO(
            null,
            "Build RestFul APis using SpringBoot and Kotlin-1.7", "Development"
        )

        val updatedCourse = webTestClient
            .put()
            .uri("/v1/courses/{courseId}", course.id)
            .bodyValue(updatedCourseDTO)
            .exchange()
            .expectStatus().isOk
            .expectBody(CourseDTO::class.java)
            .returnResult()
            .responseBody

        assertEquals("Build RestFul APis using SpringBoot and Kotlin-1.7", updatedCourse!!.name)
    }

    @Test
    fun deleteCourse() {
        // We need the following:
        // 1. existing Course to update ;
        val course = Course(
            null,
            "Build RestFul APis using SpringBoot and Kotlin", "Development"
        )
        courseRepository.save(course)

        webTestClient
            .delete()
            .uri("/v1/courses/{courseId}", course.id)
            .exchange()
            .expectStatus().isNoContent

    }
}