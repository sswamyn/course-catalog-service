package org.swamy.coursecatalogservice.repository

import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.CrudRepository
import org.swamy.coursecatalogservice.entity.Course

interface CourseRepository : CrudRepository<Course, Int> {

// https://docs.spring.io/spring-data/jpa/docs/current/reference/html/#jpa.query-methods.query-creation
    /*
    public interface UserRepository extends Repository<User, Long> {

      List<User> findByEmailAddressAndLastname(String emailAddress, String lastname);
    }
*/

    // Function name must conform to the syntax
    fun findByNameContaining(courseName : String) : List<Course>

    // If we have to use native SQL query; No restriction on function name
    @Query(value = " Select * FROM COURses where name like %?1%", nativeQuery = true)
    fun findCoursesByNameCustom(courseName : String) : List<Course>


}