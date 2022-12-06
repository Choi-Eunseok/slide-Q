package slideq.com.slideq;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

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

public class SelectCategoryActivity extends AppCompatActivity {

    private List<CategoryList> cateList;
    private List<QuizList> quList;
    private String categoryName, exampleQuestion;
    private String selectedCategory;
    SQLiteDatabase quizDB = null;
    ArrayAdapter<String> spinnerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_category);

        final Spinner spinner = (Spinner)findViewById(R.id.selectCategorySpinner);
        final TextView exampleQuestionText = (TextView) findViewById(R.id.selectCategoryExampleText);
        Button selectButton = (Button) findViewById(R.id.selectCategoryButton);

        spinnerAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, android.R.id.text1);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(spinnerAdapter);
        spinnerAdapter.add("CATEGORY");

        Intent intent = getIntent();

        cateList = new ArrayList<CategoryList>();

        final String URL = "https://ted12333.cafe24.com/CategoryList.php";

        SelectCategoryActivity.CategoryListse categoryListse = new SelectCategoryActivity.CategoryListse(URL, null);
        categoryListse.execute();

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                exampleQuestionText.setText(searchCate(spinner.getItemAtPosition(position).toString()));
                selectedCategory = spinner.getItemAtPosition(position).toString();

                //Toast.makeText(SelectCategory.this,"선택된 아이템 : "+spinner.getItemAtPosition(position),Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        selectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*final String URL = "http://ted12333.cafe24.com/quiz2List.php";
                ContentValues values = new ContentValues();
                values = null;

                SelectCategoryActivity.NetworkTask networkTask = new SelectCategoryActivity.NetworkTask(URL, values);
                networkTask.execute();*/

                new QuizListReceive().execute();
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

    public String searchCate(String categoryName){
        for(int i = 0; i < cateList.size(); i++){
            if(cateList.get(i).getCategoryName().equals(categoryName)){
                return cateList.get(i).getExampleQuestion();
            }
        }
        return "";
    }


    class QuizListReceive extends AsyncTask<Void, Void, String > {
        String target;

        @Override
        protected void onPreExecute(){
            target = "https://ted12333.cafe24.com/quiz2List.php";
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
                quList = new ArrayList<QuizList>();

                JSONObject jsonObject = new JSONObject(result);
                JSONArray jsonArray = jsonObject.getJSONArray("response");
                int count = 0;
                String qCategory, qQuiz, correctAnswer, wrongAnswer, createID, solvedID;
                while(count < jsonArray.length())
                {
                    JSONObject object = jsonArray.getJSONObject(count);
                    qCategory = object.getString("qCategory");
                    qQuiz = object.getString("qQuiz");
                    correctAnswer = object.getString("correctAnswer");
                    wrongAnswer = object.getString("wrongAnswer");
                    createID = object.getString("createID");
                    solvedID = object.getString("solvedID");
                    QuizList quizList = new QuizList(qCategory, qQuiz, correctAnswer, wrongAnswer, createID, solvedID);
                    quList.add(quizList);
                    count++;
                }

            }
            catch (Exception e){
                e.printStackTrace();
            }

            quizDB = openOrCreateDatabase("QUIZ", MODE_PRIVATE, null);
            //테이블이 존재하지 않으면 새로 생성합니다.
            quizDB.execSQL("CREATE TABLE IF NOT EXISTS quiz (Question VARCHAR(10000), CorrectAnswer VARCHAR(20), WrongAnswer VARCHAR(20), chec VARCHAR(20) );");

            //테이블이 존재하는 경우 기존 데이터를 지우기 위해서 사용합니다.
            quizDB.execSQL("DELETE FROM quiz");

            if(quList.size() == 0){
                Toast.makeText(SelectCategoryActivity.this, "0", Toast.LENGTH_SHORT).show();
            }


            for(int i = 0; i < quList.size(); i++){
                if(quList.get(i).getqCategory().equals(selectedCategory)){
                    quizDB.execSQL("INSERT INTO quiz (Question, CorrectAnswer, WrongAnswer, chec) " +
                            " Values (\"" + quList.get(i).getqQuiz() + "\", \"" + quList.get(i).getCorrectAnswer() + "\", \"" + quList.get(i).getWrongAnswer() + "\", \"0\" );");
                }
            }
            quizDB.close();
            AlertDialog.Builder builder = new AlertDialog.Builder(SelectCategoryActivity.this);

            // Set a title for alert dialog
            builder.setTitle("SlideQ");

            // Ask the final question
            builder.setMessage("Select category success");

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
        }
    }

    /*public class NetworkTask extends AsyncTask<Void, Void, String> {
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
            try{
                quList = new ArrayList<QuizList>();
                JSONObject j = new JSONObject(result);
                Toast.makeText(SelectCategoryActivity.this, "1", Toast.LENGTH_SHORT).show();
                JSONArray jsonArray = j.getJSONArray("response");
                int count = 0;
                String qCategory, qQuiz, correctAnswer, wrongAnswer, createID, solvedID;
                while(count < jsonArray.length())
                {
                    JSONObject object = jsonArray.getJSONObject(count);
                    qCategory = object.getString("qCategory");
                    qQuiz = object.getString("qQuiz");
                    correctAnswer = object.getString("correctAnswer");
                    wrongAnswer = object.getString("wrongAnswer");
                    createID = object.getString("createID");
                    solvedID = object.getString("solvedID");
                    QuizList quizList = new QuizList(qCategory, qQuiz, correctAnswer, wrongAnswer, createID, solvedID);
                    quList.add(quizList);
                    count++;
                }
            }
            catch (Exception e){

                e.printStackTrace();
            }

            quizDB = openOrCreateDatabase("QUIZ", MODE_PRIVATE, null);
            //테이블이 존재하지 않으면 새로 생성합니다.
            quizDB.execSQL("CREATE TABLE IF NOT EXISTS quiz (Question VARCHAR(10000), CorrectAnswer VARCHAR(20), WrongAnswer VARCHAR(20), chec VARCHAR(20) );");

            //테이블이 존재하는 경우 기존 데이터를 지우기 위해서 사용합니다.
            quizDB.execSQL("DELETE FROM quiz");

            if(quList.size() == 0){
                //Toast.makeText(SelectCategoryActivity.this, "0", Toast.LENGTH_SHORT).show();
            }


            for(int i = 0; i < quList.size(); i++){
                if(quList.get(i).getqCategory().equals(selectedCategory)){
                    quizDB.execSQL("INSERT INTO quiz (Question, CorrectAnswer, WrongAnswer, chec) " +
                            " Values (\"" + quList.get(i).getqQuiz() + "\", \"" + quList.get(i).getCorrectAnswer() + "\", \"" + quList.get(i).getWrongAnswer() + "\", \"0\" );");
                }
            }
            quizDB.close();
            AlertDialog.Builder builder = new AlertDialog.Builder(SelectCategoryActivity.this);

            // Set a title for alert dialog
            builder.setTitle("SlideQ");

            // Ask the final question
            builder.setMessage("Select category success");

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
                /*}
                }
            };

            // Set the alert dialog yes button click listener
            builder.setPositiveButton("okay", dialogClickListener);

            // Set the alert dialog no button click listener
            //builder.setNegativeButton("No",dialogClickListener);

            AlertDialog dialog = builder.create();
            // Display the alert dialog on interface
            dialog.show();
        }
    }*/
}
