package slideq.com.slideq;

import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

public class ItemShopActivity extends AppCompatActivity implements View.OnClickListener {
    private TextView textView1;
    private int mileage;
    private List information;
    private TextView itemText1;
    private TextView itemText2;
    private TextView itemText3;
    private TextView itemText4;
    private Button saveBtn;
    private ImageButton catBtn;
    private ImageButton penguinBtn;
    private ImageButton bearBtn;
    private ImageButton lionBtn;
    int select;
    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState)   {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_itemshop);
        select = 0;
        textView1 = (TextView) findViewById(R.id.mileagetxt) ;
        itemText1 = (TextView) findViewById(R.id.price1);
        itemText2 = (TextView) findViewById(R.id.price2);
        itemText3 = (TextView) findViewById(R.id.price3);
        itemText4 = (TextView) findViewById(R.id.price4);
        saveBtn = (Button) findViewById(R.id.savebutton);

        catBtn = (ImageButton) findViewById(R.id.catbutton);
        penguinBtn = (ImageButton) findViewById(R.id.penguinbutton);
        bearBtn = (ImageButton) findViewById(R.id.bearbutton);
        lionBtn = (ImageButton) findViewById(R.id.lionbutton);

        information = new ArrayList();
        //sharedPreferences = getSharedPreferences("sFile",MODE_PRIVATE);
        //SharedPreferences.Editor editor = sharedPreferences.edit();
        //String text = sharedPreferences.getString("mileage","0");
        //String smi = Integer.toString(Integer.parseInt(text)+1000);
        //editor.putString("mileage",smi);
        //editor.apply();
//        textView1.setText(sharedPreferences.getString("mileage",""));

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
        textView1.setText(information.get(0).toString()); // mileage
        if(Integer.parseInt(information.get(1).toString())==1) {
            itemText1.setText("SOLDOUT");
        }

        if(Integer.parseInt(information.get(2).toString())==1) {
            itemText2.setText("SOLDOUT");
        }

        if(Integer.parseInt(information.get(3).toString())==1) {
            itemText3.setText("SOLDOUT");
        }

        if(Integer.parseInt(information.get(4).toString())==1) {
            itemText4.setText("SOLDOUT");
        }

        if(Integer.parseInt(information.get(1).toString())==2) {
            itemText1.setText("SELECTED");
            select = 1;
        }

        else if(Integer.parseInt(information.get(2).toString())==2) {
            itemText2.setText("SELECTED");
            select = 2;
        }

        else if(Integer.parseInt(information.get(3).toString())==2) {
            itemText3.setText("SELECTED");
            select = 3;
        }

        else if(Integer.parseInt(information.get(4).toString())==2) {
            itemText4.setText("SELECTED");
            select = 4;
        }

        mileage = Integer.parseInt(information.get(0).toString());
        saveBtn.setOnClickListener(this);
        catBtn.setOnClickListener(this);
        lionBtn.setOnClickListener(this);
        penguinBtn.setOnClickListener(this);
        bearBtn.setOnClickListener(this);
    }

//    public void cat(View v) {
//        if (mileage >=100 && information.get(1).toString().equals("0")) {
//            mileage -= 100;
//            information.set(0,mileage);
//            information.set(1,1); // means already purchase.
//            itemText1.setText("SOLDOUT");
//            textView1.setText(Integer.toString(mileage));
//            AlertDialog.Builder builder = new AlertDialog.Builder(this);
//            builder.setTitle("Alert").setMessage("purchased");
//            AlertDialog alertDialog = builder.create();
//            alertDialog.show();
//
//            //catBtn.setEnabled(false);
//
//        } else if(information.get(1).toString().equals("1")) {
//
//            if(Integer.toString(select).equals("1")){
//                itemText1.setText("SOLDOUT");
//            }
//            if(Integer.toString(select).equals("2")){
//                itemText2.setText("SOLDOUT");
//            }
//            if(Integer.toString(select).equals("3")){
//                itemText3.setText("SOLDOUT");
//            }
//            if(Integer.toString(select).equals("4")){
//                itemText4.setText("SOLDOUT");
//            }
//            information.set(1,2);
//            select = 1;
//            itemText1.setText("SELECTED");
//            AlertDialog.Builder builder = new AlertDialog.Builder(this);
//            builder.setTitle("Alert").setMessage("Selected");
//            AlertDialog alertDialog = builder.create();
//            alertDialog.show();
////            ImageButton Btn = (ImageButton) findViewById(R.id.penguinbutton);
////            Btn.setEnabled(false);
//        } else if(mileage < 100){
//            information.set(0,mileage);
//            textView1.setText(Integer.toString(mileage));
//            AlertDialog.Builder builder = new AlertDialog.Builder(this);
//            builder.setTitle("Alert").setMessage("not enough mileage");
//            AlertDialog alertDialog = builder.create();
//            alertDialog.show();
//        }
//    }
//
//    public void penguin(View v) {
//        if (mileage  >=100 && information.get(2).toString().equals("0")) {
//            mileage -= 100;
//            information.set(0,mileage);
//            information.set(2,1); // means already purchase.
//            itemText2.setText("SOLDOUT");
//            textView1.setText(Integer.toString(mileage));
//            AlertDialog.Builder builder = new AlertDialog.Builder(this);
//            builder.setTitle("Alert").setMessage("purchased");
//            AlertDialog alertDialog = builder.create();
//            alertDialog.show();
////            ImageButton Btn = (ImageButton) findViewById(R.id.penguinbutton);
////            Btn.setEnabled(false);
//        } else if(information.get(2).toString().equals("1")) {
//            if(Integer.toString(select).equals("1")){
//                itemText1.setText("SOLDOUT");
//            }
//            if(Integer.toString(select).equals("2")){
//                itemText2.setText("SOLDOUT");
//            }
//            if(Integer.toString(select).equals("3")){
//                itemText3.setText("SOLDOUT");
//            }
//            if(Integer.toString(select).equals("4")){
//                itemText4.setText("SOLDOUT");
//            }
//            select = 2;
//            information.set(2,2);
//            itemText2.setText("SELECTED");
//            AlertDialog.Builder builder = new AlertDialog.Builder(this);
//            builder.setTitle("Alert").setMessage("Selected");
//            AlertDialog alertDialog = builder.create();
//            alertDialog.show();
////            ImageButton Btn = (ImageButton) findViewById(R.id.penguinbutton);
////            Btn.setEnabled(false);
//        } else if(mileage < 100){
//            information.set(0,mileage);
//            textView1.setText(Integer.toString(mileage));
//            AlertDialog.Builder builder = new AlertDialog.Builder(this);
//            builder.setTitle("Alert").setMessage("not enough mileage");
//            AlertDialog alertDialog = builder.create();
//            alertDialog.show();
//        }
//    }
//    public void bear(View v) {
//        if (mileage >=100 && information.get(3).toString().equals("0")) {
//            mileage -= 100;
//            information.set(0,mileage);
//            information.set(3,1); // means already purchase.
//            itemText3.setText("SOLDOUT");
//            textView1.setText(Integer.toString(mileage));
//            AlertDialog.Builder builder = new AlertDialog.Builder(this);
//            builder.setTitle("Alert").setMessage("purchased");
//            AlertDialog alertDialog = builder.create();
//            alertDialog.show();
////            ImageButton Btn = (ImageButton) findViewById(R.id.bearbutton);
////            Btn.setEnabled(false);
//        } else if(information.get(1).toString().equals("1")) {
//            if(Integer.toString(select).equals("1")){
//                itemText1.setText("SOLDOUT");
//            }
//            if(Integer.toString(select).equals("2")){
//                itemText2.setText("SOLDOUT");
//            }
//            if(Integer.toString(select).equals("3")){
//                itemText3.setText("SOLDOUT");
//            }
//            if(Integer.toString(select).equals("4")){
//                itemText4.setText("SOLDOUT");
//            }
//            select = 3;
//            information.set(3,2);
//            itemText3.setText("SELECTED");
//
//            AlertDialog.Builder builder = new AlertDialog.Builder(this);
//            builder.setTitle("Alert").setMessage("Selected");
//            AlertDialog alertDialog = builder.create();
//            alertDialog.show();
////            ImageButton Btn = (ImageButton) findViewById(R.id.penguinbutton);
////            Btn.setEnabled(false);
//        } else if(mileage < 100){
//            information.set(0,mileage);
//            textView1.setText(Integer.toString(mileage));
//            AlertDialog.Builder builder = new AlertDialog.Builder(this);
//            builder.setTitle("Alert").setMessage("not enough mileage");
//            AlertDialog alertDialog = builder.create();
//            alertDialog.show();
//        }
//    }
//    public void lion(View v) {
//        if (mileage>=100 && information.get(4).toString().equals("0")) {
//            mileage -= 100;
//            information.set(0,mileage);
//            information.set(4,1); // means already purchase.
//            itemText4.setText("SOLDOUT");
//            textView1.setText(Integer.toString(mileage));
//            AlertDialog.Builder builder = new AlertDialog.Builder(this);
//            builder.setTitle("Alert").setMessage("purchased");
//            AlertDialog alertDialog = builder.create();
//            alertDialog.show();
////            ImageButton Btn = (ImageButton) findViewById(R.id.lionbutton);
////            Btn.setEnabled(false);
//
//        } else if(information.get(1).toString().equals("1")) {
//            if(Integer.toString(select).equals("1")){
//                itemText1.setText("SOLDOUT");
//            }
//            if(Integer.toString(select).equals("2")){
//                itemText2.setText("SOLDOUT");
//            }
//            if(Integer.toString(select).equals("3")){
//                itemText3.setText("SOLDOUT");
//            }
//            if(Integer.toString(select).equals("4")){
//                itemText4.setText("SOLDOUT");
//            }
//            select = 4;
//            information.set(4,2);
//            itemText4.setText("SELECTED");
//            AlertDialog.Builder builder = new AlertDialog.Builder(this);
//            builder.setTitle("Alert").setMessage("Selected");
//            AlertDialog alertDialog = builder.create();
//            alertDialog.show();
////            ImageButton Btn = (ImageButton) findViewById(R.id.penguinbutton);
////            Btn.setEnabled(false);
//        } else if(mileage < 100){
//            information.set(0,mileage);
//            textView1.setText(Integer.toString(mileage));
//            AlertDialog.Builder builder = new AlertDialog.Builder(this);
//            builder.setTitle("Alert").setMessage("not enough mileage");
//            AlertDialog alertDialog = builder.create();
//            alertDialog.show();
//        }
//    }


    @Override
    public void onClick(View view) {
        if(view == saveBtn) {
//            Toast.makeText(this,"in function",Toast.LENGTH_LONG).show();
//            Log.d("[DEBUG]","debug code");
            SQLiteDatabase infoDB = null;

            infoDB = openOrCreateDatabase("INFO", MODE_PRIVATE, null);
            //테이블이 존재하지 않으면 새로 생성합니다.
            infoDB.execSQL("CREATE TABLE IF NOT EXISTS INFO (info INT);");

            //테이블이 존재하는 경우 기존 데이터를 지우기 위해서 사용합니다.
            infoDB.execSQL("DELETE FROM INFO");

            infoDB.execSQL("INSERT INTO INFO(info) " +
                    " Values (" + mileage + ");");

            for(int i = 1; i < 11; i++) {// 0: point 1~10: item 구매여부
                infoDB.execSQL("INSERT INTO INFO(info) " +
                        " Values (" + Integer.parseInt(information.get(i).toString()) + ");");
//                Log.d("[DEBUG]",information.get(i).toString());
            }
            finish();
        }
        if(view==catBtn) {
            if (mileage >=100 && information.get(1).toString().equals("0")) {
                mileage -= 100;
                information.set(0,mileage);
                information.set(1,1); // means already purchase.
                itemText1.setText("SOLDOUT");
                textView1.setText(Integer.toString(mileage));
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("Alert").setMessage("purchased");
                AlertDialog alertDialog = builder.create();
                alertDialog.show();

                //catBtn.setEnabled(false);

            } else if(information.get(1).toString().equals("1")) {

                if(Integer.toString(select).equals("1")){
                    itemText1.setText("SOLDOUT");
                    information.set(1,1);
                }
                else if(Integer.toString(select).equals("2")){
                    itemText2.setText("SOLDOUT");
                    information.set(2,1);
                }
                else if(Integer.toString(select).equals("3")){
                    itemText3.setText("SOLDOUT");
                    information.set(3,1);
                }
                else if(Integer.toString(select).equals("4")){
                    itemText4.setText("SOLDOUT");
                    information.set(4,1);
                }
                information.set(1,2);
                select = 1;
                itemText1.setText("SELECTED");
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("Alert").setMessage("Selected");
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
//            ImageButton Btn = (ImageButton) findViewById(R.id.penguinbutton);
//            Btn.setEnabled(false);
            } else if(mileage < 100){
                information.set(0,mileage);
                textView1.setText(Integer.toString(mileage));
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("Alert").setMessage("not enough mileage");
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
            }
        }
        else if(view==bearBtn) {
            if (mileage >=100 && information.get(3).toString().equals("0")) {
                mileage -= 100;
                information.set(0,mileage);
                information.set(3,1); // means already purchase.
                itemText3.setText("SOLDOUT");
                textView1.setText(Integer.toString(mileage));
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("Alert").setMessage("purchased");
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
//            ImageButton Btn = (ImageButton) findViewById(R.id.bearbutton);
//            Btn.setEnabled(false);
            } else if(information.get(1).toString().equals("1")) {
                if(Integer.toString(select).equals("1")){
                    itemText1.setText("SOLDOUT");
                    information.set(1,1);
                }
                else if(Integer.toString(select).equals("2")){
                    itemText2.setText("SOLDOUT");
                    information.set(2,1);
                }
                else if(Integer.toString(select).equals("3")){
                    itemText3.setText("SOLDOUT");
                    information.set(3,1);
                }
                else if(Integer.toString(select).equals("4")){
                    itemText4.setText("SOLDOUT");
                    information.set(4,1);
                }
                select = 3;
                information.set(3,2);
                itemText3.setText("SELECTED");

                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("Alert").setMessage("Selected");
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
//            ImageButton Btn = (ImageButton) findViewById(R.id.penguinbutton);
//            Btn.setEnabled(false);
            } else if(mileage < 100){
                information.set(0,mileage);
                textView1.setText(Integer.toString(mileage));
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("Alert").setMessage("not enough mileage");
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
            }
        }
        else if(view==lionBtn) {
            if (mileage>=100 && information.get(4).toString().equals("0")) {
                mileage -= 100;
                information.set(0,mileage);
                information.set(4,1); // means already purchase.
                itemText4.setText("SOLDOUT");
                textView1.setText(Integer.toString(mileage));
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("Alert").setMessage("purchased");
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
//            ImageButton Btn = (ImageButton) findViewById(R.id.lionbutton);
//            Btn.setEnabled(false);

            } else if(information.get(1).toString().equals("1")) {
                if(Integer.toString(select).equals("1")){
                    itemText1.setText("SOLDOUT");
                    information.set(1,1);
                }
                else if(Integer.toString(select).equals("2")){
                    itemText2.setText("SOLDOUT");
                    information.set(2,1);
                }
                else if(Integer.toString(select).equals("3")){
                    itemText3.setText("SOLDOUT");
                    information.set(3,1);
                }
                else if(Integer.toString(select).equals("4")){
                    itemText4.setText("SOLDOUT");
                    information.set(4,1);
                }
                select = 4;
                information.set(4,2);
                itemText4.setText("SELECTED");
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("Alert").setMessage("Selected");
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
//            ImageButton Btn = (ImageButton) findViewById(R.id.penguinbutton);
//            Btn.setEnabled(false);
            } else if(mileage < 100){
                information.set(0,mileage);
                textView1.setText(Integer.toString(mileage));
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("Alert").setMessage("not enough mileage");
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
            }
        }
        else if(view==penguinBtn) {
            if (mileage  >=100 && information.get(2).toString().equals("0")) {
                mileage -= 100;
                information.set(0,mileage);
                information.set(2,1); // means already purchase.
                itemText2.setText("SOLDOUT");
                textView1.setText(Integer.toString(mileage));
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("Alert").setMessage("purchased");
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
//            ImageButton Btn = (ImageButton) findViewById(R.id.penguinbutton);
//            Btn.setEnabled(false);
            } else if(information.get(2).toString().equals("1")) {
                if(Integer.toString(select).equals("1")){
                    itemText1.setText("SOLDOUT");
                    information.set(1,1);
                }
                else if(Integer.toString(select).equals("2")){
                    itemText2.setText("SOLDOUT");
                    information.set(2,1);
                }
                else if(Integer.toString(select).equals("3")){
                    itemText3.setText("SOLDOUT");
                    information.set(3,1);
                }
                else if(Integer.toString(select).equals("4")){
                    itemText4.setText("SOLDOUT");
                    information.set(4,1);
                }
                select = 2;
                information.set(2,2);
                itemText2.setText("SELECTED");
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("Alert").setMessage("Selected");
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
//            ImageButton Btn = (ImageButton) findViewById(R.id.penguinbutton);
//            Btn.setEnabled(false);
            } else if(mileage < 100){
                information.set(0,mileage);
                textView1.setText(Integer.toString(mileage));
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("Alert").setMessage("not enough mileage");
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
            }
        }
    }
}
