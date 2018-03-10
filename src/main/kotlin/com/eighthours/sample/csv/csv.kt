package com.eighthours.sample.csv

import org.apache.commons.csv.*
import java.io.Reader
import java.io.Writer


private val READER_FORMAT = CSVFormat.DEFAULT
        .withFirstRecordAsHeader()

abstract class CSVReader<out R>(reader: Reader, format: CSVFormat = READER_FORMAT, offset: Long = 0) {

    private val parser = CSVParser(reader, format, 0, offset)

    abstract fun CSVRecord.parse(): R

    fun lines(): Sequence<R> {
        return parser.asSequence().map { it.parse() }
    }

    open fun CSVRecord.column(c: Column): String {
        return this[c.header]
    }
}


private val WRITER_FORMAT = CSVFormat.DEFAULT
        .withRecordSeparator('\n')
        .withQuoteMode(QuoteMode.NON_NUMERIC)

abstract class CSVWriter<in R>(writer: Writer, private val headers: List<Column>, format: CSVFormat = WRITER_FORMAT) {

    private val format = format.withHeader(*(headers.map { it.header }.toTypedArray()))

    private val printer: CSVPrinter = this.format.print(writer)

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
