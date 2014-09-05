import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * クリップボードからURL撮って保存してクリップボードにコピーする担当
 */
public final class ImageReprinter {

    public static final String NULL_STR = "NULL";

    private final Toolkit kit = Toolkit.getDefaultToolkit();
    private final Clipboard clip = kit.getSystemClipboard();

    public ImageReprinter() {
    }

    private String getClipboardText() {
        try {
            return (String) clip.getData(DataFlavor.stringFlavor);
        } catch (UnsupportedFlavorException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return NULL_STR;
    }

    private File copyImageToLocal(String imageStrUrl) throws IOException {
        HttpURLConnection con = null;

        // https://pbs.twimg.com/media/BnXPzvmCEAAGHsj.png
        // URLの作成
        URL url = new URL(imageStrUrl);

        // 接続用HttpURLConnectionオブジェクト作成
        con = (HttpURLConnection) url.openConnection();
        // リクエストメソッドの設定
        con.setRequestMethod("GET");
        // リダイレクトを自動で許可しない設定
        con.setInstanceFollowRedirects(false);
        // ヘッダーの設定(複数設定可能)
        con.setRequestProperty("Accept-Language", "jp");

        // 接続
        con.connect();

        File tempFile = File.createTempFile("ImageReprintTemp", ".jpg");
        try (InputStream is = new BufferedInputStream(con.getInputStream()); OutputStream os = new BufferedOutputStream(new FileOutputStream(tempFile))) {

            byte[] fbytes = new byte[1024];

            int size = -1;
            while ((size = is.read(fbytes)) >= 0) {
                os.write(fbytes, 0, size);
            }
        }
        return tempFile;
    }

    public boolean copy() {
        try {
            File file = copyImageToLocal(getClipboardText());

            StringSelection ss = new StringSelection(file.getAbsolutePath());
            clip.setContents(ss, ss);

            return true;
        } catch (IOException e) {
            e.printStackTrace();

            return false;
        }
    }
}