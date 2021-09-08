package net.basicmodel

import android.os.Bundle
import kotlinx.android.synthetic.main.layout_bottom.*

class MainActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        requestPermissions()
        initView()
    }


    private fun initView() {
        map.setOnClickListener {
            showPosition(0)
        }
        near.setOnClickListener {
            showPosition(1)
        }
        street.setOnClickListener {
            showPosition(2)
        }
        interactive.setOnClickListener {
            showPosition(3)
        }
    }

}