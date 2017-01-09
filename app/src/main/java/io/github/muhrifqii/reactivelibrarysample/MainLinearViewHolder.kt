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

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import io.github.muhrifqii.reactivelibrarysample.bases.MyBaseViewHolder

/**
 * Created on   : 05/01/17
 * Author       : muhrifqii
 * Name         : Muhammad Rifqi Fatchurrahman Putra Danar
 * Github       : https://github.com/muhrifqii
 * LinkedIn     : https://linkedin.com/in/muhrifqii
 */

class MainLinearViewHolder(itemView: View, private val delegate: MainLinearViewHolder.Delegate) :
    MyBaseViewHolder(itemView) {
  private lateinit var data: MainModel

  interface Delegate {
    fun onMainLinearViewHolderClicked(holder: MainLinearViewHolder, data: MainModel)
  }

  override fun onClick(view: View) {
    delegate.onMainLinearViewHolderClicked(this, data)
  }

  @Throws(Exception::class)
  override fun bind(data: Any?) {
    if (data == null) {
      throw Exception("null")
    }

    this.data = data as MainModel
    val thumb = view.findViewById(R.id.iv_item_main) as ImageView
    val title = view.findViewById(R.id.tv_title) as TextView
    title.text = this.data.name
  }
}
