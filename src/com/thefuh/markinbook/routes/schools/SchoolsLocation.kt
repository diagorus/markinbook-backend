package com.thefuh.markinbook.routes.schools

import com.thefuh.markinbook.API_NAME
import com.thefuh.markinbook.API_VERSION
import io.ktor.locations.*

@KtorExperimentalLocationsAPI
@Location("$API_NAME/$API_VERSION/schools")
class SchoolsLocation {

    @Location("/add")
    data class Add(val school: SchoolsLocation) {
        companion object {
            const val TITLE = "title"
            const val LONGITUDE = "longitude"
            const val LATITUDE = "latitude"
        }
    }

    @Location("/{schoolId}")
    data class School(val schools: SchoolsLocation, val schoolId: Int) {

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

        @Location("/groups")
        data class Groups(val school: School) {

            @Location("/add")
            data class Add(val groups: Groups) {
                companion object {
                    const val ARG_TITLE = "title"
                }
            }
        }
    }

    @Location("/students")
    data class Students(val school: SchoolsLocation) {

        @Location("/current")
        data class Current(val students: Students)

        @Location("/{studentId}")
        data class Student(val students: Students, val studentId: Int) {

            @Location("/lessons")
            data class Lessons(val student: Student) {

                @Location("/add")
                data class Add(val lessons: Lessons) {
                    companion object {
                        const val ARG_DISCIPLINE_ID = "disciplineId"
                        const val ARG_GROUP_ID = "groupId"
                        const val ARG_START = "start"
                        const val ARG_DURATION_IN_MINUTES = "durationInMinutes"
                    }
                }

                @Location("/{lessonId}")
                data class Lesson(val lessons: Lessons, val lessonId: Int) {

                    @Location("/homeworks")
                    data class Homeworks(val lesson: Lesson) {

                        //todo?
                        @Location("/add")
                        data class Add(val homeworks: Homeworks) {
                            companion object {

                            }
                        }
                    }
                }

                @Location("/by-days")
                data class ByDays(val lessons: Lessons, val week: Int, val year: Int)
            }
        }
    }
}