import java.awt.*;
import java.awt.event.ActionListener;

/**
 * タスクトレイにアイコンとかぶちこむ担当
 *
 * @author Getaji
 * @since 2014/09/05
 */
public final class TrayManager {

    // ================ INSTANCE ================
    private final TrayIcon trayIcon;

    private final PopupMenu popupMenu = new PopupMenu();

    public TrayManager(Image image, String toolTip) {
        trayIcon = new TrayIcon(image);

        trayIcon.setPopupMenu(popupMenu);
    }

    public TrayManager registerTray() {
        try {
            SystemTray.getSystemTray().add(trayIcon);
        } catch (AWTException e) {
            e.printStackTrace();
        }
        return this;
    }

    public TrayManager removeTray() {
        SystemTray.getSystemTray().remove(trayIcon);
        return this;
    }

    public TrayManager addMenuItem(MenuItem menuItem) {
        popupMenu.add(menuItem);
        return this;
    }

    public TrayManager addMenuItem(MenuItem menuItem, ActionListener action) {
        menuItem.addActionListener(action);
        popupMenu.add(menuItem);
        return this;
    }

    public TrayManager addMenuItem(MenuItem menuItem, ActionListener action, MenuShortcut shortcut) {
        menuItem.addActionListener(action);
        popupMenu.add(menuItem);
        menuItem.setShortcut(shortcut);
        return this;
    }

    public TrayManager notifyInfo(String caption, String text) {
        trayIcon.displayMessage(caption, text, TrayIcon.MessageType.INFO);
        return this;
    }
}
