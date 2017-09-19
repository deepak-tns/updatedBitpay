package com.bitpay.bitpay.fragment;


import android.Manifest;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.bitpay.bitpay.R;
import com.bitpay.bitpay.activity.BaseActivity;
import com.bitpay.bitpay.activity.DashboardActivity;
import com.bitpay.bitpay.activity.TransactionDetailsActivity;
import com.bitpay.bitpay.adapter.TransactionListAdapter;
import com.bitpay.bitpay.application.BitPayApplication;
import com.bitpay.bitpay.interfaces.RecyclerClickListner;
import com.bitpay.bitpay.models.UserTransactionModel;
import com.bitpay.bitpay.utils.DateTimeUtils;
import com.bitpay.bitpay.utils.FontUtils;
import com.bitpay.bitpay.utils.LoginUtils;
import com.bitpay.bitpay.utils.SharedPreferenceUtils;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.itextpdf.text.BadElementException;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.pdf.draw.LineSeparator;
import com.itextpdf.text.pdf.draw.VerticalPositionMark;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 */
public class TransactionFragment extends BaseFragment {

    private LinearLayout currentBalanceProgressBar;
    private TextView lblCurrentBalanceTV;
    private TextView currentBalanceTV;
    private TextView lblDateTV;
    private TextView lblDescTV;
    private TextView lblAmtTV;
    private TextView lblTnxIdTV;
    private ImageView pdfDownlodeIV;
    private RecyclerView tnxRecyclerView;
    private TransactionListAdapter adapter;
    private ArrayList<UserTransactionModel> transactionModelList;

    private String currentBalance;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return view = inflater.inflate(R.layout.fragment_transaction, container, false);
    }

    public TransactionFragment() {
        // Required empty public constructor
    }


    @Override
    public void alertOkClicked(int alertType) {

    }

    @Override
    protected void initViews() {
        transactionModelList = new ArrayList<>();
        tnxRecyclerView = (RecyclerView) view.findViewById(R.id.tnxRecyclerView);
        currentBalanceProgressBar = (LinearLayout) view.findViewById(R.id.currentBalanceProgressBar);
        lblCurrentBalanceTV = (TextView) view.findViewById(R.id.lblCurrentBalanceTV);
        currentBalanceTV = (TextView) view.findViewById(R.id.currentBalanceTV);
        lblDateTV = (TextView) view.findViewById(R.id.lblDateTV);
        lblDescTV = (TextView) view.findViewById(R.id.lblDescTV);
        lblAmtTV = (TextView) view.findViewById(R.id.lblAmtTV);
        lblTnxIdTV = (TextView) view.findViewById(R.id.lblTnxIdTV);
        pdfDownlodeIV = (ImageView) view.findViewById(R.id.pdfDownlodeIV);

        adapter = new TransactionListAdapter(context, transactionModelList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(context);
        tnxRecyclerView.setLayoutManager(mLayoutManager);
        tnxRecyclerView.setItemAnimator(new DefaultItemAnimator());
        tnxRecyclerView.setAdapter(adapter);

        FontUtils.changeFont(context, lblCurrentBalanceTV, FONT_CORBEL_REGULAR);
        FontUtils.changeFont(context, currentBalanceTV, FONT_ROBOTO_REGULAR);
        FontUtils.changeFont(context, lblDateTV, FONT_CORBEL_REGULAR);
        FontUtils.changeFont(context, lblDescTV, FONT_CORBEL_REGULAR);
        FontUtils.changeFont(context, lblAmtTV, FONT_CORBEL_REGULAR);
        FontUtils.changeFont(context, lblTnxIdTV, FONT_CORBEL_REGULAR);
        getCurrentBalance();
        getTransactionList();
    }

    @Override
    protected void initContext() {
        context = getActivity();
        currentActivity = getActivity();
    }

    @Override
    protected void initListners() {
        pdfDownlodeIV.setOnClickListener(this);
        adapter.setOnItemClickListener(new RecyclerClickListner() {
            @Override
            public void onItemClick(int position, View v) {
                if (transactionModelList != null && transactionModelList.size() > position) {
                    UserTransactionModel transactionModel = transactionModelList.get(position);
                    if (transactionModel != null) {
                        if (bundle == null) {
                            bundle = new Bundle();
                        }
                        bundle.putSerializable(KEY_TRANSACTION_MODEL, transactionModel);
                        ((BaseActivity) currentActivity).startActivity(currentActivity, TransactionDetailsActivity.class, bundle, false, REQUEST_TAG_NO_RESULT, true, ANIMATION_SLIDE_UP);
                    }
                }
            }

            @Override
            public void onItemLongClick(int position, View v) {

            }
        });
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

    private void getCurrentBalance() {
        currentBalanceProgressBar.setVisibility(View.VISIBLE);
        int userId = SharedPreferenceUtils.getInstance(context).getInteger(USER_ID);

        JSONObject jsonGetCurrentBalance = null;
        String URL_GET_CURRENT_BALANCE = GET_CURRENT_BALANCE_URL;
        try {
            jsonGetCurrentBalance = new JSONObject();
            jsonGetCurrentBalance.put(KEY_USER_ID, userId + "");
            Log.e("jsonGetTransaction", jsonGetCurrentBalance.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonObjectRequest getGetCurentBalanceRequest = new JsonObjectRequest(Request.Method.POST, URL_GET_CURRENT_BALANCE, jsonGetCurrentBalance, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    currentBalanceProgressBar.setVisibility(View.GONE);
                    ((BaseActivity) currentActivity).logTesting(currentActivity.getResources().getString(R.string.nwk_response_get_balance), response.toString(), Log.ERROR);
                    String message = response.getString(RESPONCE_MESSAGE);
                    if (!response.getBoolean(RESPONCE_ERROR)) {
                        JSONObject messageJson = new JSONObject(message);
                        currentBalance = messageJson.getString(KEY_CURRENT_BALANCE);
                        if (TextUtils.isEmpty(currentBalance)) return;
                        double balance = Double.parseDouble(currentBalance);
                        currentBalanceTV.setText(new DecimalFormat("##.######").format(balance) + " " + getString(R.string.text_bitcoin));
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                currentBalanceProgressBar.setVisibility(View.GONE);
                //((BaseActivity) currentActivity).toast(currentActivity.getResources().getString(R.string.nwk_error_get_balance), true);
                ((BaseActivity) currentActivity).logTesting(currentActivity.getResources().getString(R.string.nwk_error_get_balance), error.toString(), Log.ERROR);

            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                return super.getHeaders();
            }
        };

        BitPayApplication.getInstance().addToRequestQueue(getGetCurentBalanceRequest, "getGetCurentBalanceRequest3");
    }

    @Override
    public void onStop() {
        super.onStop();
        BitPayApplication.getInstance().cancelPendingRequest("getGetCurentBalanceRequest3");
    }

    private void getTransactionList() {

        int userId = SharedPreferenceUtils.getInstance(context).getInteger(USER_ID);

        progressDialog(context, context.getString(R.string.pdialog_message_loading), context.getString(R.string.pdialog_message_loading), false, false);

        JSONObject jsonGetTransaction = null;
        String URL_GET_USER_TRANSACTION = GET_USER_TRANSACTION_URL;
        try {
            jsonGetTransaction = new JSONObject();
            jsonGetTransaction.put(KEY_USER_ID, 404);
            Log.e("jsonGetTransaction", jsonGetTransaction.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonObjectRequest getGetTransactionRequest = new JsonObjectRequest(Request.Method.POST, URL_GET_USER_TRANSACTION, jsonGetTransaction, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                cancelProgressDialog();
                try {
                    ((BaseActivity) currentActivity).logTesting(currentActivity.getResources().getString(R.string.nwk_response_get_transaction), response.toString(), Log.ERROR);
                    String message = response.getString(RESPONCE_MESSAGE);

                    if (response.getBoolean(RESPONCE_ERROR)) {
                        transactionModelList.clear();
                        adapter.notifyDataSetChanged();
                    } else {
                        GsonBuilder builder = new GsonBuilder();
                        Gson gson = builder.create();
                        List<UserTransactionModel> tnxList = Arrays.asList(gson.fromJson(message.toString(), UserTransactionModel[].class));
                        transactionModelList.clear();
                        transactionModelList.addAll(tnxList);
                        adapter.notifyDataSetChanged();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                cancelProgressDialog();
                ((BaseActivity) currentActivity).logTesting(currentActivity.getResources().getString(R.string.nwk_error_get_transaction), error.toString(), Log.ERROR);

            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                return super.getHeaders();
            }
        };

        BitPayApplication.getInstance().addToRequestQueue(getGetTransactionRequest);
    }

    private void generatePdf() {


        if (transactionModelList == null || TextUtils.isEmpty(currentBalance)) {
            toast(context, "Please try after some time");
            return;
        }
        try {
            String name = SharedPreferenceUtils.getInstance(context).getString(USER_NAME);
            String phone = SharedPreferenceUtils.getInstance(context).getString(USER_MOBILE_NO);

            File docDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS);
            if (!docDir.exists()) {
                if (!docDir.mkdirs()) {
                    return;
                }
            }
            Paragraph paragraph = new Paragraph(" ");

            String fileName = "Bitpay_" + System.currentTimeMillis() + ".pdf";
            String pdfFile = docDir + "/" + fileName;
            OutputStream file = new FileOutputStream(pdfFile);

            Rectangle pageSize = new Rectangle(PageSize.A4);
            Document document = new Document(pageSize, 10, 10, 10, 10);

            PdfWriter.getInstance(document, file);
            document.open();
            setBackground(document);
            InputStream ims = getActivity().getAssets().open("pdficon.png");
            Bitmap bmp = BitmapFactory.decodeStream(ims);
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            bmp.compress(Bitmap.CompressFormat.PNG, 100, stream);
            Image image = Image.getInstance(stream.toByteArray());
            image.setAlignment(Element.ALIGN_LEFT);
            //image.setAbsolutePosition(10f, 0f);
            document.add(image);
            document.add(paragraph);

            Font headerStyle = new Font();
            headerStyle.setStyle(Font.NORMAL);
            headerStyle.setColor(BaseColor.BLACK);
            headerStyle.setSize(35);

            document.add(new Paragraph(" "));
            LineSeparator lineSeparator = new LineSeparator();
            lineSeparator.setLineColor(BaseColor.BLACK);
            lineSeparator.setLineWidth(10);
            document.add(lineSeparator);


   /*         PdfPTable table1 = new PdfPTable(2);
            Paragraph wrong = new Paragraph("Madan Lal Khurana");
            wrong.setAlignment(Element.ALIGN_LEFT);
            wrong.setPaddingTop(20);

            PdfPCell wrongCell = new PdfPCell(wrong);

            Paragraph right = new Paragraph("Statement",headerStyle);
            PdfPCell rightCell = new PdfPCell();
            wrongCell.setBorder(Rectangle.NO_BORDER);
            rightCell.setBorder(Rectangle.NO_BORDER);
            table1.addCell(wrongCell);
            rightCell.addElement(right);

            table1.addCell(rightCell);
            document.add(table1);
           // document.close();
           */
       /*     Chunk c2 = new Chunk("Madan lal khurana");
            Chunk c3 = new Chunk("                                                                                            ");
            Chunk c4 = new Chunk("Statement ",headerStyle);

            Paragraph p1 = new Paragraph();

            p1.setPaddingTop(100);
            p1.add(new Chunk(c2));
            p1.add(new Chunk(c3));
            p1.add(new Chunk(c4));


            document.add( p1);

*/




            PdfPTable table_test = new PdfPTable(2);
            table_test.setPaddingTop(30);

            PdfPCell c1c = new PdfPCell(new Phrase("Madan Lal Khurana"));
            c1c.setHorizontalAlignment(Element.ALIGN_LEFT);
            c1c.setPaddingTop(20);


            PdfPCell c2c = new PdfPCell(new Phrase("Statement",headerStyle));
            c2c.setHorizontalAlignment(Element.ALIGN_CENTER);


            c1c.setBorder(Rectangle.NO_BORDER);
            c2c.setBorder(Rectangle.NO_BORDER);

            table_test.addCell(c1c);
            table_test.addCell(c2c);

            document.add(table_test);

            PdfPTable table_address = new PdfPTable(1);
            PdfPCell address = new PdfPCell(new Phrase("Noida"));
            address.setHorizontalAlignment(Element.ALIGN_LEFT);

            address.setBorder(Rectangle.NO_BORDER);
            table_address.addCell(address);
            document.add(table_address);

            PdfPTable table_phone = new PdfPTable(1);
            PdfPCell p_phone  = new PdfPCell(new Phrase("8368653080"));
            p_phone.setHorizontalAlignment(Element.ALIGN_LEFT);

            p_phone.setBorder(Rectangle.NO_BORDER);
            table_phone.addCell(p_phone);
            document.add(table_phone);


            PdfPTable table_mail = new PdfPTable(2);

            PdfPCell cell_mail = new PdfPCell(new Phrase("nehasharma@gmail.com"));
            cell_mail.setHorizontalAlignment(Element.ALIGN_LEFT);

            PdfPCell cell_date = new PdfPCell(new Phrase("01/01/01/2017 to 01/01/09/2017"));
            cell_date.setHorizontalAlignment(Element.ALIGN_CENTER);


            cell_date.setBorder(Rectangle.NO_BORDER);
            cell_mail.setBorder(Rectangle.NO_BORDER);

            table_mail.addCell(cell_mail);
            table_mail.addCell(cell_date);
            document.add(table_mail);

/*
            String address = SharedPreferenceUtils.getInstance(getContext()).getString(USER_ID);
            Paragraph paragraphAddress = new Paragraph("Noida");
            paragraphAddress.setAlignment(Element.ALIGN_LEFT);
            document.add(paragraphAddress);*/





            Font textStyle = new Font();
            textStyle.setSize(16);
            document.add(new Paragraph("     "));
            LineSeparator lineSeparators = new LineSeparator();
            lineSeparators.setLineColor(BaseColor.BLACK);
            lineSeparator.setLineWidth(10);
            document.add(lineSeparator);


            Calendar c = Calendar.getInstance();
            SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
            String formattedDate = df.format(c.getTime());

            double balance = Double.parseDouble(currentBalance);
            String cBalance = new DecimalFormat("##.######").format(balance);
            document.add(paragraph);
            Paragraph paragraph3 = new Paragraph("Available Balance BTC " + cBalance + "\n" + " bit as on " + formattedDate, textStyle);
            paragraph3.setPaddingTop(100);
            paragraph3.setAlignment(Element.ALIGN_CENTER);
            document.add(new Paragraph(paragraph3));

            document.add(new Paragraph("     "));

            Paragraph paragraph4 = new Paragraph("   ");
            paragraph4.setPaddingTop(50);
            document.add(new Paragraph(paragraph4));


            Font tableHeaderStyle = new Font();
            tableHeaderStyle.setStyle("bold");
            tableHeaderStyle.setSize(16);

            PdfPTable table = new PdfPTable(4);
            table.setPaddingTop(30);
            PdfPCell c1 = new PdfPCell(new Phrase("Date", tableHeaderStyle));
            c1.setHorizontalAlignment(Element.ALIGN_CENTER);
            c1.setPaddingTop(10);
            c1.setPaddingBottom(10);
            table.addCell(c1);

            c1 = new PdfPCell(new Phrase("Description", tableHeaderStyle));
            c1.setHorizontalAlignment(Element.ALIGN_CENTER);
            c1.setPaddingTop(10);
            c1.setPaddingBottom(10);
            table.addCell(c1);


            c1 = new PdfPCell(new Phrase("Transaction Id", tableHeaderStyle));
            c1.setHorizontalAlignment(Element.ALIGN_CENTER);
            c1.setPaddingTop(10);
            c1.setPaddingBottom(10);
            table.addCell(c1);

            c1 = new PdfPCell(new Phrase("Amount(Bit)", tableHeaderStyle));
            c1.setHorizontalAlignment(Element.ALIGN_CENTER);
            c1.setPaddingTop(10);
            c1.setPaddingBottom(10);
            table.addCell(c1);

            table.setHeaderRows(1);

            Font tableColStyle = new Font();
            tableColStyle.setSize(12);

            for (int i = 0; i < transactionModelList.size(); i++) {
                UserTransactionModel transactionModel = transactionModelList.get(i);
                if (transactionModel == null) continue;
                String dateTime = DateTimeUtils.getDateByTimestamp(transactionModel.getDate()) + "," + DateTimeUtils.getTimeByTimestamp(transactionModel.getDate());
                PdfPCell cell1 = new PdfPCell(new Phrase(dateTime, tableColStyle));
                cell1.setHorizontalAlignment(Element.ALIGN_CENTER);
                cell1.setPaddingTop(10);
                cell1.setPaddingBottom(10);
                table.addCell(cell1);

                PdfPCell cell4 = new PdfPCell(new Phrase(transactionModel.getTransectionType(), tableColStyle));
                cell4.setHorizontalAlignment(Element.ALIGN_CENTER);
                cell4.setPaddingTop(10);
                cell4.setPaddingBottom(10);
                table.addCell(cell4);


                PdfPCell cell2 = new PdfPCell(new Phrase(transactionModel.getTransectionId(), tableColStyle));
                cell2.setHorizontalAlignment(Element.ALIGN_CENTER);
                cell2.setPaddingTop(10);
                cell2.setPaddingBottom(10);
                table.addCell(cell2);

                PdfPCell cell3 = new PdfPCell(new Phrase(transactionModel.getBitAmmount(), tableColStyle));
                cell3.setHorizontalAlignment(Element.ALIGN_CENTER);
                cell3.setPaddingTop(10);
                cell3.setPaddingBottom(10);
                table.addCell(cell3);
            }

            document.add(table);

            document.close();
            file.close();
            toast(context, "Your transaction has been saved in your document folder");

            File generatedFile = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS) + "/" + fileName);
            if (generatedFile.exists()) {
                Intent target = new Intent(Intent.ACTION_VIEW);
                target.setDataAndType(Uri.fromFile(generatedFile), "application/pdf");
                target.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);

                Intent intent = Intent.createChooser(target, "Open File");
                try {
                    startActivity(intent);
                } catch (ActivityNotFoundException e) {
                    toast(context, "Please install pdf reader to open it");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public PdfPCell getCell(String text, int alignment) {
        PdfPCell cell = new PdfPCell(new Phrase(text));
        cell.setPadding(0);
        cell.setHorizontalAlignment(alignment);
        cell.setBorder(PdfPCell.NO_BORDER);
        return cell;
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
