package com.thechance.season2week10

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import androidx.appcompat.app.AppCompatActivity
import androidx.viewbinding.ViewBinding

abstract class BaseActivity<VB : ViewBinding> : AppCompatActivity() {

    abstract val logTag: String?
    abstract val bindingInflater: (LayoutInflater) -> VB
    private var _binding: ViewBinding? = null

    private val binding
        get() = _binding as VB

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = bindingInflater(layoutInflater)
        setContentView(binding.root)
        setup()
    }

    abstract fun setup()

    protected fun log(value: Any) {
        Log.v(logTag, value.toString())
    }
}