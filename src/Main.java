import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.MalformedURLException;

/**
 * メインクラス
 *
 * @author Getaji
 */
public class Main {

    private static MySettings mySettings;

    static {
        try {
            mySettings = new MySettings();
        } catch (IOException e) {
            System.exit(0);
        }
    }

    public static MySettings getMySettings() {
        return mySettings;
    }

    public static void main(String[] args) throws Exception {
        final ImageReprinter imageReprinter = new ImageReprinter();
        final Image image = new ImageIcon(ImageIO.read(
                Main.class.getResourceAsStream("camera.png")))
                .getImage();
        final TrayManager trayManager = new TrayManager(image, "ImageReprinter");

        trayManager.registerTray();
        trayManager.addMenuItem(new MenuItem("クリップボードの画像URLを保存して絶対パスをコピー"),
                new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        try {
                            imageReprinter.copy();
                            trayManager.notifyInfo("INFO", "コピーに成功しました。");
                        } catch (IOException exc) {
                            exc.printStackTrace();
                            if (exc instanceof MalformedURLException) {
                                trayManager.notifyError("ERROR", "URLが不正です。");
                            } else {
                                trayManager.notifyError("ERROR", "画像保存に失敗しました。");
                            }
                        }
                    }
                });
        trayManager.addMenuItem(new MenuItem("設定の再読み込み"),
                new ActionListener() {
                    @Override public void actionPerformed(ActionEvent e) {
                        mySettings.load();
                        trayManager.notifyInfo("INFO", "設定をリロードしました。");
                    }
                });
        trayManager.addMenuItem(new MenuItem("終了"),
                new ActionListener() {
                    @Override public void actionPerformed(ActionEvent e) {
                        System.exit(0);
                    }
                });
    }
}
