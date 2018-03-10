package com.eighthours.sample.csv

import org.apache.commons.csv.CSVRecord
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
import java.io.Reader
import java.io.StringReader
import java.io.StringWriter
import java.io.Writer
import java.time.LocalDate
import java.time.format.DateTimeFormatter


class CSVSimpleTest {

    private val text = """
            "Name","Address","Sex","Age","Birthday"
            "茂木 康文","mogi_yasufumi@example.com","男",32,"1983-04-25"
            "立川 弘也","tachikawa_hironari@example.com","男",69,"1946-05-12"
            "宮田 真一","miyata_shinichi@example.com","男",79,"1935-09-13"
            "浅田 俊二","asada_shunji@example.com","男",53,"1961-10-29"

        """.trimIndent()

    private val data = listOf(
            User("茂木 康文", "mogi_yasufumi@example.com", "男", 32, LocalDate.of(1983, 4, 25)),
            User("立川 弘也", "tachikawa_hironari@example.com", "男", 69, LocalDate.of(1946, 5, 12)),
            User("宮田 真一", "miyata_shinichi@example.com", "男", 79, LocalDate.of(1935, 9, 13)),
            User("浅田 俊二", "asada_shunji@example.com", "男", 53, LocalDate.of(1961, 10, 29)))


    @Test
    fun test() {
        val users = StringReader(text).use {
            val reader = SimpleUserReader(it)
            reader.lines().toList()
        }

        assertThat(users).containsSequence(data)

        val csv = StringWriter().use {
            val writer = SimpleUserWriter(it)
            users.forEach { writer.write(it) }
            it.toString()
        }

        assertThat(csv).isEqualTo(text)
    }
}


private val DATE_FORMAT: DateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")


private class SimpleUserReader(reader: Reader) : CSVReader<User>(reader) {

    private fun String.toLocalDate(): LocalDate = LocalDate.parse(this, DATE_FORMAT)

    override fun CSVRecord.parse() = User(
            name = column(UserColumn.NAME),
            address = column(UserColumn.ADDRESS),
            sex = column(UserColumn.SEX),
            age = column(UserColumn.AGE).toInt(),
            birthday = column(UserColumn.BIRTHDAY).toLocalDate())
}


private class SimpleUserWriter(writer: Writer) : CSVWriter<User>(writer, HEADERS) {

    companion object {
        private val HEADERS = UserColumn.values().toList()
    }

    override fun User.encode() = mapOf(
            UserColumn.NAME to name,
            UserColumn.ADDRESS to address,
            UserColumn.SEX to sex,
            UserColumn.AGE to age,
            UserColumn.BIRTHDAY to birthday.format(DATE_FORMAT))
}
