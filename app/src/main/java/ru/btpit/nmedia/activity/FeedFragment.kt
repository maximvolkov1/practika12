package ru.btpit.nmedia.activity

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.launch
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentContainer
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.BaseTransientBottomBar
import com.google.android.material.snackbar.Snackbar
import ru.btpit.nmedia.R
import ru.btpit.nmedia.adapter.OnInteractionListener
import ru.btpit.nmedia.adapter.PostsAdapter
import ru.btpit.nmedia.databinding.FragmentFeedBinding
import ru.btpit.nmedia.dto.Post
import ru.btpit.nmedia.viewmodel.PostViewModel
import java.util.zip.Inflater


class FeedFragment : Fragment() {
    private val viewModel: PostViewModel by viewModels(
        ownerProducer = ::requireParentFragment
    )

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentFeedBinding.inflate(
        inflater,
        container,
        false
        )

        val newPostLauncher = registerForActivityResult(NewPostResultContract()) { result ->
            result ?: return@registerForActivityResult
            viewModel.changeContent(result)
            viewModel.save()
        }

        val editPostActivityContract =
            registerForActivityResult(EditPost()) { result ->
                result?: return@registerForActivityResult
                viewModel.changeContent(result)
                viewModel.save()
            }

        val adapter = PostsAdapter(object : OnInteractionListener{
            override fun onEdit(post: Post) {
                viewModel.edit(post)
                editPostActivityContract.launch(post.content)
            }

            override fun onVideo(post: Post) {
                val intentVideo = Intent(Intent.ACTION_VIEW, Uri.parse(post.video))
                startActivity(intentVideo)
            }

            override fun onLike(post: Post) {
                viewModel.likeById(post.id)
            }


            override fun onRemove(post: Post) {
                viewModel.removeById(post.id)
            }

            override fun onShare(post: Post) {
                val intent = Intent().apply {
                    action = Intent.ACTION_SEND
                    putExtra(Intent.EXTRA_TEXT, post.content)
                    type = "text/plain"
                    viewModel.shareById(post.id)
                }

                val shareIntent =
                    Intent.createChooser(intent, getString(R.string.chooser_share_post))
                startActivity(shareIntent)
            }

        })
        binding.list?.adapter = adapter
        viewModel.data.observe(viewLifecycleOwner) { posts ->
            adapter.submitList(posts)
        }

        binding.fab?.setOnClickListener {
            findNavController().navigate(R.id.action_feedFragment_to_newPostFragment)
        }
        return binding.root
    }
       /*run {
           val preferences = getPreferences(Context.MODE_PRIVATE)
           preferences.edit().apply {
                putString("key", "value")
                commit() // commit - синхронно, apply - асинхронно
           }
       }

      run {
           getPreferences(Context.MODE_PRIVATE)
               .getString("key", "no value")?.let {
                   Snackbar.make(binding.root, it, BaseTransientBottomBar.LENGTH_INDEFINITE)
                        .show()
                }
       }*/
    }




