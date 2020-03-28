package id.technow.solve;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class MateriPembahasanActivity extends AppCompatActivity {
    TextView txtTitle,  txtSoal, txtPembahasan;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_materi_pembahasan);
        txtTitle = findViewById(R.id.txtTitle);
        txtSoal = findViewById(R.id.txtSoal);
        txtPembahasan = findViewById(R.id.txtPembahasan);

        txtTitle.setText("");
        txtSoal.setText("");
        txtPembahasan.setText("");
    }
}
