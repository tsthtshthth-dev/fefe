package com.pix.app.ui.search

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.pix.app.R
import com.pix.app.adapters.DiscoverAdapter
import com.pix.app.databinding.ActivitySearchBinding
import com.pix.app.models.HashTag
import com.pix.app.models.User
import com.pix.app.models.Video
import com.pix.app.repository.UserRepository
import com.pix.app.repository.VideoRepository
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SearchActivity : AppCompatActivity() {
    
    private lateinit var binding: ActivitySearchBinding
    private lateinit var adapter: DiscoverAdapter
    private lateinit var userRepository: UserRepository
    private lateinit var videoRepository: VideoRepository
    
    private var searchJob: Job? = null
    private var currentQuery = ""
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySearchBinding.inflate(layoutInflater)
        setContentView(binding.root)
        
        userRepository = UserRepository(this)
        videoRepository = VideoRepository(this)
        
        setupUI()
        setupSearch()
        loadTrendingContent()
    }
    
    private fun setupUI() {
        // Setup toolbar
        binding.toolbar.setNavigationOnClickListener {
            finish()
        }
        
        // Setup RecyclerView
        adapter = DiscoverAdapter { item ->
            when (item) {
                is HashTag -> {
                    // Handle hashtag click
                    searchHashtag(item.name)
                }
                is User -> {
                    // Handle user click - navigate to profile
                    // TODO: Navigate to user profile
                }
                is Video -> {
                    // Handle video click - play video
                    // TODO: Navigate to video player
                }
            }
        }
        
        binding.recyclerView.apply {
            layoutManager = LinearLayoutManager(this@SearchActivity)
            adapter = this@SearchActivity.adapter
        }
        
        // Focus on search input
        binding.editTextSearch.requestFocus()
    }
    
    private fun setupSearch() {
        binding.editTextSearch.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            
            override fun afterTextChanged(s: Editable?) {
                val query = s?.toString()?.trim() ?: ""
                if (query != currentQuery) {
                    currentQuery = query
                    performSearch(query)
                }
            }
        })
        
        binding.buttonClear.setOnClickListener {
            binding.editTextSearch.text?.clear()
        }
    }
    
    private fun performSearch(query: String) {
        // Cancel previous search
        searchJob?.cancel()
        
        if (query.isEmpty()) {
            loadTrendingContent()
            return
        }
        
        // Show loading
        binding.progressBar.visibility = View.VISIBLE
        binding.textViewNoResults.visibility = View.GONE
        
        // Debounce search
        searchJob = lifecycleScope.launch {
            delay(300) // Wait 300ms before searching
            
            try {
                val results = mutableListOf<Any>()
                
                // Search users
                val users = userRepository.searchUsers(query)
                results.addAll(users)
                
                // Search videos
                val videos = videoRepository.searchVideos(query)
                results.addAll(videos)
                
                // Search hashtags (mock data for now)
                if (query.startsWith("#")) {
                    val hashtags = searchHashtags(query.removePrefix("#"))
                    results.addAll(hashtags)
                }
                
                // Update UI
                binding.progressBar.visibility = View.GONE
                
                if (results.isEmpty()) {
                    binding.textViewNoResults.visibility = View.VISIBLE
                    binding.textViewNoResults.text = getString(R.string.no_results_found, query)
                } else {
                    binding.textViewNoResults.visibility = View.GONE
                }
                
                adapter.updateItems(results)
                
            } catch (e: Exception) {
                binding.progressBar.visibility = View.GONE
                binding.textViewNoResults.visibility = View.VISIBLE
                binding.textViewNoResults.text = getString(R.string.search_error)
            }
        }
    }
    
    private fun loadTrendingContent() {
        binding.progressBar.visibility = View.VISIBLE
        binding.textViewNoResults.visibility = View.GONE
        
        lifecycleScope.launch {
            try {
                val trendingItems = mutableListOf<Any>()
                
                // Load trending hashtags
                val trendingHashtags = getTrendingHashtags()
                trendingItems.addAll(trendingHashtags)
                
                // Load popular users
                val popularUsers = userRepository.getPopularUsers(10)
                trendingItems.addAll(popularUsers)
                
                // Load trending videos
                val trendingVideos = videoRepository.getTrendingVideos(10)
                trendingItems.addAll(trendingVideos)
                
                binding.progressBar.visibility = View.GONE
                adapter.updateItems(trendingItems)
                
            } catch (e: Exception) {
                binding.progressBar.visibility = View.GONE
                binding.textViewNoResults.visibility = View.VISIBLE
                binding.textViewNoResults.text = getString(R.string.loading_error)
            }
        }
    }
    
    private fun searchHashtag(hashtag: String) {
        binding.editTextSearch.setText("#$hashtag")
        performSearch("#$hashtag")
    }
    
    private suspend fun searchHashtags(query: String): List<HashTag> {
        // Mock hashtag search - replace with actual implementation
        return listOf(
            HashTag("1", "#$query", 1500, 50000),
            HashTag("2", "#${query}challenge", 800, 25000),
            HashTag("3", "#${query}trend", 600, 18000)
        ).filter { it.name.contains(query, ignoreCase = true) }
    }
    
    private suspend fun getTrendingHashtags(): List<HashTag> {
        // Mock trending hashtags - replace with actual implementation
        return listOf(
            HashTag("1", "#fyp", 15000, 500000),
            HashTag("2", "#viral", 12000, 450000),
            HashTag("3", "#trending", 10000, 380000),
            HashTag("4", "#dance", 8500, 320000),
            HashTag("5", "#comedy", 7200, 280000),
            HashTag("6", "#music", 6800, 250000),
            HashTag("7", "#art", 5500, 200000),
            HashTag("8", "#food", 4800, 180000)
        )
    }
}