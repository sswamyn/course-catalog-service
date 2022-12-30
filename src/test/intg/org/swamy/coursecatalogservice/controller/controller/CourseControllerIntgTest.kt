package org.swamy.coursecatalogservice.controller.controller

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.DynamicPropertyRegistry
import org.springframework.test.context.DynamicPropertySource
import org.springframework.test.web.reactive.server.WebTestClient
import org.springframework.web.util.UriComponentsBuilder
import org.swamy.coursecatalogservice.dto.CourseDTO
import org.swamy.coursecatalogservice.entity.Course
import org.swamy.coursecatalogservice.repository.CourseRepository
import org.swamy.coursecatalogservice.repository.InstructorRepository
import org.testcontainers.containers.PostgreSQLContainer
import org.testcontainers.junit.jupiter.Container
import org.testcontainers.junit.jupiter.Testcontainers
import org.testcontainers.utility.DockerImageName
import util.PostgreSQLContainerInitializer
import util.courseEntityList
import util.instructorEntity

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace= AutoConfigureTestDatabase.Replace.NONE)
@Testcontainers
class CourseControllerIntgTest : PostgreSQLContainerInitializer() {

    @Autowired
    lateinit var webTestClient: WebTestClient

    @Autowired
    lateinit var courseRepository: CourseRepository

    @Autowired
    lateinit var instructorRepository: InstructorRepository

    companion object {

        @Container
        val postgresDB = PostgreSQLContainer<Nothing>(DockerImageName.parse("postgres:13-alpine")).apply {
            withDatabaseName("testdb")
            withUsername("postgres")
            withPassword("secret")
        }

        @JvmStatic
        @DynamicPropertySource // this will override the properties provided in the application.yml
        fun properties(registry: DynamicPropertyRegistry) {
            registry.add("spring.datasource.url", postgresDB::getJdbcUrl)
            registry.add("spring.datasource.username", postgresDB::getUsername)
            registry.add("spring.datasource.password", postgresDB::getPassword)
        }
    }

    @BeforeEach
    fun setup() {
        courseRepository.deleteAll()
        instructorRepository.deleteAll()

        val instructor = instructorEntity()
        instructorRepository.save(instructor)

        val courses = courseEntityList()
        courseRepository.saveAll(courses)

    }

    @Test
    fun addCourse() {

        val instructor = instructorRepository.findAll().first()
        val courseDTO = CourseDTO(null, "Build Restful APIs using SpringBoot and Kotlin", "Swamy",
            instructor!!.id)

        println("Instructor : \t ${instructor.toString()}")
        println("Course : \t ${courseDTO.toString()}")

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

 /*   @Test
    fun addInstructor() {

        val instructor = instructorRepository.findInstructorByName("Dilip Sundarraj")

        //given
        val courseDTO = CourseDTO(null,
            "Build RestFul APis using Spring Boot and Kotlin", "Dilip Sundarraj",
            instructor.id )

        //when
        val savedCourseDTO = webTestClient
            .post()
            .uri("/v1/courses")
            .bodyValue(courseDTO)
            .exchange()
            .expectStatus().isCreated
            .expectBody(CourseDTO::class.java)
            .returnResult()
            .responseBody

        //then
        assertTrue {
            savedCourseDTO!!.id!=null
        }
    }*/


    @Test
    fun addCourse_InvlaidOInstructorId() {

        //given
        val courseDTO = CourseDTO(null,
            "Build RestFul APis using Spring Boot and Kotlin", "Dilip Sundarraj",
            999 )

        //when
        val response = webTestClient
            .post()
            .uri("/v1/courses")
            .bodyValue(courseDTO)
            .exchange()
            .expectStatus().isBadRequest
            .expectBody(String::class.java)
            .returnResult()
            .responseBody

        //then
        assertEquals("Instructor Id is not Valid!", response)
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

        val instructor = instructorRepository.findAll().first()

        // We need the following:
        // 1. existing Course to update ;
        val course = Course(
            null,
            "Build RestFul APis using SpringBoot and Kotlin", "Development", instructor
        )
        courseRepository.save(course)

        //  2. courseId ; and 3. Updated courseDTO
        val updatedCourseDTO = CourseDTO(
            null,
            "Build RestFul APis using SpringBoot and Kotlin-1.7", "Development",
            course.instructor!!.id )

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

        val instructor = instructorRepository.findAll().first()

        // We need the following:
        // 1. existing Course to update ;
        val course = Course(
            null,
            "Build RestFul APis using SpringBoot and Kotlin", "Development", instructor
        )
        courseRepository.save(course)

        webTestClient
            .delete()
            .uri("/v1/courses/{courseId}", course.id)
            .exchange()
            .expectStatus().isNoContent

    }
}