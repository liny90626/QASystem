package org.linky.qasystem.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import org.linky.qasystem.R;

public class MainActivity extends Activity {

    private Button mSingleChoiceBtn;
    private Button mMultipleChoiceBtn;
    private Button mTrueOrFalseBtn;
    //private Button mSingleChoiceWithPicturesBtn;
    private Button mMyErros;

    private ImageView mLeft;
    private TextView mTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);

        mLeft = (ImageView) findViewById(R.id.left);
        mTitle = (TextView) findViewById(R.id.title);
        mSingleChoiceBtn = (Button) findViewById(R.id.single_choice);
        mMultipleChoiceBtn = (Button) findViewById(R.id.multiple_choice);
        mTrueOrFalseBtn = (Button) findViewById(R.id.true_or_false_choice);
        mMyErros = (Button) findViewById(R.id.my_errors);
        //mSingleChoiceWithPicturesBtn = (Button) findViewById(R.id.single_choice_with_pictures);

        mLeft.setVisibility(View.GONE);
        mTitle.setText(R.string.qa_test);

        mSingleChoiceBtn.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                Intent intent = new Intent(MainActivity.this, SingleChoiceActivity.class);
                startActivity(intent);
            }
        });

        mMultipleChoiceBtn.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                Intent intent = new Intent(MainActivity.this, MultipleChoiceActivity.class);
                startActivity(intent);
            }
        });

        mTrueOrFalseBtn.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                Intent intent = new Intent(MainActivity.this, TrueOrFalseActivity.class);
                startActivity(intent);
            }
        });

        mMyErros.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                Intent intent = new Intent(MainActivity.this, MyErrorQuestionActivity.class);
                startActivity(intent);
            }
        });

        /*
        mSingleChoiceWithPicturesBtn.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                Intent intent = new Intent(MainActivity.this, SingleChoiceWithPicsActivity.class);
                startActivity(intent);
            }
        });
        */

    }

}
