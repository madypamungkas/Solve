package id.technow.solve;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

public class MateriPembahasanActivity extends AppCompatActivity {
    TextView txtTitle,  txtSoal, txtPembahasan, txtJawaban;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_materi_pembahasan);
        ImageButton btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MateriPembahasanActivity.this, Main2Activity.class));
            }
        });

        txtTitle = findViewById(R.id.txtTitle);
        txtSoal = findViewById(R.id.txtSoal);
        txtJawaban = findViewById(R.id.txtJawaban);
        txtPembahasan = findViewById(R.id.txtPembahasan);
        String title = getIntent().getStringExtra("title");
        String soal = getIntent().getStringExtra("soal");
        String jawaban = getIntent().getStringExtra("jawaban");
        String pembahasan = getIntent().getStringExtra("pembahasan");

        txtTitle.setText(title);
        txtSoal.setText("Pertanyaan : \n"+soal);
        txtJawaban.setText("Jawaban Benar : \n"+jawaban);
        txtPembahasan.setText(pembahasan);
    }
}
