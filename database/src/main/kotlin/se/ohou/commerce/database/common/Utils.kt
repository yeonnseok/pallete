package se.ohou.commerce.database.common

import java.time.Instant
import java.time.ZoneId
import java.time.ZonedDateTime

object Utils {

    val ZONE_ID: ZoneId = ZoneId.of("Asia/Seoul")

    fun String.toZonedDateTime(): ZonedDateTime = Instant.parse(this).atZone(ZONE_ID)
}
