package com.lifu.aadtop20;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import retrofit2.Call;
import retrofit2.Callback;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.material.appbar.AppBarLayout;
import com.lifu.aadtop20.service.ScoreService;
import com.lifu.aadtop20.service.SubmissionServiceBuilder;
import com.lifu.aadtop20.utils.Util;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

public class SubmissionActivity extends AppCompatActivity {

    private String TAG = getClass().getSimpleName();

    private EditText mFirstName;
    private EditText mLastName;
    private EditText mEmail;
    private EditText mProjectLink;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_submission);


        ImageView backArrow = (ImageView)findViewById(R.id.submit_back_arrow);
        ImageView imgheader = (ImageView)findViewById(R.id.imageView);

        mFirstName = (EditText)findViewById(R.id.etFirstName);
        mLastName = (EditText)findViewById(R.id.etLastName);
        mEmail = (EditText)findViewById(R.id.etEmail);
        mProjectLink = (EditText)findViewById(R.id.etProjectGithub);
        Button submitButton = (Button)findViewById(R.id.project_submit);

        //Picasso.with(this).load(R.drawable.ic_baseline_keyboard_backspace_24).into(backArrow);
        Picasso.with(this).load(R.drawable.gads_logo).into(imgheader);

        backArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), MainActivity.class);
                startActivity(intent);
            }
        });

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String fname = mFirstName.getText().toString();
                String lname = mLastName.getText().toString();
                String email_address = mEmail.getText().toString();
                String link = mProjectLink.getText().toString();
                final Submission submission = new Submission(fname,lname,email_address,link);

                //dialog
                DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which){
                            case DialogInterface.BUTTON_POSITIVE:
                                //Yes button clicked
                                Log.d(TAG, "About to call Submission API");
                                submitProject(submission);
                                cleanUi();
                                break;

                            case DialogInterface.BUTTON_NEGATIVE:
                                //No button clicked
                                break;
                        }
                    }
                };

                AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
                builder.setMessage("Are you sure?").setPositiveButton("Yes", dialogClickListener)
                        .setNegativeButton("No", dialogClickListener).show();


            }
        });

    }

    private void cleanUi() {
        mFirstName.setText("");
        mLastName.setText("");
        mEmail.setText("");
        mProjectLink.setText("");
        mFirstName.requestFocus();
    }

    private void submitProject(Submission submission) {
        if(isNetworkAvailable()){
            //new SubmitProject().execute(submission);

            //using retrofit
            ScoreService scoreService = SubmissionServiceBuilder.buildService(ScoreService.class);
            Call<Submission> request = scoreService.projectSubmission(submission.getFirstName(),
                    submission.getLastName(), submission.getEmail(), submission.getGithubLink());

            request.enqueue(new Callback<Submission>() {
                @Override
                public void onResponse(Call<Submission> request, retrofit2.Response<Submission> response) {

                    //dialog
                    DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            switch (which){
                                case DialogInterface.BUTTON_POSITIVE:
                                    //Yes button clicked
                                    break;

                            }
                        }
                    };

                    AlertDialog.Builder builder = new AlertDialog.Builder(SubmissionActivity.this);
                    builder.setMessage("Submission Successful").setPositiveButton("Ok", dialogClickListener).show();

                }

                @Override
                public void onFailure(Call<Submission> request, Throwable t) {

                    DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            switch (which){
                                case DialogInterface.BUTTON_POSITIVE:
                                    //Yes button clicked
                                    break;

                            }
                        }
                    };

                    AlertDialog.Builder builder = new AlertDialog.Builder(SubmissionActivity.this);
                    builder.setMessage("Submission Not Successful: "+t.getMessage()).setPositiveButton("Ok", dialogClickListener).show();
                }
            });

        }else{
            DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    switch (which){
                        case DialogInterface.BUTTON_POSITIVE:
                            //Yes button clicked
                            break;

                    }
                }
            };

            AlertDialog.Builder builder = new AlertDialog.Builder(SubmissionActivity.this);
            builder.setMessage("Network failure, please reconnect and try again").setPositiveButton("Ok", dialogClickListener).show();
        }
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }


    //submitcontent
    private class SubmitProject extends AsyncTask<Submission, Void, Object> {

        private Submission getJsonObject(Submission... objects){
            Submission subm = objects[0];
            /*String first_name = subm.getFirstName();
            String last_name = subm.getLastName();
            String submission_email = subm.getEmail();
            String project_link = subm.getGithubLink();

            final JSONObject jsonObject = new JSONObject();

            try {

                jsonObject.put("entry.1877115667", first_name);
                jsonObject.put("entry.2006916086", last_name);
                jsonObject.put("entry.1824927963", submission_email);
                jsonObject.put("entry.284483984", project_link);

            } catch (JSONException e) {
                e.printStackTrace();
            }

            FormBody.Builder form = new FormBody.Builder();
            form.add("entry.1877115667", first_name);
            form.add("entry.2006916086", last_name);
            form.add("entry.1824927963", submission_email);
            form.add("entry.284483984", project_link);*/

            //return jsonObject;
            return subm;
        }

        @Override
        protected Object doInBackground(Submission... objects) {
            Submission subm1 = objects[0];

            OkHttpClient client = new OkHttpClient();
            String url = Util.SUBMISSION_BASE_URL;
            Log.d(TAG, url);
            //MediaType JSON = MediaType.parse("application/json; charset=utf-8");
            // put your json here
            //JSONObject jsonObject = getJsonObject(objects);
            //FormBody.Builder jsonObject = getJsonObject(objects);

            RequestBody requestBody = new MultipartBody.Builder()
                    .setType(MultipartBody.FORM)
                    .addFormDataPart("entry.1877115667", subm1.getFirstName())
                    .addFormDataPart("entry.2006916086", subm1.getLastName())
                    .addFormDataPart("entry.1824927963", subm1.getEmail())
                    .addFormDataPart("entry.284483984", subm1.getGithubLink())
                    .build();

            //Log.d(TAG, requestBody.);
           // RequestBody body = RequestBody.create(JSON, jsonObject.toString());

            //RequestBody body = RequestBody.create(JSON, newContent);
            //Log.d(TAG, jsonObject.toString());

            Request request = new Request.Builder()
                    .url(url)
                    .post(requestBody)
                    .build();

            Response response = null;

            try{
                response = client.newCall(request).execute();
                String responseStr = response.body().string();

                Log.d(TAG, responseStr);

                return responseStr;
            }
            catch (Exception e){
                e.printStackTrace();
            }

            DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    switch (which){
                        case DialogInterface.BUTTON_POSITIVE:
                            //Yes button clicked
                            break;

                    }
                }
            };

            AlertDialog.Builder builder = new AlertDialog.Builder(SubmissionActivity.this);
            builder.setMessage("Submission Not Successful").setPositiveButton("Ok", dialogClickListener).show();

            return null;
        }

        @Override
        protected void onPostExecute(Object o) {
            String result = String.valueOf(o);
            Toast.makeText(SubmissionActivity.this, result, Toast.LENGTH_LONG).show();
            //super.onPostExecute(o);
            Log.d(TAG, result);
            //dialog
            DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    switch (which){
                        case DialogInterface.BUTTON_POSITIVE:
                            //Yes button clicked
                            break;

                    }
                }
            };

            AlertDialog.Builder builder = new AlertDialog.Builder(SubmissionActivity.this);
            builder.setMessage("Submission Successful").setPositiveButton("Ok", dialogClickListener).show();
        }
    }
}