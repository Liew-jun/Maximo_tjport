package guide.app.asset;

import guide.util.Tools;
import java.rmi.RemoteException;
import psdi.app.asset.Asset;
import psdi.mbo.MboRemote;
import psdi.mbo.MboSet;
import psdi.security.UserInfo;
import psdi.util.MXException;

public class CustAsset extends Asset
        implements CustAssetRemote
{
    public CustAsset(MboSet ms)
            throws MXException, RemoteException
    {
        super(ms);
    }

    public void init() throws MXException {
        super.init();
        if (toBeAdded())
            return;
        try {
            String wfstatus = getString("wfstatus");
            String[] allStrings = { "classparent", "classchild", "persongroup" };
            if ("已关闭".equals(wfstatus)) {
                setFieldFlag(allStrings, 7L, true);
                String apptype = getString("apptype");
                if ((apptype != null) && (apptype.equals("RJTZ"))) {
                    String[] rjStrings = { "DESCRIPTION", "GRDATE", "SYQR", "SBZT", "CQDW", "SL", "YHXM", "YZ", "SJDW", "YHFS", "SGDW", "JZNY", "SZDZ", "HDFS", "QZH", "SYQLX", "SJHZ", "PBJXMC", "BZ" };

                    setFieldFlag(rjStrings, 7L, true);
                } else if ((apptype != null) && (apptype.equals("YJTZ"))) {
                    String[] yjStrings = { "EQCODE", "DESCRIPTION", "PBJXMC", "XH", "SL", "YHXM", "YZ", "CQDW", "JJZB", "JZNY", "GRDATE" };

                    setFieldFlag(yjStrings, 7L, true);
                }
            }

            if ((Tools.checkWfinstance(this)) && (!Tools.checkWfassignment(this)))
            {
                setFlag(7L, true);
                return;
            }
        }
        catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    public void add() throws MXException, RemoteException
    {
        super.add();
        UserInfo userInfo = getUserInfo();
        String userid = userInfo.getPersonId();
        MboRemote person = Tools.getLoginPerson(this, userid);
        if ((person != null) && (!person.isNull("persongroup")))
            setValue("persongroup", person.getString("persongroup"), 11L);
        setValue("cqdw", getString("site.description"), 11L);
        setValue("sydw", getString("site.udorgjc"), 11L);
    }
}