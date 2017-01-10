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

import android.app.AlertDialog;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;
import com.trello.rxlifecycle2.components.support.RxAppCompatActivity;

/**
 * Created on   : 05/01/17
 * Author       : muhrifqii
 * Name         : Muhammad Rifqi Fatchurrahman Putra Danar
 * Github       : https://github.com/muhrifqii
 * LinkedIn     : https://linkedin.com/in/muhrifqii
 */

public abstract class SampleRx2BaseActivity extends RxAppCompatActivity {
  @Override protected void onResume() {
    super.onResume();
    popUpForSupports();
  }

  private void popUpForSupports(){
    int counter = PreferenceManager.getDefaultSharedPreferences(this).getInt("x", 0);
    counter++;
    if (counter > 3) {
      new AlertDialog.Builder(this).setTitle("Stars for supports")
          .setMessage("Please star this project on github to support the contributor(s)")
          .create()
          .show();
      counter = 0;
    }
    PreferenceManager.getDefaultSharedPreferences(this).edit().putInt("x", counter).apply();
  }
}
