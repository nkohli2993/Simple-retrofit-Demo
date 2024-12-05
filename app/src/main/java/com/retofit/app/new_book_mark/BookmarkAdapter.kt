package com.retofit.app.new_book_mark
import com.retofit.app.R
import com.retofit.app.base.BaseActivity
import com.retofit.app.base.BaseAdapter
import com.retofit.app.base.BaseViewHolder
import com.retofit.app.data_model_class.Articles
import com.retofit.app.databinding.AdapterDataBinding

class BookmarkAdapter(
    val baseActivity: BaseActivity,
    val dataList: ArrayList<Articles>
) : BaseAdapter<AdapterDataBinding>(),BaseAdapter.OnItemClick {
    override fun getLayoutRes()= R.layout.adapter_data

    override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
        val binding = holder.binding as AdapterDataBinding
        holder.setIsRecyclable(false)
        val data = dataList[position]
        binding.ivBookmark.setImageResource(R.drawable.ic_added_to_bookmark)
        binding.idTxt.text = baseActivity.setData(data.title?:"","Title")
        binding.titleTxt.text =  baseActivity.setData(data.description ?:"","Description")
        binding.root.setOnClickListener {
            onItemClick(data,"detail")
        }
    }

    override fun getItemId(position: Int): Long {
        return super.getItemId(position)
    }
    override fun getItemCount(): Int {
        return  dataList.size
    }


}