package com.cool.sarthak.hiii;

//import android.*;
//import android.content.pm.PackageManager;
//import android.os.AsyncTask;
//import android.support.v4.app.ActivityCompat;
//import android.support.v4.content.ContextCompat;
//import android.support.v7.app.AppCompatActivity;
//import android.os.Bundle;
//import android.view.View;
//import android.widget.EditText;
//import android.widget.TextView;
//
//import ai.api.AIDataService;
//import ai.api.AIListener;
//import ai.api.AIServiceException;
//import ai.api.android.AIConfiguration;
//import ai.api.android.AIService;
//import ai.api.model.AIError;
//import ai.api.model.AIRequest;
//import ai.api.model.AIResponse;
//import ai.api.model.Result;

//public class Jarvis extends AppCompatActivity implements AIListener {
//    AIService aiService;
//    TextView ques,ans;
//    EditText abcd;
//    AIRequest aiRequest;
//    private static final int RECORD_REQUEST_CODE = 101;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_jarvis);
//
//        ques=findViewById(R.id.ques);
//        ans=findViewById(R.id.ans);
//        abcd=findViewById(R.id.gettext);
//        int permission = ContextCompat.checkSelfPermission(this, android.Manifest.permission.RECORD_AUDIO);
//        if(permission!= PackageManager.PERMISSION_GRANTED)
//        {
//            makeRequest();
//        }
//        final AIConfiguration config = new AIConfiguration("3871af28fcb74502a813cc74d0446eb9",
//                AIConfiguration.SupportedLanguages.English,
//                AIConfiguration.RecognitionEngine.System);
//        aiService = AIService.getService(this, config);
//        aiService.setListener(this);
//
//        final AIDataService aiDataService = new AIDataService(config);
//
//         aiRequest = new AIRequest();
//    }
//
//    protected void makeRequest() {
//
//        ActivityCompat.requestPermissions(this,
//                new String[]{android.Manifest.permission.RECORD_AUDIO},
//                RECORD_REQUEST_CODE);
//
//    }
//
//
//    public void listen(View view) {
//        aiService.startListening();
//    }
//
//    @Override
//    public void onResult(AIResponse result) {
//
//        Result result1 = result.getResult();
//        ques.setText(result1.getResolvedQuery());
//        ans.setText(result1.getFulfillment().getSpeech());
//
//
//    }
//
//    @Override
//    public void onError(AIError error) {
//
//    }
//
//    @Override
//    public void onAudioLevel(float level) {
//
//    }
//
//    @Override
//    public void onListeningStarted() {
//
//    }
//
//    @Override
//    public void onListeningCanceled() {
//
//    }
//
//    @Override
//    public void onListeningFinished() {
//
//    }
//
//    public void textInput(View view) {
//        String q = abcd.getText().toString();
//        ques.setText(q);
//        aiRequest.setQuery(q);
//
//    }
//
//
////    new AsyncTask<AIRequest, Void, AIResponse>() {
////        @Override
////        protected AIResponse doInBackground(AIRequest... requests) {
////            final AIRequest request = requests[0];
////            try {
////                final AIResponse response = aiDataService.request(aiRequest);
////                return response;
////            } catch (AIServiceException e) {
////            }
////            return null;
////        }
////        @Override
////        protected void onPostExecute(AIResponse aiResponse) {
////            if (aiResponse != null) {
////                // process aiResponse here
////            }
////        }
////    }.execute(aiRequest);

//}
