package com.example.kakyunglee.smokingproject.activity.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.kakyunglee.smokingproject.R;
import com.example.kakyunglee.smokingproject.activity.dto.ReportDetailDTO;
import com.example.kakyunglee.smokingproject.activity.dto.response.ReportDetailResultDTO;
import com.example.kakyunglee.smokingproject.activity.dto.response.ReportResultDTO;
import com.example.kakyunglee.smokingproject.activity.serviceinterface.PostReport;
import com.example.kakyunglee.smokingproject.activity.util.ServiceRetrofit;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Response;

/**
 * Created by KakyungLee on 2017-10-08.
 */

public class ReportDetailActivity extends AppCompatActivity{

    private final int PICK_FROM_CAMERA =0;
    private final int PICK_FROM_ALBUM =1;
    private final int CROP_FROM_IMAGE= 2;

    private Uri mImageCaptureUir;
    private ByteArrayOutputStream byteBuff;
    private byte[] byteArray;
    InputStream is = null;

    /////////////////////////////////////////
    private Spinner spinner;
    private ImageView loadImage;
    private EditText email;
    private ImageButton btnGallery;
    private ImageButton btnCamera;
    private EditText editText;
    private Button button;
    private TextView reportAddress;
    /////////////////////////////////////////

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.report_detail);

        Intent intent = getIntent();
        final int reportId = intent.getExtras().getInt("report_id");
        String address = intent.getExtras().getString("address");

        //엑션바 사용자 커스텀 타이틀 설정
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.custom_bar);
        TextView title = (TextView)findViewById(R.id.title_bar);
        title.setText("상세 신고");

        //xml 객체 연결
        spinner = (Spinner)findViewById(R.id.report_spinner);
        editText = (EditText) findViewById(R.id.report_detail_content);
        loadImage = (ImageView)findViewById(R.id.load_image) ;
        email = (EditText)findViewById(R.id.report_email);
        btnGallery = (ImageButton)findViewById(R.id.open_gallery);
        btnCamera = (ImageButton)findViewById(R.id.open_camera);
        button = (Button)findViewById(R.id.submit_report);
        reportAddress = (TextView)findViewById(R.id.address);

        // 주소 설정
        reportAddress.setText(address);


        // 스피너 어댑터
        ArrayAdapter adapter = ArrayAdapter.createFromResource(this,R.array.report_detail,android.R.layout.simple_spinner_dropdown_item);
        spinner.setDropDownVerticalOffset(120);
        spinner.setAdapter(adapter);

        // 갤러리에서 이미지 불러오기 버튼
        btnGallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openGallery();
                //Toast.makeText(getApplicationContext(), "get image from gallery", Toast.LENGTH_LONG).show();
            }
        });

        // 카메라에서 이미지 불러오기 버튼
        btnCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openCamera();
                //Toast.makeText(getApplicationContext(), "get image from camera", Toast.LENGTH_LONG).show();
            }
        });

        // 입력한 정보를 서버로 전송하는 버튼
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // 입력 정규식
                if(spinner.getSelectedItemPosition() == 0){
                    Toast.makeText(getApplicationContext(),"항목을 선택해 주세요",Toast.LENGTH_SHORT).show();
                    return;
                }
                if(editText.getText().toString().isEmpty()){
                    Toast.makeText(getApplicationContext(),"내용을 입력해 주세요",Toast.LENGTH_SHORT).show();
                    return;
                }
                if(email.getText().toString().isEmpty()){
                    Toast.makeText(getApplicationContext(),"이메일을 입력해 주세요",Toast.LENGTH_SHORT).show();
                    return;
                }
                Pattern pattern = Pattern.compile("^[a-zA-Z0-9]+[@][a-zA-Z0-9]+[\\.][a-zA-Z0-9]+$");
                Matcher matcher = pattern.matcher(email.getText().toString().trim());
                if(!matcher.matches()){
                    Toast.makeText(getApplicationContext(),"이메일을 정확히 작성해 주세요",Toast.LENGTH_SHORT).show();
                    return;
                }


                // yez : retrofit service 생성하여 postService를 제공할 준비를 한다.
                PostReport postService = ServiceRetrofit.getInstance().getRetrofit().create(PostReport.class);
                // yez : 입력 받은 것을 추출하여 전송 준비하는 파트
                ReportDetailDTO reportDetailDTO = new ReportDetailDTO();
                reportDetailDTO.setReport_category_id(spinner.getSelectedItemPosition());
                reportDetailDTO.setContents(editText.getText().toString());
                reportDetailDTO.setEmail(email.getText().toString());
                reportDetailDTO.setReport_id(reportId);

                // 간편신고로부터 받는 부분

                if(byteArray!=null) postTotalData(byteArray,reportDetailDTO);
                else postTotalData(reportDetailDTO);

                // main 화면으로 돌아가기
                Intent intent =  new Intent(ReportDetailActivity.this,MainActivity.class);
                Toast.makeText(getApplication(),"신고를 완료했습니다",Toast.LENGTH_SHORT).show();
                setResult(0);
                finish();

            }
        });
        ///////////////////////////////////////////////////////////////

    }

    public  void openCamera(){
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        String url =  MediaStore.Images.Media.EXTERNAL_CONTENT_URI.toString();
        intent.putExtra(MediaStore.EXTRA_OUTPUT,url);
        startActivityForResult(intent,PICK_FROM_CAMERA);
    }// 카메라 연결 함수

    public  void openGallery(){
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType(MediaStore.Images.Media.CONTENT_TYPE);
        startActivityForResult(intent,PICK_FROM_ALBUM);
    }// 갤러리 여는 함수

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode,resultCode,data);

        if(resultCode != RESULT_OK)
            return;

        switch(requestCode){
            case PICK_FROM_CAMERA :
               Bundle extras = data.getExtras();
                if(extras != null){
                    Bitmap bitmap = extras.getParcelable("data");
                    loadImage.setImageBitmap(bitmap);
                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.JPEG,100,stream);
                    byteArray = stream.toByteArray();
                } // 카메라에서 이미지 결과 가져 오는 함수

                break;
            case PICK_FROM_ALBUM :
                mImageCaptureUir = data.getData();
                loadImage.setImageURI(mImageCaptureUir);
                try {
                    is = getContentResolver().openInputStream(data.getData());
                    byteArray=getBytes(is);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } // 갤러리에서 이미지 가져오는 함수

                break;
        }
    }
// yez : 인풋스트림을 받아 바이트 배열로 반환해주는 메서드
    public byte[] getBytes(InputStream is)  {
        byteBuff = new ByteArrayOutputStream();
        int buffSize = 1024;
        byte[] buff = new byte[buffSize];

        int len = 0;
        try {
            while ((len = is.read(buff)) != -1) {
                byteBuff.write(buff, 0, len);
            }
        }catch(IOException e){}
        return byteBuff.toByteArray();
    } // yez : 이미지를 byte 형태로 변환

    // yez : 데이터 전송과 관련된 함수는 overloading 하도록 한다.
    private void postTotalData(byte[] imageBytes,ReportDetailDTO reportDetailDTO){
        PostReport postReport=ServiceRetrofit.getInstance().getRetrofit().create(PostReport.class);
        RequestBody requestFile = RequestBody.create(MediaType.parse("image/jpeg"), imageBytes);
        String mimeType = requestFile.contentType().toString();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd-HH:MM:ss");
        String formattedDate = formatter.format(new Date());


        String newImage = formattedDate+"."+ mimeType.substring(mimeType.indexOf("/") + 1, mimeType.length());
        MultipartBody.Part body = MultipartBody.Part.createFormData("file", newImage, requestFile);
        final Call<ReportDetailResultDTO> call = postReport.postDetailReport(reportDetailDTO.getReport_category_id(),reportDetailDTO.getEmail(),reportDetailDTO.getContents(),body,reportDetailDTO.getReport_id());

        new postReportDetailCall().execute(call);

    } // yez :  이미지를 포함했을 때
    private void postTotalData(ReportDetailDTO reportDetailDTO){
        PostReport postReport=ServiceRetrofit.getInstance().getRetrofit().create(PostReport.class);
        final Call<ReportDetailResultDTO> call = postReport.postDetailReport(reportDetailDTO.getReport_category_id(),reportDetailDTO.getEmail(),reportDetailDTO.getContents(),null,reportDetailDTO.getReport_id());
        new postReportDetailCall().execute(call);
    }// yez : 이미지가 포함되지 않았을 때


    private class postReportDetailCall extends AsyncTask<Call,Void, ReportDetailResultDTO> {
        @Override
        protected ReportDetailResultDTO doInBackground(Call ... params){
            try{
                Call<ReportResultDTO> call = params[0];
                Response response = call.execute();
                return (ReportDetailResultDTO) response.body();
            }catch(IOException e){
                e.printStackTrace();
            }
            return null;
        }
        @Override
        protected void onPostExecute(ReportDetailResultDTO result) {
           // Toast.makeText(getApplicationContext(),result.toString(),Toast.LENGTH_LONG).show();
        }

    }
}
