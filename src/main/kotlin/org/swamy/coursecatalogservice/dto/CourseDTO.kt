package org.swamy.coursecatalogservice.dto

data class CourseDTO(
    val id: Int?, //Defined as a Nullable since this will be DB generated
    val name: String,
    val category : String

)