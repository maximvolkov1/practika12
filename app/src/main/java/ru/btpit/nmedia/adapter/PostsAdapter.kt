package ru.btpit.nmedia.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import ru.btpit.nmedia.R
import ru.btpit.nmedia.databinding.PostCardLayoutBinding
import ru.btpit.nmedia.dto.Post
import ru.btpit.nmedia.adapter.PostsAdapter.PostViewHolder
import java.math.RoundingMode
import java.text.DecimalFormat

interface OnInteractionListener {
    fun onLike(post: Post) {}
    fun onEdit(post: Post) {}
    fun onRemove(post: Post) {}
    fun onShare(post: Post) {}
    fun onVideo(post: Post)
}

class PostsAdapter(
    private val onInteractionListener: OnInteractionListener,
) : ListAdapter<Post, PostViewHolder>(PostDiffCallback()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder {
        val binding =
            PostCardLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PostViewHolder(binding, onInteractionListener)
    }

    override fun onBindViewHolder(holder: PostViewHolder, position: Int) {
        val post = getItem(position)
        holder.bind(post)
    }


    class PostViewHolder(
        private val binding: PostCardLayoutBinding,
        private val onInteractionListener: OnInteractionListener,
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(post: Post) {
            binding.apply {
                author.text = post.author
                published.text = post.published
                content.text = post.content
                like.isChecked = post.likedByMe
                like.text = "${post.likes}"
                share.isChecked = post.sharedByMe
                share.text = "${post.shares}"
                like.text = formatCount(post.likes)
                share.text = formatCount(post.shares)
                viewsCount.text = formatCount(post.viewes + 1)
                if (post.video == null) {
                    binding.playVideoGroup.visibility = View.GONE
                } else {
                    binding.playVideoGroup.visibility = View.VISIBLE
                }
                play.setOnClickListener { onInteractionListener.onVideo(post) }
                postImage.setOnClickListener { onInteractionListener.onVideo(post) }
                menu.setOnClickListener {
                    PopupMenu(it.context, it).apply {
                        inflate(R.menu.options_post)
                        setOnMenuItemClickListener { item ->
                            when (item.itemId) {
                                R.id.remove -> {
                                    onInteractionListener.onRemove(post)
                                    true
                                }

                                R.id.edit -> {
                                    onInteractionListener.onEdit(post)
                                    true
                                }

                                else -> false
                            }
                        }
                    }.show()
                }
                like.setOnClickListener {
                    onInteractionListener.onLike(post)
                }
                share.setOnClickListener {
                    onInteractionListener.onShare(post)
                }
            }
            }
        }
    }

    class PostDiffCallback : DiffUtil.ItemCallback<Post>() {
        override fun areItemsTheSame(oldItem: Post, newItem: Post): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Post, newItem: Post): Boolean {
            return oldItem == newItem
        }
    }

fun formatCount(num: Int): String {
    return when {
        num >= 1_000_000 -> {
            val millionPart = num / 1_000_000
            val remainder = num % 1_000_000
            if (remainder == 0) {
                "$millionPart"
            }
            else { "$millionPart.${(remainder / 1_00000)}M"
            }
        }
        num   > 999 -> {
            val numString =
                if (num % 1_000 == 0) "${num / 1_000}K" else "${num / 1_000}.${(num % 1_000) / 100}K"
            numString
        }
        num >= 10000 -> "${num / 10000}K"
        else -> num.toString()
    }
}


