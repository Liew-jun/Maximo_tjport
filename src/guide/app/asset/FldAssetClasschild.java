package guide.app.asset;

import psdi.mbo.Mbo;
import psdi.mbo.MboSetRemote;
import psdi.mbo.MboValue;
import psdi.mbo.MboValueAdapter;
import psdi.util.MXException;

import java.rmi.RemoteException;

public class FldAssetClasschild extends MboValueAdapter {
    public FldAssetClasschild(MboValue mbv) {
        super(mbv);
    }

    @Override
    public void action() throws MXException, RemoteException {
        super.action();
        Mbo mbo = getMboValue().getMbo();
        MboSetRemote classSet = mbo.getMboSet("CLASSCHILD");
        if(!classSet.isEmpty() && classSet.count() > 0){
            mbo.setValue("udssfl", classSet.getMbo(0).getString("udssfl"), 11L);
        }
    }
}
