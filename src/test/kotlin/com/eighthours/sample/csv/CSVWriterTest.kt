package com.eighthours.sample.csv

import org.apache.commons.csv.CSVFormat
import org.apache.commons.csv.QuoteMode
import org.assertj.core.api.Assertions
import org.junit.Test
import java.io.StringWriter
import java.io.Writer
import java.time.LocalDate
import java.time.format.DateTimeFormatter


class CSVWriterTest {

    @Test
    fun test_write() {
        val users = listOf(
                User("茂木 康文", "mogi_yasufumi@example.com", "男", 32, LocalDate.of(1983, 4, 25)),
                User("立川 弘也", "tachikawa_hironari@example.com", "男", 69, LocalDate.of(1946, 5, 12)),
                User("宮田 真一", "miyata_shinichi@example.com", "男", 79, LocalDate.of(1935, 9, 13)),
                User("浅田 俊二", "asada_shunji@example.com", "男", 53, LocalDate.of(1961, 10, 29)))

        val text = StringWriter().use {
            val writer = UserWriter(it)
            users.forEach { writer.write(it) }
            it.toString()
        }

        val expected = """
            "Name","Address","Sex","Age","Birthday"
            "茂木 康文","mogi_yasufumi@example.com","男",32,"1983-04-25"
            "立川 弘也","tachikawa_hironari@example.com","男",69,"1946-05-12"
            "宮田 真一","miyata_shinichi@example.com","男",79,"1935-09-13"
            "浅田 俊二","asada_shunji@example.com","男",53,"1961-10-29"

        """.trimIndent()

        Assertions.assertThat(text).isEqualTo(expected)
    }


    @Test
    fun test_write_shuffle() {
        val users = listOf(
                User("茂木 康文", "mogi_yasufumi@example.com", "男", 32, LocalDate.of(1983, 4, 25)),
                User("立川 弘也", "tachikawa_hironari@example.com", "男", 69, LocalDate.of(1946, 5, 12)),
                User("宮田 真一", "miyata_shinichi@example.com", "男", 79, LocalDate.of(1935, 9, 13)),
                User("浅田 俊二", "asada_shunji@example.com", "男", 53, LocalDate.of(1961, 10, 29)))

        val text = StringWriter().use {
            val writer = UserWriterShuffle(it)
            users.forEach { writer.write(it) }
            it.toString()
        }

        val expected = """
            "Name","Address","Sex","Age","Birthday"
            "茂木 康文","mogi_yasufumi@example.com",,32,"1983-04-25"
            "立川 弘也","tachikawa_hironari@example.com",,69,"1946-05-12"
            "宮田 真一","miyata_shinichi@example.com",,79,"1935-09-13"
            "浅田 俊二","asada_shunji@example.com",,53,"1961-10-29"

        """.trimIndent()

        Assertions.assertThat(text).isEqualTo(expected)
    }
}


class UserWriterShuffle(writer: Writer) : CSVWriter<User>(writer, HEADERS, FORMAT) {

    private companion object {
        val FORMAT: CSVFormat = CSVFormat.DEFAULT
                .withRecordSeparator("\n")
                .withQuoteMode(QuoteMode.NON_NUMERIC)

        val DATE_FORMAT: DateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")

        private val HEADERS = UserColumn.values().toList()
    }

    override fun User.encode() = mapOf(
            UserColumn.ADDRESS to address,
            UserColumn.NAME to name,
            UserColumn.BIRTHDAY to birthday.format(DATE_FORMAT),
            UserColumn.AGE to age)
}
