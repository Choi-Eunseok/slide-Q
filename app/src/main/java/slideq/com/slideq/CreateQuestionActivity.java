package slideq.com.slideq;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class CreateQuestionActivity extends AppCompatActivity {

    private List<CategoryList> cateList;
    private String userID;
    private List<QuizList> quList;
    private List<QuizList> seQuList;
    private String categoryName, exampleQuestion;
    private String selectedCategory = "";
    ArrayAdapter<String> spinnerAdapter;

    EditText question,correctAnswer, wrongAnswer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_question);

        Intent intent = getIntent();
        userID = intent.getStringExtra("userID");

        final Spinner spinner = (Spinner)findViewById(R.id.createQuestionSpinner);

        Button selectButton = (Button) findViewById(R.id.createQuestionButton);

        quList = new ArrayList<QuizList>();
        new QuizListReceive().execute();

        spinnerAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, android.R.id.text1);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(spinnerAdapter);
        spinnerAdapter.add("CATEGORY");

        question = (EditText) findViewById(R.id.createQuestionText);
        correctAnswer = (EditText) findViewById(R.id.createQuestionCorrectAnswerText);
        wrongAnswer = (EditText) findViewById(R.id.createQuestionWrongAnswerText);

        cateList = new ArrayList<CategoryList>();

        final String URL = "https://ted12333.cafe24.com/CategoryList.php";

        CreateQuestionActivity.CategoryListse categoryListse = new CreateQuestionActivity.CategoryListse(URL, null);
        categoryListse.execute();

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedCategory = spinner.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        final EditText qText = (EditText) findViewById(R.id.createQuestionText);
        final EditText cText = (EditText) findViewById(R.id.createQuestionCorrectAnswerText);
        final EditText wText = (EditText) findViewById(R.id.createQuestionWrongAnswerText);
        selectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String quizQuiz = qText.getText().toString();
                final String correct = cText.getText().toString();
                final String wrong = wText.getText().toString();
                searchQuizCate(selectedCategory);

                final String URL = "https://ted12333.cafe24.com/create2quiz.php";

                /* DB 대조 */
                ContentValues values = new ContentValues();
                values.put("qCategory", selectedCategory);
                values.put("qQuiz", quizQuiz);
                values.put("correctAnswer", correct);
                values.put("wrongAnswer", wrong);
                values.put("createID", userID);
                values.put("solvedID","");

                CreateQuestionActivity.NetworkTask networkTask = new CreateQuestionActivity.NetworkTask(URL, values);
                networkTask.execute();

            }
        });
    }

    public class CategoryListse extends AsyncTask<Void, Void, String> {
        String url;
        ContentValues values;

        CategoryListse(String url, ContentValues values) {
            this.url = url;
            this.values = values;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //progress bar를 보여주는 등등의 행위
        }

        @Override
        protected String doInBackground(Void... params) {
            String result;
            RequestHttpURLConnection requestHttpURLConnection = new RequestHttpURLConnection();
            result = requestHttpURLConnection.request(url, values);
            return result; // 결과가 여기에 담깁니다. 아래 onPostExecute()의 파라미터로 전달됩니다.
        }

        @Override
        protected void onPostExecute(String result) {
            // 통신이 완료되면 호출됩니다.
            // 결과에 따른 UI 수정 등은 여기서 합니다.
            //Toast.makeText(getApplicationContext(), result, Toast.LENGTH_LONG).show();
            try{
                JSONObject jsonObject = new JSONObject(result);
                JSONArray jsonArray = jsonObject.getJSONArray("response");
                int count = 0;
                while(count < jsonArray.length())
                {
                    JSONObject object = jsonArray.getJSONObject(count);
                    categoryName = object.getString("categoryName");
                    exampleQuestion = object.getString("exampleQuestion");
                    CategoryList categoryList = new CategoryList(categoryName, exampleQuestion);
                    cateList.add(categoryList);
                    count++;
                }
            } catch (Exception e){
                e.printStackTrace();
            }
            for(int i = 0; i < cateList.size(); i++){
                spinnerAdapter.add(cateList.get(i).getCategoryName());
            }
            spinnerAdapter.notifyDataSetChanged();
        }
    }

    class QuizListReceive extends AsyncTask<Void, Void, String > {
        String target;

        @Override
        protected void onPreExecute(){
            target = "http://ted12333.cafe24.com/quiz2List.php";
        }

        @Override
        protected String doInBackground(Void... voids){
            try{
                URL url = new URL(target);
                HttpURLConnection httpURLConnection = (HttpURLConnection)url.openConnection();
                InputStream inputStream = httpURLConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                String temp;
                StringBuilder stringBuilder = new StringBuilder();
                while((temp = bufferedReader.readLine()) != null){
                    stringBuilder.append(temp + "\n");
                }
                bufferedReader.close();
                inputStream.close();
                httpURLConnection.disconnect();
                return stringBuilder.toString().trim();
            }
            catch (Exception e){
                e.printStackTrace();
            }
            return null;
        }

        @Override
        public void onProgressUpdate(Void... values){
            super.onProgressUpdate(values);
        }

        @Override
        public void onPostExecute(String result){
            try{
                JSONObject jsonObject = new JSONObject(result);
                JSONArray jsonArray = jsonObject.getJSONArray("response");
                int count = 0;
                String quizCategory, quizQuiz, Num, quizCateNum, correctAnswer, wrongAnswer, createID, solvedID;
                while(count < jsonArray.length())
                {
                    JSONObject object = jsonArray.getJSONObject(count);
                    quizCategory = object.getString("qCategory");
                    quizQuiz = object.getString("qQuiz");
                    correctAnswer = object.getString("correctAnswer");
                    wrongAnswer = object.getString("wrongAnswer");
                    createID = object.getString("createID");
                    solvedID = object.getString("solvedID");
                    QuizList quizList = new QuizList(quizCategory, quizQuiz, correctAnswer, wrongAnswer, createID, solvedID);
                    quList.add(quizList);
                    count++;
                }

            }
            catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    public void searchQuizCate(String categoryName){
        seQuList = new ArrayList<QuizList>();
        for(int i = 0; i < quList.size(); i++){
            if(quList.get(i).getqCategory().equals(categoryName)){
                seQuList.add(quList.get(i));
            }
        }
    }

    public class NetworkTask extends AsyncTask<Void, Void, String> {
        String url;
        ContentValues values;

        NetworkTask(String url, ContentValues values) {
            this.url = url;
            this.values = values;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //progress bar를 보여주는 등등의 행위
        }

        @Override
        protected String doInBackground(Void... params) {
            String result;
            RequestHttpURLConnection requestHttpURLConnection = new RequestHttpURLConnection();
            result = requestHttpURLConnection.request(url, values);
            return result; // 결과가 여기에 담깁니다. 아래 onPostExecute()의 파라미터로 전달됩니다.
        }

        @Override
        protected void onPostExecute(String result) {
            // 통신이 완료되면 호출됩니다.
            // 결과에 따른 UI 수정 등은 여기서 합니다.
            //Toast.makeText(getApplicationContext(), result, Toast.LENGTH_LONG).show();
            try {
                JSONObject jsonResponse = new JSONObject(result);
                boolean success = jsonResponse.getBoolean("success");
                if (success) {
                    /*AlertDialog.Builder builder = new AlertDialog.Builder(CreateCategoryActivity.this);
                    builder.setMessage("Create category success").setPositiveButton("okay", null).create().show();
                    finish();*/
                    AlertDialog.Builder builder = new AlertDialog.Builder(CreateQuestionActivity.this);

                    // Set a title for alert dialog
                    builder.setTitle("SlideQ");

                    // Ask the final question
                    builder.setMessage("Create question success");

                    // Set click listener for alert dialog buttons
                    DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            switch(which){
                                case DialogInterface.BUTTON_POSITIVE:
                                    // User clicked the Yes button
                                    finish();
                                    break;

                                /*case DialogInterface.BUTTON_NEGATIVE:
                                    // User clicked the No button
                                    break;*/
                            }
                        }
                    };

                    // Set the alert dialog yes button click listener
                    builder.setPositiveButton("okay", dialogClickListener);

                    // Set the alert dialog no button click listener
                    //builder.setNegativeButton("No",dialogClickListener);

                    AlertDialog dialog = builder.create();
                    // Display the alert dialog on interface
                    dialog.show();
                } else {
                    AlertDialog.Builder builder = new AlertDialog.Builder(CreateQuestionActivity.this);
                    builder.setMessage("Create question failed").setNegativeButton("retry", null).create().show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}
