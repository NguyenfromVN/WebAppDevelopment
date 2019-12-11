package com.example.projectapplication.view;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.projectapplication.R;
import com.example.projectapplication.manager.MyApplication;
import com.example.projectapplication.model.UpdateInforRequest;
import com.example.projectapplication.network.MyAPIClient;
import com.example.projectapplication.network.UserService;

import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EditProfileActivity extends AppCompatActivity {
    private UserService userService;
    private EditText fullName, email, phone, gender, dob;
    private Button submit;
    private UpdateInforRequest request;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        init();

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editProfile();
            }
        });




    }

    private void editProfile() {
        request = new UpdateInforRequest();
        request.setFullName(fullName.getText().toString());
        request.setDob(dob.getText().toString());
        int genderInt =  Integer.parseInt(gender.getText().toString());
        request.setGender(genderInt);
        userService = MyAPIClient.getInstance().getAdapter().create(UserService.class);

        MyApplication app = (MyApplication) EditProfileActivity.this.getApplication();
        String token=app.loadToken();

        Call<JSONObject> call = userService.updateInfor(token,request);
        call.enqueue(new Callback<JSONObject>() {
            @Override
            public void onResponse(Call<JSONObject> call, Response<JSONObject> response) {
                if(response.isSuccessful()){
                    Log.d("success", "onResponse: ");
                    SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                    SharedPreferences.Editor editor = sharedPref.edit();
                    editor.putString(getString(R.string.saved_name),request.getFullName());

                    editor.putString(getString(R.string.saved_dob), request.getDob());
                    editor.putInt(getString(R.string.saved_gender), request.getGender());

                    editor.commit();

                    Intent intent = new Intent(EditProfileActivity.this, SettingActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    finish();
                }

                else{
                    try {
                        JSONObject jObjError = new JSONObject(response.errorBody().string());
                        Log.d("EditProfile", "onResponse: "+jObjError.getString("message"));
                        Toast.makeText(EditProfileActivity.this, jObjError.getString("message"), Toast.LENGTH_LONG).show();
                    } catch (Exception e) {
                        Toast.makeText(EditProfileActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                        Log.d("EditProfile", "onResponse: "+e.getMessage());
                    }
                }
            }

            @Override
            public void onFailure(Call<JSONObject> call, Throwable t) {

            }
        });


    }

    private void init() {
        submit = (Button)findViewById(R.id.btnPro);
        fullName = (EditText)findViewById(R.id.edtFullNamePro);
        gender = (EditText)findViewById(R.id.edtGenderPro);
        dob = (EditText)findViewById(R.id.edtDobPro);
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
//        dob.setText(sharedPref.getString(getString(R.string.saved_dob),null));
//
//        gender.setText(sharedPref.getLong(getString(R.string.saved_gender),0)+"");
//        fullName.setText(sharedPref.getString(getString(R.string.saved_name),null));
    }


}
