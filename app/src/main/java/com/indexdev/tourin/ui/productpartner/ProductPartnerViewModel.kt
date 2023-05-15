package com.indexdev.tourin.ui.productpartner

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.indexdev.tourin.data.Repository
import com.indexdev.tourin.data.api.Resource
import com.indexdev.tourin.data.model.response.ResponseUserMitraById
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProductPartnerViewModel @Inject constructor(private val repository: Repository) :
    ViewModel() {
    private val _responseUserPartnerById: MutableLiveData<Resource<ResponseUserMitraById>> =
        MutableLiveData()
    val responseUserPartnerById: LiveData<Resource<ResponseUserMitraById>> get() = _responseUserPartnerById

    fun getUserPartnerById(id: Int) {
        viewModelScope.launch {
            _responseUserPartnerById.postValue(Resource.loading())
            try {
                _responseUserPartnerById.postValue(Resource.success(repository.getUserPartnerById(id)))
            } catch (e: Exception) {
                _responseUserPartnerById.postValue(
                    Resource.error(
                        e.localizedMessage ?: "Error Occurred"
                    )
                )
            }
        }
    }
}