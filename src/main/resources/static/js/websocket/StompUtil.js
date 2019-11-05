/**StompUtil 工具类*/
function StompUtil(options) {
    /***协议头*/
    var protocol="ws:/";
    /***本地地址*/
    var host=window.location.host;
    /**获取当前URL路径*/
    var pathName=document.location.pathname;
    /**获取上线文路径*/
    var contextPath=pathName.substring(0,pathName.indexOf("/",1));
    /**端点名称*/
    var endPoint="websocket";
    /**连接方式*/
    var connectType="ws";
    /**webSocket对象*/
    var webSocket;
    /**stompClient客户端*/
    var stompClient;
    /**是否调试模式*/
    var debug = false;
    /**重连次数*/
    var reconnectCount = 0;
    /**心跳时间毫秒*/
    var heartBeatTime = 5000;
    /**回调map缓存*/
    var subsCallBackMap = {};
    /**用户名*/
    var userName;
    /**头部信息*/
    var headers;
    //初始化链接
    init(options);

    //初始化链接
    function init(options) {
        if (!options) {
            return;
        }
        if (options.protocol) {
            protocol = options.protocol;
        }
        if (options.host) {
            host = options.host;
        }
        if (options.contextPath) {
            contextPath = options.contextPath;
        }
        if (options.endPoint) {
            endPoint = options.endPoint;
        }
        if (options.connectType) {
            connectType = options.connectType;
        }
        if (options.debug) {
            debug = options.debug;
        }
        if (options.heartBeatTime) {
            heartBeatTime = options.heartBeatTime;
        }
        if (options.maxReconnectCount) {
            reconnectCount = options.maxReconnectCount;
        }

        if(options.headers){
            headers=options.headers;
        }else{
            initHeaders();
        }
    }
    /**准备方法*/
    this.prepare=function () {
        if(connectType==="ws"){
            webSocket=new WebSocket(protocol+host+contextPath+endPoint);
        }else {
            webSocket=new SockJS(contextPath+endPoint);
        }
        stompClient=Stomp.over(webSocket);
        stompClient.heartbeat.outgoing=heartBeatTime;
        stompClient.heartbeat.incoming=heartBeatTime;
    }
    /**连接方法*/
    this.connect=function (successCallBack,failureCallBack) {
        doConnect(successCallBack, failureCallBack, this);
    }
    /***建立连接*/
    function doConnect(successCallback,failureCallBack, _this) {
        /***如果已经创建则无需创建websocket连接*/
        if (stompClient && stompClient.connected) {
            return;
        }
        /**准备方法*/
        _this.prepare();
        /**调用stomp连接*/
        stompClient.connect(headers,function (frame) {
            var tempSubscribeCallBackMap=subsCallBackMap;
            subsCallBackMap={};
            for(var destination in tempSubscribeCallBackMap){
                doSubscribe(destination,tempSubscribeCallBackMap[destination]);
            }
            userName=frame.headers["user-name"];
            if(successCallback){
                successCallback(frame);
            }
            if (!debug) {
                stompClient.debug = function (message) {

                };
            }
        },function (frame) {
            userName=undefined;
            _this.connect(successCallback,failureCallBack);
            if(failureCallBack){
                failureCallBack(frame);
            }
        });
    }
    /**获取链接*/
    this.stompClient=function(){
        return stompClient;
    }
    /**监听*/
    this.subscribe=function (destination,callback) {
        /***判断是否已创建*/
        if (stompClient === undefined) {
            throw "websocket未连接，先调用connect(...)方法";
        }
        return doSubscribe(destination,callback);
    }
    //监听指定用户
    this.subscribeByUser = function (destination,callback) {
        if (stompClient === undefined) {
            subsCallBackMap[destination] = callback;
            throw "websocket未连接，先调用connect(...)方法";
        }
        var des = destination.substring(0, destination.indexOf("/", 2)) + "/" + userName + "/" + destination.substring(destination.indexOf("/", 2), destination.length);
        console.log("*************************"+des);
        return doSubscribe(des, callback);
    };
    /**创建订阅信息*/
    function doSubscribe(destination,callback) {
        /***判断是否已创建*/
        if (stompClient === undefined) {
            throw "websocket未连接，先调用connect(...)方法";
        }
        /**加入缓存*/
        subsCallBackMap[destination] = callback;
        /**订阅*/
        return stompClient.subscribe(destination,function (event) {
            try{
                var data=JSON.parse(event.body);
                callback(data);
            }catch (e) {
                console.dir("subscribe callback error"+e+",data="+event.body)
            }
        },headers);
    }
    /**发送消息*/
   this.send=function (destination,body){
        return stompClient.send(destination,headers,body);
    }
    /**断开连接*/
    this.disconnect=function (callback){
        if(stompClient&&stompClient.connected){
            return stompClient.disconnect(function (event) {
                userName=undefined;
                if(callback){
                    callback(event);
                }
            },headers);
        }
    }
    /**调试模式*/
    this.debug=function (isDebug) {
        debug=isDebug;
    }
    /**获取headers*/
    function initHeaders(){
        if (headers == undefined || headers === {}) {
            headers = {
                login: 'guest',
                passcode: 'guest',
                serverIncoming: heartBeatTime
            };
        }
    }
}