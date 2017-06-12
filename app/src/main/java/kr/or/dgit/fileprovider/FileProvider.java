package kr.or.dgit.fileprovider;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class FileProvider extends AppCompatActivity {
    TextView mTextView;
    String mExtStorPath;
    ImageView mImageView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_file_provider);
        mExtStorPath = Environment.getExternalStorageDirectory().getAbsolutePath();

        mTextView = (TextView) findViewById(R.id.tvResult);
        mImageView = (ImageView) findViewById(R.id.imgView);
    }

    private boolean isFileExists(){
        boolean isExist = false;
        File f = new File(mExtStorPath + "/dir/s.jpg");
        isExist = f.exists();
        Toast.makeText(this, Boolean.toString(isExist), Toast.LENGTH_SHORT).show();
        return f.exists();
    }

    public void mPicSaveClicked(View view) {
        File dir = new File(mExtStorPath + "/dir");
        dir.mkdir();
        File file = new File(mExtStorPath + "/dir/s.jpg");
        FileOutputStream fos = null;

        try{
            InputStream is = getAssets().open("s.jpg");
            fos = new FileOutputStream(file);
            byte[] datas = new byte[1024];
            while(is.read(datas)!=-1){
                fos.write(datas);
            }
            fos.flush();
            mTextView.setText("write success");
        } catch (FileNotFoundException e) {
            mTextView.setText("File Not Found." + e.getMessage());
        } catch (SecurityException e) {
            mTextView.setText("Security Exception");
        } catch (IOException e) {
          mTextView.setText(e.getMessage());
        }finally {
            close(fos);
        }
    }




    public void mPicClicked(View view) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            File reqFIle = new File(mExtStorPath + "/dir/s.jpg");
            Uri uri = android.support.v4.content.FileProvider.getUriForFile(this, "kr.or.dgit.fileprovider.fileprovider", reqFIle);
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            intent.setDataAndType(uri, getContentResolver().getType(uri));
        } else {
            Uri uri = Uri.fromFile(new File(mExtStorPath + "/dir/s.jpg"));
            intent.setDataAndType(uri, "image/jpg");
        }
        startActivity(intent);
    }
    public void mPicDeleteClicked(View view) {
        if(isFileExists()){
            File delFile = new File(mExtStorPath + "/dir/s.jpg");
            boolean result = delFile.delete();
            if(result){
                Toast.makeText(this, "삭제 성공", Toast.LENGTH_SHORT).show();
            }else{
                Toast.makeText(this, "삭제 실패", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void mPicViewClicked(View view) {
        if(isFileExists()){
            File reqFile = new File(mExtStorPath + "/dir/s.jpg");
            Uri uri = android.support.v4.content.FileProvider.getUriForFile(this, "kr.or.dgit.fileprovider.fileprovider", reqFile);
            mImageView.setImageURI(uri);
        }else{
            mImageView.setImageResource(R.mipmap.ic_launcher);
        }
    }
    private void close(OutputStream fos) {
        if(fos != null){
            try{
                fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    private void close(InputStream fis){
        if(fis != null){
            try{
                fis.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
