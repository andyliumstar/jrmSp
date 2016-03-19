
package com.jrm.skype.fragment;

import com.jrm.skype.ui.OptionsActivity;
import com.jrm.skype.ui.R;
import com.jrm.skype.util.SktApiActor;
import com.jrm.skype.util.SkypePrefrences;
import android.app.Fragment;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.view.View.OnKeyListener;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * @author andy.liu 
 * a fragment of Options , handle the vol
 */
public class AudioSetFragment extends Fragment implements OnKeyListener, OnFocusChangeListener {
    private View mLayout;

    private TextView mSpeakerVolTv;

    private TextView mRingingVolTv;

    private int mSpeakerVolVlume;

    private int mRingingVolVlume;

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mSpeakerVolVlume = SktApiActor.getSpeakerVolume();
        mRingingVolVlume = SkypePrefrences.getRingVolume(getActivity());

        mSpeakerVolTv = (TextView) mLayout.findViewById(R.id.tv_audioset_fragment_speakervol);
        mRingingVolTv = (TextView) mLayout.findViewById(R.id.tv_audioset_fragment_ringingvol);

        mSpeakerVolTv.setText(mSpeakerVolVlume + "");
        mRingingVolTv.setText(mRingingVolVlume + "");

        mSpeakerVolTv.setOnKeyListener(this);
        mSpeakerVolTv.setOnFocusChangeListener(this);

        mRingingVolTv.setOnKeyListener(this);
        mRingingVolTv.setOnFocusChangeListener(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mLayout = inflater.inflate(R.layout.options_audioset_fragment, container, false);
        return mLayout;
    }

    @Override
    public boolean onKey(View v, int keyCode, KeyEvent event) {
        if (event.getAction() == KeyEvent.ACTION_DOWN) {
            switch (v.getId()) {
                case R.id.tv_audioset_fragment_speakervol:
                    if (keyCode == KeyEvent.KEYCODE_DPAD_RIGHT) {
                        mSpeakerVolVlume = mSpeakerVolVlume + 1 > 100 ? 100 : mSpeakerVolVlume + 1;
                        mSpeakerVolTv.setText(mSpeakerVolVlume + "");
                        SktApiActor.setSpeakerVolume(mSpeakerVolVlume);
                        return true;
                    } else if (keyCode == KeyEvent.KEYCODE_DPAD_LEFT) {
                        mSpeakerVolVlume = mSpeakerVolVlume - 1 < 0 ? 0 : mSpeakerVolVlume - 1;
                        mSpeakerVolTv.setText(mSpeakerVolVlume + "");
                        SktApiActor.setSpeakerVolume(mSpeakerVolVlume);
                        return true;
                    }

                    break;
                case R.id.tv_audioset_fragment_ringingvol:
                    if (keyCode == KeyEvent.KEYCODE_DPAD_RIGHT) {
                        mRingingVolVlume = mRingingVolVlume + 1 > 100 ? 100 : mRingingVolVlume + 1;
                        mRingingVolTv.setText(mRingingVolVlume + "");
                        SkypePrefrences.setRingVolume(getActivity(), mRingingVolVlume);
                        return true;
                    } else if (keyCode == KeyEvent.KEYCODE_DPAD_LEFT) {
                        mRingingVolVlume = mRingingVolVlume - 1 < 0 ? 0 : mRingingVolVlume - 1;
                        mRingingVolTv.setText(mRingingVolVlume + "");
                        SkypePrefrences.setRingVolume(getActivity(), mRingingVolVlume);
                        return true;
                    }

                    break;
                default:
                    break;
            }
        }

        return false;
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        if (hasFocus)
            updateVolIm(hasFocus);
        else
            updateVolIm(mSpeakerVolTv.hasFocus() || mRingingVolTv.hasFocus());
    }

    /**
     * set the visibility of the vol views
     * 
     * @param visible the visibility of the vol views
     */
    public void updateVolIm(boolean visible) {
        if (visible) {
            ((OptionsActivity) getActivity()).getViewHolder().mVHVolDownIv.setVisibility(View.VISIBLE);
            ((OptionsActivity) getActivity()).getViewHolder().mVHVolUpIv.setVisibility(View.VISIBLE);
            ((OptionsActivity) getActivity()).getViewHolder().mVHVolTv.setVisibility(View.VISIBLE);
        } else {
            ((OptionsActivity) getActivity()).getViewHolder().mVHVolDownIv.setVisibility(View.GONE);
            ((OptionsActivity) getActivity()).getViewHolder().mVHVolUpIv.setVisibility(View.GONE);
            ((OptionsActivity) getActivity()).getViewHolder().mVHVolTv.setVisibility(View.GONE);
        }
    }

}
