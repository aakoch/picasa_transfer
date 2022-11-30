package com.adamkoch.picasaxfer

import org.apache.commons.configuration2.INIConfiguration
import org.apache.commons.configuration2.builder.fluent.Configurations
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger
import java.io.File


class Main {

    private val logger: Logger = LogManager.getLogger(javaClass)

    fun readConfig(filename: String): INIConfiguration {
        val configs = Configurations()
        return configs.ini(File(filename))
    }

    fun run(filename: String) {
        val iniConfig = readConfig(filename)
        val internalConfig: InternalConfig = convertIniToInternalConfig(iniConfig)
        println(internalConfig)
    }

    private fun convertIniToInternalConfig(config: INIConfiguration): InternalConfig {
        val internalConfig = InternalConfig()
        config.sections.forEach { section ->
            logger.debug("section = $section")

            if (isFileConfig(section)) {
                val filename = section
                val fileConfig = FileConfig(filename)
                val subnodeConfiguration = config.getSection(section)
                subnodeConfiguration.keys.forEach {
                    logger.debug("property name = $it")
                    val propVal = subnodeConfiguration.getString(it)
                    logger.debug("property value = $propVal")

                    if (it.equals("backuphash")) {
                    } else if (it.equals("star")) {
                        fileConfig.rating = propVal
                    } else if (it.equals("faces")) {
                        val (faceHash, rect) = splitFaceAndRect(propVal)
                        fileConfig.addFace(faceHash, rect)
                    }
               }

                fileConfig.takeUnless(FileConfig::isEmpty)?.let(internalConfig::addFileConfig)
            }
            else if (isFaceToPersonMapping(section)) {
                val subnodeConfiguration = config.getSection(section)
                subnodeConfiguration.keys.forEach { hash: String ->
                    logger.debug("hash = $hash")
                    val personInfoString = subnodeConfiguration.getString(hash)
                    logger.debug("personInfoString = $personInfoString")

                    val personInfo = extractPersonInfo(personInfoString)
                    internalConfig.addFaceMapping(FaceMapping(hash, personInfo))
                }
            }
        }

        return internalConfig
    }

    private fun extractPersonInfo(personInfoString: String): PersonInfo {
        val splitVals = personInfoString.split(";")
        return PersonInfo(splitVals.get(0), splitVals.get(1), splitVals.get(2))
    }

    private fun isFileConfig(section: String): Boolean {
        return !isFaceToPersonMapping(section)
    }

    private fun isFaceToPersonMapping(section: String): Boolean {
        return section == "Contacts2"
    }

    private fun splitFaceAndRect(propVal: String): Array<String> {
        val splitVals = propVal.split(",")
        return arrayOf(splitVals[1], splitVals[0])
    }
}

fun main(args: Array<String>) {
    val main = Main()
    val filename = args.getOrElse<String>(0) { "src/test/resources/.picasa.ini" }
    main.run(filename)
}
