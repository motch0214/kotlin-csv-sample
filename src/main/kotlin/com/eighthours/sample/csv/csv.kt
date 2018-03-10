package com.eighthours.sample.csv

import org.apache.commons.csv.CSVFormat
import org.apache.commons.csv.CSVParser
import org.apache.commons.csv.CSVPrinter
import org.apache.commons.csv.CSVRecord
import java.io.Reader
import java.io.Writer


abstract class CSVReader<out R>(reader: Reader, format: CSVFormat = CSVFormat.DEFAULT, offset: Long = 0) {

    private val parser = CSVParser(reader, format, 0, offset)

    abstract fun CSVRecord.parse(): R

    fun lines(): Sequence<R> {
        return parser.asSequence().map { it.parse() }
    }

    open fun CSVRecord.column(c: Column): String {
        return this[c.header]
    }
}


abstract class CSVWriter<in R>(writer: Writer, format: CSVFormat = CSVFormat.DEFAULT) {

    private val printer: CSVPrinter = CSVPrinter(writer, format)

    abstract val headers: List<Column>

    abstract fun R.encode(): Map<out Column, Any>

    fun write(value: R) {
        val encoded = value.encode()
        printer.printRecord(headers.map { encoded[it] })
    }
}


interface Column {
    val header: String
    val ordinal: Int
}
