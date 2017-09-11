package appcentralpet.com.newcentralpet.ListExpansivel;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.TextView;

import appcentralpet.com.newcentralpet.R;

public class TextosDuvidas extends AppCompatActivity {

    private TextView texto;
    private TextView titulo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_texto_duvidas);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setTitle("Dúvidas frequentes");

        texto = (TextView) findViewById(R.id.setText);
        titulo = (TextView) findViewById(R.id.titulo);

        Bundle extra = getIntent().getExtras();
        if(extra != null){
            String opcaoescolhida = extra.getString("opcao");
            if(opcaoescolhida.equals("op0")){

            }else if(opcaoescolhida.equals("op2")){
                titulo.setText("Reações das vacinas");
                texto.setText(".....");
            }else if(opcaoescolhida.equals("op3")){
                titulo.setText("Recomendações");
                texto.setText(".............");



            }else if(opcaoescolhida.equals("op7")){
                titulo.setText("Doenças comuns gatos - xxx");
                texto.setText("verme");
            }else if(opcaoescolhida.equals("op8")){
                titulo.setText("dcg - xxx");
                texto.setText("anti");
            }else if(opcaoescolhida.equals("op9")){
                titulo.setText("");
                texto.setText("v4");
            }else if(opcaoescolhida.equals("op10")){
                titulo.setText("");
                texto.setText("rabica");
            }else if(opcaoescolhida.equals("op11")){
                titulo.setText("");
                texto.setText("quadrupla");
            }

            else if(opcaoescolhida.equals("op12")){
                titulo.setText("Doenç 1");
                texto.setText("quadrupla");
            }else if(opcaoescolhida.equals("op13")){
                titulo.setText("");
                texto.setText("quadrupla");
            }

        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){
            case android.R.id.home:
                finish();
                break;
            default:break;
        }
        return true;
    }
}
