package psdi.mbo.custapp;

import java.rmi.RemoteException;
import psdi.mbo.Mbo;
import psdi.mbo.MboRemote;
import psdi.mbo.MboSet;
import psdi.mbo.MboValueInfo;
import psdi.util.MXException;

public class CustomMbo extends Mbo
        implements CustomMboRemote
{
    static final long serialVersionUID = 2616524468203330782L;

    public CustomMbo(MboSet ms)
            throws RemoteException
    {
        super(ms);
    }

    public MboRemote duplicate()
            throws MXException, RemoteException
    {
        return copy();
    }

    protected boolean skipCopyField(MboValueInfo mvi)
            throws RemoteException, MXException
    {
        if ((!mvi.getAttributeName().equalsIgnoreCase("SITEID")) && (!mvi.getAttributeName().equalsIgnoreCase("ORGID")) && (mvi.isKey()))
        {
            return true;
        }

        return false;
    }
}