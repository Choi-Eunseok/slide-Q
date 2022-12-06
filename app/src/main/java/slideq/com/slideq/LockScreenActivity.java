package slideq.com.slideq;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

public class LockScreenActivity extends Activity {

    public TextView clock;

    ArrayList<HashMap<String, String>> quList;

    private final String dbName = "QUIZ";
    private final String tableName = "quiz";
    private List information;

    ListView list;
    private static final String TAG_QUESTION = "Question";
    private static final String TAG_CORRECTANSWER ="CorrectAnswer";
    private static final String TAG_WRONGANSWER ="WrongAnswer";
    private static final String TAG_CHEC ="chec";

    ListAdapter adapter;

    SeekBar sb;
    TextView que, nswer1, nswer2;

    Random rnd1, rnd2;

    SQLiteDatabase quizDB = null;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        Window win = getWindow();
        win.setContentView(R.layout.lockscreen_overlay);

        //전개자로 xml파일을 가져옴
        LayoutInflater inflater = (LayoutInflater)getSystemService(
                Context.LAYOUT_INFLATER_SERVICE);
        LinearLayout linear = (LinearLayout)inflater.inflate(R.layout.lockscreen, null);

        //파라미터를 세팅해줌
        LinearLayout.LayoutParams paramlinear = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT
        );

        //윈도우에 추가시킴
        win.addContentView(linear, paramlinear);

        information = new ArrayList();
        SQLiteDatabase ReadDB = this.openOrCreateDatabase("INFO", MODE_PRIVATE, null);

        //SELECT문을 사용하여 테이블에 있는 데이터를 가져옵니다..
        Cursor c = ReadDB.rawQuery("SELECT * FROM " + "INFO", null);

        if (c != null) {
            if (c.moveToFirst()) {
                do {
                    //테이블에서 두개의 컬럼값을 가져와서
                    information.add(c.getInt(c.getColumnIndex("info")));
                } while (c.moveToNext());
            }
        }
        ReadDB.close();

        list = (ListView) findViewById(R.id.listView);
        quList = new ArrayList<HashMap<String,String>>();

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED
                | WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD);

        /*unlokcBtn = (Button) findViewById(R.id.btn2);

        unlokcBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });*/
        que = (TextView) findViewById(R.id.ques);
        nswer1 = (TextView) findViewById(R.id.txtView1);
        nswer2 = (TextView) findViewById(R.id.txtView2);
        clock = findViewById(R.id.clock);

        rnd1 = new Random();
        rnd2 = new Random();

        showList();

        DigitalClockThread thread = new DigitalClockThread();
        thread.start();
    }

    protected void showList(){

        try {

            SQLiteDatabase ReadDB = this.openOrCreateDatabase(dbName, MODE_PRIVATE, null);

            //SELECT문을 사용하여 테이블에 있는 데이터를 가져옵니다..
            Cursor c = ReadDB.rawQuery("SELECT * FROM " + tableName, null);

            if (c != null) {
                if (c.moveToFirst()) {
                    do {
                        //테이블에서 두개의 컬럼값을 가져와서
                        String qu = c.getString(c.getColumnIndex(TAG_QUESTION));
                        String co = c.getString(c.getColumnIndex(TAG_CORRECTANSWER));
                        String wr = c.getString(c.getColumnIndex(TAG_WRONGANSWER));
                        String ch = c.getString(c.getColumnIndex(TAG_CHEC));

                        //HashMap에 넣습니다.
                        HashMap<String,String> quiz ;
                        quiz = new HashMap<String,String>();

                        quiz.put(TAG_QUESTION,qu);
                        quiz.put(TAG_CORRECTANSWER,co);
                        quiz.put(TAG_WRONGANSWER,wr);
                        quiz.put(TAG_CHEC,ch);

                        if(quiz == null){
                            Toast.makeText(LockScreenActivity.this, "quiz is null", Toast.LENGTH_SHORT);
                        }

                        //ArrayList에 추가합니다..
                        quList.add(quiz);

                    } while (c.moveToNext());
                }
            }

            ReadDB.close();

            if(quList.isEmpty()) {
                finish();
            }
            //새로운 apapter를 생성하여 데이터를 넣은 후..
            adapter = new SimpleAdapter(
                    this, quList, R.layout.question_list, new String[]{TAG_QUESTION, TAG_CORRECTANSWER, TAG_WRONGANSWER, TAG_CHEC}, new int[]{ R.id.question, R.id.correctanswer, R.id.wronganswer, R.id.chec}
            );


            //화면에 보여주기 위해 Listview에 연결합니다.
            list.setAdapter(adapter);

            ran();

        } catch (SQLiteException se) {
            Toast.makeText(getApplicationContext(),  se.getMessage(), Toast.LENGTH_LONG).show();
            Log.e("",  se.getMessage());
        }

    }

    public void ran(){
        sb  = (SeekBar) findViewById(R.id.seekBar1);
        if(Integer.parseInt(information.get(1).toString())==2) {
            sb.setThumb(getDrawable(R.drawable.cat1));
            Log.d("[DEBUG]","1");
        }
        else if(Integer.parseInt(information.get(2).toString())==2) {
            sb.setThumb(getDrawable(R.drawable.pengiun));
            Log.d("[DEBUG]","2");
        }
        else if(Integer.parseInt(information.get(3).toString())==2) {
            sb.setThumb(getDrawable(R.drawable.bear));
            Log.d("[DEBUG]","3");
        }
        else if(Integer.parseInt(information.get(4).toString())==2) {
            sb.setThumb(getDrawable(R.drawable.lian));
            Log.d("[DEBUG]","4");
        }
        sb.setProgress(500);
        if(quList.isEmpty()) {
            return ;
        }
        final int num = rnd1.nextInt(quList.size());
        que.setText(quList.get(num).get(TAG_QUESTION));
        int num2 = rnd2.nextInt(2);


        if(num2 == 1){
            nswer1.setText(quList.get(num).get(TAG_CORRECTANSWER));
            nswer2.setText(quList.get(num).get(TAG_WRONGANSWER));


            sb.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                public void onStopTrackingTouch(SeekBar seekBar) {
                    if(seekBar.getProgress() > 850) {
                        seekBar.setProgress(1000);
                        Toast.makeText(LockScreenActivity.this, "Wrong", Toast.LENGTH_SHORT);
                        Handler delayHandler = new Handler();
                        delayHandler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                ran();
                            }
                        }, 1000);
                    }
                    else if(seekBar.getProgress() < 150 ) {
                        seekBar.setProgress(0);

                        quizDB = openOrCreateDatabase("QUIZ", MODE_PRIVATE, null);
                        quizDB.execSQL("UPDATE quiz SET chec = \"1\" WHERE Question = \"" + quList.get(num).get(TAG_QUESTION) + "\";");
                        quizDB.close();

                        Handler delayHandler = new Handler();
                        delayHandler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                finish();
                            }
                        }, 1000);
                    }else{
                        seekBar.setProgress(500);
                    }
                }

                public void onStartTrackingTouch(SeekBar seekBar) {
                }

                public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                }
            });
        }

        else{
            nswer2.setText(quList.get(num).get(TAG_CORRECTANSWER));
            nswer1.setText(quList.get(num).get(TAG_WRONGANSWER));


            sb.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                public void onStopTrackingTouch(SeekBar seekBar) {
                    if(seekBar.getProgress() > 850) {
                        seekBar.setProgress(1000);
                        Handler delayHandler = new Handler();
                        delayHandler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                finish();
                            }
                        }, 1000);
                    }
                    else if(seekBar.getProgress() < 150 ) {
                        seekBar.setProgress(0);
                        Toast.makeText(LockScreenActivity.this, "Wrong", Toast.LENGTH_SHORT);
                        Handler delayHandler = new Handler();
                        delayHandler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                ran();
                            }
                        }, 1000);
                    }else{
                        seekBar.setProgress(500);
                    }
                }

                public void onStartTrackingTouch(SeekBar seekBar) {
                }

                public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                }
            });
        }


    }


    class DigitalClockThread extends Thread{

        @Override
        public void run(){
            super.run();
            try{
                while(true){
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            long now = System.currentTimeMillis();
                            Date date= new Date(now);
                            SimpleDateFormat sdf = new SimpleDateFormat("hh:mm:ss");
                            String getTime = sdf.format(date);
                            clock.setText(getTime);
                        }
                    });
                    Thread.sleep(1000L);
                }
            }catch (Exception e){

            }
        }

    }

}