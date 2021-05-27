package guide.webclient.beans.com;

import psdi.mbo.MboRemote;
import psdi.mbo.MboSetRemote;
import psdi.util.MXApplicationException;
import psdi.util.MXException;
import psdi.util.MXFormat;
import psdi.webclient.system.beans.AppBean;
import psdi.webclient.system.beans.DataBean;

import java.rmi.RemoteException;

public class UDFourCostExeAppBean extends AppBean {

    public int submit() throws RemoteException, MXException{
        MboRemote mbo = app.getAppBean().getMbo();
        mbo.setValue("STATUS", "已提报", 11L);
        app.getAppBean().save();
        return 1;
    }

    public int reject() throws RemoteException, MXException{
        MboRemote mbo = app.getAppBean().getMbo();
        mbo.setValue("STATUS", "待修订", 11L);
        app.getAppBean().save();
        return 1;


    }





}
