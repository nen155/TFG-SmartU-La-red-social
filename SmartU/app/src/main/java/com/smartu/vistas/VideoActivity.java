package com.smartu.vistas;

import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.google.android.exoplayer2.C;
import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.Format;
import com.google.android.exoplayer2.LoadControl;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.extractor.ExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.MergingMediaSource;
import com.google.android.exoplayer2.source.SingleSampleMediaSource;
import com.google.android.exoplayer2.trackselection.AdaptiveVideoTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelection;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.BandwidthMeter;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.MimeTypes;
import com.google.android.exoplayer2.util.Util;
import com.smartu.R;
import com.smartu.modelos.Multimedia;
import com.smartu.utilidades.ConsultasBBDD;

import java.util.Locale;

public class VideoActivity extends AppCompatActivity {
    private SimpleExoPlayer player;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video);
        Bundle bundle = getIntent().getExtras();
        if(bundle!=null && bundle.containsKey("multimedia")) {

            Multimedia multimedia = bundle.getParcelable("multimedia");
            SimpleExoPlayerView videoView = (SimpleExoPlayerView) findViewById(R.id.video);

            // 1. Crea un slector de pista por defecto
            BandwidthMeter bandwidthMeter = new DefaultBandwidthMeter();
            TrackSelection.Factory videoTrackSelectionFactory = new AdaptiveVideoTrackSelection.Factory(bandwidthMeter);
            TrackSelector trackSelector = new DefaultTrackSelector(videoTrackSelectionFactory);

            // 2. Creo un control para el player
            LoadControl loadControl = new DefaultLoadControl();

            // 3. Creo el player con los componentes anteriores
            player = ExoPlayerFactory.newSimpleInstance(getApplicationContext(), trackSelector, loadControl);

            //Obtengo la URL completa del archivo de video
            Uri uri = Uri.parse(ConsultasBBDD.server + ConsultasBBDD.imagenes +  multimedia.getUrl());

            //A la vista le establezco el reproductor que va a usar
            videoView.setPlayer(player);

            // Procuce que se instancie el DataSource a través de los datos del video que ha sido cargado.
            DataSource.Factory dataSourceFactory = new DefaultDataSourceFactory(getApplicationContext(), Util.getUserAgent(getApplicationContext(), "SmartU"));
            // Produce un extractor que parsea el video.
            ExtractorsFactory extractorsFactory = new DefaultExtractorsFactory();
            // Construyo un MediaSource con el formato y los datos del video para asignarselo al reproductor.
            MediaSource videoSource = new ExtractorMediaSource(uri, dataSourceFactory, extractorsFactory, null, null);
            // If has subtitles load video with subtitles
            if (multimedia.getUrlSubtitulos() != null && multimedia.getUrlSubtitulos().compareTo("") != 0) {
                //Obtengo la URL completa para los subtitulos del video
                Uri subtitulosUri = Uri.parse(ConsultasBBDD.server + ConsultasBBDD.imagenes +  multimedia.getUrlSubtitulos());
                //Asigno el formato de los subtitulos
                Format textFormat = Format.createTextSampleFormat(null, MimeTypes.TEXT_VTT,
                        null, Format.NO_VALUE, Format.NO_VALUE, Locale.getDefault().getLanguage(), null);
                //Creo un MediaSource para los subtitulos para añadirselos al video
                MediaSource subtitleSource = new SingleSampleMediaSource(subtitulosUri, dataSourceFactory, textFormat, C.TIME_UNSET);
                // Mezcla el video con los subtitulos
                MergingMediaSource mergedSource = new MergingMediaSource(videoSource, subtitleSource);
                // Preparo el reproductor con los subtitulos y el video
                player.prepare(mergedSource);
            } else // Si no tiene subtitulos preparo solo el video
                player.prepare(videoSource);
        }
    }

    @Override
    protected void onStop() {
        if(player!=null)
            player.stop();
        super.onStop();
    }

    @Override
    public void onBackPressed() {
        if(player!=null)
            player.stop();
        super.onBackPressed();

    }

    @Override
    protected void onPause() {
        if(player!=null)
            player.stop();
        super.onPause();

    }
}
