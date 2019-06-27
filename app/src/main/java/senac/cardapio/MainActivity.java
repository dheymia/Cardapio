package senac.cardapio;

import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.AsyncTaskLoader;
import androidx.loader.content.Loader;

import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;

import java.util.List;

import senac.cardapio.adapters.AdapterCardapio;
import senac.cardapio.models.Cardapio;
import senac.cardapio.models.ItemCardapio;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<Cardapio>> {

    Spinner spOpcoes;
    EditText txtQuantidade;
    ProgressBar loading;
    LoaderManager loaderManager;
    public static ItemCardapio itensCardapio;

    public static final String TIPO_TEXT_KEY = "Nomes";
    public static final String QTD_TEXT_KEY = "ser";
    public static final int OPERATION_SEARCH_LOADER = 15;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        spOpcoes = findViewById(R.id.spOpcoes);
        loading = findViewById(R.id.progressBar);
        loading.setVisibility(View.GONE);

        if (savedInstanceState != null) {
            if (savedInstanceState.containsKey(TIPO_TEXT_KEY)) {
                spOpcoes.setSelection(savedInstanceState.getInt(TIPO_TEXT_KEY));
            }
            if (savedInstanceState.containsKey(QTD_TEXT_KEY)) {
                txtQuantidade.setText(savedInstanceState.getString(QTD_TEXT_KEY));
            }
        }

        loaderManager = getSupportLoaderManager();

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    String tipo = spOpcoes.getSelectedItem().toString();
                    int quantidade = Integer.parseInt(txtQuantidade.getText().toString());

                    itensCardapio = new ItemCardapio(tipo, quantidade);

                    Loader<List<Cardapio>> loader = loaderManager.getLoader(OPERATION_SEARCH_LOADER);

                    if (loader == null) {
                        loaderManager.initLoader(OPERATION_SEARCH_LOADER, null, MainActivity.this);
                    } else {
                        loaderManager.restartLoader(OPERATION_SEARCH_LOADER, null, MainActivity.this);
                    }

                } catch (Exception ex) {
                    Log.e("OnClick", ex.getMessage());
                    Snackbar.make(view, ex.getMessage(), Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putInt(TIPO_TEXT_KEY, spOpcoes.getSelectedItemPosition());
        outState.putString(QTD_TEXT_KEY, txtQuantidade.getText().toString());
    }

    @NonNull
    @Override
    public Loader<List<Cardapio>> onCreateLoader(int id, @Nullable Bundle args) {
        return new AsyncTaskLoader<List<Cardapio>>(this) {
            @Nullable
            @Override
            public List<Cardapio> loadInBackground() {
                return itensCardapio.gerarFakes();
            }

            @Override
            protected void onStartLoading() {
                listaCardapio.setVisibility(View.INVISIBLE);
                loading.setVisibility(View.VISIBLE);
                forceLoad();
            }
        };
    }

    @Override
    public void onLoadFinished(@NonNull Loader<List<Cardapio>> loader, List<Cardapio> data) {
        loading.setVisibility(View.GONE);

        listaCardapio.setVisibility(View.VISIBLE);

        listaCardapio.setAdapter(new AdapterCardapio(data, getBaseContext()));
    }

    @Override
    public void onLoaderReset(@NonNull Loader<List<Cardapio>> loader) {
        //Leave it for now as it is
    }
}
