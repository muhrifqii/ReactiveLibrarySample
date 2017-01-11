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
import android.widget.Button;
import android.widget.EditText;
import com.jakewharton.rxbinding.view.RxView;
import com.jakewharton.rxbinding.widget.RxTextView;
import io.github.muhrifqii.reactivelibrarysample.R;
import io.github.muhrifqii.reactivelibrarysample.commons.MyStringUtils;
import rx.Observable;
import rx.Subscription;
import rx.observables.ConnectableObservable;
import rx.subscriptions.CompositeSubscription;

public class ComplexTextChangesActivity extends AppCompatActivity {
  private static final String KEY_STATE = "key";
  private final CompositeSubscription subs = new CompositeSubscription();

  private EditText etName, etEmail, etPassword, etPasswordConfirm;
  private TextInputLayout tilName, tilEmail, tilPassword, tilPasswordConfirm;
  private Button btnSignUp;

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_text_changes);

    etName = (EditText) findViewById(R.id.et_name);
    etEmail = (EditText) findViewById(R.id.et_email);
    etPassword = (EditText) findViewById(R.id.et_password);
    etPasswordConfirm = (EditText) findViewById(R.id.et_password_confirm);
    tilName = (TextInputLayout) findViewById(R.id.til_name);
    tilEmail = (TextInputLayout) findViewById(R.id.til_email);
    tilPassword = (TextInputLayout) findViewById(R.id.til_password);
    tilPasswordConfirm = (TextInputLayout) findViewById(R.id.til_password_confirm);
    btnSignUp = (Button) findViewById(R.id.btn_sign_up);
  }

  @Override protected void onStart() {
    super.onStart();
    final ConnectableObservable<Boolean> coNameValid =
        RxTextView.textChanges(etName).map(MyStringUtils::isTextNotEmpty).publish();
    final ConnectableObservable<Boolean> coEmailValid =
        RxTextView.textChanges(etEmail).map(MyStringUtils::isTextEmailAddress).publish();
    final ConnectableObservable<Boolean> coPasswordSafe =
        RxTextView.textChanges(etPassword).map(MyStringUtils::isPasswordSafe).publish();
    final ConnectableObservable<Boolean> coPasswordConfirmSame =
        RxTextView.textChanges(etPasswordConfirm)
            .map(text -> MyStringUtils.isSameWith(text, etPassword.getText().toString()))
            .publish();

    final Subscription subErrorName = RxView.focusChanges(etName)
        .withLatestFrom(coNameValid, (focused, notEmpty) -> !focused && !notEmpty)
        .subscribe(error -> {
          tilName.setErrorEnabled(error);
          tilName.setError(error ? "Name can not be empty" : "");
        });
    final Subscription subErrorEmail = RxView.focusChanges(etEmail)
        .withLatestFrom(coEmailValid, (focused, isTextEmail) -> !focused && !isTextEmail)
        .subscribe(error -> {
          tilEmail.setErrorEnabled(error);
          tilEmail.setError(error ? "Insert a proper email address" : "");
        });
    final Subscription subErrorPassword = RxView.focusChanges(etPassword)
        .withLatestFrom(coPasswordSafe, (focused, passwordSafe) -> !focused && !passwordSafe)
        .subscribe(error -> {
          tilPassword.setErrorEnabled(error);
          tilPassword.setError(error ? "Password must be more than 6 characters" : "");
        });
    final Subscription subErrorPasswordConfirm = RxView.focusChanges(etPasswordConfirm)
        .withLatestFrom(coPasswordConfirmSame, (focused, passwordSame) -> !focused && !passwordSame)
        .subscribe(error -> {
          tilPasswordConfirm.setErrorEnabled(error);
          tilPasswordConfirm.setError(error ? "Password typed is different" : "");
        });
    final Subscription subButtonEnabled =
        Observable.combineLatest(coNameValid, coEmailValid, coPasswordSafe, coPasswordConfirmSame,
            (nameValid, emailValid, passwordValid, passwordConfirmValid) -> nameValid
                && emailValid
                && passwordValid
                && passwordConfirmValid).subscribe(RxView.enabled(btnSignUp));
    subs.addAll(subErrorName, subErrorEmail, subErrorPassword, subErrorPasswordConfirm,
        subButtonEnabled);

    coNameValid.connect();
    coEmailValid.connect();
    coPasswordSafe.connect();
    coPasswordConfirmSame.connect();
  }

  @Override protected void onStop() {
    super.onStop();
    subs.clear();
  }

  @Override protected void onDestroy() {
    super.onDestroy();
  }

  @Override protected void onSaveInstanceState(Bundle outState) {
    super.onSaveInstanceState(outState);
  }
}
