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

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatCheckBox;
import android.support.v7.widget.AppCompatRadioButton;
import android.support.v7.widget.SwitchCompat;
import android.view.View;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.ToggleButton;
import com.jakewharton.rxbinding.view.RxView;
import com.jakewharton.rxbinding.widget.RxCompoundButton;
import com.jakewharton.rxbinding.widget.RxRadioGroup;
import com.jakewharton.rxbinding.widget.RxTextView;
import io.github.muhrifqii.reactivelibrarysample.R;
import io.github.muhrifqii.reactivelibrarysample.commons.Dishes;
import java.util.ArrayList;
import java.util.List;
import rx.Observable;
import rx.Subscription;
import rx.observables.ConnectableObservable;
import rx.subscriptions.CompositeSubscription;

public class CheckedChangeActivity extends AppCompatActivity {
  private static final String KEY_STATE = "state";
  private String detail = "";
  private String spicyDetail = "";
  private CompositeSubscription subs = new CompositeSubscription();
  private AppCompatCheckBox frenchFies, potato, chicken;
  private SwitchCompat spices;
  private RadioGroup radioGroup;
  private TextView tvDetail;

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_checked_change);

    frenchFies = (AppCompatCheckBox) findViewById(R.id.cb_french_fries);
    potato = (AppCompatCheckBox) findViewById(R.id.cb_potato);
    chicken = (AppCompatCheckBox) findViewById(R.id.cb_chicken_wing);
    spices = (SwitchCompat) findViewById(R.id.switch_);
    radioGroup = (RadioGroup) findViewById(R.id.radio_group);
    tvDetail = (TextView) findViewById(R.id.tv_detail);

    if (savedInstanceState == null) {
      radioGroup.setVisibility(View.GONE);
      tvDetail.setText("");
    } else {
      // TODO: 12/01/17 handle statechanges
    }
  }

  @Override protected void onStart() {
    super.onStart();
    // event from checkbox
    final ConnectableObservable<Dishes> f = RxCompoundButton.checkedChanges(frenchFies)
        .map(checked -> checked ? Dishes.FRENCH_FRIES : Dishes.BLANK)
        .publish();
    final ConnectableObservable<Dishes> p = RxCompoundButton.checkedChanges(potato)
        .map(checked -> checked ? Dishes.POTATO_WEDGES : Dishes.BLANK)
        .publish();
    final ConnectableObservable<Dishes> c = RxCompoundButton.checkedChanges(chicken)
        .map(checked -> checked ? Dishes.CHICKEN_WINGS : Dishes.BLANK)
        .publish();
    final Subscription s1 =
        Observable.combineLatest(f, p, c, (x, y, z) -> combineDishesToSentences(x, y, z))
            .subscribe(RxTextView.text(tvDetail));
    final Subscription s2 = Observable.combineLatest(f, p, c,
        (x, y, z) -> !(x == Dishes.BLANK && y == Dishes.BLANK && z == Dishes.BLANK))
        .subscribe(this::spiciesSwitchEnableChanges);
    subs.add(s1);
    subs.add(s2);
    f.connect();
    p.connect();
    c.connect();
    // event from switch
    final ConnectableObservable<Boolean> sp = RxCompoundButton.checkedChanges(spices).publish();
    subs.add(sp.subscribe(RxView.visibility(radioGroup)));
    subs.add(
        sp.map(checked -> checked ? R.id.radio_1 : -1).subscribe(RxRadioGroup.checked(radioGroup)));
    sp.connect();
    // event from radio button group
    final ConnectableObservable<Integer> rb = RxRadioGroup.checkedChanges(radioGroup).publish();
    //RxRadioGroup.checkedChanges(radioGroup).map()
  }

  @Override protected void onStop() {
    super.onStop();
    subs.clear();
  }

  @Override protected void onSaveInstanceState(Bundle outState) {
    super.onSaveInstanceState(outState);
  }

  private @NonNull String combineDishesToSentences(final @NonNull Dishes... ds) {
    int ctr = 0;
    final List<Dishes> tmp = new ArrayList<>();
    final StringBuilder x = new StringBuilder("");
    for (final Dishes d : ds) {
      if (d != Dishes.BLANK) {
        ctr++;
        if (ctr == 2) {
          tmp.add(Dishes.AND);
        } else if (ctr > 2) {
          tmp.add(Dishes.COMMA);
        }
        tmp.add(d);
      }
    }
    if (tmp.size() > 0) {
      x.append("You have ordered ");
    }
    for (int i = tmp.size() - 1; i >= 0; i--) {
      x.append(tmp.get(i).toString());
    }
    detail = x.toString();
    return x.append(spicyDetail).toString();
  }

  private void spiciesSwitchEnableChanges(final boolean bool) {
    RxView.enabled(spices).call(bool);
    RxView.visibility(radioGroup).call(bool);
    RxRadioGroup.checked(radioGroup).call(-1);
  }

  private @NonNull String spicyLevelToSentence(final int id) {
    switch (id) {
      case R.id.radio_1:

        break;
      case R.id.radio_2:
        break;
      case R.id.radio_3:
        break;
      case R.id.radio_4:
        break;
      default:
        return "";
    }
    return detail;
  }
}
