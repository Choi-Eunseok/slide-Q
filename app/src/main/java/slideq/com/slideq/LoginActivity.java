package slideq.com.slideq;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class LoginActivity extends AppCompatActivity {

    private EditText idText;
    private EditText passwordText;

    private String id = "";
    private String pw = "";

    JSONObject jsonResponse;
    boolean success;

    SharedPreferences logininformation;

    CheckBox checkBox;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        logininformation = getSharedPreferences("setting",0);

        checkBox = (CheckBox) findViewById(R.id.checkBox);

        idText = (EditText) findViewById(R.id.IDText);
        passwordText = (EditText) findViewById(R.id.passwordText);

        final Button loginButton = (Button) findViewById(R.id.LoginButton);
        final TextView registerButton = (TextView) findViewById(R.id.register);

        if(logininformation.getString("id","") != "" && logininformation.getString("password","") != ""){
            final String URL = "https://ted12333.cafe24.com/Login.php";

            /* DB 대조 */
            ContentValues values = new ContentValues();
            values.put("userID", logininformation.getString("id",""));
            values.put("userPassword", logininformation.getString("password",""));

            checkBox.setChecked(true);

            NetworkTask networkTask = new NetworkTask(URL, values);
            networkTask.execute();
        }

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*Intent registerintent = new Intent(LoginActivity.this, RegisterActivitiy.class);
                LoginActivity.this.startActivity(registerintent);*/
                new LoginActivity.BackgroundTask().execute();
            }
        });

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final String URL = "https://ted12333.cafe24.com/Login.php";
                final String userID = idText.getText().toString();
                final String userPassword = passwordText.getText().toString();

                /* DB 대조 */
                ContentValues values = new ContentValues();
                values.put("userID", userID);
                values.put("userPassword", userPassword);

                NetworkTask networkTask = new NetworkTask(URL, values);
                networkTask.execute();
            }
        });
    }

    class BackgroundTask extends AsyncTask<Void, Void, String> {
        String target;

        @Override
        protected void onPreExecute() {
            target = "http://ted12333.cafe24.com/List.php";
        }

        @Override
        protected String doInBackground(Void... voids) {
            try {
                URL url = new URL(target);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                InputStream inputStream = httpURLConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                String temp;
                StringBuilder stringBuilder = new StringBuilder();
                while ((temp = bufferedReader.readLine()) != null) {
                    stringBuilder.append(temp + "\n");
                }
                bufferedReader.close();
                inputStream.close();
                httpURLConnection.disconnect();
                return stringBuilder.toString().trim();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        public void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }

        @Override
        public void onPostExecute(String result) {
            Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
            intent.putExtra("userList", result);
            LoginActivity.this.startActivity(intent);
        }
    }

    public class NetworkTask extends AsyncTask<Void, Void, String> {
        String url;
        ContentValues values;

        NetworkTask(String url, ContentValues values){
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
                if(success){
                    if(checkBox.isChecked()){
                        SharedPreferences.Editor editor = logininformation.edit();
                        editor.putString("id", values.get("userID").toString());
                        editor.putString("password", values.get("userPassword").toString());
                        editor.commit();
                    }else{
                        SharedPreferences.Editor editor = logininformation.edit();
                        editor.clear();
                        editor.commit();
                    }
                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    intent.putExtra("userID", values.get("userID").toString());
                    intent.putExtra("userPassword", values.get("userPassword").toString());
                    startActivity(intent);
                }else{
                    Toast.makeText(getApplicationContext(),"login fail",Toast.LENGTH_LONG).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}
