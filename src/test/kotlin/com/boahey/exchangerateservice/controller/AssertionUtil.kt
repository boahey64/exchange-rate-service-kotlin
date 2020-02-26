package com.boahey.exchangerateservice.controller

import com.boahey.exchangerateservice.exception.CustomErrorResponse
import com.fasterxml.jackson.databind.ObjectMapper
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers
import org.springframework.test.web.servlet.MvcResult
import java.io.IOException

@Throws(IOException::class)
fun checkAssertionsForWrongParameter(mvcResult: MvcResult, expectedErrorResponse: CustomErrorResponse?) {
    val objectMapper = ObjectMapper()
    val actualResponseBody = mvcResult.response.contentAsString
    val actualJsonNode = objectMapper.readTree(actualResponseBody)
    val expectedResponseBody = objectMapper.writeValueAsString(expectedErrorResponse)
    val expectedJsonNode = objectMapper.readTree(expectedResponseBody)
    assertThat(actualJsonNode["status"], Matchers.`is`(expectedJsonNode["status"]))
    assertThat(actualJsonNode["title"], Matchers.`is`(expectedJsonNode["title"]))
    assertThat(actualJsonNode["description"], Matchers.`is`(expectedJsonNode["description"]))
}
