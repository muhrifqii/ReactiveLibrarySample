/*
 * MIT License
 *
 * Copyright (c) 2017 Muhammad Rifqi Fatchurrahman Putra Danar
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package io.github.muhrifqii.reactivelibrarysample.rxbinding

import android.os.Bundle
import android.support.design.widget.TextInputLayout
import android.support.v7.app.AppCompatActivity
import android.widget.EditText
import android.widget.TextView
import com.jakewharton.rxbinding.widget.text
import com.jakewharton.rxbinding.widget.textChanges
import io.github.muhrifqii.reactivelibrarysample.R
import rx.functions.Func1
import rx.subscriptions.CompositeSubscription
import java.util.Locale

/**
 * Created on   : 11/01/17
 * Author       : muhrifqii
 * Name         : Muhammad Rifqi Fatchurrahman Putra Danar
 * Github       : https://github.com/muhrifqii
 * LinkedIn     : https://linkedin.com/in/muhrifqii
 */
class SimpleTextChangesKotlinActivity : AppCompatActivity() {
  private val MESSAGE_MAX_LENGTH = 100
  private val subs = CompositeSubscription()
  private lateinit var etMsg: EditText
  private lateinit var tilMsg: TextInputLayout
  private lateinit var tvMsg: TextView
  private lateinit var tvStar: TextView

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_simple_text_changes)

    etMsg = findViewById(R.id.et_message) as EditText
    tilMsg = findViewById(R.id.til_message) as TextInputLayout
    tvMsg = findViewById(R.id.tv_message) as TextView
    tvStar = findViewById(R.id.tv_star) as TextView

    tilMsg.isCounterEnabled = true
    tilMsg.counterMaxLength = MESSAGE_MAX_LENGTH
  }

  override fun onStart() {
    super.onStart()
    val obsMsg = etMsg.textChanges().filter({ text -> text.length < MESSAGE_MAX_LENGTH })
    val subMsg = obsMsg.subscribe(tvMsg.text())
    val subTotalStarMsg = obsMsg.map { it.split(" ") }
        .map { countStars(it) }
        .map { "Total Stars: %d".format(Locale.US, it) }
        .subscribe(tvStar.text())
    subs.addAll(subMsg, subTotalStarMsg)
  }

  override fun onStop() {
    super.onStop()
    subs.clear()
  }

  override fun onDestroy() {
    super.onDestroy()
  }

  private fun countStars(source: List<String>): Int {
    var total = 0
    for (word in source) {
      total = if (word.equals("star", ignoreCase = true) or word.equals("stars",
          ignoreCase = true)) total + 1 else total
    }
    return total
  }
}