package com.company.audioapp;


import android.content.ComponentName;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import android.os.RemoteException;
import android.support.v4.media.MediaBrowserCompat;
import android.support.v4.media.session.MediaControllerCompat;
import android.support.v4.media.session.MediaSessionCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

public class ListaAudioFragment extends Fragment {

    private MediaBrowserCompat mediaBrowser;

    public ListaAudioFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_lista_audio, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mediaBrowser = new MediaBrowserCompat(requireActivity(), new ComponentName(requireActivity(), MyMediaBrowserService.class),
                new MediaBrowserCompat.ConnectionCallback() {
                    @Override
                    public void onConnected() {

                        MediaSessionCompat.Token token = mediaBrowser.getSessionToken();

                        MediaControllerCompat mediaController = null;
                        try {
                            mediaController = new MediaControllerCompat(requireActivity(), token);
                        } catch (RemoteException e) {
                            e.printStackTrace();
                        }

                        MediaControllerCompat.setMediaController(requireActivity(), mediaController);

                        // Finish building the UI
                        loadSongs();
                    }

                    @Override
                    public void onConnectionSuspended() {}

                    @Override
                    public void onConnectionFailed() {}
                },
                null);

        mediaBrowser.connect();
    }

    private void loadSongs() {
        Log.e("ABCD", "connected");
        mediaBrowser.subscribe(mediaBrowser.getRoot(),
                new MediaBrowserCompat.SubscriptionCallback() {
                    @Override
                    public void onChildrenLoaded(@NonNull String parentId, @NonNull List<MediaBrowserCompat.MediaItem> children) {
                        super.onChildrenLoaded(parentId, children);

                        for(MediaBrowserCompat.MediaItem item : children){
                            Log.e("ABCD", "ITEM = " + item.getDescription().getTitle().toString());
                        }
                    }
                });
    }
}
