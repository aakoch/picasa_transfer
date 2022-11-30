package com.adamkoch.picasaxfer

import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger
import java.util.*
import kotlin.collections.ArrayList

class InternalConfig {

    private val logger: Logger = LogManager.getLogger(javaClass)

    private val faceMappings: ArrayList<FaceMapping> = ArrayList()
    private val fileConfigs: ArrayList<FileConfig> = ArrayList()

    fun addFileConfig(fileConfig: FileConfig) {
        fileConfigs.add(fileConfig)
    }

    override fun toString(): String {
        var facesString = Arrays.toString(fileConfigs.toArray())
        faceMappings.forEach { faceMapping: FaceMapping ->
            logger.debug("faceConfig = ${faceMapping}")
            facesString = facesString.replace(faceMapping.hash, faceMapping.personInfo.name)
        }
        return "People: ${Arrays.toString(faceMappings.toArray())}\nFaces: $facesString"
    }

    fun addFaceMapping(faceMapping: FaceMapping) {
        faceMappings.add(faceMapping)
    }
}
