import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

/**
 * <p>設定管理クラスです。設定ファイルからキーと値を読み込み、保持します。
 *
 * <p>キーと値はイコール"="で区切られます。2つ目以降の"="はそのまま値として読み込まれます。
 * "="が0個の行は無視されます。
 * シャープ"#"で始まる行はコメントとして無視されます。
 *
 * @author Getaji
 */
public final class Settings {

    private final Map<String, String> map = new HashMap<>();
    private final File configFile;

    /**
     * mapからgetした値がnullの場合に、この値に基づく処理をしなければならない。
     */
    private boolean putIfNull = false;

    /**
     * 設定ファイルの{@link java.io.File}インスタンスを取り、Settingsインスタンスを構築します。
     * 設定ファイルが存在しない場合は作成します。ディレクトリの場合には{@link java.io.IOException}をスローします。
     *
     * @param configFile 設定ファイル
     * @throws IOException 渡された{@link java.io.File}インスタンスがディレクトリ
     */
    public Settings(File configFile) throws IOException {
        if (configFile.isDirectory()) {
            throw new IOException(configFile.getAbsolutePath() + " is directory.");
        }
        this.configFile = configFile;
        if (!configFile.exists()) {
            configFile.createNewFile();
        }
    }

    /**
     * 設定ファイルから値を読み込みます。保持している値は事前にすべて削除されます。
     *
     * @return return this
     * @throws FileNotFoundException 設定ファイルが存在しない
     */
    public synchronized Settings load() throws FileNotFoundException {
        // TODO インスタンス生成時に存在するかチェックしてるんだしスローせずに中で処理しろ感
        final Scanner scanner = new Scanner(configFile);
        map.clear();
        while (scanner.hasNext()) {
            String line = scanner.nextLine();
            if (!line.startsWith("#")) {
                String[] property = line.split(" *= *", 2);
                if (property.length < 2) {
                    continue;
                }
                map.put(property[0], property[1]);
            }
        }
        scanner.close();
        return this;
    }

    public String get(String key) {
        return map.get(key);
    }

    public String get(String key, String defaultValue) {
        String value = map.get(key);
        if (value == null) {
            if (putIfNull) {
                map.put(key, defaultValue);
            }
            return defaultValue;
        } else {
            return value;
        }
    }

    public boolean getBoolean(String key, boolean defaultValue) {
        String value = map.get(key);
        if (value == null)  {
            if (putIfNull) {
                map.put(key, String.valueOf(defaultValue));
            }
            return defaultValue;
        }

        value = value.toLowerCase();

        if (value.equals("true"))
            return true;
        else if (value.equals("false"))
            return false;
        else
            return defaultValue;
    }

    public boolean isPutIfNull() {
        return putIfNull;
    }

    public void setPutIfNull(boolean putIfNull) {
        this.putIfNull = putIfNull;
    }

    public synchronized void save() throws IOException {
        FileWriter fileWriter = new FileWriter(configFile);
        fileWriter.write("# imageReprinter configuration file\n\n");
        for (Map.Entry<String, String> entry : map.entrySet()) {
            fileWriter.write(String.format(
                    "%s = %s\n", entry.getKey(), entry.getValue()));
        }
        fileWriter.flush();
        fileWriter.close();
    }
}
