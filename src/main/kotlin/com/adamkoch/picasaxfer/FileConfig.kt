package com.adamkoch.picasaxfer

class FileConfig(val filename: String) {
    var rating: String? = null
    private val faces = HashMap<String, String>()

    fun addFace(faceHash: String, rect: String) {
        faces.put(faceHash, rect)
    }

    override fun toString(): String {
        val r = if (rating == null) "" else " (⭐)"
        return filename + r + ": " + faces.map { it.key + ":" + it.value }.toString()
    }

    fun isEmpty(): Boolean = faces.isEmpty()
}
