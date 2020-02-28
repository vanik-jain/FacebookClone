package com.example.frenzbook;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.frenzbook.DTO.GetUserDetailsDTO;
import com.example.frenzbook.DTO.LoginRequestDTO;
import com.example.frenzbook.DTO.LoginResponseDTO;
import com.example.frenzbook.DTO.SecondLoginRequestDTO;


import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    private TextView mUserEmail;
    private TextView mUserPassword;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

    }

    public void login(View view) {
        mUserEmail = findViewById(R.id.email);
        mUserPassword = findViewById(R.id.password);

        if (!(mUserEmail.getText().toString().equals("") || mUserPassword.getText().toString().equals(""))) {
            final Api api = App.getRetrofit(Api.BASE_URL_LOGIN).create(Api.class);
            final LoginRequestDTO loginRequestDTO = new LoginRequestDTO();
            loginRequestDTO.setEmail(mUserEmail.getText().toString());
            loginRequestDTO.setPassword(mUserPassword.getText().toString());
            sharedPreferences = getSharedPreferences("user_details", MODE_PRIVATE);


            Call<LoginResponseDTO> call = api.sendLoginCredentials(loginRequestDTO);
            call.enqueue(new Callback<LoginResponseDTO>() {
                @Override
                public void onResponse(Call<LoginResponseDTO> call, Response<LoginResponseDTO> response) {

                    if (response.body() != null) {
                        LoginResponseDTO accessTokenRegisterResponse = response.body();
                        String auth_token = accessTokenRegisterResponse.getTokenType() + " " + accessTokenRegisterResponse.getAccessToken();
                        if (!auth_token.equals("")) {
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putString("auth_token", auth_token);
                            editor.apply();
                            Toast.makeText(LoginActivity.this, "Login Successful", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(LoginActivity.this, SecondSignUp.class));
                            finish();

                            SecondLoginRequestDTO secondLoginRequestDTO = new SecondLoginRequestDTO();

                            Api api1 = App.getRetrofit(Api.BASE_URL_LOGIN).create(Api.class);
                            Call<GetUserDetailsDTO> call1 = api1.getUserDetails(secondLoginRequestDTO, auth_token);
                            call1.enqueue(new Callback<GetUserDetailsDTO>() {
                                @Override
                                public void onResponse(Call<GetUserDetailsDTO> call, Response<GetUserDetailsDTO> response) {

                                    if (response.body() != null) {
                                        GetUserDetailsDTO getUserDetailsDTO = response.body();
                                        SharedPreferences.Editor editor = sharedPreferences.edit();
                                        editor.putString("user_id", getUserDetailsDTO.getId().toString());
                                        editor.putString("role", getUserDetailsDTO.getRole());
                                        editor.putString("name", getUserDetailsDTO.getName());
                                        editor.putString("email", getUserDetailsDTO.getEmail());
                                        editor.apply();
                                        if (getUserDetailsDTO.getRole() != null)
                                        {
                                            startActivity(new Intent(LoginActivity.this,LandingPageDrawer.class));
                                        }

                                        else
                                        {
                                            startActivity(new Intent(LoginActivity.this,SecondSignUp.class));
                                        }


                                    }
                                }

                                @Override
                                public void onFailure(Call<GetUserDetailsDTO> call, Throwable t) {
                                    Toast.makeText(LoginActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            });


                        }
                    }

                }

                @Override
                public void onFailure(Call<LoginResponseDTO> call, Throwable t) {
                    Toast.makeText(LoginActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });

        } else {
            Toast.makeText(LoginActivity.this, "Login or Password can't be blank!", Toast.LENGTH_SHORT).show();
        }
    }

    public void SignUp(View view)
    {
        startActivity(new Intent(LoginActivity.this, SignUP.class));
    }
}
