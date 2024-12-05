package com.retofit.app.new_list_detail


import android.annotation.SuppressLint
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.Menu
import android.view.View
import android.widget.PopupMenu
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.AppCompatButton
import androidx.core.os.bundleOf
import androidx.core.widget.NestedScrollView
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.retofit.app.R
import com.retofit.app.api_retrofit.Constants
import com.retofit.app.base.BaseAdapter
import com.retofit.app.base.BaseFragment
import com.retofit.app.data_model_class.Articles
import com.retofit.app.data_model_class.JsonDataNewList
import com.retofit.app.databinding.FragmentListBinding
import com.retofit.app.localData.SharedPreferencesUtil
import com.retofit.app.network.retrofit.DataResult
import com.retofit.app.network.retrofit.observeEvent
import com.retofit.app.view_model.NewsViewModel
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class ListFragment : BaseFragment<FragmentListBinding>(), BaseAdapter.OnItemClick {
    override fun getLayoutRes() = R.layout.fragment_list
    private var sharedPreferencesUtil: SharedPreferencesUtil? = null
    private var adapter: NewsAdapter? = null
    private var showDataList: ArrayList<Articles> = arrayListOf()
    private var dataList: JsonDataNewList? = null
    private val updateHandler = Handler(Looper.myLooper()!!)
    private var myListBookmark: ArrayList<Articles> = arrayListOf()
    private val viewModel: NewsViewModel by viewModels()

    @SuppressLint("NotifyDataSetChanged")
    override fun initViewBinding() {
        sharedPreferencesUtil = SharedPreferencesUtil(requireActivity().applicationContext)
        myListBookmark = sharedPreferencesUtil!!.list
        binding.bookmarkIV.setOnClickListener {
            findNavController().navigate(
                R.id.action_listFragment_to_bookmarkFragment
            )
        }
        binding.fab.setOnClickListener {
            showCustomDialog()
        }
        viewModel.newsResponseLiveData.observeEvent(this) { result ->
            when (result) {
                is DataResult.Failure -> {
                    baseActivity!!.hideLoading()
                    Snackbar.make(
                        binding.idNestedSV,
                        result.message.toString(),
                        Snackbar.LENGTH_SHORT
                    ).show()
                    Log.e("catch_exception", "${result.message.toString()}")
                }

                is DataResult.Loading -> {
                    baseActivity!!.hideLoading()
                }

                is DataResult.Success -> {
                    baseActivity!!.hideLoading()
                    dataList = result.data
                    Log.e("ApiClient", "dataList response: ${dataList!!.articles.size}")
                    loadList()
                }

                else -> {}
            }
        }
        setAdapter()
        initScrollListener()
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun showCustomDialog() {
        val dialogView = LayoutInflater.from(baseActivity!!).inflate(R.layout.dialog_custom, null)
        val dialog = AlertDialog.Builder(baseActivity!!)
            .setView(dialogView)
            .create()
        val buttonAscending: AppCompatButton = dialogView.findViewById(R.id.buttonAscending)
        val buttonDescending: AppCompatButton = dialogView.findViewById(R.id.buttonDescending)
        buttonAscending.setOnClickListener {
            showDataList.sortBy { it.index }
            adapter?.notifyDataSetChanged()
            dialog.dismiss()
        }

        buttonDescending.setOnClickListener {
            showDataList.sortBy { it.index }
            showDataList.reverse()
            adapter?.notifyDataSetChanged()
            dialog.dismiss()
        }

        // Show the dialog
        dialog.show()
    }

    override fun onResume() {
        super.onResume()
        if (isNetworkAvailable(baseActivity!!) && !baseActivity!!.isAPICalled) {
            binding.loadData.visibility = View.VISIBLE
            baseActivity!!.isAPICalled = true
            viewModel.getNewsList("tesla", "2024-07-22", Constants.SORT_BY, Constants.BASE_KEY)
        } else {
            binding.loadData.visibility = View.GONE
            if (!baseActivity!!.isAPICalled) {
                Snackbar.make(
                    binding.idNestedSV,
                    getString(R.string.no_internet_connection_found_please_try_again_later),
                    Snackbar.LENGTH_SHORT
                ).show()
            }
        }

    }

    private fun setAdapter() {
        adapter = NewsAdapter(baseActivity!!, showDataList)
        binding.listRl.adapter = adapter
        adapter!!.setOnItemClickListener(this)
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun loadList() {

        dataList?.articles?.forEachIndexed { j, data ->
            val article = data
            article.index = j
            article.isBookmarked = false
            if (myListBookmark.size > 0) {
                myListBookmark.forEachIndexed { index, data ->
                    if (data.index!!.toInt() == j) {
                        article.isBookmarked = true
                    }

                }
            }
            showDataList.add(article)
        }
        binding.progressBar.visibility = View.GONE
        binding.listRl.post {
            binding.loadData.visibility = View.GONE
            adapter?.notifyDataSetChanged()
        }
    }

    private fun initScrollListener() {

        binding.idNestedSV.setOnScrollChangeListener(NestedScrollView.OnScrollChangeListener { v, _, scrollY: Int, _, _ ->
            if (scrollY == v.getChildAt(0).measuredHeight - v.measuredHeight) {
                val linearLayoutManager = binding.listRl.layoutManager as LinearLayoutManager
                if (linearLayoutManager.findLastVisibleItemPosition() < (dataList?.articles?.size?.minus(
                        1
                    ) ?: 0)
                ) {
                    Log.e("catch_exception", "load completely")
                    binding.progressBar.visibility = View.VISIBLE
                    val runnable = Runnable {
                        loadList()
                    }
                    updateHandler.postDelayed(runnable, 2000)
                } else {
                    binding.progressBar.visibility = View.GONE
                }
            }
        })
    }

    override fun onItemClick(vararg items: Any) {
        if (items[1] as String == "detail") {
            findNavController().navigate(
                R.id.action_listFragment_to_detailFragment,
                bundleOf("detail_info" to items[0] as Articles)
            )
        } else {
            val indexValue = items[4] as Articles
            val position = items[3] as Int
            val isBookMarked = items[2] as Boolean
            if (isBookMarked) {
                myListBookmark.add(indexValue)
            } else {
                var indexPosition = 0
                for ((index, data) in myListBookmark.withIndex()) {
                    if (data.index!!.toInt() == indexValue.index) {
                        indexPosition = index
                        break  // Breaks out of the loop
                    }
                }
                myListBookmark.removeAt(indexPosition)
            }
            sharedPreferencesUtil?.saveList(myListBookmark)
            showDataList[position].isBookmarked = isBookMarked
            Log.e("catch_exception", "value: ${showDataList[position]}")
            adapter?.notifyItemChanged(position, showDataList[position])
        }

    }

}