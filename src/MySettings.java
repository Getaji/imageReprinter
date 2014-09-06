import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * javadoc here.
 *
 * @author Getaji
 */
public final class MySettings {

    private static final Pattern PATTERN_URL_FILE = Pattern.compile("^.+/(.+\\..+)$");

    private boolean useTemp;

    private File saveImageDir;

    private File configFile = new File("./ImageReprinter.cfg");
    private Settings settings = new Settings(configFile);

    public MySettings() throws IOException {
        boolean exists = configFile.exists();
        settings.setPutIfNull(true);

        load();

        if (!exists) {
            settings.save();
        }
    }

    public MySettings load() {
        try {
            settings.load();
        } catch (FileNotFoundException ignore) {}
        saveImageDir = new File(settings.get("file.dir_image", "./reprint-image"));
        useTemp = settings.getBoolean("boolean.use_temp", true);
        return this;
    }

    /**
     * 一時フォルダを使用するか指定します。
     * @return 一時フォルダを使用するか
     */
    public boolean isUseTemp() {
        return useTemp;
    }

    /**
     * 保存先のファイルを取得します。
     * {@link #isUseTemp()}がtrueなら一時ファイル、falseなら渡されたURLからファイル名を取得してベースフォルダに生成し、返します。
     *
     * @param url URL
     * @return 保存先ファイル
     * @throws java.io.IOException 失敗
     */
    public File getSaveTargetFile(String url) throws IOException {
        if (useTemp) {
            return File.createTempFile("ImageReprintTemp", ".jpg");
        } else {
            if (!saveImageDir.exists()) {
                saveImageDir.mkdir();
            }

            Matcher matcher = PATTERN_URL_FILE.matcher(url);
            String fileName;
            if (matcher.find()) {
                fileName = matcher.group(1);
            } else {
                throw new RuntimeException("ファイル名がない"); // 起こり得ない
            }
            File targetFile = new File(saveImageDir, fileName);
            if (!targetFile.exists()) {
                targetFile.createNewFile();
            }
            return targetFile;
        }
    }
}
