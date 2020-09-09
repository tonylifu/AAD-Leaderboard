package com.lifu.aadtop20;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.gson.Gson;
import com.lifu.aadtop20.dto.HoursDTO;
import com.lifu.aadtop20.dto.SkillIqDTO;
import com.lifu.aadtop20.utils.Util;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link TopLearnersFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TopLearnersFragment extends Fragment {

    private String TAG = getClass().getSimpleName();

    public ArrayList<SkillIqDTO> mContents;
    private RecyclerView mRecyclerView;
    private LinearLayoutManager mLayoutManager;
    private MainActivity mParent;
    private ProgressBar mMainPbar;
    private TextView mTvMainPbar;
    private ProgressBar mMainPbarRoll;
    ContentsAdapterHours mContentsAdapter;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private View mView;

    public TopLearnersFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment TopLearnersFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static TopLearnersFragment newInstance(String param1, String param2) {
        TopLearnersFragment fragment = new TopLearnersFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mView = inflater.inflate(R.layout.fragment_top_learners, container, false);

        return mView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        //mMainPbar = (ProgressBar) mView.findViewById(R.id.mainPbar);
        mMainPbarRoll = (ProgressBar) mView.findViewById(R.id.mainPbarRoll2);
        mTvMainPbar = (TextView) mView.findViewById(R.id.tvMainPbar2);

        //mMainPbar.setVisibility(View.INVISIBLE);
        mMainPbarRoll.setVisibility(View.INVISIBLE);
        mTvMainPbar.setVisibility(View.INVISIBLE);

        mRecyclerView = (RecyclerView) mView.findViewById(R.id.item_list_hours);
        mLayoutManager = new LinearLayoutManager(mView.getContext());
        mRecyclerView.setLayoutManager(mLayoutManager);

        mContents = new ArrayList<>();

        loadContents();
    }


    //Stuffs
    private void loadContents() {
        String url = Util.TOP_LEANERS_BASE_URL;
        Log.d(TAG, url);
        new TopLearnersFragment.LoadContent().execute(url);
        /*try{
            if(isNetworkAvailable()){
                String url = Util.TOP_SKILL_IQ_BASE_URL;
                new LoadContent().execute(url);
            }else{
                Toast.makeText(mView.getContext(),"Please reconnect your network and try again", Toast.LENGTH_SHORT).show();
            }
        }catch (Exception e){
            e.printStackTrace();
            Toast.makeText(mView.getContext(),"An error occurred. Please reconnect your network and try again", Toast.LENGTH_SHORT).show();
        }*/
    }


    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) mParent.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    //load contents
    private class LoadContent extends AsyncTask<String, Void, Object> {

        @Override
        protected Object doInBackground(String... urls) {

            //mMainPbarRoll.setVisibility(View.VISIBLE);

            String url = urls[0];
            OkHttpClient client = new OkHttpClient();
            //String url = Util.COVID19_STATS_BASE_URL;
            //MediaType JSON = MediaType.parse("application/json; charset=utf-8");

            Request request = new Request.Builder()
                    .url(url)
                    .get()
                    .build();

            //Response response = null;

            try{
                Response response = client.newCall(request).execute();
                String responseStr = response.body().string();

                Log.d(TAG, responseStr);

                mMainPbarRoll.setVisibility(View.INVISIBLE);

                return responseStr;
            }
            catch (Exception e){
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Object o) {

            String result = String.valueOf(o);
            //Toast.makeText(MainActivity.this, "Contents<String>: "+result, Toast.LENGTH_LONG).show();

            Gson gson = new Gson();
            //SkillIQ contentsDTO = gson.fromJson(result, SkillIQ.class);
            HoursDTO[] contentsDTO = gson.fromJson(result, HoursDTO[].class);

            //Toast.makeText(MainActivity.this, "Contents: "+contentsDTO, Toast.LENGTH_LONG).show();
            if(mContents.size() != 0){
                mContents.clear();
            }
            //Toast.makeText(MainActivity.this, "Contents Size: "+contentsDTO.GUID.size(), Toast.LENGTH_LONG).show();

            //mMainPbar.setVisibility(View.VISIBLE);
            //mTvMainPbar.setVisibility(View.VISIBLE);

            //int contentSize = contentsDTO.contents.size();
            int contentSize = contentsDTO.length;

            int i = 0;
            //i = mMainPbar.getProgress();
            //mMainPbar.setMax(contentSize);

            for(i = 0; i < contentSize; i++){
                SkillIqDTO content = new SkillIqDTO();

                /*content.name = contentsDTO.contents.get(i).name;
                content.badgeUrl = contentsDTO.contents.get(i).badgeUrl;
                content.country = contentsDTO.contents.get(i).country;
                content.score = contentsDTO.contents.get(i).score;*/

                /*content.name = contentsDTO.contents[i].name;
                content.badgeUrl = contentsDTO.contents[i].badgeUrl;
                content.country = contentsDTO.contents[i].country;
                content.score = contentsDTO.contents[i].score;*/

                content.name = contentsDTO[i].name;
                content.badgeUrl = contentsDTO[i].badgeUrl;
                content.country = contentsDTO[i].country;
                content.score = contentsDTO[i].hours;

                mContents.add(content);

                //mMainPbar.setProgress(i);
                //mTvMainPbar.setText(i+"/"+mMainPbar.getMax());

                Log.i(TAG, content.toString());
            }

            //sort
            //Collections.sort(mContents);
            //mMainPbar.setVisibility(View.INVISIBLE);
            //mTvMainPbar.setVisibility(View.INVISIBLE);

            //super.onPostExecute(o);
            mContentsAdapter = new ContentsAdapterHours(mView.getContext(),TopLearnersFragment.this, mContents);
            mRecyclerView.setAdapter(mContentsAdapter);

        }
    }
}