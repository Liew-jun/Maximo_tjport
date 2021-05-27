package guide.app.asset;

import java.rmi.RemoteException;
import psdi.mbo.MAXTableDomain;
import psdi.mbo.MboSetRemote;
import psdi.mbo.MboValue;
import psdi.util.MXException;

public class FldClassParent extends MAXTableDomain
{
    public FldClassParent(MboValue mbv)
    {
        super(mbv);
        setRelationship("UDCLASS", "classid = :classparent");

        setListCriteria("apptype =:apptype and classlevel='1' and active=:yes");

        setErrorMessage("udclass", "NotActiveClassName");

        setLookupKeyMapInOrder(new String[] { "classparent" }, new String[] { "classid" });
    }
    public void action() throws MXException, RemoteException {
        super.action();
        if (getMboValue().isNull())
            return;
        getMboValue("CLASSCHILD").setValueNull();
    }

    public MboSetRemote getList()
            throws MXException, RemoteException
    {
        MboSetRemote msr = super.getList();
        msr.setOrderBy("length(classid),classid");
        return msr;
    }
}