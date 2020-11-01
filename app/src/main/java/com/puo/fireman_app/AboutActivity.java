package com.puo.fireman_app;

import android.os.Bundle;
import com.puo.arcore_project.R;
import androidx.appcompat.app.AppCompatActivity;

import android.text.Html;
import android.widget.TextView;

public class AboutActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        //Show back button on the ActionBar
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        TextView textView = findViewById(R.id.about);
        textView.setText(Html.fromHtml(" <h3>Our Vision</h3>\n" +
                "\n" +
                "    Healthy, safe and productive life and enterprise.\n" +
                "    In the long run, we want to create a safe and healthy working life for people in Malaysia.\n" +
                "    We want to see human safety filled with high knowledge in preventing or dealing with fire issues in the workplace or study.\n" +
                "    We want to ensure that the community can use this facility very carefully.\n" +
                "\n" +
                "    <h3>Strategic Goals</h3>\n" +
                "    Five strategic priorities direct our activities and help us implement our vision and achieve our strategic objectives.\n" +
                "    Rule: Increase focus on compliance through enforcement knowledge based on fire risk in Malaysia\n" +
                "    Promoting: Supporting, educating and raising awareness to prevent accidents, injuries and health.\n" +
                "\n" +
                "    Ungku Omar Polytechnic Student\n" +
                "    Diploma Teknologi Maklumat (Teknologi Digital)\n" +
                "\n" +
                "    <ol>\n" +
                "        <li>Muhammad Khairul Aizat Bin Ahmad Yaziz (01DDT17F2022)</li>\n" +
                "        <li>Muhammad Shahkimi Hakim Bin Mohd Shukri (01DDT18F1099)</li>\n" +
                "        <li>Muhammad Faris Bin Zulkifli (01DDT18F1150)</li>\n" +
                "    </ol>"));
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }
}