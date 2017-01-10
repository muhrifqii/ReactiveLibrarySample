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
import android.support.v7.app.AppCompatActivity
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import com.jakewharton.rxbinding.view.clicks
import com.jakewharton.rxbinding.view.longClicks
import com.jakewharton.rxbinding.widget.RxTextView
import com.jakewharton.rxbinding.widget.text
import com.jakewharton.rxbinding.widget.textChanges
import io.github.muhrifqii.reactivelibrarysample.R
import rx.subscriptions.CompositeSubscription

class SimpleClickKotlinActivity : AppCompatActivity() {
  private val KEY_STATE_CTR = "ctr"
  private val subs: CompositeSubscription = CompositeSubscription()
  private var counter: Int = 0
  private lateinit var btnDecrement: Button
  private lateinit var btnIncrement: Button
  private lateinit var tvTick: TextView

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_simple_click_kotlin)
    savedInstanceState?.let { counter = it.getInt(KEY_STATE_CTR, 0) }
    btnDecrement = findViewById(R.id.btn_dec) as Button
    btnIncrement = findViewById(R.id.btn_inc) as Button
    tvTick = findViewById(R.id.tv_tick) as TextView
    tvTick.text = counter.toString()

    val btnDecSubscription = btnDecrement.clicks()
        .map {
          counter -= 1
          counter.toString()
        }
        .subscribe(tvTick.text())
    val btnIncSubscription = btnIncrement.clicks()
        .map {
          counter += 1
          counter.toString()
        }
        .subscribe(tvTick.text())
    val tvClickSub = tvTick.clicks()
        .subscribe { popToast("long press to reset") }
    val longBtnDec = btnDecrement.longClicks()
        .subscribe { popToast("-1 to the counter") }
    val longBtnInc = btnIncrement.longClicks()
        .subscribe { popToast("+1 to the counter") }
    val longTvClick = tvTick.longClicks()
        .map {
          counter = 0
          counter.toString()
        }
        .subscribe {
          tvTick.text().call(it)
          popToast("counter have been reset")
        }
    subs.addAll(btnDecSubscription, btnIncSubscription, tvClickSub, longBtnDec, longBtnInc,
        longTvClick)
  }

  override fun onDestroy() {
    super.onDestroy()
    subs.clear()
  }

  override fun onSaveInstanceState(outState: Bundle?) {
    outState!!.putInt(KEY_STATE_CTR, counter)
    super.onSaveInstanceState(outState)
  }

  private fun popToast(msg: String): Unit {
    Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
  }
}
