package guide.app.udsbtzyb;

import java.text.SimpleDateFormat;
import java.util.Date;

import psdi.mbo.MboRemote;
import psdi.mbo.MboSetRemote;
import psdi.server.MXServer;
import psdi.server.SimpleCronTask;

/**
 *@function:��Ŀ��ͬ-�жϵ�ǰϵͳʱ����ʱ���������,������,����Ƿ����ֶ� ��
 *@date:2020-03-26 09:53:44
 *@modify:
 */
public class UDContractXMzbjCronTask extends SimpleCronTask {
	public final static String YYYYMMDD = "yyyyMMdd";
	
	Date sysdate;
	String strsysdate;
	Date udzbdqr;
	String strudzbdqr;
	MboSetRemote purchviewSet = null;
	MboRemote purchview = null;
	
	public void cronAction() {
		try {
			sysdate = MXServer.getMXServer().getDate();
			strsysdate= dateToStringYYYYMMDD(sysdate);
			purchviewSet = MXServer.getMXServer().getMboSet("PURCHVIEW",this.getRunasUserInfo());
			purchviewSet.setWhere(" lb='��Ŀ��ͬ' ");
			purchviewSet.reset();
			if (!purchviewSet.isEmpty() && purchviewSet.count() > 0) {
				for (int i = 0; i < purchviewSet.count(); i++) {
					purchview = purchviewSet.getMbo(i);
					
					if (purchview.getString("udzbdqr") != null && !purchview.getString("udzbdqr").equalsIgnoreCase("")) {
						udzbdqr = purchview.getDate("udzbdqr");
					}else {
						continue;
					}
					
					strudzbdqr = dateToStringYYYYMMDD(udzbdqr);
					if (strudzbdqr.equalsIgnoreCase(strsysdate)) {
						purchview.setValue("udsfdq", "1", 11L);
					}
				}
			}
			purchviewSet.save();
			purchviewSet.close();
			
		} catch (Exception e) {
			// TODO: handle exception
		}
	}
	
	public static String dateToStringYYYYMMDD(Date date) {
		if (date != null) {
			SimpleDateFormat sdf = new SimpleDateFormat(YYYYMMDD);
			String time = sdf.format(date);
			return time;
		} else {
			return "";
		}
	}

}
