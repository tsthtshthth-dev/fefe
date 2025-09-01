package com.pix.app.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.pix.app.R
import com.pix.app.databinding.ItemDiscoverHashtagBinding
import com.pix.app.databinding.ItemDiscoverUserBinding
import com.pix.app.databinding.ItemDiscoverVideoBinding
import com.pix.app.models.HashTag
import com.pix.app.models.User
import com.pix.app.models.Video

class DiscoverAdapter(
    private val items: List<Any>,
    private val onItemAction: (Any, String) -> Unit
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    
    companion object {
        private const val TYPE_HASHTAG = 0
        private const val TYPE_USER = 1
        private const val TYPE_VIDEO = 2
    }
    
    override fun getItemViewType(position: Int): Int {
        return when (items[position]) {
            is HashTag -> TYPE_HASHTAG
            is User -> TYPE_USER
            is Video -> TYPE_VIDEO
            else -> TYPE_VIDEO
        }
    }
    
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            TYPE_HASHTAG -> {
                val binding: ItemDiscoverHashtagBinding = DataBindingUtil.inflate(
                    LayoutInflater.from(parent.context),
                    R.layout.item_discover_hashtag,
                    parent,
                    false
                )
                HashtagViewHolder(binding)
            }
            TYPE_USER -> {
                val binding: ItemDiscoverUserBinding = DataBindingUtil.inflate(
                    LayoutInflater.from(parent.context),
                    R.layout.item_discover_user,
                    parent,
                    false
                )
                UserViewHolder(binding)
            }
            else -> {
                val binding: ItemDiscoverVideoBinding = DataBindingUtil.inflate(
                    LayoutInflater.from(parent.context),
                    R.layout.item_discover_video,
                    parent,
                    false
                )
                VideoViewHolder(binding)
            }
        }
    }
    
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is HashtagViewHolder -> holder.bind(items[position] as HashTag)
            is UserViewHolder -> holder.bind(items[position] as User)
            is VideoViewHolder -> holder.bind(items[position] as Video)
        }
    }
    
    override fun getItemCount(): Int = items.size
    
    inner class HashtagViewHolder(private val binding: ItemDiscoverHashtagBinding) : 
        RecyclerView.ViewHolder(binding.root) {
        
        fun bind(hashtag: HashTag) {
            binding.hashtag = hashtag
            binding.tvHashtagName.text = hashtag.name
            binding.tvVideosCount.text = "${formatCount(hashtag.videosCount)} videos"
            binding.tvViewsCount.text = "${formatCount(hashtag.viewsCount.toInt())} views"
            
            binding.root.setOnClickListener {
                onItemAction(hashtag, "click")
            }
            
            binding.executePendingBindings()
        }
    }
    
    inner class UserViewHolder(private val binding: ItemDiscoverUserBinding) : 
        RecyclerView.ViewHolder(binding.root) {
        
        fun bind(user: User) {
            binding.user = user
            binding.tvUsername.text = "@${user.username}"
            binding.tvFullName.text = user.fullName
            binding.tvFollowersCount.text = "${formatCount(user.followersCount)} followers"
            binding.tvBio.text = user.bio
            
            // Load user avatar
            Glide.with(binding.root.context)
                .load(user.avatar)
                .placeholder(R.drawable.ic_profile)
                .circleCrop()
                .into(binding.ivUserAvatar)
            
            // Show verified badge
            binding.ivVerified.visibility = if (user.isVerified) 
                android.view.View.VISIBLE else android.view.View.GONE
            
            binding.root.setOnClickListener {
                onItemAction(user, "click")
            }
            
            binding.btnFollow.setOnClickListener {
                onItemAction(user, "follow")
            }
            
            binding.executePendingBindings()
        }
    }
    
    inner class VideoViewHolder(private val binding: ItemDiscoverVideoBinding) : 
        RecyclerView.ViewHolder(binding.root) {
        
        fun bind(video: Video) {
            binding.video = video
            binding.tvLikesCount.text = formatCount(video.likesCount)
            
            // Load video thumbnail
            Glide.with(binding.root.context)
                .load(video.thumbnailUrl)
                .placeholder(R.color.surface)
                .centerCrop()
                .into(binding.ivThumbnail)
            
            binding.root.setOnClickListener {
                onItemAction(video, "click")
            }
            
            binding.executePendingBindings()
        }
    }
    
    private fun formatCount(count: Int): String {
        return when {
            count >= 1000000 -> "${count / 1000000}M"
            count >= 1000 -> "${count / 1000}K"
            else -> count.toString()
        }
    }
}