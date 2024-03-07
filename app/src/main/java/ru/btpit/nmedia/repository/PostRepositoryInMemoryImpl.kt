package ru.btpit.nmedia.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import ru.btpit.nmedia.dto.Post

class PostRepositoryInMemoryImpl : PostRepository {
    private var nextId = 1L
    private var posts = listOf(
        Post(
            id = nextId++,
            author = "Интересно знать!",
            content = "Природа очень красочна и разнообразна. У некоторых животных и растений можно увидеть самые разнообразные раскрасы. Но так бывает не всегда. Иногда на свет появляются особи, которые кардинально отличаются от своего вида и полностью лишены ярких цветов. В этой статье мы расскажем о животных альбиносах и меланистах.",
            published = "24 января в 19:20",
            likes = 999,
            shares = 123,
            viewes = 5300,
            likedByMe = false,
            sharedByMe = false,
            viewedByMe = false,
            video = "https://www.youtube.com/watch?v=LF-CM40nLCM"
        ),
        Post(
            id = nextId++,
            author = "Интересно знать!",
            content = "Спросите у любого, какое животное на нашей планете носит гордое звание царь зверей, и каждый из них ответит, что это лев. Именно эта большая кошка из рода пантер занимает столь высокий пост. Но почему именно лев – царь зверей? Лев – хищный представитель семейства Кошачьих, один из самых умных, быстрых и крупных животных. В этой статье мы расскажем почему лев – царь зверей.",
            published = "20 января в 12:12",
            likes = 101,
            shares = 33,
            viewes = 400,
            likedByMe = false,
            sharedByMe = false,
            viewedByMe = false,
            video = null
        ),
    ).reversed()

    private val data = MutableLiveData(posts)

    override fun getAll(): LiveData<List<Post>> = data

    override fun save(post: Post) {
        if (post.id == 0L) {
            // TODO: remove hardcoded author & published
            posts = listOf(
                post.copy(
                    id = nextId++,
                    author = "Me",
                    likedByMe = false,
                    published = "now",
                    viewes = 0 + 1
                )
            ) + posts
            data.value = posts
            return
        }

        posts = posts.map {
            if (it.id != post.id) it else it.copy(content = post.content)
        }
        data.value = posts
    }

    override fun likeById(id: Long) {
        posts = posts.map {
            if (it.id != id) it else it.copy(
                likedByMe = !it.likedByMe,
                likes = if (!it.likedByMe) it.likes + 1 else it.likes - 1
            )
        }
        data.value = posts
    }

    override fun shareById(id: Long) {
        posts = posts.map {
            if (it.id != id) it else it.copy(
                sharedByMe = !it.sharedByMe,
                shares = it.shares + 1
            )
        }
        data.value = posts
    }
    override fun removeById(id: Long) {
        posts = posts.filter { it.id != id }
        data.value = posts
    }
}





