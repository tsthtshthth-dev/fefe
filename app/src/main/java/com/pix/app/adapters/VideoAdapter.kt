package com.pix.app.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.Player
import com.pix.app.R
import com.pix.app.databinding.ItemVideoBinding
import com.pix.app.models.Video
import java.text.SimpleDateFormat
import java.util.*

class VideoAdapter(
    private val videos: List<Video>,
    private val onVideoAction: (Video, String) -> Unit
) : RecyclerView.Adapter<VideoAdapter.VideoViewHolder>() {
    
    private var currentPlayingPosition = -1
    private var currentPlayer: ExoPlayer? = null
    
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VideoViewHolder {
        val binding: ItemVideoBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.item_video,
            parent,
            false
        )
        return VideoViewHolder(binding)
    }
    
    override fun onBindViewHolder(holder: VideoViewHolder, position: Int) {
        holder.bind(videos[position])
    }
    
    override fun getItemCount(): Int = videos.size
    
    fun playVideoAt(position: Int) {
        if (position == currentPlayingPosition) return
        
        // Pause current video
        currentPlayer?.pause()
        
        // Play new video
        if (position >= 0 && position < videos.size) {
            currentPlayingPosition = position
            notifyItemChanged(position)
        }
    }
    
    fun pauseAllVideos() {
        currentPlayer?.pause()
    }
    
    inner class VideoViewHolder(private val binding: ItemVideoBinding) : 
        RecyclerView.ViewHolder(binding.root) {
        
        private var player: ExoPlayer? = null
        
        fun bind(video: Video) {
            binding.video = video
            
            // Setup user info
            binding.tvUsername.text = "@${video.username}"
            binding.tvCaption.text = video.caption
            binding.tvLikesCount.text = formatCount(video.likesCount)
            binding.tvCommentsCount.text = formatCount(video.commentsCount)
            binding.tvSharesCount.text = formatCount(video.sharesCount)
            binding.tvTimeAgo.text = getTimeAgo(video.createdAt)
            
            // Load user avatar
            Glide.with(binding.root.context)
                .load(video.userAvatar)
                .placeholder(R.drawable.ic_profile)
                .circleCrop()
                .into(binding.ivUserAvatar)
            
            // Setup like button
            binding.btnLike.setImageResource(
                if (video.isLiked) R.drawable.ic_like_filled else R.drawable.ic_like
            )
            
            // Setup follow button
            binding.btnFollow.visibility = if (video.isFollowing) View.GONE else View.VISIBLE
            
            // Setup video player
            setupVideoPlayer(video)
            
            // Setup click listeners
            setupClickListeners(video)
            
            binding.executePendingBindings()
        }
        
        private fun setupVideoPlayer(video: Video) {
            if (adapterPosition == currentPlayingPosition) {
                // Initialize player for current video
                player = ExoPlayer.Builder(binding.root.context).build()
                binding.playerView.player = player
                
                val mediaItem = MediaItem.fromUri(video.videoUrl)
                player?.setMediaItem(mediaItem)
                player?.prepare()
                player?.playWhenReady = true
                player?.repeatMode = Player.REPEAT_MODE_ONE
                
                currentPlayer = player
                
                // Hide thumbnail when video starts playing
                player?.addListener(object : Player.Listener {
                    override fun onPlaybackStateChanged(playbackState: Int) {
                        if (playbackState == Player.STATE_READY) {
                            binding.ivThumbnail.visibility = View.GONE
                            binding.progressBar.visibility = View.GONE
                        } else if (playbackState == Player.STATE_BUFFERING) {
                            binding.progressBar.visibility = View.VISIBLE
                        }
                    }
                })
            } else {
                // Show thumbnail for non-playing videos
                binding.ivThumbnail.visibility = View.VISIBLE
                binding.progressBar.visibility = View.GONE
                binding.playerView.player = null
                player?.release()
                player = null
            }
        }
        
        private fun setupClickListeners(video: Video) {
            binding.btnLike.setOnClickListener {
                onVideoAction(video, "like")
            }
            
            binding.btnComment.setOnClickListener {
                onVideoAction(video, "comment")
            }
            
            binding.btnShare.setOnClickListener {
                onVideoAction(video, "share")
            }
            
            binding.btnFollow.setOnClickListener {
                onVideoAction(video, "follow")
            }
            
            binding.ivUserAvatar.setOnClickListener {
                onVideoAction(video, "profile")
            }
            
            binding.tvUsername.setOnClickListener {
                onVideoAction(video, "profile")
            }
            
            // Double tap to like
            var lastTapTime = 0L
            binding.playerView.setOnClickListener {
                val currentTime = System.currentTimeMillis()
                if (currentTime - lastTapTime < 300) {
                    // Double tap detected
                    onVideoAction(video, "like")
                    showLikeAnimation()
                }
                lastTapTime = currentTime
            }
        }
        
        private fun showLikeAnimation() {
            binding.ivLikeAnimation.visibility = View.VISIBLE
            binding.ivLikeAnimation.animate()
                .scaleX(1.5f)
                .scaleY(1.5f)
                .alpha(0f)
                .setDuration(800)
                .withEndAction {
                    binding.ivLikeAnimation.visibility = View.GONE
                    binding.ivLikeAnimation.scaleX = 1f
                    binding.ivLikeAnimation.scaleY = 1f
                    binding.ivLikeAnimation.alpha = 1f
                }
        }
        
        private fun formatCount(count: Int): String {
            return when {
                count >= 1000000 -> "${count / 1000000}M"
                count >= 1000 -> "${count / 1000}K"
                else -> count.toString()
            }
        }
        
        private fun getTimeAgo(timestamp: Long): String {
            val now = System.currentTimeMillis()
            val diff = now - timestamp
            
            return when {
                diff < 60000 -> "الآن"
                diff < 3600000 -> "${diff / 60000}د"
                diff < 86400000 -> "${diff / 3600000}س"
                diff < 604800000 -> "${diff / 86400000}ي"
                else -> {
                    val sdf = SimpleDateFormat("dd/MM", Locale.getDefault())
                    sdf.format(Date(timestamp))
                }
            }
        }
    }
}