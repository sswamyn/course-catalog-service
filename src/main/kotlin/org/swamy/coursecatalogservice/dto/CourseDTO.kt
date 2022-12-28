package org.swamy.coursecatalogservice.dto

import jakarta.validation.constraints.NotBlank

data class CourseDTO(
    val id: Int?, //Defined as a Nullable since this will be DB generated
    @get: NotBlank(message = "courseDTO.name must not be blank")
    val name: String,
    @get: NotBlank(message = "courseDTO.category must not be blank")
    val category : String

    // Annotations in Kotlin is a bit more tricky!!
    //Annotation use-site targets - https://kotlinlang.org/docs/annotations.html#annotation-use-site-targets

)