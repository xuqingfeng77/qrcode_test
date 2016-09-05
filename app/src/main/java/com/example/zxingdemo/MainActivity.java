package com.example.zxingdemo;

import com.google.zxing.WriterException;
import com.karics.library.zxing.android.CaptureActivity;
import com.karics.library.zxing.encode.CodeCreator;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {

	private static final int REQUEST_CODE_SCAN = 0x0000;

	private static final String DECODED_CONTENT_KEY = "codedContent";
	private static final String DECODED_BITMAP_KEY = "codedBitmap";

	EditText qrCoded;
	ImageView qrCodeImage;
	Button creator, scanner;
	EditText qrCodeUrl;
	EditText encResult;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		qrCoded = (EditText) findViewById(R.id.ECoder_title);
		qrCodeImage = (ImageView) findViewById(R.id.ECoder_image);
		creator = (Button) findViewById(R.id.ECoder_creator);
		scanner = (Button) findViewById(R.id.ECoder_scaning);
		qrCodeUrl = (EditText) findViewById(R.id.ECoder_input);
		encResult=(EditText)findViewById(R.id.enc_result);

		creator.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {

				String url = qrCodeUrl.getText().toString();
				try {
					Bitmap bitmap = CodeCreator.createQRCode(url);
					qrCodeImage.setImageBitmap(bitmap);
				} catch (WriterException e) {
					e.printStackTrace();
				}

			}
		});

		scanner.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(MainActivity.this,
						CaptureActivity.class);
				startActivityForResult(intent, REQUEST_CODE_SCAN);
			}
		});

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		// 扫描二维码/条码回传
		if (requestCode == REQUEST_CODE_SCAN && resultCode == RESULT_OK) {
			if (data != null) {

				String content = data.getStringExtra(DECODED_CONTENT_KEY);
				Bitmap bitmap = data.getParcelableExtra(DECODED_BITMAP_KEY);
				String tempEncResult="";
				int index=content.lastIndexOf("/");
				tempEncResult=content.substring(index+1);
				qrCoded.setText("解码结果： \n" + tempEncResult);
				try {
					tempEncResult = new DESPlus("A32BF2310942DFBA").decrypt(tempEncResult);
				}catch (Exception e){
						e.printStackTrace();
					Toast.makeText(MainActivity.this,"加密异常",Toast.LENGTH_SHORT).show();
				}
				encResult.setText(tempEncResult);
				qrCodeImage.setImageBitmap(bitmap);
			}
		}
	}
}
