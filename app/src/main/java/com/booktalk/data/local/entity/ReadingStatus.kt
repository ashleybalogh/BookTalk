package com.booktalk.data.local.entity

/**
 * Represents the reading status of a book in a user's library.
 * This enum follows a natural progression of states from initial addition to completion,
 * while supporting special cases like pausing or abandoning books.
 */
enum class ReadingStatus {
    /**
     * Book is not in user's library.
     */
    NONE,

    /**
     * Book is currently being read.
     * Indicates active reading progress is being made.
     */
    CURRENTLY_READING,

    /**
     * Book is in user's wishlist or to-read shelf.
     * Initial state when a book is first added without starting to read.
     */
    WANT_TO_READ,

    /**
     * Book has been completed.
     * Terminal state for successful reading completion.
     */
    READ,

    /**
     * Book was dropped before completion.
     * Terminal state for discontinued reading.
     */
    DID_NOT_FINISH;

    companion object {
        /**
         * Returns true if this status represents a completed state.
         */
        fun ReadingStatus.isCompleted(): Boolean = 
            this == READ || this == DID_NOT_FINISH

        /**
         * Returns true if this status represents an active reading state.
         */
        fun ReadingStatus.isActive(): Boolean =
            this == CURRENTLY_READING

        /**
         * Returns true if this status allows progress tracking.
         */
        fun ReadingStatus.allowsProgressTracking(): Boolean =
            this == CURRENTLY_READING
    }
}
