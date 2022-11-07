package com.indexdev.tourin.ui.edit

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.indexdev.tourin.data.Repository
import com.indexdev.tourin.data.api.Resource
import com.indexdev.tourin.data.model.request.UpdateUserRequest
import com.indexdev.tourin.data.model.response.ResponseUpdateUsername
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.toRequestBody
import javax.inject.Inject

@HiltViewModel
class EditViewModel @Inject constructor(private val repository: Repository) : ViewModel() {
    private var _username: MutableLiveData<Resource<ResponseUpdateUsername>> = MutableLiveData()
    val username: LiveData<Resource<ResponseUpdateUsername>> get() = _username

    fun editUsername(id:Int, updateUserRequest: UpdateUserRequest) {
//        val chngUsername =newUsername.toRequestBody("text/plain".toMediaType())
        viewModelScope.launch {
            _username.postValue(Resource.loading())
            try {
                _username.postValue(Resource.success(repository.editUsername(id, updateUserRequest)))
            } catch (e: Exception) {
                _username.postValue(Resource.error(e.message ?: "Error"))
            }
        }
    }
}