package pl.arturczopek.devoxx

import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication

@SpringBootApplication
class DevoxxApplication

fun main(args: Array<String>) {
    SpringApplication.run(DevoxxApplication::class.java, *args)
}
