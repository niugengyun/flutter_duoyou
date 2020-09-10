package com.easytbk.flutter_duoyou

import android.annotation.TargetApi
import android.app.Activity
import android.content.Context
import android.os.Build
import com.component.dly.xzzq_ywsdk.YwSDK
import com.component.dly.xzzq_ywsdk.YwSDK_WebActivity

import com.duoyou.ad.openapi.DyAdApi
import com.fendasz.moku.planet.MokuConfigured
import io.flutter.embedding.engine.plugins.FlutterPlugin
import io.flutter.embedding.engine.plugins.FlutterPlugin.FlutterPluginBinding
import io.flutter.embedding.engine.plugins.activity.ActivityAware
import io.flutter.embedding.engine.plugins.activity.ActivityPluginBinding
import io.flutter.plugin.common.MethodCall
import io.flutter.plugin.common.MethodChannel
import io.flutter.plugin.common.MethodChannel.MethodCallHandler
import io.flutter.plugin.common.PluginRegistry.Registrar

/**
 * FlutterDuoyouPlugin
 */
class FlutterDuoyouPlugin : FlutterPlugin, MethodCallHandler, ActivityAware {
    /// The MethodChannel that will the communication between Flutter and native Android
    ///
    /// This local reference serves to register the plugin with the Flutter Engine and unregister it
    /// when the Flutter Engine is detached from the Activity
    private var channel: MethodChannel? = null
    private var context: Context? = null
    private var activity: Activity? = null
    override fun onAttachedToEngine(flutterPluginBinding: FlutterPluginBinding) {
        channel = MethodChannel(flutterPluginBinding.flutterEngine.dartExecutor, "flutter_duoyou")
        channel!!.setMethodCallHandler(this)
        context = flutterPluginBinding.applicationContext
    }

    override fun onAttachedToActivity(binding: ActivityPluginBinding) {
        activity = binding.activity
    }

    override fun onDetachedFromActivityForConfigChanges() {}
    override fun onReattachedToActivityForConfigChanges(binding: ActivityPluginBinding) {}
    override fun onDetachedFromActivity() {}

    @TargetApi(Build.VERSION_CODES.GINGERBREAD)
    override fun onMethodCall(call: MethodCall, result: MethodChannel.Result) {
        if (call.method == "getPlatformVersion") {
            result.success("Android " + Build.VERSION.RELEASE)
        } else if (call.method == "init") { //初始化
            val appId = call.argument<String>("appId")
            val appSecret = call.argument<String>("appSecret")
            if (appId == null || appId.trim { it <= ' ' }.isEmpty()) {
                result.error("500", "appId can't be null", null)
            }
            if (appSecret == null || appSecret.trim { it <= ' ' }.isEmpty()) {
                result.error("500", "appSecret can't be null", null)
            }
            DyAdApi.getDyAdApi().init(appId, appSecret)
            result.success(true)
        } else if (call.method == "initMoGu") { //初始化 蘑菇星球
            val appId = call.argument<String>("appId")
            val appSecret = call.argument<String>("appSecret")
            if (appId == null || appId.trim { it <= ' ' }.isEmpty()) {
                result.error("500", "appId can't be null", null)
            }
            if (appSecret == null || appSecret.trim { it <= ' ' }.isEmpty()) {
                result.error("500", "appSecret can't be null", null)
            }
            MokuConfigured.initSDK(context, appId, appSecret)
            //initCustomStyle 1 2 3
            MokuConfigured.initCustomStyle("#ffffff", "#000000", true)
            result.success(true)
        } else if (call.method == "initYuWan") { //初始化 蘑菇星球
            var appId = call.argument<String>("appId")
            var appSecret = call.argument<String>("appSecret")
            var mediaUserId = call.argument<String>("mediaUserId")
            var oaid = call.argument<String>("oaid")
            if (appId == null || appId.trim { it <= ' ' }.isEmpty()) {
//                result.error("500", "appId can't be null", null);
                appId = "1414"
            }
            if (appSecret == null || appSecret.trim { it <= ' ' }.isEmpty()) {
//                result.error("500", "appSecret can't be null", null);
                appSecret = "i0x39fgexugkzddd8l45e637hd71qruu"
            }
            if (mediaUserId == null || mediaUserId.trim { it <= ' ' }.isEmpty()) {
                result.error("500", "mediaUserId can't be null", null)
                mediaUserId = "0"
            }
            if (oaid == null || oaid.trim { it <= ' ' }.isEmpty()) {
//                result.error("500", "mediaUserId can't be null", null);
                oaid = ""
            }
            val application = activity!!.application
            if (application == null) {
                result.error("500", "application can't be null", null)
                return
            }
            YwSDK.init(application, appSecret, appId, mediaUserId, "1", oaid)
            //  YwSDK.Companion.refreshMediaUserId(mediaUserId);
            //YwSDK.Companion.refreshAppSecret(appSecret,appId);
            result.success(true)
        } else if (call.method == "jumpYuWanDetail") { // 进入详情
            val stageId = call.argument<String>("stageId")
            val type = call.argument<Int>("type")!!
            if (stageId == null || stageId.trim { it <= ' ' }.isEmpty()) {
                result.error("500", "stageId can't be null", null)
                return
            }
            YwSDK_WebActivity.open(context!!, stageId, type)
            result.success(true)
        } else if (call.method == "jumpYuWanHome") { // 进入详情
            YwSDK_WebActivity.open(context!!)
            result.success(true)
        } else if (call.method == "jumpAdList") { // 进入列表页
            val userId = call.argument<String>("userId")
            val advertType = call.argument<Int>("advertType")!!
            if (userId == null || userId.trim { it <= ' ' }.isEmpty()) {
                result.error("500", "userId can't be null", null)
            }
            if (advertType < 0 || advertType > 2) {
                result.error("500", "advertType 只可以为 0 1 2", null)
            }
            DyAdApi.getDyAdApi().jumpAdList(activity, userId, advertType)
            result.success(true)
        } else if (call.method == "startMoGuSDK") { // 启动蘑菇星球
            val oaid = call.argument<String>("oaid")
            val userId = call.argument<String>("userId")
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                MokuConfigured.initOAID(oaid)
            }
            MokuConfigured.startSDK(activity, userId)
            result.success(true)
        } else if (call.method == "jumpAdDetail") { // 进入单个游戏（api+sdk模式，可以api获取列表，sdk进入详情页）
            val userId = call.argument<String>("userId")
            val advertId = call.argument<String>("advertId")
            if (userId == null || userId.trim { it <= ' ' }.isEmpty()) {
                result.error("500", "userId can't be null", null)
            }
            if (advertId == null || advertId.trim { it <= ' ' }.isEmpty()) {
                result.error("500", "advertId can't be null", null)
            }
            DyAdApi.getDyAdApi().jumpAdDetail(activity, userId, advertId)
            result.success(true)
        } else if (call.method == "setTitle") { // 设置标题
            val title = call.argument<String>("title")
            if (title == null || title.trim { it <= ' ' }.isEmpty()) {
                result.error("500", "title can't be null", null)
            }
            DyAdApi.getDyAdApi().setTitle(title)
            result.success(true)
        } /*else if (call.method.equals("setTitleBarColor")) { // 设置标题栏颜色
            // http://docs.aiduoyou.com/web/#/5?page_id=79
            // 这里只支持 res类型的颜色，是resInt类型，就是必须现在colors.xml里面定义了，在传入  所以flutter不可以用

            int color = call.argument("color");
            DyAdApi.getDyAdApi().setTitleBarColor(Color.parseColor("#333333"));
            result.success(true);
        } */ else if (call.method == "jumpMine") { // 进入我参与的
            val userId = call.argument<String>("userId")
            if (userId == null || userId.trim { it <= ' ' }.isEmpty()) {
                result.error("500", "userId can't be null", null)
            }
            DyAdApi.getDyAdApi().jumpMine(context, userId)
            result.success(true)
        } else if (call.method == "getAdListFragment") { // 获取当前Fragment
            val userId = call.argument<String>("userId")
            val advertType = call.argument<Int>("advertType")!!
            if (userId == null || userId.trim { it <= ' ' }.isEmpty()) {
                result.error("500", "userId can't be null", null)
            }
            if (advertType < 0 || advertType > 2) {
                result.error("500", "advertType 只可以为 0 1 2", null)
            }
            DyAdApi.getDyAdApi().getAdListFragment(userId, advertType)
            result.success(true)
        } else if (call.method == "getSdkVersion") { // 获取版本号
            val version = DyAdApi.getDyAdApi().sdkVersion
            result.success(version)
        } else if (call.method == "setOAID") { // 设置oaid
            val oaid = call.argument<String>("oaid")
            if (oaid == null || oaid.trim { it <= ' ' }.isEmpty()) {
                result.error("500", "oaid can't be null", null)
            }
            DyAdApi.getDyAdApi().setOAID(context, oaid)
            result.success(true)
        } else {
            result.notImplemented()
        }
    }

    override fun onDetachedFromEngine(binding: FlutterPluginBinding) {
        channel!!.setMethodCallHandler(null)
    }

    companion object {
        // This static function is optional and equivalent to onAttachedToEngine. It supports the old
        // pre-Flutter-1.12 Android projects. You are encouraged to continue supporting
        // plugin registration via this function while apps migrate to use the new Android APIs
        // post-flutter-1.12 via https://flutter.dev/go/android-project-migration.
        //
        // It is encouraged to share logic between onAttachedToEngine and registerWith to keep
        // them functionally equivalent. Only one of onAttachedToEngine or registerWith will be called
        // depending on the user's project. onAttachedToEngine or registerWith must both be defined
        // in the same class.
        fun registerWith(registrar: Registrar) {
            val channel = MethodChannel(registrar.messenger(), "flutter_duoyou")
            channel.setMethodCallHandler(FlutterDuoyouPlugin())
        }
    }
}
