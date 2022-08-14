package joo.jae.wan.main;

import android.content.Context;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import joo.jae.wan.R;
import kotlin.jvm.internal.Intrinsics;

public class FragMap extends Fragment {
    private View view;

    // flash
    Context ct;
    private boolean powerState;
    public final boolean getPowerState() {
        return this.powerState;
    }
    public final void setPowerState(boolean tmp_powerState) {
        this.powerState = tmp_powerState;
    }
    // flash

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.frag_map, container, false);

        // flash button control
        View tmp_btn = view.findViewById(R.id.imageButton);
        Intrinsics.checkNotNullExpressionValue(tmp_btn, "findViewById(R.id.imageButton)");
        final ImageButton btn = (ImageButton)tmp_btn;
        btn.setOnClickListener((new View.OnClickListener() {
            public void onClick(View it) {
                if (FragMap.this.getPowerState()) {
                    FragMap.this.setPowerState(false);
                    btn.setImageResource(R.drawable.flashlight_off);
                    try {
                        FragMap.this.controlFlash(FragMap.this.getPowerState());
                    } catch (CameraAccessException e) {
                        e.printStackTrace();
                    }
                } else {
                    FragMap.this.setPowerState(true);
                    btn.setImageResource(R.drawable.flashlight_on);
                    try {
                        FragMap.this.controlFlash(FragMap.this.getPowerState());
                    } catch (CameraAccessException e) {
                        e.printStackTrace();
                    }
                }

            }
        }));
        // flash button control

        return view;
    }

    // flash control
    public final void controlFlash(boolean mode) throws CameraAccessException {
        Object tmp_cameraM = getActivity().getSystemService(getContext().CAMERA_SERVICE);
        if (tmp_cameraM == null) {
            throw new NullPointerException("null cannot be cast to non-null type android.hardware.camera2.CameraManager");
        } else {
            CameraManager cameraM = (CameraManager)tmp_cameraM;
            String cameraListId = cameraM.getCameraIdList()[0];

            try {
                cameraM.setTorchMode(cameraListId, mode);
            } catch (Exception e) {
                Toast toast = Toast.makeText(getActivity().getApplicationContext(), "Camera Flash Error", Toast.LENGTH_SHORT);
                toast.show();
            }

        }
    }
    // flash control


}