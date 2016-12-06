package com.smarttop.library.utils;

import android.content.Context;
import android.os.Environment;
import android.util.Log;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * 日志工具 1.添加日志输出选项.控制日志输出位置 2.添加文件日志功能.
 * 3.控制单个日志文件最大限制.由LOG_MAXSIZE常量控制,保留两个最新日志文件 4.文件日志输出目标
 *
 * @author smartTop
 * @data 2010-5-26
 */
public class LogUtil {

    public static final int TO_CONSOLE = 0x1;
    public static final int TO_SCREEN = 0x10;
    public static final int TO_FILE = 0x100;
    public static final int FROM_LOGCAT = 0x1000;

    public static final int DEBUG_ALL = TO_CONSOLE | TO_FILE; /*
                                                             * | FROM_LOGCAT |
															 * TO_SCREEN |
															 * TO_FILE |
															 * FROM_LOGCAT
															 */

    private static final String LOG_TEMP_FILE = "log.temp";
    private static final String LOG_LAST_FILE = "log_last.txt";
    private static final String LOG_NOW_FILE = "log_now.txt";

    private static final int LOG_MAXSIZE = 2 * 1024 * 1024; // double the size
    // temporarily

    public final static boolean isDebug = GlobalParams.isDebug;

    int LOG_LEVEL = Log.VERBOSE;

    private final Object lockObj = new Object();

    PaintLogThread mPaintLogThread = null;

    OutputStream mLogStream;

    long mFileSize;

    Context mContext;

    private final static LogUtil mLog = new LogUtil();
    private static final String DEFAULT_FILE_DIR = "com.yz.faith";

    private LogUtil() {
    }

    public static LogUtil getInstance() {
        return mLog;
    }

    /**
     * 设置context
     *
     * @param context
     */
    public void setContext(Context context) {
        mContext = context;
    }

    public static void d(String tag, String msg) {
        getInstance().log(tag, msg, DEBUG_ALL, Log.DEBUG);
    }

    public static void v(String tag, String msg) {
        getInstance().log(tag, msg, DEBUG_ALL, Log.VERBOSE);
    }

    public static void e(String tag, String msg) {
        getInstance().log(tag, msg, DEBUG_ALL, Log.ERROR);
    }

    public static void i(String tag, String msg) {
        getInstance().log(tag, msg, DEBUG_ALL, Log.INFO);
    }

    public static void w(String tag, String msg) {
        getInstance().log(tag, msg, DEBUG_ALL, Log.WARN);
    }

    protected void log(String tag, String msg, int outdest, int level) {
        if (!isDebug) {
            return;
        }

        if (tag == null)
            tag = "TAG_NULL";
        if (msg == null)
            msg = "MSG_NULL";

        if (level >= LOG_LEVEL) {

            if ((outdest & TO_CONSOLE) != 0) {
                LogToConsole(tag, msg, level);
            }

            if ((outdest & TO_SCREEN) != 0) {
                LogToScreen(tag, msg, level);
            }

            if ((outdest & TO_FILE) != 0) {
                LogToFile(tag, msg, level);
            }

            if ((outdest & FROM_LOGCAT) != 0) {

                if (mPaintLogThread == null) {
                    mPaintLogThread = new PaintLogThread();
                    mPaintLogThread.start();
                }
            }
        }

    }

    Calendar mDate = Calendar.getInstance();
    StringBuffer mBuffer = new StringBuffer();

    /**
     * 组成Log字符串.添加时间信息.
     *
     * @param tag
     * @param msg
     * @return
     */
    private String getLogStr(String tag, String msg) {

        mDate.setTimeInMillis(System.currentTimeMillis());

        mBuffer.setLength(0);
        mBuffer.append("[");
        mBuffer.append(tag);
        mBuffer.append(" : ");
        mBuffer.append(mDate.get(Calendar.MONTH) + 1);
        mBuffer.append("-");
        mBuffer.append(mDate.get(Calendar.DATE));
        mBuffer.append(" ");
        mBuffer.append(mDate.get(Calendar.HOUR_OF_DAY));
        mBuffer.append(":");
        mBuffer.append(mDate.get(Calendar.MINUTE));
        mBuffer.append(":");
        mBuffer.append(mDate.get(Calendar.SECOND));
        mBuffer.append(":");
        mBuffer.append(mDate.get(Calendar.MILLISECOND));
        mBuffer.append("] ");
        mBuffer.append(msg);

        return mBuffer.toString();
    }

    /**
     * 将log打到控制台
     *
     * @param tag
     * @param msg
     * @param level
     */
    private void LogToConsole(String tag, String msg, int level) {
        switch (level) {
            case Log.DEBUG:
                Log.d(tag, msg);
                break;
            case Log.ERROR:
                Log.e(tag, msg);
                break;
            case Log.INFO:
                Log.i(tag, msg);
                break;
            case Log.VERBOSE:
                Log.v(tag, msg);
                break;
            case Log.WARN:
                Log.w(tag, msg);
                break;
            default:
                break;
        }
    }

    /**
     * 将log打到文件日志
     *
     * @param tag
     * @param msg
     * @param level
     */
    private void LogToFile(String tag, String msg, int level) {
        synchronized (lockObj) {
            OutputStream outStream = openLogFileOutStream();
            if (outStream != null) {
                try {
                    byte[] d = getLogStr(tag, msg).getBytes("utf-8");
                    // byte[] d = msg.getBytes("utf-8");
                    if (mFileSize < LOG_MAXSIZE) {
                        outStream.write(d);
                        outStream.write("\r\n".getBytes());
                        outStream.flush();
                        mFileSize += d.length;
                    } else {
                        closeLogFileOutStream();
                        if (renameLogFile()) {
                            LogToFile(tag, msg, level);
                        }
                    }
                } catch (Exception e) {
                    // TODO: handle exception
                    e.printStackTrace();
                }

            }
        }
    }

    private void LogToScreen(String tag, String msg, int level) {

    }

    /**
     * back now log file
     */
    public void backLogFile() {
        if (mContext == null) {
            return;
        }
        File cacheFolder = getLogFolder();
        synchronized (lockObj) {
            try {
                closeLogFileOutStream();

                File destFile = new File(cacheFolder, LOG_NOW_FILE);
                if (destFile.exists()) {
                    destFile.delete();
                }

                try {
                    destFile.createNewFile();
                } catch (IOException e1) {
                    e1.printStackTrace();
                    return;
                }

                File srcfile1 = new File(cacheFolder, LOG_TEMP_FILE);
                File srcfile2 = new File(cacheFolder, LOG_LAST_FILE);
                copyFile(srcfile1, destFile, false);
                copyFile(srcfile2, destFile, true);

                openLogFileOutStream();

            } catch (IOException e) {
                // TODO: handle exception
                e.printStackTrace();
            }
        }
    }

    /**
     * 获取日志临时文件输入流
     *
     * @return
     */
    private OutputStream openLogFileOutStream() {
        if (mLogStream == null && mContext != null) {
            try {

                File file = new File(getLogFolder(), LOG_TEMP_FILE);
                if (file.exists()) {
                    mLogStream = new FileOutputStream(file, true);
                    mFileSize = file.length();
                } else {
                    // file.createNewFile();
                    mLogStream = new FileOutputStream(file);
                    mFileSize = 0;
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
        return mLogStream;
    }

    /**
     * 获得日志目录
     *
     * @return
     * @author houmiao.xiong
     */
    protected File getLogFolder() {

        File folder;

        if (Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED)) {
            File sdcard = Environment.getExternalStorageDirectory();
            folder = new File(sdcard, DEFAULT_FILE_DIR);
            if (!folder.exists()) {
                folder.mkdirs();
            }
        } else {
            folder = mContext.getFilesDir();
            if (!folder.exists()) {
                folder.mkdirs();
            }
        }

        return folder;
    }

    /**
     * 关闭日志输出流
     */
    private void closeLogFileOutStream() {
        try {
            if (mLogStream != null) {
                mLogStream.close();
                mLogStream = null;
                mFileSize = 0;
            }
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }
    }

    /**
     * 文件复制
     *
     * @param src
     * @param dest
     * @throws IOException
     */
    private void copyFile(File src, File dest, boolean destAppend)
            throws IOException {

        if (!destAppend && dest.exists()) {
            dest.delete();
        }
        long total = src.length();
        FileOutputStream out = new FileOutputStream(dest, destAppend);
        FileInputStream in = new FileInputStream(src);
        long count = 0;
        byte[] temp = new byte[1024 * 10];
        while (count < total) {
            int size = in.read(temp);
            out.write(temp, 0, size);
            count += size;
        }
        in.close();
        out.close();

    }

    /**
     * rename log file
     *
     * @return 重命名成功或失败
     */
    private boolean renameLogFile() {

        synchronized (lockObj) {

            File file = new File(getLogFolder(), LOG_TEMP_FILE);
            File destFile = new File(getLogFolder(), LOG_LAST_FILE);
            if (destFile.exists()) {
                destFile.delete();
            }
            file.renameTo(destFile);
            // 重命名失败. 原文件还存在
            // 清除原文件
            return !file.exists() || file.delete();
        }
    }

    public boolean zipLogFile(String zipFileName) {
        if (mContext == null) {
            return false;
        }

        backLogFile();
        File zipfile = new File(zipFileName);
        if (zipfile.exists()) {
            zipfile.delete();
        }
        File srcfile = new File(getLogFolder(), LOG_NOW_FILE);

        return zip(srcfile, zipfile);
    }

    public void close() {

        if (mPaintLogThread != null) {
            mPaintLogThread.shutdown();
            mPaintLogThread = null;
        }
        closeLogFileOutStream();

    }

    class PaintLogThread extends Thread {

        Process mProcess;
        boolean mStop = false;

        public void shutdown() {
            Log.i("PaintLogThread", "shutdown");
            mStop = true;
            if (mProcess != null) {
                mProcess.destroy();
                mProcess = null;
            }
        }

        public void run() {
            // TODO Auto-generated method stub
            try {
                Log.i("PaintLogThread", "shutdown");
                ArrayList<String> commandLine = new ArrayList<String>();
                commandLine.add("logcat");
                // commandLine.add( "-d");

                commandLine.add("-v");
                commandLine.add("time");

                // commandLine.add( "-s");
                // commandLine.add( "tag:W");
                // commandLine.add( "-f");
                // commandLine.add("/sdcard/log.txt");

                mProcess = Runtime.getRuntime().exec(
                        commandLine.toArray(new String[commandLine.size()]));

                BufferedReader bufferedReader = new BufferedReader
                        // ( new InputStreamReader(mProcess.getInputStream()), 1024);
                        (new InputStreamReader(mProcess.getInputStream()));

                String line;
                while (!mStop) {
                    line = bufferedReader.readLine();
                    if (line != null) {
                        LogToFile("SysLog", line, Log.VERBOSE);
                    } else {
                        if (line == null) {
                            Log.i("PaintLogThread:", "readLine==null");
                            break;
                            // Log.i("PaintLogThread:","PaintLogThread sleep
                            // 1000second"
                            // );
                            // Thread.sleep(1000);
                        }
                        // Thread.sleep(1000);

                    }
                }

                bufferedReader.close();
                if (mProcess != null)
                    mProcess.destroy();
                mProcess = null;
                mPaintLogThread = null;
                Log.i("PaintLogThread:", "end PaintLogThread:");

            } catch (Exception e) {
                e.printStackTrace();
                Log.d("NeteaseLog", "logcatToFile Exception:" + e.toString());
            }
        }
    }

    private boolean zip(File unZip, File zip) {
        if (!unZip.exists())
            return false;
        if (!zip.getParentFile().exists())
            zip.getParentFile().mkdir();

        try {
            FileInputStream in = new FileInputStream(unZip);
            FileOutputStream out = new FileOutputStream(zip);

            ZipOutputStream zipOut = new ZipOutputStream(out);

            // for buffer
            byte[] buf = new byte[1024];

            int readCnt;

            zipOut.putNextEntry(new ZipEntry(unZip.getName()));
            while ((readCnt = in.read(buf)) > 0) {
                zipOut.write(buf, 0, readCnt);
            }
            zipOut.closeEntry();

            in.close();
            zipOut.close();

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

        return true;
    }

}
