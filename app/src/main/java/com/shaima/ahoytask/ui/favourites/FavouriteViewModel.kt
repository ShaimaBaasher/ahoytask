package com.shaima.ahoytask.ui.favourites

import androidx.lifecycle.*
import com.shaima.ahoytask.domain.core.BaseResult
import com.shaima.ahoytask.domain.home.usecase.GetPlacesUseCase
import com.shaima.ahoytask.domain.state.HomeFavFragmentState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.flow.map
import javax.inject.Inject

@HiltViewModel
class FavouriteViewModel @Inject constructor(private val getPlacesUseCase: GetPlacesUseCase) : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is dashboard Fragment"
    }
    val text: LiveData<String> = _text

    @OptIn(InternalCoroutinesApi::class)
    val viewState = getPlacesUseCase()
        .map { result ->
            when (result) {
                is BaseResult.Success -> HomeFavFragmentState.SuccessGetFromDb(result.data.map { it })
                is BaseResult.ErrorMsg -> result.msg?.let { HomeFavFragmentState.ShowToast(it) }
                is BaseResult.Error -> HomeFavFragmentState.ShowToast(result.exception.localizedMessage)
            }
        }
        .asLiveData(viewModelScope.coroutineContext)

}