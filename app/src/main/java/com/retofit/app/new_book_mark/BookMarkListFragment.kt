package com.retofit.app.new_book_mark

import android.util.Log
import androidx.core.os.bundleOf
import androidx.navigation.fragment.findNavController
import com.retofit.app.R
import com.retofit.app.base.BaseAdapter
import com.retofit.app.base.BaseFragment
import com.retofit.app.data_model_class.Articles
import com.retofit.app.databinding.FragmentBookMarkListBinding
import com.retofit.app.localData.SharedPreferencesUtil


class BookMarkListFragment : BaseFragment<FragmentBookMarkListBinding>(), BaseAdapter.OnItemClick {
    override fun getLayoutRes() = R.layout.fragment_book_mark_list
    private var sharedPreferencesUtil: SharedPreferencesUtil? = null
    private var adapter: BookmarkAdapter? = null
    private var myListBookmark: ArrayList<Articles> = arrayListOf()
    override fun initViewBinding() {
        sharedPreferencesUtil = SharedPreferencesUtil(requireActivity().applicationContext)
        myListBookmark = sharedPreferencesUtil!!.list
        binding.backBookmarkIV.setOnClickListener {
            findNavController().popBackStack()
        }

        setAdapter()
    }


    private fun setAdapter() {
        adapter = BookmarkAdapter(baseActivity!!, myListBookmark)
        binding.listRl.adapter = adapter
        adapter!!.setOnItemClickListener(this)
    }

    override fun onItemClick(vararg items: Any) {
        findNavController().navigate(
            R.id.action_bookmarkFragment_to_detailFragment,
            bundleOf("detail_info" to items[0] as Articles)
        )
    }
}