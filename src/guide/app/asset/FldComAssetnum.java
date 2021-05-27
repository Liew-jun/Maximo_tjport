package guide.app.asset;

import java.rmi.RemoteException;

import psdi.mbo.MAXTableDomain;
import psdi.mbo.MboRemote;
import psdi.mbo.MboSetRemote;
import psdi.mbo.MboValue;
import psdi.util.MXException;

public class FldComAssetnum extends MAXTableDomain {
    public FldComAssetnum(MboValue mbv)
            throws RemoteException, MXException {
        super(mbv);
        String attrname = getMboValue().getName();
        setRelationship("ASSET", "siteid = :siteid and assetnum = :" + attrname);
        setKeyMap("ASSET", new String[]{attrname},
                new String[]{"assetnum"});
    }

    public MboSetRemote getList() throws RemoteException, MXException {
        MboRemote mbo = getMboValue().getMbo();
        String sql = "siteid = '" + mbo.getString("siteid") + "'";
        setListCriteria(sql);
        return super.getList();
    }

    public void init() throws MXException, RemoteException {
        super.init();
    }

    public void validate() throws RemoteException, MXException {
        super.validate();
    }

    public void action() throws RemoteException, MXException {
        super.action();

        MboRemote mbo = getMboValue().getMbo();
        mbo.setValueNull("classparent", 2L);
        mbo.setValueNull("classchild", 2L);

        MboSetRemote relSet = mbo.getMboSet("ASSET");
        if ((!relSet.isEmpty()) && (relSet.count() > 0)) {
            mbo.setValue("classparent", relSet.getMbo(0).getString("classparent"), 2L);
            mbo.setValue("classchild", relSet.getMbo(0).getString("classchild"), 2L);
        }
    }
}