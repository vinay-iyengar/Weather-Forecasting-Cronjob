package com.example.cronjob.jobs

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.text.SimpleDateFormat
import java.util.*
import javax.mail.Message
import javax.mail.MessagingException
import javax.mail.Session
import javax.mail.internet.InternetAddress
import javax.mail.internet.MimeMessage
import kotlin.system.exitProcess

object CronJobMailAlertJob {
    private val logger: Logger = LoggerFactory.getLogger(CronJobMailAlertJob::class.java)

    fun processJob(username: String, encryptedPassword: String) {
        val password= String(Base64.getDecoder().decode(encryptedPassword))
        try {
            logger.info("Job STARTED")

            val formatter = SimpleDateFormat("dd-MM-yyyy HH:mm:ss")
            formatter.timeZone = TimeZone.getTimeZone("Asia/Kolkata")
            val currentDateTime = formatter.format(Date())
            logger.info("Job Ran at {}", currentDateTime)
            sendMail(currentDateTime, username, password)

        } catch (e: Exception) {
            logger.error("Job Failed due to {}", e)
            sendMail("Job FAILED Due to $e", username, password)
            exitProcess(1)

        } finally {
            logger.info("Job Completed")
            exitProcess(0)
        }
    }

    private fun sendMail(currentDate: String?, username: String, password: String) {
        val emailTo = "vinay.s@innoventes.co"
        val subject = "Test Cron Job Mail Alert Triggered!!"
        val text = "CronJob Mail Alert Job Ran at $currentDate"
        val host = "smtp.gmail.com"

        val props = System.getProperties()
        props["mail.smtp.host"] = "smtp.gmail.com"
        props["mail.smtp.port"] = "587"
        props["mail.smtp.auth"] = "true"
        props["mail.smtp.starttls.enable"] = "true"
        props["mail.smtp.socketFactory.port"] = "465"
        props["mail.smtp.socketFactory.class"] = "javax.net.ssl.SSLSocketFactory"
        val session = Session.getDefaultInstance(props, null)

        try {
            val mimeMessage = MimeMessage(session)
            mimeMessage.setFrom(InternetAddress(username))
            mimeMessage.setRecipients(Message.RecipientType.TO, InternetAddress.parse(emailTo, false))
            mimeMessage.setText(text)
            mimeMessage.subject = subject
            mimeMessage.sentDate = Date()

            val smtpTransport = session.getTransport("smtp")
            smtpTransport.connect(host, username, password)
            smtpTransport.sendMessage(mimeMessage, mimeMessage.allRecipients)
            smtpTransport.close()

            logger.info("Email Sent successfully")
        } catch (messagingException: MessagingException) {
            logger.error("Mail Exception", messagingException)
        }
    }
}
