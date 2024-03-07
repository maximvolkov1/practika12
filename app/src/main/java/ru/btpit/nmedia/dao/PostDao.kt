package ru.btpit.nmedia.dao

import ru.btpit.nmedia.dto.Post

interface PortDao {
    fun fetAll(): List<Post>
    fun save(post:Post): Post
    fun likeById(id: Long)
    fun shareById(id: Long)
    fun removeById(id: Long)
    fun getAll(): List<Post>
}
