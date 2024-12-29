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
    READING,

    /**
     * Book is on hold.
     * Indicates that the book is paused or temporarily stopped.
     */
    ON_HOLD,

    /**
     * Book has been completed.
     * Terminal state for successful reading completion.
     */
    FINISHED,

    /**
     * Book was dropped before completion.
     * Terminal state for discontinued reading.
     */
    DROPPED;

    /**
     * Returns true if this status represents a completed state.
     */
    fun isCompleted(): Boolean = 
        this == FINISHED || this == DROPPED

    /**
     * Returns true if this status represents an active reading state.
     */
    fun isActive(): Boolean =
        this == READING || this == ON_HOLD

    /**
     * Returns true if this status allows progress tracking.
     */
    fun allowsProgressTracking(): Boolean =
        this == READING || this == ON_HOLD
}
