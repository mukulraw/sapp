package com.solo.solomon.soloapp;

import android.app.Dialog;
import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.appcompat.widget.Toolbar;
import android.view.View;
import android.view.Window;
import android.widget.TextView;
import android.widget.Toast;

import com.solo.solomon.soloapp.POJO.forgotBean;
import com.solo.solomon.soloapp.POJO.userBean;
import com.solo.solomon.soloapp.interfaces.allAPIs;

import java.util.Objects;

import me.philio.pinentry.PinEntryView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class InsertPIN extends AppCompatActivity {

    PinEntryView pin;
    TextView set;
    TextView forgot;
    String id;
    Dialog dialog;
    Toolbar toolbar;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insert_pin);


        //oolbar = (Toolbar)findViewById(R.id.toolbar);


        //setSupportActionBar(toolbar);





        dialog = new Dialog(InsertPIN.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.progress_layout);




        pin = (PinEntryView)findViewById(R.id.pin);
        set = (TextView)findViewById(R.id.set);

        forgot = (TextView)findViewById(R.id.forgot);

        bean b = (bean)getApplicationContext();

        id = b.id;

        set.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                checkPIN(pin.getText().toString());


            }
        });





        forgot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialog.show();
                bean b = (bean)getApplicationContext();
                Retrofit retrofit = new Retrofit.Builder()
                        .baseUrl(b.baseurl)
                        .addConverterFactory(ScalarsConverterFactory.create())
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();

                allAPIs cr = retrofit.create(allAPIs.class);


                Call<forgotBean> call = cr.forgot(b.email);

                call.enqueue(new Callback<forgotBean>() {
                    @Override
                    public void onResponse(Call<forgotBean> call, Response<forgotBean> response) {


                        dialog.dismiss();
                        Toast.makeText(getApplicationContext() , response.body().getMessage() , Toast.LENGTH_SHORT).show();

                    }

                    @Override
                    public void onFailure(Call<forgotBean> call, Throwable t) {

                        dialog.dismiss();

                    }
                });


            }
        });


    }


    private void changePIN(String pin)
    {

        dialog.show();
        bean b = (bean)getApplicationContext();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(b.baseurl)
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        allAPIs cr = retrofit.create(allAPIs.class);


        Call<userBean> call = cr.setPIN(id , pin);

        call.enqueue(new Callback<userBean>() {
            @Override
            public void onResponse(Call<userBean> call, Response<userBean> response) {





                dialog.dismiss();
                Toast.makeText(getApplicationContext() , response.body().getMessage() , Toast.LENGTH_SHORT).show();





            }

            @Override
            public void onFailure(Call<userBean> call, Throwable t) {
                dialog.dismiss();
            }
        });



    }


    private  void checkPIN(String pin)
    {

        dialog.show();
        bean b = (bean)getApplicationContext();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(b.baseurl)
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        allAPIs cr = retrofit.create(allAPIs.class);


        Call<userBean> call = cr.checkPIN(id , pin);


        call.enqueue(new Callback<userBean>() {
            @Override
            public void onResponse(Call<userBean> call, Response<userBean> response) {


                if (Objects.equals(response.body().getMessage(), "Userpin Valid!!.."))
                {

                    Intent intent = new Intent(getApplicationContext() , MainActivity.class);
                    dialog.dismiss();

                    startActivity(intent);
                    finish();

                }
                else
                {

                    dialog.dismiss();
                    Toast.makeText(getApplicationContext() , "Invalid PIN" , Toast.LENGTH_SHORT).show();


                }



            }

            @Override
            public void onFailure(Call<userBean> call, Throwable t) {

                dialog.dismiss();

            }
        });

    }


}
