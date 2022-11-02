package com.indexdev.tourin.ui.rating

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.indexdev.tourin.data.Repository
import com.indexdev.tourin.data.api.Resource
import com.indexdev.tourin.data.model.request.RateRequest
import com.indexdev.tourin.data.model.response.ResponseRate
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RateViewModel @Inject constructor(private val repository: Repository) : ViewModel() {
    //rate
    private val _rate: MutableLiveData<Resource<ResponseRate>> = MutableLiveData()
    val rate: LiveData<Resource<ResponseRate>> get() = _rate

    fun postRate(rateRequest: RateRequest) {
        viewModelScope.launch {
            _rate.postValue(Resource.loading())
            try {
                _rate.postValue(Resource.success(repository.postRate(rateRequest)))
            } catch (e: Exception) {
                _rate.postValue(Resource.error(e.message ?: "Error"))
            }
        }
    }
}