package ru.btpit.nmedia.dto
data class Post(
    val id: Long,
    val author: String,
    val content: String,
    val published: String,
    val likes: Int = 0,
    val likedByMe: Boolean,
    val shares: Int = 0,
    val sharedByMe: Boolean,
    val viewedByMe: Boolean,
    val viewes: Int = 0 ,
    val video: String? = null
)

