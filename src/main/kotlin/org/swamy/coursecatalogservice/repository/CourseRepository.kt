package org.swamy.coursecatalogservice.repository

import org.springframework.data.repository.CrudRepository
import org.swamy.coursecatalogservice.entity.Course

interface CourseRepository: CrudRepository<Course, Int> {
}