package guide.app.asset;

import java.rmi.RemoteException;
import psdi.mbo.Mbo;
import psdi.mbo.MboValue;
import psdi.mbo.MboValueAdapter;
import psdi.util.MXException;

public class FIdAssetUdyzje extends MboValueAdapter
{
    public FIdAssetUdyzje(MboValue mbv)
            throws MXException
    {
        super(mbv);
    }

//    public void action()
//            throws MXException, RemoteException
//    {
//        super.action();
//        Mbo mbo = getMboValue().getMbo();
//        double udyzjl = getMboValue("UDYZJL").getDouble();
//
//        double yz = getMboValue("YZ").getDouble();
//
//        mbo.setValue("UDYZJE", udyzjl * yz, 11L);
//    }
}