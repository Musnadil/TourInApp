package com.indexdev.tourin.ui.auth

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.indexdev.tourin.data.Repository
import com.indexdev.tourin.data.api.Resource
import com.indexdev.tourin.data.model.request.RegisterRequest
import com.indexdev.tourin.data.model.response.ResponseRegister
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(private val repository: Repository):ViewModel() {
    // Register
    private val _register : MutableLiveData<Resource<ResponseRegister>> = MutableLiveData()
    val register : LiveData<Resource<ResponseRegister>> get() = _register

    fun authRegister(registerRequest: RegisterRequest){
        viewModelScope.launch {
            _register.postValue(Resource.loading())
            try {
                _register.postValue(Resource.success(repository.authRegister(registerRequest)))
            }catch (e:Exception){
                _register.postValue(Resource.error(e.message?:"Error"))
            }
        }
    }

    // Login
}