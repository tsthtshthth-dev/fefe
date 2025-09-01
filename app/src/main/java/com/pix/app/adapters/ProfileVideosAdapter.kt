package com.pix.app.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.pix.app.R
import com.pix.app.databinding.ItemProfileVideoBinding
import com.pix.app.models.Video

class ProfileVideosAdapter(
    private val videos: List<Video>,
    private val onVideoClick: (Video) -> Unit
) : RecyclerView.Adapter<ProfileVideosAdapter.VideoViewHolder>() {
    
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VideoViewHolder {
        val binding: ItemProfileVideoBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.item_profile_video,
            parent,
            false
        )
        return VideoViewHolder(binding)
    }
    
    override fun onBindViewHolder(holder: VideoViewHolder, position: Int) {
        holder.bind(videos[position])
    }
    
    override fun getItemCount(): Int = videos.size
    
    inner class VideoViewHolder(private val binding: ItemProfileVideoBinding) : 
        RecyclerView.ViewHolder(binding.root) {
        
        fun bind(video: Video) {
            binding.video = video
            
            // Load video thumbnail
            Glide.with(binding.root.context)
                .load(video.thumbnailUrl)
                .placeholder(R.color.surface)
                .centerCrop()
                .into(binding.ivThumbnail)
            
            // Show video stats
            binding.tvLikesCount.text = formatCount(video.likesCount)
            
            binding.root.setOnClickListener {
                onVideoClick(video)
            }
            
            binding.executePendingBindings()
        }
        
        private fun formatCount(count: Int): String {
            return when {
                count >= 1000000 -> "${count / 1000000}M"
                count >= 1000 -> "${count / 1000}K"
                else -> count.toString()
            }
        }
    }
}