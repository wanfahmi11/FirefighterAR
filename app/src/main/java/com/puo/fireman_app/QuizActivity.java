package com.puo.fireman_app;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.puo.arcore_project.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class QuizActivity extends AppCompatActivity implements ChoicesAdapter.RecyclerViewClickListener {

    private static final String PROMPT_CORRECT_ANSWER = "Correct!",
                                PROMPT_WRONG_ANSWER = "Wrong. Please try again";

    private List<Question> questions;
    private int score = 0, currentQuestion = 0;
    private TextView questionText;
    private RecyclerView choicesRV;
    private List<Object> choicesList;
    private ChoicesAdapter choicesAdapter;
    private Question current;
    private ImageView questionImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);

        questionText = findViewById(R.id.questionText);
        questionImage = findViewById(R.id.questionImage);
        choicesRV = findViewById(R.id.choices);

        choicesList = new ArrayList<>();
        choicesAdapter = new ChoicesAdapter(choicesList, this);
        choicesRV.setLayoutManager(new LinearLayoutManager(this));
        choicesRV.setAdapter(choicesAdapter);

        //Show back button on the ActionBar
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        questions = new ArrayList<>();

        buildQuestionDB();
        drawQuestion(false);

    }

    @Override
    public void answerClicked(View v, int position) {
        if (position != current.getAnswerIndex()) {
            Toast.makeText(this, PROMPT_WRONG_ANSWER, Toast.LENGTH_SHORT).show();
        }
        else {
            Toast.makeText(this, PROMPT_CORRECT_ANSWER, Toast.LENGTH_SHORT).show();
            score++;
        }

        drawQuestion(true);
    }

    //Build the so called question database
    private void buildQuestionDB() {
        //answers is an List of String, so we can push as many answers as we want
        //Questions constructors will receive question, answerIndex and arrayList of answers
        //answerIndex is counted from 0 (of course), e.g : If your answer is the 2nd choice, the answerIndex is 1

        List<Object> answers = new ArrayList<>();

        answers.add("A. Air");
        answers.add("B. Anticipate");
        answers.add("C. Aim");
        answers.add("D. Attack");
        questions.add(new Question("1. When using a fire extinguisher, the acronym PASS " +
                "will help you remember the steps to extinguishing a fire. What does the A in PASS stand for?",
                2 , answers));

        answers.clear();

        answers.add("A. Top and work down");
        answers.add("B. Base");
        answers.add("C. To the right");
        answers.add("D. To the left");
        questions.add(new Question("2. When using the fire extinguisher you are to start " +
                "at the ____________ of the fire?", 1, answers ));

        answers.clear();

        answers.add("A. In the closet in your bedroom");
        answers.add("B. In the garage");
        answers.add("C. At the firehouse");
        answers.add("D. In the kitchen where it is accessible");
        questions.add(new Question("3. A fire extinguisher should be kept...", 3, answers ));

        answers.clear();

        answers.add("A. Up and down");
        answers.add("B. Side to side");
        questions.add(new Question("4. Which direction should you sweep while using the " +
                "fire extinguisher? ", 1 , answers));

        answers.clear();

        answers.add("A. True");
        answers.add("B. False");
        questions.add(new Question("5. Electrical shock can be prevented by keeping " +
                "electrical appliances away from water? ", 0, answers));

        answers.clear();

        answers.add("A. its heat source is removed");
        answers.add("B. all its fuel is burned up");
        answers.add("C. its oxygen runs out");
        answers.add("D. any of the above answers occurs");
        questions.add(new Question("6. A fire will continue to burn until_____", 3, answers));

        answers.clear();

        answers.add("A. True");
        answers.add("B. False");
        questions.add(new Question("7. Extension cords should not be used to permanently " +
                "power equipment.", 0, answers));

        answers.clear();

        answers.add("A. the stairs");
        answers.add("B. an elevator");
        questions.add(new Question("8. You should always use _________ to evacuate the upper " +
                "levels of a facility during a fire evacuation", 0, answers));

        answers.clear();

        answers.add("A. feel the handle with your palm then feel the door from the bottom to top");
        answers.add("B. feel the handle with your palm then feel the door from the top to bottom");
        answers.add("C. feel the handle with the back of your hand then feel the door from bottom to top");
        answers.add("D. feel the handle with the back of your hand then feel the door from top to bottom");
        questions.add(new Question("9. How should you check to determine if a door is too " +
                "hot to open and proceed through", 2, answers));

        answers.clear();


        answers.add("A. combustible materials");
        answers.add("B. electricity");
        answers.add("C. flammable gases and liquids");
        questions.add(new Question("9. The most common fires in office environments are " +
                "those that involve _____ ", 1, answers));

    }

    //Will set text to question and populate answer choices
    private void drawQuestion(boolean next) {

        if (next) {
            currentQuestion++;

            if (currentQuestion == questions.size()) {
                showResult();
                return;
            }
        }

        //Remove all choices from previous question
        choicesList.clear();

        current = questions.get(currentQuestion);


        //Set question image if there's any and hide if not provided
        if (current.getQuestionImage() != 0) {
            Picasso.get().load(current.getQuestionImage()).into(questionImage);
            questionImage.setVisibility(View.VISIBLE);
        }
        else {
            questionImage.setVisibility(View.GONE);
        }

        //Set question to question text in layout
        questionText.setText(current.getQuestion());


        List<Object> currentAnswers = current.getAnswers();

        //Populate the choice
        for (int i = 0; i < currentAnswers.size(); i++) {
            choicesList.add(currentAnswers.get(i));
        }

        choicesAdapter.notifyDataSetChanged();
    }

    //show final result
    private void showResult() {

        //Hide what should be hide and show the result
        questionImage.setVisibility(View.GONE);
        choicesList.clear();
        choicesAdapter.notifyDataSetChanged();
        questionText.setText("Tahniah! \n Markah anda ialah " + score + "/" + questions.size());
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }
}
