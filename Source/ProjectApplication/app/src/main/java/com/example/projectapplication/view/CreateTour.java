package com.example.projectapplication.view;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.DatePicker;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import com.example.projectapplication.R;
import com.example.projectapplication.network.UserService;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class CreateTour extends AppCompatActivity {
    //global variables
    private String name;
    private int adults, childs, minCost, maxCost;
    private long startDate, endDate;
    private boolean isPrivate=false;

    private static TextView textViewStartDate, textViewEndDate;
    private static int startYear, startMonth, startDay;
    private static int endYear, endMonth, endDay;
    private static int flagStartDate=0, flagEndDate=0;

    private UserService userService;
    private String token;

    private String TAG="CreateTour";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_tour);

        textViewStartDate = findViewById(R.id.textViewStartDate);
        textViewEndDate = findViewById(R.id.textViewEndDate);

        findViewById(R.id.buttonStartDate).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                flagStartDate=1;
                DialogFragment newFragment = new DatePickerFragment();
                newFragment.show(getSupportFragmentManager(), "datePicker");
            }
        });

        findViewById(R.id.buttonEndDate).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                flagEndDate=1;
                DialogFragment newFragment = new DatePickerFragment();
                newFragment.show(getSupportFragmentManager(), "datePicker");
            }
        });

        findViewById(R.id.checkBoxIsPrivate).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isPrivate=!isPrivate;
            }
        });

        findViewById(R.id.buttonCreate).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //prepare data
                name=((EditText)findViewById(R.id.editTextName)).getText().toString();

                String strDate = getDate(startDay,startMonth,startYear);
                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                try {
                    Date date = sdf.parse(strDate);
                    startDate=date.getTime();
                }
                catch(Exception e) {
                    System.out.println(e);
                }

                strDate = getDate(endDay,endMonth,endYear);
                try {
                    Date date = sdf.parse(strDate);
                    endDate=date.getTime();
                }
                catch(Exception e) {
                    System.out.println(e);
                }

                adults=Integer.parseInt(((EditText)findViewById(R.id.editTextInputAdults)).getText().toString());
                childs=Integer.parseInt(((EditText)findViewById(R.id.editTextInputChilds)).getText().toString());
                minCost=Integer.parseInt(((EditText)findViewById(R.id.editTextMinCost)).getText().toString());
                maxCost=Integer.parseInt(((EditText)findViewById(R.id.editTextMaxCost)).getText().toString());

                //call add stop points activity
                Intent addPoints = new Intent(CreateTour.this, AddStopPoint.class);
                addPoints.putExtra("name",name);
                addPoints.putExtra("startDate",startDate);
                addPoints.putExtra("endDate",endDate);
                addPoints.putExtra("adults",adults);
                addPoints.putExtra("childs",childs);
                addPoints.putExtra("minCost",minCost);
                addPoints.putExtra("maxCost",maxCost);
                addPoints.putExtra("isPrivate",isPrivate);
                startActivity(addPoints);
                finish();

//                //create tour request
//                CreateTourRequest request = new CreateTourRequest();
//                request.setName(name);
//                request.setStartDate(startDate);
//                request.setEndDate(endDate);
//                request.setPrivate(isPrivate);
//                request.setAdults(adults);
//                request.setChilds(childs);
//                request.setMinCost(minCost);
//                request.setMaxCost(maxCost);
//
//                //load token from shared preferences
//                MyApplication app = (MyApplication) CreateTour.this.getApplication();
//                token=app.loadToken();
//
//                userService = MyAPIClient.getInstance().getAdapter().create(UserService.class);
//                Call<CreateTourResponse> call = userService.createTour(request,token);
//
//                call.enqueue(new Callback<CreateTourResponse>() {
//                    @Override
//                    public void onResponse(Call<CreateTourResponse> call, Response<CreateTourResponse> response) {
//                        if(response.isSuccessful()) {
//                            //if create successfully
//                        }
//                        else{
//                            try {
//                                JSONObject jObjError = new JSONObject(response.errorBody().string());
//                                Toast.makeText(CreateTour.this, jObjError.getString("message"), Toast.LENGTH_LONG).show();
//                            } catch (Exception e) {
//                                Toast.makeText(CreateTour.this, e.getMessage(), Toast.LENGTH_LONG).show();
//                            }
//                        }
//                    }
//                    @Override
//                    public void onFailure(Call<CreateTourResponse> call, Throwable t) {
//                        Log.d(TAG, t.getMessage());
//                    }
//                });
            }
        });
    }

    private String getDate(int day, int month, int year){
        String result="";
        if (day<10)
            result+="0"+day+"/";
        else
            result+=day+"/";
        if (month<10)
            result+="0"+month+"/";
        else
            result+=month+"/";
        result+=year;

        return result;
    }

    public static class DatePickerFragment extends DialogFragment
            implements DatePickerDialog.OnDateSetListener {

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the current date as the default date in the picker
            final Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);

            // Create a new instance of DatePickerDialog and return it
            return new DatePickerDialog(getActivity(), this, year, month, day);
        }

        public void onDateSet(DatePicker view, int year, int month, int day) {
            month+=1;
            if (flagStartDate==1){
                startDay=day;
                startMonth=month;
                startYear=year;
                String str=startDay+"/"+startMonth+"/"+startYear;
                textViewStartDate.setText(str);
                flagStartDate=0;
            }
            else {
                endDay=day;
                endMonth=month;
                endYear=year;
                String str=endDay+"/"+endMonth+"/"+endYear;
                textViewEndDate.setText(str);
                flagEndDate=0;
            }
        }
    }
}
