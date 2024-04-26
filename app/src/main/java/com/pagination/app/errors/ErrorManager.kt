package com.pagination.app.errors

import com.pagination.app.data.error.mapper.ErrorMapper
import javax.inject.Inject

/**
 * Created by Nikita kohli
 */


class ErrorManager @Inject constructor(private val errorMapper: ErrorMapper) : ErrorUseCase {
    override fun getError(errorCode: Int): Error {
        return Error(code = errorCode, description = errorMapper.errorsMap.getValue(errorCode))
    }
}
