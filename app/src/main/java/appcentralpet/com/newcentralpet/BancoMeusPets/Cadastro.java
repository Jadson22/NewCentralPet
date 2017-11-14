package appcentralpet.com.newcentralpet.BancoMeusPets;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RadioGroup;

import com.bumptech.glide.Glide;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.Serializable;

import appcentralpet.com.newcentralpet.NavigationDrawer;
import appcentralpet.com.newcentralpet.R;
import br.com.jansenfelipe.androidmask.MaskEditTextChangedListener;

/**
 * A simple {@link Fragment} subclass.
 */
public class Cadastro extends Fragment implements Serializable{


    EditText edtName, edtIdade;
    AutoCompleteTextView edtRaca;
    RadioGroup radioGroupSexo, radioGroupTipo;
    ImageButton imageView;


    final int REQUEST_CODE_GALLERY = 999;
    final int REQUEST_CODE_CAMERA = 200;

    public static final String[] RACAS = new String[]{"Afghan Hound", "Airedale Terrier", "Akita", "American Staffordshire Terrier", "Boiadeiro Australiano",
            "Basenji", "Basset Hound", "Beagle", "Bernese Mountain Dog", "Bichon Frisé", "Bloodhound", "Border Collie", "Borzoi", "Boston Terrier",
            "Boxer", "Buldogue Francês", "Buldogue Inglês", "Bull Terrier", "Cane Corso", "Cão de Crista Chinês", "Cavalier King Charles Spaniel",
            "Chihuahua", "Chow Chow", "Cocker Spaniel Americano", "Cocker Spaniel Inglês", "Collie", "Dachshund (Teckel)", "Dálmata", "Doberman",
            "Dogo Argentino", "Dogue Alemão", "Dogue de Bordeaux", "Fila Brasileiro", "Fox Paulistinha", "Golden Retriever", "Greyhound", "Griffon de Bruxelas",
            "Griffon de Bruxelas", "Husky Siberiano", "Jack Russell Terrier", " kuvasz", "Kuvasz", "Labrador", "Leão da Rodésia – Rhodesian Ridgeback", "Lhasa Apso",
            "Lulu da Pomerânia Spitaz Alemão", "Malamute do Alasca", "Maltês", "Mastiff", "Old English Sheepdog", "Papillon", "Pastor Alemão (Capa Preta)", "Pastor Australiano",
            "Pastor Belga", "Pastor Branco Suíço (Pastor Canadense)", "Pastor de Shetland", "Pastor Maremano Abruzês", "Pequinês", "Pinscher", "Pit Bull", "Pointer Inglês", "Poodle",
            "Pug", "Rottweiler", "Samoieda", "São Bernardo", "Schnauzer Miniatura", "Setter Irlandês", "Shar Pei", "Shiba Inu", "Shih Tzu", "Spitz Japonês", "Staffordshire Bull Terrier",
            "(SRD) Vira Lata", "Weimaraner", "Welsh Corgi Pembroke", "West Highland White Terrier", "Whippet", "Yorkshire Terrier",

            "Persa", "Siamês", "Himalaia", "Maine Coon", "Angorá", "Siberiano", "Sphynx", "Burmese", "Ragdoll", "British Shorthair", "Exótico",
            "Munchkin", "Burmese"};

    public Cadastro() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_cadastro, container, false);

        edtName = (EditText) view.findViewById(R.id.editartName);
        edtRaca = (AutoCompleteTextView) view.findViewById(R.id.editarRaca);
        ArrayAdapter<String> array = new ArrayAdapter<String>(getContext(), android.R.layout.simple_dropdown_item_1line, RACAS);
        edtRaca.setAdapter(array);

        edtIdade = (EditText) view.findViewById(R.id.editarIdade);
        MaskEditTextChangedListener maskedtIdade = new MaskEditTextChangedListener("##", edtIdade);
        edtIdade.addTextChangedListener(maskedtIdade);

        radioGroupSexo = (RadioGroup) view.findViewById(R.id.radioSexo);
        radioGroupTipo = (RadioGroup) view.findViewById(R.id.radioTipo);

        imageView = (ImageButton) view.findViewById(R.id.imageView);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CharSequence[] itens = {"Tirar foto", "Escolher da galeria"};
                AlertDialog.Builder dialog = new AlertDialog.Builder(getContext());

                dialog.setTitle("Escolher imagem");
                dialog.setItems(itens, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int item) {
                        if(item == 0 ){
                            //tirar foto
                            abrircamera();
                        }else{
                            //galeria
                            escolherImg();
                        }
                    }
                });
                dialog.show();
            }
        });

        return view;
    }

    private void abrircamera() {
        requestPermissions(
                new String[]{Manifest.permission.CAMERA},
                REQUEST_CODE_CAMERA);
    }

    public void escolherImg(){
        requestPermissions(
                new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE},
                REQUEST_CODE_GALLERY
        );
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(requestCode == REQUEST_CODE_GALLERY){
            if(grantResults.length >0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent, REQUEST_CODE_GALLERY);
            } else {
                Snackbar snackbar = Snackbar.make(getView(), "Você não tem permissão para acessar os arquivos!", Snackbar.LENGTH_SHORT);
                snackbar.show();
            }
            return;
        }
        else if(requestCode == REQUEST_CODE_CAMERA){
            if(grantResults.length >0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent, REQUEST_CODE_CAMERA);
            } else {
                Snackbar snackbar = Snackbar.make(getView(), "Você não tem permissão para acessar os arquivos!", Snackbar.LENGTH_SHORT);
                snackbar.show();
            }
            return;
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == REQUEST_CODE_GALLERY && resultCode == getActivity().RESULT_OK && data != null){
            Uri uri = data.getData();
                Glide.with(this)
                .load(uri)
                .asBitmap()
                .into(imageView);

        }  else if(requestCode == REQUEST_CODE_CAMERA && resultCode == getActivity().RESULT_OK && data!= null){
            Uri uir = data.getData();
            Glide.with(this)
                    .load(uir)
                    .asBitmap()
                    .into(imageView);
            /* CropImage.activity(uir)
                    .setAspectRatio(1, 1)
                    .start(getContext(), this);

            if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
                CropImage.ActivityResult result = CropImage.getActivityResult(data);
                if (resultCode == getActivity().RESULT_OK) {
                    Uri resultUri = result.getUri();
                        Glide.with(this)
                                .load(resultUri)
                                .asBitmap()
                                .into(imageView);

                } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                    Exception error = result.getError();
                }
            }
            */
        }

        super.onActivityResult(requestCode, resultCode, data);

    }

    NavigationDrawer nv = new NavigationDrawer();

    public void adicionar() {

        String sexo = null;
        switch (radioGroupSexo.getCheckedRadioButtonId()){
            case R.id.rbMacho:
                sexo = "macho";
                break;
            case R.id.rbFemea:
                sexo = "femea";
        }

        String tipo = null;
        switch (radioGroupTipo.getCheckedRadioButtonId()){
            case R.id.rbCao:
                tipo = "cao";
                break;
            case R.id.rbGato:
                tipo = "gato";
        }

        try {
            if((edtName.getText().toString().length() == 0) || (edtRaca.getText().toString().length() == 0)){
                if(edtName.getText().toString().length() == 0) {
                    edtName.setError("Digite um nome");
                }else {
                    edtRaca.setError("Digite uma raça");
                }
            }else {

                nv.sqLiteHelper.insertData(
                        edtName.getText().toString().trim(),
                        sexo.trim(),
                        edtRaca.getText().toString().trim(),
                        tipo.trim(),
                        edtIdade.getText().toString().trim(),
                        imageViewToByte(imageView)
                );
                Snackbar snackbar = Snackbar.make(getView(), "Novo pet salvo!", Snackbar.LENGTH_SHORT);
                snackbar.show();
                edtName.setText("");
                edtRaca.setText("");
                edtIdade.setText("");
                imageView.setImageResource(R.drawable.novaphoto);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static byte[] imageViewToByte(ImageView image) {
        Bitmap bitmap = ((BitmapDrawable)image.getDrawable()).getBitmap();
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        byte[] byteArray = stream.toByteArray();
        return byteArray;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_cadstromeuspets, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        MenuItem item = menu.findItem(R.id.adicionar);
        item.setVisible(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        //noinspection SimplifiableIfStatement
        if (id == R.id.adicionar) {
            adicionar();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
