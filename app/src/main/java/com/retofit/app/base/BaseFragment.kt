package com.retofit.app.base

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.os.Bundle
import android.view.*
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment

abstract class BaseFragment<DB : ViewDataBinding> : Fragment(){
    open lateinit var binding: DB
    var baseActivity: BaseActivity? = null
    protected abstract fun initViewBinding()

    @LayoutRes
    abstract fun getLayoutRes(): Int


    private fun init(
        inflater: LayoutInflater,
        container: ViewGroup?
    ) {
        binding = DataBindingUtil.inflate(inflater, getLayoutRes(), container, false)
        initViewBinding()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        init(inflater, container)
        super.onCreateView(inflater, container, savedInstanceState)
        binding.lifecycleOwner = this
        return binding.root
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is BaseActivity) {
            baseActivity = context
        }
    }

     fun isNetworkAvailable(context: Context): Boolean {
         val connectivityManager =
             context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
         if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
             val nw = connectivityManager.activeNetwork ?: return false
             val actNw = connectivityManager.getNetworkCapabilities(nw) ?: return false
             return when {
                 actNw.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
                 actNw.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
                 actNw.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
                 actNw.hasTransport(NetworkCapabilities.TRANSPORT_BLUETOOTH) -> true
                 else -> false
             }
         } else {
             return connectivityManager.activeNetworkInfo?.isConnected ?: false
         }
    }

}