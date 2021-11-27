package cne.heba.sie.myUIX;

import android.content.Context;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class cargando {

    public SweetAlertDialog carga(Context context, String titul){

        SweetAlertDialog cargaLog;

        cargaLog = new SweetAlertDialog(context,SweetAlertDialog.PROGRESS_TYPE);
        cargaLog.setCancelable(false);
        cargaLog.setTitle(titul);
        cargaLog.show();

        return cargaLog;

    }

    public void dismo(SweetAlertDialog a){
        a.dismissWithAnimation();
    }

}
