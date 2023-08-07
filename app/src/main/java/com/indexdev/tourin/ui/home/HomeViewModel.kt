package com.indexdev.tourin.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.indexdev.tourin.data.Repository
import com.indexdev.tourin.data.api.Resource
import com.indexdev.tourin.data.model.response.ResponseRecommendation
import com.indexdev.tourin.data.model.response.ResponseTourList
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(private val repository: Repository): ViewModel() {
    private val _popularTourList : MutableLiveData<Resource<List<ResponseTourList>>> = MutableLiveData()
    val popularTourList : LiveData<Resource<List<ResponseTourList>>> get() =  _popularTourList

    fun getPopularTourList(){
        viewModelScope.launch {
            _popularTourList.postValue(Resource.loading())
            try {
                _popularTourList.postValue(Resource.success(repository.getTourList()))
            } catch (e:Exception){
                _popularTourList.postValue(Resource.error(e.localizedMessage?:"error occurred"))
            }
        }
    }

    private val _allListTour : MutableLiveData<Resource<List<ResponseTourList>>> = MutableLiveData()
    val allListTour : LiveData<Resource<List<ResponseTourList>>> get() = _popularTourList

    fun getAllTourList(){
        viewModelScope.launch {
            _allListTour.postValue(Resource.loading())
            try {
                _allListTour.postValue(Resource.success(repository.getTourList()))
            }catch (e:Exception){
                _allListTour.postValue(Resource.error(e.localizedMessage?:"error occurred"))
            }
        }
    }

}