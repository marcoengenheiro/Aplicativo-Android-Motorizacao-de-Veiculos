package com.example.motoriza;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
//import android.util.Log;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import android.R.layout;
import android.R.id;
import android.widget.Toast;
import org.json.JSONArray;
import retrofit2.Response;
import retrofit2.Call;
import retrofit2.Callback;


public class MainActivity extends AppCompatActivity {

    private Spinner tipoVeiculo_id;
    private Spinner montadora_id;
    private TextView textView;
    private List dados;
    private TextView categoriaUltimoResultado_id;
    private TextView montadoraUltimoResultado_id;
    private TextView motorizacaoUltimoResultado_id;
    //private ListView ultimaPesquisa_id;
    //private Button botao_id;
    private String[] tiposMontadoras = new String[5];
    private String[] veiculosMontadoras = new String[10];
    private String tipo;
    private String nomeMontadora;
    private List<String> listaIds = new ArrayList<String>();
    private List<String> listaNomeMontadora = new ArrayList<String>();
    private Boolean extrairTipo;
    private JSONArray arr;
    public String DadosURL;
    //private Object urlext;
    public String url;
    private static final String ARQUIVO_SALVAR = "ArquivoSalvar";
    static final int CODIGO_REQUISICAO = 1;
    private SQLiteDatabase bancoDados;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // listas suspensas (tipos de veículos e as montadoras)
        tipoVeiculo_id = (Spinner) findViewById(R.id.tipoVeiculo_id);
        montadora_id = (Spinner) findViewById(R.id.montadora_id);

        // Armazena as últimas escolhas realizadas pelo app ao reabrir
        categoriaUltimoResultado_id = (TextView) findViewById(R.id.categoriaUltimaPesquisa_id);
        montadoraUltimoResultado_id = (TextView) findViewById(R.id.montadoraUltimaPesquisa_id);
        motorizacaoUltimoResultado_id = (TextView) findViewById(R.id.motorizacaoUltimaPesquisa_id);

        SharedPreferences sharedPreferences = getSharedPreferences(ARQUIVO_SALVAR, 0);
        // recupera o nome da montadora
        if (sharedPreferences.contains("nomeMontadoraSalvo")){
            String dadoSalvo = sharedPreferences.getString("nomeMontadoraSalvo", " ");
            montadoraUltimoResultado_id.setText(dadoSalvo);
            System.out.println(dadoSalvo);
        }
        // recupera a categoria do veículo
        if (sharedPreferences.contains("categoriaSalvo")){
            String dadoSalvo2 = sharedPreferences.getString("categoriaSalvo", " ");
            categoriaUltimoResultado_id.setText(dadoSalvo2);
        }
        // // recupera o veículo junto de sua motorização
        if (sharedPreferences.contains("veiculoEscolhidoSalvo")){
            String dadoSalvo3 = sharedPreferences.getString("veiculoEscolhidoSalvo", " ");
            motorizacaoUltimoResultado_id.setText(dadoSalvo3);
        }


        // Endereço URL para a obtenção dos dados referentes aos tipos de veículos
        url = "https://service.tecnomotor.com.br/iRasther/tipo";
        // usado para diferenciar o tipo de requisição
        extrairTipo = Boolean.TRUE;
        // Chama a execução da classe responsável por requisitar os dados via web
        new ExtrairDadosURL().execute(url);


        // Capta o item (tipo de veículo) selecionado pelo usuário na lista suspensa populada pelos
        // dados vindos da web
        tipoVeiculo_id.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int itemIdex, final long id) {
                tipo = parent.getItemAtPosition(itemIdex).toString();
                // Apenas para averiguação:
                //Log.e("Tipo", tipo);
                //Log.e("Tipo", Integer.toString(itemIdex));

                if(itemIdex == 1){
                    LevesServices service = FonteEndPoints.getRetrofitInstance().create(LevesServices.class);
                    service.getLista().enqueue(new Callback<List<Dados>>() {
                        @Override
                        public void onResponse(Call<List<Dados>> call, Response<List<Dados>> response) {
                            Toast.makeText(MainActivity.this, "Carregamento OK", Toast.LENGTH_SHORT).show();
                            // Apenas para averiguação:
                            //System.out.println(response.message());
                            assert response.body() != null;
                            listaIds.clear();
                            listaIds.add(" ");
                            listaNomeMontadora.clear();
                            listaNomeMontadora.add(" ");
                            for (Dados user : response.body()) {
                                System.out.println(Integer.toString(user.getmId()));
                                System.out.println(user.getmNome());
                                listaIds.add(Integer.toString(user.getmId()));
                                listaNomeMontadora.add(user.getmNome());
                                System.out.println(listaNomeMontadora);
                            }
                            // Cria um ArrayAdapter para popular o elemento Spinner (menu suspenso) que contém a
                            // lista das montadoras de veículos
                            ArrayAdapter<String> adaptador = new ArrayAdapter<String>(
                                    getApplicationContext(),
                                    layout.simple_spinner_dropdown_item,
                                    listaNomeMontadora);

                            montadora_id.setAdapter(adaptador);
                        }

                        @Override
                        public void onFailure(Call<List<Dados>> call, Throwable t) {
                            Toast.makeText(MainActivity.this, "Falha obtenção dados!", Toast.LENGTH_SHORT).show();
                            Toast.makeText(MainActivity.this, (CharSequence) t.fillInStackTrace(), Toast.LENGTH_SHORT).show();
                            // Apenas para averiguação:
                            //System.out.println("Falha em obter os dados");
                            //System.out.println(t.fillInStackTrace());
                        }
                    });

                }
                else if(itemIdex == 2){
                    PesadosServices service = FonteEndPoints.getRetrofitInstance().create(PesadosServices.class);
                    service.getLista().enqueue(new Callback<List<Dados>>() {
                        @Override
                        public void onResponse(Call<List<Dados>> call, Response<List<Dados>> response) {
                            Toast.makeText(MainActivity.this, "Carregamento OK", Toast.LENGTH_SHORT).show();
                            // Apenas para averiguação:
                            //System.out.println(response.message());
                            assert response.body() != null;
                            listaIds.clear();
                            listaIds.add(" ");
                            listaNomeMontadora.clear();
                            listaNomeMontadora.add(" ");
                            for (Dados user : response.body()) {
                                System.out.println(Integer.toString(user.getmId()));
                                System.out.println(user.getmNome());
                                listaIds.add(Integer.toString(user.getmId()));
                                listaNomeMontadora.add(user.getmNome());
                                System.out.println(listaNomeMontadora);
                            }
                            // Cria um ArrayAdapter para popular o elemento Spinner (menu suspenso) que contém a
                            // lista das montadoras de veículos
                            ArrayAdapter<String> adaptador = new ArrayAdapter<String>(
                                    getApplicationContext(),
                                    layout.simple_spinner_dropdown_item,
                                    listaNomeMontadora);

                            montadora_id.setAdapter(adaptador);
                        }

                        @Override
                        public void onFailure(Call<List<Dados>> call, Throwable t) {
                            Toast.makeText(MainActivity.this, "Falha obtenção dados!", Toast.LENGTH_SHORT).show();
                            Toast.makeText(MainActivity.this, (CharSequence) t.fillInStackTrace(), Toast.LENGTH_SHORT).show();
                            // Apenas para averiguação:
                            //System.out.println("Falha em obter os dados");
                            //System.out.println(t.fillInStackTrace());
                        }
                    });
                }
                else if(itemIdex == 3){
                    AgricolasServices service = FonteEndPoints.getRetrofitInstance().create(AgricolasServices.class);
                    service.getLista().enqueue(new Callback<List<Dados>>() {
                        @Override
                        public void onResponse(Call<List<Dados>> call, Response<List<Dados>> response) {
                            Toast.makeText(MainActivity.this, "Carregamento OK", Toast.LENGTH_SHORT).show();
                            // Apenas para averiguação:
                            //System.out.println(response.message());
                            assert response.body() != null;
                            listaIds.clear();
                            listaIds.add(" ");
                            listaNomeMontadora.clear();
                            listaNomeMontadora.add(" ");
                            for (Dados user : response.body()) {
                                System.out.println(Integer.toString(user.getmId()));
                                System.out.println(user.getmNome());
                                listaIds.add(Integer.toString(user.getmId()));
                                listaNomeMontadora.add(user.getmNome());
                                System.out.println(listaNomeMontadora);
                            }
                            // Cria um ArrayAdapter para popular o elemento Spinner (menu suspenso) que contém a
                            // lista das montadoras de veículos
                            ArrayAdapter<String> adaptador = new ArrayAdapter<String>(
                                    getApplicationContext(),
                                    layout.simple_spinner_dropdown_item,
                                    listaNomeMontadora);

                            montadora_id.setAdapter(adaptador);
                        }

                        @Override
                        public void onFailure(Call<List<Dados>> call, Throwable t) {
                            Toast.makeText(MainActivity.this, "Falha obtenção dados!", Toast.LENGTH_SHORT).show();
                            Toast.makeText(MainActivity.this, (CharSequence) t.fillInStackTrace(), Toast.LENGTH_SHORT).show();
                            // Apenas para averiguação:
                            //System.out.println("Falha em obter os dados");
                            //System.out.println(t.fillInStackTrace());
                        }
                    });
                }
                else if(itemIdex == 4){
                    MotosServices service = FonteEndPoints.getRetrofitInstance().create(MotosServices.class);
                    service.getLista().enqueue(new Callback<List<Dados>>() {
                        @Override
                        public void onResponse(Call<List<Dados>> call, Response<List<Dados>> response) {
                            Toast.makeText(MainActivity.this, "Carregamento OK", Toast.LENGTH_SHORT).show();
                            // Apenas para averiguação:
                            //System.out.println(response.message());
                            assert response.body() != null;
                            listaIds.clear();
                            listaIds.add(" ");
                            listaNomeMontadora.clear();
                            listaNomeMontadora.add(" ");
                            for (Dados user : response.body()) {
                                System.out.println(Integer.toString(user.getmId()));
                                System.out.println(user.getmNome());
                                listaIds.add(Integer.toString(user.getmId()));
                                listaNomeMontadora.add(user.getmNome());
                                System.out.println(listaNomeMontadora);
                            }
                            // Cria um ArrayAdapter para popular o elemento Spinner (menu suspenso) que contém a
                            // lista das montadoras de veículos
                            ArrayAdapter<String> adaptador = new ArrayAdapter<String>(
                                    getApplicationContext(),
                                    layout.simple_spinner_dropdown_item,
                                    listaNomeMontadora);

                            montadora_id.setAdapter(adaptador);
                        }

                        @Override
                        public void onFailure(Call<List<Dados>> call, Throwable t) {
                            Toast.makeText(MainActivity.this, "Falha obtenção dados!", Toast.LENGTH_SHORT).show();
                            Toast.makeText(MainActivity.this, (CharSequence) t.fillInStackTrace(), Toast.LENGTH_SHORT).show();
                            // Apenas para averiguação:
                            //System.out.println("Falha em obter os dados");
                            //System.out.println(t.fillInStackTrace());
                        }
                    });
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // alguma implementação futura
            }
        });

        // Capta o item (montadora) selecionado pelo usuário na lista suspensa populada pelos
        // dados vindos da web, soma ao tipo de veículo selecionado e requisita a lista de veículos
        // junto com sua motorização
        montadora_id.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int itemIdex, long id) {
                if(itemIdex != 0) {
                    // Endereço URL para a obtenção dos dados referentes aos veículos/motorização
                    url = "https://service.tecnomotor.com.br/iRasther/aplicacao?pm.platform=1&pm.version=17&pm.type="+
                            tipo+"&pm.assemblers="+ listaIds.get(itemIdex)+"&pm.pageIndex=0&pm.pageSize=10";
                    extrairTipo = Boolean.FALSE;  // agora, o objetivo é extrair o veículo/motorização
                    nomeMontadora = parent.getItemAtPosition(itemIdex).toString();
                    // Chama a execução da classe responsável por requisitar os dados via web
                    new ExtrairDadosURL().execute(url);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // alguma implementação futura
            }
        });

    }

    // Popula lista dos tipos de veículos (categoria)
    private String PopulaListaTipoMontadoras(String itensListaTipos){

        DadosURL = itensListaTipos;
        int index = 0;
        String[] Straux = new String[40];
        char[] arrayAux;
        arrayAux = DadosURL.toCharArray();

        for(int i=0; i< DadosURL.length(); i++) {
            if (arrayAux[i] == ',')
                index++;
            if (arrayAux[i] != ',' & arrayAux[i] != ' ' & arrayAux[i] != '[' & arrayAux[i] != ']' & arrayAux[i] != '"') {
                Straux[index] = Straux[index] + arrayAux[i];
            }
        }

        tiposMontadoras[0] = " ";

        for(int i=0; i <= index; i++){
            tiposMontadoras[i+1] = Straux[i].substring(4);
        }
        // Apenas para averiguação:
        //System.out.println(tiposMontadoras.toString());

        // Cria um ArrayAdapter para popular o elemento Spinner (menu suspenso) que contém a
        // lista dos tipos de veículos
        ArrayAdapter<String> adaptador = new ArrayAdapter<String>(
                getApplicationContext(),
                layout.simple_spinner_dropdown_item,
                id.text1,
                tiposMontadoras);

        tipoVeiculo_id.setAdapter(adaptador);

        return null;
    }

    // Popula lista dos veículos e motorização em relação à montadora selecionada e a envia junto
    // com outros parâmetros erscolhidos para a outra tela do App
    private String PopulaListaVeiculosMotorizacao(String itens){

        DadosURL = itens;
        int index = 0;
        String[] Straux = new String[11];
        char[] arrayAux;
        arrayAux = DadosURL.toCharArray();

        for (int i=0; i< DadosURL.length()-17; i++) {
            if (arrayAux[i] == 'v' & arrayAux[i+1] == 'e' & arrayAux[i+2] == 'i' & arrayAux[i+3] == 'c'
                    & arrayAux[i+4] == 'u' & arrayAux[i+5] == 'l' & arrayAux[i+6] == 'o' & arrayAux[i+7] == 'M'
                    & arrayAux[i+8] == 'o' & arrayAux[i+9] == 't' & arrayAux[i+10] == 'o' & arrayAux[i+11] == 'r'
                    & arrayAux[i+12] == 'i' & arrayAux[i+13] == 'z' & arrayAux[i+14] == 'a' &
                    arrayAux[i+15] == 'c' & arrayAux[i+16] == 'a' & arrayAux[i+17] == 'o') {
                laco:
                for (int j = (i + 21); j < DadosURL.length(); j++) {
                    Straux[index] = Straux[index] + arrayAux[j];
                    if (arrayAux[j+1] == '"')
                        break laco;
                }
                index++;
            }
        }

        for(int i=0; i < index; i++){
            veiculosMontadoras[i] = Straux[i].substring(4);
        }

        // Apenas para averiguação:
        //System.out.println(veiculosMontadoras.length);

        // Cria instância para passagem de dados para outro activity (outra tela do app)
        // É passado o tipo de veículo, a montadora e a lista dos veículos/motorização pertencentes
        // a mesma. É utilizado o ActivityForResult, ou seja, haverá o retorno de dados por parte
        // da outra tela ou activity.
        Intent intent = new Intent(MainActivity.this, Resultados.class);
        intent.putExtra("montadora", nomeMontadora);
        intent.putExtra("categoria", tipo);
        intent.putExtra("motorizacao", veiculosMontadoras);

        startActivityForResult(intent, CODIGO_REQUISICAO);

        return null;
    }

    // Lida com os dados retornados da outra activity (outra tela do App)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // Checa se o código de requisição bate com o recebido
        if (requestCode == CODIGO_REQUISICAO) {
            // Checa se a requisição retornou com sucesso
            if (resultCode == RESULT_OK) {
                // Capta os dados retornados
                montadoraUltimoResultado_id.setText(data.getStringExtra("nomeMontadora"));
                categoriaUltimoResultado_id.setText(data.getStringExtra("categoria"));
                motorizacaoUltimoResultado_id.setText(data.getStringExtra("veiculoEscolhido"));

                // Salva esse últimos dados de modo que ao reabrir o App os mesmos se apresentarão
                // na tela
                // Cria a instância para a edição dos dados
                SharedPreferences sharedPreferences = getSharedPreferences(ARQUIVO_SALVAR, 0);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                // Edita os dados a serem salvos
                editor.putString("nomeMontadoraSalvo", data.getStringExtra("nomeMontadora"));
                editor.putString("categoriaSalvo", data.getStringExtra("categoria"));
                editor.putString("veiculoEscolhidoSalvo", data.getStringExtra("veiculoEscolhido"));
                // Grava os dados
                editor.apply();

                // Rotina do banco de dados (utilizando SQLite)
                // Salva todas as seleções realizadas pelo usuário
                try {
                    // Abre ou cria o banco de dados
                    bancoDados = openOrCreateDatabase("Motoriza", MODE_PRIVATE, null);
                    // Cria a tabela "historico_Selecao" (caso já não exista)
                    bancoDados.execSQL("CREATE TABLE IF NOT EXISTS historico_Selecao(id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                            "data DATA, tipoVeic VARCHAR, montadora VARCHAR, veicMotor VARCHAR)");
                    // Insere dados na tabela
                    bancoDados.execSQL("INSERT INTO historico_Selecao (data) VALUES(CURRENT_TIMESTAMP)");
                    bancoDados.execSQL("INSERT INTO historico_Selecao (tipoVeic) VALUES('"+data.getStringExtra("categoria")+"')");
                    bancoDados.execSQL("INSERT INTO historico_Selecao (montadora) VALUES('"+data.getStringExtra("nomeMontadora")+"')");
                    bancoDados.execSQL("INSERT INTO historico_Selecao (veicMotor) VALUES('"+data.getStringExtra("veiculoEscolhido")+"')");

                    // Rotina de leitura
                    // Seleciona todas as colunas da tabela
                    Cursor cursor = bancoDados.rawQuery("SELECT * FROM historico_Selecao", null);
                    int indiceColunaId = cursor.getColumnIndex("id");
                    int indiceColunaEfemerides = cursor.getColumnIndex("data");
                    int indiceColunaTipoVeic = cursor.getColumnIndex("tipoVeic");
                    int indiceColunaMontadora = cursor.getColumnIndex("montadora");
                    int indiceColunaVeicMotor = cursor.getColumnIndex("veicMotor");

                    // Apresenta aqui os valores lidos por meio de Log para averiguação (Não é
                    // implementado nesta versão do App qualquer rotina funcional que utilize esses
                    // dados salvos no banco de dados. Isso fica como uma possível implementação futura
                    cursor.moveToFirst();
                    while (cursor != null){
                        Log.i("Log - ", "Data: " + cursor.getString(indiceColunaEfemerides));
                        cursor.moveToNext();
                        Log.i("Log - ", "Tipo de Veículo: "+ cursor.getString(indiceColunaTipoVeic));
                        cursor.moveToNext();
                        Log.i("Log - ", "Montadora: "+cursor.getString(indiceColunaMontadora));
                        cursor.moveToNext();
                        Log.i("Log - ", "Veículo - motorização: "+cursor.getString(indiceColunaVeicMotor));
                        cursor.moveToNext();
                    }

                }catch (Exception erro){
                    erro.printStackTrace();
                }

            }
        }
    }

    // Requisita dados de uma url passada retornado ou a lista dos tipos de veículo ou a lista dos
    // veículos/motorização (de acordo o requisitado)
    public class ExtrairDadosURL extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... params) {
            HttpURLConnection conectaURL = null;
            BufferedReader leitura = null;
            try {
                    URL url = new URL(params[0]);
                    conectaURL = (HttpURLConnection) url.openConnection();
                    conectaURL.setRequestMethod("GET");
                    conectaURL.connect();
                    InputStream inputStream = conectaURL.getInputStream();
                    leitura = new BufferedReader(new InputStreamReader(inputStream));
                    String linha;
                    StringBuffer buffer = new StringBuffer();
                    while((linha = leitura.readLine()) != null) {
                        buffer.append(linha);
                        buffer.append("\n");
                    }
                    return buffer.toString();
            } catch (Exception erro) {
                erro.printStackTrace();
                if (conectaURL != null) {
                    conectaURL.disconnect();
                }
                if (leitura != null) {
                    try {
                        leitura.close();
                    } catch (IOException erro2) {
                        erro2.printStackTrace();
                    }
                }
            }
            return null;
        }
        @Override
        protected void onPostExecute(String dados) {
            // Disponibilização dos dados
            if (extrairTipo){
                String resp = PopulaListaTipoMontadoras(dados);
            }
            else{
                String resp = PopulaListaVeiculosMotorizacao(dados);
            }
        }
    }

}

