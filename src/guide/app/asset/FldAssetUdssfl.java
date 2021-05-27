package guide.app.asset;

import psdi.mbo.MAXTableDomain;
import psdi.mbo.MboRemote;
import psdi.mbo.MboSetRemote;
import psdi.mbo.MboValue;
import psdi.util.MXException;

import java.rmi.RemoteException;

public class FldAssetUdssfl extends  MAXTableDomain{

    public FldAssetUdssfl(MboValue mbv) {
        super(mbv);
        String thisAttr = getMboValue().getAttributeName();                 //获取当前绑定属性名
        setRelationship("PERSON", "personid=:" + thisAttr);
        setListCriteria("status in (select value from synonymdomain where maxvalue='ACTIVE' and domainid='PERSONSTATUS')");
        setErrorMessage("person", "InvalidPerson");
    }
}
