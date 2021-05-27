package guide.app.udsbtzyb;

import java.rmi.RemoteException;
import psdi.mbo.Mbo;
import psdi.mbo.MboServerInterface;
import psdi.mbo.MboSet;
import psdi.mbo.MboSetRemote;
import psdi.util.MXException;

public class UDsbtzybSet extends MboSet
        implements MboSetRemote
{
    public UDsbtzybSet(MboServerInterface ms)
            throws MXException, RemoteException
    {
        super(ms);
    }

    protected Mbo getMboInstance(MboSet ms)
            throws MXException, RemoteException
    {
        return new UDsbtzyb(ms);
    }
}