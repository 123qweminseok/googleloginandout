package com.minseok.googlegood;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class MainActivity extends AppCompatActivity {
    Button googleAuth; // Google 로그인 버튼
    FirebaseAuth auth; // Firebase 인증 객체
    FirebaseDatabase database; // Firebase 데이터베이스 객체
    GoogleSignInClient mGoogleSignClient; // Google 로그인 클라이언트
    int RC_SIGN_IN = 20; // Google 로그인 요청 코드
    Button bubu1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        FirebaseAuth.getInstance().signOut();

        // 클릭 버튼임
        googleAuth = findViewById(R.id.btnGoogleAuth);
    bubu1=findViewById(R.id.bubu);
        // Firebase 인증 및 데이터베이스 초기화
        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();

        // Google 로그인 옵션 설정
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();





        // GoogleSignInClient 초기화
        mGoogleSignClient = GoogleSignIn.getClient(this, gso);

        // Google 로그인 버튼 클릭 리스너 설정
        googleAuth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                googleSignIn();
            }
        });

        // 사용자가 이미 로그인되어 있는지 확인하고 로그인된 경우 SecondActivity로 이동
        if (auth.getCurrentUser() != null) {
            Intent intent = new Intent(MainActivity.this, SecondActivity.class);
            startActivity(intent);
            finish();
        }
    }

    /**
     * Google 로그인 프로세스를 시작합니다.
     */
    private void googleSignIn() {
        Intent intent = mGoogleSignClient.getSignInIntent();
        startActivityForResult(intent, RC_SIGN_IN);
    }

    /**
     * Google 로그인 결과를 처리합니다.
     * @param requestCode 요청 코드
     * @param resultCode 결과 코드
     * @param data 인텐트 데이터
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                GoogleSignInAccount account = task.getResult();
                firebaseAuth(account.getIdToken());
            } catch (Exception e) {
                Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }
    }

    /**
     * Firebase로 Google 인증을 수행합니다.
     * @param idToken Google 사용자의 ID 토큰
     */
    private void firebaseAuth(String idToken) {
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        auth.signInWithCredential(credential)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Firebase에 로그인한 사용자 정보를 저장합니다.
                            FirebaseUser user = auth.getCurrentUser();
                            if (user != null) {
                                HashMap<String, Object> map = new HashMap<>();
                                map.put("id", user.getUid());
                                map.put("name", user.getDisplayName());
                                map.put("profile", user.getPhotoUrl().toString());
                                database.getReference().child("users").child(user.getUid()).setValue(map);
                            }
                            // SecondActivity로 이동합니다.
                            Intent intent = new Intent(MainActivity.this, SecondActivity.class);
                            startActivity(intent);
                        } else {
                            // 로그인 실패 시 메시지를 표시합니다.
                            Toast.makeText(MainActivity.this, "로그인에 실패했습니다.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }



}
