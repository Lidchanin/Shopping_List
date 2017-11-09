package com.lidchanin.crudindiploma.customview;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.lidchanin.crudindiploma.Constants;
import com.lidchanin.crudindiploma.R;
import com.lidchanin.crudindiploma.adapters.ProfitAdapter;
import com.lidchanin.crudindiploma.fragments.AboutUsFragment;
import com.lidchanin.crudindiploma.fragments.AllProductsFragment;
import com.lidchanin.crudindiploma.fragments.ProfitFragment;
import com.lidchanin.crudindiploma.fragments.SettingsFragment;
import com.lidchanin.crudindiploma.fragments.ShoppingListFragment;
import com.lidchanin.crudindiploma.fragments.StatisticsFragment;
import com.lidchanin.crudindiploma.utils.ThemeManager;
import com.makeramen.roundedimageview.RoundedTransformationBuilder;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

public class NavigationDrawerActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, GoogleApiClient.OnConnectionFailedListener {

    private static final int RC_SIGN_IN = 9001;

    private static final String TAG = NavigationDrawerActivity.class.getCanonicalName();

    private ImageButton buttonHamburger;
    private DrawerLayout drawer;
    private Uri photoUrl;
    private String accountName;
    private String accountEmail;
    private FirebaseUser currentUser;
    private SignInButton signInButton;
    private FirebaseAuth mAuth;
    private TextView nameTextView;
    private TextView emailTextView;
    private ImageView headerImageView;
    private Transformation transformation;
    private TextView pageTitle;
    private ImageButton alphabetSort;
    private GoogleApiClient googleApiClient;
    private ImageButton dateSort;
    private ImageButton addItem;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        new ThemeManager(this);
        super.onCreate(savedInstanceState);

    }

    @Override
    public void setContentView(@LayoutRes int layoutResID) {
        super.setContentView(R.layout.activity_drawer);
        ViewGroup content = (ViewGroup) findViewById(R.id.container);
        if (content != null) {
            getLayoutInflater().inflate(layoutResID, content);
        }
        initNavigationDrawer();
        initializeViewsAndButtons();
        currentUser = mAuth.getCurrentUser();

        if (currentUser != null) {
            photoUrl = currentUser.getPhotoUrl();
            accountName = currentUser.getDisplayName();
            accountEmail = currentUser.getEmail();
            emailTextView.setText(accountEmail);
            nameTextView.setText(accountName);
            signInButton.setVisibility(View.GONE);
            Picasso.with(getApplicationContext()).load(photoUrl).transform(transformation).into(headerImageView);
        } else {
            emailTextView.setVisibility(View.GONE);
            nameTextView.setVisibility(View.GONE);
            Picasso.with(getApplicationContext()).load(photoUrl).transform(transformation)
                    .into(headerImageView);
        }
    }

    private void initNavigationDrawer() {
        mAuth = FirebaseAuth.getInstance();
        transformation = new RoundedTransformationBuilder().oval(true).build();
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        View headerLayout = navigationView.inflateHeaderView(R.layout.nav_header_main);
        addItem = (ImageButton) findViewById(R.id.add_button);
        alphabetSort = (ImageButton) findViewById(R.id.main_screen_image_button_sort_by_alphabet);
        dateSort = (ImageButton) findViewById(R.id.main_screen_image_button_sort_by_date);
        headerImageView = (ImageView) headerLayout.findViewById(R.id.headerImageView);
        emailTextView = (TextView) headerLayout.findViewById(R.id.user_mail);
        nameTextView = (TextView) headerLayout.findViewById(R.id.user_name);
        signInButton = (SignInButton) headerLayout.findViewById(R.id.sing_in_button);
        headerLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signOut();
            }
        });
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        googleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso).build();
        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signIn();
            }
        });
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, null, R.string.navigation_drawer_open,
                R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        navigationView.setNavigationItemSelectedListener(this);
        toggle.syncState();
    }

    public void setButtonsToDefault() {
        addItem.setVisibility(View.GONE);
        alphabetSort.setVisibility(View.GONE);
        dateSort.setVisibility(View.GONE);
    }

    private void initializeViewsAndButtons() {
        pageTitle = (TextView) findViewById(R.id.title);
        buttonHamburger = (ImageButton) findViewById(R.id.hamburger);
        buttonHamburger.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawer.openDrawer(Gravity.START);
            }
        });

    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.nav_lists:
                initFragment(Constants.Bundles.SHOPPING_LIST_FRAGMENT_ID);
                break;
            case R.id.nav_existing_products:
                initFragment(Constants.Bundles.ALL_PRODUCTS_FRAGMENT_ID);
                break;
            case R.id.nav_profit:
                initFragment(Constants.Bundles.PROFIT_FRAGMENT_ID);
                break;
            case R.id.nav_statistics:
                initFragment(Constants.Bundles.STATISTICS_FRAGMENT_ID);
                break;
            case R.id.nav_settings:
                initFragment(Constants.Bundles.SETTINGS_FRAGMENT_ID);
                break;
            case R.id.nav_about_us:
                initFragment(Constants.Bundles.ABOUT_US_FRAGMENT_ID);
                break;
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void addNewItem(final ProfitAdapter profitAdapter) {
        addItem.setVisibility(View.VISIBLE);
        addItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                profitAdapter.addNewItem();
            }
        });
    }

    public ImageButton addNewItem() {
        return addItem;
    }

    public void setTitle(String title) {
        pageTitle.setText(title);
    }


    public void initFragment(String fragmentExtra) {
        initFragment(fragmentExtra, 0);
    }

    public void initFragment(String fragmentExtra, int page) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        setButtonsToDefault();
        switch (fragmentExtra) {
            case Constants.Bundles.SHOPPING_LIST_FRAGMENT_ID:
                ShoppingListFragment shoppingListFragment = new ShoppingListFragment();
                fragmentTransaction.replace(R.id.container, shoppingListFragment);
                fragmentTransaction.commit();
                break;
            case Constants.Bundles.STATISTICS_FRAGMENT_ID:
                StatisticsFragment statisticsFragment = new StatisticsFragment();
                fragmentTransaction.replace(R.id.container, statisticsFragment);
                fragmentTransaction.commit();
                break;
            case Constants.Bundles.PROFIT_FRAGMENT_ID:
                ProfitFragment profitFragment = new ProfitFragment();
                fragmentTransaction.replace(R.id.container, profitFragment);
                fragmentTransaction.commit();
                break;
            case Constants.Bundles.SETTINGS_FRAGMENT_ID:
                SettingsFragment settingsFragment = new SettingsFragment();
                if (page != 0) {
                    Bundle bundle = new Bundle();
                    bundle.putInt(Constants.Bundles.VIEWPAGER_PAGE, page);
                    settingsFragment.setArguments(bundle);
                }
                fragmentTransaction.replace(R.id.container, settingsFragment);
                fragmentTransaction.commit();
                break;
            case Constants.Bundles.ALL_PRODUCTS_FRAGMENT_ID:
                AllProductsFragment allProductsFragment = new AllProductsFragment();
                fragmentTransaction.replace(R.id.container, allProductsFragment);
                fragmentTransaction.commit();
                break;
            case Constants.Bundles.ABOUT_US_FRAGMENT_ID:
                AboutUsFragment aboutUsFragment = new AboutUsFragment();
                fragmentTransaction.replace(R.id.container, aboutUsFragment);
                fragmentTransaction.commit();
                break;
        }
    }

    private void signIn() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(googleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    private void signOut() {
        Auth.GoogleSignInApi.signOut(googleApiClient);
        mAuth.signOut();
        headerImageView.setImageResource(R.mipmap.ic_launcher_1);
        signInButton.setVisibility(View.VISIBLE);
        emailTextView.setVisibility(View.GONE);
        nameTextView.setVisibility(View.GONE);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            if (result.isSuccess()) {
                GoogleSignInAccount account = result.getSignInAccount();
                firebaseAuthWithGoogle(account);
                assert account != null;
                photoUrl = account.getPhotoUrl();
                accountName = account.getDisplayName();
                accountEmail = account.getEmail();
                emailTextView.setVisibility(View.VISIBLE);
                nameTextView.setVisibility(View.VISIBLE);
                emailTextView.setText(accountEmail);
                nameTextView.setText(accountName);
                Picasso.with(getApplicationContext()).load(photoUrl).transform(transformation).into(headerImageView);
                signInButton.setVisibility(View.GONE);
            } else {
                Toast.makeText(NavigationDrawerActivity.this, getString(R.string.auth_filed), Toast.LENGTH_SHORT).show();
            }
        }
    }


    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            FirebaseUser user = mAuth.getCurrentUser();
                        } else {
                            Toast.makeText(NavigationDrawerActivity.this, getString(R.string.auth_filed),
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
}
