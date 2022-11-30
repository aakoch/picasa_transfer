package com.adamkoch.picasaxfer

class PersonInfo(val name: String, val email: String?, val something: String?) {
    override fun toString(): String {
        return "$name, $email, $something"
    }
}
