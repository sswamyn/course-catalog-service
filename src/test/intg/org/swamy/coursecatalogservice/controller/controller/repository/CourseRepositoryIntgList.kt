package org.swamy.coursecatalogservice.controller.controller.repository

import mu.KLogger
import mu.KLogging
import org.aspectj.lang.annotation.Before
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.test.context.ActiveProfiles
import org.swamy.coursecatalogservice.repository.CourseRepository
import util.courseEntityList
import java.util.stream.Stream
import kotlin.math.exp

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@DataJpaTest
@ActiveProfiles("test")
class CourseRepositoryIntgList {

    //companion object : KLogging()
    // Commented out since ONLY one companion object is allowed in a class!
    @Autowired
    lateinit var courseRepository: CourseRepository

    @BeforeEach
    fun setup() {
        courseRepository.deleteAll()
        val courses = courseEntityList()
        courseRepository.saveAll(courses)
    }

    @Test
    fun findByNameContaining() {

        val springCourses = courseRepository.findByNameContaining("SpringBoot")

        //logger.info(springCourses.toString())
        Assertions.assertEquals(2, springCourses.size)
    }

    // Test the native SQL query function - findCoursesByNameCustom
    @Test
    fun findCoursesByNameCustom() {

        //logger.info(" STARTING test for findCoursesByNameCustom() ")
        val springCourses = courseRepository.findCoursesByNameCustom("SpringBoot")

        //logger.info(springCourses.toString())
        Assertions.assertEquals(2, springCourses.size)
    }

    // Parameterized Test case
    @ParameterizedTest
    @MethodSource("courseAndSize") // a method to feed dynamic test paramenters to this test case
    fun findCoursesByNameCustom_approach2(name: String, expectedSize : Int) {

        //logger.info(" STARTING test for findCoursesByNameCustom() ")
        val springCourses = courseRepository.findCoursesByNameCustom(name)

        //logger.info(springCourses.toString())
        Assertions.assertEquals(expectedSize, springCourses.size)


    }
    companion object {
        @JvmStatic
        fun courseAndSize(): Stream<Arguments> {
            return Stream.of(
                Arguments.arguments("SpringBoot", 2),
                Arguments.arguments("Kafka", 0)
            )
        }
    }

}