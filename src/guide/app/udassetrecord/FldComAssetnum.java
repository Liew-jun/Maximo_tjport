package guide.app.udassetrecord;

import java.rmi.RemoteException;

import psdi.mbo.MAXTableDomain;
import psdi.mbo.MboRemote;
import psdi.mbo.MboSetRemote;
import psdi.mbo.MboValue;
import psdi.util.MXException;

public class FldComAssetnum extends MAXTableDomain {
	
	public FldComAssetnum(MboValue mbv) throws RemoteException, MXException {
		super(mbv);
		String attrname = this.getMboValue().getName();
		this.setRelationship("ASSET", "siteid = :siteid and assetnum = :"+attrname+"");
		this.setKeyMap("ASSET", new String[] { attrname },
				new String[] { "assetnum" });
	}
	//UDASSETRUND
	
	public MboSetRemote getList() throws RemoteException, MXException{
		MboRemote mbo = getMboValue().getMbo();
		String sql = "siteid = '"+mbo.getString("siteid")+"'";
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
		
//		MboRemote mbo = getMboValue().getMbo();
//		mbo.setValueNull("description", 11L);
//		MboSetRemote relSet = mbo.getMboSet("关系");
//		if(!relSet.isEmpty() && relSet.count()>0)
//			mbo.setValue("description", relSet.getMbo(0).getString("description"), 11L);
		
	}

	
}