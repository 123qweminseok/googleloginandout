package com.minseok.googlegood;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GetTokenResult;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.auth.UserInfo;

import java.util.Collections;

public class SecondActivity extends AppCompatActivity {
    Button logoutButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);
        logoutButton = findViewById(R.id.logoutButton);


        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        GoogleSignInClient googleSignInClient = GoogleSignIn.getClient(this, gso);

// 버튼 클릭 시 구글 로그아웃
        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                googleSignInClient.signOut().addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            // 로그아웃 성공
                            Toast.makeText(SecondActivity.this, "로그아웃 되었습니다.", Toast.LENGTH_SHORT).show();
                            // 로그아웃 후에 추가 작업을 수행할 수 있습니다.
                        } else {
                            // 로그아웃 실패
                            Toast.makeText(SecondActivity.this, "로그아웃에 실패했습니다.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });

    }}