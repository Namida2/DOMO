package com.example.cookMain.presentation

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.cookMain.R
import com.example.cookMain.databinding.ActivityCookMainBinding

class CookMainActivity : AppCompatActivity() {

    private lateinit var bidning: ActivityCookMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bidning = ActivityCookMainBinding.inflate(layoutInflater)
        setContentView(bidning.root)
    }
}