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
import com.dicoding.githubusernavigationdanapi.databinding.FragmentFollowersActivityBinding
import com.dicoding.githubusernavigationdanapi.model.UserMain
import com.dicoding.githubusernavigationdanapi.ui.DetailActivity
import com.dicoding.githubusernavigationdanapi.viewModel.FollowersViewModel

class FragmentFollowers : Fragment() {

    private var _binding: FragmentFollowersActivityBinding? = null
    private val binding get() = _binding!!
    private val followersViewModel by viewModels<FollowersViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?): View {
        _binding = FragmentFollowersActivityBinding.inflate(layoutInflater, container, false)

        followersViewModel.followers.observe(viewLifecycleOwner) { followers ->
            if (followers == null) {
                val username = arguments?.getString(ARGS_USERNAME) ?: ""
                followersViewModel.getUserFollowers(username)
            } else {
                showFollowers(followers)
            }
        }

        followersViewModel.isLoading.observe(viewLifecycleOwner) {
            showLoading(it)
        }

        return binding.root
    }

    override fun onDestroy() {
        _binding = null
        super.onDestroy()
    }

    private fun showFollowers(users: ArrayList<UserMain>) {
        if (users.size > 0) {
            val linearLayoutManager = LinearLayoutManager(activity)
            val listAdapter = UserAdapter(users)

            binding.rvUserFollowers.apply {
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
        if (isLoading) binding.pbUserFollowers.visibility = View.VISIBLE
        else binding.pbUserFollowers.visibility = View.GONE
    }

    private fun goToDetailUser(user: UserMain) {
        Intent(activity, DetailActivity::class.java).apply {
            putExtra(DetailActivity.EXTRA_DETAIL, user.login)
        }.also {
            startActivity(it)
        }
    }
}