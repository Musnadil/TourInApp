package com.indexdev.tourin.ui.newpassword

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.indexdev.tourin.data.Repository
import com.indexdev.tourin.data.api.Resource
import com.indexdev.tourin.data.model.request.RequestNewPassword
import com.indexdev.tourin.data.model.response.ResponseNewPassword
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NewPasswordViewModel @Inject constructor(private val repository: Repository) : ViewModel() {
    private val _responseNewPassword = MutableLiveData<Resource<ResponseNewPassword>>()
    val responseNewPassword: LiveData<Resource<ResponseNewPassword>> get() = _responseNewPassword

    fun newPassword(requestNewPassword: RequestNewPassword) {
        viewModelScope.launch {
            _responseNewPassword.postValue(Resource.loading())
            try {
                _responseNewPassword.postValue(
                    Resource.success(
                        repository.newPassword(
                            requestNewPassword
                        )
                    )
                )
            } catch (e: Exception) {
                _responseNewPassword.postValue(
                    (Resource.error(
                        e.localizedMessage ?: "Error Occurred"
                    ))
                )
            }
        }
    }

}