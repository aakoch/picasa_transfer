package com.adamkoch.picasaxfer

class FaceMapping(val hash: String, val personInfo: PersonInfo) {

    override fun toString(): String {
        return "$hash: $personInfo"
    }
}
