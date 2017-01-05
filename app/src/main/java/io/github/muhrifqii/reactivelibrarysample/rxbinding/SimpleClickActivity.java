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

package io.github.muhrifqii.reactivelibrarysample.rxbinding;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import com.jakewharton.rxbinding.view.RxView;
import com.jakewharton.rxbinding.widget.RxTextView;
import io.github.muhrifqii.reactivelibrarysample.R;
import rx.functions.Func1;
import rx.subscriptions.CompositeSubscription;

/**
 * Created on   : 05/01/17
 * Author       : muhrifqii
 * Name         : Muhammad Rifqi Fatchurrahman Putra Danar
 * Github       : https://github.com/muhrifqii
 * LinkedIn     : https://linkedin.com/in/muhrifqii
 */

public class SimpleClickActivity extends AppCompatActivity {
  private static final String KEY_STATE_CTR = "ctr";
  private int counter;
  private final CompositeSubscription subs = new CompositeSubscription();
  private TextView textView, tvTick;
  private Button btnTicker;

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    if (savedInstanceState != null) {
      counter = savedInstanceState.getInt(KEY_STATE_CTR, 0);
    } else {
      counter = 0;
    }
    setContentView(R.layout.activity_simple_click);
    textView = (TextView) findViewById(R.id.tv_hello_reactive);
    tvTick = (TextView) findViewById(R.id.tv_tick);
    btnTicker = ((Button) findViewById(R.id.btn_ticker));
    tvTick.setText(String.valueOf(counter));
  }

  @Override protected void onStart() {
    super.onStart();
    subs.add(RxView.clicks(textView).subscribe(__ -> clickeEvent(textView)));
    subs.add(RxView.longClicks(textView).subscribe(__ -> longClickEvent(textView)));
    subs.add(RxView.clicks(tvTick).subscribe(__ -> clickeEvent(tvTick)));
    subs.add(RxView.longClicks(tvTick).subscribe(__ -> longClickEvent(tvTick)));
    subs.add(RxView.clicks(btnTicker)
        .map(__ -> String.valueOf(counter = (counter < 33) ? counter + 1 : 0))
        .subscribe(RxTextView.text(tvTick)));
    subs.add(RxView.longClicks(btnTicker)
        .map(__ -> String.valueOf(counter = 0))
        .subscribe(RxTextView.text(tvTick)));
  }

  @Override protected void onStop() {
    super.onStop();
    subs.unsubscribe();
  }

  @Override protected void onDestroy() {
    subs.clear();
    super.onDestroy();
  }

  @Override protected void onSaveInstanceState(Bundle outState) {
    outState.putInt(KEY_STATE_CTR, counter);
    super.onSaveInstanceState(outState);
  }

  private void clickeEvent(View view) {
    Toast.makeText(SimpleClickActivity.this, "I am ready!", Toast.LENGTH_SHORT).show();
  }

  private void longClickEvent(View view) {
    Toast.makeText(SimpleClickActivity.this, "Long click happened", Toast.LENGTH_SHORT).show();
  }
}
