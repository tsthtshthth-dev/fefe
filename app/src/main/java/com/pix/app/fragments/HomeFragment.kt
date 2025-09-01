package com.pix.app.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import androidx.recyclerview.widget.RecyclerView
import com.pix.app.R
import com.pix.app.adapters.VideoAdapter
import com.pix.app.databinding.FragmentHomeBinding
import com.pix.app.models.Video
import com.pix.app.ui.search.SearchActivity
import com.pix.app.ui.notifications.NotificationsActivity

class HomeFragment : Fragment() {
    
    private lateinit var binding: FragmentHomeBinding
    private lateinit var videoAdapter: VideoAdapter
    private val videoList = mutableListOf<Video>()
    
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_home, container, false)
        return binding.root
    }
    
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
        setupRecyclerView()
        setupClickListeners()
        loadSampleVideos()
    }
    
    private fun setupRecyclerView() {
        videoAdapter = VideoAdapter(videoList) { video, action ->
            handleVideoAction(video, action)
        }
        
        binding.recyclerViewVideos.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            adapter = videoAdapter
            
            // Add snap helper for full screen video experience
            val snapHelper = PagerSnapHelper()
            snapHelper.attachToRecyclerView(this)
            
            // Auto-play video when scrolled to
            addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                    super.onScrollStateChanged(recyclerView, newState)
                    if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                        val layoutManager = recyclerView.layoutManager as LinearLayoutManager
                        val position = layoutManager.findFirstCompletelyVisibleItemPosition()
                        if (position != RecyclerView.NO_POSITION) {
                            videoAdapter.playVideoAt(position)
                        }
                    }
                }
            })
        }
    }
    
    private fun setupClickListeners() {
        binding.btnSearch.setOnClickListener {
            val intent = Intent(requireContext(), SearchActivity::class.java)
            startActivity(intent)
        }
        
        binding.btnNotifications.setOnClickListener {
            val intent = Intent(requireContext(), NotificationsActivity::class.java)
            startActivity(intent)
        }
    }
    
    private fun loadSampleVideos() {
        // Sample data - replace with actual API call
        val sampleVideos = listOf(
            Video(
                id = "1",
                userId = "user1",
                username = "ahmed_creator",
                userAvatar = "",
                videoUrl = "https://sample-videos.com/zip/10/mp4/SampleVideo_1280x720_1mb.mp4",
                thumbnailUrl = "",
                caption = "Amazing sunset view! 🌅 #sunset #nature #beautiful",
                likesCount = 1250,
                commentsCount = 89,
                sharesCount = 45,
                isLiked = false,
                isFollowing = false,
                createdAt = System.currentTimeMillis()
            ),
            Video(
                id = "2",
                userId = "user2",
                username = "sara_dancer",
                userAvatar = "",
                videoUrl = "https://sample-videos.com/zip/10/mp4/SampleVideo_1280x720_2mb.mp4",
                thumbnailUrl = "",
                caption = "New dance moves! What do you think? 💃 #dance #trending #viral",
                likesCount = 2340,
                commentsCount = 156,
                sharesCount = 78,
                isLiked = true,
                isFollowing = true,
                createdAt = System.currentTimeMillis() - 3600000
            ),
            Video(
                id = "3",
                userId = "user3",
                username = "cooking_master",
                userAvatar = "",
                videoUrl = "https://sample-videos.com/zip/10/mp4/SampleVideo_1280x720_5mb.mp4",
                thumbnailUrl = "",
                caption = "Quick and easy pasta recipe! 🍝 #cooking #food #recipe",
                likesCount = 890,
                commentsCount = 67,
                sharesCount = 23,
                isLiked = false,
                isFollowing = false,
                createdAt = System.currentTimeMillis() - 7200000
            )
        )
        
        videoList.clear()
        videoList.addAll(sampleVideos)
        videoAdapter.notifyDataSetChanged()
        
        // Auto-play first video
        if (videoList.isNotEmpty()) {
            binding.recyclerViewVideos.post {
                videoAdapter.playVideoAt(0)
            }
        }
    }
    
    private fun handleVideoAction(video: Video, action: String) {
        when (action) {
            "like" -> {
                // Handle like action
                video.isLiked = !video.isLiked
                if (video.isLiked) {
                    video.likesCount++
                } else {
                    video.likesCount--
                }
                videoAdapter.notifyDataSetChanged()
            }
            "comment" -> {
                // Handle comment action - open comments bottom sheet
                // TODO: Implement comments functionality
            }
            "share" -> {
                // Handle share action
                // TODO: Implement share functionality
            }
            "follow" -> {
                // Handle follow action
                video.isFollowing = !video.isFollowing
                videoAdapter.notifyDataSetChanged()
            }
            "profile" -> {
                // Handle profile click - navigate to user profile
                // TODO: Implement profile navigation
            }
        }
    }
    
    override fun onPause() {
        super.onPause()
        videoAdapter.pauseAllVideos()
    }
    
    override fun onResume() {
        super.onResume()
        val layoutManager = binding.recyclerViewVideos.layoutManager as LinearLayoutManager
        val position = layoutManager.findFirstCompletelyVisibleItemPosition()
        if (position != RecyclerView.NO_POSITION) {
            videoAdapter.playVideoAt(position)
        }
    }
}