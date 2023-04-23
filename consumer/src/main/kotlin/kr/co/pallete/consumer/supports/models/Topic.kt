package kr.co.pallete.consumer.supports.models

import com.fasterxml.jackson.annotation.JsonIgnoreProperties

@JsonIgnoreProperties(ignoreUnknown = true)
data class Topic<Type, Body>(
    val type: Type,
    val version: String,
    val body: Body,
)
