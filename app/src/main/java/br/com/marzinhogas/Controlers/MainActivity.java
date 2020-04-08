package br.com.marzinhogas.Controlers;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.google.firebase.messaging.FirebaseMessaging;

import androidx.drawerlayout.widget.DrawerLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.Menu;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.NumberPicker;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Date;

import br.com.marzinhogas.Helpers.AccessFirebase;
import br.com.marzinhogas.Models.Pedido;
import br.com.marzinhogas.Models.PrecoProdutos;
import br.com.marzinhogas.R;

public class MainActivity extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
    private DrawerLayout drawer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FirebaseApp.initializeApp(MainActivity.this);

        drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_gallery, R.id.nav_slideshow,
                R.id.nav_tools, R.id.nav_share, R.id.nav_send)
                .setDrawerLayout(drawer)
                .build();

        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);
        FirebaseApp.initializeApp(MainActivity.this);

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.sair) {

            AlertDialog.Builder alert_exit = new AlertDialog.Builder(MainActivity.this);
            alert_exit.setMessage("Você deseja realmente sair ?");

            alert_exit.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {

                    AccessFirebase.getInstance().sign_out_firebase(MainActivity.this);

                }
            }).setNegativeButton("Cancelar", null);

            alert_exit.show();

        } else if (id == android.R.id.home) {

            drawer.openDrawer(Gravity.LEFT);

        } else if (id == R.id.tabela_preco) {

            final Dialog dialog_tabela = new Dialog(MainActivity.this);

            dialog_tabela.setContentView(R.layout.dialog_tabela_de_preco);

            final TextView tabela_agua = dialog_tabela.findViewById(R.id.txt_preco_agua);
            final TextView tabela_gas = dialog_tabela.findViewById(R.id.txt_preco_gas);
            Button fechar_tabela = dialog_tabela.findViewById(R.id.btn_fechar_tabela);

            FirebaseFirestore.getInstance().collection("Tabeladepreco").document("precos").addSnapshotListener(new EventListener<DocumentSnapshot>() {
                @Override
                public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {

                    if (documentSnapshot != null) {
                        PrecoProdutos precoProdutos = new PrecoProdutos();

                        precoProdutos.setPreco_agua(String.valueOf(documentSnapshot.get("precoagua")));
                        precoProdutos.setPreco_gas(String.valueOf(documentSnapshot.get("precogas")));

                        tabela_agua.setText(precoProdutos.getPreco_agua());
                        tabela_gas.setText(precoProdutos.getPreco_gas());
                    }
                }
            });

            fechar_tabela.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog_tabela.dismiss();
                }
            });
            dialog_tabela.show();
        }
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_pedido_cliente, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }


}