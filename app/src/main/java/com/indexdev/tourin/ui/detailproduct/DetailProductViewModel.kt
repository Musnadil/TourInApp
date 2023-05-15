package com.indexdev.tourin.ui.detailproduct

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.indexdev.tourin.data.Repository
import com.indexdev.tourin.data.api.Resource
import com.indexdev.tourin.data.model.response.ResponseProdukById
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailProductViewModel @Inject constructor(private val repository: Repository) : ViewModel() {

    private val _responseProductById = MutableLiveData<Resource<ResponseProdukById>>()
    val responseProductById: LiveData<Resource<ResponseProdukById>> get() = _responseProductById

    fun getProductById(id: Int) {
        viewModelScope.launch {
            _responseProductById.postValue(Resource.loading())
            try {
                _responseProductById.postValue(Resource.success(repository.getProductById(id)))
            } catch (e: Exception) {
                _responseProductById.postValue(
                    Resource.error(
                        e.localizedMessage ?: "Error occurred"
                    )
                )
            }
        }
    }
}