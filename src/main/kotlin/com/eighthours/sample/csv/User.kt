package com.eighthours.sample.csv

import com.eighthours.sample.csv.UserColumn.*
import org.apache.commons.csv.CSVFormat
import org.apache.commons.csv.CSVRecord
import org.apache.commons.csv.QuoteMode
import java.io.Reader
import java.io.Writer
import java.time.LocalDate
import java.time.format.DateTimeFormatter


data class User(
        val name: String,
        val address: String,
        val sex: String,
        val age: Int,
        val birthday: LocalDate
)


enum class UserColumn(override val header: String) : Column {
    NAME("Name"),
    ADDRESS("Address"),
    SEX("Sex"),
    AGE("Age"),
    BIRTHDAY("Birthday");
}


class UserReader(reader: Reader) : CSVReader<User>(reader, FORMAT) {

    private companion object {
        val FORMAT: CSVFormat = CSVFormat.DEFAULT.withFirstRecordAsHeader()
        val DATE_FORMAT: DateTimeFormatter = DateTimeFormatter.ofPattern("yyyy/M/d")
    }

    private fun String.toLocalDate(): LocalDate = LocalDate.parse(this, DATE_FORMAT)

    override fun CSVRecord.parse() = User(
            name = column(NAME),
            address = column(ADDRESS),
            sex = column(SEX),
            age = column(AGE).toInt(),
            birthday = column(BIRTHDAY).toLocalDate())
}


class UserWriter(writer: Writer) : CSVWriter<User>(writer, FORMAT) {

    private companion object {
        val FORMAT: CSVFormat = CSVFormat.DEFAULT
                .withRecordSeparator("\n")
                .withQuoteMode(QuoteMode.NON_NUMERIC)
        val DATE_FORMAT: DateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
    }

    override val headers = UserColumn.values().toList()

    override fun User.encode() = mapOf(
            NAME to name,
            ADDRESS to address,
            SEX to sex,
            AGE to age,
            BIRTHDAY to birthday.format(DATE_FORMAT))
}
