package  com.retofit.app.data.error.mapper

import android.content.Context
import com.retofit.app.data.error.NETWORK_ERROR
import com.retofit.app.data.error.NO_INTERNET_CONNECTION
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class ErrorMapper @Inject constructor(@ApplicationContext val context: Context) :
    ErrorMapperSource {

    override fun getErrorString(errorId: Int): String {
        return context.getString(errorId)
    }

    override val errorsMap: Map<Int, String>
        get() = mapOf(
            Pair(NO_INTERNET_CONNECTION, "No internet Connection"),
            Pair(NETWORK_ERROR, "No Network"),
        ).withDefault { "No Network" }
}
