# Kotlin csv sample

Sample of apache commons-csv with kotlin.

You can parse csv like:

```kotlin
override fun CSVRecord.parse() = User(
        name = column(NAME),
        address = column(ADDRESS),
        sex = column(SEX),
        age = column(AGE).toInt(),
        birthday = column(BIRTHDAY).toLocalDate())
```

and you can encode csv like:

```kotlin
override fun User.encode() = mapOf(
        NAME to name,
        ADDRESS to address,
        SEX to sex,
        AGE to age,
        BIRTHDAY to birthday.format(DATE_FORMAT))
```
