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
import android.support.design.widget.TextInputLayout
import android.support.v7.app.AppCompatActivity
import android.widget.Button
import android.widget.EditText
import com.jakewharton.rxbinding.view.enabled
import com.jakewharton.rxbinding.view.focusChanges
import com.jakewharton.rxbinding.widget.textChanges
import io.github.muhrifqii.reactivelibrarysample.R
import io.github.muhrifqii.reactivelibrarysample.commons.MyStringUtils
import rx.Observable
import rx.subscriptions.CompositeSubscription

/**
 * Created on   : 11/01/17
 * Author       : muhrifqii
 * Name         : Muhammad Rifqi Fatchurrahman Putra Danar
 * Github       : https://github.com/muhrifqii
 * LinkedIn     : https://linkedin.com/in/muhrifqii
 */
class ComplexTextChangesKotlinActivity : AppCompatActivity() {
  private val subs = CompositeSubscription()

  private lateinit var etName: EditText
  private lateinit var etEmail: EditText
  private lateinit var etPassword: EditText
  private lateinit var etPasswordConfirm: EditText
  private lateinit var tilName: TextInputLayout
  private lateinit var tilEmail: TextInputLayout
  private lateinit var tilPassword: TextInputLayout
  private lateinit var tilPasswordConfirm: TextInputLayout
  private lateinit var btnSignUp: Button

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_text_changes)

    etName = findViewById(R.id.et_name) as EditText
    etEmail = findViewById(R.id.et_email) as EditText
    etPassword = findViewById(R.id.et_password) as EditText
    etPasswordConfirm = findViewById(R.id.et_password_confirm) as EditText
    tilName = findViewById(R.id.til_name) as TextInputLayout
    tilEmail = findViewById(R.id.til_email) as TextInputLayout
    tilPassword = findViewById(R.id.til_password) as TextInputLayout
    tilPasswordConfirm = findViewById(R.id.til_password_confirm) as TextInputLayout
    btnSignUp = findViewById(R.id.btn_sign_up) as Button
  }

  override fun onStart() {
    super.onStart()
    val coNameValid =
        etName.textChanges().map(MyStringUtils::isTextNotEmpty).publish()
    val coEmailValid =
        etEmail.textChanges().map(MyStringUtils::isTextEmailAddress).publish()
    val coPasswordSafe =
        etPassword.textChanges().map(MyStringUtils::isPasswordSafe).publish()
    val coPasswordConfirmSame = etPasswordConfirm.textChanges()
        .map { MyStringUtils.isSameWith(it, etPassword.text.toString()) }
        .publish()

    val subErrorName = etName.focusChanges()
        .withLatestFrom(coNameValid, { focused, notEmpty -> !focused && !notEmpty })
        .subscribe {
          tilName.isErrorEnabled = it
          tilName.error = if (it) "Name can not be empty" else ""
        }
    val subErrorEmail = etEmail.focusChanges()
        .withLatestFrom(coEmailValid, { focused, isTextEmail -> !focused && !isTextEmail })
        .subscribe {
          tilEmail.isErrorEnabled = it
          tilEmail.error = if (it) "Insert a proper email address" else ""
        }
    val subErrorPassword = etPassword.focusChanges()
        .withLatestFrom(coPasswordSafe, { focused, passwordSafe -> !focused && !passwordSafe })
        .subscribe {
          tilPassword.isErrorEnabled = it
          tilPassword.error = if (it) "Password must be more than 6 characters" else ""
        }
    val subErrorPasswordConfirm = etPasswordConfirm.focusChanges()
        .withLatestFrom(coPasswordConfirmSame,
            { focused, passwordSame -> !focused && !passwordSame })
        .subscribe {
          tilPasswordConfirm.isErrorEnabled = it
          tilPasswordConfirm.error = if (it) "Password typed is different" else ""
        }
    val subButtonEnabled =
        Observable.combineLatest(coNameValid, coEmailValid, coPasswordSafe, coPasswordConfirmSame,
            { nameValid, emailValid, passwordValid, passwordConfirmValid ->
              nameValid
                  && emailValid
                  && passwordValid
                  && passwordConfirmValid
            }).subscribe(btnSignUp.enabled())
    subs.addAll(subErrorName, subErrorEmail, subErrorPassword, subErrorPasswordConfirm,
        subButtonEnabled)

    coNameValid.connect()
    coEmailValid.connect()
    coPasswordSafe.connect()
    coPasswordConfirmSame.connect()
  }

  override fun onStop() {
    super.onStop()
    subs.clear()
  }

  override fun onDestroy() {
    super.onDestroy()
  }
}