

import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class ImageReprinter {
	public static void main(String[] args) throws Exception {
		Toolkit kit = Toolkit.getDefaultToolkit();
		Clipboard clip = kit.getSystemClipboard();

		String urlStr;
		urlStr = (String) clip.getData(DataFlavor.stringFlavor);

		HttpURLConnection con = null;

		// https://pbs.twimg.com/media/BnXPzvmCEAAGHsj.png
		// URLの作成
		URL url = new URL(urlStr);

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

		StringSelection ss = new StringSelection(tempFile.getAbsolutePath());
		clip.setContents(ss, ss);
	}
}
