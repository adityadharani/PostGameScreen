package com.example.aditya.postgamescreen;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by Aditya on 6/10/16.
 */
public class UserInputPopUp extends AppCompatActivity {
    private EditText input;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_input_pop_up);

        input = (EditText) findViewById(R.id.userInput);

        //When User clicks done, checks if they actually entered something. If they did, it closes
        input.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    if (input.getText().toString() != "")
                    {
                        Intent intent = new Intent();
                        intent.putExtra("name", input.getText().toString());
                        setResult(RESULT_OK, intent);
                        finish();
                        return true;
                    }
                    else
                    {
                        Toast toast = Toast.makeText(getApplicationContext(),
                                "Please enter a name", Toast.LENGTH_LONG);
                        toast.show();
                        return false;
                    }
                }
                return false;
            }

        });
    }

    //Makes sure the user doesn't close activity by clicking back button
    public void onBackPressed()
    {

    }
}
