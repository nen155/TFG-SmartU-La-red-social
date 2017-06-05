package com.smartu.vistas;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.ImageView;
import android.widget.TextView;

import com.joanzapata.iconify.Iconify;
import com.joanzapata.iconify.fonts.FontAwesomeModule;
import com.ns.developer.tagview.entity.Tag;
import com.ns.developer.tagview.widget.TagCloudLinkView;
import com.smartu.R;
import com.smartu.adaptadores.AdapterRedesSociales;
import com.smartu.modelos.Area;
import com.smartu.modelos.Especialidad;

import com.smartu.modelos.Usuario;



/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FragmentUsuario#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragmentUsuario extends Fragment {


    private static final String ARG_USUARIO = "usuario";

    private Usuario usuario;



    public FragmentUsuario() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param usuario Parameter 1.
     * @return A new instance of fragment FragmentProyecto.
     */
    // TODO: Rename and change types and number of parameters
    public static FragmentUsuario newInstance(Usuario usuario) {
        FragmentUsuario fragment = new FragmentUsuario();
        Bundle args = new Bundle();
        args.putParcelable(ARG_USUARIO, usuario);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            usuario = getArguments().getParcelable(ARG_USUARIO);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //Pongo los iconos para los TextView
        Iconify.with(new FontAwesomeModule());
        View inflatedView =inflater.inflate(R.layout.fragment_usuario, container, false);

        TextView nombreCompleto = (TextView) inflatedView.findViewById(R.id.nombre_completo_usuario);
        TextView status = (TextView) inflatedView.findViewById(R.id.status_usuario);
        TextView web = (TextView) inflatedView.findViewById(R.id.web_usuario);
        TextView biografia = (TextView) inflatedView.findViewById(R.id.biografia_usuario);
        ImageView verficado =(ImageView) inflatedView.findViewById(R.id.verificado);
        TagCloudLinkView areasInteres = (TagCloudLinkView) inflatedView.findViewById(R.id.areas_interes_usuario);
        TagCloudLinkView especialidades = (TagCloudLinkView) inflatedView.findViewById(R.id.especialidades_usuario);
        RecyclerView redesSociales =(RecyclerView) inflatedView.findViewById(R.id.redesSociales);

        //Establezco el recyclerview con las redes sociales
        LinearLayoutManager llm = new LinearLayoutManager(getContext());
        llm.setOrientation(LinearLayoutManager.HORIZONTAL);
        redesSociales.setLayoutManager(llm);
        redesSociales.setAdapter(new AdapterRedesSociales(getContext(),usuario.getMisRedesSociales()));

        //Relleno los campos del usuario
        if(usuario.getApellidos()!=null)
            nombreCompleto.setText(usuario.getNombre()+" "+usuario.getApellidos());
        else
            nombreCompleto.setText(usuario.getNombre());
        status.setText(usuario.getMiStatus().getNombre());

        if(usuario.isVerificado())
            verficado.setVisibility(View.VISIBLE);
        else
            verficado.setVisibility(View.GONE);

        if(usuario.getWeb()!=null && usuario.getWeb().compareTo("")!=0)
            web.setText(usuario.getWeb());
        else
        {
            web.setVisibility(View.GONE);
            inflatedView.findViewById(R.id.text_web).setVisibility(View.GONE);
        }

        if(usuario.getBiografia()!=null && usuario.getBiografia().compareTo("")!=0)
            biografia.setText(usuario.getBiografia());
        else
        {
            biografia.setVisibility(View.GONE);
            inflatedView.findViewById(R.id.text_biografia).setVisibility(View.GONE);
        }
        if(usuario.getMisEspecialidades().size()>0){
        for (Especialidad e : usuario.getMisEspecialidades()) {
            especialidades.add(new Tag(e.getId(), e.getNombre()));
        }
        }else {
            especialidades.setVisibility(View.GONE);
            inflatedView.findViewById(R.id.text_especialidades).setVisibility(View.GONE);
        }
        if(usuario.getMisAreasInteres().size()>0){
            for (Area a : usuario.getMisAreasInteres()) {
                areasInteres.add(new Tag(a.getId(), a.getNombre()));
            }
        }else {
            areasInteres.setVisibility(View.GONE);
            inflatedView.findViewById(R.id.text_areas_interes).setVisibility(View.GONE);
        }

        return inflatedView;
    }

}
