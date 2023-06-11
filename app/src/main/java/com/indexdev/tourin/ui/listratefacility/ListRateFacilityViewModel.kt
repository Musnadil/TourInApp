package com.indexdev.tourin.ui.listratefacility

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.indexdev.tourin.data.Repository
import com.indexdev.tourin.data.api.Resource
import com.indexdev.tourin.data.model.response.ResponseListRateFacility
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ListRateFacilityViewModel @Inject constructor(private val repository: Repository) :
    ViewModel() {
    private val _rateFacilityList: MutableLiveData<Resource<ResponseListRateFacility>> =
        MutableLiveData()
    val rateFacilityList: LiveData<Resource<ResponseListRateFacility>> get() = _rateFacilityList

    fun getRateFacilityList(id: Int) {
        viewModelScope.launch {
            _rateFacilityList.postValue(Resource.loading())
            try {
                _rateFacilityList.postValue(Resource.success(repository.getListRateFacilityByUser(id)))
            } catch (e: Exception) {
                _rateFacilityList.postValue(Resource.error(e.localizedMessage ?: "error occurred"))
            }
        }
    }
}