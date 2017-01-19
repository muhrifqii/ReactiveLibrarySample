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

package io.github.muhrifqii.reactivelibrarysample

import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import io.github.muhrifqii.reactivelibrarysample.MainAdapter.Delegate
import io.github.muhrifqii.reactivelibrarysample.bases.SampleRx2BaseActivity
import io.github.muhrifqii.reactivelibrarysample.rxbinding.CheckedChangeActivity
import io.github.muhrifqii.reactivelibrarysample.rxbinding.CheckedChangeKtActivity
import io.github.muhrifqii.reactivelibrarysample.rxbinding.SimpleClickActivity
import io.github.muhrifqii.reactivelibrarysample.rxbinding.SimpleClickKotlinActivity
import io.github.muhrifqii.reactivelibrarysample.rxbinding.ComplexTextChangesActivity
import io.github.muhrifqii.reactivelibrarysample.rxbinding.ComplexTextChangesKotlinActivity
import io.github.muhrifqii.reactivelibrarysample.rxbinding.SimpleTextChangesActivity
import io.github.muhrifqii.reactivelibrarysample.rxbinding.SimpleTextChangesKotlinActivity

/**
 * Created on   : 05/01/17
 * Author       : muhrifqii
 * Name         : Muhammad Rifqi Fatchurrahman Putra Danar
 * Github       : https://github.com/muhrifqii
 * LinkedIn     : https://linkedin.com/in/muhrifqii
 */

class MainActivity : SampleRx2BaseActivity(), Delegate {

  private lateinit var list: RecyclerView

  public override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_main)
    list = findViewById(R.id.list_main) as RecyclerView
    list.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
    list.adapter = MainAdapter(this, initData())
  }

  override fun onDestroy() {
    super.onDestroy()
    list.adapter = null
    list.layoutManager = null
  }

  override fun onMainLinearViewHolderClicked(holder: MainLinearViewHolder, data: MainModel) {
    startActivity(Intent(this, data.clazz))
  }

  private fun initData(): List<MainModel> {
    return listOf(
        MainModel("RxBinding - Clicks", SimpleClickActivity::class.java),
        MainModel("RxBinding - Clicks in Kotlin", SimpleClickKotlinActivity::class.java),
        MainModel("RxBinding - Basic Text Changes", SimpleTextChangesActivity::class.java),
        MainModel("RxBinding - Basic Text Changes in Kotlin",
            SimpleTextChangesKotlinActivity::class.java),
        MainModel("RxBinding - Advance Text Changes", ComplexTextChangesActivity::class.java),
        MainModel("RxBinding - Advance Text Changes in Kotlin",
            ComplexTextChangesKotlinActivity::class.java),
        MainModel("RxBinding - Checked Changes", CheckedChangeActivity::class.java),
        MainModel("RxBinding - Checked Changes in Kotlin", CheckedChangeKtActivity::class.java)
    )
  }
}
