package com.example.aditya.postgamescreen;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by Aditya on 6/5/16.
 */
public class PostGameScreen extends AppCompatActivity
{
    private String time;
    private String name;
    public boolean updated;
    private TableLayout nameList;
    private TableLayout timeList;
    private SharedPreferences savedData;
    private SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_game);

        //gets time from previous activity
        time = getIntent().getStringExtra("time");

        //makes variable to represent anything needed to be manipulated on screen
        nameList = (TableLayout) findViewById(R.id.nameList);
        timeList = (TableLayout) findViewById(R.id.timeList);

        name = "";
        updated = false;
        savedData = this.getSharedPreferences("leaderboardData", MODE_PRIVATE);
        editor = savedData.edit();

        //transfers all saved names and times from file to leaderboard
        for (int i = 0; i < nameList.getChildCount(); i++)
        {
            TextView nameText = (TextView)(((TableRow) nameList.getChildAt(i)).getChildAt(0));
            nameText.setText(savedData.getString("nameDataPos" + i, "..."));

            TextView timeText = (TextView)(((TableRow) timeList.getChildAt(i)).getChildAt(0));
            timeText.setText(savedData.getString("timeDataPos" + i, "..."));
        }

        //if the user made the leaderboard, open popup window that asks for name
        if (updatePossible(time))
        {
            Intent intent = new Intent(getApplicationContext(), UserInputPopUp.class);
            startActivityForResult(intent, 1);
        }

    }

    //get data from popup window
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1)
        {
            if (resultCode == RESULT_OK)
            {
                name = data.getStringExtra("name");
                updateLeaderboard(name, time);
            }
        }
    }

    //returns whether or not user made the leaderboard and displays message if they did not
    public boolean updatePossible(String time)
    {
        for (int i = 0; i < timeList.getChildCount(); i++)
        {
            TextView currTime = (TextView)(((TableRow)timeList.getChildAt(i)).getChildAt(0));

            if((getLargerTime(time, currTime.getText().toString())).equals(time))
            {
                return true;
            }
        }
        Toast failMessage = Toast.makeText(getApplicationContext(),
                "You did not make the leaderboard :(", Toast.LENGTH_LONG);
        failMessage.show();
        return false;
    }

    //places user input appropriately on the leaderboard by comparing input to already existing values
    private void updateLeaderboard(String name, String time)
    {
        for (int i = 0; i < timeList.getChildCount(); i++)
        {
            TextView currName = (TextView)(((TableRow)nameList.getChildAt(i)).getChildAt(0));
            TextView currTime = (TextView)(((TableRow)timeList.getChildAt(i)).getChildAt(0));

            if((getLargerTime(time, currTime.getText().toString())).equals(time))
            {
                for (int j = nameList.getChildCount() - 1; j > i ; j--)
                {
                    TextView timeAt = (TextView)
                            (((TableRow)timeList.getChildAt(j)).getChildAt(0));
                    TextView prevTime = (TextView)
                            (((TableRow)timeList.getChildAt(j - 1)).getChildAt(0));
                    TextView nameAt = (TextView)
                            (((TableRow)nameList.getChildAt(j)).getChildAt(0));
                    TextView prevName = (TextView)
                            (((TableRow)nameList.getChildAt(j - 1)).getChildAt(0));

                    nameAt.setText(prevName.getText().toString());
                    timeAt.setText(prevTime.getText().toString());
                }

                currName.setText(name);
                currTime.setText(time);

                //saves data
                for (int k = 0; k < nameList.getChildCount(); k++)
                {
                    editor.putString("nameDataPos" + k, ((TextView)(((TableRow)nameList.
                            getChildAt(k)).getChildAt(0))).getText().toString());

                    editor.putString("timeDataPos" + k, ((TextView)(((TableRow)timeList.
                            getChildAt(k)).getChildAt(0))).getText().toString());
                }
                editor.apply();
                break;
            }
        }
    }

    //returns the larger of two times, or the String "neither" if they have the same value
    private String getLargerTime(String time1, String time2)
    {
        if (time1.indexOf(":") == -1)
            return time2;
        else if (time2.indexOf(":") == -1)
            return time1;

        int time1Mins = Integer.parseInt(time1.substring(0, time1.indexOf(":")));
        int time2Mins = Integer.parseInt(time2.substring(0, time2.indexOf(":")));
        int time1Secs = Integer.parseInt(time1.substring(time1.indexOf(":") + 1));
        int time2Secs = Integer.parseInt(time2.substring(time2.indexOf(":") + 1));

        if (time1Mins < time2Mins)
            return time2;
        else if (time2Mins < time1Mins)
            return time1;
        else if (time1Secs < time2Secs)
            return time2;
        else if (time2Secs < time1Secs)
            return time1;

        return "neither";
    }

    //makes sure nothing happens if back button is pressed
    @Override
    public void onBackPressed()
    {

    }

}


