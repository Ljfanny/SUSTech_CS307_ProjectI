import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;

public class GenerateData {
    public static void main(String[] args) throws IOException {
        String newFileCreate = "extraData.txt";
        File file = new File(newFileCreate);
        if (!file.exists()) {
            file.createNewFile();
        }
        Random random = new Random();
        int count = 0;
        FileWriter fw = new FileWriter(file, true);
        while (count < 10) {
            StringBuilder stringBuilder = new StringBuilder();
            int year = 2010 + random.nextInt(10);
            int mouth = random.nextInt(11) + 1;
            int day = random.nextInt(27) + 1;
            String mou = String.valueOf(mouth);
            String dy = String.valueOf(day);
            if (mou.length() < 2) {
                mou = "0" + mou;
            }
            if (dy.length() < 2) {
                dy = "0" + dy;
            }
            String est = year + "-" + mou + "-" + dy;
            year = 2010 + random.nextInt(10);
            mouth = random.nextInt(11) + 1;
            day = random.nextInt(27) + 1;
            mou = String.valueOf(mouth);
            dy = String.valueOf(day);
            if (mou.length() < 2) {
                mou = "0" + mou;
            }
            if (dy.length() < 2) {
                dy = "0" + dy;
            }
            String log = year + "-" + mou + "-" + dy;
            int quantity = random.nextInt(1000) + 50;
            String[] salesmen = {
                "11612519", "12011919", "11311100", "11610329", "11112326", "11610803", "11110725",
                "12012220", "11112911", "12112727", "11810914", "11612521", "12110421", "11212200",
                "11411929", "11611508", "11911204", "12012309", "11210120", "12211614", "11310110",
                "11210712", "11410214", "11511513", "11710218", "11612913", "11310212", "11210713",
                "11210514", "11911227", "11512503", "11512212", "11412917", "11511608", "12111803",
                "11311011", "11410003", "11212120", "11512601", "11912318", "11512713", "11512206",
                "12112619", "11310425", "11812410", "11612727", "11712421", "11412302", "12112622",
                "11510400"
            };
            String[] models = {
                "TvBaseR1",
                "ElectronicDictionary67",
                "ExhaustFanD8",
                "MultifunctionalT4",
                "ServerBarebonesH4",
                "LaptopDockingStation94",
                "DotMatrixPrinterK1",
                "HumidifierY5",
                "PhysicalSecurityIsolation35",
                "HumidifierF6",
                "LargeFormatPrinterT5",
                "Cpu38",
                "AntivirusSoftwareP9",
                "BarcodeAndCardReadingEquipment27",
                "OpticalTransceiver40",
                "FaxMachineCopierD4",
                "RemoteControl83",
                "TvBase65",
                "LaminatorMoDriveH2",
                "Anti-SlipMobilePhone26",
                "ElectricToothbrushX5",
                "PrintMedia70",
                "CameraRemoteO4",
                "MonitorF1",
                "Laptop78",
                "MultiplexerL4",
                "LaptopPowerAdapterD3",
                "CleaningAndMaintenanceD0",
                "PhysicalSecurityIsolationN5",
                "LaserPrinterT5",
                "ElectricFan12",
                "PrinterY4",
                "Chassis83",
                "AirConditioner85",
                "Anti-SlipMobilePhoneP4",
                "KeyboardAndMouseSet74",
                "CameraRemoteK4",
                "Pager17",
                "PaperCutter64",
                "DesktopPcP3",
                "OnlineTelephone15",
                "ToiletSeat05",
                "MonocularC0",
                "TabletQ7",
                "HeatSink24",
                "Multiplexer89",
                "ServerMemoryA4",
                "AttendanceMachineW1",
                "RealEstateL1",
                "AntivirusAndEmailFiltering70"
            };
            String con = "CSE000" + String.valueOf(random.nextInt(4000) + 1000);
            stringBuilder.append(est).append(";");
//            stringBuilder.append(";");
            stringBuilder.append(log).append(";");
            stringBuilder.append(quantity).append(";");
            stringBuilder.append(salesmen[random.nextInt(50)]).append(";");
            stringBuilder.append(models[random.nextInt(50)]).append(";");
            stringBuilder.append(con).append(";").append("\n");
            fw.write(stringBuilder.toString());
            count++;
        }
        fw.close();
    }
}
