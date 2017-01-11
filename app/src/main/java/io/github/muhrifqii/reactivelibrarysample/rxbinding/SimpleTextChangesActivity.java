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

import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.TextView;
import com.jakewharton.rxbinding.widget.RxTextView;
import io.github.muhrifqii.reactivelibrarysample.R;
import io.github.muhrifqii.reactivelibrarysample.commons.MyStringUtils;
import java.util.Locale;
import rx.Observable;
import rx.Subscription;
import rx.subscriptions.CompositeSubscription;

public class SimpleTextChangesActivity extends AppCompatActivity {
  private static final int MESSAGE_MAX_LENGTH = 140;
  private final CompositeSubscription subs = new CompositeSubscription();
  private EditText etMsg;
  private TextInputLayout tilMsg;
  private TextView tvMsg, tvStar;

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_simple_text_changes);

    etMsg = (EditText) findViewById(R.id.et_message);
    tilMsg = (TextInputLayout) findViewById(R.id.til_message);
    tvMsg = (TextView) findViewById(R.id.tv_message);
    tvStar = (TextView) findViewById(R.id.tv_star);

    tilMsg.setCounterEnabled(true);
    tilMsg.setCounterMaxLength(MESSAGE_MAX_LENGTH);
  }

  @Override protected void onStart() {
    super.onStart();
    final Observable<CharSequence> obsMsg =
        RxTextView.textChanges(etMsg).filter(text -> text.length() < MESSAGE_MAX_LENGTH);
    final Subscription subMsg = obsMsg.subscribe(RxTextView.text(tvMsg));
    final Subscription subTotalStarMsg = obsMsg.map(text -> text.toString().split(" "))
        .map(this::countStars)
        .map(num -> String.format(Locale.US, "Total Stars: %d", num))
        .subscribe(RxTextView.text(tvStar));
    subs.addAll(subMsg, subTotalStarMsg);
  }

  @Override protected void onStop() {
    super.onStop();
    subs.clear();
  }

  @Override protected void onDestroy() {
    super.onDestroy();
  }

  private int countStars(final String[] source) {
    int total = 0;
    for (final String word : source) {
      total = word.equalsIgnoreCase("star") || word.equalsIgnoreCase("stars") ? total + 1 : total;
    }
    return total;
  }
}
