package com.android.launcher3.ui.user;

import android.content.Context;
import android.support.transition.ChangeBounds;
import android.support.transition.ChangeImageTransform;
import android.support.transition.ChangeTransform;
import android.support.transition.TransitionSet;
import android.util.AttributeSet;

/**
 * @author tic
 *         created on 18/11/4.
 */
public class DetailTransition extends TransitionSet {

    public DetailTransition() {
        init();
    }

    /**
     * This constructor allows us to use this transition in XML
     */
    public DetailTransition(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        setOrdering(ORDERING_TOGETHER);
        addTransition(new ChangeBounds()).
                addTransition(new ChangeTransform()).
                addTransition(new ChangeImageTransform());
    }
}
