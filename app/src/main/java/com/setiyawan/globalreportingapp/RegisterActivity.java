package com.setiyawan.globalreportingapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class RegisterActivity extends AppCompatActivity {
    // keys
    private static final String EMAIL_KEY = "email";
    private static final String ICAO_KEY = "icao";
    private static final String ROLE_KEY = "role";

    // declare constant
    private static String EXTRA_EMAIL = "EXTRA_EMAIL";
    private static String EXTRA_ROLE = "EXTRA_ROLE";
    private static String EXTRA_ICAO = "EXTRA_ICAO";

    // declare variables
    private EditText editName, editEmail, editPassword, editIcao;
    private TextView tvSwitchLogin;
    private AppCompatButton btnRegister;
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private AutoCompleteTextView ddRole;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        // hide the actionbar
        Objects.requireNonNull(getSupportActionBar()).hide();

        // init variables
        editName = findViewById(R.id.et_register_name);
        editEmail = findViewById(R.id.et_register_email);
        editPassword = findViewById(R.id.et_register_password);
        editIcao = findViewById(R.id.et_register_icao);
        tvSwitchLogin = findViewById(R.id.tv_switch_login);
        btnRegister = findViewById(R.id.btn_register);
        ddRole = findViewById(R.id.dd_role);
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        // set dropdown menu for role
        ArrayAdapter<CharSequence> roleAdapter = ArrayAdapter.createFromResource(this, R.array.roles, R.layout.dropdown_item);
        ddRole.setAdapter(roleAdapter);

        // switch to login screen
        tvSwitchLogin.setOnClickListener(v -> {
            startActivity(new Intent(getApplicationContext(), LoginActivity.class));
            finish();
        });

        // register button logic
        // logic = checkUser() -> addUser() if email not registered -> register() if user added
        btnRegister.setOnClickListener(v -> {
            if (editName.length() > 0 && editEmail.length() > 0 && editPassword.length() > 0) {
                checkUser(editEmail.getText().toString());
            } else {
                Toast.makeText(getApplicationContext(), "Mohon isi semua data terlebih dahulu", Toast.LENGTH_SHORT).show();
            }
        });
    }

    // register function
    private void register(String name, String email, String password) {
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful() && task.getResult() != null) {
                        FirebaseUser user = task.getResult().getUser();
                        if (user != null) {
                            UserProfileChangeRequest req = new UserProfileChangeRequest.Builder()
                                    .setDisplayName(name)
                                    .build();
                            user.updateProfile(req).addOnCompleteListener(task1 -> reload(ddRole.getText().toString()));
                        } else {
                            Toast.makeText(getApplicationContext(), "Registrasi gagal", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(getApplicationContext(), Objects.requireNonNull(task.getException()).getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    // check user in Firestore
    private void checkUser(String email) {
        db.collection("users")
                .whereEqualTo("email", email)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        // check if email not found
                        if (task.isSuccessful()) {
                            QuerySnapshot doc = task.getResult();
                            // check if collection is null
                            if (doc.isEmpty()) {
                                addUser(editEmail.getText().toString(), editIcao.getText().toString(), ddRole.getText().toString());
                            } else {
                                for (QueryDocumentSnapshot document : task.getResult()) {
                                    Toast.makeText(getApplicationContext(), "Email tersebut sudah terdaftar", Toast.LENGTH_SHORT).show();
                                }
                            }
                        } else {
                            addUser(editEmail.getText().toString(), editIcao.getText().toString(), ddRole.getText().toString());
                        }
                    }
                });
    }

    // add user to Firestore
    private void addUser(String email, String icao, String role) {
        Map<String, Object> user = new HashMap<>();
        user.put(EMAIL_KEY, email);
        user.put(ICAO_KEY, icao);
        user.put(ROLE_KEY, role);

        db.collection("users")
                .add(user)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        register(editName.getText().toString(), editEmail.getText().toString(), editPassword.getText().toString());
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getApplicationContext(), "Registrasi gagal", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    // navigator depend on user's role
    private void reload(String role) {
        Intent intent;
        if (role.equalsIgnoreCase("inspektor")) {
            intent = new Intent(getApplicationContext(), InspectorActivity.class);
        } else {
            intent = new Intent(getApplicationContext(), NavigatorActivity.class);
        }
        intent.putExtra(EXTRA_EMAIL, editEmail.getText().toString());
        intent.putExtra(EXTRA_ROLE, ddRole.getText().toString());
        intent.putExtra(EXTRA_ICAO, editIcao.getText().toString());
        startActivity(intent);

        finish();
    }

    // check user is logged in?
    @Override
    protected void onStart() {
        super.onStart();
    }
}