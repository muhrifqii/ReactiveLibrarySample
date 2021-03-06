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

import android.support.annotation.LayoutRes
import android.view.View
import io.github.muhrifqii.reactivelibrarysample.bases.MyBaseAdapter
import io.github.muhrifqii.reactivelibrarysample.bases.MyBaseViewHolder

/**
 * Created on   : 08/01/17
 * Author       : muhrifqii
 * Name         : Muhammad Rifqi Fatchurrahman Putra Danar
 * Github       : https://github.com/muhrifqii
 * LinkedIn     : https://linkedin.com/in/muhrifqii
 */

class MainAdapter(private val delegate: MainAdapter.Delegate, datas: List<MainModel>) :
    MyBaseAdapter() {

  interface Delegate : MainLinearViewHolder.Delegate

  init {
    bind(datas)
  }

  override fun layout(): Int {
    return R.layout.list_item_main_view
  }

  override fun viewHolder(@LayoutRes layout: Int, view: View): MyBaseViewHolder {
    return MainLinearViewHolder(view, delegate)
  }
}
