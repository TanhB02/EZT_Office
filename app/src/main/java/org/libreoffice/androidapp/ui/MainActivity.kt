package org.libreoffice.androidapp.ui

import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import org.libreoffice.androidapp.databinding.ActivityMainBinding
import org.libreoffice.androidlib.utils.OtherExt.logD
import org.libreoffice.androidlib.utils.UtilsOffice.openFile
import org.libreoffice.androidlib.utils.UtilsOffice.pickAndOpenDocument

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupSubmitButton()
        pickAndOpenDocument()
    }

    private fun setupSubmitButton() {
        binding.submitButton.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                val uriString = binding.uriEditText.text.toString().trim()

                if (uriString.isEmpty()) {
                    Toast.makeText(this@MainActivity, "Vui lòng nhập URI!", Toast.LENGTH_SHORT)
                        .show()
                    return
                }

                try {
                    val uri = Uri.parse(uriString)
                    openFile( uri){
                        logD("TANHXXXX =>>>>> kill process")
                    }
                } catch (e: Exception) {
                    Toast.makeText(
                        this@MainActivity,
                        "URI không hợp lệ: " + e.message,
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
        })
    }

}