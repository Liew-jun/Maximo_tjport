package guide.app.asset;




import psdi.mbo.*;
import psdi.server.MXServer;
import psdi.util.MXApplicationException;
import psdi.util.MXException;

import java.rmi.RemoteException;

import static javax.swing.UIManager.getString;


public class Test extends MboValueAdapter
{
    public Test(MboValue mbv) {
        super(mbv);
    }


    @Override
    public void action() throws MXException, RemoteException {
        super.action();
        Mbo mbo = getMboValue().getMbo();
        MboSetRemote classSet = mbo.getMboSet("CLASSCHILD");

//        MboSetRemote class1Set = mbo.getMboSet("$CLASSCHILD", "CLASSCHILD", "1=1");
//        MboSetRemote class2Set = MXServer.getMXServer().getMboSet("CLASSCHILD", MXServer.getMXServer().getSystemUserInfo());
//        class2Set.setWhere("");
        if(!classSet.isEmpty() && classSet.count() > 0){
            mbo.setValue("udssfl", classSet.getMbo(0).getString("udssfl"), 11L);
//            mbo.setValue("udssfl", mbo.getString("CLASSCHILD.udssfl"), 11L);
        }
//        System.out.println("#########################"+mbo.getString("CLASSCHILD.udssfl"));
//        String classchild = getMboValue("CLASSCHILD").getString();
//        String classparent = getMboValue("CLASSPARENT").getString();
//        System.out.println(classchild);
//        System.out.println("#########################");
//        System.out.println(classparent);
    }
}