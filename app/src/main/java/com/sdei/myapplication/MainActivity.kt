package com.sdei.myapplication

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.widget.AppCompatButton
import androidx.appcompat.widget.AppCompatEditText
import androidx.appcompat.widget.AppCompatTextView
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.jsoup.nodes.Element

class MainActivity : AppCompatActivity() {

    private var inputEt: AppCompatEditText? = null
    private var fetchButton: AppCompatButton? = null
    private var versionTv: AppCompatTextView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        inputEt = findViewById(R.id.input)
        fetchButton = findViewById(R.id.button)
        versionTv = findViewById(R.id.version_tv)

        fetchButton!!.setOnClickListener { fetchAppVersion() }
    }

    private fun fetchAppVersion() {

        var packageName = inputEt!!.text.toString().trim()
        if (packageName.isBlank()) {
            packageName = this@MainActivity.packageName
        }

        doAsync {
            val connect: Document = Jsoup.connect("https://play.google.com/store/apps/details?id=$packageName&hl=en")
                .timeout(10000)
                .userAgent("Mozilla/5.0 (Windows; U; WindowsNT 5.1; en-US; rv1.8.1.6) Gecko/20070725 Firefox/2.0.0.6")
                .referrer("http://www.google.com")
                .get()
            val connectSelect: Element =
                connect.select("div.hAyfc:nth-child(4) > span:nth-child(2) > div:nth-child(1) > span:nth-child(1)")
                    .first()
            val versionAsString = connectSelect.ownText()
            uiThread {
                versionTv!!.text = "$packageName has $versionAsString version"
            }
        }

    }

}
