package br.com.luizgadao.materialdesign;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import br.com.luizgadao.materialdesign.model.Disk;
import butterknife.Bind;
import butterknife.ButterKnife;

public class DetailsActivity extends AppCompatActivity {

    public static final String EXTRA_DISK = "disk";

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
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        Disk disk = (Disk) getIntent().getSerializableExtra(EXTRA_DISK);

        ButterKnife.bind(this);
        setupToolbar(disk.titulo);
        loadImage(LoadDisk.URL + disk.capaGrande);
        fillFields(disk);
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

}
