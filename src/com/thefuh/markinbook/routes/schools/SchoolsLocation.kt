package com.thefuh.markinbook.routes.schools

import com.thefuh.markinbook.API_VERSION
import io.ktor.locations.*

@KtorExperimentalLocationsAPI
@Location("$API_VERSION/schools")
class SchoolsLocation {

    @Location("/add")
    data class Add(val school: SchoolsLocation) {
        companion object {
            const val TITLE = "title"
        }
    }

    @Location("/{schoolId}")
    data class School(val schools: SchoolsLocation, val schoolId: Int) {

        @KtorExperimentalLocationsAPI
        @Location("/disciplines")
        data class Disciplines(val school: School) {

            @Location("/add")
            data class Add(val disciplines: Disciplines) {
                companion object {
                    const val TITLE = "title"
                }
            }

            @Location("/{disciplineId}")
            data class Discipline(val disciplines: Disciplines, val disciplineId: Int)
        }
    }

    @KtorExperimentalLocationsAPI
    @Location("/students")
    data class Students(val school: School) {

        @Location("/add")
        data class Add(val students: Students) {
            companion object {
                const val ARG_FIRST_NAME = "firstName"
                const val ARG_LAST_NAME = "lastName"
                const val ARG_MIDDLE_NAME = "middleName"
            }
        }

        @Location("/{studentId}")
        data class Student(val students: Students, val studentId: Int) {

            @KtorExperimentalLocationsAPI
            @Location("/classes")
            data class Lessons(val student: Student) {

                @Location("/add")
                data class Add(val lessons: Lessons)
            }
        }
    }
}