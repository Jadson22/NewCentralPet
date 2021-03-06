package appcentralpet.com.newcentralpet.Vacinas;


import android.app.Activity;
import android.app.AlarmManager;
import android.app.Dialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import java.io.Serializable;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import appcentralpet.com.newcentralpet.BancoMeusPets.Cadastro;
import appcentralpet.com.newcentralpet.NavigationDrawer;
import appcentralpet.com.newcentralpet.R;
import br.com.jansenfelipe.androidmask.MaskEditTextChangedListener;

/**
 * A simple {@link Fragment} subclass.
 */
public class Vacinas extends Fragment implements Serializable {

    public static SQLiteHelperVacinas sqLiteHelperVacinas;
    private FloatingActionButton indicações;
    EditText edtRetorno, nomepet;
    AutoCompleteTextView edtVacina;
    ListView listaVacinas;
    ArrayList<Vacina> list;
    VacinaListAdapter adapter = null;
    ImageView semvacina;
    ListView reminderListView;
    EditText editarSData;


    public Vacinas() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_vacinas, container, false);


        sqLiteHelperVacinas = new SQLiteHelperVacinas(getContext(), "PetVacina.sqlite", null, 1);
        sqLiteHelperVacinas.queryData("CREATE TABLE IF NOT EXISTS VACINAS " +
                "(Id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "nome VARCHAR, " +
                "vacina VARCHAR, " +
                "pdata VARCHAR, " +
                "sdata VARCHAR)");

        listaVacinas = (ListView) view.findViewById(R.id.list_vacinas);
        list = new ArrayList<>();
        adapter = new VacinaListAdapter(getContext(), R.layout.vacina_itens, list);
        listaVacinas.setAdapter(adapter);
        listaVacinas.setEmptyView(semvacina);

        Cursor cursor = sqLiteHelperVacinas.getData("SELECT * FROM VACINAS");
        list.clear();
        while (cursor.moveToNext()) {
            int id = cursor.getInt(0);
            String nomePet = cursor.getString(1);
            String nomeVacina = cursor.getString(2);
            String sData = cursor.getString(3);

            list.add(new Vacina(id, nomePet, nomeVacina, sData));
        }
        adapter.notifyDataSetChanged();

        listaVacinas.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                CharSequence[] itens = {"Editar", "Excluir"};
                AlertDialog.Builder dialog = new AlertDialog.Builder(getContext());

                dialog.setTitle("Alterar");
                dialog.setItems(itens, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int item) {
                        if (item == 0) {
                            //editar
                            Cursor cur = sqLiteHelperVacinas.getData("SELECT id FROM VACINAS");
                            ArrayList<Integer> arrayId = new ArrayList<Integer>();
                            while (cur.moveToNext()) {
                                arrayId.add(cur.getInt(0));
                            }
                            showDialogUpdate(getActivity(), arrayId.get(position));

                        } else {
                            //apagar
                            Cursor cur = sqLiteHelperVacinas.getData("SELECT id FROM VACINAS");
                            ArrayList<Integer> arrayId = new ArrayList<Integer>();
                            while (cur.moveToNext()) {
                                arrayId.add(cur.getInt(0));
                            }
                            showDialogDelete(arrayId.get(position));
                        }
                    }
                });
                dialog.show();
                return true;
            }
        });

        indicações = (FloatingActionButton) view.findViewById(R.id.fab2);

        indicações.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(), "Parcerias em breve!", Toast.LENGTH_SHORT).show();
            }
        });

        return view;
    }

    private void showDialogUpdate(Activity activity, final int position) {

        final Dialog dialogEdit = new Dialog(activity);
        dialogEdit.setContentView(R.layout.add_vacinas);
        dialogEdit.setTitle("Editar");

        final AutoCompleteTextView editarVacina = (AutoCompleteTextView) dialogEdit.findViewById(R.id.edtVacina);
        MaskEditTextChangedListener masketretorno = new MaskEditTextChangedListener("(##)#####-####", editarVacina);
        editarVacina.addTextChangedListener(masketretorno);

        final EditText editarSData = (EditText) dialogEdit.findViewById(R.id.edtRetorno);

        Button btnEditar = (Button) dialogEdit.findViewById(R.id.btnAdd);

        final EditText editarNomePet = (EditText) dialogEdit.findViewById(R.id.nomePet);


        int width = (int) (getActivity().getResources().getDisplayMetrics().widthPixels * 0.95);
        int height = (int) (getActivity().getResources().getDisplayMetrics().heightPixels * 0.7);
        dialogEdit.getWindow().setLayout(width, height);
        dialogEdit.show();

        btnEditar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    if ((editarNomePet.getText().toString().length() == 0) || (editarVacina.getText().toString().length() == 0)) {
                        if (editarNomePet.getText().toString().length() == 0) {
                            editarNomePet.setError("Preencha um nome");
                        } else {
                            editarVacina.setError("Preencha o contato");
                        }
                    } else {
                        sqLiteHelperVacinas.updateData(
                                editarNomePet.getText().toString().trim(),
                                editarVacina.getText().toString().trim(),
                                editarSData.getText().toString().trim(),
                                position
                        );
                    }
                    dialogEdit.dismiss();
                    Toast.makeText(getContext(), "Editado com sucesso", Toast.LENGTH_SHORT).show();

                } catch (Exception error) {
                    Log.e("Update erro: ", error.getMessage());
                }
                atualizarListView();
            }
        });

    }

    private void showDialogDelete(final int idVacina) {
        final AlertDialog.Builder dialogDelete = new AlertDialog.Builder(getContext());

        dialogDelete.setTitle("Atenção!");
        dialogDelete.setMessage("Apagar contato de emergência?");
        dialogDelete.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                try {
                    sqLiteHelperVacinas.deleteData(idVacina);
                    Toast.makeText(getContext(), "Excluido", Toast.LENGTH_SHORT).show();
                } catch (Exception e) {
                    Log.e("error", e.getMessage());
                }
                atualizarListView();
            }
        });

        dialogDelete.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        dialogDelete.show();
    }

    private void ShowDialogAdd() {

        final Dialog dialog = new Dialog(getActivity());
        dialog.setContentView(R.layout.add_vacinas);
        dialog.setTitle("Adicionar");


        edtVacina = (AutoCompleteTextView) dialog.findViewById(R.id.edtVacina);
        MaskEditTextChangedListener masketretorno = new MaskEditTextChangedListener("(##)#####-####", edtVacina);
        edtVacina.addTextChangedListener(masketretorno);

        edtRetorno = (EditText) dialog.findViewById(R.id.edtRetorno);

        nomepet = (EditText) dialog.findViewById(R.id.nomePet);

        Button btnAdd = (Button) dialog.findViewById(R.id.btnAdd);
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                adicionarVacina();
                dialog.dismiss();

            }


        });

        int width = (int) (getActivity().getResources().getDisplayMetrics().widthPixels * 0.95);
        int height = (int) (getActivity().getResources().getDisplayMetrics().heightPixels * 0.7);
        dialog.getWindow().setLayout(width, height);
        dialog.show();
    }

    private void adicionarVacina() {

        try {

            if ((nomepet.getText().toString().length() == 0) || (edtVacina.getText().toString().length() == 0)) {
                if (nomepet.getText().toString().length() == 0) {
                    nomepet.setError("Preencha um nome");
                } else {
                    edtVacina.setError("Selecione um contato");
                }
            } else {

                sqLiteHelperVacinas.insertData(
                        nomepet.getText().toString().trim(),
                        edtVacina.getText().toString().trim(),
                        edtRetorno.getText().toString().trim()
                );
                Toast.makeText(getContext(), "adicionado", Toast.LENGTH_SHORT).show();

                nomepet.setText("");
                edtVacina.setText("");
                edtRetorno.setText("");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        atualizarListView();
    }

    private void atualizarListView() {

        Cursor cursor = sqLiteHelperVacinas.getData("SELECT * FROM VACINAS");
        list.clear();
        while (cursor.moveToNext()) {
            int id = cursor.getInt(0);
            String nomePet = cursor.getString(1);
            String nomeVacina = cursor.getString(2);
            String sData = cursor.getString(3);

            list.add(new Vacina(id, nomePet, nomeVacina, sData));
        }
        adapter.notifyDataSetChanged();

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_meuspets, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        MenuItem item = menu.findItem(R.id.addic);
        item.setVisible(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.addic) {
            ShowDialogAdd();
        }
        return super.onOptionsItemSelected(item);
    }


}
