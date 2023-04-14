package com.dicoding.githubusernavigationdanapi.ui

import android.app.SearchManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.githubusernavigationdanapi.R
import com.dicoding.githubusernavigationdanapi.adapter.UserAdapter
import com.dicoding.githubusernavigationdanapi.databinding.ActivityMainBinding
import com.dicoding.githubusernavigationdanapi.model.UserMain
import com.dicoding.githubusernavigationdanapi.ui.DetailActivity.Companion.EXTRA_DETAIL
import com.dicoding.githubusernavigationdanapi.viewModel.MainViewModel


class MainActivity : AppCompatActivity() {

    private var _mainBinding: ActivityMainBinding? = null
    private val binding get() = _mainBinding!!
    private lateinit var userAdapter: UserAdapter
    private val mainViewModel by viewModels<MainViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _mainBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        mainViewModel.userMain.observe(this) {
            showSearchingResult(it)
        }

        mainViewModel.isLoading.observe(this) {
            showLoading(it)
        }

        mainViewModel.isError.observe(this) { error ->
            if (error) errorOccurred()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.search_menu, menu)

        val searchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager
        val searchView = menu.findItem(R.id.search).actionView as androidx.appcompat.widget.SearchView

            searchView.setSearchableInfo(searchManager.getSearchableInfo(componentName))
            searchView.queryHint = getString(R.string.github_username)
            searchView.setOnQueryTextListener(object : androidx.appcompat.widget.SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String?): Boolean {
                    mainViewModel.findUser(query ?: "")
                    searchView.clearFocus()
                    return true
                }

                override fun onQueryTextChange(newText: String?): Boolean {
                    return false
                }

            })
        return true
    }

    override fun onDestroy() {
        _mainBinding = null
        super.onDestroy()
    }

    private fun errorOccurred() {
        Toast.makeText(this@MainActivity, "An Error is Occurred", Toast.LENGTH_SHORT).show()
    }

    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            binding.progressBar.visibility = View.VISIBLE
        } else {
            binding.progressBar.visibility = View.GONE
        }
    }

    private fun showSearchingResult(user: ArrayList<UserMain>) {
        userAdapter = UserAdapter(user)
        binding.rvUser.apply {
            layoutManager = LinearLayoutManager(this@MainActivity)
            adapter = userAdapter
            setHasFixedSize(true)
        }

        userAdapter.setOnItemClickCallback(object : UserAdapter.OnItemClickCallback {
            override fun onItemClicked(user: UserMain) {
                goToDetailUser(user)
            }

        })

    }

        private fun goToDetailUser(user: UserMain) {
            Intent(this@MainActivity, DetailActivity::class.java).apply {
                putExtra(EXTRA_DETAIL, user.login)
            }.also {
                startActivity(it)
            }
    }
}


