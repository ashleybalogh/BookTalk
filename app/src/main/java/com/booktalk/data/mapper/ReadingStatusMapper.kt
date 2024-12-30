package com.booktalk.data.mapper

import com.booktalk.data.local.entity.ReadingStatus as DataReadingStatus
import com.booktalk.domain.model.book.ReadingStatus as DomainReadingStatus

fun DomainReadingStatus.toData(): DataReadingStatus {
    return when (this) {
        DomainReadingStatus.CURRENTLY_READING -> DataReadingStatus.CURRENTLY_READING
        DomainReadingStatus.WANT_TO_READ -> DataReadingStatus.WANT_TO_READ
        DomainReadingStatus.READ -> DataReadingStatus.READ
        DomainReadingStatus.DID_NOT_FINISH -> DataReadingStatus.DID_NOT_FINISH
        DomainReadingStatus.NONE -> DataReadingStatus.NONE
    }
}

fun DataReadingStatus.toDomain(): DomainReadingStatus {
    return when (this) {
        DataReadingStatus.CURRENTLY_READING -> DomainReadingStatus.CURRENTLY_READING
        DataReadingStatus.WANT_TO_READ -> DomainReadingStatus.WANT_TO_READ
        DataReadingStatus.READ -> DomainReadingStatus.READ
        DataReadingStatus.DID_NOT_FINISH -> DomainReadingStatus.DID_NOT_FINISH
        DataReadingStatus.NONE -> DomainReadingStatus.NONE
    }
}
