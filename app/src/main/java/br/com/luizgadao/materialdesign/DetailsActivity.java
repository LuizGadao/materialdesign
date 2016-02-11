package br.com.luizgadao.materialdesign;

import android.animation.Animator;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import br.com.luizgadao.materialdesign.model.Disk;
import butterknife.Bind;
import butterknife.ButterKnife;

public class DetailsActivity extends AppCompatActivity {

    public static final String EXTRA_DISK = "disk";
    public static final String X_POS_ANIM = "CX";
    public static final String Y_POS_ANIM = "CY";

    Target mPicassoTarget;

    @Bind(R.id.fab)
    FloatingActionButton mFab;
    @Bind(R.id.imgDisk)
    ImageView mImgCapa;
    @Bind(R.id.txtTitulo)
    TextView mTxtTitulo;
    @Bind(R.id.txtAno)
    TextView mTxtAno;
    @Bind(R.id.txtGravadora)
    TextView mTxtGravadora;
    @Bind(R.id.txtFormacao)
    TextView mTxtFormacao;
    @Bind(R.id.txtMusicas)
    TextView mTxtMusicas;

    @Nullable
    @Bind(R.id.coordinator)
    CoordinatorLayout mCoordinator;
    @Nullable
    @Bind(R.id.appBar)
    AppBarLayout mAppBar;
    @Nullable
    @Bind(R.id.collapseToolbar)
    CollapsingToolbarLayout mCollapsingToolbarLayout;
    @Bind(R.id.toolbar)
    Toolbar mToolbar;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        /*if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
            Window window = getWindow();
            window.requestFeature(Window.FEATURE_CONTENT_TRANSITIONS);
            window.setExitTransition(new Explode());
            window.setEnterTransition(new Explode());
        }*/
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_details);
        Disk disk = (Disk) getIntent().getSerializableExtra(EXTRA_DISK);

        ButterKnife.bind(this);
        setupAnimationIn();
        setupToolbar(disk.titulo);
        loadImage(LoadDisk.URL + disk.capaGrande);
        fillFields(disk);

        //reveal animation
        /*mCoordinator.setVisibility(View.INVISIBLE);
        mCoordinator.post(new Runnable() {
            @Override
            public void run() {
                showOrHide(mCoordinator);
            }
        });*/
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    private void setupAnimationIn() {
        ViewCompat.setTransitionName(mImgCapa, "diskCover");
        ViewCompat.setTransitionName(mTxtTitulo, "title");
        ViewCompat.setTransitionName(mTxtAno, "year");
        ActivityCompat.postponeEnterTransition(this);
    }

    private void fillFields(Disk disk) {
        mTxtTitulo.setText(disk.titulo);
        mTxtAno.setText(String.valueOf(disk.ano));
        mTxtGravadora.setText(disk.gravadora);
        StringBuilder sb = new StringBuilder();
        for (String integrante : disk.formacao) {
            if (sb.length() != 0) sb.append('\n');
            sb.append(integrante);
        }
        mTxtFormacao.setText(sb.toString());
        sb = new StringBuilder();
        for (int i = 0; i < disk.faixas.length; i++) {
            if (sb.length() != 0)
                sb.append('\n');
            sb.append(i + 1).append(". ").append(disk.faixas[i]);
        }
        mTxtMusicas.setText(sb.toString());
    }

    private void loadImage(String capaGrande) {
        if (mPicassoTarget == null) {
            mPicassoTarget = new Target() {
                @Override
                public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                    mImgCapa.setImageBitmap(bitmap);
                    startAnimationIN(mCoordinator);
                }

                @Override
                public void onBitmapFailed(Drawable errorDrawable) {
                    startAnimationIN(mCoordinator);
                }

                @Override
                public void onPrepareLoad(Drawable placeHolderDrawable) {

                }
            };
        }

        Picasso.with(this)
                .load(capaGrande)
                .into(mPicassoTarget);
    }

    private void startAnimationIN(final View sharedElement) {
        sharedElement.getViewTreeObserver().addOnPreDrawListener(
                new ViewTreeObserver.OnPreDrawListener() {
                    @Override
                    public boolean onPreDraw() {
                        sharedElement.getViewTreeObserver().removeOnPreDrawListener(this);
                        ActivityCompat.startPostponedEnterTransition(DetailsActivity.this);
                        return true;
                    }
        });
    }

    private void setupToolbar(String title) {
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        if (mAppBar != null) {
            if (mAppBar.getLayoutParams() instanceof CoordinatorLayout.LayoutParams) {
                CoordinatorLayout.LayoutParams layoutParams = (CoordinatorLayout.LayoutParams) mAppBar.getLayoutParams();
                layoutParams.height = getResources().getDisplayMetrics().widthPixels;
            }
        }

        if (mCollapsingToolbarLayout != null) {
            getSupportActionBar().setDisplayShowTitleEnabled(true);
            mCollapsingToolbarLayout.setTitle(title);
        } else {
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }
    }

    public void showOrHide(final View view) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            int cx = getIntent().getIntExtra(X_POS_ANIM, view.getLeft() + (view.getWidth() / 2));
            int cy = getIntent().getIntExtra(Y_POS_ANIM, view.getTop() + (view.getHeight() / 2));
            int radius = Math.max(mCoordinator.getWidth(), view.getHeight());

            if (view.getVisibility() == View.INVISIBLE) {
                Animator anim = ViewAnimationUtils.createCircularReveal(view, cx, cy, 0, radius);
                anim.setDuration(4 * 100);
                view.setVisibility(View.VISIBLE);
                anim.start();
            }
        }
    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "Details Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app deep link URI is correct.
                Uri.parse("android-app://br.com.luizgadao.materialdesign/http/host/path")
        );
        AppIndex.AppIndexApi.start(client, viewAction);
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "Details Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app deep link URI is correct.
                Uri.parse("android-app://br.com.luizgadao.materialdesign/http/host/path")
        );
        AppIndex.AppIndexApi.end(client, viewAction);
        client.disconnect();
    }
}
