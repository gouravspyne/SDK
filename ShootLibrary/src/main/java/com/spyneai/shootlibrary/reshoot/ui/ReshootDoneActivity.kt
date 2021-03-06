package com.spyneai.shootlibrary.reshoot.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.spyneai.shoot.ui.base.ImageProcessingStartedFragment
import com.spyneai.shootlibrary.R

class ReshootDoneActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reshoot_done)

        supportFragmentManager.beginTransaction()
            .add(R.id.flContainer, ImageProcessingStartedFragment())
            .commit()
    }

    override fun onBackPressed() {

    }
}