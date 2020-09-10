import 'dart:async';

import 'package:flutter/material.dart';
import 'package:flutter/services.dart';

MethodChannel _channel = const MethodChannel('flutter_duoyou');

Future<String> get platformVersion async {
  final String version = await _channel.invokeMethod('getPlatformVersion');
  return version;
}

// 初始化
Future<bool> init({
  @required String appId,
  @required String appSecret,
}) async {
  return await _channel.invokeMethod("init", {
    "appId": appId,
    "appSecret": appSecret,
  });
}

// 初始化蘑菇
Future<bool> initMoGu({
  @required String appId,
  @required String appSecret,
}) async {
  return await _channel.invokeMethod("initMoGu", {
    "appId": appId,
    "appSecret": appSecret,
  });
}

// 初始化鱼玩
Future<bool> initYuWan({
  @required String appId,
  @required String appSecret,
  @required String mediaUserId,
  @required String oaid,
}) async {
  return await _channel.invokeMethod("initYuWan", {
    "appId": appId,
    "appSecret": appSecret,
    "mediaUserId": mediaUserId,
    "oaid": oaid,
  });
}

// 鱼玩首页
Future<bool> jumpYuWanHome() async {
  return await _channel.invokeMethod("jumpYuWanHome");
}

// 鱼玩详情
Future<bool> jumpYuWanDetail({
  @required String stageId,
  @required int type,
}) async {
  return await _channel.invokeMethod("jumpYuWanDetail", {
    "stageId": stageId,
    "type": type,
  });
}

// 启动蘑菇
Future<bool> startMoGuSDK({
  @required String userId,
  @required String oaid,
}) async {
  return await _channel.invokeMethod("startMoGuSDK", {
    "userId": userId,
    "oaid": oaid,
  });
}

// 进入列表页
Future<bool> jumpAdList({
  @required String userId,
  int advertType = 0,
}) async {
  return await _channel.invokeMethod("jumpAdList", {
    "userId": userId,
    "advertType": advertType,
  });
}

// 进入单个游戏（api+sdk模式，可以api获取列表，sdk进入详情页）
Future<bool> jumpAdDetail({
  @required String userId,
  @required String advertId,
}) async {
  return await _channel.invokeMethod("jumpAdDetail", {
    "userId": userId,
    "advertId": advertId,
  });
}

// 设置标题栏颜色  只有原生安卓支持，所以不支持flutter
//Future<bool> setTitleBarColor(int color) async {
//  return await _channel.invokeMethod("setTitleBarColor", {"color": color});
//}

// 设置标题
Future<bool> setTitle(String title) async {
  return await _channel.invokeMethod("setTitle", {"title": title});
}

// 进入我参与的
Future<bool> jumpMine(String userId) async {
  return await _channel.invokeMethod("jumpMine", {"userId": userId});
}

// 获取当前Fragment
Future<bool> getAdListFragment({
  @required String userId,
  int advertType = 0,
}) async {
  return await _channel.invokeMethod("getAdListFragment", {
    "userId": userId,
    "advertType": advertType,
  });
}

// 获取版本号
Future<String> get getSdkVersion async {
  final String version = await _channel.invokeMethod('getSdkVersion');
  return version;
}

// 设置OAID
Future<bool> setOAID(String oaid) async {
  return await _channel.invokeMethod("setOAID", {"oaid": oaid});
}
