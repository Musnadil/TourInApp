package com.indexdev.tourin.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.indexdev.tourin.data.Repository
import com.indexdev.tourin.data.api.Resource
import com.indexdev.tourin.data.model.response.ResponseTourList
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(private val repository: Repository): ViewModel() {
    private val _tourList : MutableLiveData<Resource<List<ResponseTourList>>> = MutableLiveData()
    val tourList : LiveData<Resource<List<ResponseTourList>>> get() =  _tourList

    fun getTourList(){
        viewModelScope.launch {
            _tourList.postValue(Resource.loading())
            try {
                _tourList.postValue(Resource.success(repository.getTourList()))
            } catch (e:Exception){
                _tourList.postValue(Resource.error(e.localizedMessage?:"error occurred"))
            }
        }
    }
}