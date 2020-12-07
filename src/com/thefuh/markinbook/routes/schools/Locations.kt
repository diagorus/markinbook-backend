package com.thefuh.markinbook.routes.schools

import com.thefuh.markinbook.API_NAME
import com.thefuh.markinbook.API_VERSION
import io.ktor.locations.*

@KtorExperimentalLocationsAPI
@Location("$API_NAME/$API_VERSION/schools")
class SchoolsLocation {

    @Location("/add")
    class Add(val school: SchoolsLocation) {
        companion object {
            const val TITLE = "title"
            const val LONGITUDE = "longitude"
            const val LATITUDE = "latitude"
        }
    }

    @Location("/{schoolId}")
    class School(val schools: SchoolsLocation, val schoolId: Int) {

        @Location("/disciplines")
        class Disciplines(val school: School) {

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
        class Groups(val school: School) {

            @Location("/add")
            class Add(val groups: Groups) {
                companion object {
                    const val ARG_TITLE = "title"
                }
            }

            @Location("/{groupId}")
            class Group(val groups: Groups, val groupId: Int)
        }
    }
}

@KtorExperimentalLocationsAPI
@Location("$API_NAME/$API_VERSION/students")
class StudentsLocation {

    @Location("/{studentId}")
    class Student(val students: StudentsLocation, val studentId: Int)

    @Location("/current")
    class Current(val students: StudentsLocation) {

        @Location("/add-profile-image")
        class AddProfileImage(val current: Current)
    }
}

@KtorExperimentalLocationsAPI
@Location("$API_NAME/$API_VERSION/teachers")
class TeachersLocation {

    @Location("/current")
    class Current(val students: TeachersLocation) {

        @Location("/add-profile-image")
        class AddProfileImage(val current: Current)
    }
}

@KtorExperimentalLocationsAPI
@Location("$API_NAME/$API_VERSION/lessons")
class LessonsLocation {

    @Location("/add")
    class Add(val lessons: LessonsLocation) {
        companion object {
            const val ARG_GROUP_ID = "groupId"
            const val ARG_DISCIPLINE_ID = "disciplineId"
            const val ARG_START = "start"
            const val ARG_DURATION_IN_MINUTES = "durationInMinutes"
        }
    }

    @Location("/{lessonId}")
    class Lesson(val lessons: LessonsLocation, val lessonId: Int) {

        @Location("/marks")
        data class Marks(val lesson: Lesson) {

            @Location("/add")
            data class Add(val marks: Marks) {
                companion object {
                    const val ARG_STUDENT_ID = "studentId"
                    const val ARG_VALUE = "value"
                }
            }
        }
    }

    @Location("/by-days")
    class ByDays(val lessons: LessonsLocation, val week: Int, val year: Int)
}

@KtorExperimentalLocationsAPI
@Location("$API_NAME/$API_VERSION/homeworks")
class HomeworksLocation {

//    @Location("/add")
//    data class Add(val homeworks: Homeworks) {
//    }

    @Location("/{homeworkId}")
    data class Homework(val homeworks: HomeworksLocation, val homeworkId: Int) {

        @Location("/marks")
        data class Marks(val homework: Homework) {

            @Location("/add")
            data class Add(val marks: Marks) {
                companion object {
                    const val ARG_STUDENT_ID = "studentId"
                    const val ARG_VALUE = "value"
                }
            }
        }
    }
}