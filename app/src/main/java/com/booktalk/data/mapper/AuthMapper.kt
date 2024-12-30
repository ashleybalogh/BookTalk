package com.booktalk.data.mapper

import com.booktalk.data.remote.dto.auth.UserResponse
import com.booktalk.domain.model.User

fun UserResponse.toUser(): User {
    return User(
        id = id,
        email = email,
        displayName = displayName,
        photoUrl = photoUrl,
        isEmailVerified = emailVerified
    )
}
