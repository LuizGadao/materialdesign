package br.com.luizgadao.materialdesign;

import android.animation.Animator;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.transition.Explode;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import br.com.luizgadao.materialdesign.model.Disk;
import butterknife.Bind;
import butterknife.ButterKnife;

public class DetailsActivity extends AppCompatActivity {

    public static final String EXTRA_DISK = "disk";
    public static final String X_POS_ANIM = "CX";
    public static final String Y_POS_ANIM = "CY";

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
            Window window = getWindow();
            window.requestFeature(Window.FEATURE_CONTENT_TRANSITIONS);
            window.setExitTransition(new Explode());
            window.setEnterTransition(new Explode());
        }
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_details);

        Disk disk = (Disk) getIntent().getSerializableExtra(EXTRA_DISK);

        ButterKnife.bind(this);
        setupToolbar(disk.titulo);
        loadImage(LoadDisk.URL + disk.capaGrande);
        fillFields(disk);

        mCoordinator.setVisibility(View.INVISIBLE);
        mCoordinator.post(new Runnable() {
            @Override
            public void run() {
                showOrHide(mCoordinator);
            }
        });
    }

    private void fillFields(Disk disk) {
        mTxtTitulo.setText(disk.titulo);
        mTxtAno.setText(String.valueOf(disk.ano));
        mTxtGravadora.setText(disk.gravadora);
        StringBuilder sb = new StringBuilder();
        for (String integrante : disk.formacao){
            if (sb.length() != 0) sb.append('\n');
            sb.append(integrante);
        }
        mTxtFormacao.setText(sb.toString());
        sb = new StringBuilder();
        for (int i = 0; i < disk.faixas.length; i++){
            if (sb.length() != 0)
                sb.append('\n');
            sb.append(i+1).append(". ").append(disk.faixas[i]);
        }
        mTxtMusicas.setText(sb.toString());
    }

    private void loadImage(String capaGrande) {
        Picasso.with(this)
                .load(capaGrande)
                .into(mImgCapa);
    }

    private void setupToolbar(String title) {
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        if (mAppBar != null){
            if (mAppBar.getLayoutParams() instanceof CoordinatorLayout.LayoutParams){
                CoordinatorLayout.LayoutParams layoutParams = (CoordinatorLayout.LayoutParams) mAppBar.getLayoutParams();
                layoutParams.height = getResources().getDisplayMetrics().widthPixels;
            }
        }

        if (mCollapsingToolbarLayout != null){
            getSupportActionBar().setDisplayShowTitleEnabled(true);
            mCollapsingToolbarLayout.setTitle(title);
        }else {
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }
    }

    public void showOrHide(final View view){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
            int cx = getIntent().getIntExtra(X_POS_ANIM, view.getLeft() + (view.getWidth() / 2));
            int cy = getIntent().getIntExtra(Y_POS_ANIM, view.getTop() + (view.getHeight() / 2));
            int radius = Math.max(mCoordinator.getWidth(), view.getHeight());

            if (view.getVisibility() == View.INVISIBLE){
                Animator anim = ViewAnimationUtils.createCircularReveal(view, cx, cy, 0, radius);
                anim.setDuration(4 * 100);
                view.setVisibility(View.VISIBLE);
                anim.start();
            }
        }
    }

}
