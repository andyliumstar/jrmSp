
package com.jrm.skype.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

/**
 * @author andy.liu t
 * he base dialog that contains the base functions for using
 */
public abstract class MainDialog extends Dialog {
    private Window mDialogWindow;

    private View mLayout;

    private WindowManager mWM;

    // call it the first time the dialog show
    public MainDialog(Context context, int theme) {
        super(context, theme);

        // set the owner activity of the dialog
        if (context instanceof Activity){
            setOwnerActivity((Activity) context);
        }
        mDialogWindow = this.getWindow();
        mWM = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        
        mDialogWindow.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE |
                WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
    }

    /**
     * @param res the id of a layout
     */
    public void setDialogContentView(int res) {
        // inflate a view for layout
        @SuppressWarnings("static-access")
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(
                getContext().LAYOUT_INFLATER_SERVICE);
        mLayout = inflater.inflate(res, null);

        this.setContentView(mLayout);
    }

    /**
     * @param h the height of the dialog,px
     * @param w the width of the dialog,px
     */
    public void setDialogSize(int h, int w) {
        if (h > 0 && w > 0) {
            WindowManager.LayoutParams lp = mDialogWindow.getAttributes();
            lp.height = h;
            lp.width = w;
            mDialogWindow.setAttributes(lp);
        }
    }

    /**
     * @param h the percent of the whole screen at height
     * @param w the percent of the whole screen at width
     */
    public void setDialogSizeF(double h, double w) {
        if (h > 0 && w > 0) {
            WindowManager.LayoutParams lp = mDialogWindow.getAttributes();
            lp.height = (int) (mWM.getDefaultDisplay().getHeight() * h);
            lp.width = (int) (mWM.getDefaultDisplay().getWidth() * w);
            mDialogWindow.setAttributes(lp);
        }
    }

    /**
     * @param x px
     * @param y px if the value > 0 the Position will move to right in x and
     *            down in y if the value < 0 the Position will move to left in x
     *            and up in y
     */
    public void setDialogPosition(int x, int y) {

        WindowManager.LayoutParams lp = mDialogWindow.getAttributes();
        lp.x = x;
        lp.y = y;
        this.onWindowAttributesChanged(lp);

    }

    /**
     * @param x the percent of the hole screen at width
     * @param y the percent of the hole screen at height the same meaning of
     *            setDialogPosition of the value
     */
    public void setDialogPositionF(double x, double y) {

        WindowManager.LayoutParams lp = mDialogWindow.getAttributes();
        lp.x = (int) (mWM.getDefaultDisplay().getWidth() * x);
        lp.y = (int) (mWM.getDefaultDisplay().getHeight() * y);
        this.onWindowAttributesChanged(lp);
    }

    public View getLayout() {
        return mLayout;
    }

    /*
     * must call after setDialogContentView
     */
    public abstract void findView();

    public abstract void setViewListenner();

    public abstract void initVar(Bundle args);

}
