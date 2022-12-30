package org.swamy.coursecatalogservice.repository

import org.springframework.data.repository.CrudRepository
import org.swamy.coursecatalogservice.entity.Instructor

interface InstructorRepository : CrudRepository<Instructor, Int>