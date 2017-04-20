package com.metide.cnblogs.utils;

import android.text.TextUtils;
import android.util.ArrayMap;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 日志记录器
 *
 * Created by metide on 2017/3/13.
 *
 * 功能：
 *
 * 1、支持多种日志，默认已经实现了控制台日志输出
 * 2、可自定义扩展,只需实现ILog接口
 * 3、支持自定义输出格式，输出格式随心所欲
 * 4、支持格式化参数，string.format
 * 5、异步记录日志，更加高效
 * 6、支持自定义日志级别，只有高于此级别的日志才会输出
 * 7、自动获取类名作为tag
 * 8、可以定位到代码位置，精确到行数
 * 9、格式化输出json字符串，从此json结构和数据一目了然
 *
 */

public class Logger {

    /**
     *  日志组件集合
     */
    private static Map<String, AbsLog> sLogTempMap;

    /**
     * 获取调用日志的类名在堆栈中的索引
     *
     * 此处具有采用硬编码，如果修改代码中的log方法调用的位置，
     * 也需要更改此值
     *
     * TODO 20170315 考虑修改为自动获取该索引值
     *
     */
    private static final int CLASS_NAME_INDEX = 6;

    /* 以下日志级别同系统 android.util.Log */
    /**
     * Priority constant for the println method;
     */
    public static final int VERBOSE = 2;

    /**
     * Priority constant for the println method;
     */
    public static final int DEBUG = 3;

    /**
     * Priority constant for the println method;
     */
    public static final int INFO = 4;

    /**
     * Priority constant for the println method;
     */
    public static final int WARN = 5;

    /**
     * Priority constant for the println method;
     */
    public static final int ERROR = 6;

    /**
     * Priority constant for the println method.
     */
    public static final int ASSERT = 7;

    /* 以上日志级别同系统 android.util.Log */


    public static final String LOG_CAT = "LOGCAT";
    public static final String LOG_FILE = "FILE";
    public static final String LOG_CRASH = "CRASH";
    public static final String LOG_SQL = "SQL";

    /**
     * 默认时间格式
     */
    private static SimpleDateFormat sSimpleDateFormat=
            new SimpleDateFormat("yyyy-MM-dd HH:mm:ss SSS");

    /**
     * 日志组件集合中不包含指定名称的日志组件
     */
    private static final int NOT_CONTAINS_LOG = -1;

    /**
     * 默认的线程数
     */
    private static int DEFAULT_THREAD_POOL_SIZE = 4;

    /**
     * 日志线程池
     */
    private static ExecutorService sThreadPool = Executors.newFixedThreadPool(
            DEFAULT_THREAD_POOL_SIZE, Executors.defaultThreadFactory());


    static {
        sLogTempMap =
                Collections.synchronizedMap(new ArrayMap<String, AbsLog>());
        add(LOG_CAT, LogCat.getInstance);
    }


    /**
     * 向日志记录器中添加日志组件
     *
     * @param name      日志名称，如果存在相同的，则覆盖，否则加入
     * @param log       实现了AbsLog的日志组件
     */
    public synchronized static void add(String name, AbsLog log){
        if(TextUtils.isEmpty(name)){
            throw new IllegalArgumentException("日志名称不能为空");
        }
        sLogTempMap.put(name, log);
    }

    /**
     * 从日志记录器中删除指定名称的日志组件
     * @param name   日志名称
     */
    public static void remove(String name){
        if(!TextUtils.isEmpty(name) && sLogTempMap.containsKey(name)){
            sLogTempMap.remove(name);
        }
    }

    /**
     * 从日志记录器中删除所有的日志组件
     */
    public static void removeAll(){
        sLogTempMap.clear();
    }

    /**
     * 获取指定名称的日志组件
     * @param name  日志名称
     * @return  实现了AbsLog的日志组件
     */
    public static AbsLog getLog(String name){
        if(TextUtils.isEmpty(name)){
            throw new IllegalArgumentException("the log name could not empty");
        }

        if(!sLogTempMap.containsKey(name)){
            return null;
        }

        return sLogTempMap.get(name);
    }

    /**
     * 获取当前日志记录器中日志组件的数量
     * @return
     */
    public static int logSize(){
        return sLogTempMap.size();
    }

    /**
     * 获取指定日志组件的日志级别
     * @param name 日志名称
     * @return 日志级别
     */
    public static int getLevel(String name){
        if(TextUtils.isEmpty(name)){
            throw new IllegalArgumentException("the log name could not empty");
        }

        if(!sLogTempMap.containsKey(name)){
            return NOT_CONTAINS_LOG;
        }

        return sLogTempMap.get(name).getLevel();
    }

    /* verbose log*/
    public static void v(String message) {
        v(null,null, message);
    }
    public static void v(Throwable t) {
        v(null,t,null);
    }
    public static void v(String tag, String message) {
        v(tag, null, message);
    }
    public static void v(String tag, String message, Throwable t) {
        v(tag,t,message);
    }
    public static void v(String tag, Throwable t, String message, Object... args) {
        for(AbsLog log : sLogTempMap.values()){
            log.v(tag, t, message, args);
        }
    }

    /* debug log*/
    public static void d(String message) {
        d(null,null, message);
    }
    public static void d(Throwable t) {
        d(null,t,null);
    }
    public static void d(String tag, String message) {
        d(tag, null, message);
    }
    public static void d(String tag, String message, Throwable t) {
        d(tag,t,message);
    }
    public static void d(final String tag,final Throwable t,final String message,final Object... args) {
        for(AbsLog log : sLogTempMap.values()){
            log.d(tag, t, message, args);
        }
    }

    /* info log*/
    public static void i(String message) {
        i(null,null, message);
    }
    public static void i(Throwable t) {
        i(null,t,null);
    }
    public static void i(String tag, String message) {
        i(tag, null, message);
    }
    public static void i(String tag, String message, Throwable t) {
        i(tag,t,message);
    }
    public static void i(String tag, Throwable t, String message, Object... args) {
        for(AbsLog log : sLogTempMap.values()){
            log.i(tag, t, message, args);
        }
    }

    /* warn log*/
    public static void w(String message) {
        w(null,null, message);
    }
    public static void w(Throwable t) {
        w(null,t,null);
    }
    public static void w(String tag, String message) {
        w(tag, null, message);
    }
    public static void w(String tag, String message, Throwable t) {
        w(tag,t,message);
    }
    public static void w(String tag, Throwable t, String message, Object... args) {
        for(AbsLog log : sLogTempMap.values()){
            log.w(tag, t, message, args);
        }
    }

    /* error log*/
    public static void e(String message) {
        e(null,null, message);
    }
    public static void e(Throwable t) {
        e(null,t,null);
    }
    public static void e(String tag, String message) {
        e(tag, null, message);
    }
    public static void e(String tag, String message, Throwable t) {
        e(tag,t,message);
    }
    public static void e(String tag,Throwable t, String message, Object... args) {
        for(AbsLog log : sLogTempMap.values()){
            log.e(tag, t, message, args);
        }
    }

    /* assert log*/
    public static void wtf(String message) {
        wtf(null,null, message);
    }
    public static void wtf(Throwable t) {
        wtf(null,t,null);
    }
    public static void wtf(String tag, String message) {
        wtf(tag, null, message);
    }
    public static void wtf(String tag, String message, Throwable t) {
        wtf(tag,t,message);
    }
    public static void wtf(String tag, Throwable t, String message, Object... args) {
        for(AbsLog log : sLogTempMap.values()){
            log.wtf(tag, t, message, args);
        }
    }

    /**
     * 获取对应日志级别的字符串
     * @param level 日志级别
     * @return  相应字符串
     */
    public static String getLogLevel(int level){
        switch (level){
            case VERBOSE:
                return "VERBOSE";
            case DEBUG:
                return "DEBUG";
            case INFO:
                return "INFO";
            case WARN:
                return "WARN";
            case ERROR:
                return "ERROR";
            case ASSERT:
                return "ASSERT";
            default:
                return "UNKNOWN";
        }
    }

    /**
     * 获取指定堆栈索引的堆栈元素
     * @param index 堆栈索引
     * @return 堆栈元素
     */
    private static StackTraceElement getMethodStack(int index) {
        StackTraceElement[] methodStacks = (new Exception()).getStackTrace();
        return methodStacks[index];
    }

    /**
     * 格式化字符串
     * @param message 格式化前字符串
     * @param args  占位符替换值
     * @return 格式化后的字符串
     */
    private static String formatMessage(String message, Object[] args) {
        return String.format(message, args);
    }

    /**
     * 获取异常堆栈信息
     * @param t  异常
     * @return 异常堆栈信息
     */
    public static String getStackTraceString(Throwable t) {
        if(t != null){
            StringWriter sw = new StringWriter(256);
            PrintWriter pw = new PrintWriter(sw, false);
            t.printStackTrace(pw);
            pw.flush();
            return sw.toString();
        }
        return "";
    }

    /**
     * 日志记录接口
     * 所有日志组件必须都要实现的接口
     *
     * ILog过时了，请实现抽象日志类AbsLog
     */
    @Deprecated
    public interface ILog{

        /**
         * 记录日志
         * @param level     日志级别
         * @param tag       日志标签，如果没有手动指定，会自动获取到调用日志输出的类的类名
         * @param message   日志内容
         * @param t         异常
         */
        void log(int level, String tag, String message, Throwable t);
    }

    /**
     * 默认实现了ILog接口的控制台日志
     */
    public static class LogCat extends AbsLog{

        public static LogCat getInstance = new LogCat();

        private LogCat(){}

        @Override
        protected int getLevel() {
            return DEBUG;
        }


        @Override
        protected String getIFormat() {
            return  "[%c] [%t] [%l] [%m]";
        }

        @Override
        protected String getWFormat() {
            return  "[%c] [%t] [%l] [%m]";
        }

        @Override
        protected String getEFormat() {
            return  "[%c] [%t] [%l] [%m]\n%e";
        }

        @Override
        public void log(Date date, int level, String tag,String location, String message, Throwable t) {
            switch (level){
                case VERBOSE:
                    Log.v(tag,message,null);
                    break;
                case DEBUG:
                    Log.d(tag,message,null);
                    break;
                case INFO:
                    Log.i(tag,message,null);
                    break;
                case WARN:
                    Log.w(tag,message,null);
                    break;
                case ERROR:
                    Log.e(tag,message,null);
                    break;
                case ASSERT:
                    Log.wtf(tag,message,null);
                    break;
            }
        }
    }

    public static abstract class AbsLog{


        /**
         * 默认的内容输出格式
         *
         * %c   日志类型：VERBOSE/DEBUG/INFO/WARN/ERROR/ASSERT
         * %d   日期，格式见sSimpleDateFormat字段
         * %t   线程名称
         * %l   日志所有源码中的位置，包括类名.方法名和行数
         * %m   日志消息内容
         * %e   异常堆栈
         *
         */
        private static final String DEFAULT_FORMAT =
                "-----------------------------------------------" +
                        "----------------------------------------------------\n" +
                        "* [类型] %c\n" +
                        "* [时间] %d\n" +
                        "* [线程] %t\n" +
                        "* [位置] %l\n" +
                        "* [内容] %m\n" +
                        "* [异常] %e\n" ;


        /**
         * 日志输出级别
         *
         * @return 日志级别，只有高于或等于此级别的日志才会输出
         */
        protected abstract int getLevel();

        /**
         * VERBOSE 级别格式
         * @return 默认为空，也就是原样输出日志
         */
        protected String getVFormat(){ return ""; }

        /**
         * DEBUG 级别格式
         * @return 默认为空，也就是原样输出日志
         */
        protected String getDFormat(){ return ""; }

        /**
         * INFO 级别格式
         * @return 默认为空，也就是原样输出日志
         */
        protected String getIFormat(){ return ""; }

        /**
         * WARN 级别格式
         * @return 默认为空，也就是原样输出日志
         */
        protected String getWFormat(){ return ""; }

        /**
         * Error级别格式
         * @return 默认为空，也就是原样输出日志
         */
        protected String getEFormat(){ return ""; }

        /**
         * Assert级别格式
         * @return 默认为空，也就是原样输出日志
         */
        protected String getAFormat(){ return ""; }

        /**
         *  获取对应级别的内容格式
         * @param level 日志级别
         * @return 内容格式
         */
        private String getFormat(int level){
            switch (level){
                case VERBOSE:
                    return getVFormat();
                case DEBUG:
                    return getDFormat();
                case INFO:
                    return getIFormat();
                case WARN:
                    return getWFormat();
                case ERROR:
                    return getEFormat();
                case ASSERT:
                    return getAFormat();
                default:
                    return DEFAULT_FORMAT;
            }
        }

        void v(String tag, Throwable t, String message, Object... args) {
            prepareLog(VERBOSE,tag, t, message, args);
        }

        void d(String tag, Throwable t, String message, Object... args) {
            prepareLog(DEBUG,tag, t, message, args);
        }


        void i(String tag, Throwable t, String message, Object... args) {
            prepareLog(INFO,tag, t, message, args);
        }

        void w(String tag, Throwable t, String message, Object... args) {
            prepareLog(WARN, tag, t, message, args);
        }


        void e(String tag, Throwable t, String message, Object... args) {
            prepareLog(ERROR, tag, t, message, args);
        }


        void wtf(String tag, Throwable t, String message, Object... args) {
            prepareLog(ASSERT, tag, t, message, args);
        }

        private void prepareLog(final int logLevel, final String tag, final Throwable t,
                                final String message, final Object... args){

            //级别以下，不输出日志
            if(logLevel < getLevel()) return;

            final StackTraceElement methodStack = getMethodStack(CLASS_NAME_INDEX);
            final String className = methodStack.getClassName();

            sThreadPool.execute(new Runnable() {
                @Override
                public void run() {
                    try{
                        String newTag = tag == null || tag.equals("") ?
                                className.substring(className.lastIndexOf(".") + 1, className.length()) :
                                tag;
                        String newMessage = message;

                        if(!TextUtils.isEmpty(newMessage)){
                            if(args.length > 0){
                                newMessage = formatMessage(newMessage,args);
                            }

                            //只有debug级别才会转换json格式
                            if(logLevel == DEBUG){
                                if (newMessage.startsWith("{")) {
                                    JSONObject jsonObject = new JSONObject(newMessage);
                                    newMessage = jsonObject.toString(2);
                                    newMessage = "\n" + newMessage;
                                } else if (newMessage.startsWith("[")) {
                                    JSONArray jsonArray = new JSONArray(newMessage);
                                    newMessage = jsonArray.toString(2);
                                    newMessage = "\n" + newMessage;
                                }
                            }
                        }

                        String location = className + "." + methodStack.getMethodName()+"(" +
                                methodStack.getFileName() +":" + methodStack.getLineNumber() + ")";

                        String format = getFormat(logLevel);
                        if(TextUtils.isEmpty(format)){
                            format = DEFAULT_FORMAT;
                        }

                        Date curDate = new Date();
                        final String result = format
                                .replace("%c", getLogLevel(logLevel))
                                .replace("%d", sSimpleDateFormat.format(curDate))
                                .replace("%t", Thread.currentThread().getName())
                                .replace("%l", location)
                                .replace("%e", t == null ? "" : getStackTraceString(t))
                                .replace("%m", newMessage == null ? "" : newMessage);

                        log(curDate, logLevel, newTag, location, result, t);
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }
            });

        }

        /**
         * 日志记录核心方法
         *
         *
         * @param date 日志记录的时间
         * @param level 日志级别
         * @param tag 标签
         * @param location 记录日志的位置
         * @param message 内容
         * @param t 异常
         */
        public abstract void log(Date date,
                                 int level,
                                 String tag,
                                 String location,
                                 String message,
                                 Throwable t);
    }
}
