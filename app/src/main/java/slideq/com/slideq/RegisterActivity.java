package slideq.com.slideq;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
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

public class RegisterActivity extends AppCompatActivity {

    private List<User> saveList;
    private int chec;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        Intent intent = getIntent();

        saveList = new ArrayList<User>();

        try{
            JSONObject jsonObject = new JSONObject(intent.getStringExtra("userList"));
            JSONArray jsonArray = jsonObject.getJSONArray("response");
            int count = 0;
            String userID, userPassword, userName, userAge;
            while(count < jsonArray.length())
            {
                JSONObject object = jsonArray.getJSONObject(count);
                userID = object.getString("userID");
                userPassword = object.getString("userPassword");
                userName = object.getString("userName");
                userAge = object.getString("userAge");
                User user = new User(userID, userPassword, userName, userAge);
                if(!userID.equals("admin")) {
                    saveList.add(user);
                }
                count++;
            }
        } catch (Exception e){
            e.printStackTrace();
        }

        final EditText idText = (EditText) findViewById(R.id.registerIDText);
        final EditText passwordText = (EditText) findViewById(R.id.registerPasswordText);
        final EditText passwordCheck = (EditText) findViewById(R.id.registerPasswordCheck);
        final EditText nameText = (EditText) findViewById(R.id.registerNameText);
        final EditText ageText = (EditText) findViewById(R.id.registerAgeText);
        Button checkButton = (Button) findViewById(R.id.checkButton);
        Button registerButton = (Button) findViewById(R.id.registerButton);

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String userID = idText.getText().toString();
                final String userPassword = passwordText.getText().toString();
                final String userName = nameText.getText().toString();
                final int userAge = Integer.parseInt(ageText.getText().toString());

                if(chec == 0){
                    Toast.makeText(RegisterActivity.this,"Check your ID", Toast.LENGTH_SHORT).show();
                } else{
                    if(passwordText.getText().toString().equals(userPassword)){
                        final String URL = "https://ted12333.cafe24.com/Register.php";

                        /* DB 대조 */
                        ContentValues values = new ContentValues();
                        values.put("userID", userID);
                        values.put("userPassword", userPassword);
                        values.put("userName", userName);
                        values.put("userAge", userAge);

                        RegisterActivity.NetworkTask networkTask = new RegisterActivity.NetworkTask(URL, values);
                        networkTask.execute();
                    }else{
                        Toast.makeText(RegisterActivity.this,"Check your password", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        checkButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String userID = idText.getText().toString();
                if(searchSameID(userID)){
                    Toast.makeText(RegisterActivity.this,"You can use this ID", Toast.LENGTH_SHORT).show();
                    chec = 1;
                }
                else{
                    Toast.makeText(RegisterActivity.this, "ID already exists", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public boolean searchSameID(String tempUserID){
        for(int i = 0; i < saveList.size(); i++){
            if(saveList.get(i).getUserID().equals(tempUserID)){
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
                    AlertDialog.Builder builder = new AlertDialog.Builder(RegisterActivity.this);

                    // Set a title for alert dialog
                    builder.setTitle("SlideQ");

                    // Ask the final question
                    builder.setMessage("Register success");

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
                    AlertDialog.Builder builder = new AlertDialog.Builder(RegisterActivity.this);
                    builder.setMessage("failed in registration").setNegativeButton("retry", null).create().show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

}
