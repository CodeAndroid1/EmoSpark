package ibmmobileappbuilder.behaviors;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnticipateInterpolator;
import android.view.animation.OvershootInterpolator;

import ibmmobileappbuilder.core.R;
import ibmmobileappbuilder.util.ColorUtils;

/**
 * A behavior for implementing floating action buttons as main actions in a fragment
 * TODO: Add animations
 */
public class FabBehaviour extends NoOpBehavior implements View.OnClickListener {

    private final View.OnClickListener mListener;
    private final boolean mAnimated;
    Fragment mFragment;
    int mResource;
    int duration;
    FloatingActionButton mFab;

    public FabBehaviour(Fragment fragment, int drawableResource, View.OnClickListener listener) {
        this(fragment, drawableResource, listener, false);
    }

    public FabBehaviour(Fragment fragment, int drawableResource, View.OnClickListener listener, boolean animated) {
        this.mFragment = fragment;
        mListener = listener;
        mResource = drawableResource;
        duration = fragment.getResources().getInteger(android.R.integer.config_mediumAnimTime);
        mAnimated = animated;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setupFab(view);
    }

    private void setupFab(View view) {
        // Tint fab drawable
        LayoutInflater inflater = LayoutInflater.from(mFragment.getActivity());
        inflater.inflate(R.layout.fab, (ViewGroup) view);

        mFab = (FloatingActionButton) view.findViewById(R.id.fab);
        mFab.setImageResource(mResource);

        if (mFab != null) {
            ColorUtils.tintIcon(mFab.getDrawable(), R.color.textBarColor, mFragment.getActivity());
            mFab.setOnClickListener(this);
        }
    }

    @Override
    public void onResume() {
        show(null);
    }

    @Override
    public void onClick(final View v) {
        hide(new AnimatorListenerAdapter() {
                 @Override
                 public void onAnimationEnd(Animator animation) {
                     mListener.onClick(v);
                 }
             }
        );
    }

    public void hide(final Animator.AnimatorListener listener) {
        if (mAnimated) {
            mFab.animate()
                    .translationX(300f)
                    .setDuration(duration)
                    .setInterpolator(new AnticipateInterpolator())
                    .setListener(new AnimatorListenerAdapter() {
                                     @Override
                                     public void onAnimationEnd(Animator animation) {
                                         mFab.setVisibility(View.GONE);
                                         listener.onAnimationEnd(animation);
                                     }
                                 }
                    );
        } else {
            listener.onAnimationEnd(null);
        }
    }

    public void show(Animator.AnimatorListener listener) {
        if (mFab.getVisibility() == View.GONE) {
            if (mAnimated) {
                //mFab.setTranslationX(300f);
                mFab.setVisibility(View.VISIBLE);
                mFab.animate()
                        .translationX(0)
                        .setDuration(duration)
                        .setInterpolator(new OvershootInterpolator())
                        .setListener(listener)
                        .start();
            } else {
                mFab.setVisibility(View.VISIBLE);
            }
        }
    }

}
