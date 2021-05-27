package guide.webclient.beans.com;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.rmi.RemoteException;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import psdi.mbo.MboRemote;
import psdi.mbo.MboSetRemote;
import psdi.server.MXServer;
import psdi.util.MXApplicationException;
import psdi.util.MXException;
import psdi.webclient.system.beans.DataBean;
import psdi.webclient.system.controller.AppInstance;
import psdi.webclient.system.controller.ControlInstance;
import psdi.webclient.system.controller.UploadFile;
import psdi.webclient.system.session.WebClientSession;

import static psdi.iface.util.SecurityUtil.getUserInfo;

public class UDExcelExecuteDataBean extends DataBean
{
    private int totalrows = 0;
    private int successrows = 0;

    public int execute() throws MXException, RemoteException {
        String label = this.clientSession.getCurrentEventHandler().getProperty("label");
        System.out.println("\n------------" + label + "----------------");

        UploadFile uploadfile = (UploadFile)this.app.get("importfile");
        String fullName = uploadfile.getFileName();
        String absoluteName = uploadfile.getAbsoluteFileName();
        excelValidate(fullName, absoluteName);
        try
        {
            String s = MXServer.getMXServer().getProperty("mxe.doclink.doctypes.defpath");
            System.out.println("\n------------" + s + "----------------");
            uploadfile.setDirectoryName(s.trim());
            uploadfile.writeToDisk();

            excelOpen(uploadfile.getAbsoluteFileName(), label);
        }
        catch (IOException e) {
            e.printStackTrace();
        }

        this.app.remove("importfile");
        this.app.getAppBean().save();
        String message = "导入完成，共计 " + this.totalrows + " 条，成功导入 " + this.successrows + " 条！";
        if (this.totalrows > this.successrows)
            message = message + "\n第 " + (this.successrows + 1) + " 条导入失败，请检查数据是否正确！";
        this.clientSession.showMessageBox(this.clientSession.getCurrentEvent(), "提示", message, 1);
        return 1;
    }

    private void excelValidate(String fullName, String absoluteName) throws MXApplicationException {
        boolean flag = (fullName != null) && (fullName.length() > 0);
        boolean flag1 = (absoluteName != null) && (absoluteName.length() > 0);
        if ((!flag) && (!flag1)) {
            Object[] params = { "导入的文件名不能为空！" };
            throw new MXApplicationException("instantmessaging", "tsdimexception", params);
        }
        if (fullName.equalsIgnoreCase("unknown")) {
            Object[] params = { "请选择导入文件！" };
            throw new MXApplicationException("instantmessaging", "tsdimexception", params);
        }
    }

    public void excelOpen(String fileName, String businessname) throws MXApplicationException {
        try {
            File xlsFile = new File(fileName);
            Workbook workbook = null;

            InputStream excel03 = new FileInputStream(xlsFile);
            String type = fileName.substring(fileName.lastIndexOf("."), fileName.length());
            if (type.equalsIgnoreCase(".xls"))
            {
                workbook = new HSSFWorkbook(excel03);
            }
            else workbook = new XSSFWorkbook(fileName);

            excelRead(workbook, businessname);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void excelRead(Workbook workbook, String businessname) throws Exception {
        String personid = app.getBeanForApp().getMbo().getUserInfo().getPersonId();
        System.out.println("-------excel005------" + personid);
        MboSetRemote excelimpSet = MXServer.getMXServer().getMboSet("UDEXCELIMP", MXServer.getMXServer().getUserInfo(personid));
        excelimpSet.setWhere("businessname = '" + businessname + "'");
        excelimpSet.setOrderBy("cellnum");
        if ((!excelimpSet.isEmpty()) && (excelimpSet.count() > 0)) {
            MboRemote excelimp = null;
            MboRemote addMbo = null;

            MboSetRemote addMboSet = MXServer.getMXServer().getMboSet(excelimpSet.getMbo(0).getString("objectname"), MXServer.getMXServer().getUserInfo(personid));
            addMboSet.setWhere("1=2");

            Sheet sheet = workbook.getSheetAt(0);
            int rows = sheet.getLastRowNum() + 1;
            this.totalrows = (rows - 1);
            this.successrows = 0;
            for (int i = 1; i < rows; i++) {
                addMbo = addMboSet.add();
                addMbo.setValue("linenum", i, 11L);
                for (int j = 0; (excelimp = excelimpSet.getMbo(j)) != null; j++) {
                    dataWrite(sheet.getRow(i).getCell(j), excelimp, addMbo);
                }
                addMboSet.save();
                this.successrows += 1;
            }
            addMboSet.close();
        }
        excelimpSet.close();
    }

    private void dataWrite(Cell cell, MboRemote excelimp, MboRemote addMbo) throws NumberFormatException, RemoteException, MXException
    {
        if (Integer.parseInt(excelimp.getString("cellnum")) > 199) {
            if ((excelimp.getString("defaultvalue") != null) && (!excelimp.getString("defaultvalue").equalsIgnoreCase(""))) {
                if (excelimp.getString("mboconstants").equalsIgnoreCase("2L"))
                    addMbo.setValue(excelimp.getString("attributename"), excelimp.getString("defaultvalue"), 2L);
                else
                    addMbo.setValue(excelimp.getString("attributename"), excelimp.getString("defaultvalue"), 11L);
            } else if ((excelimp.getString("fromattr") != null) && (!excelimp.getString("fromattr").equalsIgnoreCase(""))) {
                if (excelimp.getString("mboconstants").equalsIgnoreCase("2L"))
                    addMbo.setValue(excelimp.getString("attributename"), this.app.getAppBean().getMbo().getString(excelimp.getString("fromattr")), 2L);
                else
                    addMbo.setValue(excelimp.getString("attributename"), this.app.getAppBean().getMbo().getString(excelimp.getString("fromattr")), 11L);
            }
            System.out.println("\n------------MAX" + addMbo.getString(excelimp.getString("attributename")) + "----------------");
        } else {
            if (excelimp.getString("datatype").equalsIgnoreCase("STRING")) {
                if (excelimp.getString("mboconstants").equalsIgnoreCase("2L")){
                    addMbo.setValue(excelimp.getString("attributename"), getCellString(cell, 0), 2L);
                    }
                else
                    addMbo.setValue(excelimp.getString("attributename"), getCellString(cell, 0), 11L);
            } else if (excelimp.getString("datatype").equalsIgnoreCase("INTEGER")) {
                if (excelimp.getString("mboconstants").equalsIgnoreCase("2L"))
                    addMbo.setValue(excelimp.getString("attributename"), getCellNumber(cell, 0), 2L);
                else
                    addMbo.setValue(excelimp.getString("attributename"), getCellNumber(cell, 0), 11L);
            }
            System.out.println("\n------------XLS" + addMbo.getString(excelimp.getString("attributename")) + "----------------");
        }
    }

    private String getCellString(Cell cell, int celltype)
    {
        if (cell != null) {
            celltype = cell.getCellType();
        }
        switch (celltype) {
            case 1:
                return cell.getStringCellValue().trim();
            case 0:
                return String.valueOf((int)cell.getNumericCellValue());
        }
        return cell.getStringCellValue().trim();
    }

    private double getCellNumber(Cell cell, int celltype) {
        if (cell != null) {
            celltype = cell.getCellType();
        }
        switch (celltype) {
            case 1:
                return 0.0D;
            case 0:
                return cell.getNumericCellValue();
        }
        return 0.0D;
    }
}