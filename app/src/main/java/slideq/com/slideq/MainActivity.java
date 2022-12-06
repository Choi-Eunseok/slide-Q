package slideq.com.slideq;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.android.volley.Response;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private final String dbName = "QUIZ";
    private final String tableName = "quiz";

    ArrayList<HashMap<String, String>> quList;

    private List<QuizList> quiList;

    private String userID;

    private Button onBtn, offBtn;
    private ToggleButton toggleBtn;
    private int cateCheck = 0;

    ListView list;
    private static final String TAG_QUESTION = "Question";
    private static final String TAG_CORRECTANSWER ="CorrectAnswer";
    private static final String TAG_WRONGANSWER ="WrongAnswer";
    private static final String TAG_CHEC ="chec";

    private Boolean toggle = false;
    private Button itemShop;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //저장된 값을 불러오기 위해 같은 네임파일을 찾음.
        SharedPreferences sf = getSharedPreferences("sFile",MODE_PRIVATE);
        //text라는 key에 저장된 값이 있는지 확인. 아무값도 들어있지 않으면 ""를 반환
        toggle = sf.getBoolean("toggle",false);
        Intent intent2 = new Intent(MainActivity.this, ScreenService.class);
        if(toggle) startService(intent2);
        itemShop = (Button) findViewById(R.id.itemShop);
        quiList = new ArrayList<QuizList>();
        SQLiteDatabase ReadDB = this.openOrCreateDatabase("QUIZ", MODE_PRIVATE, null);
        String tableName = "quiz";
        String chec = "1";
        //SELECT문을 사용하여 테이블에 있는 데이터를 가져옵니다..
        ReadDB.execSQL("CREATE TABLE IF NOT EXISTS quiz (Question VARCHAR(10000), CorrectAnswer VARCHAR(20), WrongAnswer VARCHAR(20), chec VARCHAR(20) );");
        Cursor c = (Cursor) ReadDB.rawQuery("select * from " +tableName+ " where chec = "+chec+";",null);

        if (c != null) {
            if (c.moveToFirst()) {
                do {
                    //테이블에서 두개의 컬럼값을 가져와서
                    String qu = c.getString(c.getColumnIndex(TAG_QUESTION));
                    String co = c.getString(c.getColumnIndex(TAG_CORRECTANSWER));
                    String wr = c.getString(c.getColumnIndex(TAG_WRONGANSWER));
                    String ch = c.getString(c.getColumnIndex(TAG_CHEC));

                    //HashMap에 넣습니다.
                    HashMap<String,String> quiz;
                    quiz = new HashMap<String,String>();

                    quiz.put(TAG_QUESTION,qu);
                    quiz.put(TAG_CORRECTANSWER,co);
                    quiz.put(TAG_WRONGANSWER,wr);
                    quiz.put(TAG_CHEC,ch);

                    if(quiz == null){
                        Toast.makeText(MainActivity.this, "quiz is null", Toast.LENGTH_SHORT);
                    }
                    //ArrayList에 추가합니다..

                    if(ch.equals("1")){
                        String temp = "";
                        new QuizListReceive().execute();

                        for(int i = 0; i < quiList.size(); i++){
                            temp = quiList.get(i).solvedID;
                            temp ="\n" + userID;
                        }

                        final String URL = "https://ted12333.cafe24.com/updateSolved.php";

                        /* DB 대조 */
                        ContentValues values = new ContentValues();
                        values.put("qQuiz", qu);
                        values.put("solvedID", temp);

                        MainActivity.NetworkTask networkTask = new MainActivity.NetworkTask(URL, values);
                        networkTask.execute();
                    }

                } while (c.moveToNext());
            }
        }

        ReadDB.close();

        TextView idText = (TextView) findViewById(R.id.idViewText);
        TextView passwordText = (TextView) findViewById(R.id.passwordViewText);
        TextView welcomeMessage = (TextView) findViewById(R.id.welcomeMessage);
        Button managementButton = (Button) findViewById(R.id.managementButton);
        Button categorySelectButton = (Button) findViewById(R.id.categorySelectButton);
        Button categoryCreateButton = (Button) findViewById(R.id.categoryCreateButton);
        Button questionCreateButton = (Button) findViewById(R.id.questionCreateButton);
        toggleBtn = (ToggleButton) findViewById(R.id.toggleBtn);
        toggleBtn.setChecked(toggle);
        toggleBtn.setOnClickListener(this);
        itemShop.setOnClickListener(this);
        SQLiteDatabase infoDB = null;

        infoDB = openOrCreateDatabase("INFO", MODE_PRIVATE, null);
        //테이블이 존재하지 않으면 새로 생성합니다.
        infoDB.execSQL("CREATE TABLE IF NOT EXISTS INFO (info INT);");
//        테이블이 존재하는 경우 기존 데이터를 지우기 위해서 사용합니다.
//        infoDB.execSQL("DELETE FROM INFO");

        Cursor cursor = infoDB.rawQuery("SELECT * FROM INFO", null);
        if(cursor.getCount() == 0) {
            infoDB.execSQL("INSERT INTO INFO(info) " +
                    " Values (10000);");
            for (int i = 1; i < 11; i++) // 0: point 1~10: item 구매여부
                infoDB.execSQL("INSERT INTO INFO(info) " +
                        " Values (0);");
        }

        infoDB.close();
//        onBtn= (Button)findViewById(R.id.onBtn);
//        offBtn= (Button)findViewById(R.id.offbtn);

//        onBtn.setOnClickListener(new View.OnClickListener(){
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(MainActivity.this, ScreenService.class);
//                startService(intent);
//
//                ActivityCompat.finishAffinity(MainActivity.this);
//
//            }
//        });
//
//        offBtn.setOnClickListener(new View.OnClickListener(){
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(MainActivity.this, ScreenService.class);
//                stopService(intent);
//
//            }
//        });

        Intent intent = getIntent();
        userID = intent.getStringExtra("userID");
        String userPassword = intent.getStringExtra("userPassword");
        String message = "환영합니다. " + userID + "님!";

        idText.setText(userID);
        passwordText.setText(userPassword);
        welcomeMessage.setText(message);

        if(!userID.equals("admin")){
            managementButton.setVisibility(View.GONE);
        }

        managementButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new UserList().execute();
            }
        });

        categorySelectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cateCheck= 1;
                new CategoryList().execute();
            }
        });

        categoryCreateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cateCheck = 2;
                new CategoryList().execute();
            }
        });

        questionCreateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cateCheck = 3;
                new CategoryList().execute();
            }
        });
    }

    @Override
    public void onClick(View view) {
        if(view == toggleBtn) {
            if(toggleBtn.isChecked()) {
                //toggleBtn.setBackgroundColor(getResources().getColor(R.color.pressed));
                Intent intent = new Intent(MainActivity.this, ScreenService.class);

                SharedPreferences sharedPreferences = getSharedPreferences("sFile",MODE_PRIVATE);

                //저장을 하기위해 editor를 이용하여 값을 저장시켜준다.
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putBoolean("toggle",true);
                editor.commit();

                startService(intent);

                ActivityCompat.finishAffinity(MainActivity.this);
            } else {
                //toggleBtn.setBackgroundColor(getResources().getColor(R.color.notPressed));
                Intent intent = new Intent(MainActivity.this, ScreenService.class);

                SharedPreferences sharedPreferences = getSharedPreferences("sFile",MODE_PRIVATE);

                //저장을 하기위해 editor를 이용하여 값을 저장시켜준다.
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putBoolean("toggle",false);
                editor.commit();

                stopService(intent);
            }

        }
        if(view == itemShop) {
            Intent intent = new Intent(this, ItemShopActivity.class);
            startActivity(intent);
        }
    }

    class UserList extends AsyncTask<Void, Void, String > {
        String target;

        @Override
        protected void onPreExecute(){
            target = "http://ted12333.cafe24.com/List.php";
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
            Intent intent = new Intent(MainActivity.this, ManagementActivity.class);
            intent.putExtra("userList", result);
            MainActivity.this.startActivity(intent);
        }
    }

    class CategoryList extends AsyncTask<Void, Void, String >{
        String target;

        @Override
        protected void onPreExecute(){
            target = "http://ted12333.cafe24.com/CategoryList.php";
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
            if(cateCheck == 1){
                Intent intent = new Intent(MainActivity.this, SelectCategoryActivity.class);
                intent.putExtra("CategoryList", result);
                MainActivity.this.startActivity(intent);
                cateCheck = 0;
            } else if(cateCheck == 2){
                Intent intent = new Intent(MainActivity.this, CreateCategoryActivity.class);
                intent.putExtra("CategoryList", result);
                intent.putExtra("userID", userID);
                MainActivity.this.startActivity(intent);
                cateCheck = 0;
            } else if(cateCheck == 3){
                Intent intent = new Intent(MainActivity.this, CreateQuestionActivity.class);
                intent.putExtra("CategoryList", result);
                intent.putExtra("userID", userID);
                MainActivity.this.startActivity(intent);
                cateCheck = 0;
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
        }
    }

    class QuizListReceive extends AsyncTask<Void, Void, String > {
        String target;

        @Override
        protected void onPreExecute() {
            target = "http://ted12333.cafe24.com/quiz2List.php";
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
            try {
                quiList = new ArrayList<QuizList>();

                JSONObject jsonObject = new JSONObject(result);
                JSONArray jsonArray = jsonObject.getJSONArray("response");
                int count = 0;
                String qCategory, qQuiz, correctAnswer, wrongAnswer, createID, solvedID;
                while (count < jsonArray.length()) {
                    JSONObject object = jsonArray.getJSONObject(count);
                    qCategory = object.getString("qCategory");
                    qQuiz = object.getString("qQuiz");
                    correctAnswer = object.getString("correctAnswer");
                    wrongAnswer = object.getString("wrongAnswer");
                    createID = object.getString("createID");
                    solvedID = object.getString("solvedID");
                    QuizList quizList = new QuizList(qCategory, qQuiz, correctAnswer, wrongAnswer, createID, solvedID);
                    quiList.add(quizList);
                    count++;
                }


            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
