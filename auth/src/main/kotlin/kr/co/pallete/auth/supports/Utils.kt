package kr.co.pallete.auth.supports

import java.time.Instant
import java.time.ZoneId
import java.time.ZonedDateTime

object Utils {

    private val ZONE_ID: ZoneId = ZoneId.of("Asia/Seoul")

    fun String.toZonedDateTime(): ZonedDateTime = Instant.parse(this).atZone(ZONE_ID)
}
