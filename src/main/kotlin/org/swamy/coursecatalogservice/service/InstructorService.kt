package org.swamy.coursecatalogservice.service

import org.springframework.stereotype.Service
import org.swamy.coursecatalogservice.dto.InstructorDTO
import org.swamy.coursecatalogservice.entity.Instructor
import org.swamy.coursecatalogservice.repository.InstructorRepository
import java.util.*

@Service
class InstructorService(val instructorRepository: InstructorRepository) {
    fun createInstructor(instructorDTO: InstructorDTO): InstructorDTO {

        val instructorEntity = instructorDTO.let {
            Instructor(it.id, it.name)
        }

        instructorRepository.save(instructorEntity)

        return instructorEntity.let {
            InstructorDTO(it.id, it.name)
        }
    }

    fun findByInstructorId(instructorId: Int): Optional<Instructor> {

        return instructorRepository.findById(instructorId)

    }


}
