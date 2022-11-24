package com.erickpimentel.mercadoeditorial.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.erickpimentel.mercadoeditorial.utils.Status
import com.erickpimentel.mercadoeditorial.utils.Type

class FilterViewModel: ViewModel() {

    private val _type = MutableLiveData<Type?>()
    val type: LiveData<Type?> get() = _type

    private val _status = MutableLiveData<Status?>()
    val status: LiveData<Status?> get() = _status

    fun clearAll(){
        _type.value = null
        _status.value = null
    }

    private fun insertType(type: Type?){
        _type.value = type
    }

    fun updateType(type: Type?){
        insertType(type)
    }

    private fun insertStatus(status: Status?){
        _status.value = status
    }

    fun updateStatus(status: Status?){
        insertStatus(status)
    }


}