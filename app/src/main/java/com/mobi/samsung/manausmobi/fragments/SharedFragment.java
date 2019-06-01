package com.mobi.samsung.manausmobi.fragments;


import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.mobi.samsung.manausmobi.R;
import com.mobi.samsung.manausmobi.listeners.OnSharedListener;
import com.mobi.samsung.manausmobi.models.Shared;
import com.mobi.samsung.manausmobi.models.SharedIntensity;
import com.mobi.samsung.manausmobi.models.SharedType;

/**
 * A simple {@link Fragment} subclass.
 */
public class SharedFragment extends android.app.Fragment {


    private final Shared shared = new Shared();
    private OnSharedListener mSharedListener;

    private RelativeLayout relLayOption;
    private RelativeLayout relLayTraffic;
    private RelativeLayout relLayWarning;
    private RelativeLayout relLaySend;


    public SharedFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_sharing, container, false);
        relLayOption = view.findViewById(R.id.linlay_option);
        relLayTraffic = view.findViewById(R.id.linlay_traffic);
        relLayWarning = view.findViewById(R.id.linlay_warning);
        relLaySend = view.findViewById(R.id.linlay_send);
        mSharedListener.screenState(false);

        ImageButton btnTraffic = view.findViewById(R.id.btn_engarrafamento);
        btnTraffic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                shared.type = SharedType.Traffic;
                setVisibilityForOptionTraffic();
                mSharedListener.screenState(true);

            }
        });

        ImageButton btnWarning = view.findViewById(R.id.btn_perigo);
        btnWarning.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                shared.type = SharedType.Warning;
                setVisibilityForOptionWarning();
                mSharedListener.screenState(true);
            }
        });

        ImageButton btnModerate = view.findViewById(R.id.btn_moderado);
        btnModerate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                shared.intensity = SharedIntensity.Moderate;

                setVisibilityToSend();
            }
        });

        ImageButton btnIntense = view.findViewById(R.id.btn_intenso);
        btnIntense.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                shared.intensity = SharedIntensity.Intense;
                setVisibilityToSend();
            }
        });

        ImageButton btnStopped = view.findViewById(R.id.btn_parado);
        btnStopped.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                shared.intensity = SharedIntensity.Stopped;
                setVisibilityToSend();
            }
        });

        ImageButton btnInRoute = view.findViewById(R.id.btn_emrota);
        btnInRoute.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                shared.intensity = SharedIntensity.InRoute;
                setVisibilityToSend();
            }
        });

        ImageButton btnCoasting = view.findViewById(R.id.btn_acostamento);
        btnCoasting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                shared.intensity = SharedIntensity.Coasting;
                setVisibilityToSend();
            }
        });

        ImageButton btnClimate = view.findViewById(R.id.btn_clima);
        btnClimate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                shared.intensity = SharedIntensity.Climate;
                setVisibilityToSend();
            }
        });

        ImageButton btnCamera = view.findViewById(R.id.btn_camera);
        btnCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mSharedListener.getCamera();
            }
        });

        ImageButton btnSave = view.findViewById(R.id.btn_enviar);
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ConnectivityManager cm = (ConnectivityManager) view.getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo netInfo = cm.getActiveNetworkInfo();
                if ((netInfo != null) && (netInfo.isConnectedOrConnecting()) && (netInfo.isAvailable())) {
                    mSharedListener.saveShared(shared);
                    setVisibilityFromSend();
                } else {
                    Toast.makeText(view.getContext(), getString(R.string.erroDeConexao), Toast.LENGTH_SHORT).show();
                }
            }
        });
        return view;
    }

    public void setImage(String encoded) {
        shared.image = encoded;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mSharedListener = (OnSharedListener) activity;
    }

    private void setVisibilityForOptionTraffic() {
        relLayOption.setVisibility(View.GONE);
        relLayTraffic.setVisibility(View.VISIBLE);
        relLayWarning.setVisibility(View.GONE);
        relLaySend.setVisibility(View.GONE);
    }

    private void setVisibilityForOptionWarning() {
        relLayOption.setVisibility(View.GONE);
        relLayTraffic.setVisibility(View.GONE);
        relLayWarning.setVisibility(View.VISIBLE);
        relLaySend.setVisibility(View.GONE);

    }

    private void setVisibilityToSend() {
        relLayOption.setVisibility(View.GONE);
        relLayTraffic.setVisibility(View.GONE);
        relLayWarning.setVisibility(View.GONE);
        relLaySend.setVisibility(View.VISIBLE);

    }

    private void setVisibilityFromSend() {
        relLayOption.setVisibility(View.GONE);
        relLayTraffic.setVisibility(View.GONE);
        relLayWarning.setVisibility(View.GONE);
        relLaySend.setVisibility(View.GONE);

    }
}