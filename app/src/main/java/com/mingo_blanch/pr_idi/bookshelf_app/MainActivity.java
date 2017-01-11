package com.mingo_blanch.pr_idi.bookshelf_app;


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

import com.mingo_blanch.pr_idi.bookshelf_app.About.AboutFragment;
import com.mingo_blanch.pr_idi.bookshelf_app.AddBook.AddBookFragment;
import com.mingo_blanch.pr_idi.bookshelf_app.BookDatabase.Book;
import com.mingo_blanch.pr_idi.bookshelf_app.BookDatabase.BookData;
import com.mingo_blanch.pr_idi.bookshelf_app.BooksByAuthor.BooksByAuthorFragment;
import com.mingo_blanch.pr_idi.bookshelf_app.BooksByCategory.BooksByCategoryFragment;
import com.mingo_blanch.pr_idi.bookshelf_app.DeleteBook.DeleteDialogFragment;
import com.mingo_blanch.pr_idi.bookshelf_app.EditPersonalEvaluation.PersEvalDialogFragment;
import com.mingo_blanch.pr_idi.bookshelf_app.Help.HelpFragment;
import com.mingo_blanch.pr_idi.bookshelf_app.MainWindow.MainFragment;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,
        AddBookFragment.AddBookFragmentListener,
            DeleteDialogFragment.DeleteBookListener, PersEvalDialogFragment.PersEvalListener {

    private BookData bookData;
    private Fragment mFragment;
    private Fragment mBackFragment;
    private SearchView mSearchView;
    private boolean addingBook;
    private Toolbar mToolbar;
    private NavigationView mNavigationView;
    private int mNavItemId;
    private CharSequence mQueryText;
    private CharSequence mTitle;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bookData = new BookData(this);
        bookData.open();
        addingBook = false;

        // Check that the activity is using the layout version with
        // the fragment_container FrameLayout
        if (findViewById(R.id.fragment_container) != null) {

            // However, if we're being restored from a previous state,
            // then we don't need to do anything and should return or else
            // we could end up with overlapping fragments.
            if (savedInstanceState != null) {
                setToolbar();
                setNavigationDrawer();
                setFloatingActionButton();
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

        setToolbar();
        setNavigationDrawer();
        setFloatingActionButton();

        // Set the database
        if (bookData.getAllBooks().size() == 0) setDataBase();
    }

    private void setToolbar() {
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
    }

    private void setNavigationDrawer() {
        DrawerLayout mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle mDrawerToggle = new ActionBarDrawerToggle(
                this, mDrawerLayout, mToolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        mDrawerLayout.setDrawerListener(mDrawerToggle);
        mDrawerToggle.syncState();

        mNavigationView = (NavigationView) findViewById(R.id.nav_view);
        mNavigationView.setNavigationItemSelectedListener(this);
        mNavItemId = R.id.nav_home;
        mNavigationView.getMenu().findItem(mNavItemId).setChecked(true);
    }

    private void setFloatingActionButton() {
        FloatingActionButton fabCreate = (FloatingActionButton)findViewById(R.id.fab_btn_create);
        fabCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onAddBook();
            }
        });
        FloatingActionButton fabCreationDone = (FloatingActionButton)findViewById(R.id.fab_btn_creation_done);
        fabCreationDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((AddBookFragment)mFragment).saveListener();
            }
        });
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
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else if (mSearchView != null && !mSearchView.isIconified()) {
            mSearchView.setIconified(true);
        } else if (addingBook) {
            addingBook = false;
            // Navigation drawer: check last navigation drawer item, before creating book
            setAddBookNavItemChecked(false);
            replaceFragment(mBackFragment);
        } else if (!(mFragment instanceof MainFragment)){
            onNavigationItemSelected(mNavigationView.getMenu().findItem(R.id.nav_home));
        } else
            super.onBackPressed();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present
        getMenuInflater().inflate(R.menu.options_menu, menu);

        // Associate the item with the view
        MenuItem searchItem = menu.findItem(R.id.search);
        mSearchView = (SearchView) searchItem.getActionView();

        mSearchView.setQueryHint(getString(R.string.search_title) + "...");
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        mSearchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                ((SearchableList)mFragment).filter(newText);
                return true;
            }
        });

        if (mQueryText != null) {
            mSearchView.setIconified(false);
            mSearchView.setQuery(mQueryText, false);
        }

        if (mTitle != null) {
            setActionBarTitle((String) mTitle);
            mTitle = null;
        }

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.

        switch (item.getItemId()) {
            case R.id.search :
                mSearchView.setIconified(false);
                return true;
            default :
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem menuItem) {
        // Update the checked item in the navigation menu
        mNavItemId = menuItem.getItemId();

        switch (menuItem.getItemId()) {
            case R.id.nav_books_by_author :
                replaceFragment(new BooksByAuthorFragment());
                break;
            case R.id.nav_home :
                replaceFragment(new MainFragment());
                break;
            case R.id.nav_list_category:
                replaceFragment(new BooksByCategoryFragment());
                break;
            case R.id.nav_create_book:
                replaceFragment(new AddBookFragment());
                break;
            case R.id.nav_about:
                replaceFragment(new AboutFragment());
                break;
            case R.id.nav_help:
                replaceFragment(new HelpFragment());
                break;
        }

        // Close the navigation drawer
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);

        // Reload navigation drawer
        mNavigationView.getMenu().clear();
        mNavigationView.inflateMenu(R.menu.activity_main_drawer);
        mNavigationView.getMenu().findItem(mNavItemId).setChecked(true);

        return true;
    }

    private void replaceFragment(Fragment newFragment) {
        mBackFragment = mFragment; // Save the fragment before replace it
        FragmentManager mFragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = mFragmentManager.beginTransaction();
        // Replace whatever is in the fragment_container view with this fragment
        transaction.replace(R.id.fragment_container, newFragment);
        // Add the transaction to the back stack so the user can navigate back
        //transaction.addToBackStack(null);
        // Commit the transaction
        transaction.commit();
        // Update global variable
        mFragment = newFragment;

        // Set the search item iconified
        mSearchView.setIconified(true);
    }

    private void showBookDeletedSnackbar(final Book b) {
        Snackbar.make(findViewById(R.id.clayout), "'"+b.getTitle()+"' "+getString(R.string.was_deleted), Snackbar.LENGTH_LONG)
                .setAction(getString(R.string.UNDO), new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        bookData.addBook(b);
                        ((UpdatableList)mFragment).updateList();
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
                        ((UpdatableList)mFragment).updateList();
                        Snackbar snackbar1 = Snackbar.make(findViewById(R.id.clayout), "'"+b.getTitle()+"' " + getString(R.string.was_deleted), Snackbar.LENGTH_LONG);
                        snackbar1.show();
                    }
                })
                .show();
    }

    private void showPersEvalEditedSnackbar(final Book b, final String persEval) {
        Snackbar.make(findViewById(R.id.clayout), getString(R.string.your_pers_eval_for_book) + " '"+b.getTitle()+"' " + getString(R.string.is) + ": " + b.getPersonal_evaluation(), Snackbar.LENGTH_LONG)
                .setAction(getString(R.string.UNDO), new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        bookData.updatePersonalEvaluation(b.getId(), persEval);
                        ((UpdatableList)mFragment).updateList();
                        Snackbar snackbar1 = Snackbar.make(findViewById(R.id.clayout), getString(R.string.changes_undone), Snackbar.LENGTH_LONG);
                        snackbar1.show();
                    }
                })
                .show();
    }

    private void onAddBook() {
        addingBook = true;
        setAddBookNavItemChecked(true);
        replaceFragment(new AddBookFragment());
    }

    @Override
    public void onBookCreated(final Book b) {
        replaceFragment(mBackFragment);
        setAddBookNavItemChecked(false);
        showBookCreatedSnackbar(b);
        addingBook = false;
    }

    @Override
    public void onBookDeleteConfirmed(Book b) {
        bookData.deleteBookById(b.getId());
        ((UpdatableList)mFragment).updateList();
        showBookDeletedSnackbar(b);
    }

    @Override
    public void onEditPersEvalConfirmed(Book book, String persEval) {
        ((UpdatableList)mFragment).updateList();
        showPersEvalEditedSnackbar(book, persEval);
    }

    public void setActionBarTitle(String title) {
        if (mToolbar != null)
            mToolbar.setTitle(title);
    }

    public void setAddBookNavItemChecked(boolean check) {
        mNavigationView.getMenu().clear();
        mNavigationView.inflateMenu(R.menu.activity_main_drawer);
        if (check)
            mNavigationView.getMenu().findItem(R.id.nav_create_book).setChecked(true);
        else
            mNavigationView.getMenu().findItem(mNavItemId).setChecked(true);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        // Save mFragment
        getSupportFragmentManager().putFragment(outState, "mFragment", mFragment);
        // Save navigation item to check after configuration change
        outState.putInt("mNavItemId", mNavItemId);
        // Save search view state
        if (mSearchView.isIconified())
            mQueryText = null;
        else
            mQueryText = mSearchView.getQuery();
        outState.putCharSequence("mQueryText", mQueryText);
        // Save toolbar title
        if (mToolbar.getTitle() == getString(R.string.app_name))
            mTitle = null;
        else
            mTitle = mToolbar.getTitle();
        outState.putCharSequence("mTitle", mTitle);
    }

    @Override
    protected void onRestoreInstanceState(Bundle inState) {
        super.onRestoreInstanceState(inState);
        // Restore mFragment
        mFragment = getSupportFragmentManager().getFragment(inState, "mFragment");
        // Restore navigation item to check after configuration change
        mNavItemId = inState.getInt("mNavItemId");
        // Reload navigation drawer menu
        mNavigationView.getMenu().clear();
        mNavigationView.inflateMenu(R.menu.activity_main_drawer);
        mNavigationView.getMenu().findItem(mNavItemId).setChecked(true);
        // Restore search view
        mQueryText = inState.getCharSequence("mQueryText");
        // Restore toolbar title
        mTitle = inState.getCharSequence("mTitle");
    }

    @SuppressWarnings("unused")
    private void setDataBase() {
        //////////////////////////////// SET DATABASE
        bookData.createBook("Harry Potter i la pedra filosofal", "J. K. Rowling", 1999, "Empúries", "Novel·la fantàstica", getString(R.string.good));
        bookData.createBook("Steve Jobs", "Walter Isaacson", 2011, "Simon & Schuster", "Biografia", getString(R.string.very_good));
        bookData.createBook("The Da Vinci Code", "Dan Brown", 2003, "Doubleday", "Thriller", getString(R.string.regular));
        bookData.createBook("Cien años de soledad", "Gabriel García Márquez", 1967, "Harper", "Novel·la", getString(R.string.very_bad));
    }
}