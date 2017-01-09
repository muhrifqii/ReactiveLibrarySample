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

package io.github.muhrifqii.reactivelibrarysample.bases;

import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import java.util.ArrayList;
import java.util.List;

/**
 * Created on   : 05/01/17
 * Author       : muhrifqii
 * Name         : Muhammad Rifqi Fatchurrahman Putra Danar
 * Github       : https://github.com/muhrifqii
 * LinkedIn     : https://linkedin.com/in/muhrifqii
 */

public abstract class MyBaseAdapter extends RecyclerView.Adapter<MyBaseViewHolder> {
  private List<Object> datas = new ArrayList<>();

  public abstract @LayoutRes int layout();

  public abstract @NonNull MyBaseViewHolder viewHolder(final @LayoutRes int layout,
      final @NonNull View view);

  @Override public MyBaseViewHolder onCreateViewHolder(final @NonNull ViewGroup parent,
      final @LayoutRes int viewType) {
    final View view = LayoutInflater.from(parent.getContext()).inflate(viewType, parent, false);
    return viewHolder(viewType, view);
  }

  @Override public void onBindViewHolder(MyBaseViewHolder holder, int position) {
    final Object data = datas.get(position);
    try {
      holder.bind(data);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  @Override public int getItemCount() {
    return datas.size();
  }

  @Override public int getItemViewType(final int position) {
    return layout();
  }

  public <T> void bind(final @NonNull List<T> datas) {
    this.datas = new ArrayList<>(datas);
  }
}
