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
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bitpay.bitpay.R;
import com.bitpay.bitpay.utils.FontUtils;
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

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class TransactionInfoActivity extends BaseActivity {

    private TextView tnxMessageTV;
    private TextView thanksMsgTV;
    private TextView lblTransactionIdTV;
    private TextView transactionIdTV;
    private Button btnHome;
    private ImageView pdfDownlodeIV;
    private String transactionId = "";
    private int transactionType;
    private String transactionAmt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mobile_recharge_info);
    }

    @Override
    protected void initViews() {
        settingTitle(getString(R.string.title_tnx_details));
        tnxMessageTV = (TextView) findViewById(R.id.tnxMessageTV);
        thanksMsgTV = (TextView) findViewById(R.id.thanksMsgTV);
        lblTransactionIdTV = (TextView) findViewById(R.id.lblTransactionIdTV);
        transactionIdTV = (TextView) findViewById(R.id.transactionIdTV);
        btnHome = (Button) findViewById(R.id.btnHome);
        pdfDownlodeIV = (ImageView) findViewById(R.id.pdfDownlodeIV);
        FontUtils.changeFont(this, tnxMessageTV, FONT_CORBEL_BOLD);
        FontUtils.changeFont(this, thanksMsgTV, FONT_CORBEL_REGULAR);
        FontUtils.changeFont(this, lblTransactionIdTV, FONT_CORBEL_REGULAR);
        FontUtils.changeFont(this, transactionIdTV, FONT_ROBOTO_REGULAR);
        FontUtils.changeFont(this, btnHome, FONT_ROBOTO_REGULAR);
        if (getIntent().getExtras() != null) {
            transactionId = getIntent().getExtras().getString(KEY_TRANSACTION_ID);
            transactionType = getIntent().getExtras().getInt(KEY_TRANSACTION_TYPE);
            transactionAmt = getIntent().getExtras().getString(KEY_BIT_AMMOUNT);
        }
        transactionIdTV.setText("#" + transactionId);

    }

    @Override
    protected void initContext() {
        context = TransactionInfoActivity.this;
        currentActivity = TransactionInfoActivity.this;
    }

    @Override
    protected void initListners() {
        btnHome.setOnClickListener(this);
        pdfDownlodeIV.setOnClickListener(this);
    }

    @Override
    protected boolean isActionBar() {
        return true;
    }

    @Override
    protected boolean isHomeButton() {
        return false;
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnHome: {
                startActivity(currentActivity, DashboardActivity.class, bundle, false, REQUEST_TAG_NO_RESULT, true, ANIMATION_SLIDE_UP);
                break;
            }
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

    @Override
    public void onBackPressed() {

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
                generatePdf();
                break;
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

            Document document = new Document(new Rectangle(PageSize.A4), 20, 20, 20, 20);
            PdfWriter.getInstance(document, file);

            document.open();
            setBackground(document);
            Calendar c = Calendar.getInstance();
            SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
            String formattedDate = df.format(c.getTime());

            String header = "";
            String message = "";
            if (transactionType == TYPE_BUY_BITCOIN) {
                header = "Buy Bitcoin";
                message = "You have successfully bought " + transactionAmt + " bit on " + formattedDate + ".";
            } else if (transactionType == TYPE_SELL_BITCOIN) {
                header = "Sell Bitcoin";
                message = "You have successfully sold " + transactionAmt + " bit on " + formattedDate + ".";
            } else if (transactionType == TYPE_SEND_BITCOIN) {
                header = "Transfer Bitcoin";
                message = "You have successfully transfer " + transactionAmt + " bit on " + formattedDate + ".";
            } else if (transactionType == TYPE_MOBILE_TOPUP) {
                header = "Mobile Topup";
                message = "Your mobile has been recharged successfully with INR " + transactionAmt + " on " + formattedDate + ".";
            } else if (transactionType == TYPE_DTH) {
                header = "DTH Recharge";
                message = "Your Dth has been recharged successfully with INR " + transactionAmt + " on " + formattedDate + ".";
            } else if (transactionType == TYPE_GAS) {
                header = "Gas Bill Payment";
                message = "Your Gas bill payment of has been successed with INR " + transactionAmt + " on " + formattedDate + ".";
            } else if (transactionType == TYPE_ELECTRICITY) {
                header = "Electricity Bill Payment";
                message = "Your Electricity bill payment has been successed with INR " + transactionAmt + " on " + formattedDate + ".";
            }
            Paragraph paragraph = new Paragraph(" ");

            InputStream ims = getAssets().open("launcher_icon.png");
            Bitmap bmp = BitmapFactory.decodeStream(ims);
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            bmp.compress(Bitmap.CompressFormat.PNG, 100, stream);
            Image image = Image.getInstance(stream.toByteArray());
            image.setAlignment(Element.ALIGN_CENTER);
            document.add(image);
            document.add(paragraph);
            Font headerStyle = new Font();
            headerStyle.setStyle("bold");
            headerStyle.setSize(25);
            Paragraph paragraph1 = new Paragraph(header, headerStyle);
            paragraph1.setAlignment(Element.ALIGN_CENTER);
            document.add(new Paragraph(paragraph1));

            document.add(paragraph);
            document.add(paragraph);

            Font tableColStyle = new Font();
            tableColStyle.setSize(20);
            tableColStyle.setColor(new BaseColor(31,174,255));

            String toText = "To\n\n";
            if (TextUtils.isEmpty(name)) {
                toText = toText + "" + phone;
            } else {
                toText = toText + "" + name;
            }

            String fromText = "From\n\n";
            fromText += "Bitpay IT Services PVT Ltd\nwww.bitpay.co.in";

            Font tableHeaderStyle = new Font();
            tableHeaderStyle.setStyle("bold");
            tableHeaderStyle.setSize(20);

            PdfPTable table = new PdfPTable(2);
            table.setPaddingTop(30);

            PdfPCell cell1 = new PdfPCell(new Phrase(toText, tableColStyle));
            cell1.setHorizontalAlignment(Element.ALIGN_LEFT);
            cell1.setPadding(10);
            table.addCell(cell1);

            PdfPCell cell2 = new PdfPCell(new Phrase(fromText, tableColStyle));
            cell2.setHorizontalAlignment(Element.ALIGN_LEFT);
            cell2.setPadding(10);
            table.addCell(cell2);

            document.add(table);

            Font textStyle = new Font();
            textStyle.setSize(22);

            document.add(paragraph);
            document.add(paragraph);
            Paragraph paragraph2 = new Paragraph(message, textStyle);
            paragraph2.setAlignment(Element.ALIGN_CENTER);
            document.add(paragraph2);
            document.add(paragraph);
            textStyle.setSize(24);
            String tnxId = "Your Transaction Id is #" + transactionId + ".";
            Paragraph paragraph3 = new Paragraph(tnxId, textStyle);
            paragraph3.setAlignment(Element.ALIGN_CENTER);
            document.add(paragraph3);


            document.add(paragraph);
            document.add(paragraph);
            document.add(paragraph);
            textStyle.setSize(30);
            Paragraph paragraph4 = new Paragraph("Thank You for using Bitpay", textStyle);
            paragraph4.setAlignment(Element.ALIGN_CENTER);
            document.add(paragraph4);

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
