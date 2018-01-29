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
import com.example.kakyunglee.smokingproject.activity.dto.QuestionDTO;
import com.example.kakyunglee.smokingproject.activity.dto.response.QuestionResponseDTO;
import com.example.kakyunglee.smokingproject.activity.serviceinterface.PostQuestion;
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

import static com.example.kakyunglee.smokingproject.R.layout.question;

/**
 * Created by KakyungLee on 2017-10-08.
 */

public class QuestionActivity extends AppCompatActivity {

    private final int PICK_FROM_CAMERA = 0;
    private final int PICK_FROM_ALBUM = 1;
    private final int CROP_FROM_IMAGE = 2;

    private Uri mImageCaptureUir;
    private ImageView loadImage;
    private ByteArrayOutputStream byteBuff;
    private byte[] byteArray;
    InputStream is = null;

    //////////////////////////////////////////////
    private EditText questionTitle;
    private EditText questionContent;
    private EditText questionEmail;
    private Button postImage;
    private Spinner spinner;
    private ImageButton btnGallery;
    private ImageButton btnCamera;
    //////////////////////////////////////////////


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(question);

        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.custom_bar);
        TextView title = (TextView)findViewById(R.id.title_bar);
        title.setText("정부문의");

        ///////////////////////////////////////////////////////////////////
        spinner = (Spinner) findViewById(R.id.question_spinner);
        questionTitle = (EditText) findViewById(R.id.question_title_edit);
        questionContent = (EditText) findViewById(R.id.question_content_edit);
        loadImage = (ImageView) findViewById(R.id.load_image_question);
        questionEmail = (EditText) findViewById(R.id.question_email_edit);
        btnGallery = (ImageButton) findViewById(R.id.open_gallery_question);
        btnCamera = (ImageButton) findViewById(R.id.open_camera_question);
        postImage = (Button) findViewById(R.id.postquest_btn);
        /////////////////////////////////////////////////////////////////////

        // 스피너 어댑터 설정
        ArrayAdapter adapter = ArrayAdapter.createFromResource(this, R.array.question_detail, android.R.layout.simple_spinner_dropdown_item);
        spinner.setDropDownVerticalOffset(120);
        spinner.setAdapter(adapter);

        // 갤러리 이미지 불러오기 버튼
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

        //////////////////////////////////////////
        postImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(spinner.getSelectedItemPosition() == 0){
                    Toast.makeText(getApplicationContext(),"항목을 선택해 주세요",Toast.LENGTH_SHORT).show();
                    return;
                }
                if(questionTitle.getText().toString().isEmpty()){
                    Toast.makeText(getApplicationContext(),"제목을 입력해 주세요",Toast.LENGTH_SHORT).show();
                    return;
                }

                if(questionContent.getText().toString().isEmpty()){
                    Toast.makeText(getApplicationContext(),"내용을 입력해 주세요",Toast.LENGTH_SHORT).show();
                    return;
                }
                if(questionEmail.getText().toString().isEmpty()){
                    Toast.makeText(getApplicationContext(),"이메일을 입력해 주세요",Toast.LENGTH_SHORT).show();
                    return;
                }
                Pattern pattern = Pattern.compile("^[a-zA-Z0-9]+[@][a-zA-Z0-9]+[\\.][a-zA-Z0-9]+$");
                Matcher matcher = pattern.matcher(questionEmail.getText().toString().trim());
                if(!matcher.matches()){
                    Toast.makeText(getApplicationContext(),"이메일을 정확히 작성해 주세요",Toast.LENGTH_SHORT).show();
                    return;
                }


                QuestionDTO questionDto = new QuestionDTO();
                questionDto.setTitle(questionTitle.getText().toString());
                questionDto.setReport_category_id(spinner.getSelectedItemPosition());
                questionDto.setContents(questionContent.getText().toString());
                questionDto.setEmail(questionEmail.getText().toString());
                // inputStream에 뭔가 들어온 게 있는 경우
                if(byteArray!=null) postTotalData(byteArray,questionDto);
                // 없는 경우 그냥 Dto내 텍스트 데이터만 전송
                else postTotalData(questionDto);

                // main 화면으로 돌아가기
                Intent intent =  new Intent(QuestionActivity.this,MainActivity.class);
                Toast.makeText(getApplication(),"문의를 완료했습니다",Toast.LENGTH_SHORT).show();
                setResult(0);
                finish();


            }
        });
        //////////////////////////////////////////
    }

    public void openCamera() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        String url = MediaStore.Images.Media.EXTERNAL_CONTENT_URI.toString();
        intent.putExtra(MediaStore.EXTRA_OUTPUT, url);
        startActivityForResult(intent, PICK_FROM_CAMERA);

    }

    public void openGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType(MediaStore.Images.Media.CONTENT_TYPE);
        startActivityForResult(intent, PICK_FROM_ALBUM);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode != RESULT_OK)
            return;

        switch (requestCode) {
            case PICK_FROM_CAMERA:
                Bundle extras = data.getExtras();
                if (extras != null) {
                    Bitmap bitmap = extras.getParcelable("data");
                    loadImage.setImageBitmap(bitmap);
                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.JPEG,100,stream);
                    byteArray = stream.toByteArray();
                }
                break;
            case PICK_FROM_ALBUM:
                mImageCaptureUir = data.getData();
                loadImage.setImageURI(mImageCaptureUir);
               // InputStream is = null;
                try {
                    is = getContentResolver().openInputStream(mImageCaptureUir);
                    byteArray=getBytes(is);
                    //getBytes(is);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                break;
        }
    }
///////////////////////// 다소 수정 ///////////////////////////
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
    }


    //////////////////////////////////////////////////////////////////////////
    private void postTotalData(byte[] imageBytes, QuestionDTO inputData) {
        PostQuestion postReportService = ServiceRetrofit.getInstance().getRetrofit().create(PostQuestion.class);
        RequestBody requestFile = RequestBody.create(MediaType.parse("image/jpeg"), imageBytes);
        String mimeType = requestFile.contentType().toString();
        /////////////// 파일명 처리 ///////////////
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd-HH:MM:ss");
        String formattedDate = formatter.format(new Date());


        String newImage = formattedDate+"."+ mimeType.substring(mimeType.indexOf("/") + 1, mimeType.length());
        MultipartBody.Part body = MultipartBody.Part.createFormData("file", newImage, requestFile);
        final Call<QuestionResponseDTO> call = postReportService.postQuestion(inputData.getTitle(),inputData.getReport_category_id(),inputData.getEmail(),inputData.getContents(),body);
        new postQuestionCall().execute(call);

    }

    private void postTotalData(QuestionDTO inputData){

        PostQuestion postReportService = ServiceRetrofit.getInstance().getRetrofit().create(PostQuestion.class);
        final Call<QuestionResponseDTO> call = postReportService.postQuestion(inputData.getTitle(),inputData.getReport_category_id(),inputData.getEmail(),inputData.getContents(),null);
        new postQuestionCall().execute(call);

    }
    ////////////////////////////////////////////////////////////////////////////////
    private class postQuestionCall extends AsyncTask<Call,Void, QuestionResponseDTO> {
        @Override
        protected QuestionResponseDTO doInBackground(Call ... params){
            try{
                Call<QuestionResponseDTO> call = params[0];
                Response response = call.execute();
                return (QuestionResponseDTO) response.body();
            }catch(IOException e){
                e.printStackTrace();
            }
            return null;
        }
        @Override
        protected void onPostExecute(QuestionResponseDTO result) {
           // Toast.makeText(getApplicationContext(),result.toString(),Toast.LENGTH_LONG).show();
        }

    }
}


