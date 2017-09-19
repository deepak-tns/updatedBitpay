package com.bitpay.bitpay.activity;

import android.Manifest;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bitpay.bitpay.R;
import com.bitpay.bitpay.models.UserTransactionModel;
import com.bitpay.bitpay.utils.DateTimeUtils;
import com.bitpay.bitpay.utils.SharedPreferenceUtils;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.pdf.draw.LineSeparator;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;


public class TransactionDetailsActivity extends BaseActivity {

    private TextView transactionTypeTV;
    private TextView senderIdTV;
    private TextView receiverIdTV;
    private TextView receiverMobileTV;
    private TextView transactionDateTimeTV;
    private TextView transactionStatusTV;
    private TextView transferBtcTV;
    private TextView networkFeeTV;
    private TextView totalAmountTV;
    private ImageView pdfDownlodeIV;
    private UserTransactionModel transactionModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transaction_details);
    }

    @Override
    protected void initViews() {
        settingTitle(getString(R.string.title_tnx_details));
        transactionTypeTV = (TextView) findViewById(R.id.transactionTypeTV);
        senderIdTV = (TextView) findViewById(R.id.senderIdTV);
        receiverIdTV = (TextView) findViewById(R.id.receiverIdTV);
        receiverMobileTV = (TextView) findViewById(R.id.receiverMobileTV);
        transactionDateTimeTV = (TextView) findViewById(R.id.transactionDateTimeTV);
        transactionStatusTV = (TextView) findViewById(R.id.transactionStatusTV);
        transferBtcTV = (TextView) findViewById(R.id.transferBtcTV);
        networkFeeTV = (TextView) findViewById(R.id.networkFeeTV);
        totalAmountTV = (TextView) findViewById(R.id.totalAmountTV);
        pdfDownlodeIV = (ImageView) findViewById(R.id.pdfDownlodeIV);
        if (getIntent().getExtras() != null) {
            transactionModel = (UserTransactionModel) getIntent().getExtras().getSerializable(KEY_TRANSACTION_MODEL);
        }
        settingDefaultText();
    }

    @Override
    protected void initContext() {
        context = TransactionDetailsActivity.this;
        currentActivity = TransactionDetailsActivity.this;
    }

    @Override
    protected void initListners() {
        pdfDownlodeIV.setOnClickListener(this);
    }

    @Override
    protected boolean isActionBar() {
        return true;
    }

    @Override
    protected boolean isHomeButton() {
        return true;
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.pdfDownlodeIV: {

                if (!checkWriteStoragePermission()) {
                    requestPermission();
                } else {
                    generatePdf();
                }
                break;
            }
        }
    }

    @Override
    public void onAlertClicked(int alertType) {

    }

    private boolean checkWriteStoragePermission() {
        int result = ContextCompat.checkSelfPermission(currentActivity, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (result == PackageManager.PERMISSION_GRANTED) {
            return true;
        } else {
            return false;
        }
    }

    private void requestPermission() {
        ActivityCompat.requestPermissions(currentActivity, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, WRITE_EXTERNAL_STORAGE_REQUEST);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case WRITE_EXTERNAL_STORAGE_REQUEST:
                //generatePdf();
                break;
        }
    }

    private void settingDefaultText() {
        if (transactionModel == null) {
            return;
        }
        String dateTime = DateTimeUtils.getTimeByTimestamp(transactionModel.getDate()) + "," + DateTimeUtils.getDateByTimestamp(transactionModel.getDate());
        transactionTypeTV.setText("Transactions Type : " + transactionModel.getTransectionType().toUpperCase());
        senderIdTV.setText("Sender ID : " + SharedPreferenceUtils.getInstance(context).getString(USER_BIT_ADDRESS));
        receiverIdTV.setText("Receiver ID : " + transactionModel.getToBitAddress());
        String phone = transactionModel.getToPhone();
        if (TextUtils.isEmpty(phone)) {
            phone = "N/A";
        }
        receiverMobileTV.setText("Receiver Mobile : " + phone);
        transactionDateTimeTV.setText("Transactions Date & Time : " + dateTime);
        transactionStatusTV.setText("Transactions Status : " + transactionModel.getStatus());

        if (!TextUtils.isEmpty(transactionModel.getBitAmmount())) {
            Double bitAmt = Double.parseDouble(transactionModel.getBitAmmount());
            transferBtcTV.setText("Transfer BTC : " + new DecimalFormat("##.######").format(bitAmt) + " " + getString(R.string.text_bitcoin));
        }
        if (!TextUtils.isEmpty(transactionModel.getNetworkFee())) {
            Double networkFee = Double.parseDouble(transactionModel.getNetworkFee());
            networkFeeTV.setText("Network Fee  : " + new DecimalFormat("##.######").format(networkFee) + " " + getString(R.string.text_bitcoin));
            networkFeeTV.setVisibility(View.VISIBLE);
        } else {
            networkFeeTV.setVisibility(View.GONE);
        }

        if (!TextUtils.isEmpty(transactionModel.getBitAmmount()) && !TextUtils.isEmpty(transactionModel.getNetworkFee())) {
            Double bitAmt = Double.parseDouble(transactionModel.getBitAmmount());
            Double networkFee = Double.parseDouble(transactionModel.getNetworkFee());
            totalAmountTV.setText("Total Amount : " + (new DecimalFormat("##.######").format(bitAmt + networkFee) + " " + getString(R.string.text_bitcoin)));
        }
    }

    private void generatePdf() {

        try {
            String name = SharedPreferenceUtils.getInstance(context).getString(USER_NAME);
            String phone = SharedPreferenceUtils.getInstance(context).getString(USER_MOBILE_NO);

            File docDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS);
            if (!docDir.exists()) {
                if (!docDir.mkdirs()) {
                    return;
                }
            }

            String fileName = "Bitpay_" + System.currentTimeMillis() + ".pdf";
            String pdfFile = docDir + "/" + fileName;
            OutputStream file = new FileOutputStream(pdfFile);

            Rectangle pageSize = new Rectangle(PageSize.A4);
            Document document = new Document(pageSize, 20, 20, 20, 20);

            PdfWriter.getInstance(document, file);
            document.open();
            setBackground(document);
            InputStream ims = getAssets().open("launcher_icon.png");
            Bitmap bmp = BitmapFactory.decodeStream(ims);
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            bmp.compress(Bitmap.CompressFormat.PNG, 100, stream);
            Image image = Image.getInstance(stream.toByteArray());
            image.setAlignment(Element.ALIGN_LEFT);


            PdfPTable table = new PdfPTable(1);
            PdfPCell cell = new PdfPCell(image);
            cell.setBorderWidth(10);
            cell.setPadding(20);
            table.addCell(cell);

            Paragraph paragraph = new Paragraph("Hello");
            cell = new PdfPCell(paragraph);
            cell.setBorderWidth(10);
            cell.setPadding(20);
            table.addCell(cell);
            document.add(table);

            document.close();
            file.close();
            toast("Your transaction has been saved in your document folder", true);

            File generatedFile = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS) + "/" + fileName);
            if (generatedFile.exists()) {
                Intent target = new Intent(Intent.ACTION_VIEW);
                target.setDataAndType(Uri.fromFile(generatedFile), "application/pdf");
                target.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);

                Intent intent = Intent.createChooser(target, "Open File");
                try {
                    startActivity(intent);
                } catch (ActivityNotFoundException e) {
                    toast("Please install pdf reader to open it", true);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setBackground(Document document) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        Bitmap bitmap = BitmapFactory.decodeResource(currentActivity.getResources(), R.drawable.bg_app);
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
        Image img;
        try {
            img = Image.getInstance(stream.toByteArray());
            img.setAbsolutePosition(0, 0);

            document.add(img);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

}
