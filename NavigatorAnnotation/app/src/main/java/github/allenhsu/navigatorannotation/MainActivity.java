package github.allenhsu.navigatorannotation;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;


import github.allenhsu.navigaotrannotation.annotation.NewIntent;
import github.allenhsu.navigatorannotation.processor.Navigator;

@NewIntent
public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Navigator.setDebuggable(true);
    }
}
