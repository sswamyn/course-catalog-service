package org.swamy.coursecatalogservice.controller

import mu.KLogging
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import org.swamy.coursecatalogservice.service.GreetingService

@RestController
@RequestMapping("/v1/greetings")
class GreetingController(val greetingService: GreetingService) {

    companion object : KLogging()
    @GetMapping("/{name}")
    fun retrieveGreeting(@PathVariable("name") name: String): String {

        // return "Hello $name"
        // Instead of keeping the logic to build the response in the Controller,
        //      we will move it to the Service class service/GreetingService
        //      this is accomplished by injecting that Service into the constructor of the controller
        logger.info("Name is $name")
        return greetingService.retrieveGreeting(name)
    }
}