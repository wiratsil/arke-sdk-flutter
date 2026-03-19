package com.arke.sdk.util.printer;
import android.annotation.SuppressLint;
import android.content.Context;
import android.os.RemoteException;
import com.arke.sdk.ArkeSdkDemoApplication;
import com.usdk.apiservice.aidl.printer.OnPrintListener;
import com.usdk.apiservice.aidl.printer.PrinterError;
import com.usdk.apiservice.aidl.printer.UPrinter;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import io.reactivex.Completable;

/**
 * Printer API.
 */

public class Printer {
    private static final int WIDTH = 372;
    /**
     * Printer object.
     */
    private UPrinter printer = ArkeSdkDemoApplication.getDeviceService().getPrinter();

    /**
     * Context.
     */
    private Context context = ArkeSdkDemoApplication.getContext();

    /**
     * Constructor.
     */

    /**
     * Get status.
     */
    public void getStatus() throws RemoteException {
        int ret = printer.getStatus();
        if (ret != PrinterError.SUCCESS) {
            throw new RemoteException(getErrorMessage(ret));
        }
    }

    /**
     * Set gray.
     */
    public void setPrnGray(int gray) throws RemoteException {
        printer.setPrnGray(gray);
    }

    /**
     * Print text.
     */
    public void addText(int align, String text) throws RemoteException {
        printer.addText(align, text);
    }

    /**
     * Print barcode.
     */
    public void addBarCode(int align, int codeWith, int codeHeight, String barcode) throws RemoteException {
        printer.addBarCode(align, codeWith, codeHeight, barcode);
    }

    /**
     * Print QR code.
     */
    public void addQrCode(int align, int imageHeight, int ecLevel, String qrCode) throws RemoteException {
        printer.addQrCode(align, imageHeight, ecLevel, qrCode);
    }

    /**
     * Print image.
     */
    public void addImage(int align, byte[] imageData) throws RemoteException {
        printer.addImage(align, imageData);
    }

    /**
     * Feed line.
     */
    public void feedLine(int line) throws RemoteException {
        printer.feedLine(line);
    }

    /**
     * Feed pix.
     */
    public void feedPix(int pix) throws RemoteException {
        printer.feedPix(pix);
    }

    /**
     * Print BMP image.
     */
    public void addBmpImage(int offset, int factor, byte[] imageData) throws RemoteException {
        printer.addBmpImage(offset, factor, imageData);
    }

    /**
     * Print BMP image by path.
     */
    public void addBmpPath(int offset, int factor, String bmpPath) throws RemoteException {
        printer.addBmpPath(offset, factor, bmpPath);
    }

    /**
     * Start print.
     */
    public void start(OnPrintListener onPrintListener) throws RemoteException {
        printer.startPrint(onPrintListener);
    }

    /**
     * Set ASC size.
     */
    public void setAscSize(int ascSize) throws RemoteException {
        printer.setAscSize(ascSize);
    }

    /**
     * Set ASC scale.
     */
    public void setAscScale(int ascScale) throws RemoteException {
        printer.setAscScale(ascScale);
    }

    /**
     * Set HZ size.
     */
    public void setHzSize(int hzSize) throws RemoteException {
        printer.setHzSize(hzSize);
    }

    /**
     * Set HZ scale.
     */
    public void setHzScale(int hzScale) throws RemoteException {
        printer.setHzScale(hzScale);
    }

    /**
     * Set X space.
     */
    public void setXSpace(int xSpace) throws RemoteException {
        printer.setXSpace(xSpace);
    }

    /**
     * Set Y space.
     */
    public void setYSpace(int ySpace) throws RemoteException {
        printer.setYSpace(ySpace);
    }

    /**
     * Creator.
     */
    private static class Creator {
        private static final Printer INSTANCE = new Printer();
    }

    /**
     * Get printer instance.
     */
    public static Printer getInstance() {
        return Creator.INSTANCE;
    }

    /**
     * Error code.
     */
    private static Map<Integer, Integer> errorCodes;

    static {
        errorCodes = new Hashtable<>();
        errorCodes.put(PrinterError.SUCCESS, 0); // Placeholder, not used for string lookup
        errorCodes.put(PrinterError.SERVICE_CRASH, 1);
        errorCodes.put(PrinterError.REQUEST_EXCEPTION, 2);
        errorCodes.put(PrinterError.ERROR_PAPERENDED, 3);
        errorCodes.put(PrinterError.ERROR_HARDERR, 4);
        errorCodes.put(PrinterError.ERROR_OVERHEAT, 5);
        errorCodes.put(PrinterError.ERROR_BUFOVERFLOW, 6);
        errorCodes.put(PrinterError.ERROR_LOWVOL, 7);
        errorCodes.put(PrinterError.ERROR_PAPERENDING, 8);
        errorCodes.put(PrinterError.ERROR_MOTORERR, 9);
        errorCodes.put(PrinterError.ERROR_PENOFOUND, 10);
        errorCodes.put(PrinterError.ERROR_PAPERJAM, 11);
        errorCodes.put(PrinterError.ERROR_NOBM, 12);
        errorCodes.put(PrinterError.ERROR_BUSY, 13);
        errorCodes.put(PrinterError.ERROR_BMBLACK, 14);
        errorCodes.put(PrinterError.ERROR_WORKON, 15);
        errorCodes.put(PrinterError.ERROR_LIFTHEAD, 16);
        errorCodes.put(PrinterError.ERROR_CUTPOSITIONERR, 17);
        errorCodes.put(PrinterError.ERROR_LOWTEMP, 18);
    }

    /**
     * Get error message.
     */
    public static String getErrorMessage(int errorCode) {
        switch (errorCode) {
            case PrinterError.SUCCESS: return "Succeed";
            case PrinterError.SERVICE_CRASH: return "Service Crash";
            case PrinterError.REQUEST_EXCEPTION: return "Request Exception";
            case PrinterError.ERROR_PAPERENDED: return "Out of paper";
            case PrinterError.ERROR_HARDERR: return "Hardware error";
            case PrinterError.ERROR_OVERHEAT: return "Overheat";
            case PrinterError.ERROR_BUFOVERFLOW: return "Buffer overflow";
            case PrinterError.ERROR_LOWVOL: return "Low VOL protection";
            case PrinterError.ERROR_PAPERENDING: return "Out of paper soon";
            case PrinterError.ERROR_MOTORERR: return "Printer engine error";
            case PrinterError.ERROR_PENOFOUND: return "Failed to locate automatically";
            case PrinterError.ERROR_PAPERJAM: return "Paper jam";
            case PrinterError.ERROR_NOBM: return "Black mark not found";
            case PrinterError.ERROR_BUSY: return "Printer is busy";
            case PrinterError.ERROR_BMBLACK: return "Black signal";
            case PrinterError.ERROR_WORKON: return "Printer is powered on";
            case PrinterError.ERROR_LIFTHEAD: return "Printer head is lifted";
            case PrinterError.ERROR_CUTPOSITIONERR: return "Cutter not in position";
            case PrinterError.ERROR_LOWTEMP: return "Low temperature";
            default: return "Unknown Error (" + errorCode + ")";
        }
    }

    @SuppressLint("WrongConstant")
    public void addImages(List<byte[]> images) throws RemoteException {
        Printer.getInstance().setYSpace(0);
        Iterator var2 = images.iterator();

        while(var2.hasNext()) {
            byte[] image = (byte[])var2.next();
            Format format = new Format();
            format.setAlign(1);
            this.addImage(format, image);
        }
    }

    /**
     * Add image.
     * <p>
     * addImage into printer in order
     *
     * @throws RemoteException exception
     */
    public void addImage(byte[] image) throws RemoteException {
        Format format = new Format();
        format.setAlign(Format.ALIGN_LEFT);
        addImage(format, image);
    }

    /**
     * Add image.
     */
    public void addImage(Format format, byte[] image) throws RemoteException {
        printer.addImage(format.getAlign(), image);
    }

    /**
     * Init web view.
     * <p>
     * Just invoke once when application start
     *
     * @param context context
     */
    public static void initWebView(Context context) {
        com.printerutils.PrinterUtils.initWebView(context,WIDTH);
    }

    /**
     * Print.
     *
     * @return Single allows getting print results using Rx .
     */
    public Completable print() {
        return Completable.create(e -> printer.startPrint(new com.usdk.apiservice.aidl.printer.OnPrintListener.Stub() {
            @Override
            public void onFinish() throws RemoteException {
                e.onComplete();
            }

            @Override
            public void onError(int errorCode) throws RemoteException {
                e.onError(new Exception("nymph_printer_print_error"));
            }
        }));
    }
}
