import 'package:flutter/material.dart';
import 'dart:async';

import 'package:flutter/services.dart';
import 'package:flutter_duoyou/flutter_duoyou.dart' as FlutterDuoyou;

void main() {
  runApp(MyApp());
}

///
/// 使用前先看下文档
/// http://docs.aiduoyou.com/web/#/5?page_id=79
///
/// 开发环境
/// Flutter 1.17.5 • channel stable • https://github.com/flutter/flutter.git
/// Framework • revision 8af6b2f038 (4 weeks ago) • 2020-06-30 12:53:55 -0700
/// Engine • revision ee76268252
/// Tools • Dart 2.8.4
///
class MyApp extends StatefulWidget {
  @override
  _MyAppState createState() => _MyAppState();
}

class _MyAppState extends State<MyApp> {
  String _platformVersion = 'Unknown';
  String _sdkVersion = 'Unknown';

  @override
  void initState() {
    super.initState();
    initPlatformState();
  }

  // Platform messages are asynchronous, so we initialize in an async method.
  Future<void> initPlatformState() async {
    // 初始化多游SDK
    bool initResult = await FlutterDuoyou.init(
      appId: "dy_59631652",
      appSecret: "ff9d2a4de0ec07e9dd4707bace3afbe7",
    );

    print("init====>$initResult");

    /// 初始化鱼玩开始
    var initResult2 = await FlutterDuoyou.initYuWan(
      appId: "1414",
      appSecret: "i0x39fgexugkzddd8l45e637hd71qruu",
      mediaUserId: "0",
      oaid: "1234566", //设备唯一标识 获取安卓oaid
    );

    print(">>>>>>>>>>$initResult2");

    /// 初始化鱼玩结束

    String platformVersion;
    String sdkVersion;
    // Platform messages may fail, so we use a try/catch PlatformException.
    try {
      platformVersion = await FlutterDuoyou.platformVersion;
      sdkVersion = await FlutterDuoyou.getSdkVersion;
    } on PlatformException {
      platformVersion = 'Failed to get platform version.';
      sdkVersion = 'Failed to get sdk version.';
    }

    // If the widget was removed from the tree while the asynchronous platform
    // message was in flight, we want to discard the reply rather than calling
    // setState to update our non-existent appearance.
    if (!mounted) return;

    setState(() {
      _platformVersion = platformVersion;
      _sdkVersion = sdkVersion;
    });
  }

  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      home: Scaffold(
        appBar: AppBar(
          title: const Text('Plugin example app'),
        ),
        body: ListView(
          padding: EdgeInsets.all(10),
          shrinkWrap: true,
          children: <Widget>[
            Center(
              child: Text(
                'Running on: $_platformVersion，多游SDK Version:$_sdkVersion',
                style: TextStyle(fontSize: 11),
              ),
            ),
            RaisedButton(
              child: Text("进入列表页"),
              color: Colors.blue,
              textColor: Colors.white,
              onPressed: () async {
                await FlutterDuoyou.jumpAdList(userId: "1", advertType: 0);
              },
            ),
            RaisedButton(
              child: Text("进入单个游戏"),
              color: Colors.blue,
              textColor: Colors.white,
              onPressed: () async {
                await FlutterDuoyou.jumpAdDetail(
                  userId: "1",
                  advertId: "1500012846",
                );
              },
            ),
            RaisedButton(
              child: Text("标题设为Hello"),
              color: Colors.blue,
              textColor: Colors.white,
              onPressed: () async {
                await FlutterDuoyou.setTitle("Hello");
              },
            ),
            RaisedButton(
              child: Text("进入我参与的"),
              color: Colors.blue,
              textColor: Colors.white,
              onPressed: () async {
                await FlutterDuoyou.jumpMine("1");
              },
            ),

            /// 对应文档第：https://doc.zhuoyixia.com/web/?#/4?page_id=539
            RaisedButton(
              child: Text("鱼玩列表"),
              color: Colors.blue,
              textColor: Colors.white,
              onPressed: () async {
                /// 1、对照上面的2文档，使用dio获取广告列表
                /// 2、从列表可以取到第一个参数
                /// 3、参数1对应列表的taskId
                /// 4、参数2  type==3 cpa  (type的值为 接口里的detailType)    type==1 cpl经典版详情页   type==2  cpl简介版详情页
                await FlutterDuoyou.jumpYuWanDetail(stageId: "1", type: 1);
              },
            ),

            /// 对应文档第9条：http://doc.yuwanbox.com/web/#/4?page_id=113
            RaisedButton(
              child: Text("鱼玩首页"),
              color: Colors.blue,
              textColor: Colors.white,
              onPressed: () async {
                await FlutterDuoyou.jumpYuWanHome();
              },
            ),
          ],
        ),
      ),
    );
  }
}
