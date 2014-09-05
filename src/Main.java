import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * メインクラス
 *
 * @author Getaji
 */
public class Main {
    public static void main(String[] args) throws Exception {
        final ImageReprinter imageReprinter = new ImageReprinter();
        final TrayManager trayManager = new TrayManager(new ImageIcon(
                ImageIO.read(Main.class.getResourceAsStream("camera.png")))
                        .getImage(), "ImageReprinter"
        );

        trayManager.registerTray();
        trayManager.addMenuItem(new MenuItem("クリップボードの画像URLを保存して絶対パスをコピー"),
                new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        if (imageReprinter.copy()) {
                            trayManager.notifyInfo("Info", "コピーに成功しました");
                        }
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
