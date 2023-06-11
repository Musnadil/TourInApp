package com.indexdev.tourin.ui.listratefacility.ratefacility

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.indexdev.tourin.data.Repository
import com.indexdev.tourin.data.api.Resource
import com.indexdev.tourin.data.model.request.RateFacilityRequest
import com.indexdev.tourin.data.model.response.ResponseRateFacility
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RateFacilityDialogViewModel @Inject constructor(private val repository: Repository) :
    ViewModel() {
    private val _responseRateFacility: MutableLiveData<Resource<ResponseRateFacility>> =
        MutableLiveData()
    val responseRateFacility: LiveData<Resource<ResponseRateFacility>> get() = _responseRateFacility

    fun rateFacility(id: Int, request: RateFacilityRequest) {
        viewModelScope.launch {
            _responseRateFacility.postValue(Resource.loading())
            try {
                _responseRateFacility.postValue(
                    Resource.success(
                        repository.sendRatingFacility(
                            id,
                            request
                        )
                    )
                )
            } catch (e: Exception) {
                _responseRateFacility.postValue(
                    Resource.error(
                        e.localizedMessage ?: "Error Occurred"
                    )
                )
            }
        }
    }
}