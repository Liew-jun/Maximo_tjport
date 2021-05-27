package guide.app.asset;

import psdi.mbo.Mbo;
import psdi.mbo.MboValue;
import psdi.mbo.MboValueAdapter;
import psdi.util.MXException;

import java.rmi.RemoteException;

public class FldAssetJz extends MboValueAdapter {
    public FldAssetJz(MboValue mbv) {
        super(mbv);
    }

    @Override
    public void action() throws MXException, RemoteException {
        super.action();
        Mbo mbo = getMboValue().getMbo();
        double yz = this.getMboValue("YZ").getDouble();
        double udyzjje = this.getMboValue("UDYZJJE").getDouble();
        double jz = yz -udyzjje;
        mbo.setValue("JZ",jz,11L);
    }
}