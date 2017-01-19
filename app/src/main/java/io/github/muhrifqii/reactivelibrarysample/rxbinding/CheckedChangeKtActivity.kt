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
import android.support.annotation.IdRes
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.AppCompatCheckBox
import android.support.v7.widget.SwitchCompat
import android.view.View
import android.widget.RadioGroup
import android.widget.TextView
import com.jakewharton.rxbinding.view.enabled
import com.jakewharton.rxbinding.view.visibility
import com.jakewharton.rxbinding.widget.checked
import com.jakewharton.rxbinding.widget.checkedChanges
import com.jakewharton.rxbinding.widget.text
import io.github.muhrifqii.reactivelibrarysample.R
import io.github.muhrifqii.reactivelibrarysample.commons.Dishes
import rx.Observable
import rx.subscriptions.CompositeSubscription
import java.util.ArrayList

/**
 * Created on   : 19/01/17
 * Author       : muhrifqii
 * Name         : Muhammad Rifqi Fatchurrahman Putra Danar
 * Github       : https://github.com/muhrifqii
 * LinkedIn     : https://linkedin.com/in/muhrifqii
 */
class CheckedChangeKtActivity : AppCompatActivity() {
  private val KEY_STATE = "state"
  private var detail = ""
  private var spicyDetail = ""
  private val subs = CompositeSubscription()
  private lateinit var frenchFies: AppCompatCheckBox
  private lateinit var potato: AppCompatCheckBox
  private lateinit var chicken: AppCompatCheckBox
  private lateinit var spices: SwitchCompat
  private lateinit var radioGroup: RadioGroup
  private lateinit var tvDetail: TextView

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_checked_change)

    frenchFies = findViewById(R.id.cb_french_fries) as AppCompatCheckBox
    potato = findViewById(R.id.cb_potato) as AppCompatCheckBox
    chicken = findViewById(R.id.cb_chicken_wing) as AppCompatCheckBox
    spices = findViewById(R.id.switch_) as SwitchCompat
    radioGroup = findViewById(R.id.radio_group) as RadioGroup
    tvDetail = findViewById(R.id.tv_detail) as TextView

    if (savedInstanceState == null) {
      radioGroup.visibility = View.GONE
      tvDetail.text = ""
    }
  }

  override fun onStart() {
    super.onStart()
    // event from checkbox
    val f = frenchFies.checkedChanges()
        .map { if (it) Dishes.FRENCH_FRIES else Dishes.BLANK }
        .publish()
    val p = potato.checkedChanges()
        .map { if (it) Dishes.POTATO_WEDGES else Dishes.BLANK }
        .publish()
    val c = chicken.checkedChanges()
        .map { if (it) Dishes.CHICKEN_WINGS else Dishes.BLANK }
        .publish()
    val s1 = Observable.combineLatest(f, p, c) { x, y, z -> combineDishesToSentences(x, y, z) }
        .subscribe(tvDetail.text())
    val s2 = Observable.combineLatest(f, p, c
    ) { x, y, z -> !(x == Dishes.BLANK && y == Dishes.BLANK && z == Dishes.BLANK) }
        .subscribe { spices.enabled() }
    subs.add(s1)
    subs.add(s2)
    f.connect()
    p.connect()
    c.connect()
    // event from switch
    val sp = spices.checkedChanges().publish()
    subs.add(sp.filter { !it }.subscribe { spicyDetail = "" })
    subs.add(sp.subscribe(radioGroup.visibility()))
    subs.add(sp.map { if (it) R.id.radio_1 else -1 }.subscribe(radioGroup.checked()))
    sp.connect()
    // event from radio button group
    val s3 = radioGroup.checkedChanges()
        .map { this.spicyLevelToSentence(it) }
        .subscribe(tvDetail.text())
    subs.add(s3)
  }

  override fun onStop() {
    super.onStop()
    subs.clear()
  }

  override fun onSaveInstanceState(outState: Bundle) {
    super.onSaveInstanceState(outState)
  }

  private fun combineDishesToSentences(vararg ds: Dishes): String {
    var ctr = 0
    val tmp = ArrayList<Dishes>()
    val x = StringBuilder("")
    ds.filter { it != Dishes.BLANK }.forEach {
      ctr++
      if (ctr == 2) {
        tmp.add(Dishes.AND)
      } else if (ctr > 2) {
        tmp.add(Dishes.COMMA)
      }
      tmp.add(it)
    }
    if (tmp.size > 0) {
      x.append("You have ordered ")
    }
    tmp.reversed().forEach { x.append(it.toString()) }
//    for (i in tmp.indices.reversed()) {
//      x.append(tmp[i].toString())
//    }
    detail = x.toString()
    return x.append(spicyDetail).toString()
  }

  private fun spicyLevelToSentence(@IdRes id: Int): String {
    when (id) {
      R.id.radio_1 -> spicyDetail = " Level 1"
      R.id.radio_2 -> spicyDetail = " Level 2"
      R.id.radio_3 -> spicyDetail = " Level 3"
      R.id.radio_4 -> spicyDetail = " Level 4"
      else -> spicyDetail = ""
    }
    return detail + spicyDetail
  }
}