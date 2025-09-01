package com.pix.app.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import com.pix.app.R
import com.pix.app.adapters.DiscoverAdapter
import com.pix.app.databinding.FragmentDiscoverBinding
import com.pix.app.models.HashTag
import com.pix.app.models.User
import com.pix.app.models.Video
import com.pix.app.ui.search.SearchActivity

class DiscoverFragment : Fragment() {
    
    private lateinit var binding: FragmentDiscoverBinding
    private lateinit var discoverAdapter: DiscoverAdapter
    private val discoverItems = mutableListOf<Any>()
    
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_discover, container, false)
        return binding.root
    }
    
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
        setupRecyclerView()
        setupSearchView()
        loadDiscoverContent()
    }
    
    private fun setupRecyclerView() {
        discoverAdapter = DiscoverAdapter(discoverItems) { item, action ->
            handleDiscoverAction(item, action)
        }
        
        binding.recyclerViewDiscover.apply {
            layoutManager = GridLayoutManager(context, 2).apply {
                spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
                    override fun getSpanSize(position: Int): Int {
                        return when (discoverItems[position]) {
                            is HashTag -> 2 // Full width for hashtags
                            is User -> 2 // Full width for users
                            else -> 1 // Half width for videos
                        }
                    }
                }
            }
            adapter = discoverAdapter
        }
    }
    
    private fun setupSearchView() {
        binding.searchView.setOnClickListener {
            val intent = Intent(requireContext(), SearchActivity::class.java)
            startActivity(intent)
        }
        
        binding.searchView.setOnQueryTextListener(object : androidx.appcompat.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                query?.let { 
                    val intent = Intent(requireContext(), SearchActivity::class.java)
                    intent.putExtra("query", it)
                    startActivity(intent)
                }
                return true
            }
            
            override fun onQueryTextChange(newText: String?): Boolean {
                if (newText.isNullOrEmpty()) {
                    loadDiscoverContent()
                } else if (newText.length > 2) {
                    performSearch(newText)
                }
                return true
            }
        })
    }
    
    private fun loadDiscoverContent() {
        // Sample trending hashtags
        val trendingHashtags = listOf(
            HashTag("#viral", 12500, 2500000),
            HashTag("#dance", 8900, 1800000),
            HashTag("#comedy", 7600, 1200000),
            HashTag("#food", 5400, 900000),
            HashTag("#travel", 4200, 750000)
        )
        
        // Sample suggested users
        val suggestedUsers = listOf(
            User(
                id = "user1",
                username = "ahmed_creator",
                fullName = "Ahmed Creator",
                email = "",
                avatar = "",
                bio = "Content creator & influencer",
                website = "",
                followersCount = 125000,
                followingCount = 890,
                videosCount = 234,
                likesCount = 1500000,
                isVerified = true,
                createdAt = System.currentTimeMillis()
            ),
            User(
                id = "user2",
                username = "sara_dancer",
                fullName = "Sara Dancer",
                email = "",
                avatar = "",
                bio = "Professional dancer 💃",
                website = "",
                followersCount = 89000,
                followingCount = 456,
                videosCount = 189,
                likesCount = 890000,
                isVerified = false,
                createdAt = System.currentTimeMillis()
            )
        )
        
        // Sample trending videos
        val trendingVideos = listOf(
            Video(
                id = "v1",
                userId = "user1",
                username = "ahmed_creator",
                userAvatar = "",
                videoUrl = "",
                thumbnailUrl = "",
                caption = "Amazing sunset! 🌅",
                likesCount = 1250,
                commentsCount = 89,
                sharesCount = 45,
                isLiked = false,
                isFollowing = false,
                createdAt = System.currentTimeMillis()
            ),
            Video(
                id = "v2",
                userId = "user2",
                username = "sara_dancer",
                userAvatar = "",
                videoUrl = "",
                thumbnailUrl = "",
                caption = "New dance moves! 💃",
                likesCount = 2340,
                commentsCount = 156,
                sharesCount = 78,
                isLiked = false,
                isFollowing = false,
                createdAt = System.currentTimeMillis()
            ),
            Video(
                id = "v3",
                userId = "user3",
                username = "cooking_master",
                userAvatar = "",
                videoUrl = "",
                thumbnailUrl = "",
                caption = "Quick pasta recipe! 🍝",
                likesCount = 890,
                commentsCount = 67,
                sharesCount = 23,
                isLiked = false,
                isFollowing = false,
                createdAt = System.currentTimeMillis()
            ),
            Video(
                id = "v4",
                userId = "user4",
                username = "travel_blogger",
                userAvatar = "",
                videoUrl = "",
                thumbnailUrl = "",
                caption = "Beautiful beach view! 🏖️",
                likesCount = 1560,
                commentsCount = 92,
                sharesCount = 34,
                isLiked = false,
                isFollowing = false,
                createdAt = System.currentTimeMillis()
            )
        )
        
        discoverItems.clear()
        discoverItems.addAll(trendingHashtags)
        discoverItems.addAll(suggestedUsers)
        discoverItems.addAll(trendingVideos)
        
        discoverAdapter.notifyDataSetChanged()
    }
    
    private fun performSearch(query: String) {
        // Simulate search results
        val searchResults = mutableListOf<Any>()
        
        // Search hashtags
        if (query.startsWith("#")) {
            searchResults.add(HashTag(query, 1250, 250000))
        }
        
        // Search users
        if (query.contains("ahmed")) {
            searchResults.add(
                User(
                    id = "search_user1",
                    username = "ahmed_creator",
                    fullName = "Ahmed Creator",
                    email = "",
                    avatar = "",
                    bio = "Content creator",
                    website = "",
                    followersCount = 125000,
                    followingCount = 890,
                    videosCount = 234,
                    likesCount = 1500000,
                    isVerified = true,
                    createdAt = System.currentTimeMillis()
                )
            )
        }
        
        // Search videos (mock results)
        searchResults.addAll(
            listOf(
                Video(
                    id = "search_v1",
                    userId = "user1",
                    username = "search_result",
                    userAvatar = "",
                    videoUrl = "",
                    thumbnailUrl = "",
                    caption = "Search result for: $query",
                    likesCount = 890,
                    commentsCount = 45,
                    sharesCount = 12,
                    isLiked = false,
                    isFollowing = false,
                    createdAt = System.currentTimeMillis()
                )
            )
        )
        
        discoverItems.clear()
        discoverItems.addAll(searchResults)
        discoverAdapter.notifyDataSetChanged()
    }
    
    private fun handleDiscoverAction(item: Any, action: String) {
        when (action) {
            "click" -> {
                when (item) {
                    is HashTag -> {
                        // Navigate to hashtag videos
                        // TODO: Implement hashtag videos screen
                    }
                    is User -> {
                        // Navigate to user profile
                        // TODO: Implement user profile navigation
                    }
                    is Video -> {
                        // Play video or navigate to video details
                        // TODO: Implement video playback
                    }
                }
            }
            "follow" -> {
                if (item is User) {
                    // Handle follow/unfollow
                    // TODO: Implement follow functionality
                }
            }
        }
    }
}