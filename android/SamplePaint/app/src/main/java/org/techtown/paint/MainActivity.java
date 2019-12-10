package org.techtown.paint;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        BestPaintBoard view = new BestPaintBoard(this);
//        PaintBoard view = new PaintBoard(this);

        setContentView(view);

    }
}
