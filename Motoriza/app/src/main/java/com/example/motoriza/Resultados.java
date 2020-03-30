package com.example.motoriza;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class Resultados extends AppCompatActivity {

    private TextView nomeMontadora;
    private ListView listaMotorizacao;
    private ImageView botaoVoltar;
    private TextView categoria;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resultados);

        nomeMontadora = (TextView) findViewById(R.id.nomeMontadora_id);
        categoria = (TextView) findViewById(R.id.categoria_id);
        listaMotorizacao = (ListView) findViewById(R.id.listaResultadoVeiculosMotorizacao_id);
        botaoVoltar = (ImageView) findViewById(R.id.botaoVoltar_id);
        Bundle extra = getIntent().getExtras();

        botaoVoltar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        if(extra != null){
            String termoPassado = extra.getString("montadora");
            nomeMontadora.setText(termoPassado);
            String termoPassado2 = extra.getString("categoria");
            categoria.setText(termoPassado2);
            String[] listaPassada = extra.getStringArray("motorizacao");

            System.out.println(Arrays.toString(listaPassada));
            assert listaPassada != null;
            ArrayAdapter<String> adaptador = new ArrayAdapter<String>(
                    getApplicationContext(),android.R.layout.simple_list_item_1, android.R.id.text1, listaPassada);
            listaMotorizacao.setAdapter(adaptador);

        }
        listaMotorizacao.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                System.out.println(parent.getItemAtPosition(position).toString());
                Intent retornaIntent = new Intent();
                retornaIntent.putExtra("veiculoEscolhido", parent.getItemAtPosition(position).toString());
                retornaIntent.putExtra("nomeMontadora", nomeMontadora.getText());
                retornaIntent.putExtra("categoria", categoria.getText());
                setResult(RESULT_OK, retornaIntent);
                finish();

            }
        });
    }
}
