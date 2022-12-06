package slideq.com.slideq;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class CreateCategoryActivity extends AppCompatActivity {

    private String userID;

    private String categoryName, exampleQuestion;
    private List<CategoryList> cateList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_category);

        Button finishButton = (Button) findViewById(R.id.createCategoryButton);

        Intent intent = getIntent();
        userID = intent.getStringExtra("userID");

        cateList = new ArrayList<CategoryList>();

        final String URL = "https://ted12333.cafe24.com/CategoryList.php";

        CreateCategoryActivity.CategoryListse categoryListse = new CreateCategoryActivity.CategoryListse(URL, null);
        categoryListse.execute();

        final EditText categoryNameText = (EditText) findViewById(R.id.createCategoryText);
        final EditText exampleQuestionText = (EditText) findViewById(R.id.selectCategoryExampleText);

        finishButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final String categoryName = categoryNameText.getText().toString();
                String exampleQuestion = exampleQuestionText.getText().toString();

                if (searchSameCate(categoryName)) {
                    final String URL = "https://ted12333.cafe24.com/CreateCategory.php";

                    /* DB 대조 */
                    ContentValues values = new ContentValues();
                    values.put("categoryName", categoryName);
                    values.put("exampleQuestion", exampleQuestion);
                    values.put("creatorID", userID);

                    CreateCategoryActivity.NetworkTask networkTask = new CreateCategoryActivity.NetworkTask(URL, values);
                    networkTask.execute();
                } else {
                    Toast.makeText(CreateCategoryActivity.this, "Category name exists", Toast.LENGTH_SHORT).show();
                }
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
            try {
                JSONObject jsonObject = new JSONObject(result);
                JSONArray jsonArray = jsonObject.getJSONArray("response");
                int count = 0;
                while (count < jsonArray.length()) {
                    JSONObject object = jsonArray.getJSONObject(count);
                    categoryName = object.getString("categoryName");
                    exampleQuestion = object.getString("exampleQuestion");
                    CategoryList categoryList = new CategoryList(categoryName, exampleQuestion);
                    cateList.add(categoryList);
                    count++;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public boolean searchSameCate(String tempCateName) {
        for (int i = 0; i < cateList.size(); i++) {
            if (cateList.get(i).getCategoryName().equals(tempCateName)) {
                return false;
            }
        }
        return true;
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
                    AlertDialog.Builder builder = new AlertDialog.Builder(CreateCategoryActivity.this);

                    // Set a title for alert dialog
                    builder.setTitle("SlideQ");

                    // Ask the final question
                    builder.setMessage("Create category success");

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
                    AlertDialog.Builder builder = new AlertDialog.Builder(CreateCategoryActivity.this);
                    builder.setMessage("Create category failed").setNegativeButton("retry", null).create().show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}
