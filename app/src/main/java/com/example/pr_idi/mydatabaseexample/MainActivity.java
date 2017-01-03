package com.example.pr_idi.mydatabaseexample;

import android.app.SearchManager;
import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.example.pr_idi.mydatabaseexample.BooksByAuthor.BooksByAuthorFragment;
import com.example.pr_idi.mydatabaseexample.BooksByCategory.ListCategoryFragment;
import com.example.pr_idi.mydatabaseexample.CreateBook.CreateBookFragment;
import com.example.pr_idi.mydatabaseexample.MainWindow.MainFragment;

import java.util.List;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, ListCategoryFragment.ListCategoryFragmentListener, CreateBookFragment.CreateBookFragmentListener {

    private BookData bookData;
    private Fragment mFragment;
    private SearchView mSearchView;
    private FragmentManager mFragmentManager;
    private MenuItem mHomeItem;
    private NavigationView mNavigationView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bookData = new BookData(this);
        bookData.open();

        // Check that the activity is using the layout version with
        // the fragment_container FrameLayout
        if (findViewById(R.id.fragment_container) != null) {

            // However, if we're being restored from a previous state,
            // then we don't need to do anything and should return or else
            // we could end up with overlapping fragments.
            if (savedInstanceState != null) {
                return;
            }

            // Create a new Fragment to be placed in the activity layout
            mFragment = new MainFragment();

            // In case this activity was started with special instructions from an
            // Intent, pass the Intent's extras to the fragment as arguments
            mFragment.setArguments(getIntent().getExtras());

            // Add the fragment to the 'fragment_container' FrameLayout
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.fragment_container, mFragment).commit();
        }

        // Toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Navigation Drawer
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        mNavigationView = (NavigationView) findViewById(R.id.nav_view);
        mNavigationView.setNavigationItemSelectedListener(this);

        FloatingActionButton fabCreate = (FloatingActionButton)findViewById(R.id.fab_btn_create);
        fabCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("MyActivity","FAB premut!");
                onAddBook();
            }
        });
        FloatingActionButton fabCreationDone = (FloatingActionButton)findViewById(R.id.fab_btn_creation_done);
        fabCreationDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("MyActivity","FAB2 premut!");
                ((CreateBookFragment)mFragment).saveListener();
            }
        });

        // Set the database
        //setDataBase();
    }

    // Life cycle methods. Check whether it is necessary to reimplement them

    @Override
    protected void onResume() {
        bookData.open();
        super.onResume();
    }

    // Life cycle methods. Check whether it is necessary to reimplement them

    @Override
    protected void onPause() {
        bookData.close();
        super.onPause();
    }

    @Override
    public void onBackPressed() { // TODO: Check changes [Arnau]
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
            return;
        }
        else if (mFragmentManager.getBackStackEntryCount() == 1) {
            // If the fragment to show is the MainFragment
            //mSearchView.setQueryHint("Cercar títol...");
            mSearchView.setIconified(true);
            mHomeItem.setVisible(false);
        }
        super.onBackPressed();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present
        getMenuInflater().inflate(R.menu.options_menu, menu);
        // Associate the item with the view
        MenuItem searchItem = menu.findItem(R.id.search);
        mSearchView = (SearchView) searchItem.getActionView();

        mHomeItem = menu.findItem(R.id.home);

        mSearchView.setQueryHint("Cercar títol...");

        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        mSearchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                switch (mFragment.getClass().getSimpleName()) {
                    case "MainFragment" :
                        ((MainFragment) mFragment).filter(newText);
                        break;
                    case "BooksByAuthorFragment" :
                        ((BooksByAuthorFragment)mFragment).filter(newText);
                        break;
                    default :
                        break;
                }
                return true;
            }
        });

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.

        switch (item.getItemId()) {
            case R.id.search :
                mSearchView = (SearchView) item.getActionView();
                mSearchView.setIconified(false);
                return true;
            case R.id.home :
                replaceFragment(new MainFragment(), true, false);
            default :
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case R.id.nav_books_by_author :
                replaceFragment(new BooksByAuthorFragment(), true, true);
                break;
            case R.id.nav_home :
                replaceFragment(new MainFragment(), true, false);
                break;
            case R.id.nav_list_category:
                replaceFragment(new ListCategoryFragment(), true, true);
                break;
            case R.id.create_book_fragment:
                replaceFragment(new CreateBookFragment(), true, true);
                break;
            default :
                break;
        }

        // Close the navigation drawer
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);

        return true;
    }

    /**
     * @param newFragment fragment that replace the actual fragment
     * @param iconifiedSV collapse (true) or expande (false) the search button
     * @param visibleHI set the toolbar home button visible (true) or not (false)
     */
    private void replaceFragment(Fragment newFragment, boolean iconifiedSV,
                                 boolean visibleHI) {
        mFragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = mFragmentManager.beginTransaction();
        // Replace whatever is in the fragment_container view with this fragment
        transaction.replace(R.id.fragment_container, newFragment);
        // Add the transaction to the back stack so the user can navigate back
        transaction.addToBackStack(null);
        // Commit the transaction
        transaction.commit();
        // Update global variable
        mFragment = newFragment;

        // Set the search item iconified
        mSearchView.setIconified(iconifiedSV);
        // Set the home item visible
        mHomeItem.setVisible(visibleHI);
    }

    private void setDataBase() {
        //////////////////////////////// SET DATABASE
        bookData.createBook("1984", "George Orwell", 1949, "Unknown", "Distopía", "REGULAR");
        bookData.createBook("Cien años de soledad", "Gabriel García Márquez", 1967, "Desconegut", "Novel·la", getString(R.string.very_good));
        bookData.createBook("El Conde de Montecristo", "Alexandre Dumas", 1844, "Desconegut", "Novel·la", getString(R.string.very_bad));
        bookData.createBook("La Divina comedia", "Dante", 1300, "Desconegut", "Vers", getString(R.string.bad));
        bookData.createBook("Don Quijote de la Mancha", "Miguel de Cervantes", 1605, "Desconegut", "Novel·la", getString(R.string.good));
        bookData.createBook("El Gran Gatsby", "Francis Scott Fitzgerald", 1925, "Desconegut", "Novel·la", getString(R.string.regular));
        bookData.createBook("Todo esto te daré", "Dolores Redondo", 2016, "Editorial Planeta", "Novel·la", getString(R.string.regular));
        bookData.createBook("El guardián invisible", "Dolores Redondo", 2013, "Editorial Destino", "Thriller", getString(R.string.good));
        bookData.createBook("Legado en los huesos", "Dolores Redondo", 2013, "Editorial Destino", "Thriller", getString(R.string.bad));
        bookData.createBook("El amor en los tiempos del cólera", "Gabriel García Márquez", 1985, "Desconegut", "Novel·la", getString(R.string.good));
        bookData.createBook("Crónica de una muerte anunciada", "Gabriel García Márquez", 1981, "Desconegut", "Novel·la", getString(R.string.very_good));
        bookData.createBook("El otoño del patriarca", "Gabriel García Márquez", 1975, "Desconegut", "Novel·la", getString(R.string.very_bad));
        bookData.createBook("El proceso", "Franz Kafka", 1925, "Desconegut", "Novel·la", getString(R.string.regular));
        bookData.createBook("La Metamorfosis", "Franz Kafka", 1915, "Desconegut", "Relat", getString(R.string.very_good));
    }

    @Override
    public void onAddBook() {
        replaceFragment(new CreateBookFragment(), true, false); // TODO: què són el 2n i 3r paràmetre?
    }


    private void showBookDeletedSnackbar(final Book b) {
        Snackbar.make(findViewById(R.id.clayout), "'"+b.getTitle()+"' "+getString(R.string.was_deleted), Snackbar.LENGTH_LONG)
                .setAction(getString(R.string.UNDO), new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        bookData.addBook(b);
                        ((ListCategoryFragment)mFragment).updateBooks();
                        Snackbar snackbar1 = Snackbar.make(findViewById(R.id.clayout), "'"+b.getTitle()+"' " + getString(R.string.was_restored), Snackbar.LENGTH_LONG);
                        snackbar1.show();
                    }
                })
                .show();
    }

    private void showBookCreatedSnackbar(final Book b) {
        Snackbar.make(findViewById(R.id.clayout), "'"+b.getTitle()+"' " + getString(R.string.was_created), Snackbar.LENGTH_LONG)
                .setAction(getString(R.string.UNDO), new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        bookData.deleteBookById(b.getId());
                        ((ListCategoryFragment)mFragment).updateBooks();
                        Snackbar snackbar1 = Snackbar.make(findViewById(R.id.clayout), "'"+b.getTitle()+"' " + getString(R.string.was_deleted), Snackbar.LENGTH_LONG);
                        snackbar1.show();
                    }
                })
                .show();
    }

    @Override
    public void onBookCreated(final Book b) {
        replaceFragment(new ListCategoryFragment(), true, false); // TODO: què són el 2n i 3r paràmetre?
        showBookCreatedSnackbar(b);
        // TODO: snackbar informatiu + adaptar segons fragment previ a la creació
    }

    public void onBookDeleteConfirmed(Book b) {
        bookData.deleteBookById(b.getId());
        ((ListCategoryFragment)mFragment).updateBooks();
        showBookDeletedSnackbar(b);
    }
}