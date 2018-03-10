package com.eighthours.sample.csv

import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
import java.io.StringReader
import java.time.LocalDate

class CSVReaderTest {

    @Test
    fun test_read() {
        val source = """
            "Name","Address","Sex","Age","Birthday"
            "茂木 康文","mogi_yasufumi@example.com","男",32,"1983/4/25"
            "立川 弘也","tachikawa_hironari@example.com","男",69,"1946/5/12"
            "宮田 真一","miyata_shinichi@example.com","男",79,"1935/9/13"
            "浅田 俊二","asada_shunji@example.com","男",53,"1961/10/29"
        """.trimIndent()

        val users = StringReader(source).use {
            val reader = UserReader(it)
            reader.lines().toList()
        }

        assertThat(users).containsSequence(
                User("茂木 康文", "mogi_yasufumi@example.com", "男", 32, LocalDate.of(1983, 4, 25)),
                User("立川 弘也", "tachikawa_hironari@example.com", "男", 69, LocalDate.of(1946, 5, 12)),
                User("宮田 真一", "miyata_shinichi@example.com", "男", 79, LocalDate.of(1935, 9, 13)),
                User("浅田 俊二", "asada_shunji@example.com", "男", 53, LocalDate.of(1961, 10, 29)))
    }
}
