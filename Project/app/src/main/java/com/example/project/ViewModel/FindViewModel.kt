package com.example.project.ViewModel

import androidx.lifecycle.*
import com.example.project.Model.ProductFull
import com.example.project.Repository.Colors
import com.example.project.Repository.Imgs
import com.example.project.Repository.Products
import com.example.project.Repository.Sizes
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.text.Normalizer
import java.util.Locale

class FindViewModel : ViewModel() {
    private val _error = MutableLiveData<String>()
    val error: LiveData<String> = _error

    private val _listProFull = MutableLiveData<List<ProductFull>>()
    val listProFull: LiveData<List<ProductFull>> = _listProFull

    private val _query = MutableStateFlow("")
    val query: StateFlow<String> = _query

    private val productRepo = Products()
    private val imgRepo = Imgs()
    private val sizeRepo = Sizes()
    private val colorRepo = Colors()

    init {
        viewModelScope.launch {
            _query
                .debounce(0)                // Đợi 0ms sau khi nhập xong
                .filter { it.isNotBlank() }   // Bỏ qua nếu rỗng
//                .distinctUntilChanged()       // Bỏ qua nếu giống query cũ
                .flatMapLatest { proName ->
                    flow {
                        try {
                            val all = productRepo.selectAll()
                            val productList = all?.filter { it.name.removeAccents().contains(proName.removeAccents()) }
                            val productFulls = buildList {
                                for (pro in productList!!) {
                                    val id = pro.id
                                    val s = sizeRepo.select(id)
                                    val c = colorRepo.select(id)
                                    val i = imgRepo.select(id)
                                    add(ProductFull(pro, s.toMutableList(), c.toMutableList(), i.toMutableList()))
                                }
                            }
                            emit(productFulls)
                        } catch (e: Exception) {
                            _error.postValue(e.message)
                        }
                    }.catch { e ->
                        _error.postValue(e.message)
                    }
                }.collect { list ->
                    _listProFull.postValue(list)
                }
        }
    }

    fun setSearchQuery(q: String) {
        _query.value = q
        if (q.isBlank()) {
            _listProFull.value = emptyList()
        }
    }
    fun String.removeAccents(): String {
        val normalized = Normalizer.normalize(this, Normalizer.Form.NFD)
        return normalized.replace("\\p{InCombiningDiacriticalMarks}+".toRegex(), "")
            .lowercase(Locale.getDefault())
    }
}
