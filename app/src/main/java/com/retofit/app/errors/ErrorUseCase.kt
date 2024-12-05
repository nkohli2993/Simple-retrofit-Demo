package com.retofit.app.errors


interface ErrorUseCase {
    fun getError(errorCode: Int): Error
}
