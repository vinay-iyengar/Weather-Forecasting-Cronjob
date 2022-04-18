package com.example.cronjob

import com.example.cronjob.jobs.CronJobMailAlertJob
import io.dropwizard.Application
import io.dropwizard.setup.Environment
import java.io.File

class CronJobMailApplication : Application<CronJobMailConfig>() {

    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            val application = CronJobMailApplication()

            if (args.isNotEmpty()) {
                application.run(*args)
            } else {
                val configFile = File.createTempFile("config", ".yaml")
                configFile.writeText(
                    application.javaClass.getResourceAsStream("/cronjobConfig.yaml").bufferedReader()
                        .readText()
                )
                configFile.deleteOnExit()

                application.run("server", configFile.absolutePath)
            }
        }
    }

    override fun run(cronJobMailConfig: CronJobMailConfig, environment: Environment) {
        CronJobMailAlertJob.processJob(
            username = cronJobMailConfig.username,
            encryptedPassword = cronJobMailConfig.password,
        )
    }
}
