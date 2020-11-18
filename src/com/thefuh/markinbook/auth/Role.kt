package com.thefuh.markinbook.auth;

enum class Role(val title: String) {
    TEACHER("teacher"),
    STUDENT("student"),
    PARENT("parent");

    companion object {
        fun findByTitle(title: String): Role? {
            return values().find { it.title == title }
        }
    }
}