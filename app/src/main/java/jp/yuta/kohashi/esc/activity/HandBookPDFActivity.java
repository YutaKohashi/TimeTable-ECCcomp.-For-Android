package jp.yuta.kohashi.esc.activity;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import jp.yuta.kohashi.esc.R;

public class HandBookPDFActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hand_book_pdf);
//
//        FragmentManager manager = getSupportFragmentManager();
//        FragmentTransaction transaction = manager.beginTransaction();
//
//        transaction.add(R.id.handbook_root_container, new HandBookPDFFragment());
//
//        transaction.commit();
//        pdfView = (PDFView)findViewById(R.id.pdfview);
//        pdfView.fromAsset("handbook2015.pdf")
//                .defaultPage(0)
//                .load();

    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.none_anim, R.anim.push_out_up);
    }
}
