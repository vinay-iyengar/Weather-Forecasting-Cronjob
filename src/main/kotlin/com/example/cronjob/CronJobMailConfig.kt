package com.example.cronjob
import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty
import io.dropwizard.Configuration

@JsonIgnoreProperties(ignoreUnknown = true)
class CronJobMailConfig(
    @JsonProperty("username") val username: String,
    @JsonProperty("password") val password: String
): Configuration()
