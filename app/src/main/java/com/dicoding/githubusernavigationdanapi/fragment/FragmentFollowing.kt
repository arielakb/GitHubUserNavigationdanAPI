package com.dicoding.githubusernavigationdanapi.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.githubusernavigationdanapi.adapter.SectionPagerAdapter.Companion.ARGS_USERNAME
import com.dicoding.githubusernavigationdanapi.adapter.UserAdapter
import com.dicoding.githubusernavigationdanapi.databinding.FragmentFollowingActivityBinding
import com.dicoding.githubusernavigationdanapi.model.UserMain
import com.dicoding.githubusernavigationdanapi.ui.DetailActivity
import com.dicoding.githubusernavigationdanapi.viewModel.FollowingViewModel

class FragmentFollowing : Fragment() {

    private var _binding: FragmentFollowingActivityBinding? = null
    private val binding get() = _binding!!
    private val followingViewModel by viewModels<FollowingViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?): View {
        _binding = FragmentFollowingActivityBinding.inflate(layoutInflater, container, false)

        followingViewModel.following.observe(viewLifecycleOwner) { following ->
            if (following == null) {
                val username = arguments?.getString(ARGS_USERNAME) ?: ""
                followingViewModel.getUserFollowing(username)
            } else {
                showFollowing(following)
            }
        }

        followingViewModel.isLoading.observe(viewLifecycleOwner) {
            showLoading(it)
        }

        return binding.root
    }

    override fun onDestroy() {
        _binding = null
        super.onDestroy()
    }

    private fun showFollowing(users: ArrayList<UserMain>) {
        if (users.size > 0) {
            val linearLayoutManager = LinearLayoutManager(activity)
            val listAdapter = UserAdapter(users)

            binding.rvUserFollowing.apply {
                layoutManager = linearLayoutManager
                adapter = listAdapter
                setHasFixedSize(true)
            }

            listAdapter.setOnItemClickCallback(object :
                UserAdapter.OnItemClickCallback {
                override fun onItemClicked(user: UserMain) {
                    goToDetailUser(user)
                }

            })
        }
    }

    private fun showLoading(isLoading: Boolean) {
        if (isLoading) binding.pbUserFollowing.visibility = View.VISIBLE
        else binding.pbUserFollowing.visibility = View.GONE
    }

    private fun goToDetailUser(user: UserMain) {
        Intent(activity, DetailActivity::class.java).apply {
            putExtra(DetailActivity.EXTRA_DETAIL, user.login)
        }.also {
            startActivity(it)
        }
    }
}
