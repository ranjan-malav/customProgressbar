package com.example.customprogressbar

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.view.View
import android.widget.Button
import android.widget.EditText

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val progressPerc: EditText = findViewById(R.id.progess_et)
        val animateButton: Button = findViewById(R.id.animate_button)
        val progressbar: CustomProgressbar = findViewById(R.id.progress_bar)

        animateButton.setOnClickListener {
            val progressString = progressPerc.text.toString()
            if (progressString.isNotEmpty()) {
                val progress: Int = progressString.toInt()
                progressbar.reset()
                progressbar.setProgress(progress)
            }
        }
    }
}
