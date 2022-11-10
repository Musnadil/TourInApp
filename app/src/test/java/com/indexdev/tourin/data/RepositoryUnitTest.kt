package com.indexdev.tourin.data

import com.indexdev.tourin.data.api.ApiHelper
import com.indexdev.tourin.data.api.ApiService
import com.indexdev.tourin.data.model.request.LoginRequest
import com.indexdev.tourin.data.model.request.RateRequest
import com.indexdev.tourin.data.model.request.RegisterRequest
import com.indexdev.tourin.data.model.request.UpdateUserRequest
import com.indexdev.tourin.data.model.response.*
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test

class RepositoryUnitTest {

    //Collaborator
    private lateinit var apiHelper: ApiHelper
    private lateinit var apiService: ApiService

    //System Under Test
    private lateinit var repository: Repository

    @Before
    fun setUp() {
        apiService = mockk()
        apiHelper = ApiHelper(apiService)
        repository = Repository(apiHelper)
    }

    @Test
    fun authRegister(): Unit = runBlocking {
        val responseRegister = mockk<ResponseRegister>()
        val registerRequest = RegisterRequest("", "", "")
        every {
            runBlocking {
                repository.authRegister(registerRequest)
            }
        } returns responseRegister

        repository.authRegister(registerRequest)

        verify {
            runBlocking {
                repository.authRegister(registerRequest)
            }
        }
    }

    @Test
    fun authLogin(): Unit = runBlocking {
        val responseLogin = mockk<ResponseLogin>()
        val loginRequest = LoginRequest("", "")
        every {
            runBlocking {
                repository.authLogin(loginRequest)
            }
        } returns responseLogin

        repository.authLogin(loginRequest)

        verify {
            runBlocking {
                repository.authLogin(loginRequest)
            }
        }
    }

    @Test
    fun getTourList(): Unit = runBlocking {
        val responseTourList = mockk<List<ResponseTourList>>()
        every {
            runBlocking {
                repository.getTourList()
            }
        } returns responseTourList

        repository.getTourList()

        verify {
            runBlocking {
                repository.getTourList()
            }
        }
    }

    @Test
    fun getPoiById(): Unit = runBlocking {
        val responseGetPOI = mockk<List<ResponsePOI>>()
        every {
            runBlocking {
                repository.getPoiById(1)
            }
        } returns responseGetPOI

        repository.getPoiById(1)

        verify {
            runBlocking {
                repository.getPoiById(1)
            }
        }

    }

    @Test
    fun postRate(): Unit = runBlocking {
        val responsePostRate = mockk<ResponseRate>()
        val rateRequest = RateRequest(null, "", "", "", null)
        every {
            runBlocking {
                repository.postRate(rateRequest)
            }
        } returns responsePostRate

        repository.postRate(rateRequest)

        verify {
            runBlocking {
                repository.postRate(rateRequest)
            }
        }
    }

    @Test
    fun editUsername(): Unit = runBlocking {
        val responseEditUsername = mockk<ResponseUpdateUsername>()
        val requestEditUsername = UpdateUserRequest("")
        every {
            runBlocking {
                repository.editUsername(1, requestEditUsername)
            }
        } returns responseEditUsername

        repository.editUsername(1, requestEditUsername)

        verify {
            runBlocking {
                repository.editUsername(1, requestEditUsername)
            }
        }
    }

    @Test
    fun getRecommendationList(): Unit = runBlocking {
        val responseRecommendation = mockk<List<ResponseRecommendation>>()
        every {
            runBlocking {
                repository.getRecommendationList()
            }
        } returns responseRecommendation

        repository.getRecommendationList()

        verify {
            runBlocking {
                repository.getRecommendationList()
            }
        }
    }
}