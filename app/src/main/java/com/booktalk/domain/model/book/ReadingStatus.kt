package com.booktalk.domain.model.book

enum class ReadingStatus {
    /**
     * Book is not in user's library.
     */
    NONE,

    /**
     * Book is in user's wishlist or to-read shelf.
     * Initial state when a book is first added without starting to read.
     */
    WANT_TO_READ,

    /**
     * Book is currently being read.
     * Indicates active reading progress is being made.
     */
    CURRENTLY_READING,

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

    /**
     * Returns true if this status represents a completed state.
     */
    fun isCompleted(): Boolean = 
        this == READ || this == DID_NOT_FINISH

    /**
     * Returns true if this status represents an active reading state.
     */
    fun isActive(): Boolean =
        this == CURRENTLY_READING
}
