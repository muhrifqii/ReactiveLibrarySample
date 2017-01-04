package io.github.muhrifqii.reactivelibrarysample;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;
import com.jakewharton.rxbinding.view.RxView;
import rx.subscriptions.CompositeSubscription;

public class MainActivity extends AppCompatActivity {

  final CompositeSubscription subs = new CompositeSubscription();
  private TextView textView;

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    textView = (TextView) findViewById(R.id.tv_hello_reactive);
  }

  @Override protected void onStart() {
    super.onStart();

    subs.add(RxView.clicks(textView)
        .subscribe(
            __ -> Toast.makeText(MainActivity.this, "I am ready!", Toast.LENGTH_SHORT).show()));
    subs.add(RxView.longClicks(textView)
        .subscribe(
            __ -> Toast.makeText(MainActivity.this, "Long click happened", Toast.LENGTH_SHORT)
                .show()));
  }

  @Override protected void onStop() {
    super.onStop();
    subs.unsubscribe();
  }

  @Override protected void onDestroy() {
    subs.clear();
    super.onDestroy();
  }
}
