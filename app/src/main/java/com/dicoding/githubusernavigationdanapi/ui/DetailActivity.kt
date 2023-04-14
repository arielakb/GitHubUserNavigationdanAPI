package com.dicoding.githubusernavigationdanapi.ui


import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.dicoding.githubusernavigationdanapi.R
import com.dicoding.githubusernavigationdanapi.adapter.SectionPagerAdapter
import com.dicoding.githubusernavigationdanapi.databinding.ActivityDetailBinding
import com.dicoding.githubusernavigationdanapi.model.DetailResponse
import com.dicoding.githubusernavigationdanapi.viewModel.DetailViewModel
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator


class DetailActivity : AppCompatActivity() {

    private var _detailBinding: ActivityDetailBinding? = null
    private val binding get() = _detailBinding!!
    private var username: String? = null
    private val detailViewModel by viewModels<DetailViewModel>()

    companion object {
        const val EXTRA_DETAIL = "extra_detail"
        private val TAB_TITLES = intArrayOf(
            R.string.followers,
            R.string.following
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        _detailBinding = ActivityDetailBinding.inflate(layoutInflater)
        username = intent.extras?.getString(EXTRA_DETAIL) as String

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        setViewPager()
        setContentView(binding.root)

        detailViewModel.userDetail.observe(this) { user ->
            if (user != null) {
                parseUserDetail(user)
            }
        }

        detailViewModel.callCounter.observe(this) { counter ->
            if (counter < 1) detailViewModel.getUserDetail(username!!)
        }

    }
    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    private fun setViewPager() {
        val viewPager: ViewPager2 = binding.viewPager
        val tabs: TabLayout = binding.tabs

        viewPager.adapter = SectionPagerAdapter(this, username!!)

        TabLayoutMediator(tabs, viewPager) { tab, position ->
            tab.text = resources.getString(TAB_TITLES[position])
        }.attach()
    }

    private fun parseUserDetail(user: DetailResponse) {
        binding.apply {
            tvDetailName.text = user.login
            tvDetailNumberOfRepos.text = user.publicRepos.toString()
            tvDetailNumberOfFollowers.text = user.followers.toString()
            tvDetailNumberOfFollowing.text = user.following.toString()
            tvDetailCompany.text = user.company
            tvDetailLocation.text = user.location

            Glide.with(this@DetailActivity)
                .load(user.avatarUrl)
                .apply(RequestOptions.circleCropTransform())
                .into(ivDetailAvatar)
        }
    }

    override fun onDestroy() {
        _detailBinding = null
        username = null

        super.onDestroy()
    }
}