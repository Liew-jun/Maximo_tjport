package guide.app.asset;

import psdi.mbo.Mbo;
import psdi.mbo.MboValue;
import psdi.mbo.MboValueAdapter;
import psdi.util.MXException;

import java.rmi.RemoteException;

public class FdlAssetCz extends MboValueAdapter {
    public FdlAssetCz(MboValue mbv) {
        super(mbv);
    }

    @Override
    public void action() throws MXException, RemoteException {
        super.action();
        Mbo mbo = getMboValue().getMbo();
        double yz = getMboValue("YZ").getDouble();          //获取原值
        double udczl = getMboValue("UDCZL").getDouble();    //获取残值率
        mbo.setValue("UDCZ",yz*udczl,11L);
    }
}
