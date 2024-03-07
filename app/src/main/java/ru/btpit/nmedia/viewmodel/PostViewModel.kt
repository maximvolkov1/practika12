package ru.btpit.nmedia.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ru.btpit.nmedia.db.AppDb
import ru.btpit.nmedia.dto.Post
import ru.btpit.nmedia.repository.PostRepository
import ru.btpit.nmedia.repository.PostRepositoryInMemoryImpl
import ru.btpit.nmedia.repository.PostRepositoryFileImpl
import ru.btpit.nmedia.repository.PostRepositorySQLiteImpl

private val empty = Post(
    id = 0,
    content = "",
    author = "",
    likedByMe = false,
    published = "",
    sharedByMe = false,
    viewedByMe = false,
    video = null
)

class PostViewModel(appication: Application) : AndroidViewModel(appication) {
    private val repository: PostRepository = PostRepositorySQLiteImpl(
        AppDb.getInstance(appication).postDao
    )
    val data = repository.getAll()
    val edited = MutableLiveData(empty)

    fun save() {
        edited.value?.let {
            repository.save(it)
        }
        edited.value = empty
    }

    fun edit(post: Post) {
        edited.value = post
    }

    fun changeContent(content: String) {
        val text = content.trim()
        if (edited.value?.content == text) {
            return
        }
        edited.value = edited.value?.copy(content = text)
    }

    fun likeById(id: Long) = repository.likeById(id)
    fun shareById(id: Long) = repository.shareById(id)
    fun removeById(id: Long) = repository.removeById(id)
}