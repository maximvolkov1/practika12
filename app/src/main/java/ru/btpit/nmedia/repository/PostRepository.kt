package ru.btpit.nmedia.repository

import androidx.lifecycle.LiveData
import ru.btpit.nmedia.dto.Post

interface PostRepository {
    fun getAll(): LiveData<List<Post>>
    fun likeById(id: Long)
    fun save(post: Post)
    fun shareById(id: Long)
    fun removeById(id: Long)
}