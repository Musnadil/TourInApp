package com.indexdev.tourin.ui.maps

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.indexdev.tourin.data.Repository
import com.indexdev.tourin.data.api.Resource
import com.indexdev.tourin.data.model.response.ResponsePOI
import com.indexdev.tourin.data.model.response.ResponseUserMitra
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MapsViewModel @Inject constructor(private val repository: Repository) : ViewModel() {
    private val _poiList: MutableLiveData<Resource<List<ResponsePOI>>> = MutableLiveData()
    val poiList: LiveData<Resource<List<ResponsePOI>>> get() = _poiList

    private val _poiListMitra: MutableLiveData<Resource<List<ResponseUserMitra>>> = MutableLiveData()
    val poiListMitra: LiveData<Resource<List<ResponseUserMitra>>> get() = _poiListMitra

    fun getPoiList(id: Int) {
        viewModelScope.launch {
            _poiList.postValue(Resource.loading())
            try {
                _poiList.postValue(Resource.success(repository.getPoiById(id)))
            } catch (e: Exception) {
                _poiList.postValue(Resource.error(e.localizedMessage ?: "error occurred"))
            }
        }
    }

    fun getPoiListMitra() {
        viewModelScope.launch {
            _poiListMitra.postValue(Resource.loading())
            try {
                _poiListMitra.postValue(Resource.success(repository.getAllUserMitra()))
            } catch (e: Exception) {
                _poiListMitra.postValue(Resource.error(e.localizedMessage ?: "error occurred"))
            }
        }
    }
}