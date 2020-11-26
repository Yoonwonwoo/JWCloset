package com.example.jwcloset;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.Button;



public class StylePopupActivity extends AppCompatActivity {
    Button styleBtn1, styleBtn2, styleBtn3, styleBtn4, styleBtn5, styleBtn6, styleBtn7, styleBtn8, styleBtn9;
    String styleString;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_style_popup);

        styleBtn1 = findViewById(R.id.styleBtn1);
        styleBtn2 = findViewById(R.id.styleBtn2);
        styleBtn3 = findViewById(R.id.styleBtn3);
        styleBtn4 = findViewById(R.id.styleBtn4);
        styleBtn5 = findViewById(R.id.styleBtn5);
        styleBtn6 = findViewById(R.id.styleBtn6);
        styleBtn7 = findViewById(R.id.styleBtn7);
        styleBtn8 = findViewById(R.id.styleBtn8);
        styleBtn9 = findViewById(R.id.styleBtn9);

        onClickListener();

    }

    private void onClickListener(){
        Button.OnClickListener onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()){
                    case R.id.styleBtn1:
                        putIntent(styleBtn1.getText().toString());
                        break;

                    case R.id.styleBtn2:
                        putIntent(styleBtn2.getText().toString());
                        break;

                    case R.id.styleBtn3:
                        putIntent(styleBtn3.getText().toString());
                        break;

                    case R.id.styleBtn4:
                        putIntent(styleBtn4.getText().toString());
                        break;

                    case R.id.styleBtn5:
                        putIntent(styleBtn5.getText().toString());
                        break;

                    case R.id.styleBtn6:
                        putIntent(styleBtn6.getText().toString());
                        break;

                    case R.id.styleBtn7:
                        putIntent(styleBtn7.getText().toString());
                        break;

                    case R.id.styleBtn8:
                        putIntent(styleBtn8.getText().toString());
                        break;

                    case R.id.styleBtn9:
                        putIntent(styleBtn9.getText().toString());
                        break;
                }
            }
        };

        styleBtn1.setOnClickListener(onClickListener);
        styleBtn2.setOnClickListener(onClickListener);
        styleBtn3.setOnClickListener(onClickListener);
        styleBtn4.setOnClickListener(onClickListener);
        styleBtn5.setOnClickListener(onClickListener);
        styleBtn6.setOnClickListener(onClickListener);
        styleBtn7.setOnClickListener(onClickListener);
        styleBtn8.setOnClickListener(onClickListener);
        styleBtn9.setOnClickListener(onClickListener);
    }

    private void putIntent(String style){
        Intent intent = new Intent();
        intent.putExtra("style", style);
        setResult(RESULT_OK, intent);
        finish();
    }
}