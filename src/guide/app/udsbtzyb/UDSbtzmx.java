package guide.app.udsbtzyb;

import psdi.mbo.Mbo;
import psdi.mbo.MboRemote;
import psdi.mbo.MboSet;
import psdi.util.MXException;

import java.rmi.RemoteException;

public class UDSbtzmx extends Mbo implements MboRemote {

    public UDSbtzmx(MboSet ms) throws RemoteException {
        super(ms);
    }

    public void add() throws MXException, RemoteException {
        super.add();
        int linenum = (int)getThisMboSet().max("linenum") + 1;
        setValue("LINENUM", linenum, 2L);
        MboRemote owner = getOwner();
        if (owner != null)
            if ("UDSBTZYB".equalsIgnoreCase(owner.getName()))
                setValue("UDSBTZYBID", owner.getUniqueIDValue(), 11L);
    }

}
