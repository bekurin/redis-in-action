package com.example.pipeline

import com.fasterxml.jackson.databind.ObjectMapper
import jakarta.annotation.PostConstruct
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.stereotype.Service
import java.time.LocalDate

@Service
class PipelineService(
    private val redisTemplate: RedisTemplate<String, String>,
    private val objectMapper: ObjectMapper,
) {
    private val redisKey = "BOARDABLE_ROUTE"
    private val log = LoggerFactory.getLogger(PipelineService::class.java)

    @PostConstruct
    fun init() {
        val now = LocalDate.now()
        val localDates = (1..10000L).map { dayToAdds ->
            now.plusDays(dayToAdds)
        }

        val currentTimeMillisForWithPipeline = System.currentTimeMillis()
        // 321ms 327ms 331ms 392ms 319ms
        saveToRedisWithPipeline(localDates)
        log.info("running with pipeline took {}ms", System.currentTimeMillis() - currentTimeMillisForWithPipeline)

        redisTemplate.delete(redisKey)

        val currentTimeMillisForWithoutPipeline = System.currentTimeMillis()
        // 2613ms 2448ms 2363ms 2448ms 2713ms
        saveToRedisWithoutPipeline(localDates)
        log.info("running without pipeline took {}ms", System.currentTimeMillis() - currentTimeMillisForWithoutPipeline)
    }

    fun saveToRedisWithoutPipeline(localDates: List<LocalDate>): Int {
        localDates.mapIndexed { index, localDate ->
            val helloDto = HelloDto("$localDate", "hello$index")
            redisTemplate.opsForHash<String, String>()
                .put(
                    redisKey,
                    localDate.toString(),
                    objectMapper.writeValueAsString(helloDto)
                )
        }
        return localDates.size
    }

    fun saveToRedisWithPipeline(localDates: List<LocalDate>): Int {
        redisTemplate.executePipelined { connection ->
            localDates.mapIndexed { index, localDate ->
                val helloDto = HelloDto("$localDate", "hello$index")
                connection.hashCommands()
                    .hSet(
                        redisKey.toByteArray(),
                        localDate.toString().toByteArray(),
                        objectMapper.writeValueAsBytes(helloDto)
                    )
            }
            return@executePipelined null
        }
        return localDates.size
    }

    data class HelloDto(
        val createdAt: String,
        val message: String
    )
}
