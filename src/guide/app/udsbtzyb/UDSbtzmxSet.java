package guide.app.udsbtzyb;

import psdi.mbo.Mbo;
import psdi.mbo.MboServerInterface;
import psdi.mbo.MboSet;
import psdi.mbo.MboSetRemote;
import psdi.util.MXException;

import java.rmi.RemoteException;

public class UDSbtzmxSet extends MboSet implements MboSetRemote {
    public UDSbtzmxSet(MboServerInterface ms) throws RemoteException {
        super(ms);
    }

    @Override
    protected Mbo getMboInstance(MboSet mboSet) throws MXException, RemoteException {
        return new UDSbtzmx(mboSet);
    }
}