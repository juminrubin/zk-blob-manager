package org.jumin.sample.zk.blob

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import javax.annotation.PostConstruct
import javax.annotation.PreDestroy

@SpringBootApplication
class BlobManagerApplication {

    companion object {
        val LOGGER: Logger = LoggerFactory.getLogger(BlobManagerApplication::class.java)
    }

    @PostConstruct
    fun iniAfterStart() {
        LOGGER.info("Initializing after start...")
    }

    @PreDestroy
    fun destroy() {
        LOGGER.info("Closing stuffs before shutdown...")
    }
}

fun main(args: Array<String>) {
    runApplication<BlobManagerApplication>(*args)
}
