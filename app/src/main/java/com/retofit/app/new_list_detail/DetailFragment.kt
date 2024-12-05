package com.retofit.app.new_list_detail

import android.util.Log
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.retofit.app.R
import com.retofit.app.base.BaseFragment
import com.retofit.app.data_model_class.Articles
import com.retofit.app.databinding.FragmentDetailBinding
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class DetailFragment : BaseFragment<FragmentDetailBinding>() {
    override fun getLayoutRes() = R.layout.fragment_detail
    private var detail: Articles? = null
    override fun initViewBinding() {
        binding.backIV.setOnClickListener {
            findNavController().popBackStack()
        }

        if (requireArguments().containsKey("detail_info")) {
            detail = requireArguments().getParcelable("detail_info")!!
            binding.titleTxt.text =
                baseActivity!!.setData(detail?.title.toString(), "Title")
            binding.detailTxt.text =
                baseActivity!!.setData(detail?.description.toString(), "Detail")
            binding.contentTxt.text =
                baseActivity!!.setData(detail?.content.toString(), "Content")

            if (detail!!.urlToImage != null && detail!!.urlToImage!!.isNotEmpty()) {
                Log.e("catch_exception","image: ${detail!!.urlToImage}")
                Glide.with(this)
                    .load(detail!!.urlToImage)
                    .into(binding.ivNews)
            }

        }
    }
}
